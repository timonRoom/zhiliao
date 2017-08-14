package com.learningword.fragment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.learningword.R;
import com.learningword.activity.ZihuDetailactivity;
import com.learningword.adapter.ZihuListviewAdapter;
import com.learningword.adapter.ZIhuVpAdapter;
import com.learningword.model.NewsModel;
import com.learningword.utils.netutils;
import com.learningword.utils.unitUtils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout.LayoutParams;

public class ZihuFragment extends Fragment {
	private  ViewPager vp_top;
	private  TextView tv_toptitle;
	private  LinearLayout ll_top_point;
	private  Context context;
	private View include_deflault;
	private TextView tv_net_failure;
	private RelativeLayout relativeLayout1;
	private ProgressBar pb_deflault;
	public ZihuFragment(Context context) {
		super();
		this.context = context;
	}
	private  List<NewsModel> top_stories;
	private  ListView lv_mian_pager;
	public static final int API_DATA_RETURN = 0;
	public static final int VP_CAROUSEL = 2;
	public static final int API_LOADMORE = 1;
	public static final int API_FAILURE = 3;
	private int loadingdate;
	//viewPager 轮播时间间隔
	public static  final int vp_time = 5000;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.e("ZihuFragment", "onCreateView");
		View view = View.inflate(context, R.layout.fragment_zihu, null);
		lv_mian_pager = (ListView) view.findViewById(R.id.lv_mian_pager);
		include_deflault = view.findViewById(R.id.include_deflault);
		tv_net_failure = (TextView) include_deflault.findViewById(R.id.tv_net_failure);
		lv_mian_pager .setOnItemClickListener(new listviewlistener());
		lv_mian_pager.setOnScrollListener(new lisetviewOnScrollListener());
		lv_mian_pager.setVisibility(View.GONE);
		View headnews =  View.inflate(context, R.layout.fragment_zihu_listview_headnews, null);
		vp_top = (ViewPager) headnews.findViewById(R.id.vp_top);
		relativeLayout1 = (RelativeLayout) headnews.findViewById(R.id.relativeLayout1);
		tv_toptitle = (TextView) headnews.findViewById(R.id.tv_toptitle);
		ll_top_point = (LinearLayout) headnews.findViewById(R.id.ll_top_point);
		lv_mian_pager.addHeaderView(headnews);
		loadingdate = getsystimes()+1;
		pb_deflault= (ProgressBar) include_deflault.findViewById(R.id.pb_deflault);
		((View) tv_net_failure.getParent()).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Log.e("KANKAN_API_FAILURE", "onClick");
				if (isfailure) {
					pb_deflault.setVisibility(View.VISIBLE);
					netutils.getzihuapidata(zihuhandler, false, 0);
					tv_net_failure.setText("正在重试...");
				}
			}
		});
		initview();
	
		return view;
	}
	class lisetviewOnScrollListener implements OnScrollListener {

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			if (scrollState==OnScrollListener.SCROLL_STATE_IDLE||scrollState==OnScrollListener.SCROLL_STATE_FLING) {
				if (lv_mian_pager.getLastVisiblePosition()>=lv_mian_pager.getCount()-1) {
					lvfooterview.setPadding(8, 8, 8, 8);
				}
			}
		}
		
	}
	class listviewlistener implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			Log.e("onItemClick", position+"");
			if (position!=0) {
				newsdatas.get(position-1).isclicked = true;
				adapter.notifyDataSetChanged();
				Intent intent = new Intent();
				intent.setClass(context, ZihuDetailactivity.class);
				intent.putExtra("news", newsdatas.get(position-1));
				startActivity(intent);
			}
			
		}

	}
	@Override
	public void onResume() {
		Log.e("ZihuFragment", "onResume");
		if (top_stories!=null&&top_stories.size()>0) {
			
			zihuhandler.removeMessages(VP_CAROUSEL);
			zihuhandler.sendEmptyMessageDelayed(VP_CAROUSEL,vp_time);
		}
		super.onResume();
	}
	@Override
	public void onPause() {
		Log.e("ZihuFragment", "onPause");
		zihuhandler.removeMessages(VP_CAROUSEL);
		super.onPause();
	}
	@Override
	public void onDestroy() {
		Log.e("ZihuFragment", "onDestroy");
		zihuhandler.removeCallbacksAndMessages(null);;
		super.onDestroy();
	}
	private View lvfooterview;
	private ProgressBar pb_lv_footer;
	private TextView tv_lv_footer;
	private int lvfooterhight ;
	private void initview() {
		android.widget.RelativeLayout.LayoutParams params =  (android.widget.RelativeLayout.LayoutParams) relativeLayout1.getLayoutParams();
		params.height= unitUtils.getscreenwidth(context);
		relativeLayout1.setLayoutParams(params);
		Log.e("replace", "initview");
		lvfooterview = View.inflate(context, R.layout.zihu_listview_footer, null);
		pb_lv_footer = (ProgressBar) lvfooterview.findViewById(R.id.pb_lv_footer);
		pb_lv_footer.setVisibility(View.GONE);
		tv_lv_footer =  (TextView) lvfooterview.findViewById(R.id.tv_lv_footer);
		lv_mian_pager.addFooterView(lvfooterview);
		lvfooterview.measure(0, 0);
		lvfooterhight = lvfooterview.getMeasuredHeight();
		lvfooterview.setPadding(0, -lvfooterhight, 0, 0);
		lvfooterview.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				pb_lv_footer.setVisibility(View.VISIBLE);
				tv_lv_footer.setText("正在加载...");
				loadingdate= loadingdate-1;
				netutils.getzihuapidata(zihuhandler, true, loadingdate);
			}
		});
		netutils.getzihuapidata(zihuhandler, false, 0);
		
	}
	private boolean isfailure = false;
	private  Handler zihuhandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case API_DATA_RETURN:
				isfailure = false;
				lv_mian_pager.setVisibility(View.VISIBLE);
				include_deflault.setVisibility(View.GONE);
				setlistdata();
				setviewpager();
				zihuhandler.removeMessages(API_DATA_RETURN);
				break;
			case API_FAILURE:
				isfailure = true;
				pb_deflault.setVisibility(View.GONE);
				tv_net_failure.setText("联网超时,点击任意处重试!");
				zihuhandler.removeMessages(API_FAILURE);
				break;
			case VP_CAROUSEL:
