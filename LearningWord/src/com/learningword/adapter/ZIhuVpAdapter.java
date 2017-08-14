package com.learningword.adapter;

import java.util.List;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.learningword.R;
import com.learningword.activity.MainActivity;
import com.learningword.activity.ZihuDetailactivity;
import com.learningword.fragment.ZihuFragment;
import com.learningword.model.NewsModel;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.opengl.Matrix;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

public class ZIhuVpAdapter extends PagerAdapter {
	private List<NewsModel> top_stories;
	private Context context;
	private Handler handler;

	public ZIhuVpAdapter(List<NewsModel> top_stories, Context context, Handler handler) {
		super();
		this.top_stories = top_stories;
		this.context = context;
		this.handler = handler;
	}
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		final int pos = position;
		NewsModel newsModel = top_stories.get(position);
		ImageView imageView = new ImageView(context);
		imageView.setScaleType(ScaleType.FIT_XY);
		imageView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				switch (arg1.getAction()) {
				case MotionEvent.ACTION_DOWN:
					handler.removeMessages(ZihuFragment.VP_CAROUSEL);
					break;
				case MotionEvent.ACTION_UP:
					handler.removeMessages(ZihuFragment.VP_CAROUSEL);
					handler.sendEmptyMessageDelayed(ZihuFragment.VP_CAROUSEL,ZihuFragment.vp_time);
					break;

				}
				return false;
			}
		});
		imageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(context, ZihuDetailactivity.class);
				intent.putExtra("news", top_stories.get(pos));
				context.startActivity(intent);
			}
		});
		Glide.with(context)
		.load(newsModel.getImagerurl())
		.placeholder(R.drawable.app)
		.error(R.drawable.image_error)
		.into(imageView);
		container.addView(imageView);
		//		MainActivity.tv_toptitle.setWidth(getimagewidth(imageView));
		//		Glide.with(context)  
		//        .load(newsModel.getImagerurl())  
		//        .asBitmap()  
		//        .placeholder(R.drawable.app)  
		//        .dontAnimate()  
		//        .into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL){
		//
		//			@Override
		//			public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> arg1) {
		//				Log.e("getWidth>>>", resource.getWidth()+"");
		//				MainActivity.tv_toptitle.setWidth(resource.getWidth());
		//			}
		//        	
		//        });
		//		MainActivity.tv_toptitle.setWidth(imageView.getWidth());
		Log.e("getWidth>>>", imageView.getWidth()+"");
		return imageView;
	}



	private int getimagewidth(ImageView iv) {
		//获得ImageView中Image的变换矩阵  
		android.graphics.Matrix m = iv.getImageMatrix();  
		float[] values = new float[10];  
		m.getValues(values);  

		//Image在绘制过程中的变换矩阵，从中获得x和y方向的缩放系数  
		float sx = values[0]; 
		//获得ImageView中Image的真实宽高，  
		int dw = iv.getDrawable().getBounds().width(); 
		//计算Image在屏幕上实际绘制的宽高  
		int cw = (int)(dw * sx);
		return cw;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return top_stories.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// TODO Auto-generated method stub
		return arg0==arg1;
	}

}
