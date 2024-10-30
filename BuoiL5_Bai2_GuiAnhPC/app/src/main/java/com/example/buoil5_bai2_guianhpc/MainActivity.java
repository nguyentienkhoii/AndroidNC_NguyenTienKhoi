package com.example.buoil5_bai2_guianhpc;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_PICK_IMAGE = 1;
    private TextView tvStatus;
    private Uri selectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnSelectImage = findViewById(R.id.btnSelectImage);
        tvStatus = findViewById(R.id.tvStatus);

        btnSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagePicker();
            }
        });
    }


    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            tvStatus.setText("Trạng thái: Đã chọn ảnh, đang gửi...");
            sendImageToServer(selectedImageUri);
        } else {
            tvStatus.setText("Trạng thái: Hủy chọn ảnh");
        }
    }

    private void sendImageToServer(Uri uri) {
        new Thread(() -> {
            try (Socket socket = new Socket("192.168.2.11", 5556);
                 InputStream is = getContentResolver().openInputStream(uri);
                 OutputStream os = socket.getOutputStream()) {

                byte[] buffer = new byte[10240];
                int bytesRead;
                while ((bytesRead = is.read(buffer)) > -1) {
                    os.write(buffer, 0, bytesRead);
                }

                os.flush();
                runOnUiThread(() -> tvStatus.setText("Trạng thái: Đã gửi ảnh thành công!"));

            } catch (IOException e) {
                e.printStackTrace();
                runOnUiThread(() -> tvStatus.setText("Trạng thái: Lỗi gửi ảnh"));
            }
        }).start();
    }
}