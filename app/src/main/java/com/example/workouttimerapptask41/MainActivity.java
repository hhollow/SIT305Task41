package com.example.workouttimerapptask41;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.CountDownTimer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {
    TextView displayCountDownWorkout, displayCountDownRest, displaySetNumber;
    Button buttonStartStop;
    EditText userInputWorkout, userInputRest, userInputSets;
    CountDownTimer countDownTimerWorkout, countDownTimerRest;
    ProgressBar progressBarWorkout, progressBarRest;
    boolean timerRunningWorkout, timerRunningRest;
    long timeRemainingWorkout, timeRemainingRest;
    int setCount = 0;
    int sets;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        displayCountDownWorkout = (TextView)  findViewById(R.id.displayCountDownWorkout);
        displayCountDownRest = (TextView)  findViewById(R.id.displayCountDownRest);
        displaySetNumber = (TextView) findViewById(R.id.displaySetNumber);
        buttonStartStop = (Button) findViewById(R.id.button_start_pause);
        userInputWorkout = (EditText) findViewById(R.id.userInputWorkout);
        userInputRest = (EditText) findViewById(R.id.userInputRest);
        userInputSets = (EditText) findViewById(R.id.userInputSets);
        progressBarWorkout = (ProgressBar) findViewById(R.id.progressBarWorkout);
        progressBarRest = (ProgressBar) findViewById(R.id.progressBarRest);


        buttonStartStop.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                //Check each input field for values and warn if empty
                if (userInputWorkout.length() <1 ){
                    Toast.makeText(MainActivity.this, "Enter Workout Time", Toast.LENGTH_SHORT).show();
                }
                if (userInputRest.length() <1 ){
                    Toast.makeText(MainActivity.this, "Enter Rest Time", Toast.LENGTH_SHORT).show();
                }
                if (userInputSets.length() <1 ) {
                    Toast.makeText(MainActivity.this, "Enter Number of Sets", Toast.LENGTH_SHORT).show();
                }
                else if (userInputWorkout != null && userInputRest != null && userInputSets !=null && !timerRunningWorkout && !timerRunningRest) {
                    // Set number of sets to run through
                    sets = Integer.parseInt((userInputSets.getText().toString()));
                    // run the timer
                    startWorkoutTimer();
                    buttonStartStop.setText("Stop");
                    setCount = 0;
                    displaySetNumber.setText(String.valueOf(setCount));
                }
                else if (userInputWorkout != null && userInputRest != null && userInputSets != null) {
                    // Stop the timer
                    resetWorkoutTimer();
                    resetRestTimer();
                    buttonStartStop.setText("Start");
                    setCount = 0;
                    displaySetNumber.setText(String.valueOf(setCount));
                    progressBarWorkout.setProgress(0);
                    progressBarRest.setProgress(0);
                }
            }
        });
    }


    // This method switches the user inputs to milliseconds
    public long minsToMilliseconds(int timeInput){
        return timeInput * 60000L;
    }

    // Method for the workout timer. The rest phase is automatically called at the end of this method
    private void startWorkoutTimer() {
        int inputValue = Integer.parseInt((userInputWorkout.getText().toString()));
        long startTimeWorkout = minsToMilliseconds(inputValue);
        timeRemainingWorkout = minsToMilliseconds(inputValue);
        final int[] i = {0};
        progressBarWorkout.setProgress(i[0]);
        countDownTimerWorkout = new CountDownTimer(timeRemainingWorkout, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeRemainingWorkout = millisUntilFinished;
                updateWorkoutDisplayCountdown();
                i[0]++;
                progressBarWorkout.setProgress((int) ((int) i[0] *100/(startTimeWorkout/1000)));
            }
            @Override
            public void onFinish() {
                timerRunningWorkout = false;
                i[0]++;
                progressBarWorkout.setProgress(0);
                startRestTimer();
            }
        }.start();
        timerRunningWorkout = true;
    }

    // Rest phase method, this method will call the workout timer again on the condition that there are remaining sets to be completed
    private void startRestTimer() {
        int inputValueRest = Integer.parseInt((userInputRest.getText().toString()));
        long startTimeRest = minsToMilliseconds(inputValueRest);
        timeRemainingRest = minsToMilliseconds(inputValueRest);
        final int[] j = {0};
        progressBarRest.setProgress(j[0]);
        countDownTimerRest = new CountDownTimer(timeRemainingRest, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeRemainingRest = millisUntilFinished;
                updateRestDisplayCountdown();
                j[0]++;
                progressBarRest.setProgress((int) ((int) j[0] *100/(startTimeRest/1000)));
            }
            @SuppressLint("SetTextI18n")
            @Override
            public void onFinish() {
                timerRunningRest = false;
                j[0]++;
                progressBarRest.setProgress(0);
                setCount = setCount + 1;
                displaySetNumber.setText(String.valueOf(setCount));
                if(setCount < sets){
                    startWorkoutTimer();
                } else {
                    buttonStartStop.setText("Start");
                }
            }
        }.start();
        timerRunningRest = true;
    }

    // Methods to stop and reset both timers
    private void resetWorkoutTimer() {
        if(countDownTimerWorkout != null) {
            countDownTimerWorkout.cancel();
        }
        timerRunningWorkout = false;
        timeRemainingWorkout = 0;
        setCount = 0;
        displaySetNumber.setText(String.valueOf(setCount));
        updateWorkoutDisplayCountdown();
    }
    private void resetRestTimer() {
        if(countDownTimerRest != null) {
            countDownTimerRest.cancel();
        }
        timerRunningRest = false;
        timeRemainingRest = 0;
        setCount = 0;
        displaySetNumber.setText(String.valueOf(setCount));
        updateRestDisplayCountdown();
    }

    // These methods control the countdown displays to show the times remaining for each timer.
    private void updateWorkoutDisplayCountdown() {
        int minutes = (int) (timeRemainingWorkout / 1000) / 60;
        int seconds = (int) (timeRemainingWorkout / 1000) % 60;
        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        displayCountDownWorkout.setText(timeLeftFormatted);
    }
    private void updateRestDisplayCountdown() {
        int minutes = (int) (timeRemainingRest / 1000) / 60;
        int seconds = (int) (timeRemainingRest / 1000) % 60;
        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        displayCountDownRest.setText(timeLeftFormatted);
    }
}