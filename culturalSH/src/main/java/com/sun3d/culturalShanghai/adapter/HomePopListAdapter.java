package com.sun3d.culturalShanghai.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.R;
import com.sun3d.culturalShanghai.object.EventInfo;

import java.util.List;

/**
 * Created by Administrator on 2016/11/4 0004.
 */
public class HomePopListAdapter extends BaseAdapter {
    private List<EventInfo> list;
    private Context mContext;

    public HomePopListAdapter(Context mContext, List<EventInfo> list) {
        // TODO Auto-generated constructor stub
        this.mContext = mContext;
        this.list = list;
    }

    public void setData(List<EventInfo> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mHolder = null;
        EventInfo info = list.get(position);
        if (convertView == null) {
            mHolder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.home_pop_adapter_lv,
                    null);
            mHolder.mRelativeLayout = (RelativeLayout) convertView.findViewById(R.id.rl);
            mHolder.mGridView = (GridView) convertView.findViewById(R.id.gv);
            mHolder.mText_Frist = (TextView) convertView.findViewById(R.id.text_frist);
            mHolder.mGridView.setFocusable(false);
            mHolder.mTextView = (TextView) convertView.findViewById(R.id.tv);
            mHolder.mTextView.setTypeface(MyApplication.GetTypeFace());
            mHolder.mText_Frist.setTypeface(MyApplication.GetTypeFace());
            convertView.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) convertView.getTag();
        }
        if (info.getActivityIsWant()) {
            mHolder.mRelativeLayout.setVisibility(View.GONE);
        } else {
            mHolder.mRelativeLayout.setVisibility(View.VISIBLE);
            HomePopGridAdapter mHomePopGridAdapter = new HomePopGridAdapter(mContext, info
                    .getEventInfosList());
            mHolder.mGridView.setAdapter(mHomePopGridAdapter);
            MyApplication.setGridViewHeight(mHolder.mGridView, 4, 9);

        }
        String frist_str = "";
        if (list.get(position).getActivityArea() != "" && list.get(position).getActivityArea() !=
                null) {
            frist_str = MyApplication.getSpells(list.get(position).getActivityArea());
            frist_str = frist_str.substring(0, 1).toUpperCase();
        }

        mHolder.mText_Frist.setText(frist_str);
        mHolder.mTextView.setText(list.get(position).getActivityArea());
        return convertView;
    }

    class ViewHolder {
        TextView mTextView;
        TextView mText_Frist;
        RelativeLayout mRelativeLayout;
        GridView mGridView;

    }
}
