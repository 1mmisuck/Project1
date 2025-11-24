package com.example.hhh;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private TextView headerDate, headerTime;
    private Handler timeHandler;
    private DatabaseHelper databaseHelper;
    private LinearLayout repairRequestsContainer, paintRequestsContainer;
    private Button secretButton;
    private int clickCount = 0;
    private static final int SECRET_CLICKS_NEEDED = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseHelper = new DatabaseHelper(this);

        // Инициализация элементов интерфейса
        initializeViews();

        // Установка обработчиков нажатий
        setupClickListeners();

        // Запуск обновления времени
        startTimeUpdates();

        // Загрузка заявок
        loadRequests();
    }

    private void initializeViews() {
        headerDate = findViewById(R.id.header_date);
        headerTime = findViewById(R.id.header_time);
        repairRequestsContainer = findViewById(R.id.repair_requests_container);
        paintRequestsContainer = findViewById(R.id.paint_requests_container);
        secretButton = findViewById(R.id.secret_button);

        // Установка текущей даты и времени
        updateDateTime();
    }

    private void setupClickListeners() {
        Button btnZayavki = findViewById(R.id.btn_zayavki);
        Button btnStatistika = findViewById(R.id.btn_statistika);
        Button btnPokraska = findViewById(R.id.btn_pokraska);

        TextView repairHeader = findViewById(R.id.repair_header);
        TextView paintHeader = findViewById(R.id.paint_header);

        // Вотермарк
        LinearLayout watermarkLayout = findViewById(R.id.watermark_layout);

        btnZayavki.setOnClickListener(v -> navigateToRepairRequest());
        btnStatistika.setOnClickListener(v -> navigateToStatistics());
        btnPokraska.setOnClickListener(v -> navigateToPaintRequest());

        repairHeader.setOnClickListener(v -> navigateToDetails());
        paintHeader.setOnClickListener(v -> navigateToDetails());

        // Обработчик нажатия на вотермарк
        if (watermarkLayout != null) {
            watermarkLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    playVideo();
                }
            });
        }

        // Обработчик для секретной кнопки
        secretButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSecretImage();
            }
        });

        // Активация секретной кнопки при многократном нажатии на вотермарк
        if (watermarkLayout != null) {
            watermarkLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    clickCount++;
                    if (clickCount >= SECRET_CLICKS_NEEDED) {
                        secretButton.setVisibility(View.VISIBLE);
                        clickCount = 0;
                    }
                    return true;
                }
            });
        }
    }

    private void showSecretImage() {
        Intent intent = new Intent(MainActivity.this, ImageActivity.class);
        startActivity(intent);
    }

    private void updateDateTime() {
        String currentDate = DateFormat.format("dd.MM.yyyy", new Date()).toString();
        String currentTime = DateFormat.format("HH:mm:ss", new Date()).toString();
        headerDate.setText(currentDate);
        headerTime.setText(currentTime);
    }

    private void playVideo() {
        Intent intent = new Intent(MainActivity.this, VideoActivity.class);
        startActivity(intent);
    }

    private void navigateToRepairRequest() {
        Intent intent = new Intent(this, MainActivity2.class);
        startActivity(intent);
    }

    private void navigateToStatistics() {
        Intent intent = new Intent(this, MainActivity3.class);
        startActivity(intent);
    }

    private void navigateToPaintRequest() {
        Intent intent = new Intent(this, MainActivity4.class);
        startActivity(intent);
    }

    private void navigateToDetails() {
        Intent intent = new Intent(this, MainActivity5.class);
        startActivity(intent);
    }

    private void startTimeUpdates() {
        timeHandler = new Handler();
        timeHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                updateTime();
                timeHandler.postDelayed(this, 1000);
            }
        }, 1000);
    }

    private void updateTime() {
        String currentTime = DateFormat.format("HH:mm:ss", new Date()).toString();
        headerTime.setText(currentTime);
    }

    private void loadRequests() {
        loadRepairRequests();
        loadPaintRequests();
    }

    private void loadRepairRequests() {
        // Очищаем контейнер
        repairRequestsContainer.removeAllViews();

        Cursor cursor = databaseHelper.getReadableDatabase().rawQuery(
                "SELECT * FROM " + DatabaseHelper.TABLE_REQUESTS +
                        " WHERE " + DatabaseHelper.COLUMN_TYPE + " = 'repair'" +
                        " ORDER BY " + DatabaseHelper.COLUMN_DATE + " DESC LIMIT 3",
                null
        );

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID));
                String client = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CLIENT));
                String phone = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PHONE));
                String carModel = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CAR_MODEL));
                String description = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DESCRIPTION));
                String date = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DATE));
                String status = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_STATUS));

                addRequestCard(repairRequestsContainer, id, "repair", client, phone, carModel, description, "", date, status);

            } while (cursor.moveToNext());
        } else {
            addEmptyMessage(repairRequestsContainer, "Нет заявок на ремонт");
        }

        cursor.close();
    }

    private void loadPaintRequests() {
        // Очищаем контейнер
        paintRequestsContainer.removeAllViews();

        Cursor cursor = databaseHelper.getReadableDatabase().rawQuery(
                "SELECT * FROM " + DatabaseHelper.TABLE_REQUESTS +
                        " WHERE " + DatabaseHelper.COLUMN_TYPE + " = 'paint'" +
                        " ORDER BY " + DatabaseHelper.COLUMN_DATE + " DESC LIMIT 3",
                null
        );

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID));
                String client = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CLIENT));
                String phone = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PHONE));
                String carModel = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CAR_MODEL));
                String color = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_COLOR));
                String date = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DATE));
                String status = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_STATUS));

                addRequestCard(paintRequestsContainer, id, "paint", client, phone, carModel, "", color, date, status);

            } while (cursor.moveToNext());
        } else {
            addEmptyMessage(paintRequestsContainer, "Нет заявок на покраску");
        }

        cursor.close();
    }

    private void addRequestCard(LinearLayout container, final int id, final String type,
                                String client, String phone, String carModel,
                                String description, String color, String date, String status) {

        // Создаем карточку из layout
        LayoutInflater inflater = LayoutInflater.from(this);
        View cardView = inflater.inflate(R.layout.card_main, container, false);

        // Находим элементы карточки
        TextView textClientName = cardView.findViewById(R.id.text_client_name);
        TextView textPhone = cardView.findViewById(R.id.text_phone);
        TextView textCarModel = cardView.findViewById(R.id.text_car_model);
        TextView textDate = cardView.findViewById(R.id.text_date);
        TextView textColor = cardView.findViewById(R.id.text_color);
        TextView textDescription = cardView.findViewById(R.id.text_description);
        TextView textStatus = cardView.findViewById(R.id.text_status);
        TextView textAvatar = cardView.findViewById(R.id.text_avatar);
        LinearLayout avatarLayout = (LinearLayout) cardView.findViewById(R.id.text_avatar).getParent();

        // Заполняем данными
        textClientName.setText(client);
        textPhone.setText(phone);
        textCarModel.setText(carModel);
        textDate.setText(date);

        // Настраиваем аватарку с разными цветами
        setupAvatar(avatarLayout, textAvatar, client);

        // Настраиваем специфичные поля в зависимости от типа заявки
        if (type.equals("paint")) {
            textColor.setText("Цвет: " + color);
            textColor.setVisibility(View.VISIBLE);
            textDescription.setVisibility(View.GONE);
        } else {
            // Для ремонтных заявок показываем описание проблемы
            if (description != null && !description.isEmpty()) {
                textDescription.setText("Проблема: " + (description.length() > 30 ?
                        description.substring(0, 30) + "..." : description));
                textDescription.setVisibility(View.VISIBLE);
            } else {
                textDescription.setVisibility(View.GONE);
            }
            textColor.setVisibility(View.GONE);
        }

        // Настраиваем статус
        updateCardStatus(textStatus, status);

        // Добавляем карточку в контейнер
        container.addView(cardView);
    }

    private void setupAvatar(LinearLayout avatarLayout, TextView textAvatar, String clientName) {
        // Получаем первую букву имени (или цифру если имя начинается с цифры)
        String firstChar = "";
        if (clientName != null && !clientName.isEmpty()) {
            firstChar = clientName.substring(0, 1).toUpperCase();
        } else {
            firstChar = "?";
        }

        textAvatar.setText(firstChar);

        // Генерируем цвет на основе имени для постоянства
        int color = generateColorFromString(clientName);
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.OVAL);
        drawable.setColor(color);
        avatarLayout.setBackground(drawable);
    }

    private int generateColorFromString(String text) {
        if (text == null || text.isEmpty()) {
            return 0xFF757575; // Серый по умолчанию
        }

        // Используем хэш строки для генерации цвета
        int hash = text.hashCode();

        // Цветовая палитра - приятные цвета
        int[] colors = {
                0xFFF44336, // Красный
                0xFFE91E63, // Розовый
                0xFF9C27B0, // Фиолетовый
                0xFF673AB7, // Глубокий фиолетовый
                0xFF3F51B5, // Индиго
                0xFF2196F3, // Синий
                0xFF03A9F4, // Голубой
                0xFF00BCD4, // Циан
                0xFF009688, // Бирюзовый
                0xFF4CAF50, // Зеленый
                0xFF8BC34A, // Светло-зеленый
                0xFFCDDC39, // Лаймовый
                0xFFFFC107, // Январный
                0xFFFF9800, // Оранжевый
                0xFFFF5722, // Глубокий оранжевый
                0xFF795548, // Коричневый
                0xFF607D8B  // Синий серый
        };

        int index = Math.abs(hash) % colors.length;
        return colors[index];
    }

    private void updateCardStatus(TextView textStatus, String status) {
        if (status.equals("completed")) {
            textStatus.setText("Выполнено");
            textStatus.setTextColor(0xFF4CAF50); // Зеленый
        } else {
            textStatus.setText("В работе");
            textStatus.setTextColor(0xFFFF9800); // Оранжевый
        }
    }

    private void addEmptyMessage(LinearLayout container, String message) {
        TextView emptyText = new TextView(this);
        emptyText.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        emptyText.setText(message);
        emptyText.setTextColor(0xFF666666);
        emptyText.setGravity(android.view.Gravity.CENTER);
        emptyText.setPadding(32, 32, 32, 32);
        emptyText.setTextSize(14);
        container.addView(emptyText);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Обновляем данные при возвращении на экран
        loadRequests();
        updateDateTime();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timeHandler != null) {
            timeHandler.removeCallbacksAndMessages(null);
        }
        if (databaseHelper != null) {
            databaseHelper.close();
        }
    }
}