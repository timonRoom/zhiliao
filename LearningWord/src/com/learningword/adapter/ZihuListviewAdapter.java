package com.learningword.adapter;

import java.util.List;

import com.bumptech.glide.Glide;
import com.learningword.R;
import com.learningword.model.NewsModel;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ZihuListviewAdapter extends BaseAdapter {
	private List<NewsModel> newsdatas;
	private Context context;
	public ZihuListviewAdapter(List<NewsModel> newsdatas, Context context) {
		super();
		this.newsdatas = newsdatas;
		this.context = context;
	}


	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return newsdatas.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		NewsModel newsModel = newsdatas.get(position);
		View view;
		viewholder vholder;
		if (convertView==null) {
			view = View.inflate(context, R.layout.activity_mian_listview_item, null);
			vholder = new viewholder();
			vholder.iv_news_image = (ImageView) view.findViewById(R.id.iv_news_image);
			vholder.tv_news_title = (TextView) view.findViewById(R.id.tv_news_title);
			view.setTag(vholder);
		}else {
			view = convertView;
			vholder = (viewholder) view.getTag();
		}
		if (newsModel.isclicked) {
			vholder.tv_news_title.setTextColor(Color.rgb(123, 123, 123));
		}
		vholder.tv_news_title.setText(newsModel.getTitel());
		Glide.with(context)
		.load(newsModel.getImagerurl())
		.placeholder(R.drawable.app)
		.error(R.drawable.image_error)
		.into(vholder.iv_news_image);
		return view;
	}
	class viewholder{
		public ImageView iv_news_image;
		public TextView tv_news_title;
	}

}
