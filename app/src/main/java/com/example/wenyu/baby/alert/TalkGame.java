package com.example.wenyu.baby.alert;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wenyu.baby.R;

import java.util.ArrayList;

public class TalkGame extends AppCompatActivity implements RecognitionListener {
    //private TextView returnedText;
    private ProgressBar progressBar;
    private SpeechRecognizer speech = null;
    private Intent recognizerIntent;
    private String LOG_TAG = "VoiceRecognitionActivity";
    private String[] questions = {"人一田入加一筆\n是什麼?", "小雞和小狗和小貓\n哪一個會先被抓走?","紅豆的小孩是誰？","豬七戒的弟弟是???","每個人都有念錯字\n的時候，什麼字\n一定會念錯?","小白+小白=？","真正的豬喝的是什麼奶茶?"};
    private String[] answers = {"便","小狗","南國","豬八戒","錯","小白兔","珍珠奶茶"};
    private String[] tips = {"大~?(猜一字)","一種餅乾的名字","一首詩","一個人物","妹子知道但妹子不說~","!!","妹子知道但妹子不說~"};

    private TextView tip;
    private TextView question;
    private SoundPool soundPool ;

    private Handler time_handler = new Handler();
    int randomNum;
    public MediaPlayer mp ;

    private boolean guess = false;

    Button startButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_talk_game);

        randomNum = (int) (Math.random() * 6);

        soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 5);
        //sneezeId = soundPool.load(this, R.raw.hit, 1);
        mp =MediaPlayer.create(this, R.raw.background_sound);
        mp.setLooping(true);
        mp.start();

        startButton = (Button) findViewById(R.id.startButton);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mp.release();
                speech.startListening(recognizerIntent);//start_voice
            }
        });

        tip = (TextView) findViewById(R.id.textView1);
        tip.setText("*小提示: " + tips[randomNum]);

        question = (TextView) findViewById(R.id.questions);
        question.setText(questions[randomNum]);

        //mp.start();
        //Toast.makeText(getApplicationContext(), answers[randomNum], Toast.LENGTH_LONG).show();
        //returnedText = (TextView) findViewById(R.id.textView1);
        progressBar = (ProgressBar) findViewById(R.id.progressBar1);

        speech = SpeechRecognizer.createSpeechRecognizer(this);
        speech.setRecognitionListener(this);
        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "en");
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, this.getPackageName());
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3);
    }
    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (speech != null) {
            speech.destroy();
        }
    }
    ////////Beginning
    @Override
    public void onBeginningOfSpeech() {
        // mp.stop();
        progressBar.setIndeterminate(false);
        progressBar.setMax(10);
        //if beginning voice record , delay 3 seconds do stop
        time_handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                speech.stopListening();//stop_voice

            }
        }, 1500);
    }

    @Override
    public void onBufferReceived(byte[] buffer) {

    }
    ////////End
    @Override
    public void onEndOfSpeech() {
        progressBar.setIndeterminate(true);
        mp =MediaPlayer.create(this, R.raw.background_sound);
        mp.setLooping(true);
        mp.start();
    }

    @Override
    public void onError(int errorCode) {
        String errorMessage = getErrorText(errorCode);

        //returnedText.setText(errorMessage);
    }

    @Override
    public void onEvent(int arg0, Bundle arg1) {

    }

    @Override
    public void onPartialResults(Bundle arg0) {

    }

    @Override
    public void onReadyForSpeech(Bundle arg0) {

    }

    @Override
    public void onResults(Bundle results) {
        ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        String text = "";
        for (String result : matches) {
            text += result + "\n";
            if (result.equals(answers[randomNum])) {
                guess = true;
                new AlertDialog.Builder(TalkGame.this)
                        .setTitle("喔喔喔好棒棒")
                        .setMessage("不愧是北鼻!怎麼那麼聰明呢?")
                        .setPositiveButton("關閉鬧鐘", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mp.release();
                                TalkGame.this.finish();
                            }
                        }) .show();
                break;
            }

        }
        if(guess==false) Toast.makeText(getApplicationContext(), "猜錯嘍寶貝~再加油啊啊啊", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRmsChanged(float rmsdB) {
        progressBar.setProgress((int) rmsdB);
    }

    public static String getErrorText(int errorCode) {
        String message;
        switch (errorCode) {
            case SpeechRecognizer.ERROR_AUDIO:
                message = "Audio recording error";
                break;
            case SpeechRecognizer.ERROR_CLIENT:
                message = "Client side error";
                break;
            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                message = "Insufficient permissions";
                break;
            case SpeechRecognizer.ERROR_NETWORK:
                message = "Network error";
                break;
            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                message = "Network timeout";
                break;
            case SpeechRecognizer.ERROR_NO_MATCH:
                message = "No match";
                break;
            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                message = "RecognitionService busy";
                break;
            case SpeechRecognizer.ERROR_SERVER:
                message = "error from server";
                break;
            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                message = "No speech input";
                break;
            default:
                message = "Didn't understand, please try again.";
                break;
        }
        return message;
    }
}

