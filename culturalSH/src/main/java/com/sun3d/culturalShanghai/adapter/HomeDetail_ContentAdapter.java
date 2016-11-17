package com.sun3d.culturalShanghai.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.R;
import com.sun3d.culturalShanghai.Util.TextUtil;
import com.sun3d.culturalShanghai.activity.ActivityDetailActivity;
import com.sun3d.culturalShanghai.object.HomeDetail_ContentInfor;

import java.util.List;

public class HomeDetail_ContentAdapter extends BaseAdapter {
	private Context mContext;
	private List<HomeDetail_ContentInfor> mList;
	private static final int TYPE_COUNT = 2;// item类型的总数
	private static final int TYPE_MAIN = 0;// 主類型
	private static final int TYPE_BANNER = 1;// 廣告類型
	private int currentType;// 当前item类型
	private ViewHolder_Main mMainHolder = null;
	private ViewHolder_Banner mBannerHolder = null;
	private String TAG = "HomeDetail_ContentAdapter";
	/**
	 * 广告的数量
	 */
	private int banner_num = 0;
	private int pos_num = 0;

	public HomeDetail_ContentAdapter(Context context,
			List<HomeDetail_ContentInfor> list, int num) {
		// TODO Auto-generated constructor stub
		this.mContext = context;
		this.mList = list;
		this.banner_num = num;
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
	public int getViewTypeCount() {
		return TYPE_COUNT;
	}

	@Override
	public int getItemViewType(int position) {

		if (position % 4 == 3 && position != 0 && position < banner_num * 4) {
			return TYPE_BANNER;// 每隔几行添加一个广告
		} else {
			return TYPE_MAIN;// 主配置文件
		}

	}

	@Override
	public View getView(int pos, View convertView, ViewGroup arg2) {
		currentType = getItemViewType(pos);
		if (currentType == TYPE_BANNER) {
			convertView = addBannerView(pos, convertView);
		} else if (currentType == TYPE_MAIN) {
			convertView = addMainView(pos, convertView);
		}
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

	private View addMainView(int pos, View convertView) {

		if (convertView == null) {
			convertView = View.inflate(mContext,
					R.layout.home_fragment_content_item, null);
			mMainHolder = new ViewHolder_Main();
			mMainHolder.ll = (LinearLayout) convertView.findViewById(R.id.ll);
			mMainHolder.img = (ImageView) convertView.findViewById(R.id.img);
			mMainHolder.top_left_img = (ImageView) convertView
					.findViewById(R.id.top_left_img);
			mMainHolder.buttom_right_tv = (TextView) convertView
					.findViewById(R.id.buttom_right_tv);
			mMainHolder.name_tv = (TextView) convertView
					.findViewById(R.id.name_tv);
			mMainHolder.address_tv = (TextView) convertView
					.findViewById(R.id.address_tv);
			mMainHolder.item_subject = (TextView) convertView
					.findViewById(R.id.item_subject);
			mMainHolder.item_subject1 = (TextView) convertView
					.findViewById(R.id.item_subject1);
			mMainHolder.item_subject2 = (TextView) convertView
					.findViewById(R.id.item_subject2);

			mMainHolder.buttom_right_tv
					.setTypeface(MyApplication.GetTypeFace());
			mMainHolder.name_tv.setTypeface(MyApplication.GetTypeFace());
			mMainHolder.address_tv.setTypeface(MyApplication.GetTypeFace());
			mMainHolder.item_subject.setTypeface(MyApplication.GetTypeFace());
			mMainHolder.item_subject1.setTypeface(MyApplication.GetTypeFace());
			mMainHolder.item_subject2.setTypeface(MyApplication.GetTypeFace());
			convertView.setTag(mMainHolder);
		} else {
			mMainHolder = (ViewHolder_Main) convertView.getTag();
		}
		final HomeDetail_ContentInfor info = mList.get(pos);
		if (mList.get(pos).getActivityPrice() != null) {
			if (mList.get(pos).getActivityPrice().equals("0")) {
				mMainHolder.buttom_right_tv.setText("免费");
			} else {
				if (mList.get(pos).getPriceType() == 0) {
					mMainHolder.buttom_right_tv.setText(mList.get(pos)
							.getActivityPrice() + "元起");
				} else {
					mMainHolder.buttom_right_tv.setText(mList.get(pos)
							.getActivityPrice() + "元/人");
				}
			}
		}

		mMainHolder.name_tv.setText(mList.get(pos).getActivityName());
		if (mList.get(pos).getActivityEndTime() != null
				&& !mList.get(pos).getActivityStartTime()
						.equals(mList.get(pos).getActivityEndTime())) {
			String start = mList.get(pos).getActivityStartTime()
					.replaceAll("-", ".").substring(5, 10);
			String end = mList.get(pos).getActivityEndTime()
					.replaceAll("-", ".").substring(5, 10);
			if (!mList.get(pos).getActivityLocationName().equals("")) {
				mMainHolder.address_tv.setText(start + "-" + end + " | "
						+ mList.get(pos).getActivityLocationName());
			} else {
				mMainHolder.address_tv.setText(start + "-" + end + " | "
						+ mList.get(pos).getActivityAddress());
			}

		} else {
			if (mList.get(pos).getActivityStartTime() != null) {
				String start = mList.get(pos).getActivityStartTime()
						.replaceAll("-", ".").substring(5, 10);
				if (!mList.get(pos).getActivityLocationName().equals("")) {
					mMainHolder.address_tv.setText(start + " | "
							+ mList.get(pos).getActivityLocationName());
				} else {
					mMainHolder.address_tv.setText(start + " | "
							+ mList.get(pos).getActivityAddress());
				}

			}

		}
		if (mList.get(pos).getActivityIconUrl() != null) {
			MyApplication
					.getInstance()
					.getImageLoader()
					.displayImage(
							TextUtil.getUrlMiddle(mList.get(pos)
									.getActivityIconUrl()), mMainHolder.img);
		}

		if (mList.get(pos).getActivityIsReservation() == 2) {
			if (mList.get(pos).getSpikeType() == 1) {
				mMainHolder.top_left_img.setImageResource(R.drawable.icon_miao);
			} else {
				mMainHolder.top_left_img.setImageResource(R.drawable.icon_ding);
			}
		}
		if (mList.get(pos).getTagName() != null
				&& !mList.get(pos).getTagName().equals("")
				&& !mList.get(pos).getTagName().equals("null")) {
			mMainHolder.item_subject.setVisibility(View.VISIBLE);
			mMainHolder.item_subject.setText(mList.get(pos).getTagName());
		} else {
			mMainHolder.item_subject.setVisibility(View.GONE);
		}
		if (mList.get(pos).getTagNameList() == null
				|| mList.get(pos).getTagNameList().size() == 0) {
			mMainHolder.item_subject1.setVisibility(View.GONE);
			mMainHolder.item_subject2.setVisibility(View.GONE);
		} else {
			if (mList.get(pos).getTagNameList().size() == 1) {
				mMainHolder.item_subject1.setVisibility(View.VISIBLE);
				mMainHolder.item_subject2.setVisibility(View.GONE);
				mMainHolder.item_subject1.setText(mList.get(pos)
						.getTagNameList().get(0));
			} else if (mList.get(pos).getTagNameList().size() > 1) {
				mMainHolder.item_subject1.setVisibility(View.VISIBLE);
				mMainHolder.item_subject2.setVisibility(View.VISIBLE);
				mMainHolder.item_subject1.setText(mList.get(pos)
						.getTagNameList().get(0));
				mMainHolder.item_subject2.setText(mList.get(pos)
						.getTagNameList().get(1));
			} else {
				mMainHolder.item_subject1.setVisibility(View.GONE);
				mMainHolder.item_subject2.setVisibility(View.GONE);
			}
		}

		mMainHolder.img.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(mContext,
						ActivityDetailActivity.class);
				intent.putExtra("eventId", info.getActivityId());
				MyApplication.currentCount = -1;
				MyApplication.spike_Time = -1;
				MyApplication.total_availableCount = -1;
				mContext.startActivity(intent);
			}
		});
		return convertView;
	}

	class ViewHolder_Main {
		ImageView img;
		ImageView top_left_img;
		TextView buttom_right_tv;
		TextView name_tv;
		TextView address_tv;
		TextView item_subject;
		TextView item_subject1;
		TextView item_subject2;
		LinearLayout ll;

	}

	class ViewHolder_Banner {
		ImageView img;
	}

}
