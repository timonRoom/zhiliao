package com.learningword.fragment;

import com.learningword.R;
import com.learningword.activity.MainActivity;
import com.learningword.utils.unitUtils;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class MenuFragment extends Fragment implements OnCheckedChangeListener{
	private Context context;
	private ImageView iv_meum_image;
	private RadioGroup rg_radiogroup;
	private Handler mainactivityhander;
	public MenuFragment(Context context, Handler mainactivityhander) {
		super();
		this.context = context;
		this.mainactivityhander = mainactivityhander;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = View.inflate(context, R.layout.activity_main_menu, null);
		iv_meum_image = (ImageView) view.findViewById(R.id.iv_meum_image);
		rg_radiogroup = (RadioGroup) view.findViewById(R.id.rg_radiogroup);
		rg_radiogroup.setOnCheckedChangeListener(this);
		initmenuview();
		return view;
	}
	private void initmenuview() {
		rg_radiogroup.check(R.id.rb_zihu);
		int screenWidth = unitUtils.getscreenwidth(context)/3*2;
		ViewGroup.LayoutParams lp = iv_meum_image.getLayoutParams();
		lp.width = screenWidth;
		lp.height = LayoutParams.WRAP_CONTENT;
		iv_meum_image.setLayoutParams(lp);
		iv_meum_image.setMaxWidth(screenWidth);
		iv_meum_image.setMaxHeight(screenWidth * 5);
	}
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		Log.e("onCheckedChanged", checkedId+"");
		switch (checkedId) {
		case R.id.rb_zihu:
			mainactivityhander.sendEmptyMessage(MainActivity.SHOW_FG_ZIHU);
			break;
		case R.id.rb_kankan:
			mainactivityhander.sendEmptyMessage(MainActivity.SHOW_FG_kankan);
			break;
		case R.id.rb_news:
			mainactivityhander.sendEmptyMessage(MainActivity.SHOW_FG_news);
			break;
		}
	}

}
