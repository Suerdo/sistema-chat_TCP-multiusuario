import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SimpleTCPServer {
    private ServerSocket serverSocket;
    private Socket socket;
    private DataInputStream input;
    private DataOutputStream output;

    // Inicia o servidor TCP na porta especificada
    public void start(int port) throws IOException {
        System.out.println("[S1] Criando server socket para aguardar conexões de clientes em loop");
        serverSocket = new ServerSocket(port);

        while (serverSocket.isBound()) {
            System.out.println("[S2] Aguardando conexão em: " + serverSocket.getLocalSocketAddress());
            socket = serverSocket.accept(); // Aguarda e aceita conexão de cliente

            System.out.println("[S3] Conexão estabelecida com cliente: " + socket.getRemoteSocketAddress());

            input = new DataInputStream(socket.getInputStream());
            output = new DataOutputStream(socket.getOutputStream());

            String msg = input.readUTF(); // Lê mensagem do cliente
            System.out.println("[S4] Mensagem recebida de " + socket.getRemoteSocketAddress() + ": " + msg);

            String reply = msg.toUpperCase(); // Processa resposta
            output.writeUTF(reply); // Envia resposta ao cliente

            System.out.println("[S5] Mensagem enviada para " + socket.getRemoteSocketAddress() + ": " + reply);
        }
    }

    // Encerra conexões e libera recursos
    public void stop() throws IOException {
        input.close();
        output.close();
        socket.close();
        serverSocket.close();
    }

    // Ponto de entrada do servidor
    public static void main(String[] args) {
        int serverPort = 6666;
        try {
            SimpleTCPServer server = new SimpleTCPServer();
            server.start(serverPort);
            server.stop();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