//				vp_top.setCurrentItem(item);
				int item = (vp_top.getCurrentItem()+1)%top_stories.size();
				vp_top.setCurrentItem(item);
				zihuhandler.removeMessages(VP_CAROUSEL);
				zihuhandler.sendEmptyMessageDelayed(VP_CAROUSEL,vp_time);
				break;
				case API_LOADMORE:
					if (netutils.zihu_newsdata!=null) {
						newsdatas.addAll(netutils.zihu_newsdata);
						adapter.notifyDataSetChanged();
						Toast.makeText(context, "加载成功^_^", Toast.LENGTH_SHORT).show();
						pb_lv_footer.setVisibility(View.GONE);
						tv_lv_footer.setText("点击加载更多");
						lvfooterview.setPadding(0, -lvfooterhight, 0, 0);
					}
					zihuhandler.removeMessages(API_LOADMORE);
					break;

			}
		}

		};
		private List<NewsModel> newsdatas;
		private ZihuListviewAdapter adapter;
		protected void setlistdata() {
			newsdatas = netutils.zihu_newsdata;
			if (newsdatas!=null) {
				 adapter= new ZihuListviewAdapter(newsdatas , context);
				lv_mian_pager.setAdapter(adapter);
			}
		}
		@SuppressWarnings("deprecation")
		protected void setviewpager() {
			if (netutils.zihu_top_stories!=null) {
				top_stories = netutils.zihu_top_stories;
				for (int i = 0; i < top_stories.size(); i++) {
					ImageView point = new ImageView(context);
					point.setBackgroundResource(R.drawable.point_normal);
					LayoutParams params = new LayoutParams(unitUtils.dip2px(context, 10),unitUtils.dip2px(context, 10));
					if (i!=0) {
						params.leftMargin=unitUtils.dip2px(context, 10);
					}
					point.setLayoutParams(params);
					ll_top_point.addView(point);
				}
				ZIhuVpAdapter adapter = new ZIhuVpAdapter(top_stories, context, zihuhandler);
				vp_top.setAdapter(adapter);
				ll_top_point.getChildAt(0).setBackgroundResource(R.drawable.point_red);
				tv_toptitle.setText(netutils.zihu_top_stories.get(0).getTitel());
				zihuhandler.sendEmptyMessage(VP_CAROUSEL);
				vp_top.setOnPageChangeListener(new OnPageChangeListener() {

					@Override
					public void onPageSelected(int arg0) {
//						Log.e("onPageSelected", arg0+"");
						tv_toptitle.setText(netutils.zihu_top_stories.get(arg0).getTitel());
						for (int j = 0; j < ll_top_point.getChildCount(); j++) {
							if (j==arg0) {
								ll_top_point.getChildAt(j).setBackgroundResource(R.drawable.point_red);
							}else {
								ll_top_point.getChildAt(j).setBackgroundResource(R.drawable.point_normal);
							}
						}
					}

					@Override
					public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

					}
					private boolean isdragging =false;
					@Override
					public void onPageScrollStateChanged(int arg0) {
						if (arg0 ==ViewPager.SCROLL_STATE_DRAGGING ) {
							isdragging=true;
							
						}else if (arg0==ViewPager.SCROLL_STATE_SETTLING&&isdragging) {
							isdragging=false;
							zihuhandler.removeMessages(VP_CAROUSEL);
							zihuhandler.sendEmptyMessageDelayed(VP_CAROUSEL,vp_time);
						}else if (arg0==ViewPager.SCROLL_STATE_IDLE&&isdragging) {
							isdragging=false;
							zihuhandler.removeMessages(VP_CAROUSEL);
							zihuhandler.sendEmptyMessageDelayed(VP_CAROUSEL,vp_time);
						}
					}
				});
			}
		}
		private int getsystimes() {
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");  
	        String t=format.format(new Date());
	        int i= Integer.parseInt(t);  
			return i;  
		};
	}

