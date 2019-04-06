package com.example.ballscollision;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;

import java.util.concurrent.ThreadLocalRandom;

public class Ball {

    public double vx;
    public double vy;
    private float mass;
    private double centerX;
    private double centerY;
    private double radius;
    private int color;
    private double spring = 0.7f;
    private float bounce = -0.8f;
    private double gravityX = 0.0f;
    private double gravityY = 0.0f;
    private Paint paint;
    private Bitmap bitmap;
    private RectF bitmapRectF;


    public Ball(float centerX, float centerY) {
        this.vx = (float)ThreadLocalRandom.current().nextDouble(0,10f);
        this.vy = (float)ThreadLocalRandom.current().nextDouble(0,10f);
        this.radius = (float) ThreadLocalRandom.current().nextDouble(30f, 80f);
        this.mass = (float) Math.pow(this.radius, 1.0 / 3.0);
        this.color = Color.argb(255, ThreadLocalRandom.current().nextInt(255), ThreadLocalRandom.current().nextInt(255), ThreadLocalRandom.current().nextInt(255));
        this.centerX = centerX;
        this.centerY = centerY;
    }

    public Ball(float centerX, float centerY, Paint paint) {
        this.vx = 0;
        this.vy = 0;
        this.radius = (float) ThreadLocalRandom.current().nextDouble(30, 80);
        this.mass = (float) Math.pow(this.radius, 1.0 / 3.0);
        this.color = Color.argb(255, ThreadLocalRandom.current().nextInt(255), ThreadLocalRandom.current().nextInt(255), ThreadLocalRandom.current().nextInt(255));
        this.centerX = centerX;
        this.centerY = centerY;
        this.paint = paint;
        this.paint.setColor(this.color);
    }

    public Ball(float centerX, float centerY, Bitmap bitmap) {
        this.vx = 0;
        this.vy = 0;
        this.radius = (float) ThreadLocalRandom.current().nextDouble(30, 80);
        this.mass = (float) Math.pow(this.radius, 1.0 / 3.0);
        this.color = Color.argb(255, ThreadLocalRandom.current().nextInt(255), ThreadLocalRandom.current().nextInt(255), ThreadLocalRandom.current().nextInt(255));
        this.centerX = centerX;
        this.centerY = centerY;
        this.bitmap = bitmap;
    }


    public Paint getPaint() {
        return paint;
    }

    public void setPaint(Paint paint) {
        this.paint = paint;
    }


    public void draw(Canvas canvas) {
        if (null == bitmap) {
            canvas.drawCircle((float) centerX, (float) centerY, (float) radius, paint);
        } else {
            bitmapRectF = new RectF((float) (centerX - radius), (float) (centerY - radius), (float) (centerX + radius), (float) (centerY + radius));
            canvas.drawBitmap(bitmap, null, bitmapRectF, null);
        }
    }

    public double getCenterX() {
        return centerX;
    }

    public void setCenterX(double centerX) {
        this.centerX = centerX;
    }

    public double getCenterY() {
        return centerY;
    }

    public void setCenterY(double centerY) {
        this.centerY = centerY;
    }

    public int getColor() {
        return color;
    }

    public double getRadius() {
        return radius;
    }

    public double getSpring() {
        return spring;
    }

    public double getVx() {
        return vx;
    }

    public void setVx(double vx) {
        this.vx = vx;
    }

    public double getVy() {
        return vy;
    }

    public void setVy(double vy) {
        this.vy = vy;
    }

    public float getMass() {
        return mass;
    }

    public void calculateBallIsOutOfBounds(float width, float height) {
        double edgeDistance = centerX + radius - width;
        if (edgeDistance > 0) {
            centerX = width - radius;
            vx *= bounce;
        }
        edgeDistance = this.centerX - radius;
        if (edgeDistance < 0) {
            centerX = radius;
            vx *= bounce;
        }
        edgeDistance = centerY + radius - height;
        if (edgeDistance > 0) {
            centerY = height - radius;
            vy *= bounce;
        }
        edgeDistance = centerY - radius;
        if (edgeDistance < 0) {
            centerY = radius;
            vy *= bounce;
        }
    }


    public void calculateNextPositionOfBall() {
        this.vy += gravityY;
        this.vx += gravityX;
        this.centerX += vx;
        this.centerY += vy;
    }

    public void setGravityY(double gravityY) {
        this.gravityY = gravityY;
    }

    public void setGravityX(double gravityX) {
        this.gravityX = gravityX;
    }

    public void setBallGravity(double gravityX, double gravityY, double gravityConst){
            this.gravityX = gravityX*gravityConst;
            this.gravityY = gravityY*gravityConst;
    }
}
