package com.sun3d.culturalShanghai.wff.calender;

import java.util.ArrayList;
import java.util.List;

import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.R;
import com.sun3d.culturalShanghai.fragment.CalenderFragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * <p>
 * 日历适配器
 * </p>
 * 
 * @author mengjiankang
 * @date 2013-8-31
 ********************************************************************** 
 * @modified <修改人姓名> @date <日期>
 * @comment
 * 
 ********************************************************************** 
 */
@SuppressLint("ResourceAsColor")
public class CalendarGridViewAdapter extends BaseAdapter {
	private ArrayList<String> list_date;
	private int nowmonth = 0;
	private int nowday = 0;
	private CalenderFragment cf;
	private int pos;
	private Resources mRes;
	/** 上下文 */
	private Context mContext;
	/** 日期实体集合 */
	private List<DateEntity> mDataList;
	/** 因为position是从0开始的，所以用当做一个中间者，用来加1.以达到判断除数时，为哪个星期 */
	private int temp;
	private int clickTemp = -1;

	public CalendarGridViewAdapter(Context context, Resources res,
			CalenderFragment cf) {
		this.mContext = context;
		this.mRes = res;
		this.cf = cf;
	}

	/** 设置日期数据 */
	public void setDateList(List<DateEntity> dataList) {
		this.mDataList = dataList;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (mDataList == null) {
			return 0;
		}
		return mDataList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mDataList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public void setSelect(int position) {
		clickTemp = position;
	}

	public void setdate(ArrayList<String> list_date) {
		this.list_date = list_date;
	};

	public void setNowTime(int nowmonth, int nowday) {
		this.nowmonth = nowmonth;
		this.nowday = nowday;
	};

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// 通过传递过来的MenuItem值给每一个item设置数据
		ViewHolder vh;
		if (convertView == null) {
			vh = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.calendar_item_layout, null);
			vh.calendar_item_iv_day1 = (ImageView) convertView
					.findViewById(R.id.calendar_item_iv_day);
			vh.date = (TextView) convertView
					.findViewById(R.id.calendar_item_tv_day);
			vh.calendar_num_iv = (ImageView) convertView
					.findViewById(R.id.calendar_num_iv);
			vh.num = (TextView) convertView.findViewById(R.id.calendar_num);

			vh.date.setTypeface(MyApplication.GetTypeFace());
			vh.num.setTypeface(MyApplication.GetTypeFace());
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}
		if (clickTemp == position) {
			// vh.date.setBackgroundResource(R.drawable.cricle_selelct);
			vh.calendar_item_iv_day1.setImageResource(R.drawable.select_cricle);
		} else {
			vh.date.setBackgroundResource(R.color.text_color_00ff);
			vh.calendar_item_iv_day1
					.setImageResource(R.drawable.select_cricle_null);
		}
		if (mDataList.get(position).isSelfMonthDate
				&& nowmonth == mDataList.get(position).month) {
			vh.num.setVisibility(View.VISIBLE);
			// 表示本月的 而且时间也是一样的
			if (nowday > mDataList.get(position).day) {
				// 已经过去的时间段
				vh.date.setTextColor(mContext.getResources().getColor(
						R.color.text_color_99));
				vh.num.setTextColor(mContext.getResources().getColor(
						R.color.text_color_99));
				if (list_date != null) {
					for (int i = 0; i < list_date.size(); i++) {
						if (Integer.valueOf(list_date.get(i).split(":")[0]
								.replaceAll("\"", "").split("-")[2]) == mDataList
								.get(position).day) {
							String num = list_date.get(i).split(":")[1]
									.replaceAll("\"", "");

							num = num.replaceAll("\\}", "");
							vh.num.setText(num + "场");
						}
					}

				}
			} else {

				vh.date.setTextColor(mContext.getResources().getColor(
						R.color.text_color_26));
				vh.num.setTextColor(mContext.getResources().getColor(
						R.color.text_color_99));
				if (list_date != null) {
					for (int i = 0; i < list_date.size(); i++) {
						if (Integer.valueOf(list_date.get(i).split(":")[0]
								.replaceAll("\"", "").split("-")[2]) == mDataList
								.get(position).day) {
							String num = list_date.get(i).split(":")[1]
									.replaceAll("\"", "");

							num = num.replaceAll("\\}", "");
							vh.num.setText(num + "场");
						}
					}

				}
			}
		} else if (mDataList.get(position).isSelfMonthDate
				&& nowmonth < mDataList.get(position).month) {
			vh.num.setVisibility(View.VISIBLE);
			// 表示本月的
			vh.date.setTextColor(mContext.getResources().getColor(
					R.color.text_color_26));
			vh.num.setTextColor(mContext.getResources().getColor(
					R.color.text_color_99));
			if (list_date != null) {
				for (int i = 0; i < list_date.size(); i++) {
					if (Integer.valueOf(list_date.get(i).split(":")[0]
							.replaceAll("\"", "").split("-")[2]) == mDataList
							.get(position).day) {
						String num = list_date.get(i).split(":")[1].replaceAll(
								"\"", "");
						num = num.replaceAll("\\}", "");
						vh.num.setText(num + "场");
					}
				}

			}
		}

		else {
			vh.date.setTextColor(mContext.getResources().getColor(
					R.color.text_color_e5));
			vh.num.setVisibility(View.GONE);
		}
		if (mDataList.get(position).isNowDate) {
			// 获取 现在是哪天的 position
			if (cf.now_time_position_change) {
				cf.pos = position;
			}

			vh.date.setTextColor(Color.WHITE);
			vh.calendar_item_iv_day1.setImageResource(R.drawable.red_cricle);
		}
		if (mDataList != null) {
			vh.date.setText(mDataList.get(position).day + "");
			// if ((TextUtils.equals(CalendarTool.SATURDAY,
			// mDataList.get(position).weekDay))
			// || TextUtils.equals(CalendarTool.SUNDAY,
			// mDataList.get(position).weekDay)) {
			// // 是周末则背景为灰
			// vh.date.setBackgroundColor(mRes.getColor(R.color.dark_grey));
			// }
			// if (mDataList.get(position).isNowDate
			// && mDataList.get(position).isSelfMonthDate) {
			// // 如果为当前号数，则设置为白色背景并，字体为黑色
			// vh.date.setBackgroundColor(mRes
			// .getColor(R.color.list_item_view_70_white));
			// vh.date.setTextColor(mRes.getColor(R.color.black));
			// }
			// if (!mDataList.get(position).isSelfMonthDate) {// 是否为本月的号数
			// vh.date.setTextColor(mRes.getColor(R.color.dark_slate_gray));
			// }
		}

		return convertView;
	}

	class ViewHolder {
		TextView date;
		TextView num;
		ImageView calendar_item_iv_day1;
		ImageView calendar_num_iv;
	}
}
