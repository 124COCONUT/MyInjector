package com.example.coconut;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class InjectorActivity extends AppCompatActivity {

    private EditText scriptInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_injector);

        scriptInput = findViewById(R.id.scriptInput);
        Button saveBtn = findViewById(R.id.saveScriptBtn);

        // Загружаем сохраненный скрипт, если он есть
        SharedPreferences prefs = getSharedPreferences("CheatPrefs", MODE_PRIVATE);
        String savedScript = prefs.getString("my_script", "");
        scriptInput.setText(savedScript);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String script = scriptInput.getText().toString();
                SharedPreferences.Editor editor = getSharedPreferences("CheatPrefs", MODE_PRIVATE).edit();
                editor.putString("my_script", script);
                editor.apply();
                
                Toast.makeText(InjectorActivity.this, "Скрипт сохранен! Длина: " + script.length(), Toast.LENGTH_SHORT).show();
                finish(); // Закрываем экран и возвращаемся
            }
        });
    }
}
