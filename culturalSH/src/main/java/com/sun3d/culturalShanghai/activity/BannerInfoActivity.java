package com.sun3d.culturalShanghai.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sun3d.culturalShanghai.R;
import com.sun3d.culturalShanghai.Util.AppConfigUtil;
import com.sun3d.culturalShanghai.Util.ViewUtil;
import com.sun3d.culturalShanghai.dialog.DialogTypeUtil;
import com.sun3d.culturalShanghai.object.ShareInfo;
import com.sun3d.culturalShanghai.view.FastBlur;

/**
 * 这是BannerInfo webview
 * Created by wangmingming on 2016/3/3.
 */
public class BannerInfoActivity extends Activity implements View.OnClickListener {

    private WebView mWebView;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mContext = this;
        setContentView(R.layout.banner);
        initView();
        super.onCreate(savedInstanceState);
    }

    private void initView() {
        setTitle();
        mWebView = (WebView) findViewById(R.id.banner_layout_web);
        //启用支持javascript
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);

        WebSettings webSettings = mWebView.getSettings();
        webSettings.setSupportZoom(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSettings.setUseWideViewPort(true);
        webSettings.setJavaScriptEnabled(true);
        settings.setLoadWithOverviewMode(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        mWebView.loadUrl(ThisWeekActivity.listBannerInfo.get(0).getAdvertConnectUrl());

        mWebView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) { //  重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
                view.loadUrl(url);
                return true;
            }
        });
    }

    /**
     * 设置标题
     */
    private void setTitle() {
        RelativeLayout mTitle = (RelativeLayout) findViewById(R.id.banner_layout);
        mTitle.findViewById(R.id.title_left).setOnClickListener(this);
        ImageButton mTitleRight = (ImageButton) mTitle.findViewById(R.id.title_right);
        mTitleRight.setOnClickListener(this);
        mTitleRight.setImageResource(R.drawable.sh_icon_title_share);
        TextView mTitle_tv = (TextView) mTitle.findViewById(R.id.title_content);
        mTitle_tv.setText(ThisWeekActivity.listBannerInfo.get(0).getAdvertTitle());
        mTitle_tv.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_left:
                finish();
                break;
            case R.id.title_right:
                Intent intent = new Intent(mContext, UserDialogActivity.class);
                FastBlur.getScreen((Activity) mContext);
                ShareInfo info = new ShareInfo();
                info.setTitle(ThisWeekActivity.listBannerInfo.get(0).getAdvertTitle());
                info.setImageUrl((ThisWeekActivity.listBannerInfo.get(0).getAdvertPicUrl()));
                info.setContentUrl(ThisWeekActivity.listBannerInfo.get(0).getAdvertConnectUrl());
                info.setContent(ViewUtil.getTextFromHtml(ThisWeekActivity.listBannerInfo.get(0).getAdvertContent()));
                intent.putExtra(AppConfigUtil.INTENT_SHAREINFO, info);
                intent.putExtra(DialogTypeUtil.DialogType, DialogTypeUtil.ThirdPartyDialogType.THIRDPARTY_SHARE);
                startActivity(intent);
                break;
        }


    }
}
