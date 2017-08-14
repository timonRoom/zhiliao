package com.learningword;

import java.util.ArrayList;
import java.util.List;

import com.learningword.activity.LaunchActivity;
import com.learningword.activity.MainActivity;
import com.learningword.utils.sputils;
import com.learningword.utils.unitUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalFocusChangeListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

public class GuideActivity extends Activity {
	private ViewPager vp_viewpager;
	private List<ImageView> images;
	private LinearLayout ll_point;
	private ImageView iv_point_red;
	private int leftmax;
	private Button btn_start;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guide);
		ll_point= (LinearLayout) findViewById(R.id.ll_point);
		btn_start = (Button) findViewById(R.id.btn_start);
		iv_point_red = (ImageView) findViewById(R.id.iv_point_red);
		vp_viewpager = (ViewPager) findViewById(R.id.vp_viewpager);
		setviewpager();
		btn_start.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				sputils.putboolean(getApplicationContext(),LaunchActivity.LOGIN,true);
				Intent intent = new Intent();
				intent.setClass(getApplicationContext(), MainActivity.class);
				startActivity(intent);
				finish();
			}
		});
	}
	private void setviewpager() {
		int ids[] = new int[]{R.drawable.load_1,R.drawable.load_2,R.drawable.load_3,R.drawable.load_4};
		images =  new ArrayList<ImageView>();
		for (int i = 0; i < ids.length; i++) {
			ImageView imageView  = new ImageView(this);
			imageView.setBackgroundResource(ids[i]);
			images.add(imageView);
			ImageView point = new ImageView(this);
			point.setBackgroundResource(R.drawable.point_normal);
			LayoutParams params = new LayoutParams(unitUtils.dip2px(getApplicationContext(), 10),unitUtils.dip2px(getApplicationContext(), 10));
			if (i!=0) {
				params.leftMargin=unitUtils.dip2px(getApplicationContext(), 10);
			}
			point.setLayoutParams(params);
			ll_point.addView(point);
		}
		vp_viewpager.setAdapter(new viewpageradapter());
		iv_point_red.getViewTreeObserver().addOnGlobalLayoutListener(new pointOnGlobalLayoutListener());
		vp_viewpager.addOnPageChangeListener(new vpOnPageChangeListener());
	}
	class vpOnPageChangeListener implements OnPageChangeListener{

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			int leftmargin = (int) (arg1*leftmax)+arg0*leftmax;
			android.widget.RelativeLayout.LayoutParams params = (android.widget.RelativeLayout.LayoutParams) iv_point_red.getLayoutParams();
			params.leftMargin = leftmargin;
			iv_point_red.setLayoutParams(params);
		}

		@Override
		public void onPageSelected(int arg0) {
			if (arg0==images.size()-1) {
				btn_start.setVisibility(View.VISIBLE);
			}else {
				btn_start.setVisibility(View.GONE);
			}
		}
		
	}
	class pointOnGlobalLayoutListener implements OnGlobalLayoutListener{

		@Override
		public void onGlobalLayout() {
			iv_point_red.getViewTreeObserver().removeGlobalOnLayoutListener(this);
			leftmax = ll_point.getChildAt(1).getLeft()-ll_point.getChildAt(0).getLeft();
		}

	}
	class viewpageradapter extends PagerAdapter{

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
//			super.destroyItem(container, position, object);
			container.removeView((View) object);
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			ImageView imageView = images.get(position);
			container.addView(imageView);
			return imageView;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return images.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0 == arg1;
		}
		
	}
	
}
