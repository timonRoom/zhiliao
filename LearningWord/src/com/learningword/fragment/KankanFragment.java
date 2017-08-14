package com.learningword.fragment;

import java.util.List;

import com.learningword.R;
import com.learningword.activity.ImageReader;
import com.learningword.adapter.KankanListAdapter;
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
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;

public class KankanFragment extends Fragment {
	private Context context;
	private ListView lv_kankan;
	private View include_deflault;
	private View lvfooter;
	public static final int KANKAN_API_RETURN = 0;
	public static final int KANKAN_API_FAILURE = 1;
	public static final int KANKAN_API_LOADMORE = 2;
	private ProgressBar pb_lv_footer;
	private ProgressBar pb_deflault;
	private TextView tv_lv_footer;
	private int lvfooterhight ;
	private List<String> imageurls;
	private int adddatanumber = 10;
	private TextView tv_net_failure;
	public KankanFragment(Context context) {
		super();
		this.context = context;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = View.inflate(context, R.layout.fragment_kankan, null);
		lv_kankan = (ListView) view.findViewById(R.id.lv_kankan);
		lv_kankan.setVisibility(View.GONE);
		lv_kankan.setOnScrollListener(new lisetviewOnScrollListener());
		include_deflault = view.findViewById(R.id.include_deflault);
		tv_net_failure = (TextView) include_deflault.findViewById(R.id.tv_net_failure);
		pb_deflault= (ProgressBar) include_deflault.findViewById(R.id.pb_deflault);
		((View) tv_net_failure.getParent()).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Log.e("KANKAN_API_FAILURE", "onClick");
				if (isfailure) {
					pb_deflault.setVisibility(View.VISIBLE);
					netutils.getkankanapidata(hanhan_handler, false, adddatanumber);
					tv_net_failure.setText("正在重试...");
				}
			}
		});
		initview();
		return view;
	}
	private boolean isfailure = false;
	private Handler hanhan_handler= new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case KANKAN_API_RETURN:
				isfailure = false;
				if (netutils.kankan_image_urls!=null) {
					tv_net_failure.setText("正在加载...");
					include_deflault.setVisibility(View.GONE);
					lv_kankan.setVisibility(View.VISIBLE);
					imageurls=netutils.kankan_image_urls;
					showlistview();
				}
				
				hanhan_handler.removeMessages(KANKAN_API_RETURN);
				break;
				case KANKAN_API_FAILURE:
					isfailure = true;
					pb_deflault.setVisibility(View.GONE);
					tv_net_failure.setText("联网超时,点击任意处重试!");
					hanhan_handler.removeMessages(KANKAN_API_FAILURE);
					break;
				case KANKAN_API_LOADMORE:
					if (netutils.kankan_image_urls!=null) {
						listAdapter.notifyDataSetChanged();
						Toast.makeText(context, "加载成功^_^", Toast.LENGTH_SHORT).show();
						pb_lv_footer.setVisibility(View.GONE);
						tv_lv_footer.setText("点击加载更多");
						lvfooter.setPadding(0, -lvfooterhight, 0, 0);
					}
					hanhan_handler.removeMessages(KANKAN_API_LOADMORE);
					break;
			}
		}
		
	};
	private void initview() {
		lvfooter = View.inflate(context, R.layout.zihu_listview_footer, null);
		pb_lv_footer = (ProgressBar) lvfooter.findViewById(R.id.pb_lv_footer);
		pb_lv_footer.setVisibility(View.GONE);
		tv_lv_footer =  (TextView) lvfooter.findViewById(R.id.tv_lv_footer);
		lv_kankan.addFooterView(lvfooter);
		lvfooter.measure(0, 0);
		lvfooterhight = lvfooter.getMeasuredHeight();
		lvfooter.setPadding(0, -lvfooterhight, 0, 0);
		lvfooter.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				adddatanumber+=10;
				netutils.getkankanapidata(hanhan_handler, true, adddatanumber);
				pb_lv_footer.setVisibility(View.VISIBLE);
				tv_lv_footer.setText("正在加载...");
			}
		});
		netutils.getkankanapidata(hanhan_handler, false, adddatanumber);
	}
	private KankanListAdapter listAdapter;
	protected void showlistview() {
		 listAdapter = new KankanListAdapter(imageurls, context);
		lv_kankan.setAdapter(listAdapter);
		lv_kankan.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View imageView, int arg2, long arg3) {
				Intent intent = new Intent();
				intent.setClass(context, ImageReader.class);
				intent.putExtra("imageurl", imageurls.get(arg2));
				startActivity(intent);
			}
		});
	}
	class lisetviewOnScrollListener implements OnScrollListener {

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			if (scrollState==OnScrollListener.SCROLL_STATE_IDLE||scrollState==OnScrollListener.SCROLL_STATE_FLING) {
				if (lv_kankan.getLastVisiblePosition()>=lv_kankan.getCount()-1) {
					lvfooter.setPadding(8, 8, 8, 8);
				}
			}
		}
		
	}
}
