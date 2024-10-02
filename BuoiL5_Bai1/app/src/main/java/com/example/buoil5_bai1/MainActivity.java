package com.example.buoil5_bai1;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    private static final int IMAGE_PICK_CODE = 1000;
    private static final String SERVER_IP = "192.168.2.11";
    private static final int SERVER_PORT = 5555;

    private TextView statusTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        statusTextView = findViewById(R.id.tv_status);

        Button pickImageBtn = findViewById(R.id.btn_pick_image);
        pickImageBtn.setOnClickListener(v -> pickImageFromGallery());
    }

    private void pickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, IMAGE_PICK_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_PICK_CODE && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();

            //Lay kich thyuoc anh
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;

            //decodestream de lay kich thuoc anh
            try (InputStream inputStream = getContentResolver().openInputStream(selectedImage)) {
                BitmapFactory.decodeStream(inputStream, null, options);
            } catch (IOException e) {
                e.printStackTrace();
            }

            int width = options.outWidth;
            int height = options.outHeight;

            sendImageToPC(selectedImage.getLastPathSegment(), width, height);
        } else {
            CancelImg();
        }
    }

    private void sendImageToPC(String fileName, int width, int height) {
        new Thread(() -> {
            try (Socket socket = new Socket(SERVER_IP, SERVER_PORT);
                 DataOutputStream dos = new DataOutputStream(socket.getOutputStream())) {
                dos.writeUTF("File: " + fileName + "\nSize: " + width + "x" + height);
                dos.flush();

                runOnUiThread(() -> statusTextView.setText("Đã gửi ảnh: " + fileName + " (" + width + "x" + height + ")"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void CancelImg() {
        new Thread(() -> {
            try (Socket socket = new Socket(SERVER_IP, SERVER_PORT);
                 DataOutputStream dos = new DataOutputStream(socket.getOutputStream())) {
                dos.writeUTF("Đã hủy chọn ảnh.");

                runOnUiThread(() -> statusTextView.setText("Hủy chọn ảnh."));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
