package com.learningword.activity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.learningword.R;
import com.learningword.myview.MyImageView;
import com.learningword.utils.netutils;
import com.squareup.okhttp.OkHttpClient;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ImageReader extends Activity implements OnClickListener {
	private String imagerurl;
	private Context context;
	private ImageView iv_back;
	private ImageView iv_download;
	private int window_width, window_height;// 控件宽度
	public static final int IMAGE_DOWNLOAD=0;
	private com.learningword.myview.DragImageView dragImageView;// 自定义控件
	private int state_height;// 状态栏的高度
	private String imagedir = Environment
			.getExternalStorageDirectory()+"/ziliao/image";
	private ViewTreeObserver viewTreeObserver;
	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.iv_back:
			finish();
			break;
		case R.id.iv_download:
			netutils.downloadfile(imagerurl, imagedir, imagerurl.substring(imagerurl.length()-10, imagerurl.length()),image_hander);
			break;
		}
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_imagereader);
		iv_back = (ImageView) findViewById(R.id.iv_back);
		iv_download = (ImageView) findViewById(R.id.iv_download);
		iv_download.setOnClickListener(this);
		iv_back.setOnClickListener(this);

		WindowManager manager = getWindowManager();
		context = this;
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND,
				WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
		window_width = manager.getDefaultDisplay().getWidth();
		window_height = manager.getDefaultDisplay().getHeight();
		dragImageView =  (com.learningword.myview.DragImageView) findViewById(R.id.iv_activity_images);
//		dragImageView.setOnLongClickListener(new OnLongClickListener() {
//
//			@Override
//			public boolean onLongClick(View v) {
//				Log.e("onLongClick", "onLongClick");
//				showdownloaddialog();
//
//				return true;
//			}
//		});
		getdataforimage();
		initview();
		viewTreeObserver = dragImageView.getViewTreeObserver();
		viewTreeObserver
		.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

			@Override
			public void onGlobalLayout() {
				if (state_height == 0) {
					// 获取状况栏高度
					Rect frame = new Rect();
					getWindow().getDecorView()
					.getWindowVisibleDisplayFrame(frame);
					state_height = frame.top;
					dragImageView.setScreen_H(window_height-state_height);
					dragImageView.setScreen_W(window_width);
				}

			}
		});
	}
	private Handler image_hander = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if (msg.what==IMAGE_DOWNLOAD) {
				Toast.makeText(context, "保存图片成功^_^", Toast.LENGTH_SHORT).show();
			}
		}

	};
	protected void showdownloaddialog() {
		final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
		alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable());
		alertDialog.show();
		alertDialog.setContentView(R.layout.dialog_image_download);
		TextView tv_dialog_cancel = (TextView) alertDialog.findViewById(R.id.tv_dialog_cancel);
		tv_dialog_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				alertDialog.cancel();
			}
		});
		TextView tv_dialog_ok = (TextView) alertDialog.findViewById(R.id.tv_dialog_ok);
		tv_dialog_ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				alertDialog.cancel();
				netutils.downloadfile(imagerurl, imagedir, imagerurl.substring(imagerurl.length()-10, imagerurl.length()),image_hander);
			}
		});
		alertDialog.setCanceledOnTouchOutside(false);
	}
	@SuppressWarnings("unchecked")
	private void initview() {
		//		Target<GlideDrawable> into = Glide.with(getApplicationContext())
		//		.load(imagerurl)
		//		.placeholder(R.drawable.app)
		//		.error(R.drawable.image_error)
		//		.into(imageView);
		Glide.with(getApplicationContext())  
		.load(imagerurl)  
		.asBitmap() //必须  
		.into(target)  ;

	}
	private SimpleTarget target = new SimpleTarget<Bitmap>() {  
		@Override
		public void onResourceReady(Bitmap bitmap, GlideAnimation glideAnimation) {
			//这里我们拿到回掉回来的bitmap，可以加载到我们想使用到的地方
			dragImageView.setImageBitmap(bitmap);
			dragImageView.setmActivity((Activity) context);//注入Activity.

		}
	};
	private void getdataforimage() {
		imagerurl = getIntent().getStringExtra("imageurl");
		//		 mLocationX = getIntent().getIntExtra("locationX", 0);  
		//		mLocationY = getIntent().getIntExtra("locationY", 0);  
		//		mWidth = getIntent().getIntExtra("width", 0);  
		//		mHeight = getIntent().getIntExtra("height", 0); 
	}
	@Override
	protected void onDestroy() {
		imagerurl = null;
		super.onDestroy();
	}

}
