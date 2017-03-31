package com.ruikong.funcloud;
import java.io.IOException;
import java.util.Date;
import android.os.Bundle;
import android.os.Handler;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

import com.squareup.okhttp.Response;

public class MainActivity extends AppCompatActivity
		implements NavigationView.OnNavigationItemSelectedListener  {
	ListView lv;
	FloatingActionButton bt;
	RecordUtil mRecorduUtil;
	LinearLayout ll_record;
	ImageView iv_volume;
	AudioRec audio = null;
	long mStartTime = 0;
	Handler mHandler = new Handler();
	UploadFile mFileUpload = new UploadFile();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		AudioStroe.getInstance().setContext( getApplicationContext() );

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		//抽屉布局
		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
				this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
		drawer.addDrawerListener(toggle);
		toggle.syncState();

		//导航栏
		NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
		navigationView.setNavigationItemSelectedListener(this);


		ll_record = (LinearLayout) findViewById(R.id.ll_record);
		iv_volume = (ImageView) findViewById(R.id.iv_volume);
		mRecorduUtil = new RecordUtil();
		lv = (ListView) findViewById(R.id.lv_custom_bell);
		updateBell();
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if (audio != null && mRecorduUtil.isPlaying()) {
					mRecorduUtil.stopPlay();
				}
				audio = (AudioRec) parent.getAdapter().getItem(position);

				mRecorduUtil.startPlay(audio.getFilePath(), false);
			}
		});

		lv.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {

				audio = (AudioRec) parent.getAdapter().getItem(position);

				Builder builder = new Builder(MainActivity.this);
				builder.setTitle("提示");
				builder.setMessage("请选择操作？");
				builder.setNegativeButton("取消", null);
				builder.setNeutralButton("上传", new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int i) {

						try {
							mFileUpload.upload(audio.getFilePath(), new UploadInterface() {
								@Override
								public void onUploadCallBack(Response res, Error error) {
									String ret = "";
									if (error==null){
										audio.setIsUpload( true );
										ret = "success";
									}
									else
									{
										ret = error.getMessage();
									}

									//提示
									Toast.makeText(MainActivity.this, ret, 0).show();
									updateBell();
								}
							});
						}
						catch (IOException ex){

						}

					}
				});
				builder.setPositiveButton("删除", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						if ( AudioStroe.getInstance().del( audio ) ) {
							Toast.makeText(MainActivity.this, "success", 0).show();
							updateBell();
						}
					}
				});
				builder.show();
				return true;
			}
		});


		//底部漂浮按钮
		bt = (FloatingActionButton) findViewById(R.id.fab);


		bt.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					mStartTime = new Date().getTime();
					bt.setImageResource(android.R.drawable.ic_media_pause);
					mRecorduUtil.startRecord();
					if (mRecorduUtil.isRecording()) {
						ll_record.setVisibility(View.VISIBLE);
						Thread t = new Thread(mPollTask);
						t.start();
					}
					break;

				case MotionEvent.ACTION_UP:
					bt.setImageResource(android.R.drawable.ic_media_play);
					ll_record.setVisibility(View.GONE);
					mRecorduUtil.stopRecord();
					updateBell();
					mHandler.removeCallbacks(mPollTask);

					lv.setSelection(lv.getBottom());
					break;
				}
				return true;
			}
		});


	}

	private Runnable mPollTask = new Runnable() {
		public void run() {
			int mVolume = mRecorduUtil.getVolume();
			Log.d("volume", mVolume + "");
			updateVolume(mVolume);
			mHandler.postDelayed(mPollTask, 100);
		}
	};

	private void updateBell() {
		lv.setAdapter(new CustomBellAdapter(AudioStroe.getInstance().getmAudioRecList(), MainActivity.this));
	}

	private void updateVolume(int volume) {
		switch (volume) {
		case 1:
			iv_volume.setImageResource(R.mipmap.p1);
			break;
		case 2:
			iv_volume.setImageResource(R.mipmap.p2);
			break;
		case 3:
			iv_volume.setImageResource(R.mipmap.p3);
			break;
		case 4:
			iv_volume.setImageResource(R.mipmap.p4);
			break;
		case 5:
			iv_volume.setImageResource(R.mipmap.p5);
			break;
		case 6:
			iv_volume.setImageResource(R.mipmap.p6);
			break;
		case 7:
			iv_volume.setImageResource(R.mipmap.p7);
			break;
		default:
			break;
		}
	}


	@Override
	public void onBackPressed() {
		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		if (drawer.isDrawerOpen(GravityCompat.START)) {
			drawer.closeDrawer(GravityCompat.START);
		} else {
			super.onBackPressed();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@SuppressWarnings("StatementWithEmptyBody")
	@Override
	public boolean onNavigationItemSelected(MenuItem item) {
		// Handle navigation view item clicks here.
		int id = item.getItemId();

		if (id == R.id.nav_camera) {
			// Handle the camera action
		} else if (id == R.id.nav_gallery) {

		} else if (id == R.id.nav_slideshow) {

		} else if (id == R.id.nav_manage) {

		} else if (id == R.id.nav_share) {

		} else if (id == R.id.nav_send) {

		}

		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		drawer.closeDrawer(GravityCompat.START);
		return true;
	}

}
