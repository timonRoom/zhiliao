package com.learningword.adapter;

import java.util.List;

import com.bumptech.glide.Glide;
import com.learningword.R;
import com.learningword.model.NewsDataModel;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class NewsListAdapter extends BaseAdapter{
	private List<NewsDataModel> newsdatamodels;
	private Context context;
	public NewsListAdapter(List<NewsDataModel> newsdatamodels, Context context) {
		super();
		this.newsdatamodels = newsdatamodels;
		this.context = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return newsdatamodels.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		NewsDataModel newsModel = newsdatamodels.get(position);
		View view;
		viewholder vholder;
		if (convertView==null) {
			view = View.inflate(context, R.layout.fragment_news_listview_item, null);
			vholder = new viewholder();
			vholder.iv_lv_news_image = (ImageView) view.findViewById(R.id.iv_lv_news_image);
			vholder.tv_lv_news_title = (TextView) view.findViewById(R.id.tv_lv_news_title);
			vholder.tv_lv_author = (TextView) view.findViewById(R.id.tv_lv_author);
			vholder.tv_lv_date = (TextView) view.findViewById(R.id.tv_lv_date);
			view.setTag(vholder);
		}else {
			view = convertView;
			vholder = (viewholder) view.getTag();
		}
		vholder.tv_lv_news_title.setText(newsModel.getTitle());
		vholder.tv_lv_author.setText(newsModel.getAuthor_name());
		vholder.tv_lv_date.setText(newsModel.getData());
		if (newsModel.isclicked) {
			vholder.tv_lv_news_title.setTextColor(Color.rgb(123,123,123));
			vholder.tv_lv_author.setTextColor(Color.rgb(123,123,123));
			vholder.tv_lv_date.setTextColor(Color.rgb(123,123,123));
		}
		Glide.with(context)
		.load(newsModel.getImageurl())
		.placeholder(R.drawable.app)
		.error(R.drawable.image_error)
		.into(vholder.iv_lv_news_image);
		return view;
	}
	class viewholder{
		public ImageView iv_lv_news_image;
		public TextView tv_lv_news_title;
		public TextView tv_lv_author;
		public TextView tv_lv_date;
	}

}
