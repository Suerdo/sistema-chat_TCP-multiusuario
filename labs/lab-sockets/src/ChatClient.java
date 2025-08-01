import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ChatClient {
    private static final String SERVER_IP = "localhost";
    private static final int SERVER_PORT = 6666;

    private Socket socket;
    private DataInputStream input;
    private DataOutputStream output;
    private String username;

    public static void main(String[] args) {
        ChatClient client = new ChatClient();
        client.startChat(); // Inicia o cliente
    }

    public void startChat() {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.println("=== CLIENTE DE CHAT ===");
            System.out.print("Digite seu nome de usuário: ");
            username = scanner.nextLine();

            // Conecta ao servidor
            System.out.println("Conectando ao servidor " + SERVER_IP + ":" + SERVER_PORT);
            socket = new Socket(SERVER_IP, SERVER_PORT);
            input = new DataInputStream(socket.getInputStream());
            output = new DataOutputStream(socket.getOutputStream());

            // Envia o nome do usuário
            output.writeUTF(username);

            System.out.println("Conectado! Digite suas mensagens (ou '/sair' para sair):");

            // Inicia thread para receber mensagens do servidor
            Thread receiveThread = new Thread(new MessageReceiver());
            receiveThread.setDaemon(true); // Termina com o encerramento do programa principal
            receiveThread.start();

            // Loop para envio de mensagens
            String message;
            while (true) {
                message = scanner.nextLine();

                if (message.equalsIgnoreCase("/sair") || message.equalsIgnoreCase("/quit")) {
                    output.writeUTF(message);
                    break;
                }

                if (!message.trim().isEmpty()) {
                    output.writeUTF(message);
                }
            }

            scanner.close();
            disconnect(); // Finaliza conexão

        } catch (IOException e) {
            System.err.println("Erro de conexão: " + e.getMessage());
        }
    }

    // Encerra conexão com o servidor
    private void disconnect() {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
            System.out.println("Desconectado do servidor.");
        } catch (IOException e) {
            System.err.println("Erro ao desconectar: " + e.getMessage());
        }
    }

    // Thread para receber mensagens do servidor
    private class MessageReceiver implements Runnable {
        @Override
        public void run() {
            try {
                String message;
                while ((message = input.readUTF()) != null) {
                    System.out.println(message);
                }
            } catch (IOException e) {
                if (!socket.isClosed()) {
                    System.err.println("Conexão perdida com o servidor.");
                }
            }
        }
    }
}

