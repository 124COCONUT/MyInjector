package com.example.coconut;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

public class OverlayService extends AccessibilityService {

    private WindowManager windowManager;
    private LinearLayout floatingMenu;
    private boolean isMenuVisible = false;

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        // Нам не нужно отслеживать события, мы просто используем службу для оверлея
    }

    @Override
    public void onInterrupt() {}

    @Override
    public void onServiceConnected() {
        super.onServiceConnected();
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && "SHOW_MENU".equals(intent.getAction())) {
            showMenu();
        }
        return START_STICKY;
    }

    private void showMenu() {
        if (isMenuVisible) {
            // Если меню уже висит, прячем его
            if (floatingMenu != null && windowManager != null) {
                windowManager.removeView(floatingMenu);
                floatingMenu = null;
                isMenuVisible = false;
            }
            return;
        }

        if (windowManager == null) return;

        floatingMenu = new LinearLayout(this);
        floatingMenu.setOrientation(LinearLayout.VERTICAL);
        floatingMenu.setBackgroundColor(Color.parseColor("#E61A1A1A"));
        floatingMenu.setPadding(40, 40, 40, 40);

        TextView title = new TextView(this);
        title.setText("HACK MENU (Accessibility)");
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
        closeBtn.setOnClickListener(v -> {
            if (windowManager != null && floatingMenu != null) {
                windowManager.removeView(floatingMenu);
                floatingMenu = null;
                isMenuVisible = false;
            }
        });
        floatingMenu.addView(closeBtn);

        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY, // САМЫЙ ВАЖНЫЙ ФЛАГ!
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
        );
        params.gravity = Gravity.CENTER;

        windowManager.addView(floatingMenu, params);
        isMenuVisible = true;
    }
}
