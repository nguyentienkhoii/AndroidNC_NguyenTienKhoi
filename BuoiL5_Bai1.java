import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class BuoiL5_Bai1 {

    public static void main(String[] args) {
        int port = 5555;

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server đang chờ keeets nối...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Kết nối thành công từ client: " + clientSocket.getInetAddress());

                try (DataInputStream dis = new DataInputStream(clientSocket.getInputStream())) {
                    // đoc thông tin nhaanja được
                    String message = dis.readUTF();
                    System.out.println("Thông tin nhận được: " + message);
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    clientSocket.close();
                }
            }

        } catch (IOException e) {
            System.out.println("Lỗi khi khởi tạo server: " + e.getMessage());
        }
    }
}
