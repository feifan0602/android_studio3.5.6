package com.sun3d.culturalShanghai.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.R;
import com.sun3d.culturalShanghai.Util.TextUtil;
import com.sun3d.culturalShanghai.Util.ToastUtil;
import com.sun3d.culturalShanghai.activity.ActivityRoomDateilsActivity;
import com.sun3d.culturalShanghai.object.ActivityListRoomInfo;
import com.sun3d.culturalShanghai.object.HomeDetail_TopLayoutInfor;
import com.sun3d.culturalShanghai.object.RoomDetailTimeSlotInfor;

public class HomeDetail_TopLayoutAdapter extends BaseAdapter {
	private Context mContext;
	private List<HomeDetail_TopLayoutInfor> mList;
	public static final int APP_PAGE_SIZE = 4;
	private PackageManager pm;

	public HomeDetail_TopLayoutAdapter(Context context,
			List<HomeDetail_TopLayoutInfor> list, int page) {
		// TODO Auto-generated constructor stub
		this.mContext = context;
		pm = context.getPackageManager();
		mList = new ArrayList<HomeDetail_TopLayoutInfor>();
		int i = page * APP_PAGE_SIZE;
		int iEnd = i + APP_PAGE_SIZE;
		while ((i < list.size()) && (i < iEnd)) {
			mList.add(list.get(i));
			i++;
		}
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

	public void setList(List<HomeDetail_TopLayoutInfor> list) {
		this.mList = list;
	}

	@Override
	public View getView(int pos, View convertView, ViewGroup arg2) {
		ViewHolder mHolder = null;
		if (convertView == null) {
			convertView = View.inflate(mContext,
					R.layout.home_fragment_toplayout_item, null);
			mHolder = new ViewHolder();
			mHolder.ll = (LinearLayout) convertView.findViewById(R.id.ll);
			mHolder.tv = (TextView) convertView.findViewById(R.id.tv);
			mHolder.tv.setTypeface(MyApplication.GetTypeFace());
			mHolder.img = (ImageView) convertView.findViewById(R.id.img);
			convertView.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) convertView.getTag();
		}
		mHolder.tv.setText(mList.get(pos).getAdvertTitle());
		MyApplication
				.getInstance()
				.getImageLoader()
				.displayImage(
						TextUtil.getUrlSmall_140_120(mList.get(pos)
								.getAdvertImgUrl()), mHolder.img);
		return convertView;
	}

	class ViewHolder {
		ImageView img;
		TextView tv;
		LinearLayout ll;
	}

}
