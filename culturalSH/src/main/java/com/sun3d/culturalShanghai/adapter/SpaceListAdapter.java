package com.sun3d.culturalShanghai.adapter;

import java.util.ArrayList;
import java.util.List;

import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.R;
import com.sun3d.culturalShanghai.Util.AppConfigUtil;
import com.sun3d.culturalShanghai.Util.TextUtil;
import com.sun3d.culturalShanghai.Util.ToastUtil;
import com.sun3d.culturalShanghai.activity.ActivityDetailActivity;
import com.sun3d.culturalShanghai.activity.BannerWebView;
import com.sun3d.culturalShanghai.activity.EventListActivity;
import com.sun3d.culturalShanghai.activity.VenueDetailActivity;
import com.sun3d.culturalShanghai.activity.VenueListActivity;
import com.sun3d.culturalShanghai.adapter.ActivityHomeListAdapter.ViewHolder_Advertisement;
import com.sun3d.culturalShanghai.fragment.ActivityFragment;
import com.sun3d.culturalShanghai.fragment.SpaceFragment;
import com.sun3d.culturalShanghai.object.ActivityConditionInfo;
import com.sun3d.culturalShanghai.object.EventInfo;
import com.sun3d.culturalShanghai.object.ScreenInfo;
import com.sun3d.culturalShanghai.object.SpaceInfo;
import com.sun3d.culturalShanghai.object.Space_Info;
import com.sun3d.culturalShanghai.object.VenueDetailInfor;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SpaceListAdapter extends BaseAdapter implements OnClickListener {
	private static final int TYPE_COUNT = 3;// item类型的总数
	private static final int TYPE_BANNER = 2;// 廣告類型
	private static final int TYPE_MAIN = 1;// 主類型
	private static final int TYPE_FRIST = 0;// 顶部三个广告
	private static final int TYPE_NULL = 5;// 空的布局
	private static final int TYPE_BANNERLAST = 4;// 空的布局
	private List<SpaceInfo> mList = new ArrayList<SpaceInfo>();
	private List<SpaceInfo> list_banner = new ArrayList<SpaceInfo>();
	private int currentType;// 当前item类型
	private Context mContext;
	private SpaceFragment space_fragment;
	private ViewHolder_Banner banner_vh;
	private ViewHolder_FristBanner fristbanner_vh;
	private ViewHolder_Main main_vh;
	private ViewHolder_Null null_vh;
	private String TAG = "SpaceListAdapter";
	private View FristBanner_view, Banner_view, Main_view, Null_view;
	private boolean haveBannerCell = false;
	private ListView mListView;
	/**
	 * 判断 选项框 是否显示1 为显示 2 为不显示
	 */
	private int pop_gone_visible = 1;
	private int pos_img;
	private int num = 0;
	private String content = "";
	private SpaceInfo topInfo, leftInfo, rightInfo;

	public SpaceListAdapter(Context mContext, boolean isShowAddress,
			SpaceFragment sf) {
		// TODO Auto-generated constructor stub
		this.space_fragment = sf;
		this.mContext = mContext;
	}

	public void setList(List<SpaceInfo> list) {
		this.mList = list;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (haveBannerCell) {
			return mList.size() + 1;
		} else {
			return mList.size();
		}
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		if (haveBannerCell) {
			if (arg0 > 0)
				return mList.get(arg0 - 1);
			else
				return this.list_banner.get(0);
		} else {
			return mList.get(arg0);
		}

	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public int getViewTypeCount() {
		return TYPE_COUNT;
	}

	@Override
	public int getItemViewType(int position) {
		if (position == 0) {
			return TYPE_FRIST;// 顶部的广告
		} else {
			return TYPE_MAIN;// 主配置文件
		}

	}

	@Override
	public View getView(int arg0, View convertView, ViewGroup arg2) {
		if (mListView == null)
			mListView = (ListView) arg2;

		currentType = getItemViewType(arg0);
		if (currentType == TYPE_BANNER) {
			convertView = addBannerView(arg0, convertView);
		} else if (currentType == TYPE_FRIST) {
			convertView = addFristBannerView(arg0, convertView);
		} else if (currentType == TYPE_MAIN) {
			convertView = addMainView(arg0, convertView);
		} else if (currentType == TYPE_NULL) {
			convertView = addNull(arg0, convertView);
		}
		return convertView;
	}

	private View addBannerView(int arg0, View convertView) {

		if (convertView == null) {
			banner_vh = new ViewHolder_Banner();
			Banner_view = View.inflate(mContext,
					R.layout.adapter_event_advertisement_list_item, null);
			banner_vh.img = (ImageView) Banner_view.findViewById(R.id.img);
			Banner_view.setTag(banner_vh);
			convertView = Banner_view;
		} else {
			banner_vh = (ViewHolder_Banner) convertView.getTag();
		}
		MyApplication.getInstance().setImageView(mContext,
				MyApplication.Text_Big_Url, banner_vh.img);
		return convertView;
	}

	private View addFristBannerView(int arg0, View convertView) {
		if (convertView == null) {
			fristbanner_vh = new ViewHolder_FristBanner();
			FristBanner_view = View.inflate(mContext,
					R.layout.space_item_fristbanner_adapter, null);
			fristbanner_vh.top_iv = (ImageView) FristBanner_view
					.findViewById(R.id.top_img);
			fristbanner_vh.left_iv = (ImageView) FristBanner_view
					.findViewById(R.id.left_img);
			fristbanner_vh.right_iv = (ImageView) FristBanner_view
					.findViewById(R.id.right_img);
			FristBanner_view.setTag(fristbanner_vh);
			convertView = FristBanner_view;
		} else {
			fristbanner_vh = (ViewHolder_FristBanner) convertView.getTag();
		}
		if (list_banner == null || list_banner.size() == 0) {
			// MyApplication.getInstance().setImageView(
			// MyApplication.getContext(), MyApplication.Text_Url,
			// fristbanner_vh.top_iv);
			// MyApplication.getInstance().setImageView(
			// MyApplication.getContext(), MyApplication.Text_Url,
			// fristbanner_vh.left_iv);
			// MyApplication.getInstance().setImageView(
			// MyApplication.getContext(), MyApplication.Text_Url,
			// fristbanner_vh.right_iv);
		} else {
			for (int i = 0; i < list_banner.size(); i++) {
				pos_img = i;
				switch (list_banner.get(i).getAdvertSort()) {
				case 1:
					MyApplication
							.getInstance()
							.getImageLoader()
							.displayImage(
									TextUtil.getUrlSmall_750_250(list_banner
											.get(pos_img).getAdvertImgUrl()),
									fristbanner_vh.top_iv);
					topInfo = list_banner.get(i);
					fristbanner_vh.top_iv.setOnClickListener(this);

					break;
				case 2:
					MyApplication
							.getInstance()
							.getImageLoader()
							.displayImage(
									TextUtil.getUrlSmall_750_310(list_banner
											.get(pos_img).getAdvertImgUrl()),
									fristbanner_vh.left_iv);
					leftInfo = list_banner.get(pos_img);
					fristbanner_vh.left_iv.setOnClickListener(this);

					break;
				case 3:
					MyApplication
							.getInstance()
							.getImageLoader()
							.displayImage(
									TextUtil.getUrlSmall_750_310(list_banner
											.get(pos_img).getAdvertImgUrl()),
									fristbanner_vh.right_iv);
					rightInfo = list_banner.get(pos_img);
					fristbanner_vh.right_iv.setOnClickListener(this);

					break;
				}
			}
		}
		return convertView;
	}

	/**
	 * 获取顶部三个广告的数据
	 * 
	 * @param list_banner
	 */
	public void setBannerList(List<SpaceInfo> list_banner) {
		this.list_banner = list_banner;
		if (this.list_banner != null && this.list_banner.size() > 0) {
			haveBannerCell = true;
		} else {
			haveBannerCell = false;
		}
		this.notifyDataSetChanged();
	}

	private View addMainView(int arg0, View convertView) {

		if (convertView == null) {
			main_vh = new ViewHolder_Main();
			Main_view = View.inflate(mContext,
					R.layout.space_item_main_adapter, null);
			main_vh.name_tv = (TextView) Main_view.findViewById(R.id.name_tv);
			main_vh.address_tv = (TextView) Main_view
					.findViewById(R.id.address_tv);
			main_vh.activity_tv = (TextView) Main_view
					.findViewById(R.id.activity_num_tv);
			main_vh.room_tv = (TextView) Main_view
					.findViewById(R.id.room_num_tv);
			main_vh.space_bg_iv = (ImageView) Main_view
					.findViewById(R.id.space_bg);
			main_vh.sieve_ll = (LinearLayout) Main_view
					.findViewById(R.id.Sieve);
			main_vh.shopping_areas_tv = (TextView) Main_view
					.findViewById(R.id.shopping_areas);
			main_vh.preface_tv = (TextView) Main_view
					.findViewById(R.id.preface);
			main_vh.sieve_tv = (TextView) Main_view.findViewById(R.id.sieve);
			main_vh.center_tv = (TextView) Main_view
					.findViewById(R.id.middle_tv);
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
		} else {
			main_vh = (ViewHolder_Main) convertView.getTag();
		}
		int venueIndex = 0;
		if (haveBannerCell && arg0 > 0) {
			venueIndex = arg0 - 1;
		}
		// 这里以后要判断 是否有广告
		if (arg0 == 1 && pop_gone_visible == 1) {
			main_vh.sieve_ll.setVisibility(View.VISIBLE);
			main_vh.shopping_areas_tv.setOnClickListener(this);
			main_vh.sieve_tv.setOnClickListener(this);
			main_vh.preface_tv.setOnClickListener(this);
			Log.i(TAG, "看看传入的数据 ==  " + content + "  num==  " + num);
			if (num == 1) {
				main_vh.shopping_areas_tv.setText(content);
			} else if (num == 2) {
				main_vh.preface_tv.setText(content);
			} else if (num == 3) {
				main_vh.sieve_tv.setText(content);
			}
		} else {
			main_vh.sieve_ll.setVisibility(View.GONE);
			main_vh.shopping_areas_tv.setOnClickListener(null);
			main_vh.sieve_tv.setOnClickListener(null);
			main_vh.preface_tv.setOnClickListener(null);
		}
		if (mList.size() != 0 && mList.get(venueIndex) != null) {
			MyApplication.getInstance().setImageView(
					MyApplication.getContext(),
					TextUtil.getUrlMiddle(mList.get(venueIndex)
							.getVenueIconUrl()), main_vh.space_bg_iv);
			main_vh.name_tv.setText(mList.get(venueIndex).getVenueName());
			main_vh.address_tv.setText(mList.get(venueIndex).getVenueAddress());
			// 暂时的数据

			int numOfActivity = mList.get(venueIndex).getActCount();
			int numOfRooms = mList.get(venueIndex).getRoomCount();

			if (numOfActivity > 0) {
				main_vh.activity_tv.setVisibility(View.VISIBLE);
				String str_activity = numOfActivity + "个在线活动";
				main_vh.activity_tv.setText(MyApplication
						.getYellowTextColor(str_activity));
				if (numOfRooms == 0) {// 单个需要居中
					main_vh.center_tv.setVisibility(View.VISIBLE);
					main_vh.activity_tv.setVisibility(View.GONE);
					main_vh.center_tv.setText(MyApplication
							.getYellowTextColor(str_activity));
				}
			} else {
				main_vh.activity_tv.setVisibility(View.GONE);
			}

			if (numOfRooms > 0) {
				String str_venue = numOfRooms + "个活动室";
				main_vh.room_tv.setVisibility(View.VISIBLE);
				main_vh.room_tv.setText(MyApplication
						.getYellowTextColor(str_venue));
				if (numOfActivity == 0) {// 单个需要居中
					main_vh.center_tv.setVisibility(View.VISIBLE);
					main_vh.room_tv.setVisibility(View.GONE);
					main_vh.center_tv.setText(MyApplication
							.getYellowTextColor(str_venue));
				}

			} else {
				main_vh.room_tv.setVisibility(View.GONE);
			}
			if (numOfRooms > 0 && numOfActivity > 0 || numOfActivity == 0
					&& numOfRooms == 0) {
				main_vh.center_tv.setVisibility(View.GONE);
			}

		}

		return convertView;
	}

	public void updateView(int itemIndex) {
		// 得到第一个可显示控件的位置，
		if (mListView != null) {
			View view = mListView.getChildAt(3);
			if (view != null) {
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

	private View addNull(int arg0, View convertView) {

		if (convertView == null) {
			null_vh = new ViewHolder_Null();
			convertView = View.inflate(mContext,
					R.layout.adapter_event_advertisement_list_item, null);
			convertView.setTag(null_vh);
		} else {
			null_vh = (ViewHolder_Null) convertView.getTag();
		}
		return convertView;
	}

	class ViewHolder_Banner {
		ImageView img;
	}

	class ViewHolder_FristBanner {
		ImageView top_iv;
		ImageView left_iv;
		ImageView right_iv;
	}

	class ViewHolder_Main {

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

	class ViewHolder_Null {

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.shopping_areas:
			space_fragment.showPopuwindow(1, 1);
			break;
		case R.id.preface:
			space_fragment.showPopuwindow(2, 1);
			break;
		case R.id.sieve:
			space_fragment.showPopuwindow(3, 1);
			break;
		case R.id.top_img:
			if (topInfo.getAdvertLink() == 0) {
				MyApplication.selectImg(mContext, topInfo.getAdvertLinkType(),
						topInfo.getAdvertUrl());
			} else {
				MyApplication.selectWeb(mContext, topInfo.getAdvertUrl());
			}
			break;
		case R.id.left_img:
			if (leftInfo.getAdvertLink() == 0) {
				MyApplication.selectImg(mContext, leftInfo.getAdvertLinkType(),
						leftInfo.getAdvertUrl());
			} else {
				MyApplication.selectWeb(mContext, leftInfo.getAdvertUrl());
			}

			break;
		case R.id.right_img:
			if (rightInfo.getAdvertLink() == 0) {
				MyApplication
						.selectImg(mContext, rightInfo.getAdvertLinkType(),
								rightInfo.getAdvertUrl());
			} else {
				MyApplication.selectWeb(mContext, rightInfo.getAdvertUrl());
			}
			break;
		default:
			break;
		}

	}

	public void setGone() {
		pop_gone_visible = 2;
		notifyDataSetChanged();
	}

	public void setText(int num_type, String Content) {
		num = num_type;
		content = Content;
	};

	public void setVisible() {
		pop_gone_visible = 1;
		notifyDataSetChanged();

	}
}
