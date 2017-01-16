package com.example.wenyu.baby;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wenyu.baby.database.DataBase_girl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AlarmListAdapter extends BaseAdapter {

    private AlarmActivity alarmActivity;
    private List<Alarm> alarms = new ArrayList<Alarm>();
    ArrayList<String> beauty = new ArrayList<String>();
    ArrayList<String> tone = new ArrayList<String>();
    public List<Girl> girl;//取得 DB 妹子資料


    public AlarmListAdapter(AlarmActivity alarmActivity) {
        this.alarmActivity = alarmActivity;
        DataBase_girl a= new DataBase_girl(alarmActivity.getApplicationContext());
        girl =  a.getAll();
        for (Girl sublist : girl)
        {
            beauty.add(sublist.getGirlsPhotoPath());
            tone.add(sublist.getGirlRecordPath());
        }
    }

    @Override
    public int getCount() {
        return alarms.size();
    }

    @Override
    public Object getItem(int position) {
        return alarms.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        if (null == view)
            view = LayoutInflater.from(alarmActivity).inflate(
                    R.layout.alarm_list_element, null);

        Alarm alarm = (Alarm) getItem(position);

        CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkBox_alarm_active);
        checkBox.setChecked(alarm.getAlarmActive());
        checkBox.setTag(position);
        checkBox.setOnClickListener((View.OnClickListener) alarmActivity);


        ImageView alarmImageView = (ImageView) view
                .findViewById(R.id.imageView);
        Bitmap bm = null;
        File file = new File(beauty.get(alarm.getGirlsID()));//beauty.get(alarm.getGirlsID())
        if (file.exists()) {
            bm = BitmapFactory.decodeFile(beauty.get(alarm.getGirlsID()));
        }
        alarmImageView.setImageBitmap(bm);

        TextView alarmTimeView = (TextView) view
                .findViewById(R.id.textView_alarm_time);
        alarmTimeView.setText(alarm.getAlarmTimeString());

        TextView alarmDaysView = (TextView) view
                .findViewById(R.id.textView_alarm_days);
        alarmDaysView.setText(alarm.getRepeatDaysString());


        return view;
    }

    public List<Alarm> getMathAlarms() {
        return alarms;
    }

    public void setMathAlarms(List<Alarm> alarms) {
        this.alarms = alarms;
    }

}

