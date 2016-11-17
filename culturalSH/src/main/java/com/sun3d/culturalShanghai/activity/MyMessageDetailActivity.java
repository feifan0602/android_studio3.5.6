package com.sun3d.culturalShanghai.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sun3d.culturalShanghai.R;

/**
 * Created by wangmingming on 2016/3/12.
 */
public class MyMessageDetailActivity extends Activity implements View.OnClickListener{
    private RelativeLayout mTitle;
    private TextView tv_title;
    private TextView tv_message;
    private Context mContext;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_message_item_detail);
        mContext = this;
        initView();
        setData();
    }

    private void setData() {
        mTitle = (RelativeLayout) findViewById(R.id.title);
        mTitle.findViewById(R.id.title_right).setVisibility(View.GONE);
        mTitle.findViewById(R.id.title_left).setOnClickListener(this);
        TextView title = (TextView) mTitle.findViewById(R.id.title_content);
        title.setVisibility(View.VISIBLE);
        title.setText(mContext.getResources().getString(R.string.message_title));
        tv_title.setText(this.getIntent().getStringExtra("title"));
        tv_message.setText(this.getIntent().getStringExtra("message"));
    }

    private void initView() {
        tv_title = (TextView) findViewById(R.id.message_title);
        tv_message = (TextView) findViewById(R.id.message_content);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_left:
                finish();
                break;

            default:
                break;
        }
    }
}
