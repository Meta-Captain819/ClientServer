import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;

public class ClientGUI extends Application {

    private TextArea messageArea;
    private TextField inputField;
    private PrintWriter writer;
    private Socket socket;

    @Override
    public void start(Stage primaryStage) {
        // UI Setup
        primaryStage.setTitle("Client Chat");

        messageArea = new TextArea();
        messageArea.setEditable(false);
        messageArea.setWrapText(true);

        inputField = new TextField();
        inputField.setPromptText("Type your message...");
        inputField.setOnAction(e -> sendMessage());

        Button sendButton = new Button("Send");
        sendButton.setOnAction(e -> sendMessage());

        HBox inputBox = new HBox(10, inputField, sendButton);
        inputBox.setHgrow(inputField, Priority.ALWAYS);

        VBox root = new VBox(10, messageArea, inputBox);
        root.setPrefSize(400, 300);
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();

        // Connect to server
        connectToServer();
    }

    private void connectToServer() {
        try {
            socket = new Socket("192.168.1.3", 1234); // Server IP and Port
            writer = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Thread to read messages from the server
            new Thread(() -> {
                String line;
                try {
                    while ((line = reader.readLine()) != null) {
                        String msg = line;
                        javafx.application.Platform.runLater(() -> {
                            messageArea.appendText("Server: " + msg + "\n");
                        });
                    }
                } catch (IOException e) {
                    showError("Disconnected from server.");
                }
            }).start();

        } catch (IOException e) {
            showError("Could not connect to server.");
        }
    }

    private void sendMessage() {
        String message = inputField.getText();
        if (!message.isEmpty()) {
            writer.println(message);
            messageArea.appendText("You: " + message + "\n");
            inputField.clear();
        }
    }

    private void showError(String msg) {
        javafx.application.Platform.runLater(() -> {
            messageArea.appendText("[Error] " + msg + "\n");
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
