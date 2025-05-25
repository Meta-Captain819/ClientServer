import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.*;
import java.net.*;

public class ServerGUI extends Application {

    private TextArea messageArea;
    private TextField inputField;
    private Button sendButton;

    private ServerSocket serverSocket;
    private Socket clientSocket;
    private BufferedReader in;
    private PrintWriter out;

    @Override
    public void start(Stage primaryStage) {
        messageArea = new TextArea();
        messageArea.setEditable(false);
        messageArea.setWrapText(true);

        inputField = new TextField();
        inputField.setPromptText("Type your message...");
        sendButton = new Button("Send");

        sendButton.setOnAction(e -> sendMessage());

        HBox inputBox = new HBox(10, inputField, sendButton);
        VBox root = new VBox(10, messageArea, inputBox);
        root.setPrefSize(500, 400);

        primaryStage.setTitle("Server Chat");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

        // Run server in background thread to avoid freezing UI
        new Thread(this::startServer).start();
    }

    private void startServer() {
        try {
            serverSocket = new ServerSocket(1234);

            // Update UI on JavaFX thread
            Platform.runLater(() -> appendMessage("Server started. Waiting for client..."));

            clientSocket = serverSocket.accept();

            Platform.runLater(() -> appendMessage("Client connected: " + clientSocket.getInetAddress()));

            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream(), true);

            String msg;
            while ((msg = in.readLine()) != null) {
                String finalMsg = msg;
                Platform.runLater(() -> appendMessage("Client: " + finalMsg));
            }

        } catch (IOException e) {
            Platform.runLater(() -> appendMessage("Error: " + e.getMessage()));
        }
    }

    private void sendMessage() {
        String msg = inputField.getText();
        if (msg.isEmpty() || out == null) return;

        appendMessage("Server: " + msg);
        out.println(msg); // send to client
        inputField.clear();
    }

    private void appendMessage(String msg) {
        messageArea.appendText(msg + "\n");
    }

    @Override
    public void stop() throws Exception {
        if (clientSocket != null) clientSocket.close();
        if (serverSocket != null) serverSocket.close();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
