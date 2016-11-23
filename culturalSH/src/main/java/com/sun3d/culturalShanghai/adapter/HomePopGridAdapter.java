package com.sun3d.culturalShanghai.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.R;
import com.sun3d.culturalShanghai.fragment.HomeFragment;
import com.sun3d.culturalShanghai.object.EventInfo;

import java.util.List;

/**
 * Created by Administrator on 2016/11/4 0004.
 */
public class HomePopGridAdapter extends BaseAdapter {
    private List<EventInfo> list;
    private Context mContext;
    private HomeFragment mHomeFragment;

    public HomePopGridAdapter(Context mContext, List<EventInfo> list, HomeFragment homeFragment) {
        // TODO Auto-generated constructor stub
        this.mContext = mContext;
        this.list = list;
        this.mHomeFragment = homeFragment;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder mHolder = null;
        if (convertView == null) {
            mHolder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.home_pop_adapter,
                    null);
            mHolder.tv = (TextView) convertView.findViewById(R.id.tv);
            mHolder.tv.setTypeface(MyApplication.GetTypeFace());
            convertView.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) convertView.getTag();
        }
        mHolder.tv.setText(list.get(position).getCityName());
        mHolder.tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (list.get(position).getCityCode()==0){
                    mHomeFragment.changeToChina();
                }else{
                    MyApplication.saveNowAddress(mContext, list.get(position).getCityName());
                    mHomeFragment.getNewIP(list.get(position).getCityCode());
                    mHomeFragment.left_tv.setVisibility(View.VISIBLE);
                    mHomeFragment.left_tv.setText(list.get(position).getCityName());

                }
                mHomeFragment.openPopWindow();

            }
        });
        return convertView;
    }

    class ViewHolder {
        TextView tv;
    }
}
