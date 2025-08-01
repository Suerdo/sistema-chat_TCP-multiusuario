import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Scanner;

public class SimpleUDPClient {
    private DatagramSocket socket;
    private byte[] buffer;

    // Inicia o cliente e realiza o envio e recepção de mensagens
    public void start(String serverIp, int serverPort) throws IOException {
        System.out.println("[C1] Criando socket UDP para enviar mensagem para servidor");
        socket = new DatagramSocket();
        this.buffer = new byte[1024];

        Scanner scanner = new Scanner(System.in);
        System.out.print("Digite uma mensagem: ");
        String msg = scanner.nextLine();
        scanner.close();

        System.out.println("[C2] Enviando msg para " + serverIp + ":" + serverPort + ": " + msg);
        sendMessage(msg, serverIp, serverPort); // Envia mensagem ao servidor

        System.out.println("[C3] Mensagem enviada, recebendo resposta");
        String response = receiveMessage(); // Aguarda resposta
        System.out.println("[C4] Resposta recebida: " + response);
    }

    // Envia uma mensagem para o servidor
    public void sendMessage(String msg, String ip, int port) throws IOException {
        InetAddress address = InetAddress.getByName(ip);
        DatagramPacket packet = new DatagramPacket(msg.getBytes(), msg.length(), address, port);
        socket.send(packet);
    }

    // Recebe uma mensagem do servidor
    public String receiveMessage() throws IOException {
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        socket.receive(packet);
        return new String(packet.getData());
    }

    // Fecha o socket e limpa o buffer
    public void stop() {
        this.socket.close();
        this.buffer = null;
    }

    // Ponto de entrada do cliente
    public static void main(String[] args) {
        String serverIp = "127.0.0.1";
        int serverPort = 6789;
        try {
            SimpleUDPClient client = new SimpleUDPClient();
            client.start(serverIp, serverPort);
            client.stop();
        } catch (SocketException e){
            System.out.println("Socket: " + e.getMessage());
        } catch (IOException e){
            System.out.println("IO: " + e.getMessage());
        }
    }
}
