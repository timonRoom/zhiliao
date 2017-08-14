package com.learningword.myview;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.learningword.R;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class RefreshListview extends ListView{
	private LinearLayout headerview;
	private TextView tv_refresh_time;
	private TextView tv_refresh_start;
	private ProgressBar pb_refresh;
	private ImageView iv_arrws;
	private LinearLayout ll_refresh;
	private int  pulldonwnviewHeight;
	public static final int PULL_DOWN = 0;
	public static final int RELEASE_HANDER =1;
	public static final int REFRESHING=2;
	private int currentstatues ;
	public RefreshListview(Context context) {
		this(context, null);
		// TODO Auto-generated constructor stub
	}
	public RefreshListview(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		// TODO Auto-generated constructor stub
	}
	public RefreshListview(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initheaderview(context);
		initanimation();
	}
	private Animation up_animation;
	private Animation down_animation;
	private void initanimation() {
		up_animation = new RotateAnimation(0, -180, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		up_animation.setDuration(500);
		up_animation.setFillAfter(true);
		
		down_animation = new RotateAnimation(-180, -360, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		down_animation.setDuration(500);
		down_animation.setFillAfter(true);
	}
	private void initheaderview(Context context) {
		headerview = (LinearLayout) View.inflate(context, R.layout.refresh_headerview, null);
		tv_refresh_time = (TextView) headerview.findViewById(R.id.tv_refresh_time);
		tv_refresh_start = (TextView) headerview.findViewById(R.id.tv_refresh_start);
		pb_refresh = (ProgressBar) headerview.findViewById(R.id.pb_refresh);
		iv_arrws = (ImageView) headerview.findViewById(R.id.iv_arrws);
		//下拉刷新控件
		ll_refresh = (LinearLayout) headerview.findViewById(R.id.ll_refresh);
		ll_refresh.measure(0, 0);
		pulldonwnviewHeight = ll_refresh.getMeasuredHeight();
		//隐藏控件
		ll_refresh.setPadding(0, -pulldonwnviewHeight, 0, 0);
		addHeaderView(headerview);
	}
	private float startY=-1;
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			startY = ev.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			if (startY==-1) {
				startY = ev.getY();
			}
			if (currentstatues == REFRESHING) {
				break;
			}
			float endY = ev.getY();
			float distance = endY - startY;
			if (distance>0) {

				int padding = (int) (-pulldonwnviewHeight+distance);
				if (padding<0&&currentstatues!=PULL_DOWN) {
					currentstatues = PULL_DOWN;
					refreshview();
				}else if (padding>0&&currentstatues!=RELEASE_HANDER) {
					currentstatues = RELEASE_HANDER;
					refreshview();
				}
				ll_refresh.setPadding(0, padding, 0, 0);
			}
			break;
		case MotionEvent.ACTION_UP:
			startY=-1;
			if (currentstatues==PULL_DOWN) {
				ll_refresh.setPadding(0, -pulldonwnviewHeight, 0, 0);
			}else if (currentstatues ==RELEASE_HANDER) {
				currentstatues = REFRESHING;
				ll_refresh.setPadding(0, 0, 0, 0);
				refreshview();
				if (mOnRefreshListener!=null) {
					mOnRefreshListener.OnstartRefresh();
				}
			}
			break;
		}
		return super.onTouchEvent(ev);

	}

	private void refreshview() {
		switch (currentstatues) {
		case PULL_DOWN:
			iv_arrws.startAnimation(down_animation);
			tv_refresh_start.setText("下拉刷新...");
			
			break;
		case RELEASE_HANDER:
			iv_arrws.startAnimation(up_animation);
			tv_refresh_start.setText("松手刷新...");

			break;
		case REFRESHING:
			iv_arrws.clearAnimation();
			iv_arrws.setVisibility(GONE);
			pb_refresh.setVisibility(View.VISIBLE);
			tv_refresh_start.setText("正在刷新...");
			break;
		}
	}
	public interface OnRefreshListener{
		public void  OnstartRefresh();
	}
	private OnRefreshListener mOnRefreshListener;
	public void SetOnRefreshListener(OnRefreshListener lo){
		this.mOnRefreshListener = lo;
	}
	public void onpulldownrefresh() {
		iv_arrws.clearAnimation();
		iv_arrws.setVisibility(View.VISIBLE);
		pb_refresh.setVisibility(GONE);
		currentstatues = PULL_DOWN;
		tv_refresh_start.setText("已刷新!");
		Toast.makeText(getContext(), "已刷新 ^_^", Toast.LENGTH_SHORT).show();;
		tv_refresh_time.setText("刷新时间："+getsystimes());
		handler.sendEmptyMessageDelayed(0, 2000);
	}
	private Handler handler  = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			ll_refresh.setPadding(0, -pulldonwnviewHeight, 0, 0);
		}
		
	};
	private CharSequence getsystimes() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");  
        String t=format.format(new Date());
		return t;  
	};

}
