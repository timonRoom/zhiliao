package com.learningword.activity;

import java.io.InputStream;
import java.net.URL;

import com.learningword.R;
import com.learningword.event.TexviewLinkMovement;
import com.learningword.model.MessageSpan;
import com.learningword.utils.netutils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class NewsDetailActivity extends Activity{
	private View include_deflault;
	private TextView tv_net_failure;
	private ImageView iv_news_back;
	private TextView tv_news_text;
	private String newsurl;
	protected Spanned fromHtml;
	private Animation textanimation;
	private Context context;
	protected final int NEWS_DETAIL_TEXT_RESTLT = 2;
	protected final int NEWS_DETAIL_EVENT = 1;
	public static final int NEWS_DETAIL_RETURN = 0;
	public static final int NEWS_DETAIL_FAILURE = 3;
	private ProgressBar pb_deflault;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		context= this;
		setContentView(R.layout.activity_news_detail);
		iv_news_back = (ImageView) findViewById(R.id.iv_news_back);
		include_deflault =findViewById(R.id.include_deflault);
		tv_net_failure = (TextView) include_deflault.findViewById(R.id.tv_net_failure);
		iv_news_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		tv_news_text = (TextView) findViewById(R.id.tv_news_text);
		tv_news_text.setVisibility(View.GONE);
		pb_deflault= (ProgressBar) include_deflault.findViewById(R.id.pb_deflault);
		((View) tv_net_failure.getParent()).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Log.e("KANKAN_API_FAILURE", "onClick");
				if (isfailure) {
					pb_deflault.setVisibility(View.VISIBLE);
					initview();
					tv_net_failure.setText("正在重试...");
				}
			}
		});
		initanimation();
		getdataformintent();
		initview();
	}
	private void initanimation() {
		textanimation =new AlphaAnimation(0, 1);
		textanimation.setDuration(1000);
		textanimation.setFillAfter(true);
	}
	private boolean isfailure = false;
	private Handler news_details_hander = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case NEWS_DETAIL_RETURN:
				isfailure = false;
				tv_news_text.setVisibility(View.VISIBLE);
				if (netutils.news_detail_text!=null) {
					settvtext();
				}
				break;
				case NEWS_DETAIL_FAILURE:
					isfailure = true;
					pb_deflault.setVisibility(View.GONE);
					tv_net_failure.setText("联网超时,点击任意处重试!");
					news_details_hander.removeMessages(NEWS_DETAIL_FAILURE);
					break;
			case NEWS_DETAIL_TEXT_RESTLT:
				Log.e("fromHtml", fromHtml.toString());
				include_deflault.setVisibility(View.GONE);
				tv_news_text.setText(fromHtml);
				tv_news_text.startAnimation(textanimation);
				tv_news_text.setMovementMethod(TexviewLinkMovement.getInstance(news_details_hander,NEWS_DETAIL_EVENT, ImageSpan.class));
				news_details_hander.removeMessages(NEWS_DETAIL_TEXT_RESTLT);
				break;
			case NEWS_DETAIL_EVENT:
				MessageSpan ms = (MessageSpan) msg.obj; 
				Object[] spans = (Object[]) ms.getObj(); 
				for (Object span : spans) { 
					if (span instanceof ImageSpan) {
						Log.d("Movement", "点击了图片"+((ImageSpan) span).getSource()); //处理自己的逻辑 }
						Intent intent = new Intent();
						intent.setClass(context, ImageReader.class);
						intent.putExtra("imageurl", ((ImageSpan) span).getSource());
						startActivity(intent);
					}
				}
				break;
			}
		}
		
	};
	
	
	private void getdataformintent() {
		newsurl = getIntent().getStringExtra("news_url");
		
	}
	protected void settvtext() {
		new Thread(new Runnable() {
			
			@SuppressWarnings("deprecation")
			@Override
			public void run() {
				// TODO Auto-generated method stub
//				fromHtml = Html.fromHtml(netutils.news_detail_text, null, null); 
				fromHtml = Html.fromHtml(netutils.news_detail_text, new myimagergetter(), null); 
				Log.e("sendEmptyMessage", "HTMLCONTEXT");
				news_details_hander.sendEmptyMessage(NEWS_DETAIL_TEXT_RESTLT);
			}
		}).start();
	}
	@Override
	protected void onDestroy() {
		netutils.news_detail_text = null;
		news_details_hander.removeCallbacksAndMessages(null);
		fromHtml= null;
		TexviewLinkMovement.disInstance();
		super.onDestroy();
	}
	private void initview() {
		netutils.getnewsdetail(news_details_hander,newsurl);
	}
	private   class myimagergetter implements ImageGetter {
		@Override
		public Drawable getDrawable(String source) {
			 InputStream is = null;  
			   Drawable d  = null;
			   try {  
			    is = (InputStream) new URL(source).getContent();  
			    d = Drawable.createFromStream(is, "src");  
			    d.setBounds(0, 0, d.getIntrinsicWidth(),  d.getIntrinsicHeight());
			    is.close(); 
			   
			    return d;  
			   } catch (Exception e) {  
//			    d = Drawable;
//			    d.setBounds(0, 0, d.getIntrinsicWidth(),  d.getIntrinsicHeight());
			    return d;  
			   }  
		}
		
	}
}
