package com.example.hhh;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

public class VideoActivity extends AppCompatActivity {

    private VideoView videoView;
    private ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        videoView = findViewById(R.id.videoView);
        btnBack = findViewById(R.id.btn_back);

        // Настройка кнопки назад
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Воспроизведение видео
        playVideo();
    }

    private void playVideo() {
        try {
            // Путь к видеофайлу в папке raw
            String videoPath = "android.resource://" + getPackageName() + "/" + R.raw.bem;
            Uri videoUri = Uri.parse(videoPath);

            videoView.setVideoURI(videoUri);
            videoView.setOnPreparedListener(mp -> {
                mp.setLooping(true); // Зациклить видео
                videoView.start();
            });

        } catch (Exception e) {
            e.printStackTrace();
            finish(); // Закрыть активность при ошибке
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (videoView != null) {
            videoView.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (videoView != null) {
            videoView.start();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (videoView != null) {
            videoView.stopPlayback();
        }
    }
}