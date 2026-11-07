package com.example.coconut;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnToggle;
    private boolean isEnabled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btnInjector).setOnClickListener(this);
        findViewById(R.id.btnColors).setOnClickListener(this);
        
        btnToggle = findViewById(R.id.btnToggle);
        btnToggle.setOnClickListener(this);

        // Проверка разрешения на оверлей
        if (!Settings.canDrawOverlays(this)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivity(intent);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        
        if (id == R.id.btnInjector) {
            // Открываем экран для написания скрипта
            Intent intent = new Intent(MainActivity.this, InjectorActivity.class);
            startActivity(intent);
        } else if (id == R.id.btnColors) {
            Toast.makeText(this, "Настройки цветов (в разработке)", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.btnToggle) {
            toggleState();
        }
    }

    private void toggleState() {
        if (!isEnabled) {
            // ПРОВЕРКА: есть ли сохраненный скрипт?
            SharedPreferences prefs = getSharedPreferences("CheatPrefs", MODE_PRIVATE);
            String script = prefs.getString("my_script", "");
            
            if (script.isEmpty()) {
                Toast.makeText(this, "Сначала напишите скрипт в ИНЖЕКТОРЕ!", Toast.LENGTH_LONG).show();
                return; // Не включаем, если скрипта нет
            }

            // Включаем
            isEnabled = true;
            btnToggle.setText("ЧИТ АКТИВЕН");
            btnToggle.setBackgroundTintList(android.content.res.ColorStateList.valueOf(Color.RED));
            Toast.makeText(this, "Инжектор активирован", Toast.LENGTH_SHORT).show();
            
            // Запускаем службу
            Intent intent = new Intent(this, OverlayService.class);
            intent.setAction("SHOW_MENU");
            startService(intent);
        } else {
            // Выключаем
            isEnabled = false;
            btnToggle.setText("ВКЛ/ВЫКЛ");
            btnToggle.setBackgroundTintList(android.content.res.ColorStateList.valueOf(Color.parseColor("#FF9800")));
            Toast.makeText(this, "Инжектор отключен", Toast.LENGTH_SHORT).show();
            
            // Останавливаем службу (если она запущена)
            Intent intent = new Intent(this, OverlayService.class);
            stopService(intent);
        }
    }
}
