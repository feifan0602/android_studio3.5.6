package com.sun3d.culturalShanghai.adapter;

import java.util.ArrayList;
import java.util.List;

import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.R;
import com.sun3d.culturalShanghai.Util.TextUtil;

import com.sun3d.culturalShanghai.object.SpaceInfo;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class VenueListNewAdapter extends BaseAdapter implements OnClickListener
{
	private static final int TYPE_COUNT = 3;// item类型的总数
	private static final int TYPE_BANNER = 2;// 廣告類型
	private static final int TYPE_MAIN = 1;// 主類型
	private static final int TYPE_FRIST = 0;// 顶部三个广告

	private List<SpaceInfo> mList = new ArrayList<SpaceInfo>();
	private List<SpaceInfo> list_banner = new ArrayList<SpaceInfo>();
	private int currentType;// 当前item类型
	private Context mContext;

	private ViewHolder_Main main_vh;

	private String TAG = "VenueListNewAdapter";
	private View Main_view;
	private ListView mListView;
	/**
	 * 判断 选项框 是否显示1 为显示 2 为不显示
	 */
	private int pop_gone_visible = 1;
	private int pos_img;
	private int num = 0;
	private String content = "";

	public VenueListNewAdapter(Context mContext, List<SpaceInfo> list)
	{
		// TODO Auto-generated constructor stub

		this.mContext = mContext;
		this.mList = list;
	}

	public void setList(List<SpaceInfo> list)
	{
		this.mList = list;
		notifyDataSetChanged();
	}

	@Override
	public int getCount()
	{
		// TODO Auto-generated method stub
		return mList.size();
	}

	@Override
	public Object getItem(int arg0)
	{
		// TODO Auto-generated method stub
		return mList.get(arg0);
	}

	@Override
	public long getItemId(int arg0)
	{
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public int getViewTypeCount()
	{
		return TYPE_COUNT;
	}

	@Override
	public int getItemViewType(int position)
	{
		if (position == 0)
		{
			return TYPE_FRIST;// 顶部的广告
		} else
		{
			return TYPE_MAIN;// 主配置文件
		}

	}

	@Override
	public View getView(int arg0, View convertView, ViewGroup arg2)
	{

		if (convertView == null)
		{
			main_vh = new ViewHolder_Main();
			Main_view = View.inflate(mContext, R.layout.space_item_main_adapter, null);
			main_vh.name_tv = (TextView) Main_view.findViewById(R.id.name_tv);
			main_vh.address_tv = (TextView) Main_view.findViewById(R.id.address_tv);
			main_vh.activity_tv = (TextView) Main_view.findViewById(R.id.activity_num_tv);
			main_vh.room_tv = (TextView) Main_view.findViewById(R.id.room_num_tv);
			main_vh.space_bg_iv = (ImageView) Main_view.findViewById(R.id.space_bg);
			main_vh.sieve_ll = (LinearLayout) Main_view.findViewById(R.id.Sieve);
			main_vh.sieve_ll.setVisibility(View.GONE);

			main_vh.center_tv = (TextView) Main_view.findViewById(R.id.middle_tv);
			main_vh.shopping_areas_tv.setTypeface(MyApplication.GetTypeFace());
			main_vh.sieve_tv.setTypeface(MyApplication.GetTypeFace());
			main_vh.center_tv.setTypeface(MyApplication.GetTypeFace());
			main_vh.preface_tv.setTypeface(MyApplication.GetTypeFace());
			main_vh.name_tv.setTypeface(MyApplication.GetTypeFace());
			main_vh.address_tv.setTypeface(MyApplication.GetTypeFace());
			main_vh.activity_tv.setTypeface(MyApplication.GetTypeFace());
			main_vh.room_tv.setTypeface(MyApplication.GetTypeFace());
			main_vh.activity_tv.getBackground().setAlpha(200);
			main_vh.room_tv.getBackground().setAlpha(200);
			Main_view.setTag(main_vh);
			convertView = Main_view;
		} else
		{
			main_vh = (ViewHolder_Main) convertView.getTag();
		}

		int numOfActivity = mList.get(arg0).getActCount();
		int numOfRooms = mList.get(arg0).getRoomCount();

		if (numOfActivity > 0)
		{
			main_vh.activity_tv.setVisibility(View.VISIBLE);
			String str_activity = numOfActivity + "个在线活动";
			main_vh.activity_tv.setText(MyApplication.getYellowTextColor(str_activity));
			if (numOfRooms == 0)
			{// 单个需要居中
				main_vh.center_tv.setVisibility(View.VISIBLE);
				main_vh.activity_tv.setVisibility(View.GONE);
				main_vh.center_tv.setText(MyApplication.getYellowTextColor(str_activity));
			}
		} else
		{
			main_vh.activity_tv.setVisibility(View.GONE);
		}

		if (numOfRooms > 0)
		{
			String str_venue = numOfRooms + "个活动室";
			main_vh.room_tv.setVisibility(View.VISIBLE);
			main_vh.room_tv.setText(MyApplication.getYellowTextColor(str_venue));
			if (numOfActivity == 0)
			{// 单个需要居中
				main_vh.center_tv.setVisibility(View.VISIBLE);
				main_vh.room_tv.setVisibility(View.GONE);
				main_vh.center_tv.setText(MyApplication.getYellowTextColor(str_venue));
			}

		} else
		{
			main_vh.room_tv.setVisibility(View.GONE);
		}
		if (numOfRooms > 0 && numOfActivity > 0 || numOfActivity == 0 && numOfRooms == 0)
		{
			main_vh.center_tv.setVisibility(View.GONE);
		}

		return convertView;
	}

	public void updateView(int itemIndex)
	{
		// 得到第一个可显示控件的位置，
		if (mListView != null)
		{
			View view = mListView.getChildAt(3);
			if (view != null)
			{
				ViewHolder_Main vm = (ViewHolder_Main) view.getTag();
				vm.activity_tv.setText("sdfsdfsdf");
				notifyDataSetChanged();

			}

		}

		// int visiblePosition = mListView.getFirstVisiblePosition();
		//
		// //只有当要更新的view在可见的位置时才更新，不可见时，跳过不更新
		// if (itemIndex - visiblePosition >= 0) {
		// //得到要更新的item的view
		// View view = mListView.getChildAt(itemIndex - visiblePosition);
		// //从view中取得holder
		// ViewHolder holder = (ViewHolder) view.getTag();
		// HashMap<String, Object> item = data.get(itemIndex);
		//
		// holder.listItem = (RelativeLayout) view.findViewById(R.id.rl_item);
		// holder.ibPlay = (ImageButton) view.findViewById(R.id.ib_play);
		// holder.ibDelete = (ImageButton) view.findViewById(R.id.ib_delete);
		// holder.tvName = (TextView)
		// view.findViewById(R.id.tv_record_sound_name);
		// holder.tvLastModifyTime = (TextView) view
		// .findViewById(R.id.tv_record_time);
		// holder.tvCurrentPlayTime = (TextView) view
		// .findViewById(R.id.tv_current_play_time);
		// holder.tvTotalTime = (TextView)
		// view.findViewById(R.id.tv_total_time);
		// holder.sb = (MySeekBar) view.findViewById(R.id.pb_play);
		// holder.layout = (LinearLayout) view
		// .findViewById(R.id.play_progress_info);
		//
		// updateData(itemIndex, holder, item);
		// }
	}

	class ViewHolder_Banner
	{
		ImageView img;
	}

	class ViewHolder_FristBanner
	{
		ImageView top_iv;
		ImageView left_iv;
		ImageView right_iv;
	}

	class ViewHolder_Main
	{

		TextView name_tv;
		TextView address_tv;
		TextView activity_tv;
		TextView room_tv;
		TextView center_tv;
		ImageView space_bg_iv;
		TextView shopping_areas_tv;
		TextView preface_tv;
		TextView sieve_tv;
		LinearLayout sieve_ll;
	}

	class ViewHolder_Null
	{

	}

	@Override
	public void onClick(View v)
	{

	}

}
