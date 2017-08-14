package com.learningword.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.learningword.activity.ImageReader;
import com.learningword.activity.MainActivity;
import com.learningword.activity.NewsDetailActivity;
import com.learningword.activity.ZihuDetailactivity;
import com.learningword.fragment.KankanFragment;
import com.learningword.fragment.NewsFragment;
import com.learningword.fragment.ZihuFragment;
import com.learningword.model.NewsDataModel;
import com.learningword.model.NewsDetailModel;
import com.learningword.model.NewsModel;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import android.R.string;
import android.opengl.EGL14;
import android.os.Environment;
import android.os.Handler;
import android.os.StatFs;
import android.util.Log;


public class netutils {
	private static final String ZIHU_NEWS_API_TOTAL = "https://news-at.zhihu.com/api/4/news/latest";
	private  static final String ZIHU_NEWS_API_DETAIL = "https://news-at.zhihu.com/api/4/news/";
	private  static final String ZIHU_NEWS_API_MORE ="https://news-at.zhihu.com/api/4/news/before/";
	private static final String NEWS_NEWS_API = "http://v.juhe.cn/toutiao/index?type=top&key=";
	private static final String NEWS_APIKEY="c7a4627293ae2099285d863c27d54fd8";
	private static final String KANKAN_API = "http://gank.io/api/data/福利/";
	public static List<NewsModel> zihu_newsdata=null;
	public static List<NewsModel> zihu_top_stories=null;
	public static void getzihuapidata(final Handler zihuhandler,final boolean isloadmore,int i) {
		String url;
		if (isloadmore) {
			url = ZIHU_NEWS_API_MORE+i;
		}else {
			url=ZIHU_NEWS_API_TOTAL;
		}
		OkHttpClient oClient = new OkHttpClient();
		oClient.setConnectTimeout(5, TimeUnit.SECONDS);
		final Request  request = new Request.Builder()
				.url(url)
				.build();
		com.squareup.okhttp.Call call = oClient.newCall(request);
		call.enqueue(new Callback() {
			@Override
			public void onResponse(Response response) throws IOException {
				String result = response.body().string();
				Log.e("result>>>>", result);
				 try {
					parsejson(result,isloadmore);
					if (isloadmore) {
						zihuhandler.sendEmptyMessage(ZihuFragment.API_LOADMORE);
					}else {
						
						zihuhandler.sendEmptyMessage(ZihuFragment.API_DATA_RETURN);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			
			@Override
			public void onFailure(Request request, IOException e) {
				Log.e("getzihuapidata onFailure>>>>", e.getMessage());
				zihuhandler.sendEmptyMessage(ZihuFragment.API_FAILURE);
				
			}
		});
	
	}
	public static boolean isNetfailure =false;
	public static void parsejson(String data,boolean isloadmore) throws JSONException{
		 zihu_newsdata = new ArrayList<NewsModel>();
		JSONObject jObject = new JSONObject(data);
		JSONArray stories = jObject.getJSONArray("stories");
		for (int i = 0; i < stories.length(); i++) {
			NewsModel nModel = new NewsModel();
			JSONObject object = (JSONObject) stories.get(i);
			nModel.setId(object.getString("id"));
			nModel.setTitel(object.getString("title"));
			JSONArray jsonArray = object.getJSONArray("images");
			String im = (String) jsonArray.get(0);
			nModel.setImagerurl(im);
			zihu_newsdata.add(nModel);
		}
		if (!isloadmore) {
			 zihu_top_stories = new ArrayList<NewsModel>();
			JSONArray top = jObject.getJSONArray("top_stories");
			for (int i = 0; i < top.length(); i++) {
				NewsModel nModel = new NewsModel();
				JSONObject object = (JSONObject) top.get(i);
				nModel.setId(object.getString("id"));
				nModel.setTitel(object.getString("title"));
				nModel.setImagerurl(object.getString("image"));
				zihu_top_stories.add(nModel);
				Log.e("top_stories>>>", zihu_top_stories.size()+"");
			}
		}
		
	}

	public static void getnewsimage(final Handler newsdetail_handler, String id) {
		OkHttpClient oClient = new OkHttpClient();
		oClient.setConnectTimeout(5, TimeUnit.SECONDS);
		final Request  request = new Request.Builder()
				.url(ZIHU_NEWS_API_DETAIL+id)
//				.url("http://mini.eastday.com/mobile/170728153438742.html")
				.build();
		com.squareup.okhttp.Call call = oClient.newCall(request);
		call.enqueue(new Callback() {
			
			@Override
			public void onResponse(Response arg0) throws IOException {
				String reString=arg0.body().string();
				try {
					parsejsondetail(reString);
					newsdetail_handler.sendEmptyMessage(ZihuDetailactivity.GETNEWSDETAILS);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			@Override
			public void onFailure(Request arg0, IOException arg1) {
				newsdetail_handler.sendEmptyMessage(ZihuDetailactivity.NET_FAILURE);
			}
		});
			
	}
	public static String testurl;
	public static NewsDetailModel zihu_newsDetailModel;
	protected static void parsejsondetail(String reString) throws JSONException {
//		testurl = reString;
//		Log.e("testurl>>>", testurl);
		zihu_newsDetailModel = new NewsDetailModel();
		JSONObject json = new JSONObject(reString);
		zihu_newsDetailModel.setBody(json.getString("body"));
		zihu_newsDetailModel.setImageuri(json.getString("image"));
		zihu_newsDetailModel.setTitle(json.getString("title"));
		
	}
//	public static void getwangyinewsimage() {
//		OkHttpClient oClient = new OkHttpClient();
//		final Request  request = new Request.Builder()
//				.url("http://wangyi.butterfly.mopaasapp.com/detail/api?simpleId=21572")
//				.build();
//		com.squareup.okhttp.Call call = oClient.newCall(request);
//		call.enqueue(new Callback() {
//			
//			@Override
//			public void onResponse(Response arg0) throws IOException {
//				String reString=arg0.body().string();
//				Log.e("onResponse", reString);
//			}
//			
//			@Override
//			public void onFailure(Request arg0, IOException e) {
//				Log.e("onFailure", e.toString());
//			}
//		});
//			
//	}

	public static void getnewsapidata(final Handler news_hander) {
		OkHttpClient okHttpClient = new OkHttpClient();
		okHttpClient.setConnectTimeout(5, TimeUnit.SECONDS);
		final Request request = new Request.Builder()
				.url(NEWS_NEWS_API+NEWS_APIKEY)
				.build();
		Call call = okHttpClient.newCall(request);
		call.enqueue(new Callback() {
			
			@Override
			public void onResponse(Response arg0) throws IOException {
				String result = arg0.body().string();
				try {
					parsenewsresult(result);
					news_hander.sendEmptyMessage(NewsFragment.NEWS_DATA_RETURN);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			@Override
			public void onFailure(Request arg0, IOException arg1) {
				Log.e("getnewsapidata onFailure>>>>", arg1.getMessage());
				news_hander.sendEmptyMessage(NewsFragment.NEWS_DATA_FAILURE);
			}
		});
	}
public static List<NewsDataModel> newsDataModels;
	protected static void parsenewsresult(String json) throws JSONException {
		JSONObject  object = new JSONObject(json);
		JSONObject result = object.getJSONObject("result");
		JSONArray data = result.getJSONArray("data");
		newsDataModels = new ArrayList<NewsDataModel>();
		for (int i = 0; i < data.length(); i++) {
			NewsDataModel dModel = new NewsDataModel();
			JSONObject item = (JSONObject) data.get(i);
			dModel.setData(item.getString("date"));
			dModel.setAuthor_name(item.getString("author_name"));
			dModel.setTitle(item.getString("title"));
			dModel.setUrl(item.getString("url"));
			dModel.setImageurl(item.getString("thumbnail_pic_s"));
			newsDataModels.add(dModel);
		}
	}

	public static void getkankanapidata(final Handler hanhan_handler,final boolean isloadmore,final int number) {
		OkHttpClient okHttpClient =  new OkHttpClient();
		okHttpClient.setConnectTimeout(4, TimeUnit.SECONDS);
		Request request = new Request.Builder()
				.url(KANKAN_API+number+"/1")
				.build();
		Call call = okHttpClient.newCall(request);
		call.enqueue(new Callback() {
			
			@Override
			public void onResponse(Response arg0) throws IOException {
					String result = arg0.body().string();
					try {
						parsekankan(result, isloadmore, number);
						if (isloadmore) {
							hanhan_handler.sendEmptyMessage(KankanFragment.KANKAN_API_LOADMORE);

						}else {
							
							hanhan_handler.sendEmptyMessage(KankanFragment.KANKAN_API_RETURN);
						}
					} catch (JSONException e) {
						
						e.printStackTrace();
					}
			}
			
			@Override
			public void onFailure(Request arg0, IOException arg1) {
				hanhan_handler.sendEmptyMessage(KankanFragment.KANKAN_API_FAILURE);
			}
		});
	}
	public static List<String> kankan_image_urls;
	protected static void parsekankan(String result,boolean isloadmore,int number) throws JSONException {
		int num = number-10;
		if (isloadmore) {
		}else {
			kankan_image_urls = new ArrayList<String>();
		}
		JSONObject data = new JSONObject(result);
		JSONArray  results = data.getJSONArray("results");
		
		for (int i = num; i < results.length(); i++) {
			JSONObject object = (JSONObject) results.get(i);
			kankan_image_urls.add(object.getString("url"));
		}
	}
	public static String news_detail_text;
	public static void getnewsdetail(final Handler news_details_hander, String newsurl) {
		OkHttpClient okHttpClient =  new OkHttpClient();
		okHttpClient.setConnectTimeout(5, TimeUnit.SECONDS);
		Request request = new Request.Builder()
				.url(newsurl)
				.build();
		Call call = okHttpClient.newCall(request);
		call.enqueue(new Callback() {
			
			@Override
			public void onResponse(Response response) throws IOException {
				news_detail_text = response.body().string();
				news_details_hander.sendEmptyMessage(NewsDetailActivity.NEWS_DETAIL_RETURN);
			}
			
			@Override
			public void onFailure(Request request, IOException e) {
				news_details_hander.sendEmptyMessage(NewsDetailActivity.NEWS_DETAIL_FAILURE);
			}
		});
	}

	public static void downloadfile(String imagerurl,final String dir,final String filename, final Handler image_hander) {
		OkHttpClient okHttpClient =  new OkHttpClient();
		okHttpClient.setConnectTimeout(5, TimeUnit.SECONDS);
		Request request = new Request.Builder()
				.url(imagerurl)
				.build();
		Call call = okHttpClient.newCall(request);
		call.enqueue(new Callback() {
			@Override
			public void onFailure(Request request, IOException e) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onResponse(Response response) throws IOException {
				if (1 > freeSpaceOnSd()) {
					return;
				}
				if (!Environment.MEDIA_MOUNTED.equals(Environment
						.getExternalStorageState()))
					return;
				File dirPath = new File(dir);
				if (!dirPath.exists()) {
					dirPath.mkdirs();
				}
				File file = new File(dir + "/" + filename);
				   InputStream is = null;
		            byte[] buf = new byte[1024];
		            int len = 0;
		            FileOutputStream fos = null;
		            try {
		                is = response.body().byteStream();
		                fos = new FileOutputStream(file);
		                while ((len = is.read(buf)) != -1) {
		                    fos.write(buf, 0, len);
		                }
		                fos.flush();
		                image_hander.sendEmptyMessage(ImageReader.IMAGE_DOWNLOAD);
		            } catch (IOException e) {
		                e.printStackTrace();
		            } finally {
		                if (is != null) is.close();
		                if (fos != null) fos.close();
		            }
			}
	    });
	}
	/** * 计算sdcard上的剩余空间 * @return */
	private static int freeSpaceOnSd() {
		StatFs stat = new StatFs(Environment.getExternalStorageDirectory()
				.getPath());
		double sdFreeMB = ((double) stat.getAvailableBlocks() * (double) stat
				.getBlockSize()) / (1024*1024);

		return (int) sdFreeMB;
	}
	
}
