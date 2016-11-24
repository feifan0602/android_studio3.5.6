package com.sun3d.culturalShanghai.adapter;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.R;
import com.sun3d.culturalShanghai.Util.TextUtil;
import com.sun3d.culturalShanghai.object.HomeDetail_ContentInfor;

import java.util.List;

public class HomeDetail_ContentAllCityAdapter extends BaseAdapter {
    private Context mContext;
    private List<HomeDetail_ContentInfor> mList;
    private ViewHolder_Banner mBannerHolder;
    public HomeDetail_ContentAllCityAdapter(Context context,
                                            List<HomeDetail_ContentInfor> list) {
        this.mContext = context;
        this.mList = list;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mList.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return mList.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }

    public void setList(List<HomeDetail_ContentInfor> list) {
        this.mList = list;
    }


    @Override
    public View getView(int pos, View convertView, ViewGroup arg2) {
        convertView = addBannerView(pos, convertView);
        return convertView;
    }

    private View addBannerView(int pos, View convertView) {
        if (convertView == null) {
            convertView = View.inflate(mContext,
                    R.layout.adapter_event_advertisement_list_item, null);
            mBannerHolder = new ViewHolder_Banner();
            mBannerHolder.img = (ImageView) convertView.findViewById(R.id.img);
            convertView.setTag(mBannerHolder);
        } else {
            mBannerHolder = (ViewHolder_Banner) convertView.getTag();
        }
        final HomeDetail_ContentInfor info = mList.get(pos);
        MyApplication
                .getInstance()
                .getImageLoader()
                .displayImage(
                        TextUtil.getUrlSmall_750_250(mList.get(pos)
                                .getAdvertImgUrl()), mBannerHolder.img);
        mBannerHolder.img.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (info.getAdvertLink() == 0) {
                    MyApplication.selectImg(mContext, info.getAdvertLinkType(),
                            info.getAdvertUrl());

                } else {
                    MyApplication.selectWeb(mContext, info.getAdvertUrl());

                }
            }
        });
        return convertView;
    }


    class ViewHolder_Banner {
        ImageView img;
    }

}
