package com.sun3d.culturalShanghai.handler;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.R;
import com.sun3d.culturalShanghai.Util.ButtonUtil;
import com.sun3d.culturalShanghai.Util.NetWorkUtil;
import com.sun3d.culturalShanghai.Util.ToastUtil;

import pl.droidsonroids.gif.GifImageView;

/**
 * 这个是 loading页面 进行loading的开始，停止
 *
 * @author wenff
 */
public class LoadingHandler implements OnClickListener {
    private RelativeLayout loadingLayout;
    private GifImageView loading_gif;
    private TextView null_tv;
    private ImageView null_img;
    private RelativeLayout null_rl;
    private Button null_btn;

    public LoadingHandler(RelativeLayout loading) {
        this.loadingLayout = loading;
        loading_gif = (GifImageView) this.loadingLayout
                .findViewById(R.id.loading_gif);
        null_tv = (TextView) this.loadingLayout.findViewById(R.id.null_tv);
        null_img = (ImageView) this.loadingLayout.findViewById(R.id.null_img);
        null_rl = (RelativeLayout) this.loadingLayout
                .findViewById(R.id.null_rl);
        null_btn = (Button) this.loadingLayout.findViewById(R.id.null_btn);
        this.loadingLayout.setVisibility(View.GONE);
    }

    public void startLoading() {
        if (!NetWorkUtil.isConnected()){
            isNotNetConnection();
            return;
        }
        this.loadingLayout.setVisibility(View.VISIBLE);
        loading_gif.setVisibility(View.VISIBLE);
        null_rl.setVisibility(View.GONE);
        null_tv.setVisibility(View.GONE);
        null_img.setVisibility(View.GONE);
        null_btn.setVisibility(View.GONE);

    }

    public void showErrorText(String context) {
        ToastUtil.showToast(context);
    }

    public void isNotNetConnection() {
        this.loadingLayout.setVisibility(View.VISIBLE);
        loading_gif.setVisibility(View.GONE);
        null_rl.setVisibility(View.VISIBLE);
        null_tv.setVisibility(View.VISIBLE);
        null_img.setVisibility(View.VISIBLE);
        null_btn.setVisibility(View.VISIBLE);
        MyApplication.getInstance().displayFromDrawable(R.drawable.icon_nonet,
                null_img);
        null_tv.setText("你已经飞到外太空去了......");
        null_btn.setOnClickListener(this);
    }

    public void isNotContent() {
        this.loadingLayout.setVisibility(View.VISIBLE);
        loading_gif.setVisibility(View.GONE);
        null_rl.setVisibility(View.VISIBLE);
        null_tv.setVisibility(View.VISIBLE);
        null_img.setVisibility(View.VISIBLE);
        null_btn.setVisibility(View.VISIBLE);
        MyApplication.getInstance().displayFromDrawable(
                R.drawable.icon_refresh, null_img);
        null_tv.setText("内容还在采集,请等等再来。");
        null_btn.setOnClickListener(this);
    }

    public void stopLoading() {
        this.loadingLayout.setVisibility(View.GONE);
        null_rl.setVisibility(View.GONE);
        null_tv.setVisibility(View.GONE);
        null_img.setVisibility(View.GONE);
        null_btn.setVisibility(View.GONE);
    }

    private RefreshListenter mRefreshListenter;

    public void setOnRefreshListenter(RefreshListenter refreshListenter) {
        this.mRefreshListenter = refreshListenter;

    }

    public interface RefreshListenter {

        public void onLoadingRefresh();
    }

    @Override
    public void onClick(View view) {
        // TODO Auto-generated method stub
        if (!ButtonUtil.isDelayClick()) {
            return;
        }
        if (this.mRefreshListenter != null) {
            this.mRefreshListenter.onLoadingRefresh();
        }

    }

}
