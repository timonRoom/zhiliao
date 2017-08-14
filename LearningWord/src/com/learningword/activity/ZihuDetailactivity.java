package com.learningword.activity;

import java.io.InputStream;
import java.net.URL;

import org.xml.sax.XMLReader;

import com.bumptech.glide.Glide;
import com.learningword.R;
import com.learningword.event.TexviewLinkMovement;
import com.learningword.model.MessageSpan;
import com.learningword.model.NewsModel;
import com.learningword.utils.netutils;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.text.Html.TagHandler;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

public class ZihuDetailactivity extends Activity {
	@Override
	protected void onDestroy() {
		newsdetail_handler.removeCallbacksAndMessages(null);
		netutils.zihu_newsDetailModel = null;
		TexviewLinkMovement.disInstance();
		iv_news_image = null;
		tv_news_text = null;
		tv_top_titles =null;
		context = null;
		fromHtml = null;
		super.onDestroy();
	}
	private static Spanned fromHtml;
	public static final int HTMLCONTEXT = 1;
	public static final int GETNEWSDETAILS = 0;
	public static final int VIEWEVENT = 2;
	public static final int NET_FAILURE = 3;
	private  ImageView iv_news_image;
	private  TextView tv_news_text;
	private NewsModel news;
	private Animation textanimation;
	private static TextView tv_top_titles;
	private ImageView iv_title_back;
	private static Context context;
	private View include_deflault;
	private TextView tv_net_failure;
	private ProgressBar pb_deflault;
	private LinearLayout ll_zihu;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_zihu_detail);
		iv_news_image = (ImageView) findViewById(R.id.iv_news_image);
		tv_news_text =(TextView) findViewById(R.id.tv_news_text);
		tv_top_titles =(TextView) findViewById(R.id.tv_top_titles);
		ll_zihu = (LinearLayout) findViewById(R.id.ll_zihu);
		ll_zihu.setVisibility(View.GONE);
		iv_title_back = (ImageView) findViewById(R.id.iv_title_back);
		include_deflault =findViewById(R.id.include_deflault);
		tv_net_failure = (TextView) include_deflault.findViewById(R.id.tv_net_failure);
		tv_news_text.setMovementMethod(ScrollingMovementMethod.getInstance());
		context = this;
		pb_deflault= (ProgressBar) include_deflault.findViewById(R.id.pb_deflault);
		((View) tv_net_failure.getParent()).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Log.e("KANKAN_API_FAILURE", "onClick");
				if (isfailure) {
					pb_deflault.setVisibility(View.VISIBLE);
					initdata();
					tv_net_failure.setText("正在重试...");
				}
			}
		});
		initanimation();
		getinitdata();
		initdata();
		iv_title_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
	private void initanimation() {
		textanimation =new AlphaAnimation(0, 1);
		textanimation.setDuration(1000);
		textanimation.setFillAfter(true);
	}
	private void initdata() {
		netutils.getnewsimage(newsdetail_handler,news.getId());
	}
	private void getinitdata() {
		news = (NewsModel) getIntent().getSerializableExtra("news");
	}
	private boolean isfailure = false;
	private    Handler newsdetail_handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case GETNEWSDETAILS:
				
				tv_top_titles.setText(netutils.zihu_newsDetailModel.getTitle());
				showdata();
				newsdetail_handler.removeMessages(GETNEWSDETAILS);
				break;
				case NET_FAILURE :
					isfailure = true;
					pb_deflault.setVisibility(View.GONE);
					tv_net_failure.setText("联网超时,点击任意处重试!");
					newsdetail_handler.removeMessages(NET_FAILURE);
					break;
			case HTMLCONTEXT:
				ll_zihu.setVisibility(View.VISIBLE);
				tv_news_text.startAnimation(textanimation);
				include_deflault.setVisibility(View.GONE);
				tv_news_text.setText(fromHtml);
				tv_news_text.startAnimation(textanimation);
				tv_news_text.setMovementMethod(TexviewLinkMovement.getInstance(newsdetail_handler,VIEWEVENT, ImageSpan.class));
				newsdetail_handler.removeMessages(HTMLCONTEXT);
				break;
			case VIEWEVENT :
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
	protected  void showdata() {
		if (netutils.zihu_newsDetailModel!=null) {
//			if (netutils.testurl!=null) {
			
			Glide.with(context)
			.load(netutils.zihu_newsDetailModel.getImageuri())
			.placeholder(R.drawable.app)
			.error(R.drawable.image_error)
			.dontAnimate()
			.into(iv_news_image);
			Log.e("getBody", netutils.zihu_newsDetailModel.getBody());
			
//		tv_news_context.setText(Html.fromHtml(netutils.newsDetailModel.getBody()));
			new Thread(new Runnable() {
				
				@SuppressWarnings("deprecation")
				@Override
				public void run() {
					// TODO Auto-generated method stub
//					fromHtml = Html.fromHtml(netutils.newsDetailModel.getBody(), new myimagergetter(), null); 
					fromHtml = Html.fromHtml(netutils.zihu_newsDetailModel.getBody(), new myimagergetter(), null); 
					newsdetail_handler.sendEmptyMessage(HTMLCONTEXT);
					    Log.e("sendEmptyMessage", "HTMLCONTEXT");
				}
			}).start();
		}
		
	}
	public static class myTagHandler implements TagHandler{
		  private int sIndex = 0;
	        private int eIndex = 0;
	        private final Context mContext;

	        public myTagHandler(Context context) {
	            mContext = context;
	        }
		@Override
		public void handleTag(boolean opening, String tag, Editable output, XMLReader xmlReader) {
			 if (tag.toLowerCase().equals("img")) {
	                if (opening) {
	                    sIndex = output.length();
	                } else {
	                    eIndex = output.length();
	                    output.setSpan(new myClickableSpan(), sIndex, eIndex,
	                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
	                }
	            }
		}
		
	}
	public static class myClickableSpan extends ClickableSpan{

		@SuppressLint("NewApi")
		@Override
		public void onClick(View widget) {
			Log.e("ClickableSpan", widget.getResources().toString());
			
		}
		
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
