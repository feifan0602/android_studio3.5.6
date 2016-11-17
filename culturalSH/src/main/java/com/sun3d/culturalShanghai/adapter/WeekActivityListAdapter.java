package com.sun3d.culturalShanghai.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.R;
import com.sun3d.culturalShanghai.Util.AppConfigUtil;
import com.sun3d.culturalShanghai.Util.Options;
import com.sun3d.culturalShanghai.Util.TextUtil;
import com.sun3d.culturalShanghai.activity.ActivityDetailActivity;
import com.sun3d.culturalShanghai.activity.BannerInfoActivity;
import com.sun3d.culturalShanghai.activity.BannerWebView;
import com.sun3d.culturalShanghai.activity.EventListActivity;
import com.sun3d.culturalShanghai.activity.ThisWeekActivity;
import com.sun3d.culturalShanghai.activity.VenueDetailActivity;
import com.sun3d.culturalShanghai.activity.VenueListActivity;
import com.sun3d.culturalShanghai.adapter.ActivityListAdapter.ViewHolder_Advertisement;
import com.sun3d.culturalShanghai.animation.ContainerAnimation;
import com.sun3d.culturalShanghai.handler.WeekDateHandler;
import com.sun3d.culturalShanghai.object.ActivityConditionInfo;
import com.sun3d.culturalShanghai.object.BannerInfo;
import com.sun3d.culturalShanghai.object.EventInfo;
import com.sun3d.culturalShanghai.object.ScreenInfo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressLint("UseSparseArrays")
public class WeekActivityListAdapter extends BaseAdapter {
	private Map<Integer, View> m = new HashMap<Integer, View>();
	private Context mContext;
	private List<EventInfo> list;
	private TextView Show_datetv;
	private View view;
	private List<BannerInfo> mlistBannerInfo;
	private View view_week;
	private static final int TYPE_BANNER = 0;// 廣告類型
	private static final int TYPE_MAIN = 1;// 主類型
	private int currentType;// 当前item类型
	private static final int TYPE_COUNT = 2;// item类型的总数
	private ViewHolder vh_main;
	private String TAG = "WeekActivityListAdapter";

