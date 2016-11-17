package com.sun3d.culturalShanghai.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.download.ImageDownloader.Scheme;
import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.R;
import com.sun3d.culturalShanghai.Util.AppConfigUtil;
import com.sun3d.culturalShanghai.Util.FolderUtil;
import com.sun3d.culturalShanghai.Util.Options;
import com.sun3d.culturalShanghai.Util.PicImgCompressUtil;
import com.sun3d.culturalShanghai.Util.ToastUtil;
import com.sun3d.culturalShanghai.Util.WindowsUtil;
import com.sun3d.culturalShanghai.animation.VibrateAnimation;
import com.sun3d.culturalShanghai.image.ImageLoader;
import com.sun3d.culturalShanghai.image.ImageLoader.Type;
import com.sun3d.culturalShanghai.thirdparty.MyShare;
import com.sun3d.culturalShanghai.thirdparty.ShareName;
import com.sun3d.culturalShanghai.view.PhotoView;
import com.sun3d.culturalShanghai.view.PhotoView.OnPhotoLongPressListener;
import com.sun3d.culturalShanghai.view.ViewPagerFixed;
import com.sun3d.culturalShanghai.windows.ImageViewSaveWindows;
import com.sun3d.culturalShanghai.windows.ImageViewSaveWindows.SaveImgListener;
import com.umeng.analytics.MobclickAgent;

/**
 * 图片放大滑动页面
 * 
 * @author yangyoutao
 * 
 */
