package com.sun3d.culturalShanghai.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.R;
import com.sun3d.culturalShanghai.Util.Options;
import com.sun3d.culturalShanghai.object.NewInfo;

public class SpecialAdapter extends BaseAdapter {
    private Context mContext;
    private List<NewInfo> list;

    public SpecialAdapter(Context mContext, List<NewInfo> list) {
        // TODO Auto-generated constructor stub
        this.mContext = mContext;
        this.list = list;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return list.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }

    public void setList(List<NewInfo> list) {
        this.list = list;
    }

    @Override
    public View getView(int arg0, View arg1, ViewGroup arg2) {
        // TODO Auto-generated method stub
        ViewHolder mHolder = null;
        if (arg1 == null) {
            mHolder = new ViewHolder();
            arg1 = View.inflate(mContext, R.layout.specialitem, null);
            mHolder.title = (TextView) arg1.findViewById(R.id.title);
            mHolder.img_url = (ImageView) arg1.findViewById(R.id.img);
            mHolder.author_name = (TextView) arg1.findViewById(R.id.author_name);
            mHolder.time = (TextView) arg1.findViewById(R.id.time);
            mHolder.label = (TextView) arg1.findViewById(R.id.label);
            mHolder.content = (TextView) arg1.findViewById(R.id.coutent);
            arg1.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) arg1.getTag();
        }
        NewInfo info = list.get(arg0);
        mHolder.title.setText(info.getNewsTitle());
        mHolder.author_name.setText(info.getNewsReportUser());
        mHolder.time.setText(info.getNewReportTime());
        mHolder.label.setText(info.getNewType());
        mHolder.content.setText(info.getNewsDesc());
        MyApplication.getInstance().setImageView(MyApplication.getContext(), info.getNewsImgUrl(), mHolder.img_url);
//		MyApplication.getInstance().getImageLoader().displayImage(info.getNewsImgUrl(), mHolder.img_url, Options.getListOptions());
        return arg1;
    }

    class ViewHolder {
        private TextView title;
        private ImageView img_url;
        private TextView author_name;
        private TextView time;
        private TextView label;
        private TextView content;
    }
}
