package com.learningword.activity;

import com.learningword.GuideActivity;
import com.learningword.R;
import com.learningword.R.id;
import com.learningword.R.layout;
import com.learningword.utils.sputils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;

public class LaunchActivity extends Activity {
	public static final String LOGIN = "a";
	private  RelativeLayout rl_launch;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_launch);
		rl_launch = (RelativeLayout) findViewById(R.id.rl_launch);
		showanimation();
	}
	private final int intomain = 0;
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case intomain:
				boolean isfirst = sputils.getspresult(LaunchActivity.this,LOGIN);
				Intent intent = new Intent();
				if (!isfirst) {
					intent.setClass(getApplicationContext(), GuideActivity.class);
				}else {
					intent.setClass(getApplicationContext(), MainActivity.class);
				}
				
				startActivity(intent);
				this.removeMessages(intomain);
				finish();
				break;
			}
		}
		
	};
	private void showanimation() {
		//渐变动画
		AlphaAnimation aa = new AlphaAnimation(0, 1);
		//伸展动画
		ScaleAnimation sa = new ScaleAnimation(0, 1, 0, 1, ScaleAnimation.RELATIVE_TO_SELF, 0.5f, ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
		//动画管理
		AnimationSet set = new AnimationSet(false);
		set.setDuration(1000);
		set.setFillAfter(true);
		
		set.addAnimation(sa);
		set.addAnimation(aa);
		
		rl_launch.setAnimation(set);
		set.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				handler.sendEmptyMessageDelayed(intomain, 1500);
			}
		});
		
	}
}
