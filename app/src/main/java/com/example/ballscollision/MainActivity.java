package com.example.ballscollision;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextSwitcher;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {


    private TextSwitcher ballCountSwitcher;
    private TextSwitcher collisionCountSwitcher;
    private Sensor accelerometer;
    private Sensor magnetometer;
    private SensorManager mSensorManager;
    private BallsCollisionView ballsCollisionView;
    private ImageButton clearView;
    private ImageButton stopView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        ballsCollisionView = findViewById(R.id.view_balls_collision);

        ballCountSwitcher = findViewById(R.id.text_switcher_ball_count);
        collisionCountSwitcher = findViewById(R.id.text_switcher_collision_count);
        clearView = findViewById(R.id.image_button_clear);
        clearView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ballsCollisionView.clearBalls();
                initTextSwitchers();
            }
        });

        stopView = findViewById(R.id.image_button_stop_balls);
        stopView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ballsCollisionView.stopBalls();
            }
        });

        initTextSwitchers();

        ballsCollisionView.setOnBallCollisionListener(new BallsCollisionView.OnBallCollision() {
            @Override
            public void onCollision() {
                TextView currentTextView = (TextView) collisionCountSwitcher.getCurrentView();
                final int currentCollisionCount = Integer.valueOf(currentTextView.getText().toString()) + 1;
                collisionCountSwitcher.setText(String.valueOf(currentCollisionCount));
            }
        });

        ballsCollisionView.setOnNewBallAddedListener(new BallsCollisionView.OnNewBallAdded() {
            @Override
            public void onAdd(int ballCount) {
                ballCountSwitcher.setText(String.valueOf(ballCount));
            }
        });
    }

    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_FASTEST);
    }

    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            final float[] acceleration = event.values;

            ballsCollisionView.setGravityX((-2)*acceleration[0]/SensorManager.GRAVITY_EARTH);
            ballsCollisionView.setGravityY(2*acceleration[1]/SensorManager.GRAVITY_EARTH);
        }
    }

    private void initTextSwitchers() {
        ballCountSwitcher.setText("0");
        collisionCountSwitcher.setText("0");
    }
}

