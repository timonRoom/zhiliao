package com.learningword.activity;

import java.util.ArrayList;
import java.util.List;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnCloseListener;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnOpenListener;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.learningword.R;
import com.learningword.fragment.KankanFragment;
import com.learningword.fragment.MenuFragment;
import com.learningword.fragment.NewsFragment;
import com.learningword.fragment.ZihuFragment;
import com.learningword.utils.sputils;
import com.learningword.utils.unitUtils;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * @author Timon
 *	主页面
 */
public class MainActivity extends SlidingFragmentActivity implements OnClickListener{
	public static final int SHOW_FG_ZIHU = 0;
	public static final int SHOW_FG_news = 1;
	public static final int SHOW_FG_kankan = 2;
	public static  boolean title_about_isvisibity=false;
	
	private  Context context;
	private TextView tv_title_item;
	private ImageView iv_title_more;
	private FrameLayout fl_menu;
	private ZihuFragment zihufragment;
	private NewsFragment  newsFragment ;
	private KankanFragment kankanFragment;
	private List<Fragment> fragmentlist;
	private Animation title_in_animation;
	private Animation title_back_animation;
	private Animation about_text_out;
	private static Animation about_text_in;
	private ImageView iv_title_about;	
	private int position ;
	private View ic_title;
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {  
			moveTaskToBack(true);
			Log.e("onKeyDown", "back");  
            return true;   
            }
		else {
			return super.onKeyDown(keyCode, event);
		}
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		context = this;
		//设置主页面布局
		setContentView(R.layout.activity_main);
		tv_title_item = (TextView)findViewById(R.id.tv_title_item);
		iv_title_more = (ImageView) findViewById(R.id.iv_title_more);
		iv_title_about =(ImageView) findViewById(R.id.iv_title_about);
		iv_title_about.setOnClickListener(this);
		iv_title_more.setOnClickListener(this);
		initanimation();
		initfragment();
		initdata();
		setfragment();
		//设置侧滑页面布局
		setmenuview();
		
	}

	/**
	 * TODO
	 */
	private void initdata() {
		position = 0;
		sputils.putboolean(context, LaunchActivity.LOGIN, true);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_title_more:
			showmenu();
			break;
		case R.id.iv_title_about:
			Intent intent  = new Intent();
			intent.setClass(context, AboutAppActivity.class);
			startActivity(intent);
			break;

		}
	}
	private void initanimation() {
		title_in_animation =  new RotateAnimation(0, 90, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		title_in_animation.setDuration(500);
		title_in_animation.setFillAfter(true);

		title_back_animation = new RotateAnimation(90, 0, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		title_back_animation.setDuration(500);
		title_back_animation.setFillAfter(true);
		
	}
	private boolean isopen=false;
	private void showmenu(){
		//如果菜单没打开 
		if (!isopen) {
			iv_title_more.startAnimation(title_in_animation);
			slidingMenu.showMenu();
			isopen=true;
		}else {

		}
	};
	private void closemenu(){
		//如果菜单打开了 
		if (isopen) {
			iv_title_more.startAnimation(title_back_animation);
			slidingMenu.showContent();;
			isopen=false;
		}else {

		}
	};

	private void initfragment() {
		fragmentlist = new ArrayList<Fragment>();
		zihufragment = new ZihuFragment(context);
		newsFragment = new NewsFragment(context);
		kankanFragment = new KankanFragment(context);
		fragmentlist.add(zihufragment);
		fragmentlist.add(newsFragment);
		fragmentlist.add(kankanFragment);
	}

	Handler mainactivityhander = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case SHOW_FG_ZIHU:
				slidingMenu.showContent(true);
				tv_title_item.setText("< 知乎日报 >");
				//				tv_title_item.setText("<- 知乎日报 ->");
				position = 0;
				setfragment();
				break;
			case SHOW_FG_news:
				slidingMenu.showContent(true);
				tv_title_item.setText("< 新闻速递 >");
				//				tv_title_item.setText("<- 新闻速递 ->");
				position = 1;
				setfragment();
				break;
			case SHOW_FG_kankan:
				slidingMenu.showContent(true);
				//				tv_title_item.setText("每日看看");
				tv_title_item.setText("< 妹  纸 >");
				position = 2;
				setfragment();
				break;
			}
		}

	};
	private void setfragment() {
		FragmentManager supportFragmentManager = getSupportFragmentManager();
		FragmentTransaction beginTransaction = supportFragmentManager.beginTransaction();
		beginTransaction.replace(R.id.fl_activity_mian, fragmentlist.get(position)).commit();
	}
	private SlidingMenu slidingMenu;
	private void setmenuview() {
		setBehindContentView(R.layout.activity_main_menu_fragment);
		fl_menu = (FrameLayout) findViewById(R.id.fl_menu);
		MenuFragment menuFragment = new MenuFragment(context,mainactivityhander);
		getSupportFragmentManager().beginTransaction()  
		.replace(R.id.fl_menu, menuFragment).commit(); 
		slidingMenu = getSlidingMenu();
		slidingMenu.setMode(SlidingMenu.LEFT);
		slidingMenu.setFadeEnabled(true);
		slidingMenu.setTouchModeAbove(SlidingMenu.LEFT);
		slidingMenu.setBehindOffset(unitUtils.getscreenwidth(context)/3);
		slidingMenu.setOnOpenListener(new OnOpenListener() {

			@Override
			public void onOpen() {
				showmenu();
			}
		});
		slidingMenu.setOnCloseListener(new OnCloseListener() {

			@Override
			public void onClose() {
				closemenu();
			}
		});
	}

}
