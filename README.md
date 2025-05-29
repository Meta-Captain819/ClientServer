## Project Description

This project is a Java-based Client-Server Chat Application that demonstrates fundamental concepts of network communication using the client-server architecture. The application uses JavaFX to provide a graphical user interface for both the server and clients, allowing real-time messaging.

Key aspects of the project include:

- Multiple clients can connect to the server simultaneously.
- Each client must register with a unique username.
- Clients send messages to the server, which can reply privately or broadcast messages to all connected clients.
- The system manages client connections, message routing, and ensures data integrity over the network.
- The application showcases multithreading for handling concurrent client connections and asynchronous communication.


This project serves as a practical implementation for learning Java networking, concurrency, and GUI development.


## 📚 Table of Contents

- [📖 Project Description](#project-description)
- [📂 Folder Structure](#folder-structure)
- [✨ Features](#features)
- [🧰 Tech Stack](#tech-stack)
- [💻 Installation & Running Instructions](#installation--running-instructions)
- [🚀 Usage Guide](#usage-guide)
- [🛡️ License](#license)
- [👨‍💻 Author Rights](#-author-rights)

## Folder Structure

```
JavaChatGUI/
├── .vscode/
│   ├── launch.json
│   └── settings.json
├── bin/
│   ├── ClientGUI.class
│   ├── ServerGUI$ClientHandler.class
│   └── ServerGUI.class
├── lib/
├── src/
│   ├── ClientGUI.java
│   └── ServerGUI.java
```

## Features

- **Multi-client Support:** Multiple clients can connect and communicate with the server simultaneously.
- **Unique Usernames:** Each client registers with a unique username for identification.
- **Private Messaging:** Server can send messages to specific clients individually.
- **Broadcast Messaging:** Server can broadcast messages to all connected clients.
- **Real-time Chat:** Messages are sent and received in real-time with GUI updates.
- **JavaFX User Interface:** Friendly and interactive graphical interface for both client and server.
- **Thread-safe Connection Handling:** Uses multithreading to manage multiple clients without blocking the UI.
- **Connection Management:** Handles client connection and disconnection events gracefully.
- **Error Handling:** Displays meaningful error messages in the GUI on connection failures or disconnections.

## Tech Stack

- **Programming Language:** Java  
- **Network Communication:** Java Sockets (TCP)  
- **User Interface:** JavaFX  
- **Concurrency:** Java Multithreading (for handling multiple clients concurrently)  
- **Build & Run:** Standard Java Development Kit (JDK)  

## Installation and Running Instructions

### Prerequisites

- **Java Development Kit (JDK) 8 or higher** installed on your system.  
- An IDE like IntelliJ IDEA, Eclipse, or use command line.  
- (Optional) Git to clone the repository.

---

### Step 1: Clone the Repository

Open your terminal or command prompt and run:

```bash
git clone https://github.com/Meta-Captain819/ClientServer.git
cd ClientServer
```
### Step 2: Compile the Server and the Client code
If you are using an IDE, simply open the project and build it.
If you prefer command line then run the following

```bash
javac ServerGUI.java
javac ClientGUI.java
```

### Step 3: Run the Server
Use the command according to your own path.
```bash
java --module-path "C:\javafx-sdk-21\javafx-sdk-24.0.1\lib" --add-modules javafx.controls,javafx.fxml -cp bin ServerGUI
```

### Step 4: Run the Client/Clients
You can run multiple Clients at once as well, Use the command according to your own path.


```bash
java --module-path "C:\javafx-sdk-21\javafx-sdk-24.0.1\lib" --add-modules javafx.controls,javafx.fxml -cp bin ClientGUI
```
---

This section shows the installation and the running instructions follow all the steps accordingly.

## Usage Guide

### Starting the Server

1. Launch the server application by running the `ServerGUI` class.
2. The server window will display a message indicating it is waiting for clients to connect.
3. The server supports multiple clients connecting simultaneously.

### Connecting Clients

1. Run the `ClientGUI` application on one or more machines.
2. When the client window opens, you will be prompted to enter a unique username.
3. After entering a valid username, the client connects to the server and can start sending messages.

### Sending Messages

- **From Client to Server:**
  - Type your message in the input box.
  - Press the **Enter** key or click the **Send** button to send the message.
  - The message will be sent exclusively to the server and not to other clients.

- **From Server to Client(s):**
  - The server can send messages to:
    - A specific client by entering the client’s username.
    - All connected clients by selecting the broadcast option.
  - Type the message in the server’s input box.
  - Choose the recipient (single client or broadcast).
  - Click **Send** to deliver the message.

### Managing Clients

- Each client has a unique username for identification.
- The server maintains a list of connected clients.
- The server GUI displays client connection and disconnection events.
- If a client disconnects, the server updates its client list accordingly.

### Error Handling

- If the client fails to connect to the server, an error message will appear in the client’s message area.
- If the server unexpectedly disconnects, clients will receive a disconnection notice.
- The server GUI shows error messages related to network or I/O issues.

---

This guide ensures smooth operation of the chat system and helps users understand how to interact effectively.


## 🛡️ License

This project is licensed under the [MIT License](https://opensource.org/licenses/MIT).


## 👨‍💻 Author Rights

This software is developed and maintained by **Muzammil Mehdi**.  
You are free to use, contribute, and extend the project under the terms of the MIT License.

If you use this project in your own work, giving proper credit is appreciated and it is mandatory as well.

For any inquiries or collaboration requests, feel free to reach out via GitHub: [Meta-Captain819](https://github.com/Meta-Captain819)



