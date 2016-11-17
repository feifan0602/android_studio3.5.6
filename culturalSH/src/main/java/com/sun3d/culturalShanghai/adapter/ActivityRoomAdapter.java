package com.sun3d.culturalShanghai.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.R;
import com.sun3d.culturalShanghai.Util.ToastUtil;
import com.sun3d.culturalShanghai.activity.ActivityRoomDateilsActivity;
import com.sun3d.culturalShanghai.object.ActivityListRoomInfo;
import com.sun3d.culturalShanghai.object.RoomDetailTimeSlotInfor;

public class ActivityRoomAdapter extends BaseAdapter {
	private ActivityRoomDateilsActivity mContext;
	private List<RoomDetailTimeSlotInfor> list;
	private Map<String, Boolean> map_bol;
	private ArrayList<ActivityListRoomInfo> mList;
	private ActivityRoomTimesAdapter ala;
	private int change_num = 0;
	private String bookid;

	public ActivityRoomAdapter(ActivityRoomDateilsActivity mContext,
			List<RoomDetailTimeSlotInfor> list) {
		// TODO Auto-generated constructor stub
		this.mContext = mContext;
		this.list = list;
		MyApplication.roomList.clear();
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

	public void setList(List<RoomDetailTimeSlotInfor> list) {
		this.list = list;
	}

	@Override
	public View getView(int pos, View convertView, ViewGroup arg2) {
		ViewHolder mHolder = null;
		if (convertView == null) {
			convertView = View.inflate(mContext,
					R.layout.activity_room_date_item, null);
			mHolder = new ViewHolder();
			mHolder.ll = (LinearLayout) convertView.findViewById(R.id.ll);
			mHolder.listview = (ListView) convertView
					.findViewById(R.id.listview);
			// LinearLayout.LayoutParams imagebtn_params = new
			// LinearLayout.LayoutParams(
			// LayoutParams.WRAP_CONTENT, 350);
			// imagebtn_params.width = MyApplication.getWindowWidth() / 4;
			// mHolder.ll.setLayoutParams(imagebtn_params);
			convertView.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) convertView.getTag();
		}
		mList = new ArrayList<ActivityListRoomInfo>();
		String[] bookstatus = list.get(pos).getBookStatus().split(",");
		ActivityListRoomInfo alri1 = new ActivityListRoomInfo();
		alri1.setCurDate(list.get(pos).getDate());
		if (list.get(pos).getBookId().length() != 0
				&& !list.get(pos).getBookId().equals("")) {
			mList.add(alri1);
		}

		MyApplication.roomList.add(alri1);
		for (int i = 0; i < bookstatus.length; i++) {
			Log.i("ceshi", "boookid==  " + list.get(pos).getBookId().length());
			if (list.get(pos).getBookId().length() != 0
					&& !list.get(pos).getBookId().equals(" ")
					&& list.get(pos).getBookId() != null) {
				Log.i("ceshi", "添加了===  ");
				ActivityListRoomInfo alri = new ActivityListRoomInfo();
				alri.setBookId(list.get(pos).getBookId().split(",")[i]);
				alri.setBookStatus(list.get(pos).getBookStatus().split(",")[i]);
				alri.setCurDate(list.get(pos).getDate());
				alri.setOpenPeriod(list.get(pos).getTimeslot().split(",")[i]);
				alri.setStatus(list.get(pos).getStatus().split(",")[i]);
				mList.add(alri);
				MyApplication.roomList.add(alri);
				MyApplication.roomList_new.add(alri);
			}

		}
		if (change_num == 0) {
			initItemNum(MyApplication.roomList);
		}
		if (MyApplication.roomList_new.size() == 0) {
			mContext.null_tv.setVisibility(View.VISIBLE);
			mContext.activityroom_reserve.setVisibility(View.GONE);
		}
		if (mList.size() == 0) {
			mHolder.listview.setVisibility(View.GONE);

		} else {
			ala = new ActivityRoomTimesAdapter(mContext, mList, map_bol);
			mHolder.listview.setAdapter(ala);
			MyApplication.setListViewHeightBasedOnChildren(mHolder.listview);
			mHolder.listview.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int pos, long arg3) {
					ActivityListRoomInfo room_info = (ActivityListRoomInfo) parent
							.getItemAtPosition(pos);
					bookid = room_info.getBookId();
					LinearLayout bg = (LinearLayout) view.findViewById(R.id.bg);
					if (room_info != null && room_info.getBookStatus() != null
							&& room_info.getBookStatus().equals("1")) {
						if (map_bol.get(bookid) == true) {
							// 选中
							initItemNum(MyApplication.roomList);
							map_bol.put(bookid, false);

							MyApplication.bookid = room_info.getBookId();
							MyApplication.curDate = room_info.getCurDate();
							MyApplication.openPeriod = room_info
									.getOpenPeriod();

							// Toast.makeText(mContext, "看看bookid== "
							// + MyApplication.bookid + "curDate ="
							// + MyApplication.curDate + "openPeriod=="
							// + MyApplication.openPeriod, 1000);
							change_num = 1;
							notifyDataSetChanged();
						} else {
							// 取消
							map_bol.put(bookid, true);
							MyApplication.bookid = "";
							MyApplication.curDate = "";
							MyApplication.openPeriod = "";
							change_num = 1;
							notifyDataSetChanged();
						}
					} else {
						// ToastUtil.showToast("不可选");
					}

					// ItemString.get(key)

				}
			});
		}

		// MyApplication.setListViewHeightBasedOnChildren(mHolder.listview);
		// if (list.get(pos).getStutas().equals("已被预订")) {
		// mHolder.bg.setBackgroundResource(R.color.text_color_f2eb);
		// } else if (list.get(pos).getStutas().equals("可预订")) {
		// mHolder.bg.setBackgroundResource(R.color.text_color_72);
		// mHolder.time.setTextColor(mContext.getResources().getColor(
		// R.color.text_color_ff));
		// mHolder.stutas.setTextColor(mContext.getResources().getColor(
		// R.color.text_color_ff));
		// } else if (list.get(pos).getStutas().equals("不开放")) {
		// mHolder.bg.setBackgroundResource(R.color.text_color_f2);
		// }
		// mHolder.time.setText(list.get(pos).getTime());
		// mHolder.stutas.setText(list.get(pos).getStutas());

		return convertView;
	}

	class ViewHolder {
		ListView listview;
		TextView time;
		TextView stutas;
		LinearLayout bg;
		LinearLayout ll;
	}

	class ViewHolderData {
		TextView text;
	};

	public void clearColor(ListView lv) {
		lv.setBackgroundColor(mContext.getResources().getColor(
				R.color.text_color_ff));
		this.notifyDataSetChanged();
	}

	private void initItemNum(List<ActivityListRoomInfo> info_list) {
		map_bol = new HashMap<String, Boolean>();
		for (int i = 0; i < info_list.size(); i++) {
			map_bol.put(info_list.get(i).getBookId(), true);
		}
	}
}
