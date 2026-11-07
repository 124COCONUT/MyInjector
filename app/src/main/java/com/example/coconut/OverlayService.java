package com.example.coconut;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

public class OverlayService extends AccessibilityService {

    private WindowManager windowManager;
    private LinearLayout floatingMenu;
    private LinearLayout menuContent;
    private boolean isMenuVisible = false;
    private boolean isCollapsed = false;
    private WindowManager.LayoutParams params;

    private int initialX;
    private int initialY;
    private float initialTouchX;
    private float initialTouchY;

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {}

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
            if (windowManager != null && floatingMenu != null) {
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
        floatingMenu.setPadding(15, 15, 15, 15);

        // --- Заголовок (за него будем таскать) ---
        LinearLayout headerLayout = new LinearLayout(this);
        headerLayout.setOrientation(LinearLayout.HORIZONTAL);
        headerLayout.setGravity(Gravity.CENTER_VERTICAL);

        TextView title = new TextView(this);
        title.setText("MY CHEAT PANEL");
        title.setTextColor(Color.GREEN);
        title.setTextSize(16);
        title.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
        headerLayout.addView(title);

        // Кнопка свернуть
        Button collapseBtn = new Button(this);
        collapseBtn.setText("—");
        collapseBtn.setTextSize(14);
        collapseBtn.setPadding(20, 0, 20, 0);
        collapseBtn.setBackgroundColor(Color.DKGRAY);
        collapseBtn.setTextColor(Color.WHITE);
        collapseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleCollapse();
            }
        });
        headerLayout.addView(collapseBtn);

        floatingMenu.addView(headerLayout);

        // --- Контент ---
        menuContent = new LinearLayout(this);
        menuContent.setOrientation(LinearLayout.VERTICAL);
        menuContent.setVisibility(View.VISIBLE);

        CheckBox espBox = new CheckBox(this);
        espBox.setText("ESP (Wallhack)");
        espBox.setTextColor(Color.WHITE);
        menuContent.addView(espBox);

        CheckBox aimBox = new CheckBox(this);
        aimBox.setText("Aimbot (Silent)");
        aimBox.setTextColor(Color.WHITE);
        menuContent.addView(aimBox);

        Button closeBtn = new Button(this);
        closeBtn.setText("ЗАКРЫТЬ");
        closeBtn.setBackgroundColor(Color.RED);
        closeBtn.setTextColor(Color.WHITE);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (windowManager != null && floatingMenu != null) {
                    windowManager.removeView(floatingMenu);
                    floatingMenu = null;
                    isMenuVisible = false;
                }
            }
        });
        menuContent.addView(closeBtn);

        floatingMenu.addView(menuContent);

        // --- Настройки окна ---
        params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
        );
        params.gravity = Gravity.TOP | Gravity.START;
        params.x = 100;
        params.y = 200;

        // --- ДОБАВЛЯЕМ ПЕРЕТАСКИВАНИЕ ---
        headerLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        initialX = params.x;
                        initialY = params.y;
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        params.x = initialX + (int) (event.getRawX() - initialTouchX);
                        params.y = initialY + (int) (event.getRawY() - initialTouchY);
                        if (windowManager != null) {
                            windowManager.updateViewLayout(floatingMenu, params);
                        }
                        return true;
                }
                return false;
            }
        });

        windowManager.addView(floatingMenu, params);
        isMenuVisible = true;
    }

    private void toggleCollapse() {
        if (isCollapsed) {
            menuContent.setVisibility(View.VISIBLE);
            isCollapsed = false;
        } else {
            menuContent.setVisibility(View.GONE);
            isCollapsed = true;
        }
    }
}
