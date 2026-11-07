package com.example.coconut;

import android.content.Intent;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btnInjector).setOnClickListener(this);
        findViewById(R.id.btnColors).setOnClickListener(this);
        
        btnToggle = findViewById(R.id.btnToggle);
        btnToggle.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        
        if (id == R.id.btnInjector) {
            Toast.makeText(this, "Редактор (в разработке)", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.btnColors) {
            Toast.makeText(this, "Цвета (в разработке)", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.btnToggle) {
            toggleService();
        }
    }

    private void toggleService() {
        // Проверяем, включена ли наша служба оверлея
        if (!isAccessibilityServiceEnabled()) {
            Toast.makeText(this, "Включите COCONUT в Спец. возможностях!", Toast.LENGTH_LONG).show();
            // Открываем настройки спец. возможностей
            Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
            startActivity(intent);
        } else {
            // Служба включена, значит меню уже работает (или мы его перезапускаем)
            Toast.makeText(this, "Служба активна! Меню должно быть на экране.", Toast.LENGTH_SHORT).show();
            // Отправляем команду службе показать меню
            Intent intent = new Intent(this, OverlayService.class);
            intent.setAction("SHOW_MENU");
            startService(intent);
        }
    }

    private boolean isAccessibilityServiceEnabled() {
        String service = getPackageName() + "/" + OverlayService.class.getCanonicalName();
        try {
            String enabledServices = Settings.Secure.getString(getContentResolver(),
                    Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            return enabledServices != null && enabledServices.contains(service);
        } catch (Exception e) {
            return false;
        }
    }
}
