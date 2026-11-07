package com.example.coconut;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnToggle;
    private WindowManager windowManager;
    private LinearLayout floatingMenu;
    private boolean isEnabled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Запрашиваем разрешение на отображение поверх других приложений
        if (!Settings.canDrawOverlays(this)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivity(intent);
        }

        findViewById(R.id.btnInjector).setOnClickListener(this);
        findViewById(R.id.btnColors).setOnClickListener(this);
        
        btnToggle = findViewById(R.id.btnToggle);
        btnToggle.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        
        if (id == R.id.btnInjector) {
            Toast.makeText(this, "Открыт редактор кода (в разработке)", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.btnColors) {
            Toast.makeText(this, "Настройки цветов (в разработке)", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.btnToggle) {
            toggleState();
        }
    }

    private void toggleState() {
        if (isEnabled) {
            // Выключаем
            isEnabled = false;
            btnToggle.setText("ВКЛ/ВЫКЛ");
            btnToggle.setBackgroundTintList(android.content.res.ColorStateList.valueOf(Color.parseColor("#FF9800")));
            Toast.makeText(this, "Инжектор отключен", Toast.LENGTH_SHORT).show();
            
            if (windowManager != null && floatingMenu != null) {
                windowManager.removeView(floatingMenu);
            }
        } else {
            // Включаем
            isEnabled = true;
            btnToggle.setText("ЧИТ АКТИВЕН");
            btnToggle.setBackgroundTintList(android.content.res.ColorStateList.valueOf(Color.RED));
            Toast.makeText(this, "Инжектор активирован", Toast.LENGTH_SHORT).show();
            
            showFloatingMenu();
        }
    }

    private void showFloatingMenu() {
        if (floatingMenu != null) return;

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        
        floatingMenu = new LinearLayout(this);
        floatingMenu.setOrientation(LinearLayout.VERTICAL);
        floatingMenu.setBackgroundColor(Color.parseColor("#E61A1A1A"));
        floatingMenu.setPadding(40, 40, 40, 40);

        TextView title = new TextView(this);
        title.setText("HACK MENU");
        title.setTextColor(Color.GREEN);
        title.setTextSize(20);
        title.setGravity(Gravity.CENTER);
        floatingMenu.addView(title);

        CheckBox espBox = new CheckBox(this);
        espBox.setText("ESP (Wallhack)");
        espBox.setTextColor(Color.WHITE);
        floatingMenu.addView(espBox);

        CheckBox aimBox = new CheckBox(this);
        aimBox.setText("Aimbot (Silent)");
        aimBox.setTextColor(Color.WHITE);
        floatingMenu.addView(aimBox);

        Button closeBtn = new Button(this);
        closeBtn.setText("ЗАКРЫТЬ");
        closeBtn.setBackgroundColor(Color.RED);
        closeBtn.setTextColor(Color.WHITE);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (windowManager != null && floatingMenu != null) {
                    windowManager.removeView(floatingMenu);
                }
            }
        });
        floatingMenu.addView(closeBtn);

        int layoutFlag = (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) 
            ? WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY 
            : WindowManager.LayoutParams.TYPE_PHONE;

        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                layoutFlag,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
        );
        params.gravity = Gravity.CENTER;
        
        try {
            windowManager.addView(floatingMenu, params);
        } catch (Exception e) {
            Toast.makeText(this, "Ошибка окна: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
