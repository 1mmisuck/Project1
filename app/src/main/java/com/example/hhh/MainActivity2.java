package com.example.hhh;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity2 extends AppCompatActivity {

    private TextInputEditText nameEditText, phoneEditText, carModelEditText, problemEditText;
    private TextInputEditText dateEditText, timeEditText;
    private TextInputLayout nameInputLayout, phoneInputLayout, carModelInputLayout, problemInputLayout;
    private TextInputLayout dateInputLayout, timeInputLayout;
    private Calendar calendar;
    private SimpleDateFormat dateFormatter, timeFormatter;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        // Инициализация базы данных
        databaseHelper = new DatabaseHelper(this);

        // Инициализация Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        // Инициализация календаря и форматов
        calendar = Calendar.getInstance();
        dateFormatter = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        timeFormatter = new SimpleDateFormat("HH:mm", Locale.getDefault());

        // Находим все поля ввода
        initializeViews();

        // Устанавливаем обработчики
        setupDatePicker();
        setupTimePicker();
        setupSubmitButton();
        setupErrorClearing();
    }

    private void initializeViews() {
        nameEditText = findViewById(R.id.nameEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        carModelEditText = findViewById(R.id.carModelEditText);
        problemEditText = findViewById(R.id.problemEditText);
        dateEditText = findViewById(R.id.dateEditText);
        timeEditText = findViewById(R.id.timeEditText);

        nameInputLayout = findViewById(R.id.nameInputLayout);
        phoneInputLayout = findViewById(R.id.phoneInputLayout);
        carModelInputLayout = findViewById(R.id.carModelInputLayout);
        problemInputLayout = findViewById(R.id.problemInputLayout);
        dateInputLayout = findViewById(R.id.dateInputLayout);
        timeInputLayout = findViewById(R.id.timeInputLayout);
    }

    private void setupDatePicker() {
        dateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        dateEditText.setFocusable(false);
        dateEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showDatePickerDialog();
                }
            }
        });
    }

    private void setupTimePicker() {
        timeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog();
            }
        });

        timeEditText.setFocusable(false);
        timeEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showTimePickerDialog();
                }
            }
        });
    }

    private void setupSubmitButton() {
        Button submitButton = findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitApplication();
            }
        });
    }

    private void setupErrorClearing() {
        // Очистка ошибок при вводе текста
        nameEditText.addTextChangedListener(new SimpleTextWatcher() {
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                nameInputLayout.setErrorEnabled(false);
            }
        });

        phoneEditText.addTextChangedListener(new SimpleTextWatcher() {
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                phoneInputLayout.setErrorEnabled(false);
            }
        });

        carModelEditText.addTextChangedListener(new SimpleTextWatcher() {
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                carModelInputLayout.setErrorEnabled(false);
            }
        });

        problemEditText.addTextChangedListener(new SimpleTextWatcher() {
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                problemInputLayout.setErrorEnabled(false);
            }
        });

        dateEditText.addTextChangedListener(new SimpleTextWatcher() {
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                dateInputLayout.setErrorEnabled(false);
            }
        });

        timeEditText.addTextChangedListener(new SimpleTextWatcher() {
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                timeInputLayout.setErrorEnabled(false);
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

    private void showTimePickerDialog() {
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);
                        updateTimeInView();
                    }
                },
                hour, minute, true
        );

        timePickerDialog.show();
    }

    private void updateDateInView() {
        String selectedDate = dateFormatter.format(calendar.getTime());
        dateEditText.setText(selectedDate);
    }

    private void updateTimeInView() {
        String selectedTime = timeFormatter.format(calendar.getTime());
        timeEditText.setText(selectedTime);
    }

    private void submitApplication() {
        boolean hasError = false;

        // Сбрасываем ошибки
        resetErrors();

        // Проверяем заполнение всех полей
        if (nameEditText.getText().toString().trim().isEmpty()) {
            setError(nameInputLayout, "Введите имя владельца");
            hasError = true;
        }

        if (phoneEditText.getText().toString().trim().isEmpty()) {
            setError(phoneInputLayout, "Введите номер телефона");
            hasError = true;
        }

        if (carModelEditText.getText().toString().trim().isEmpty()) {
            setError(carModelInputLayout, "Введите модель автомобиля");
            hasError = true;
        }

        if (problemEditText.getText().toString().trim().isEmpty()) {
            setError(problemInputLayout, "Введите описание проблемы");
            hasError = true;
        }

        if (dateEditText.getText().toString().isEmpty()) {
            setError(dateInputLayout, "Выберите дату");
            hasError = true;
        }

        if (timeEditText.getText().toString().isEmpty()) {
            setError(timeInputLayout, "Выберите время");
            hasError = true;
        }

        if (hasError) {
            return;
        }

        // Сохраняем в базу данных
        long result = databaseHelper.addRepairRequest(
                nameEditText.getText().toString().trim(),
                phoneEditText.getText().toString().trim(),
                carModelEditText.getText().toString().trim(),
                problemEditText.getText().toString().trim(),
                dateEditText.getText().toString(),
                timeEditText.getText().toString()
        );

        if (result != -1) {
            Toast.makeText(this, "Заявка сохранена в базе данных! Статус: В работе", Toast.LENGTH_LONG).show();

            // Очищаем поля после успешного сохранения
            nameEditText.setText("");
            phoneEditText.setText("");
            carModelEditText.setText("");
            problemEditText.setText("");
            dateEditText.setText("");
            timeEditText.setText("");

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
        nameInputLayout.setErrorEnabled(false);
        phoneInputLayout.setErrorEnabled(false);
        carModelInputLayout.setErrorEnabled(false);
        problemInputLayout.setErrorEnabled(false);
        dateInputLayout.setErrorEnabled(false);
        timeInputLayout.setErrorEnabled(false);
    }

    private void navigateToStatistics() {
        Intent intent = new Intent(MainActivity2.this, MainActivity3.class);
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