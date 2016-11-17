package com.sun3d.culturalShanghai.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.R;
import com.sun3d.culturalShanghai.Util.Options;
import com.sun3d.culturalShanghai.object.GroupDeatilInfo;

public class GroupListAdapter extends BaseAdapter {
	private Map<Integer, View> m = new HashMap<Integer, View>();
	private Context mContext;
	private List<GroupDeatilInfo> list;

	public GroupListAdapter(Context mContext, List<GroupDeatilInfo> list) {
		// TODO Auto-generated constructor stub
		this.mContext = mContext;
		this.list = list;
	}

	public void setList(List<GroupDeatilInfo> list) {
		this.list = list;
		this.notifyDataSetChanged();
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

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHolder mHolder = null;
		arg1 = m.get(arg0);
		if (arg1 == null) {
			mHolder = new ViewHolder();
			arg1 = View.inflate(mContext, R.layout.adapter_group_item, null);
			mHolder.mName = (TextView) arg1.findViewById(R.id.group_name);
			mHolder.mUrl = (ImageView) arg1.findViewById(R.id.group_url);
			arg1.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) arg1.getTag();
		}
		GroupDeatilInfo GDI = list.get(arg0);
		if (GDI != null) {
			mHolder.mName.setText(GDI.getGroupName());
			MyApplication.getInstance().getImageLoader()
					.displayImage(GDI.getGroupImgUrl(), mHolder.mUrl, Options.getListOptions());

		}
		m.put(arg0, arg1);
		return arg1;
	}

	private class ViewHolder {
		private ImageView mUrl;
		private TextView mName;

	}
}