public class ImageOriginalActivity extends Activity implements OnClickListener,
		OnPhotoLongPressListener, SaveImgListener {
	private Context mContext;
	private RelativeLayout mTitle;
	private ViewPagerFixed pager;
	private MyPageAdapter adapter;
	// 当前的位置
	private int location = 0;
	// 显示
	private TextView show;
	private ArrayList<PhotoView> listViews = null;
	private List<String> mSelectedImagePath = new LinkedList<String>();
	private final String mPageName = "GroupEditActivity";
	private Boolean isLocation = true;
	@SuppressWarnings("unused")
	private Map<Integer, Boolean> islocalShow = new HashMap<Integer, Boolean>();
	private RelativeLayout mainView;
	private Bitmap longpressBitmap;
	private String longpressStringUrl;

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onPageStart(mPageName);
		MobclickAgent.onResume(mContext);
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(mPageName);
		MobclickAgent.onPause(mContext);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_original);
		MyApplication.getInstance().addActivitys(this);
		mContext = this;
		init();
		setData();
		setTitle();
		initData();
	}

	/**
	 * 加载本地大图
	 */
	private void setLocaImage(int index) {
		islocalShow.put(index, true);
		Bitmap Bit;
		String path = mSelectedImagePath.get(index);
		if (ImageLoader.getInstance() == null) {
			ImageLoader.getInstance(3, Type.LIFO);

		}
		Bit = ImageLoader.getBitmapFromLruCache(path
				+ AppConfigUtil.Local.ImageTag);
		if (Bit == null) {
			MyApplication
					.getInstance()
					.getImageLoader()
					.displayImage(Scheme.FILE.wrap(path), listViews.get(index),
							Options.getListOptions());
		} else {
			listViews.get(index).setImageBitmap(Bit);
		}

	}

	/**
	 * 设置数据
	 */
	private void setData() {
		String select = this.getIntent().getStringExtra("select_imgs");
		Log.d("select", select);
		if (select.indexOf("http") > -1) {
			isLocation = false;
			getServerData(select);
		} else {
			isLocation = true;
			getAddLocaImgs(select);
		}
		Log.d("select", "isLocation:" + isLocation);

	}

	/**
	 * 设置网络图片数据
	 */
	private void getServerData(String select) {
		if (select != null && select.length() > 0) {
			String[] str = select.split(",");
			listViews = new ArrayList<PhotoView>();
			mSelectedImagePath.clear();
			int index = 0;
			for (String strText : str) {
				View Photoview_layout = LayoutInflater.from(mContext).inflate(
						R.layout.comment_imgitem, null);
				PhotoView img_iamgeView = (PhotoView) Photoview_layout
						.findViewById(R.id.imageView1);
				img_iamgeView.enable();
				img_iamgeView.setTag(index);
				img_iamgeView.setOnPhotoLongPressListener(this);
				img_iamgeView.setLayoutParams(new LinearLayout.LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
				MyApplication
						.getInstance()
						.getImageLoader()
						.displayImage(strText, img_iamgeView,
								Options.getListOptions());
				mSelectedImagePath.add(strText);
				listViews.add(img_iamgeView);
				index += 1;
			}
		}

	}

	/**
	 * 设置本地图片的数据
	 */
	private void getAddLocaImgs(String select) {
		if (select != null && select.length() > 0) {
			String[] str = select.split(",");
			listViews = new ArrayList<PhotoView>();
			mSelectedImagePath.clear();
			int index = 0;
			for (String strText : str) {
				islocalShow.put(index, false);
				View Photoview_layout = LayoutInflater.from(mContext).inflate(
						R.layout.comment_imgitem, null);
				PhotoView img_iamgeView = (PhotoView) Photoview_layout
						.findViewById(R.id.imageView1);
				img_iamgeView.enable();
				img_iamgeView.setTag(index);
				img_iamgeView.setOnPhotoLongPressListener(this);
				img_iamgeView.setLayoutParams(new LinearLayout.LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
				if (ImageLoader.getInstance() == null) {
					ImageLoader.getInstance(3, Type.LIFO);
				}
				Bitmap bit = ImageLoader.getBitmapFromLruCache(strText);
				if (bit == null) {
					bit = PicImgCompressUtil.get100Bitmap(strText);
				}
				img_iamgeView.setImageBitmap(bit);
				mSelectedImagePath.add(strText);
				listViews.add(img_iamgeView);
				index += 1;
			}
		}

	}

	private void init() {
		mainView = (RelativeLayout) findViewById(R.id.rl);
		show = (TextView) findViewById(R.id.tv_show);
		pager = (ViewPagerFixed) findViewById(R.id.gallery_page);
		pager.setOnPageChangeListener(pageChangeListener);
		ImageViewSaveWindows.setSaveImgListener(this);
	}

	/**
	 * 数据初始化
	 */
	private void initData() {
		adapter = new MyPageAdapter(listViews);
		pager.setAdapter(adapter);
		int id = this.getIntent().getIntExtra("id", 0);
		pager.setOffscreenPageLimit(8);
		pager.setCurrentItem(id);
		show.setText((id + 1) + "/" + listViews.size());
		if (isLocation) {
			setLocaImage(id);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.image_original, menu);
		return true;
	}

	/**
	 * 设置标题
	 */
	private void setTitle() {
		mTitle = (RelativeLayout) findViewById(R.id.title);
		mTitle.findViewById(R.id.title_left).setOnClickListener(this);
		mTitle.findViewById(R.id.title_right).setVisibility(View.GONE);
		TextView title = (TextView) mTitle.findViewById(R.id.title_content);
		TextView delete = (TextView) mTitle.findViewById(R.id.title_send);
		title.setVisibility(View.GONE);
		if (isLocation) {
			delete.setVisibility(View.VISIBLE);
			delete.setText("删除");
			delete.setTextColor(getResources().getColor(R.color.white_color));
			delete.setOnClickListener(this);
		} else {
			delete.setVisibility(View.GONE);
		}

	}

	/**
	 * 发送图片信息
	 */
	private void sendSelectData() {
		String selects = "";
		Intent backintent = new Intent();
		if (mSelectedImagePath.size() > 0) {
			for (String imgpath : mSelectedImagePath) {
				selects = selects + "," + imgpath;
			}
			selects = selects.substring(1, selects.length());
		} else {
			selects = "";
		}
		backintent.putExtra("select_imgs", selects);
		setResult(30, backintent);
		finish();
	}

	/**
	 * 删除图片
	 */
	private void deleteImage() {
		if (listViews.size() < 2) {
			mSelectedImagePath.clear();
			listViews.clear();
			Log.d("delete", "mSelectedImagePath:" + mSelectedImagePath.size());
			sendSelectData();
		} else {
			pager.removeAllViews();
			listViews.remove(location);
			mSelectedImagePath.remove(location);
			adapter.setListViews(listViews);
			adapter.notifyDataSetChanged();
			show.setText((location + 1) + "/" + listViews.size());
		}

	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.title_left:// 返回
			sendSelectData();
			break;
		case R.id.title_send:// 删除
			deleteImage();
			break;
		default:
			break;
		}
	}

	/**
	 * 滑动监听
	 */
	private OnPageChangeListener pageChangeListener = new OnPageChangeListener() {
		public void onPageSelected(int arg0) {
			longpressStringUrl = null;
			longpressBitmap = null;
			location = arg0;

			show.setText((arg0 + 1) + "/" + listViews.size());
		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		public void onPageScrollStateChanged(int arg0) {
			Log.d("onPageScrollStateChanged", arg0 + "");
			Log.d("location", location + "");
			if (arg0 == 0 && isLocation && !islocalShow.get(location)) {
				setLocaImage(location);
			}

		}
	};

	/**
	 * 适配器
	 * 
	 */
	class MyPageAdapter extends PagerAdapter {

		private ArrayList<PhotoView> listViews;

		private int size;

		public MyPageAdapter(ArrayList<PhotoView> listViews) {
			this.listViews = listViews;
			size = listViews == null ? 0 : listViews.size();
			Log.i("ceshi", "kankan size ==  " + size);
		}

		public void setListViews(ArrayList<PhotoView> listViews) {
			this.listViews = listViews;
			size = listViews == null ? 0 : listViews.size();
		}

		public int getCount() {
			return size;
		}

		public int getItemPosition(Object object) {
			return POSITION_NONE;
		}

		public void destroyItem(View arg0, int arg1, Object arg2) {
			Log.i("ceshi", "tttt=== " + arg1 % size);
			((ViewPagerFixed) arg0).removeView(listViews.get(arg1 % size));
		}

		public void finishUpdate(View arg0) {
		}

		public Object instantiateItem(View arg0, int arg1) {
			try {
				Log.i("ceshi", "kkk=== " + arg1 % size);
				((ViewPagerFixed) arg0).addView(listViews.get(arg1 % size), 0);

			} catch (Exception e) {
			}
			return listViews.get(arg1 % size);
		}

		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

	}

	@Override
	public void onPhotoLongPress(View view, MotionEvent event) {
		// TODO Auto-generated method stub
		WindowsUtil.Vibrate(this, 100);
		ImageViewSaveWindows.showLoadingText(mContext, mainView);
		if (!isLocation) {
			ImageView imgview = (ImageView) view;
			BitmapDrawable bitd = (BitmapDrawable) imgview.getDrawable();
			longpressBitmap = bitd.getBitmap();
		}
		longpressStringUrl = mSelectedImagePath.get((Integer) view.getTag());
	}

	@Override
	public void onclick(int type) {
		// TODO Auto-generated method stub
		switch (type) {
		case ImageViewSaveWindows.click_QQ:
			if (isLocation) {
				if (longpressStringUrl != null) {
					MyShare.showShare(ShareName.QQ, mContext,
							longpressStringUrl);
				}
			} else {
				if (longpressBitmap != null) {
					String locatPath = FolderUtil
							.saveImgeToshare(longpressBitmap);
					MyShare.showShare(ShareName.QQ, mContext, locatPath);
				}
			}

			break;
		case ImageViewSaveWindows.click_weixin:
			if (isLocation) {
				if (longpressStringUrl != null) {
					MyShare.showShare(ShareName.Wechat, mContext,
							longpressStringUrl);
				}
			} else {
				if (longpressBitmap != null) {
					String locatPath = FolderUtil
							.saveImgeToshare(longpressBitmap);
					MyShare.showShare(ShareName.Wechat, mContext, locatPath);
				}
			}
			break;
		case ImageViewSaveWindows.click_savephone:
			if (isLocation) {
				ToastUtil.showToast("本地图片。图片路径：" + longpressStringUrl);
			} else {
				if (longpressBitmap != null) {
					String locatPath = FolderUtil
							.saveImgeToshare(longpressBitmap);
					ToastUtil.showToast("保存成功。图片路径：" + locatPath);
				}
			}
			break;
		default:
			break;
		}
	}

}
