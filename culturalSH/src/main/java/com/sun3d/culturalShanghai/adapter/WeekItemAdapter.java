package com.sun3d.culturalShanghai.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sun3d.culturalShanghai.R;
import com.sun3d.culturalShanghai.object.UserBehaviorInfo;

public class WeekItemAdapter extends BaseAdapter {
    public static List<UserBehaviorInfo> mlist = new ArrayList<UserBehaviorInfo>();
    private Context mContext;
    public static HashMap<Integer, Boolean> indexSelect = new HashMap<Integer, Boolean>();
    private UserBehaviorInfo userBehaviorInfo = new UserBehaviorInfo();

    public WeekItemAdapter(Context mContext, List<UserBehaviorInfo> list) {
        mlist.clear();
        userBehaviorInfo.setTagId("");
        userBehaviorInfo.setTagName("全部");
        userBehaviorInfo.setSelect(true);
        // TODO Auto-generated constructor stub
        mlist.add(userBehaviorInfo);
        mlist.addAll(list);
        init(false);
        this.mContext = mContext;
    }

    public static void initAll(boolean b) {
        for (int i = 1; i < mlist.size(); i++) {
            indexSelect.put(i, b);
        }
    }

    public static void init(boolean b) {
        for (int i = 0; i < mlist.size(); i++) {
            if (i==0){
                indexSelect.put(i, true);
            }else {
                indexSelect.put(i, b);
            }
        }
    }

    public static void re_init(boolean b) {
        indexSelect.put(0, b);
    }

    public void setIndexSelect(HashMap<Integer, Boolean> indexSelect) {
        this.indexSelect = indexSelect;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mlist.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return mlist.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }

    public void setList(List<UserBehaviorInfo> list) {
        this.mlist = list;
    }

    @Override
    public View getView(int arg0, View arg1, ViewGroup arg2) {
        // TODO Auto-generated method stub
        ViewHolder mHolder = null;
        if (arg1 == null) {
            arg1 = View.inflate(mContext, R.layout.pop_weekitem, null);
            mHolder = new ViewHolder();
            mHolder.text = (TextView) arg1.findViewById(R.id.menuitem);
            mHolder.icon = (TextView) arg1.findViewById(R.id.pop_weekitem_icon);
            arg1.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) arg1.getTag();
        }
        mHolder.text.setText(this.mlist.get(arg0).getTagName());

        if (indexSelect.get(arg0)) {
            mHolder.text.setTextColor(mContext.getResources().getColor(R.color.orange_3_1_color));
            mHolder.icon.setVisibility(View.VISIBLE);
        } else {
            mHolder.text.setTextColor(mContext.getResources().getColor(R.color.app_text_color));
            mHolder.icon.setVisibility(View.GONE);
        }


        return arg1;
    }

    class ViewHolder {
        private TextView text;
        private TextView icon;
    }
}
