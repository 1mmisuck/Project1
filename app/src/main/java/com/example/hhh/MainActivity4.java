package com.example.hhh;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity4 extends AppCompatActivity {

    private TextInputEditText nameEditText4, phoneEditText4, carModelEditText4, colorEditText4;
    private TextInputEditText dateEditText4;
    private TextInputLayout nameInputLayout4, phoneInputLayout4, carModelInputLayout4, colorInputLayout4;
    private TextInputLayout dateInputLayout4;
    private Calendar calendar;
    private SimpleDateFormat dateFormatter;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);

        // Инициализация базы данных
        databaseHelper = new DatabaseHelper(this);

        // Инициализация Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar4);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        // Инициализация календаря и формата даты
        calendar = Calendar.getInstance();
        dateFormatter = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());

        // Находим все поля ввода
        initializeViews();

        // Устанавливаем обработчики
        setupDatePicker();
        setupSubmitButton();
        setupErrorClearing();
    }

    private void initializeViews() {
        nameEditText4 = findViewById(R.id.nameEditText4);
        phoneEditText4 = findViewById(R.id.phoneEditText4);
        carModelEditText4 = findViewById(R.id.carModelEditText4);
        colorEditText4 = findViewById(R.id.colorEditText4);
        dateEditText4 = findViewById(R.id.dateEditText4);

        nameInputLayout4 = findViewById(R.id.nameInputLayout4);
        phoneInputLayout4 = findViewById(R.id.phoneInputLayout4);
        carModelInputLayout4 = findViewById(R.id.carModelInputLayout4);
        colorInputLayout4 = findViewById(R.id.colorInputLayout4);
        dateInputLayout4 = findViewById(R.id.dateInputLayout4);
    }

    private void setupDatePicker() {
        dateEditText4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        dateEditText4.setFocusable(false);
        dateEditText4.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showDatePickerDialog();
                }
            }
        });
    }

    private void setupSubmitButton() {
        Button submitButton = findViewById(R.id.submitButton4);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitApplication();
            }
        });
    }

    private void setupErrorClearing() {
        // Очистка ошибок при вводе текста
        nameEditText4.addTextChangedListener(new SimpleTextWatcher() {
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                nameInputLayout4.setErrorEnabled(false);
            }
        });

        phoneEditText4.addTextChangedListener(new SimpleTextWatcher() {
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                phoneInputLayout4.setErrorEnabled(false);
            }
        });

        carModelEditText4.addTextChangedListener(new SimpleTextWatcher() {
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                carModelInputLayout4.setErrorEnabled(false);
            }
        });

        colorEditText4.addTextChangedListener(new SimpleTextWatcher() {
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                colorInputLayout4.setErrorEnabled(false);
            }
        });

        dateEditText4.addTextChangedListener(new SimpleTextWatcher() {
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                dateInputLayout4.setErrorEnabled(false);
            }
        });
    }

    private void showDatePickerDialog() {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        updateDateInView();
                    }
                },
                year, month, day
        );

        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    private void updateDateInView() {
        String selectedDate = dateFormatter.format(calendar.getTime());
        dateEditText4.setText(selectedDate);
    }

    private void submitApplication() {
        boolean hasError = false;

        // Сбрасываем ошибки
        resetErrors();

        // Проверяем заполнение всех полей
        if (nameEditText4.getText().toString().trim().isEmpty()) {
            setError(nameInputLayout4, "Введите имя владельца");
            hasError = true;
        }

        if (phoneEditText4.getText().toString().trim().isEmpty()) {
            setError(phoneInputLayout4, "Введите номер телефона");
            hasError = true;
        }

        if (carModelEditText4.getText().toString().trim().isEmpty()) {
            setError(carModelInputLayout4, "Введите модель автомобиля");
            hasError = true;
        }

        if (colorEditText4.getText().toString().trim().isEmpty()) {
            setError(colorInputLayout4, "Введите желаемый цвет покраски");
            hasError = true;
        }

        if (dateEditText4.getText().toString().isEmpty()) {
            setError(dateInputLayout4, "Выберите дату");
            hasError = true;
        }

        if (hasError) {
            return;
        }

        // Сохраняем в базу данных
        long result = databaseHelper.addPaintRequest(
                nameEditText4.getText().toString().trim(),
                phoneEditText4.getText().toString().trim(),
                carModelEditText4.getText().toString().trim(),
                colorEditText4.getText().toString().trim(),
                dateEditText4.getText().toString()
        );

        if (result != -1) {
            Toast.makeText(this, "Заявка на покраску сохранена в базе данных! Статус: В работе", Toast.LENGTH_LONG).show();

            // Очищаем поля после успешного сохранения
            nameEditText4.setText("");
            phoneEditText4.setText("");
            carModelEditText4.setText("");
            colorEditText4.setText("");
            dateEditText4.setText("");

            // Автоматический переход на статистику
            navigateToStatistics();

        } else {
            Toast.makeText(this, "Ошибка сохранения заявки", Toast.LENGTH_SHORT).show();
        }
    }

    private void setError(TextInputLayout layout, String error) {
        layout.setError(error);
        layout.setErrorEnabled(true);
    }

    private void resetErrors() {
        nameInputLayout4.setErrorEnabled(false);
        phoneInputLayout4.setErrorEnabled(false);
        carModelInputLayout4.setErrorEnabled(false);
        colorInputLayout4.setErrorEnabled(false);
        dateInputLayout4.setErrorEnabled(false);
    }

    private void navigateToStatistics() {
        Intent intent = new Intent(MainActivity4.this, MainActivity3.class);
        startActivity(intent);
        finish(); // Закрываем текущую активность
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    protected void onDestroy() {
        databaseHelper.close();
        super.onDestroy();
    }

    // Простой TextWatcher
    private abstract class SimpleTextWatcher implements TextWatcher {
        @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        @Override public void afterTextChanged(Editable s) {}
    }
}