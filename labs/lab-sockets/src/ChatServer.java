import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class ChatServer {
    private static final int PORT = 6666;
    private static Set<ClientHandler> clientHandlers = ConcurrentHashMap.newKeySet();
    private static ExecutorService executor = Executors.newCachedThreadPool();

    public static void main(String[] args) {
        System.out.println("=== SERVIDOR DE CHAT ===");
        System.out.println("Iniciando servidor na porta " + PORT);
        
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Servidor aguardando conexões em: " + serverSocket.getLocalSocketAddress());
            
            new Thread(new ServerConsoleHandler()).start();
            
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Nova conexão de: " + clientSocket.getRemoteSocketAddress());
                
                ClientHandler clientHandler = new ClientHandler(clientSocket);
                clientHandlers.add(clientHandler);
                executor.submit(clientHandler);
            }
        } catch (IOException e) {
            System.err.println("Erro no servidor: " + e.getMessage());
        }
    }

    public static void broadcastMessage(String message, ClientHandler sender) {
        Iterator<ClientHandler> iterator = clientHandlers.iterator();
        while (iterator.hasNext()) {
            ClientHandler handler = iterator.next();
            if (handler != sender) { 
                if (!handler.sendMessage(message)) {
                    iterator.remove(); 
                }
            }
        }
    }

    public static void broadcastServerMessage(String message) {
        String serverMessage = "[SERVIDOR]: " + message;
        Iterator<ClientHandler> iterator = clientHandlers.iterator();
        while (iterator.hasNext()) {
            ClientHandler handler = iterator.next();
            if (!handler.sendMessage(serverMessage)) {
                iterator.remove(); 
            }
        }
    }

    
    public static void removeClient(ClientHandler client) {
        clientHandlers.remove(client);
    }

    public static int getConnectedClientsCount() {
        return clientHandlers.size();
    }
}

class ClientHandler implements Runnable {
    private Socket socket;
    private DataInputStream input;
    private DataOutputStream output;
    private String clientName;

    public ClientHandler(Socket socket) {
        this.socket = socket;
        try {
            this.input = new DataInputStream(socket.getInputStream());
            this.output = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            System.err.println("Erro ao criar streams para cliente: " + e.getMessage());
        }
    }




    @Override
    public void run() {
        try {
            clientName = input.readUTF();
            System.out.println("Cliente '" + clientName + "' entrou no chat");
            
            ChatServer.broadcastMessage("*** " + clientName + " entrou no chat ***", this);
            
            sendMessage("Bem-vindo ao chat, " + clientName + "! Há " + 
                       ChatServer.getConnectedClientsCount() + " usuários conectados.");

            String message;
            while ((message = input.readUTF()) != null) {
                if (message.equalsIgnoreCase("/sair") || message.equalsIgnoreCase("/quit")) {
                    break;
                }
                
                String formattedMessage = "[" + clientName + "]: " + message;
                System.out.println(formattedMessage);
                ChatServer.broadcastMessage(formattedMessage, this);
            }
        } catch (IOException e) {
            System.out.println("Cliente desconectado: " + 
                             (clientName != null ? clientName : socket.getRemoteSocketAddress()));
        } finally {
            disconnect();
        }
    }


    

    public boolean sendMessage(String message) {
        try {
            output.writeUTF(message);
            return true;
        } catch (IOException e) {
            return false; 
        }
    }

    private void disconnect() {
        try {
            if (clientName != null) {
                ChatServer.broadcastMessage("*** " + clientName + " saiu do chat ***", this);
            }
            ChatServer.removeClient(this);
            socket.close();
        } catch (IOException e) {
            System.err.println("Erro ao desconectar cliente: " + e.getMessage());
        }
    }
}

class ServerConsoleHandler implements Runnable {
    @Override
    public void run() {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Digite mensagens do servidor (ou 'sair' para encerrar):");
            
            while (true) {
                String message = scanner.nextLine();
                if (message.equalsIgnoreCase("sair") || message.equalsIgnoreCase("quit")) {
                    System.out.println("Encerrando servidor...");
                    System.exit(0);
                }
                
                if (!message.trim().isEmpty()) {
                    ChatServer.broadcastServerMessage(message);
                }
            }
        }
    }
}
