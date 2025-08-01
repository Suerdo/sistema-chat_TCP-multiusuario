import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class SimpleUDPServer {
    private final int BUFFER_SIZE = 1000; // Tamanho do buffer de recepção
    private DatagramSocket socket;

    // Inicia o servidor na porta especificada
    public void start(int port) throws IOException {
        System.out.println("[S1] Criando socket UDP para aguardar mensagens de clientes em loop");
        socket = new DatagramSocket(port);

        while (true) {
            System.out.println("[S2] Aguardando nova mensagem em: " + socket.getLocalSocketAddress());

            DatagramPacket request = receivePacket(); // Recebe mensagem do cliente
            String msg = new String(request.getData());

            System.out.println("[S3] Mensagem recebida de " + request.getSocketAddress() + ": " + msg);

            String reply = msg.toUpperCase(); // Processa a mensagem
            sendMessage(reply, request.getAddress().getHostAddress(), request.getPort()); // Envia resposta

            System.out.println("[S4] Mensagem enviada para " + request.getSocketAddress() + ": " + reply);
        }
    }

    // Envia mensagem UDP para o cliente
    public void sendMessage(String msg, String ip, int port) throws IOException {
        InetAddress address = InetAddress.getByName(ip);
        DatagramPacket packet = new DatagramPacket(msg.getBytes(), msg.length(), address, port);
        socket.send(packet);
    }

    // Recebe um pacote UDP
    public DatagramPacket receivePacket() throws IOException {
        byte[] buffer = new byte[BUFFER_SIZE];
        DatagramPacket packet = new DatagramPacket(buffer, BUFFER_SIZE);
        socket.receive(packet);
        return packet;
    }

    // Ponto de entrada do servidor
    public static void main(String[] args) {
        try {
            SimpleUDPServer server = new SimpleUDPServer();
            int port = 6789;
            server.start(port);
        } catch (SocketException e) {
            System.out.println("Socket: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
        }
    }
}