	public WeekActivityListAdapter(Context mContext, List<EventInfo> list,
			List<BannerInfo> listBannerInfo) {
		// TODO Auto-generated constructor stub
		this.mContext = mContext;
		this.mlistBannerInfo = listBannerInfo;
		this.list = list;
		// view = View.inflate(mContext, R.layout.weektop_tv, null);
		// Show_datetv = (TextView) view.findViewById(R.id.toptv);
		// Show_datetv.setText(TextUtil.Time2Format(TextUtil.getToDay()));
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

	public void setList(List<EventInfo> list, List<BannerInfo> listBannerInfo) {
		this.list = list;
		this.mlistBannerInfo = listBannerInfo;
		this.notifyDataSetChanged();
	}

	@Override
	public View getView(int arg0, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		currentType = getItemViewType(arg0);
		if (currentType == TYPE_MAIN) {
			convertView = addMainView(arg0, convertView);
		} else if (currentType == TYPE_BANNER) {
			convertView = addBannerView(arg0, convertView);
		}
		return convertView;
		// convertView = m.get(arg0);
		// convertView = addAcrivitiTopPage(arg0, convertView);
		// m.put(arg0, convertView);
		// return convertView;
	}

	ContainerAnimation ca = new ContainerAnimation();

	@SuppressLint("NewApi")
	WeekHandler mTopPageHolder = null;

	@Override
	public int getItemViewType(int position) {
		if (mlistBannerInfo.size() != 0
				&& mlistBannerInfo.get(0).getAdvImgUrl() != null
				&& position == 0) {
			return TYPE_BANNER;// 广告类型
		} else {
			return TYPE_MAIN;// 每隔三个有一个广告类型
		}
		// if (position >= 0) {
		//
		// } else {
		//
		// }
	}

	@Override
	public int getViewTypeCount() {
		return TYPE_COUNT;
	}

	// private View addAcrivitiTopPage(int arg0, View convertView) {
	// if (arg0 > 1) {
	// view = addMainView(arg0, convertView);
	// } else {
	// view = addNull(arg0, convertView);
	// }
	// return addNull(arg0, convertView);
	// if (convertView == null) {
	// mTopPageHolder = new WeekHandler();
	// convertView = View.inflate(mContext, R.layout.weeklayout, null);
	// mTopPageHolder.weeklayout = (LinearLayout) convertView
	// .findViewById(R.id.weeklayoutid);
	// mTopPageHolder.guang_gao = (ImageView) convertView
	// .findViewById(R.id.guang_gao);
	// mTopPageHolder.mWeekDateHandler = new WeekDateHandler(mContext,
	// mTopPageHolder.weeklayout, Show_datetv,
	// this.mWeekLisenner);
	// convertView.setTag(mTopPageHolder);
	// } else {
	// mTopPageHolder = (WeekHandler) convertView.getTag();
	// }
	// ViewGroup.LayoutParams lp = mTopPageHolder.guang_gao
	// .getLayoutParams();
	// lp.width = MyApplication.getWindowWidth();
	// lp.height = MyApplication.getWindowWidth() / 5;
	// mTopPageHolder.guang_gao.setLayoutParams(lp);
	// if (mlistBannerInfo.size() > 0) {
	// MyApplication
	// .getInstance()
	// .getImageLoader()
	// .displayImage(
	// TextUtil.getUrlSmall_750_150(mlistBannerInfo
	// .get(0).getAdvertPicUrl()),
	// mTopPageHolder.guang_gao);
	// }
	// mTopPageHolder.guang_gao
	// .setOnClickListener(new View.OnClickListener() {
	// @Override
	// public void onClick(View v) {
	// Intent intent = new Intent(mContext,
	// BannerInfoActivity.class);
	// mContext.startActivity(intent);
	// }
	// });
	// return convertView;
	// }
	// else {
	// return view;
	// }
	// }

	private class WeekHandler {
		private WeekDateHandler mWeekDateHandler;
		private LinearLayout weeklayout;
		private ImageView guang_gao;
	}

	private View addMainView(final int pos, View convertView) {
		if (convertView == null) {
			vh_main = new ViewHolder();
			convertView = View.inflate(mContext,
					R.layout.adapter_week_fragment_list_item, null);
			vh_main.ll = (LinearLayout) convertView.findViewById(R.id.ll);
			vh_main.img = (ImageView) convertView.findViewById(R.id.img);
			vh_main.top_left_img = (ImageView) convertView
					.findViewById(R.id.top_left_img);
			vh_main.buttom_right_tv = (TextView) convertView
					.findViewById(R.id.buttom_right);
			vh_main.name_tv = (TextView) convertView.findViewById(R.id.name_tv);
			vh_main.address_tv = (TextView) convertView
					.findViewById(R.id.address_tv);
			vh_main.item_subject = (TextView) convertView
					.findViewById(R.id.item_subject);
			vh_main.item_subject1 = (TextView) convertView
					.findViewById(R.id.item_subject1);
			vh_main.item_subject2 = (TextView) convertView
					.findViewById(R.id.item_subject2);
			vh_main.buttom_right_tv.setTypeface(MyApplication.GetTypeFace());
			vh_main.name_tv.setTypeface(MyApplication.GetTypeFace());
			vh_main.address_tv.setTypeface(MyApplication.GetTypeFace());
			vh_main.item_subject.setTypeface(MyApplication.GetTypeFace());
			vh_main.item_subject1.setTypeface(MyApplication.GetTypeFace());
			vh_main.item_subject2.setTypeface(MyApplication.GetTypeFace());
			convertView.setTag(vh_main);
		} else {
			vh_main = (ViewHolder) convertView.getTag();
		}
		if (list.size() != 0) {
			MyApplication
					.getInstance()
					.getImageLoader()
					.displayImage(
							TextUtil.getUrlMiddle(list.get(pos)
									.getActivityIconUrl()), vh_main.img,
							Options.getRoundOptions(0));
			vh_main.name_tv.setText(list.get(pos).getActivityName());
			if (list.get(pos).getActivityPrice().equals("0")) {
				vh_main.buttom_right_tv.setText("免费");
			} else {
				if (list.get(pos).getPriceType() == 0) {
					// XX元起
					vh_main.buttom_right_tv.setText(list.get(pos)
							.getActivityPrice() + "元起");
				} else {
					// XX元/人
					vh_main.buttom_right_tv.setText(list.get(pos)
							.getActivityPrice() + "元/人");
				}
			}

			if (list.get(pos).getActivityEndTime() != null
					&& !list.get(pos).getActivityStartTime()
							.equals(list.get(pos).getActivityEndTime())) {
				String start = list.get(pos).getActivityStartTime()
						.replaceAll("-", ".").substring(5, 10);
				String end = list.get(pos).getActivityEndTime()
						.replaceAll("-", ".").substring(5, 10);
				if (!list.get(pos).getActivityLocationName().equals("")) {
					vh_main.address_tv.setText(start + "-" + end + "|"
							+ list.get(pos).getActivityLocationName());
				} else {
					vh_main.address_tv.setText(start + "-" + end + "|"
							+ list.get(pos).getActivityAddress());
				}

			} else {
				String start = list.get(pos).getActivityStartTime()
						.replaceAll("-", ".").substring(5, 10);
				if (!list.get(pos).getActivityLocationName().equals("")) {
					vh_main.address_tv.setText(start + "|"
							+ list.get(pos).getActivityLocationName());
				} else {
					vh_main.address_tv.setText(start + "|"
							+ list.get(pos).getActivityAddress());
				}

			}
			vh_main.name_tv.setText(list.get(pos).getActivityName());
			if (list.get(pos).getActivityIsReservation().equals("2")) {
				vh_main.top_left_img.setVisibility(View.VISIBLE);
				if (list.get(pos).getSpikeType() == 1) {
					vh_main.top_left_img.setImageResource(R.drawable.icon_miao);
				} else {
					vh_main.top_left_img.setImageResource(R.drawable.icon_ding);
				}
			} else {
				vh_main.top_left_img.setVisibility(View.GONE);
			}
			if (list.get(pos).getTagName() != null
					&& !list.get(pos).getTagName().equals("")) {
				vh_main.item_subject.setVisibility(View.VISIBLE);
				vh_main.item_subject.setText(list.get(pos).getTagName());
			} else {
				vh_main.item_subject.setVisibility(View.GONE);
			}
			if (list.get(pos).getTagNameList() == null
					|| list.get(pos).getTagNameList().size() == 0) {
				vh_main.item_subject1.setVisibility(View.GONE);
				vh_main.item_subject2.setVisibility(View.GONE);
			} else {
				if (list.get(pos).getTagNameList().size() == 1) {
					vh_main.item_subject1.setVisibility(View.VISIBLE);
					vh_main.item_subject2.setVisibility(View.GONE);
					vh_main.item_subject1.setText(list.get(pos)
							.getTagNameList().get(0));
				} else if (list.get(pos).getTagNameList().size() > 1) {
					vh_main.item_subject1.setVisibility(View.VISIBLE);
					vh_main.item_subject2.setVisibility(View.VISIBLE);
					vh_main.item_subject1.setText(list.get(pos)
							.getTagNameList().get(0));
					vh_main.item_subject2.setText(list.get(pos)
							.getTagNameList().get(1));
				} else {
					vh_main.item_subject1.setVisibility(View.GONE);
					vh_main.item_subject2.setVisibility(View.GONE);
				}
			}

		}
		return convertView;
	}

	class ViewHolder {
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

	private WeekLisenner mWeekLisenner;

	public void setWeekLisenner(WeekLisenner weekLisenner) {
		this.mWeekLisenner = weekLisenner;
	}

	public interface WeekLisenner {
		public void onWeekClick(String date);
	}

	private View addNull(int arg0, View convertView) {
		if (convertView == null) {
			convertView = View.inflate(mContext,
					R.layout.adapter_event_null_list_item, null);
		}

		return convertView;
	}

	private View addBannerView(int arg0, View convertView) {
		ViewHolder_Advertisement vha = null;
		if (convertView == null) {
			vha = new ViewHolder_Advertisement();
			convertView = View.inflate(mContext,
					R.layout.adapter_event_advertisement_list_item, null);
			vha.img = (ImageView) convertView.findViewById(R.id.img);
			convertView.setTag(vha);
		} else {
			vha = (ViewHolder_Advertisement) convertView.getTag();
		}
		final int advBannerFIsLink = mlistBannerInfo.get(arg0).getAdvLink();
		final String url = mlistBannerInfo.get(arg0).getAdvUrl();
		vha.img.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				if (advBannerFIsLink == 0) {
					// 内链
					// selectImg("0", advBannerFUrl);
					// 接口沒有返回
				} else {
					// 外链
					selectWeb(url);
				}
			}

		});
		MyApplication
				.getInstance()
				.getImageLoader()
				.displayImage(mlistBannerInfo.get(arg0).getAdvImgUrl(), vha.img);
		// vha.img.setBackgroundResource(R.drawable.smile);
		return convertView;
	}

	public void selectImg(int type, String activityInfo) {
		Intent intent;

		switch (type) {

		// 活动列表
		case 0:
			intent = new Intent(mContext, EventListActivity.class);
			ActivityConditionInfo event_activityInfo = new ActivityConditionInfo();
			event_activityInfo.setActivityKeyWord(activityInfo);
			intent.putExtra("tagId", "");
			intent.putExtra("activityInfo", event_activityInfo);
			intent.putExtra("searchType", "");
			intent.putExtra("AreaId", "");
			intent.putExtra("ActivityName", "");
			// 这个ID 还不知道传什么
			mContext.startActivity(intent);
			break;
		// 活动详情
		case 1:
			intent = new Intent(mContext, ActivityDetailActivity.class);
			// 这个ID 还不知道传什么
			intent.putExtra("eventId", activityInfo);
			mContext.startActivity(intent);
			break;
		// 场馆列表
		case 2:
			// intent = new Intent(mContext, VenueDetailActivity.class);
			// intent.putExtra("venueId", activityInfo);
			// mContext.startActivity(intent);
			intent = new Intent(mContext, VenueListActivity.class);
			ScreenInfo screenInfo = new ScreenInfo();
			screenInfo.setSerach(activityInfo);
			intent.putExtra(AppConfigUtil.INTENT_SCREENINFO, screenInfo);
			mContext.startActivity(intent);
			break;
		// 场馆详情
		case 3:
			intent = new Intent(mContext, VenueDetailActivity.class);
			intent.putExtra("venueId", activityInfo);
			mContext.startActivity(intent);
			break;
		}
	}

	private void selectWeb(String url) {
		Intent intent = new Intent();
		intent.setClass(mContext, BannerWebView.class);
		intent.putExtra("url", url);
		mContext.startActivity(intent);
	}

	class ViewHolder_Advertisement {
		ImageView img;
	}
}
