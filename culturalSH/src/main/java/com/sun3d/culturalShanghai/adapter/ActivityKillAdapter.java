package com.sun3d.culturalShanghai.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.R;
import com.sun3d.culturalShanghai.activity.ActivityDetailActivity;
import com.sun3d.culturalShanghai.object.KillInfo;
import com.sun3d.culturalShanghai.object.MessageInfo;

public class ActivityKillAdapter extends BaseAdapter {
	private Context mContext;
	private List<KillInfo> list;
	// 根据 这个索引 将上面的活动 都设置成 已结束
	private int index = 0;
	// 根据 这个索引 将最小的时间标记
	private int index_pos = 0;
	// 记录时间
	private int small_time = 0;
	// 记录时间
	private int small_data = 0;

	public ActivityKillAdapter(Context mContext, List<KillInfo> list) {
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

	public void setList(List<KillInfo> list) {
		this.list = list;
	}

	@Override
	public View getView(int pos, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHolder mHolder = null;
		if (arg1 == null) {
			arg1 = View.inflate(mContext, R.layout.activity_kill_item, null);
			mHolder = new ViewHolder();
			mHolder.kill_item_color = (LinearLayout) arg1
					.findViewById(R.id.kill_item_bg);
			mHolder.item_date = (TextView) arg1.findViewById(R.id.item_date);
			mHolder.item_time = (TextView) arg1.findViewById(R.id.item_time);
			mHolder.item_num = (TextView) arg1.findViewById(R.id.item_num);
			mHolder.item_stutas = (TextView) arg1
					.findViewById(R.id.item_stutas);
			mHolder.item_date.setTypeface(MyApplication.GetTypeFace());
			mHolder.item_time.setTypeface(MyApplication.GetTypeFace());
			mHolder.item_num.setTypeface(MyApplication.GetTypeFace());
			mHolder.item_stutas.setTypeface(MyApplication.GetTypeFace());
			arg1.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) arg1.getTag();
		}
		String status;
		String nowTime = MyApplication.getSystemTime();
		String nowTime_str = nowTime.replaceAll("-", "");
		int nowTime_int = Integer.valueOf(nowTime_str);
		
		MyApplication.total_availableCount = list.get(pos).getAvailableCount()
				+ MyApplication.total_availableCount;
		Log.i("ceshi", "看看 数据 为什么 没有了getAvailableCount===  "+MyApplication.total_availableCount);
		if (MyApplication.activityispast == 1) {
			status = "已结束";
		} else {
			Log.i("ceshi", "  pos  ==  " + pos + "为什么还有倒计时===  "
					+ list.get(pos).getSpikeDifference());
			if (list.get(pos).getSpikeDifference() <= 0) {
				// 倒计时结束
				// 还有一种情况 就是 秒杀的场次时间 到了
				String event_str = list.get(pos).getEventDate();
				String str = event_str.replaceAll("-", "");
				int event_time = Integer.valueOf(str);
				Log.i("ceshi",
						"  pos  ==  " + pos + "为什么还有票数===  "
								+ list.get(pos).getAvailableCount());
				if (list.get(pos).getAvailableCount() <= 0) {
					if (index == 0) {
						index = pos;
					}
					if (index < pos) {
						index = pos;
					}
					// 没票了
					status = "已结束";
				} else {
					// 正在秒杀的时候不需要记录倒计时时间
					// String time = MyApplication.get_NewTime(list.get(pos)
					// .getSpikeTime());
					// String end_str = time.replaceAll("-", "");
					// int end_time = Integer.valueOf(end_str.split(" ")[0]);

					// if (end_time >= nowTime_int) {
					MyApplication.currentCount = list.get(pos)
							.getAvailableCount();
					status = "正在秒杀";
					// 这里来获取 秒杀的最近一项
					// 这里来判断 哪一场时间段的时间正在秒杀
					String time = MyApplication.get_NewTime(list.get(pos)
							.getSpikeTime());
					String end_str = time.replaceAll("-", "").replaceAll(":",
							"");
					int end_time = Integer.valueOf(end_str.split(" ")[0]);
					int end_time_hour = Integer.valueOf(end_str.split(" ")[1]);
					if (small_data == 0) {
						small_data = end_time;
					}
					if (small_time == 0) {
						small_time = end_time_hour;
					}
					// 这里是日期
					if (small_data > end_time) {

					} else {
						small_data = end_time;
						// 这里是时间
						if (end_time_hour > small_time) {

						} else {
							small_time = end_time_hour;

							MyApplication.spike_event_id = list.get(pos)
									.getEventId();
							MyApplication.spike_endTime = list.get(pos)
									.getEventDate();
							MyApplication.spike_endTimeHour = list.get(pos)
									.getEventTime();

						}
					}

					// if (pos >= 0) {
					//
					// // 这里判断是哪个 开始倒计时
					//
					// String time_next = MyApplication.get_NewTime(list.get(
					// pos - 1).getSpikeTime());
					//
					// String end_str_next = time_next.replaceAll("-", "")
					// .replaceAll(":", "");
					// int end_time_next = Integer.valueOf(end_str_next
					// .split(" ")[0]);
					// int end_time_next_hour = Integer.valueOf(end_str_next
					// .split(" ")[1]);
					//
					// if (end_time_next < end_time) {
					// // 这个是判断 日期
					// // 算出 最小的秒杀时间段
					// Log.i("ceshi", "秒杀000");
					// MyApplication.spike_event_id = list.get(pos - 1)
					// .getEventId();
					// // MyApplication.spike_endTime =
					// // time_next.split(" ")[0];
					// MyApplication.spike_endTime = list.get(pos - 1)
					// .getEventDate();
					// MyApplication.spike_endTimeHour = list.get(pos - 1)
					// .getEventTime();
					// } else if (end_time_next == end_time) {
					//
					// if (end_time_next_hour < end_time_hour) {
					//
					//
					// } else {
					// Log.i("ceshi", "秒杀333");
					// MyApplication.spike_event_id = list.get(pos)
					// .getEventId();
					// MyApplication.spike_endTime = list.get(pos)
					// .getEventDate();
					// MyApplication.spike_endTimeHour = list.get(pos)
					// .getEventTime();
					// }
					// } else {
					// Log.i("ceshi", "秒杀444");
					// // list.get(pos).getAvailableCount() > 0
					// MyApplication.spike_event_id = list.get(pos)
					// .getEventId();
					// MyApplication.spike_endTime = list.get(pos)
					// .getEventDate();
					// MyApplication.spike_endTimeHour = list.get(pos)
					// .getEventTime();
					// }
					//
					// } else {
					// Log.i("ceshi", "秒杀555");
					// MyApplication.spike_event_id = list.get(pos)
					// .getEventId();
					// MyApplication.spike_endTime = list.get(pos)
					// .getEventDate();
					// MyApplication.spike_endTimeHour = list.get(pos)
					// .getEventTime();
					// }

				}
				if (pos < index) {
					status = "已结束";
				}
			} else if (list.get(pos).getSpikeDifference() > 0
					&& list.get(pos).getSpikeDifference() < 86400) {
				Log.i("ceshi", "即将开始==  ");
				status = "即将开始";
				if (list.get(pos).getAvailableCount() > 0) {

					// 不需要记录它的余票
					if (pos >= 1) {
						Log.i("ceshi", "即将开始000==  ");
						// 这里判断是哪个 开始倒计时
						if (list.get(pos - 1) != null) {
							if (list.get(pos - 1).getSpikeDifference() == 0) {
								MyApplication.spike_Time = list.get(pos)
										.getSpikeDifference();
								Log.i("ceshi", "即将开始333==  "
										+ MyApplication.spike_Time);
							} else if (list.get(pos).getSpikeDifference() < list
									.get(pos - 1).getSpikeDifference()) {
								MyApplication.spike_Time = list.get(pos)
										.getSpikeDifference();
								Log.i("ceshi", "即将开始111==  "
										+ MyApplication.spike_Time);
							}
						}
					} else {
						MyApplication.spike_Time = list.get(pos)
								.getSpikeDifference();
						Log.i("ceshi", "即将开始222==  " + MyApplication.spike_Time);
						// MyApplication.spike_endTime = list.get(pos)
						// .getEventEndDate();
						// MyApplication.spike_eventTime= list.get(pos)
						// .getEventTime();
						// MyApplication.spike_Time = list.get(pos)
						// .getSpikeDifference();
					}

					// if (spike_num < MyApplication.spike_Time) {

					// }

				}

			} else {
				status = "未开始";
			}
		}
		if (status.equals("正在秒杀")) {
			mHolder.kill_item_color
					.setBackgroundResource(R.color.text_color_c0);
		} else if (status.equals("未开始")) {
			mHolder.kill_item_color
					.setBackgroundResource(R.color.text_color_dd);
			mHolder.item_date.setTextColor(Color.BLACK);
			mHolder.item_time.setTextColor(Color.BLACK);
			mHolder.item_num.setTextColor(Color.BLACK);
			mHolder.item_stutas.setTextColor(Color.BLACK);
		} else if (status.equals("即将开始")) {
			mHolder.kill_item_color
					.setBackgroundResource(R.color.text_color_72);
		} else if (status.equals("已结束")) {
			mHolder.kill_item_color
					.setBackgroundResource(R.color.text_color_f2);
			mHolder.item_date.setTextColor(mContext.getResources().getColor(
					R.color.text_color_80));
			mHolder.item_time.setTextColor(mContext.getResources().getColor(
					R.color.text_color_80));
			mHolder.item_num.setTextColor(mContext.getResources().getColor(
					R.color.text_color_80));
			mHolder.item_stutas.setTextColor(mContext.getResources().getColor(
					R.color.text_color_80));
		}
		if (list.get(pos).getSpikeTime() != "") {
			String time = MyApplication.get_NewTime(list.get(pos)
					.getSpikeTime());
			mHolder.item_date.setText(time.split(" ")[0]);
			mHolder.item_time.setText(time.split(" ")[1]);
		}

		mHolder.item_num.setText(list.get(pos).getAvailableCount() + "张");
		mHolder.item_stutas.setText(status);
		return arg1;
	}

	class ViewHolder {
		LinearLayout kill_item_color;
		TextView item_date;
		TextView item_time;
		TextView item_num;
		TextView item_stutas;
	}
}
