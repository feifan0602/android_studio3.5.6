package com.sun3d.culturalShanghai.Mp3;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.TextView;

import com.sun3d.culturalShanghai.Util.TextUtil;
import com.sun3d.culturalShanghai.Util.ToastUtil;

/**
 * 音频播放类
 * 
 * @author yangyoutao
 * 
 */
public class Player implements OnBufferingUpdateListener, OnCompletionListener, OnPreparedListener {

	public MediaPlayer mediaPlayer; // 媒体播放器
	private SeekBar seekBar; // 拖动条
	private Timer mTimer = new Timer(); // 计时器
	private TextView tvshow;

	/**
	 * 初始化播放器
	 */
	public Player(SeekBar seekBar, TextView tvshow) {
		super();
		this.seekBar = seekBar;
		this.tvshow = tvshow;
		try {
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);// 设置媒体流类型
			mediaPlayer.setOnBufferingUpdateListener(this);
			mediaPlayer.setOnPreparedListener(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 每一秒触发一次
		mTimer.schedule(timerTask, 0, 1000);
	}

	/**
	 * 计时器
	 */
	TimerTask timerTask = new TimerTask() {

		@Override
		public void run() {
			if (mediaPlayer == null)
				return;
			if (mediaPlayer.isPlaying() && seekBar.isPressed() == false) {
				handler.sendEmptyMessage(0); // 发送消息
			}
		}
	};
	/**
	 * 更改进度条的数据
	 */
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			int position = mediaPlayer.getCurrentPosition();
			int duration = mediaPlayer.getDuration();
			if (duration > 0) {
				// 计算进度（获取进度条最大刻度*当前音乐播放位置 / 当前音乐时长）
				long pos = seekBar.getMax() * position / duration;
				seekBar.setProgress((int) pos);
				if (tvshow != null) {
					tvshow.setText(TextUtil.getDurationTime(position));
				}
			}
		};
	};

	/**
	 * 开始播放
	 */
	public void play() {
		mediaPlayer.start();
	}

	/**
	 * url地址 设置
	 * 
	 * @param url
	 * 
	 */
	public void playUrl(String url) {
		Looper.prepare();
		if (url != null && url.length() > 0) {
			try {
				mediaPlayer.reset();
				mediaPlayer.setDataSource(url); // 设置数据源
				mediaPlayer.prepareAsync(); // prepare自动播放
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		} else {
			ToastUtil.showToast("没有播放地址");
		}
		Looper.loop();

	}

	/**
	 * 暂停
	 */
	public void pause() {
		mediaPlayer.pause();
	}

	/**
	 * 停止
	 */
	public void stop() {
		if (mediaPlayer != null) {
			mTimer.cancel();
			timerTask.cancel();
			mediaPlayer.stop();
			try {
				mediaPlayer.release();
			} catch (Exception e) {
				e.printStackTrace();
			}

			mediaPlayer = null;
		}
	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		mp.start();
		Log.e("mediaPlayer", "onPrepared");
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		Log.e("mediaPlayer", "onCompletion");
	}

	/**
	 * 缓冲更新
	 */
	@Override
	public void onBufferingUpdate(MediaPlayer mp, int percent) {
		seekBar.setSecondaryProgress(percent);
		if (mediaPlayer.getDuration()!=0) {
			int currentProgress = seekBar.getMax() * mediaPlayer.getCurrentPosition()
					/ mediaPlayer.getDuration();
		}else {
			
		}
		
//		Log.e(currentProgress + "% play", percent + " buffer");

	}

}
