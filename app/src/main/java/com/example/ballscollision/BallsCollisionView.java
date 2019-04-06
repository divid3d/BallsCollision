package com.example.ballscollision;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;


public class BallsCollisionView extends View {

    private final int maxBallCount = 50;
    private final double gravityConst = 1;
    public double gravityX = 0.0;
    public double gravityY = 0.0;
    private List<Ball> ballList = new ArrayList<>();
    private OnBallCollision onBallCollisionListener;
    private OnNewBallAdded onNewBallAddedListener;


    public BallsCollisionView(Context context) {
        this(context, null);
    }

    public BallsCollisionView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public void clearBalls(){
        ballList.clear();
    }

    public BallsCollisionView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setOnBallCollisionListener(OnBallCollision listener) {
        this.onBallCollisionListener = listener;
    }

    public void setOnNewBallAddedListener(OnNewBallAdded listener) {
        this.onNewBallAddedListener = listener;
    }

    private boolean ballIntersectList(Ball ball, List<Ball> ballList) {
        for (Ball anotherBall : ballList) {
            if (ballIntersect(ball, anotherBall)) {
                return true;
            }
        }
        return false;
    }

    private boolean ballIntersect(Ball ball, Ball anotherBall) {
        double dx = anotherBall.getCenterX() - ball.getCenterX();
        double dy = anotherBall.getCenterY() - ball.getCenterY();
        double dist = Math.sqrt(dx * dx + dy * dy);
        return ball.getRadius() + anotherBall.getRadius() >= dist;
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        hitBall();
        for (Ball ball : ballList) {
            ball.setBallGravity(gravityX, gravityY, gravityConst);
            ball.calculateNextPositionOfBall();
            ball.calculateBallIsOutOfBounds(getWidth(), getHeight());
        }
        for (Ball ball : ballList) {
            ball.draw(canvas);
        }

        postInvalidate();
    }


    public void hitBall() {
        int num = ballList.size();
        for (int i = 0; i < num; i++) {
            Ball ball = ballList.get(i);
            for (int j = i + 1; j < num; j++) {
                Ball anotherBall = ballList.get(j);
                double dx = anotherBall.getCenterX() - ball.getCenterX();
                double dy = anotherBall.getCenterY() - ball.getCenterY();
                double dist = Math.sqrt((dx * dx) + (dy * dy));
                double misDist = ball.getRadius() + anotherBall.getRadius();
                if (dist < misDist) {
                    /*if (onBallCollisionListener != null) {
                        onBallCollisionListener.onCollision();
                    }*/
                    double angle = Math.atan2(dy, dx);
                    double tx = ball.getCenterX() + Math.cos(angle) * misDist;
                    double ty = ball.getCenterY() + Math.sin(angle) * misDist;
                    double ax = (tx - anotherBall.getCenterX()) * ball.getSpring();
                    double ay = (ty - anotherBall.getCenterY()) * ball.getSpring();
                    ball.vx -= ax;
                    ball.vy -= ay;
                    anotherBall.vx += ax;
                    anotherBall.vy += ay;
                }
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            addNewBall(x, y);
            invalidate();
        }
        return true;
    }

    private void addNewBall(float x, float y) {
        if (ballList.size() < maxBallCount) {

            Ball ball = new Ball(x, y, new Paint());
            if (!ballIntersectList(ball, ballList)) {
                ballList.add(ball);
            }
            if (onNewBallAddedListener != null) {
                onNewBallAddedListener.onAdd(ballList.size());
            }
        }
    }

    public void setGravityX(double gravityX) {
        this.gravityX = gravityX;
    }

    public void setGravityY(double gravityY) {
        this.gravityY = gravityY;
    }


    public interface OnNewBallAdded {
        void onAdd(int ballCount);
    }


    public interface OnBallCollision {
        void onCollision();
    }

    public void  stopBalls(){
        for(Ball ball: ballList){
            ball.setVx(0);
            ball.setVy(0);
        }
    }
}
