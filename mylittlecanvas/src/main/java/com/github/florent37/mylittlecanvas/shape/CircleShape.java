package com.github.florent37.mylittlecanvas.shape;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Canvas;

public class CircleShape extends Shape {

    private int radius;
    private int centerX;
    private int centerY;

    private int borderColor;
    private int borderWidth;

    public CircleShape setRadius(final int radius) {
        this.radius = radius;
        return this;
    }

    public CircleShape setRadius(final float radius) {
        this.radius = (int) radius;
        return this;
    }

    public CircleShape setCenterX(final int centerX) {
        this.centerX = centerX;
        return this;
    }

    public CircleShape setCenterY(final int centerY) {
        this.centerY = centerY;
        return this;
    }

    public CircleShape setCenterX(final float centerX) {
        this.centerX = (int) centerX;
        return this;
    }

    public CircleShape setCenterY(final float centerY) {
        this.centerY = (int) centerY;
        return this;
    }

    public ValueAnimator animateCenterX(float...values){
        return ObjectAnimator.ofFloat(this, "centerX", values);
    }

    public ValueAnimator animateCenterY(float...values){
        return ObjectAnimator.ofFloat(this, "centerY", values);
    }

    public ValueAnimator animateRadius(float...values){
        return ObjectAnimator.ofFloat(this, "radius", values);
    }

    public CircleShape setBorderColor(final int borderColor) {
        this.borderColor = borderColor;
        return this;
    }

    public CircleShape setBorderWidth(final int borderWidth) {
        this.borderWidth = borderWidth;
        return this;
    }

    @Override
    protected void draw(final Canvas canvas) {
        if(borderWidth > 0) {
            int color = paint.getColor();
            paint.setColor(borderColor);
            canvas.drawCircle(centerX, centerY, radius - borderWidth, paint);
            paint.setColor(color);
        }
        canvas.drawCircle(centerX, centerY, radius, paint);
    }

    public int getRadius() {
        return radius;
    }

    public int getCenterX() {
        return centerX;
    }

    public int getCenterY() {
        return centerY;
    }

    @Override
    public boolean containsTouch(float x, float y) {
        return Math.pow(x-centerX, 2) + Math.pow(y - centerY, 2) < Math.pow(radius, 2);
    }

    public int getBorderColor() {
        return borderColor;
    }

    public int getBorderWidth() {
        return borderWidth;
    }

    @Override
    public CircleShape setColor(int color) {
        return (CircleShape) super.setColor(color);
    }
}
