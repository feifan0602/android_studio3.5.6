package com.sun3d.culturalShanghai.handler;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.R;
import com.sun3d.culturalShanghai.Mp3.Player;
import com.sun3d.culturalShanghai.Util.TextUtil;
import com.sun3d.culturalShanghai.Util.ToastUtil;

public class VenueDetailMusicHandler implements OnClickListener {
	private SeekBar mSeekBar;
	private ImageView PlayMusic;
	private Boolean isPlay = false;
	private Player mPlayer;
	private String Musicurl;
	private Boolean isStartPlay = false;
	private LinearLayout maincontanrl;
	private TextView show;
	private int showWidth = 0;

	public VenueDetailMusicHandler(LinearLayout contanrl) {
		isPlay = false;
		maincontanrl = contanrl;
		View musicView = (View) contanrl.findViewById(R.id.venue_muisc);
		PlayMusic = (ImageView) musicView.findViewById(R.id.music_paly);
		mSeekBar = (SeekBar) musicView.findViewById(R.id.music_seekbar);
		show = (TextView) musicView.findViewById(R.id.textshow);
		PlayMusic.setOnClickListener(this);
		mPlayer = new Player(mSeekBar, show);
		mSeekBar.setOnSeekBarChangeListener(new SeekBarChangeEvent());
		showWidth = MyApplication.getWindowWidth() - TextUtil.getViewWidth(show) + 20;
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	class SeekBarChangeEvent implements OnSeekBarChangeListener {
		int progress;

		@TargetApi(Build.VERSION_CODES.HONEYCOMB)
		@SuppressLint("NewApi")
		@Override
		public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
			if (mPlayer == null | mPlayer.mediaPlayer == null) {
				return;
			}
			this.progress = progress * mPlayer.mediaPlayer.getDuration() / seekBar.getMax();
			float x = seekBar.getWidth();
			float seekbarWidth = seekBar.getX();
			float y = seekBar.getY();
			float width = (progress * x) / 100 + seekbarWidth;
			if (width >= showWidth) {
				width = showWidth;
			}
			Log.d("width", width + "");
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

	/**
 * 
 */
	public void setMusicUrl(String url) {
		if (url != null && url.length() > 0) {
			Musicurl = url;

		} else {
			maincontanrl.setVisibility(View.GONE);
		}

	}

	/**
	 * 开始播放
	 * 
	 * @param url
	 */
	public void startMusic() {
		if (Musicurl == null | Musicurl.trim().length() == 0) {
			ToastUtil.showToast("播放地址不存在");
			return;
		}
		new Thread(new Runnable() {

			@Override
			public void run() {
				mPlayer.playUrl(Musicurl);

			}
		}).start();
	}

	/**
	 * 停止播放
	 */
	public void stopMusic() {
		if (mPlayer != null) {
			mPlayer.stop();
			mPlayer = null;
		}
	}

	/**
	 * 暂停播放
	 */
	public void pause() {
		if (mPlayer != null) {
			mPlayer.pause();
		}
	}

	/**
	 * 恢复播放
	 */
	public void play() {
		if (mPlayer != null) {
			mPlayer.play();
		}
	}

	/**
	 * 设置和判断是否播放还是暂停
	 */
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
			PlayMusic.setImageResource(R.drawable.sh_icon_pause);
		}
		isPlay = !isPlay;

	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.music_paly:// 开始
			setPlay();
			break;
		default:
			break;
		}
	}
}
