package com.sun3d.culturalShanghai.activity;

import java.util.HashMap;
import java.util.Map;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.R;
import com.sun3d.culturalShanghai.Mp3.Player;
import com.sun3d.culturalShanghai.Util.AppConfigUtil;
import com.sun3d.culturalShanghai.Util.JsonUtil;
import com.sun3d.culturalShanghai.Util.Options;
import com.sun3d.culturalShanghai.Util.TextUtil;
import com.sun3d.culturalShanghai.Util.ViewUtil;
import com.sun3d.culturalShanghai.handler.LoadingHandler;
import com.sun3d.culturalShanghai.http.HttpCode;
import com.sun3d.culturalShanghai.http.HttpRequestCallback;
import com.sun3d.culturalShanghai.http.HttpUrlList;
import com.sun3d.culturalShanghai.http.MyHttpRequest;
import com.sun3d.culturalShanghai.object.CollectionInfor;
import com.sun3d.culturalShanghai.view.pulltozoomscrollview.PullToZoomScrollViewEx;
import com.umeng.analytics.MobclickAgent;

/**
 * 藏品展示 图片可以放大缩小
 * 
 * @author wenff
 * 
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class CollcetionDetailsActivity extends Activity implements
		OnClickListener {
	private String TAG = "CollcetionDetailsActivity";
	private Context mContext;
	private SeekBar mSeekBar;
	private ImageView PlayMusic;
	private Boolean isPlay = false;
	private Player mPlayer;
	private Boolean isStartPlay = false;
	private WebView mydetail;
	private PullToZoomScrollViewEx scrollView;
	private View contentView;
	private String Id = "123";
	private ImageView zoomView;
	private LoadingHandler mLoadingHandler;
	private CollectionInfor mCollectionInfor;
	private RelativeLayout musicLayout;
	private TextView show;
	private final String mPageName = "CollcetionDetailsActivity";
	private int showWidth = 0;

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
		setContentView(R.layout.activity_collcetion_details);
		MyApplication.getInstance().addActivitys(this);
		Id = this.getIntent().getExtras().getString("Id");
		mContext = this;
		init();
	}

	private void init() {
		setTitle();
		RelativeLayout loadingLayout = (RelativeLayout) findViewById(R.id.loading);
		mLoadingHandler = new LoadingHandler(loadingLayout);
		mLoadingHandler.startLoading();
		scrollView = (PullToZoomScrollViewEx) findViewById(R.id.scroll_view);
		View headView = LayoutInflater.from(this).inflate(
				R.layout.event_detail_collectlayout, null, false);
		headView.findViewById(R.id.event_collect).setVisibility(View.GONE);
		scrollView.setHeaderView(headView);
		zoomView = (ImageView) LayoutInflater.from(this).inflate(
				R.layout.scrollview_profile_zoom_view, null, false);
		contentView = LayoutInflater.from(this).inflate(
				R.layout.activitycollcetion_scrollcontent_layout, null, false);
		scrollView.setZoomView(zoomView);
		scrollView.setScrollContentView(contentView);
		musicLayout = (RelativeLayout) contentView.findViewById(R.id.music);
		mSeekBar = (SeekBar) musicLayout.findViewById(R.id.music_seekbar);
		PlayMusic = (ImageView) musicLayout.findViewById(R.id.music_paly);
		show = (TextView) musicLayout.findViewById(R.id.textshow);
		PlayMusic.setOnClickListener(this);
		mPlayer = new Player(mSeekBar, show);
		mydetail = (WebView) contentView.findViewById(R.id.detail);
		ViewUtil.setWebViewSettings(mydetail, mContext);
		mSeekBar.setOnSeekBarChangeListener(new SeekBarChangeEvent());
		addData();
		showWidth = MyApplication.getWindowWidth()
				- TextUtil.getViewWidth(show) + 20;
	}

	private void addData() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("antiqueId", Id);
		MyHttpRequest.onHttpPostParams(
				HttpUrlList.Collection.COLLECTION_DETAIL_GETURL, params,
				new HttpRequestCallback() {
					@Override
					public void onPostExecute(int statusCode, String resultStr) {
						// TODO Auto-generated method stub
						Log.d(TAG, resultStr);
						if (HttpCode.HTTP_Request_Success_CODE == statusCode) {
							mCollectionInfor = JsonUtil
									.getCollectionDetailInfo(resultStr);
							setData();
						} else {
							mLoadingHandler.showErrorText(resultStr);
						}
					}
				});

	}

	private void setData() {
		if (null != mCollectionInfor.getId()) {
			TextView name = (TextView) contentView.findViewById(R.id.name);
			TextView time = (TextView) contentView.findViewById(R.id.time);
			TextView spec = (TextView) contentView.findViewById(R.id.spec);
			TextView venue = (TextView) contentView.findViewById(R.id.venue);
			name.setText(mCollectionInfor.getCollectionName());
			time.setText("时        间 : " + mCollectionInfor.getCollectionTime());
			spec.setText("藏品类型 : " + mCollectionInfor.getCollectionSpec());
			venue.setText("藏        馆 : "
					+ mCollectionInfor.getCollectionVeune());
			// detailSHow.setText("简介："+DeleHtmlUtil.delHTMLTag(mCollectionInfor.getCollectionInfor()));
			// mydetail.setVisibility(View.GONE);
			if (mCollectionInfor.getCollectionMP3url() == null
					|| mCollectionInfor.getCollectionMP3url().trim().length() == 0) {
				musicLayout.setVisibility(View.GONE);
			} else {
				musicLayout.setVisibility(View.VISIBLE);
			}
			MyApplication
					.getInstance()
					.getImageLoader()
					.displayImage(
							TextUtil.getUrlMiddle(mCollectionInfor
									.getCollectionImgUrl()), zoomView,
							Options.getListOptions());
			mydetail.loadDataWithBaseURL(null, ViewUtil.initContent(
					mCollectionInfor.getCollectionInfor(), mContext),
					AppConfigUtil.APP_MIMETYPE, AppConfigUtil.APP_ENCODING,
					null);
			mLoadingHandler.stopLoading();
		} else {
			mLoadingHandler.showErrorText("数据加载失败！");
		}
		scrollView.scrollBy(0, 0);
	}

	/**
	 * 全角转半角
	 * 
	 * @param input
	 *            String.
	 * @return 半角字符串
	 * */
	public static String ToDBC(String input) {
		char[] c = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == 12288) {
				c[i] = (char) 32;
				continue;
			}
			if (c[i] > 65280 && c[i] < 65375)
				c[i] = (char) (c[i] - 65248);
		}
		return new String(c);
	}

	class SeekBarChangeEvent implements OnSeekBarChangeListener {
		int progress;

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			// 原本是(progress/seekBar.getMax())*player.mediaPlayer.getDuration()
			this.progress = progress * mPlayer.mediaPlayer.getDuration()
					/ seekBar.getMax();
			float x = seekBar.getWidth();
			float seekbarWidth = seekBar.getX();
			float y = seekBar.getY();
			float width = (progress * x) / 100 + seekbarWidth;
			if (width >= showWidth) {
				width = showWidth;
			}
			show.setX(width);
			show.setY(y);
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {

		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			// seekTo()的参数是相对与影片时间的数字，而不是与seekBar.getMax()相对的数字
			mPlayer.mediaPlayer.seekTo(progress);
		}

	}

	private void startMusic() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				mPlayer.playUrl(mCollectionInfor.getCollectionMP3url());
			}
		}).start();
	}

	private void stopMusic() {
		if (mPlayer != null) {
			mPlayer.stop();
			mPlayer = null;
		}
	}

	private void pause() {
		if (mPlayer != null) {
			mPlayer.pause();
		}
	}

	private void play() {
		if (mPlayer != null) {
			mPlayer.play();
		}
	}

	/**
	 * 设置标题
	 */
	private void setTitle() {
		RelativeLayout mTitle = (RelativeLayout) findViewById(R.id.title);
		mTitle.findViewById(R.id.title_left).setOnClickListener(this);
		ImageButton mTitleRight = (ImageButton) mTitle
				.findViewById(R.id.title_right);
		// mTitleRight.setOnClickListener(this);
		// mTitleRight.setImageResource(R.drawable.sh_icon_title_share);
		mTitleRight.setVisibility(View.GONE);
		TextView title = (TextView) mTitle.findViewById(R.id.title_content);
		title.setText("藏品展示");
		title.setVisibility(View.VISIBLE);
	}

	private void setPlay() {
		if (isPlay) {
			pause();
			PlayMusic.setImageResource(R.drawable.sh_icon_play);
		} else {
			if (!isStartPlay) {
				startMusic();
				isStartPlay = true;
			} else {
				play();
			}
			// ToastUtil.showToast("时间:" + mMusicPlayerUtil.getTime());

			PlayMusic.setImageResource(R.drawable.sh_icon_pause);
		}
		isPlay = !isPlay;

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.collcetion_details, menu);
		return true;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		stopMusic();
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.music_paly:
			setPlay();
			break;

		default:
			finish();
			break;
		}
	}

}
