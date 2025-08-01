import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class SimpleTCPClient {
    private Socket socket;
    private DataInputStream input;
    private DataOutputStream output;

    // Conecta ao servidor, envia mensagem e recebe resposta
    public void start(String serverIp, int serverPort) throws IOException {
        System.out.println("[C1] Conectando com servidor " + serverIp + ":" + serverPort);
        socket = new Socket(serverIp, serverPort);

        input = new DataInputStream(socket.getInputStream());
        output = new DataOutputStream(socket.getOutputStream());

        System.out.println("[C2] Conexão estabelecida, eu sou o cliente: " + socket.getLocalSocketAddress());

        System.out.print("Digite uma mensagem: ");
        Scanner scanner = new Scanner(System.in);
        String msg = scanner.nextLine();
        scanner.close();

        System.out.println("[C3] Enviando mensagem para servidor");
        output.writeUTF(msg); // Envia mensagem ao servidor

        System.out.println("[C4] Mensagem enviada, recebendo resposta");
        String response = input.readUTF(); // Lê resposta do servidor

        System.out.println("[C5] Resposta recebida: " + response);
    }

    // Fecha conexões e recursos
    public void stop() {
        try {
            input.close();
            output.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Ponto de entrada do cliente
    public static void main(String[] args) {
        String serverIp = "0.0.0.0"; // Altere para "127.0.0.1" se estiver testando localmente
        int serverPort = 6666;
        try {
            SimpleTCPClient client = new SimpleTCPClient();
            client.start(serverIp, serverPort);
            client.stop();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

