import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.awt.image.BufferedImage;

public class BuoiL5_Bai2 {

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(5556)) {
            System.out.println("Server đang chờ keets nối...");

            while (true) {
                try (Socket clientSocket = serverSocket.accept();
                     InputStream is = new BufferedInputStream(clientSocket.getInputStream())) {

                    System.out.println("Kết nối thành công tới client: " + clientSocket.getInetAddress());

                    BufferedImage image = ImageIO.read(is);
                    if (image != null) {
                        displayImage(image);
                        System.out.println("Hinh ảnh đã nhận và hien thị.");
                    } else {
                        System.out.println("Không nhận được ảnh.");
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void displayImage(BufferedImage image) {
        JFrame frame = new JFrame("Image");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(new Dimension(image.getWidth(), image.getHeight()));
        JLabel label = new JLabel(new ImageIcon(image));
        frame.add(label);
        frame.pack();
        frame.setVisible(true);
    }
}
