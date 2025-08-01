import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;

class ClientHandler extends Thread {
    DataInputStream in;
    DataOutputStream out;
    Socket clientSocket;

    // Construtor: associa o socket e inicializa streams de entrada/saída
    public ClientHandler(Socket aClientSocket) {
        try {
            clientSocket = aClientSocket;
            in = new DataInputStream(clientSocket.getInputStream());
            out = new DataOutputStream(clientSocket.getOutputStream());
        } catch (IOException e) {
            System.out.println("Connection: " + e.getMessage());
        }
    }

    // Executa a lógica de comunicação com o cliente
    public void run() {
        try {
            String data = in.readUTF(); // Lê mensagem do cliente
            System.out.println("Mensagem recebida: " + data);
            out.writeUTF(data.toUpperCase()); // Retorna resposta em maiúsculas
        } catch (EOFException e) {
            System.out.println("EOF: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("readline: " + e.getMessage());
        } finally {
            try {
                clientSocket.close(); // Fecha conexão ao finalizar
            } catch (IOException e) {
                // Falha ao fechar
            }
        }
    }
}
