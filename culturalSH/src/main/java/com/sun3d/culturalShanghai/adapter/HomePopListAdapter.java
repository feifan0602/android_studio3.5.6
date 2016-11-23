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
import com.sun3d.culturalShanghai.fragment.HomeFragment;
import com.sun3d.culturalShanghai.object.EventInfo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/11/4 0004.
 */
public class HomePopListAdapter extends BaseAdapter {
    private List<EventInfo> list;
    private Context mContext;
    private HomeFragment mHomeFragment;
    public HomePopListAdapter(Context mContext, List<EventInfo> list,HomeFragment homeFragment) {
        // TODO Auto-generated constructor stub
        this.mContext = mContext;
        this.list = list;
        this.mHomeFragment=homeFragment;
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
            JSONArray ja = info.getCityList();
            ArrayList<EventInfo> mList=new ArrayList<EventInfo>();
            for (int i = 0; i < ja.length(); i++) {
                EventInfo info_even = new EventInfo();
                try {
                    JSONObject jo = ja.getJSONObject(i);
                    info_even.setIsQuickSearch(jo.optInt("isQuickSearch", 0));
                    info_even.setCityId(jo.optInt("cityId", 0));
                    info_even.setCityCode(jo.optInt("cityCode", 0));
                    info_even.setAreaId(jo.optInt("areaId", 0));
                    info_even.setCityName(jo.optString("cityName", ""));
                    info_even.setCityIndex(jo.optInt("cityIndex", 0));
                    info_even.setFirstLetter(jo.optString("firstLetter", ""));
                    mList.add(info_even);
                }catch(Exception e){
                    e.printStackTrace();
                }

            }
            HomePopGridAdapter mHomePopGridAdapter = new HomePopGridAdapter(mContext, mList,mHomeFragment);
            mHolder.mGridView.setAdapter(mHomePopGridAdapter);
            MyApplication.setGridViewHeight(mHolder.mGridView, 4, 20);

        }
        mHolder.mText_Frist.setText(list.get(position).getFirstLetter());
        mHolder.mTextView.setText(list.get(position).getCityName());
        return convertView;
    }

    class ViewHolder {
        TextView mTextView;
        TextView mText_Frist;
        RelativeLayout mRelativeLayout;
        GridView mGridView;

    }
}
