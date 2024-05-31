package com.example.mytravelsver2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.VideoView;

public class MainActivity extends AppCompatActivity {
    // Khai báo biến
    VideoView videoView;
    Button button;
    @Override // Oncreate được gọi khi một activity được tạo ra
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        // Yêu cầu sử dụng tính năng đặc biệt trường hợp này là yêu cầu hiển thị video, số 1 ở đây tương ứng với feature no title, giúp giao diện người dùng nhìn gọn gàng hơn
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        // đặt cờ cho cửa sổ hiển thị, loại bỏ giới hạn của cửa sổ tạo ra một giao diện tràn màn hình không có giới hạn về việc phân chia thanh trạng thái và thanh tiêu đề
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        // đặt màu sắc thanh trạng thái là màu trong suốt, tạo nên giao diện liền mạch và mở rộng không gian hiển thị
        setContentView(R.layout.activity_main);
        // đặt giao diện người dùng Activity thành activity_main gắn kết giao diện người dùng và hiển thị nội dung tương tác
        button = findViewById(R.id.nextActivity);
        videoView = findViewById(R.id.viewVideo);
        // Ánh xạ biến với các tên địa chỉ Id của giao diện
        String path = "android.resource://com.example.mytravelsver2/"+R.raw.intro_video;
        // Tạo ra đường dẫn đến resource file trong android, tệp tài nguyên được truy cập bằng giao thức "android.resource" trong gói "com.example.mytravelsver2" sau đó tham chiếu đến tệp nguồn "raw resource file" trong đó có "introvideo"

        Uri uri = Uri.parse(path);
        videoView.setVideoURI(uri);
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
            }
        });
        // Gọi và phát video
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.start();
            }
        });
        // Tạo một còng lặp phát lại một video khi đã kết thúc
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        });
        // Nút ấn chuyển sang một activity khác
    }
    private class TRANSPARENT {
    }// Đóng gói lớp dữ liệu
}