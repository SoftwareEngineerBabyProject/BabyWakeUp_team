package com.example.wenyu.baby.preferences;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.wenyu.baby.Alarm;
import com.example.wenyu.baby.BaseActivity;
import com.example.wenyu.baby.Girl;
import com.example.wenyu.baby.GirlsAddActivity;
import com.example.wenyu.baby.R;
import com.example.wenyu.baby.database.DataBase_girl;
import com.example.wenyu.baby.database.Database;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AlarmPreferencesActivity extends BaseActivity implements OnPageChangeListener {

	private Alarm alarm;
	private MediaPlayer mp = new MediaPlayer();

	private ListAdapter listAdapter;
	private ListView listView;

	Button buttonNext;
	Button buttonBack;

///
//宣告妹子圖片所需物件
	ImageView imageView;
	ArrayList<Integer>girl_id = new ArrayList<Integer>();
	ArrayList<String> beauty = new ArrayList<String>();
	ArrayList<String> tone = new ArrayList<String>();
	private String photoPath;
	private String tonePath;
	private int PhotoNo = 0; //目前第幾個妹子
	//
//db
	public List<Girl> girl;//取得 DB 妹子資料
	int total = 0;//目前幾個妹子
	 Bitmap bm = null;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// requestWindowFeature(Window.FEATURE_NO_TITLE);
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.alarm_preferences);

		buttonNext=(Button) findViewById(R.id.right_button);
		buttonBack=(Button) findViewById(R.id.left_button);
		imageView = (ImageView)findViewById(R.id.imageView);

		//傳alarm資料
		Bundle bundle = getIntent().getExtras();
		if (bundle != null && bundle.containsKey("alarm")) {
			setMathAlarm((Alarm) bundle.getSerializable("alarm"));
		} else {
			setMathAlarm(new Alarm());
		}
		if (bundle != null && bundle.containsKey("adapter")) {
			setListAdapter((AlarmPreferenceListAdapter) bundle.getSerializable("adapter"));
		} else {
			setListAdapter(new AlarmPreferenceListAdapter(this, getMathAlarm()));
		}

		//db
		//抓id圖片音檔
		DataBase_girl a= new DataBase_girl(getApplicationContext());
		 girl =  a.getAll();
		for (Girl sublist : girl)
		{
			girl_id.add(sublist.getId());
			beauty.add(sublist.getGirlsPhotoPath());
			tone.add(sublist.getGirlRecordPath());
			total ++;
		}
		//有妹子時放妹子圖片跟聲音供選擇
        if(total!=0){
			PhotoNo = getMathAlarm().getGirlsID();
			bm = GetPic(PhotoNo);
			imageView.setImageBitmap(bm);
			setTonePath(tone.get(PhotoNo));
			try {
				mp.setDataSource(tone.get(PhotoNo));
				mp.prepare();
			} catch (IOException e) {
				e.printStackTrace();
			}
			mp.start();
		}

		buttonNext.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (total > 1) {
					Log.d("next", String.valueOf(PhotoNo));
					if ((PhotoNo + 1) < total) {
						PhotoNo++;
						mp.stop();
						mp.reset();
					} else {
						PhotoNo = 0;
						mp.stop();
						mp.reset();
					}
					bm = GetPic(PhotoNo);
					imageView.setImageBitmap(bm);
					setTonePath(tone.get(PhotoNo));
					try {
						mp.setDataSource(tone.get(PhotoNo));
						mp.prepare();
					} catch (IOException e) {
						e.printStackTrace();
					}
					mp.start();
				}
			}
		});

		buttonBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (total > 1) {
					Log.d("last", String.valueOf(PhotoNo));
					if ((PhotoNo - 1) >= 0) {
						PhotoNo--;
						mp.stop();
						mp.reset();
					} else {
						PhotoNo = total-1;
						mp.stop();
						mp.reset();
					}
					bm = GetPic(PhotoNo);
					imageView.setImageBitmap(bm);
					setTonePath(tone.get(PhotoNo));
					try {
						mp.setDataSource(tone.get(PhotoNo));
						mp.prepare();
					} catch (IOException e) {
						e.printStackTrace();
					}
					mp.start();
				}
			}
		});


		getListView().setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> l, View v, int position, long id) {
				final AlarmPreferenceListAdapter alarmPreferenceListAdapter = (AlarmPreferenceListAdapter) getListAdapter();
				final AlarmPreference alarmPreference = (AlarmPreference) alarmPreferenceListAdapter.getItem(position);

				AlertDialog.Builder alert;
				v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
				switch (alarmPreference.getType()) {
					case BOOLEAN:
						CheckedTextView checkedTextView = (CheckedTextView) v;
						boolean checked = !checkedTextView.isChecked();
						((CheckedTextView) v).setChecked(checked);
						switch (alarmPreference.getKey()) {
							case ALARM_ACTIVE:
								alarm.setAlarmActive(checked);
								break;
							case ALARM_VIBRATE:
								alarm.setVibrate(checked);
								if (checked) {
									Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
									vibrator.vibrate(1000);
								}
								break;
						}
						alarmPreference.setValue(checked);
						break;
					case LIST:
						alert = new AlertDialog.Builder(AlarmPreferencesActivity.this);

						alert.setTitle(alarmPreference.getTitle());
						// alert.setMessage(message);

						CharSequence[] items = new CharSequence[alarmPreference.getOptions().length];
						for (int i = 0; i < items.length; i++)
							items[i] = alarmPreference.getOptions()[i];

						alert.setItems(items, new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								switch (alarmPreference.getKey()) {
									case ALARM_DIFFICULTY:
										Alarm.Difficulty d = Alarm.Difficulty.values()[which];
										alarm.setDifficulty(d);
										break;
									default:
										break;
								}
								alarmPreferenceListAdapter.setMathAlarm(getMathAlarm());
								alarmPreferenceListAdapter.notifyDataSetChanged();
							}

						});

						alert.show();
						break;
					case MULTIPLE_LIST:
						alert = new AlertDialog.Builder(AlarmPreferencesActivity.this);

						alert.setTitle(alarmPreference.getTitle());
						// alert.setMessage(message);

						CharSequence[] multiListItems = new CharSequence[alarmPreference.getOptions().length];
						for (int i = 0; i < multiListItems.length; i++)
							multiListItems[i] = alarmPreference.getOptions()[i];

						boolean[] checkedItems = new boolean[multiListItems.length];
						for (Alarm.Day day : getMathAlarm().getDays()) {
							checkedItems[day.ordinal()] = true;
						}
						alert.setMultiChoiceItems(multiListItems, checkedItems, new OnMultiChoiceClickListener() {

							@Override
							public void onClick(final DialogInterface dialog, int which, boolean isChecked) {

								Alarm.Day thisDay = Alarm.Day.values()[which];

								if (isChecked) {
									alarm.addDay(thisDay);
								} else {
									// Only remove the day if there are more than 1
									// selected
									if (alarm.getDays().length > 1) {
										alarm.removeDay(thisDay);
									} else {
										// If the last day was unchecked, re-check
										// it
										((AlertDialog) dialog).getListView().setItemChecked(which, true);
									}
								}

							}
						});
						alert.setOnCancelListener(new OnCancelListener() {
							@Override
							public void onCancel(DialogInterface dialog) {
								alarmPreferenceListAdapter.setMathAlarm(getMathAlarm());
								alarmPreferenceListAdapter.notifyDataSetChanged();

							}
						});
						alert.show();
						break;
					case TIME:
						TimePickerDialog timePickerDialog = new TimePickerDialog(AlarmPreferencesActivity.this, new OnTimeSetListener() {

							@Override
							public void onTimeSet(TimePicker timePicker, int hours, int minutes) {
								Calendar newAlarmTime = Calendar.getInstance();
								newAlarmTime.set(Calendar.HOUR_OF_DAY, hours);
								newAlarmTime.set(Calendar.MINUTE, minutes);
								newAlarmTime.set(Calendar.SECOND, 0);
								alarm.setAlarmTime(newAlarmTime);
								alarmPreferenceListAdapter.setMathAlarm(getMathAlarm());
								alarmPreferenceListAdapter.notifyDataSetChanged();
							}
						}, alarm.getAlarmTime().get(Calendar.HOUR_OF_DAY), alarm.getAlarmTime().get(Calendar.MINUTE), true);
						timePickerDialog.setTitle(alarmPreference.getTitle());
						timePickerDialog.show();
					default:
						break;
				}
			}
		});
	}
	///////////////////////////////////////////////

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		boolean result = super.onCreateOptionsMenu(menu);
		menu.findItem(R.id.menu_girls_new).setVisible(false);
		return result;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menu_item_new:
				Intent intent = new Intent();
				intent.setClass(AlarmPreferencesActivity.this, GirlsAddActivity.class);
				startActivity(intent);
				AlarmPreferencesActivity.this.finish();
				break;
			case R.id.menu_item_save:
				//存入妹子圖片及音樂資料及妹子id
				Log.d("ddd",getPhotoPath());
				Log.d("ddd",getTonePath());
				getMathAlarm().setAlarmTonePath(tone.get(PhotoNo));
				getMathAlarm().setAlarmPhotoPath(getPhotoPath());
				getMathAlarm().setGirlsID(PhotoNo);
				//存入db
				Database.init(getApplicationContext());
				if (getMathAlarm().getId() < 1) {
					Database.create(getMathAlarm());
				} else {
					Database.update(getMathAlarm());
				}
				callMathAlarmScheduleService();
				Toast.makeText(AlarmPreferencesActivity.this, getMathAlarm().getTimeUntilNextAlarmMessage(), Toast.LENGTH_LONG).show();
				mp.stop();
				finish();
				break;
			case R.id.menu_item_delete:
				/*DataBase_girl a= new DataBase_girl(getApplicationContext());
				if (getMathAlarm().getId() < 1) {
					// Alarm not saved
				} else {
					a.delete((long)PhotoNo);
					finish();
				}*/

				AlertDialog.Builder dialog = new AlertDialog.Builder(AlarmPreferencesActivity.this);
				dialog.setTitle("Delete");
				dialog.setMessage("Delete this alarm?");
				dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

						Database.init(getApplicationContext());
						if (getMathAlarm().getId() < 1) {
							// Alarm not saved
						} else {
							Database.deleteEntry(alarm);
							callMathAlarmScheduleService();
						}
						mp.stop();
						finish();
					}
				});
				dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
				dialog.show();

				break;
		}
		return super.onOptionsItemSelected(item);
	}

	private CountDownTimer alarmToneTimer;

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putSerializable("alarm", getMathAlarm());
		outState.putSerializable("adapter", (AlarmPreferenceListAdapter) getListAdapter());
	}

	;

	@Override
	protected void onPause() {
		super.onPause();
		try {
			if (mp != null)
				mp.release();
		} catch (Exception e) {
		}
		// setListAdapter(null);
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		try {
			if (mp != null)
				mp.release();
		} catch (Exception e) {
		}
		// setListAdapter(null);
	}
	@Override
	protected void onStop() {
		super.onStop();
	}
	@Override
	protected void onResume() {
		super.onResume();
	}

	public Alarm getMathAlarm() {
		return alarm;
	}

	public void setMathAlarm(Alarm alarm) {
		this.alarm = alarm;
	}

	public ListAdapter getListAdapter() {
		return listAdapter;
	}

	public void setListAdapter(ListAdapter listAdapter) {
		this.listAdapter = listAdapter;
		getListView().setAdapter(listAdapter);

	}

	public ListView getListView() {
		if (listView == null)
			listView = (ListView) findViewById(android.R.id.list);
		return listView;
	}

	public void setListView(ListView listView) {
		this.listView = listView;
	}

	public void onClick(View v) {
		// super.onClick(v);

	}

	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

	}

	@Override
	public void onPageSelected(int position) {

	}

	@Override
	public void onPageScrollStateChanged(int state) {

	}

	public class MyAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return Integer.MAX_VALUE;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(View container, int position, Object object) {
			//((ViewPager)container).removeView(mImageViews[position % mImageViews.length]);
		}
	}

	//////show photo
	Bitmap GetPic(int PhotoNo){
		setPhotoPath(beauty.get(PhotoNo));
		File file = new File(beauty.get(PhotoNo));
		if (file.exists()) {
			 bm = BitmapFactory.decodeFile(beauty.get(PhotoNo));
		}
		return bm;
	}
	void setPhotoPath(String photoPath){
			  this.photoPath = photoPath;
	}
	String getPhotoPath(){
		return this.photoPath;
	}
	void setTonePath(String tonePath){
		this.tonePath = tonePath;
	}
	String getTonePath(){
		return this.tonePath;
	}
}
