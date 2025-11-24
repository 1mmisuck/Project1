package com.example.hhh;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class ImageActivity extends AppCompatActivity {

    private ImageView fullscreenImage;
    private ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        fullscreenImage = findViewById(R.id.fullscreen_image);
        btnBack = findViewById(R.id.btn_back_image);

        // Настройка кнопки назад
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Загрузка изображения
        loadImage();
    }

    private void loadImage() {
        try {
            // Загрузка изображения из папки drawable
            fullscreenImage.setImageResource(R.drawable.kizyak);
        } catch (Exception e) {
            e.printStackTrace();
            finish(); // Закрыть активность при ошибке
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}