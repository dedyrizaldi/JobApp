package com.example.lokerapp.tools;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;

import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.FrameLayout;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.view.MenuItemCompat;

import com.google.android.material.navigation.NavigationView;

public class DrawerBadge extends AppCompatTextView {

    private float strokeWidth;

    int strokeColor = Color.parseColor("#000000"); // black

    int solidColor = Color.parseColor("#FF0000"); // red

    // **** THIS IS THE FULL CONSTRUCTOR YOU HAVE TO CALL ****
    public DrawerBadge(Context context, NavigationView navigationView, int idItem, String value, String letterColor, String strokeColor, String solidColor) {
        super(context);
        MenuItemCompat.setActionView(navigationView
                .getMenu().findItem(idItem), this);
        DrawerBadge badge = (DrawerBadge) MenuItemCompat
                .getActionView(navigationView
                        .getMenu().findItem(idItem));
        badge.setGravity(Gravity.CENTER);
        badge.setTypeface(null, Typeface.BOLD);
        badge.setTextColor(Color.parseColor(letterColor));
        badge.setText(value);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,FrameLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        badge.setLayoutParams(params);
        badge.setPadding(3,3,3,3);
        badge.setStrokeWidth(1);
        badge.setStrokeColor(strokeColor);
        badge.setSolidColor(solidColor);
        badge.requestLayout();
    }

    public DrawerBadge(Context context) {
        super(context);
    }

    public DrawerBadge(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DrawerBadge(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void draw(Canvas canvas) {

        Paint circlePaint = new Paint();
        circlePaint.setColor(solidColor);
        circlePaint.setFlags(Paint.ANTI_ALIAS_FLAG);

        Paint strokePaint = new Paint();
        strokePaint.setColor(strokeColor);
        strokePaint.setFlags(Paint.ANTI_ALIAS_FLAG);

        int  h = this.getHeight();
        int  w = this.getWidth();

        int diameter = ((h > w) ? h : w);
        int radius = diameter/2;

        this.setHeight(diameter);
        this.setWidth(diameter);

        canvas.drawCircle(diameter / 2 , diameter / 2, radius, strokePaint);

        canvas.drawCircle(diameter / 2, diameter / 2, radius-strokeWidth, circlePaint);

        super.draw(canvas);
    }

    public void setStrokeWidth(int dp)
    {
        float scale = getContext().getResources().getDisplayMetrics().density;
        strokeWidth = dp*scale;

    }

    public void setStrokeColor(String color)
    {
        strokeColor = Color.parseColor(color);
    }

    public void setSolidColor(String color)
    {
        solidColor = Color.parseColor(color);

    }
}
