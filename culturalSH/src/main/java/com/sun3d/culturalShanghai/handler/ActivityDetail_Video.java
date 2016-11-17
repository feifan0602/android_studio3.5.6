package com.sun3d.culturalShanghai.handler;

import android.content.Context;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.sun3d.culturalShanghai.R;
import com.sun3d.culturalShanghai.video.MyVideoView;
import com.sun3d.culturalShanghai.video.VideoInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 活动视频
 *
 * @author yangyoutao
 */
public class ActivityDetail_Video {
    private LinearLayout content;
    private Context mContext;
    private MyVideoView mMyVideoView;
    /**
     * 播放信息列表
     */
    private List<VideoInfo> videoPalyList = new ArrayList<VideoInfo>();

    public LinearLayout getContent() {
        return content;
    }

    public ActivityDetail_Video(Context context) {
        this.mContext = context;
        content = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.why_video_layout,
                null);
        mMyVideoView = (MyVideoView) content.findViewById(R.id.whyvideo);
    }

    public void addVideoData(List<VideoInfo> videoPalyList) {// 静态数据
//		String TEST_PLAY_URL = "http://v.iask.com/v_play_ipad.php?vid=138152839";
//		String TEST_PLAY_URL2 = "http://v.iask.com/v_play_ipad.php?vid=138116139";
//		String TEST_PLAY_TITLE = "(GTA5)闪电侠席卷圣洛城";
//		String TEST_PLAY_TITLE2 = "DOTA2-TI5国际邀请赛";
//		String urlimg = "http://d.hiphotos.baidu.com/image/pic/item/a2cc7cd98d1001e9b230cf71ba0e7bec54e79744.jpg";
//		String urling2 = "http://imgsrc.baidu.com/forum/w%3D580/sign=4b286c1b4fed2e73fce98624b700a16d/b5cdbede9c82d158b1edda2e850a19d8bd3e4202.jpg";
//		VideoInfo vi = new VideoInfo();
//		vi.setVideo_img(urlimg);
//		vi.setVideo_title(TEST_PLAY_TITLE);
//		vi.setVideo_url(TEST_PLAY_URL);
//		videoPalyList.add(vi);
//		VideoInfo vi1 = new VideoInfo();
//		vi1.setVideo_img(urling2);
//		vi1.setVideo_title(TEST_PLAY_TITLE2);
//		vi1.setVideo_url(TEST_PLAY_URL2);
//		videoPalyList.add(vi1);
        mMyVideoView.initPlatyUrlList(videoPalyList);


//		for ()
    }

    public void onResume() {
        mMyVideoView.onResume();
    }

    public void onDestroy() {
        mMyVideoView.onDestroy();
    }

    public void onStop() {
        mMyVideoView.onStop();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return mMyVideoView.onKeyDown(keyCode, event);
    }

    public int getVideoConfiguration() {
        return mMyVideoView.getVideoConfiguration();
    }

    public void onPause() {
        mMyVideoView.onPause();
    }
}
