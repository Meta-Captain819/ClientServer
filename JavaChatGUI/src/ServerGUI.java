import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class ServerGUI extends Application {

    private TextArea messageArea;
    private TextField inputField;
    private ComboBox<String> clientSelector;
    private Button sendButton;

    private ServerSocket serverSocket;
    private ExecutorService pool = Executors.newCachedThreadPool();
    private Map<String, ClientHandler> clients = new ConcurrentHashMap<>();

    @Override
    public void start(Stage primaryStage) {
        messageArea = new TextArea();
        messageArea.setEditable(false);

        inputField = new TextField();
        inputField.setPromptText("Type your message...");

        clientSelector = new ComboBox<>();
        clientSelector.getItems().add("All");
        clientSelector.setValue("All");

        sendButton = new Button("Send");
        sendButton.setOnAction(e -> sendMessage());

        HBox inputBox = new HBox(10, new Label("To:"), clientSelector, inputField, sendButton);
        HBox.setHgrow(inputField, Priority.ALWAYS);

        VBox root = new VBox(10, messageArea, inputBox);
        root.setPrefSize(600, 400);

        primaryStage.setTitle("Server Chat");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

        new Thread(this::startServer).start();
    }

    private void startServer() {
        try {
            serverSocket = new ServerSocket(1234);
            Platform.runLater(() -> appendMessage("Server started. Waiting for clients..."));

            while (true) {
                Socket socket = serverSocket.accept();
                ClientHandler handler = new ClientHandler(socket);
                pool.execute(handler);
            }
        } catch (IOException e) {
            Platform.runLater(() -> appendMessage("Server error: " + e.getMessage()));
        }
    }

    private void sendMessage() {
        String target = clientSelector.getValue();
        String msg = inputField.getText();
        if (msg.isEmpty()) return;

        appendMessage("Server to " + target + ": " + msg);
        if (target.equals("All")) {
            clients.values().forEach(client -> client.send("Server (broadcast): " + msg));
        } else {
            ClientHandler client = clients.get(target);
            if (client != null) client.send("Server: " + msg);
        }
        inputField.clear();
    }

    private void appendMessage(String msg) {
        Platform.runLater(() -> messageArea.appendText(msg + "\n"));
    }

    @Override
    public void stop() throws Exception {
        if (serverSocket != null) serverSocket.close();
        pool.shutdown();
    }

    private class ClientHandler implements Runnable {
        private Socket socket;
        private BufferedReader in;
        private PrintWriter out;
        private String clientName;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                clientName = in.readLine(); // First message is the name
                clients.put(clientName, this);
                Platform.runLater(() -> {
                    clientSelector.getItems().add(clientName);
                    appendMessage(clientName + " connected.");
                });

                String msg;
                while ((msg = in.readLine()) != null) {
                    String received = clientName + ": " + msg;
                    Platform.runLater(() -> appendMessage(received));
                }
            } catch (IOException e) {
                Platform.runLater(() -> appendMessage("Connection with " + clientName + " lost."));
            } finally {
                try {
                    clients.remove(clientName);
                    Platform.runLater(() -> clientSelector.getItems().remove(clientName));
                    if (socket != null) socket.close();
                } catch (IOException ignored) {}
            }
        }

        public void send(String msg) {
            out.println(msg);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
