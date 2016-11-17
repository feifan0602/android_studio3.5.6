package com.sun3d.culturalShanghai.adapter;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sun3d.culturalShanghai.R;
import com.sun3d.culturalShanghai.object.EventInfo;

public class VenueProfileItemAdapter extends BaseAdapter {

	private List<EventInfo> list;
	private Context context;

	public VenueProfileItemAdapter(Context context, List<EventInfo> list) {
		this.list = list;
		this.context = context;
	}

	public void setData(List<EventInfo> list) {
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
	public View getView(int arg0, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		Log.i("ztp_adapter", getCount() + "");
		ViewHolder vh = null;
		if (convertView == null) {
			convertView = View.inflate(context, R.layout.venue_profile_campaign_item, null);
			vh = new ViewHolder();
			vh.mId = (TextView) convertView.findViewById(R.id.venue_listView_tv);
			vh.mName = (TextView) convertView.findViewById(R.id.venue_listview_title);
			vh.mData = (TextView) convertView.findViewById(R.id.venue_listview_data);
			vh.mSala = (TextView) convertView.findViewById(R.id.venue_listview_sala);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}
		String[] start1 = list.get(arg0).getActivityStartTime().split("-");
		String statttime = "";
		if (null != start1 && start1.length > 2) {
			statttime = start1[1] + "月" + start1[2] + "日 至 ";
		}
		String[] end1 = list.get(arg0).getEventEndTime().split("-");
		String eddtime = "";
		if (null != end1 && end1.length > 2) {
			eddtime = end1[1] + "月" + end1[2] + "日";
		}
		vh.mId.setText(String.valueOf(arg0 + 1));
		vh.mName.setText(list.get(arg0).getEventName());
		vh.mData.setText("时间：  " + statttime + eddtime);
		if (list.get(arg0).getEventPrice().equals("0")
				| list.get(arg0).getEventPrice().length() == 0) {
			vh.mSala.setText("费用：  免费");
		} else {
			vh.mSala.setText("费用：  收费");
		}

		return convertView;
	}

	class ViewHolder {
		private TextView mId;
		private TextView mName;
		private TextView mData;
		private TextView mSala;
	}

}
