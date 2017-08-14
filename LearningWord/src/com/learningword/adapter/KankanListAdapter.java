package com.learningword.adapter;

import java.util.List;

import com.bumptech.glide.Glide;
import com.learningword.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class KankanListAdapter extends BaseAdapter{
	private List<String > images;
	private Context context;
	public KankanListAdapter(List<String> images, Context context) {
		super();
		this.images = images;
		this.context = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return images.size();
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
		String imageurl = images.get(position);
		View view;
		viewholder viewholder;
		if (convertView!=null) {
			view = convertView;
			viewholder = (com.learningword.adapter.KankanListAdapter.viewholder) view.getTag();
		}else {
			view = View.inflate(context, R.layout.fragment_kankan_listview_item, null);
			viewholder = new viewholder();
			viewholder.iv_lv_kankan_image = (ImageView) view.findViewById(R.id.iv_lv_kankan_image);
			view.setTag(viewholder);
		}
		Glide.with(context)
		.load(imageurl)
		.thumbnail(0.1f)
		.placeholder(R.drawable.app)
		.error(R.drawable.image_error)
		.into(viewholder.iv_lv_kankan_image);
		return view;
	}
	class viewholder{
		private ImageView iv_lv_kankan_image;
	}

}
