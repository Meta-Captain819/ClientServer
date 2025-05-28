// ClientGUI.java (Updated Client Code)

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;

public class ClientGUI extends Application {

    private TextArea messageArea;
    private TextField inputField;
    private TextField nameField;
    private PrintWriter writer;
    private BufferedReader reader;
    private Socket socket;
    private String clientName;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Client Chat");

        nameField = new TextField();
        nameField.setPromptText("Enter your name and press Enter");
        nameField.setOnAction(e -> {
            clientName = nameField.getText().trim();
            if (!clientName.isEmpty()) {
                connectToServer();
                nameField.setDisable(true);
            }
        });

        primaryStage.setOnCloseRequest(event -> {
            cleanup();
            Platform.exit();
            System.exit(0);
        });

        messageArea = new TextArea();
        messageArea.setEditable(false);
        messageArea.setWrapText(true);

        inputField = new TextField();
        inputField.setPromptText("Type your message...");
        inputField.setOnAction(e -> sendMessage());
        inputField.setDisable(true);

        Button sendButton = new Button("Send");
        sendButton.setOnAction(e -> sendMessage());
        sendButton.setDisable(true);

        HBox inputBox = new HBox(10, inputField, sendButton);
        inputBox.setHgrow(inputField, Priority.ALWAYS);

        VBox root = new VBox(10, nameField, messageArea, inputBox);
        root.setPrefSize(500, 400);
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();

        inputField.setUserData(sendButton); // link to enable later
    }

    private void connectToServer() {
        try {
            socket = new Socket("localhost", 1234); // Adjust IP if needed
            writer = new PrintWriter(socket.getOutputStream(), true);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Send name as first message
            writer.println(clientName);

            // Start a thread to read messages from server
            new Thread(() -> {
                String line;
                try {
                    while ((line = reader.readLine()) != null) {
                        String finalLine = line;
                        Platform.runLater(() -> messageArea.appendText(finalLine + "\n"));
                    }
                } catch (IOException e) {
                    showError("Disconnected from server.");
                }
            }).start();

            inputField.setDisable(false);
            ((Button) inputField.getUserData()).setDisable(false);

        } catch (IOException e) {
            showError("Could not connect to server.");
        }
    }

       private void sendMessage() {
        String message = inputField.getText();
        if (!message.isEmpty() && writer != null) {
            writer.println(message);
            // Add this line to show the sent message in client's message area
            messageArea.appendText("You: " + message + "\n");
            inputField.clear();
        }
    }
      private void cleanup() {
        try {
            if (writer != null) {
                writer.close();
            }
            if (reader != null) {
                reader.close();
            }
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            System.err.println("Error during cleanup: " + e.getMessage());
        }
    }

    private void showError(String msg) {
        Platform.runLater(() -> messageArea.appendText("[Error] " + msg + "\n"));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
