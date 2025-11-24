package com.example.hhh;

import android.database.Cursor;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity5 extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private LinearLayout repairRequestsContainer, paintRequestsContainer;
    private ImageButton sortRepairButton, sortPaintButton;
    private boolean repairSortAscending = true; // true - по возрастанию, false - по убыванию для ремонта
    private boolean paintSortAscending = true;  // true - по возрастанию, false - по убыванию для покраски

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5);

        // Инициализация базы данных
        databaseHelper = new DatabaseHelper(this);

        // Инициализация Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar5);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        // Находим контейнеры и кнопки сортировки
        repairRequestsContainer = findViewById(R.id.repair_requests_container);
        paintRequestsContainer = findViewById(R.id.paint_requests_container);
        sortRepairButton = findViewById(R.id.sort_repair_button);
        sortPaintButton = findViewById(R.id.sort_paint_button);

        // Обработчик кнопки сортировки для ремонта
        sortRepairButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Переключаем сортировку при каждом нажатии
                repairSortAscending = !repairSortAscending;

                // Показываем короткое сообщение о текущей сортировке
                String message = repairSortAscending ?
                        "Ремонт: старые сначала" :
                        "Ремонт: новые сначала";
                Toast.makeText(MainActivity5.this, message, Toast.LENGTH_SHORT).show();

                // Обновляем данные только для ремонта
                loadRepairRequests();
            }
        });

        // Обработчик кнопки сортировки для покраски
        sortPaintButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Переключаем сортировку при каждом нажатии
                paintSortAscending = !paintSortAscending;

                // Показываем короткое сообщение о текущей сортировке
                String message = paintSortAscending ?
                        "Покраска: старые сначала" :
                        "Покраска: новые сначала";
                Toast.makeText(MainActivity5.this, message, Toast.LENGTH_SHORT).show();

                // Обновляем данные только для покраски
                loadPaintRequests();
            }
        });

        // Загружаем данные
        loadRepairRequests();
        loadPaintRequests();
    }

    private void loadRepairRequests() {
        // Очищаем контейнер
        repairRequestsContainer.removeAllViews();

        String orderBy = repairSortAscending ?
                DatabaseHelper.COLUMN_DATE + " ASC" :
                DatabaseHelper.COLUMN_DATE + " DESC";

        Cursor cursor = databaseHelper.getReadableDatabase().rawQuery(
                "SELECT * FROM " + DatabaseHelper.TABLE_REQUESTS +
                        " WHERE " + DatabaseHelper.COLUMN_TYPE + " = 'repair'" +
                        " ORDER BY " + orderBy,
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

        String orderBy = paintSortAscending ?
                DatabaseHelper.COLUMN_DATE + " ASC" :
                DatabaseHelper.COLUMN_DATE + " DESC";

        Cursor cursor = databaseHelper.getReadableDatabase().rawQuery(
                "SELECT * FROM " + DatabaseHelper.TABLE_REQUESTS +
                        " WHERE " + DatabaseHelper.COLUMN_TYPE + " = 'paint'" +
                        " ORDER BY " + orderBy,
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
        View cardView = inflater.inflate(R.layout.card_request, container, false);

        // Находим элементы карточки
        TextView textClientName = cardView.findViewById(R.id.text_client_name);
        TextView textPhone = cardView.findViewById(R.id.text_phone);
        TextView textCarModel = cardView.findViewById(R.id.text_car_model);
        TextView textDate = cardView.findViewById(R.id.text_date);
        TextView textColor = cardView.findViewById(R.id.text_color);
        TextView textDescription = cardView.findViewById(R.id.text_description);
        TextView textStatus = cardView.findViewById(R.id.text_status);
        Button buttonComplete = cardView.findViewById(R.id.button_complete);
        Button buttonDelete = cardView.findViewById(R.id.button_delete);
        TextView textAvatar = cardView.findViewById(R.id.text_avatar);
        LinearLayout avatarLayout = (LinearLayout) cardView.findViewById(R.id.text_avatar).getParent();

        // Заполняем данными
        textClientName.setText(client);
        textPhone.setText(phone);
        textCarModel.setText(carModel);
        textDate.setText(date);

        // Настраиваем аватарку
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

        // Настраиваем статус и кнопку в зависимости от текущего состояния
        updateCardStatus(textStatus, buttonComplete, status);

        // Обработчик кнопки "Выполнить/Отменить"
        buttonComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (status.equals("completed")) {
                    // Если заявка выполнена, отменяем выполнение
                    undoCompleteRequest(id, type);
                } else {
                    // Если заявка в работе, выполняем её
                    completeRequest(id, type);
                }
            }
        });

        // Обработчик кнопки "Удалить"
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteConfirmationDialog(id, type);
            }
        });

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

    private void updateCardStatus(TextView textStatus, Button buttonComplete, String status) {
        if (status.equals("completed")) {
            textStatus.setText("Выполнено");
            textStatus.setTextColor(0xFF4CAF50); // Зеленый
            buttonComplete.setText("Отменить");
            buttonComplete.setBackgroundTintList(android.content.res.ColorStateList.valueOf(0xFFFF9800)); // Оранжевый
            buttonComplete.setEnabled(true);
            buttonComplete.setAlpha(1f);
        } else {
            textStatus.setText("В работе");
            textStatus.setTextColor(0xFFFF9800); // Оранжевый
            buttonComplete.setText("Выполнить");
            buttonComplete.setBackgroundTintList(android.content.res.ColorStateList.valueOf(0xFF4CAF50)); // Зеленый
            buttonComplete.setEnabled(true);
            buttonComplete.setAlpha(1f);
        }
    }

    private void addEmptyMessage(LinearLayout container, String message) {
        TextView emptyText = new TextView(this);
        emptyText.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        emptyText.setText(message);
        emptyText.setTextColor(0xFFFFFFFF);
        emptyText.setGravity(android.view.Gravity.CENTER);
        emptyText.setPadding(32, 32, 32, 32);
        container.addView(emptyText);
    }

    private void completeRequest(int id, String type) {
        boolean success = databaseHelper.updateRequestStatus(id, "completed");

        if (success) {
            Toast.makeText(this, "Заявка выполнена!", Toast.LENGTH_SHORT).show();
            // Обновляем данные
            if (type.equals("repair")) {
                loadRepairRequests();
            } else {
                loadPaintRequests();
            }
        } else {
            Toast.makeText(this, "Ошибка при выполнении заявки", Toast.LENGTH_SHORT).show();
        }
    }

    private void undoCompleteRequest(int id, String type) {
        boolean success = databaseHelper.updateRequestStatus(id, "in_progress");

        if (success) {
            Toast.makeText(this, "Выполнение заявки отменено!", Toast.LENGTH_SHORT).show();
            // Обновляем данные
            if (type.equals("repair")) {
                loadRepairRequests();
            } else {
                loadPaintRequests();
            }
        } else {
            Toast.makeText(this, "Ошибка при отмене выполнения заявки", Toast.LENGTH_SHORT).show();
        }
    }

    private void showDeleteConfirmationDialog(final int id, final String type) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("Подтверждение удаления");
        builder.setMessage("Уверены ли вы, что хотите удалить эту заявку?");

        builder.setPositiveButton("Удалить", (dialog, which) -> {
            deleteRequest(id, type);
        });

        builder.setNegativeButton("Отмена", (dialog, which) -> {
            dialog.dismiss();
        });

        android.app.AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void deleteRequest(int id, String type) {
        // Используем метод из DatabaseHelper вместо прямого доступа к базе
        boolean success = databaseHelper.deleteRequest(id);

        if (success) {
            Toast.makeText(this, "Заявка удалена", Toast.LENGTH_SHORT).show();
            // Обновляем данные
            if (type.equals("repair")) {
                loadRepairRequests();
            } else {
                loadPaintRequests();
            }
        } else {
            Toast.makeText(this, "Ошибка при удалении заявки", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Обновляем данные при возвращении на экран
        loadRepairRequests();
        loadPaintRequests();
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