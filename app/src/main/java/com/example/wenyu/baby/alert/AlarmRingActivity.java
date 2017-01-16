package com.example.wenyu.baby.alert;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.wenyu.baby.Alarm;
import com.example.wenyu.baby.BaseActivity;
import com.example.wenyu.baby.Girl;
import com.example.wenyu.baby.R;
import com.example.wenyu.baby.database.DataBase_girl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AlarmRingActivity extends BaseActivity {

    private ImageView imageView;
    private Alarm alarm;
    private MediaPlayer mediaPlayer;

    private StringBuilder answerBuilder = new StringBuilder();

    private com.example.wenyu.baby.alert.MathProblem mathProblem;
    private Vibrator vibrator;
    private boolean alarmActive;
    ArrayList<String> beauty = new ArrayList<String>();
    ArrayList<String> tone = new ArrayList<String>();
    public List<Girl> girl;//取得 DB 妹子資料

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_alarm_ring);
        Bundle bundle = this.getIntent().getExtras();
        alarm = (Alarm) bundle.getSerializable("alarm");
        imageView = (ImageView)findViewById(R.id.imageView2);

        Log.d("aaa", String.valueOf(alarm.getGirlsID()));
        Log.d("aaa", String.valueOf(alarm.getAlarmPhotoPath()));
        DataBase_girl a= new DataBase_girl(getApplicationContext());
        girl =  a.getAll();
        for (Girl sublist : girl)
        {
            beauty.add(sublist.getGirlsPhotoPath());
            tone.add(sublist.getGirlRecordPath());
        }
        Bitmap bm = null;
        File file = new File(beauty.get(alarm.getGirlsID()));
        if (file.exists()) {
            bm = BitmapFactory.decodeFile(beauty.get(alarm.getGirlsID()));
        }
        imageView.setImageBitmap(bm);
        //this.setTitle(alarm.getAlarmName());
        Button delay = (Button) findViewById(R.id.delay);
        Button closed = (Button) findViewById(R.id.closed);
        closed.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                switch (alarm.getDifficulty()) {
                    case MATH:
                        intent.setClass(AlarmRingActivity.this, AlarmAlertActivity.class);
                        intent.putExtra("alarm", alarm);
                        //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        mediaPlayer.release();
                        AlarmRingActivity.this.finish();
                        break;
                    case COCKROACH:
                        intent.setClass(AlarmRingActivity.this, HitCockroach.class);
                        startActivity(intent);
                        mediaPlayer.release();
                        AlarmRingActivity.this.finish();
                        break;
                    case TYPE:
                        intent.setClass(AlarmRingActivity.this,KeyWordGameActivity.class);
                        startActivity(intent);
                        mediaPlayer.release();
                        AlarmRingActivity.this.finish();
                        break;
                    case SHAKE:
                        intent.setClass(AlarmRingActivity.this,ShakeShakeGameActivity.class);
                        startActivity(intent);
                        mediaPlayer.release();
                        AlarmRingActivity.this.finish();
                        break;
                    case GUESS:
                        intent.setClass(AlarmRingActivity.this, TalkGame.class);
                        startActivity(intent);
                        mediaPlayer.release();
                        AlarmRingActivity.this.finish();
                        break;
                }
            }
        });
        delay.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                snooze();
                Toast.makeText(getApplicationContext(), "鬧鐘將延遲 10 分鐘", Toast.LENGTH_LONG).show();
                mediaPlayer.release();
                AlarmRingActivity.this.finish();
            }
        });
        startAlarm();
    }

    @Override
    protected void onResume() {
        super.onResume();
        alarmActive = true;
    }

    private void startAlarm() {
        mediaPlayer = new MediaPlayer();
        if (alarm.getAlarmTonePath() != "") {
            if (alarm.getVibrate()) {
                vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                long[] pattern = { 1000, 200, 200, 200 };
                vibrator.vibrate(pattern, 0);
            }

                mediaPlayer.setVolume(1.0f, 1.0f);
                try {
                    mediaPlayer.setDataSource(tone.get(alarm.getGirlsID()));
                    mediaPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mediaPlayer.setLooping(true);
                mediaPlayer.start();
        }

    }

    /*
     * (non-Javadoc)
     *
     * @see android.app.Activity#onBackPressed()
     */
    @Override
    public void onBackPressed() {
        if (!alarmActive)
            super.onBackPressed();
    }

    /*
     * (non-Javadoc)
     *
     * @see android.app.Activity#onPause()
     */
    @Override
    protected void onPause() {
        super.onPause();
        com.example.wenyu.baby.alert.StaticWakeLock.lockOff(this);
    }

    @Override
    protected void onDestroy() {
        try {
            if (vibrator != null)
                vibrator.cancel();
        } catch (Exception e) {

        }
        try {
            mediaPlayer.stop();
        } catch (Exception e) {

        }
        try {
            mediaPlayer.release();
        } catch (Exception e) {

        }
        super.onDestroy();
    }
    private void snooze()
    {
        int SNOOZE_MIN = 2;
        //Set Calendar Value for Snooze Alarm
        Calendar calendar = Calendar.getInstance();
        //int snoozeTime = mMinute + SNOOZE_MIN;
        calendar.add(Calendar.MINUTE, SNOOZE_MIN); //SNOOZE_MIN = 1;
        long snoozeTime = calendar.getTimeInMillis();
        //Build Intent and Pending Intent to Set Snooze Alarm
        Intent AlarmIntent = new Intent(AlarmRingActivity.this,AlarmAlertBroadcastReciever.class);
        AlarmManager AlmMgr = (AlarmManager)getSystemService(ALARM_SERVICE);
        AlarmIntent.putExtra("alarm", alarm);
        PendingIntent Sender = PendingIntent.getBroadcast(AlarmRingActivity.this,0, AlarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlmMgr.set(AlarmManager.RTC_WAKEUP, snoozeTime, Sender);


    }
}
