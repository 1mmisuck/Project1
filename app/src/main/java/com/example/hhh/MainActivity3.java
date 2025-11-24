package com.example.hhh;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity3 extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private TextView totalRepairText, repairInProgressText, repairDoneText;
    private TextView totalPaintText, paintInProgressText, paintDoneText;
    private Button refreshButton, deleteRequestsButton, createRequestButton;
    private Button repairDetailsButton, paintDetailsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        // Инициализация базы данных
        databaseHelper = new DatabaseHelper(this);

        Toolbar toolbar = findViewById(R.id.toolbar3);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        // Находим все View
        initializeViews();

        // Устанавливаем обработчики для кнопок
        setupButtonListeners();

        // Обновляем статистику при создании активности
        updateStatistics();
    }

    private void initializeViews() {
        totalRepairText = findViewById(R.id.totalRepairText);
        repairInProgressText = findViewById(R.id.repairInProgressText);
        repairDoneText = findViewById(R.id.repairDoneText);
        totalPaintText = findViewById(R.id.totalPaintText);
        paintInProgressText = findViewById(R.id.paintInProgressText);
        paintDoneText = findViewById(R.id.paintDoneText);
        refreshButton = findViewById(R.id.refreshButton);
        deleteRequestsButton = findViewById(R.id.deleteRequestsButton);
        createRequestButton = findViewById(R.id.createRequestButton);
        repairDetailsButton = findViewById(R.id.repairDetailsButton);
        paintDetailsButton = findViewById(R.id.paintDetailsButton);
    }

    private void setupButtonListeners() {
        // Обработчик для кнопки обновления
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateStatistics();
                Toast.makeText(MainActivity3.this, "Статистика обновлена", Toast.LENGTH_SHORT).show();
            }
        });

        // Обработчик для кнопки удаления заявок
        deleteRequestsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteConfirmationDialog();
            }
        });

        // Обработчик для кнопки создания заявки
        createRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRequestTypeDialog();
            }
        });

        // Обработчик для кнопки "Подробнее" ремонт
        repairDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToDetails();
            }
        });

        // Обработчик для кнопки "Подробнее" покраска
        paintDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToDetails();
            }
        });
    }

    private void navigateToDetails() {
        Intent intent = new Intent(MainActivity3.this, MainActivity5.class);
        startActivity(intent);
    }

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Подтверждение удаления");
        builder.setMessage("Вы уверены, что хотите удалить ВСЕ заявки? Это действие нельзя отменить.");

        builder.setPositiveButton("Удалить все", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteAllRequests();
            }
        });

        builder.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void deleteAllRequests() {
        try {
            int deletedCount = databaseHelper.deleteAllRequests();

            updateStatistics();

            String message = "Удалено заявок: " + deletedCount;
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            Toast.makeText(this, "Ошибка при удалении заявок: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    private void showRequestTypeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Выберите тип заявки");

        String[] requestTypes = {"Ремонт", "Покраска"};

        builder.setItems(requestTypes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0: // Ремонт
                        navigateToRepairRequest();
                        break;
                    case 1: // Покраска
                        navigateToPaintRequest();
                        break;
                }
            }
        });

        builder.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void navigateToRepairRequest() {
        Intent intent = new Intent(MainActivity3.this, MainActivity2.class);
        startActivity(intent);
    }

    private void navigateToPaintRequest() {
        Intent intent = new Intent(MainActivity3.this, MainActivity4.class);
        startActivity(intent);
    }

    private void updateStatistics() {
        try {
            // Получаем данные из базы
            int totalRepair = databaseHelper.getTotalRepairCount();
            int repairInProgress = databaseHelper.getRepairInProgressCount();
            int repairCompleted = databaseHelper.getRepairCompletedCount();
            int totalPaint = databaseHelper.getTotalPaintCount();
            int paintInProgress = databaseHelper.getPaintInProgressCount();
            int paintCompleted = databaseHelper.getPaintCompletedCount();

            // Устанавливаем значения в TextView
            totalRepairText.setText("Общее количество заявок на ремонт: " + totalRepair);
            repairInProgressText.setText("Заявки на ремонт в работе: " + repairInProgress);
            repairDoneText.setText("Заявки на ремонт выполнены: " + repairCompleted);
            totalPaintText.setText("Общее количество заявок на покраску: " + totalPaint);
            paintInProgressText.setText("Заявки на покраску в работе: " + paintInProgress);
            paintDoneText.setText("Заявки на покраску выполнены: " + paintCompleted);

            // Устанавливаем цвета в зависимости от количества заявок
            setTextColors(repairInProgress, repairCompleted, paintInProgress, paintCompleted);

        } catch (Exception e) {
            Toast.makeText(this, "Ошибка загрузки статистики: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    private void setTextColors(int repairInProgress, int repairCompleted, int paintInProgress, int paintCompleted) {
        // Цвета
        int orangeColor = Color.parseColor("#FF9800"); // Оранжевый
        int greenColor = Color.parseColor("#4CAF50");  // Зеленый
        int whiteColor = Color.parseColor("#FFFFFF");  // Белый

        // Ремонт - в работе
        if (repairInProgress > 0) {
            repairInProgressText.setTextColor(orangeColor);
        } else {
            repairInProgressText.setTextColor(whiteColor);
        }

        // Ремонт - выполнено
        if (repairCompleted > 0) {
            repairDoneText.setTextColor(greenColor);
        } else {
            repairDoneText.setTextColor(whiteColor);
        }

        // Покраска - в работе
        if (paintInProgress > 0) {
            paintInProgressText.setTextColor(orangeColor);
        } else {
            paintInProgressText.setTextColor(whiteColor);
        }

        // Покраска - выполнено
        if (paintCompleted > 0) {
            paintDoneText.setTextColor(greenColor);
        } else {
            paintDoneText.setTextColor(whiteColor);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Обновляем статистику при возвращении на экран
        updateStatistics();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    protected void onDestroy() {
        if (databaseHelper != null) {
            databaseHelper.close();
        }
        super.onDestroy();
    }
}