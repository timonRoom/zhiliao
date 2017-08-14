package com.learningword.fragment;

import com.learningword.R;
import com.learningword.activity.NewsDetailActivity;
import com.learningword.adapter.NewsListAdapter;
import com.learningword.myview.RefreshListview;
import com.learningword.myview.RefreshListview.OnRefreshListener;
import com.learningword.utils.netutils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ProgressBar;
import android.widget.TextView;

public class NewsFragment extends Fragment {
	private Context context;
	private  RefreshListview lv_news_listview;
	private View include_deflault;
	private TextView tv_net_failure;
	private ProgressBar pb_deflault;
	public NewsFragment(Context context) {
		super();
		this.context = context;
	}
	private boolean isfailure = false;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = View.inflate(context, R.layout.fragment_news, null);
		lv_news_listview = (RefreshListview) view.findViewById(R.id.lv_news_listview);
		include_deflault = view.findViewById(R.id.include_deflault);
		lv_news_listview.setVisibility(View.GONE);
		pb_deflault= (ProgressBar) include_deflault.findViewById(R.id.pb_deflault);
		tv_net_failure = (TextView) include_deflault.findViewById(R.id.tv_net_failure);
		((View) tv_net_failure.getParent()).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Log.e("KANKAN_API_FAILURE", "onClick");
				if (isfailure) {
					pb_deflault.setVisibility(View.VISIBLE);
					getnewsdata();
					tv_net_failure.setText("正在重试...");
				}
			}
		});
		lv_news_listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				if (position!=0) {
					netutils.newsDataModels.get(position-1).isclicked = true;
					listAdapter.notifyDataSetChanged();
					Intent intent =  new Intent();
					intent.putExtra("news_url", netutils.newsDataModels.get(position-1).getUrl());
					intent.setClass(context, NewsDetailActivity.class);
					startActivity(intent);
				}
			}
		});
		lv_news_listview.SetOnRefreshListener(new myOnRefreshListener());
		getnewsdata();
		return view;
	}
	class myOnRefreshListener implements OnRefreshListener{

		@Override
		public void OnstartRefresh() {
			// TODO Auto-generated method stub
			getnewsdata();
		}
		
	}
	public static final int NEWS_DATA_RETURN = 0;
	public static final int NEWS_DATA_FAILURE = 1;
	private Handler news_hander = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case NEWS_DATA_RETURN:
				isfailure =false;
				lv_news_listview.setVisibility(View.VISIBLE);
				include_deflault.setVisibility(View.GONE);
				setlistview();
				break;
				case NEWS_DATA_FAILURE:
					isfailure = true;
					pb_deflault.setVisibility(View.GONE);
					tv_net_failure.setText("联网超时,点击任意处重试!");
					news_hander.removeMessages(NEWS_DATA_FAILURE);
					break;
			}
		}
		
	};
	private void getnewsdata() {
		netutils.getnewsapidata(news_hander);
	}
	private NewsListAdapter listAdapter;
	protected void setlistview() {
		if (netutils.newsDataModels!=null) {
			 listAdapter = new NewsListAdapter(netutils.newsDataModels, context);
			lv_news_listview.setAdapter(listAdapter);
			lv_news_listview.onpulldownrefresh();
		}
	}
}
