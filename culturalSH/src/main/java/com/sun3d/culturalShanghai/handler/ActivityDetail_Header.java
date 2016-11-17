package com.sun3d.culturalShanghai.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.R;
import com.sun3d.culturalShanghai.Util.AppConfigUtil;
import com.sun3d.culturalShanghai.Util.CollectUtil;
import com.sun3d.culturalShanghai.Util.Options;
import com.sun3d.culturalShanghai.Util.TextUtil;
import com.sun3d.culturalShanghai.Util.ToastUtil;
import com.sun3d.culturalShanghai.Util.ValidateUtil;
import com.sun3d.culturalShanghai.Util.ViewUtil;
import com.sun3d.culturalShanghai.activity.BigMapViewActivity;
import com.sun3d.culturalShanghai.activity.EventListActivity;
import com.sun3d.culturalShanghai.activity.EventReserveActivity;
import com.sun3d.culturalShanghai.activity.NearbyParkingActivity;
import com.sun3d.culturalShanghai.activity.UserDialogActivity;
import com.sun3d.culturalShanghai.callback.CollectCallback;
import com.sun3d.culturalShanghai.dialog.DialogTypeUtil;
import com.sun3d.culturalShanghai.dialog.MessageDialog;
import com.sun3d.culturalShanghai.object.EventInfo;
import com.sun3d.culturalShanghai.view.FastBlur;

/**
 * 活动头部
 * 
 * @author yangyoutao
 */
public class ActivityDetail_Header implements OnClickListener {
	private LinearLayout content;
	private Context mContext;
	private ImageView ivHeader;
	private TextView tvActivityName;
	private TextView tvActivityLocation;
	private TextView tvActivityPhone;
	private TextView tvActivityMoney;
	private EventInfo eventInfo;
	private ImageView ivCollect;
	private boolean isCollect;
	private ImageView mmoney_iv;
	private TextView mTime;
	private TextView mTimeQuantum;
	private TextView mDidian;
	private RelativeLayout Re_Notice;
	private TextView venue_Notice_quantum;
	private ImageView back_iv;
	private Activity activity;
	private TextView ableCount, name, name1, name2;
	private StringBuffer sb;
	private List<Integer> list;
	private WebView reminder_web;
	private TextView price, last_name;
	private String TAG = "ActivityDetail_Header";

	public LinearLayout getContent() {
		return content;
	}

	public ActivityDetail_Header(Context context, Activity activity) {
		this.mContext = context;
		this.activity = activity;
		content = (LinearLayout) LayoutInflater.from(mContext).inflate(
				R.layout.activity_headerlayout, null);
		Re_Notice = (RelativeLayout) content.findViewById(R.id.venue_sj_Notice);
		reminder_web = (WebView) content.findViewById(R.id.reminder_web);
		venue_Notice_quantum = (TextView) content
				.findViewById(R.id.venue_Notice_quantum);
		ivHeader = (ImageView) content.findViewById(R.id.activity_details_head);
		tvActivityName = (TextView) content.findViewById(R.id.event_name);
		tvActivityLocation = (TextView) content
				.findViewById(R.id.venue_item_location);
		tvActivityPhone = (TextView) content
				.findViewById(R.id.venue_item_phone);
		tvActivityMoney = (TextView) content
				.findViewById(R.id.event_item_money);
		price = (TextView) content.findViewById(R.id.price);
		last_name = (TextView) content.findViewById(R.id.last_name);
		mTime = (TextView) content.findViewById(R.id.venue_time_time);
		mTimeQuantum = (TextView) content.findViewById(R.id.venue_time_quantum);
		ivCollect = (ImageView) content.findViewById(R.id.event_collect);
		mmoney_iv = (ImageView) content.findViewById(R.id.event_item_money_iv);
		mDidian = (TextView) content.findViewById(R.id.venue_item_didian);
		name = (TextView) content.findViewById(R.id.name);
		name1 = (TextView) content.findViewById(R.id.name1);
		name2 = (TextView) content.findViewById(R.id.name2);
		ableCount = (TextView) content.findViewById(R.id.ableCount);
		tvActivityName.setTypeface(MyApplication.GetTypeFace());
		tvActivityLocation.setTypeface(MyApplication.GetTypeFace());
		tvActivityPhone.setTypeface(MyApplication.GetTypeFace());
		tvActivityMoney.setTypeface(MyApplication.GetTypeFace());

		mDidian.setTypeface(MyApplication.GetTypeFace());
		price.setTypeface(MyApplication.GetTypeFace());
		last_name.setTypeface(MyApplication.GetTypeFace());
		mTime.setTypeface(MyApplication.GetTypeFace());
		mTimeQuantum.setTypeface(MyApplication.GetTypeFace());
		name.setTypeface(MyApplication.GetTypeFace());
		name1.setTypeface(MyApplication.GetTypeFace());
		name2.setTypeface(MyApplication.GetTypeFace());
		ableCount.setTypeface(MyApplication.GetTypeFace());
		back_iv = (ImageView) content.findViewById(R.id.back_iv);
		back_iv.setOnClickListener(this);
		ivCollect.setOnClickListener(this);
		content.findViewById(R.id.event_parking).setOnClickListener(this);
		content.findViewById(R.id.venue_location).setOnClickListener(this);
		content.findViewById(R.id.venue_phone).setOnClickListener(this);
	}

	public void setHeaderData(EventInfo eventInfo) {
		this.eventInfo = eventInfo;

		// 1表示已经收藏，0表示未收藏
		if (eventInfo.getEventIsCollect() != null
				&& "1".equals(eventInfo.getEventIsCollect())) {
			ivCollect.setImageResource(R.drawable.sh_icon_collect_after);
			isCollect = true;
		} else {
			ivCollect.setImageResource(R.drawable.sh_icon_collect_befor);
			isCollect = false;
		}
		MyApplication
				.getInstance()
				.getImageLoader()
				.displayImage(
						TextUtil.getUrlMiddle(eventInfo
								.getEventDetailsIconUrl()), ivHeader,
						Options.getListOptions());
		// MyApplication.getInstance().setImageView(MyApplication.getContext(),eventInfo.getEventDetailsIconUrl(),ivHeader);
		tvActivityName.setText(eventInfo.getEventName());
		tvActivityLocation.setText(eventInfo.getEventAddress());
		tvActivityPhone.setText(eventInfo.getEventTel());

		// 3.5.4 标签 最新
		if (eventInfo.getTagName() != null
				&& !eventInfo.getTagName().equals("")) {
			name.setVisibility(View.VISIBLE);
			name.setText(eventInfo.getTagName());
		}
		ArrayList<String> tagNameList = eventInfo.getTagNameList();
		if (tagNameList!=null&&tagNameList.size() != 0) {
			if (tagNameList.size() == 1) {
				name1.setVisibility(View.VISIBLE);
				name1.setText(tagNameList.get(0));
			}
			if (tagNameList.size() > 1) {
				name1.setVisibility(View.VISIBLE);
				name2.setVisibility(View.VISIBLE);
				name1.setText(tagNameList.get(0));
				name2.setText(tagNameList.get(1));
			}
		} else {
			name1.setVisibility(View.GONE);
			name2.setVisibility(View.GONE);
		}
		// if (eventInfo.getActivityTags() != null) {
		// if (eventInfo.getActivityTags().size() == 1) {
		// name.setText(eventInfo.getActivityTags().get(0));
		// } else if (eventInfo.getActivityTags().size() == 2) {
		// name1.setVisibility(View.VISIBLE);
		// name.setText(eventInfo.getActivityTags().get(0));
		// name1.setText(eventInfo.getActivityTags().get(1));
		// } else if (eventInfo.getActivityTags().size() == 3) {
		// name1.setVisibility(View.VISIBLE);
		// name2.setVisibility(View.VISIBLE);
		// name.setText(eventInfo.getActivityTags().get(0));
		// name1.setText(eventInfo.getActivityTags().get(1));
		// name2.setText(eventInfo.getActivityTags().get(2));
		// }
		// } else {
		// name.setVisibility(View.GONE);
		// name1.setVisibility(View.GONE);
		// name2.setVisibility(View.GONE);
		// }

		String profile_num = eventInfo.getActivityIsReservation();
		if (eventInfo.getSpikeType() == 1) {
			ableCount.setText("限时秒杀");
		} else {
			if (profile_num.equals("2")) {
				String text = "余票" + eventInfo.getActivityAbleCount() + "张";
				SpannableStringBuilder style = MyApplication.getTextColor(text);
				ableCount.setText(style);
			} else {
				ableCount.setText("无需预约");
			}
		}

		ViewUtil.setWebViewSettings(reminder_web, mContext);
		String text = eventInfo.getActivityTips();
		reminder_web.loadDataWithBaseURL(null, ViewUtil.initContent(
				ViewUtil.subString(text, true, null), mContext),
				AppConfigUtil.APP_MIMETYPE, AppConfigUtil.APP_ENCODING, null);
		mDidian.setText(eventInfo.getActivitySite());
		if (eventInfo.getActivityIsFree() == 2) {
			if (eventInfo.getEventPrice().equals("0")
					| eventInfo.getEventPrice().length() == 0) {
				price.setText("收费");
				last_name.setVisibility(View.GONE);
				tvActivityMoney.setText("收费");
				mmoney_iv.setVisibility(View.GONE);
			} else {
				if (ValidateUtil.isNumeric(eventInfo.getEventPrice())) {
					mmoney_iv.setVisibility(View.VISIBLE);
				} else {
					mmoney_iv.setVisibility(View.GONE);
				}
				price.setText(eventInfo.getEventPrice());

				last_name.setVisibility(View.VISIBLE);
				Log.i(TAG, "看看  type==  " + eventInfo.getPriceType());
				if (eventInfo.getPriceType() == 0) {
					last_name.setText("元起");
				} else {
					last_name.setText("元/人");
				}
				tvActivityMoney.setText(eventInfo.getEventPrice());
			}
		} else {
			price.setText("免费");
			last_name.setVisibility(View.GONE);
			tvActivityMoney.setText("免费");
			mmoney_iv.setVisibility(View.GONE);
		}

		if (eventInfo.getActivityStartTime() != null
				&& !eventInfo
						.getActivityStartTime()
						.replaceAll("-", ".")
						.equals(eventInfo.getEventEndTime()
								.replaceAll("-", "."))) {
			mTime.setText(eventInfo.getActivityStartTime().replaceAll("-", ".")
					+ "-" + eventInfo.getEventEndTime().replaceAll("-", "."));
		} else {
			mTime.setText(eventInfo.getActivityStartTime().replaceAll("-", "."));
		}

		if (eventInfo.getTimeQuantum() != null
				&& eventInfo.getTimeQuantum().length() > 0) {
			// mTimeQuantum.setText(TextUtil.getTime(eventInfo.getTimeQuantum()));
			String[] timeQuantum = eventInfo.getTimeQuantum().split(",");
			StringBuffer time = new StringBuffer();
			for (int i = 0; i < timeQuantum.length; i++) {
				if (i % 2 == 0 && i != 0) {
					time.append("\n");
				}
				time.append(timeQuantum[i] + "  ");
			}
			if (time != null) {
				if (!eventInfo.getActivityTimeDes().equals("")) {
					mTimeQuantum.setText(time.toString() + "\n"
							+ eventInfo.getActivityTimeDes());
				} else {
					mTimeQuantum.setText(time.toString());
				}

			}

		} else {
			mTimeQuantum.setText("没有时间");
		}

		if (eventInfo.getActivityTimeDes().length() > 0) {
			Re_Notice.setVisibility(View.GONE);
			venue_Notice_quantum.setText(eventInfo.getActivityTimeDes() + "");
		} else {
			Re_Notice.setVisibility(View.GONE);
		}

	}

	private boolean matcherReg(CharSequence c) {
		String regEx = "[^0-9]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(c.toString());
		if (m.matches()) {
			return false;
		}
		return true;
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		Intent intent = null;
		switch (arg0.getId()) {
		case R.id.event_parking:
			if (eventInfo.getEventLat() != null
					&& eventInfo.getEventLat().length() > 0
					&& eventInfo.getEventLon() != null
					&& eventInfo.getEventLon().length() > 0) {
				intent = new Intent(mContext, NearbyParkingActivity.class);
				intent.putExtra("titleContent", eventInfo.getEventName());
				intent.putExtra("lat", eventInfo.getEventLat());
				intent.putExtra("lon", eventInfo.getEventLon());
				mContext.startActivity(intent);
			} else {
				ToastUtil.showToast("没有相关位置信息");
			}
			break;
		case R.id.venue_location:
			if (eventInfo.getEventLat() != null
					&& eventInfo.getEventLat().length() > 0
					&& !eventInfo.getEventLat().equals("0")
					&& eventInfo.getEventLon() != null
					&& eventInfo.getEventLon().length() > 0
					&& !eventInfo.getEventLon().equals("0")) {
				Log.i("ceshi",
						"ditu==  " + eventInfo.getEventName() + "  lat== "
								+ eventInfo.getEventLat() + "lon == "
								+ eventInfo.getEventLon() + "  address=="
								+ eventInfo.getEventAddress());
				intent = new Intent(mContext, BigMapViewActivity.class);
				intent.putExtra(AppConfigUtil.INTENT_TYPE, "2");
				intent.putExtra("titleContent", eventInfo.getEventName());
				// 传入场馆的位置
				intent.putExtra("lat", eventInfo.getEventLat());
				intent.putExtra("lon", eventInfo.getEventLon());
				intent.putExtra("address", eventInfo.getEventAddress());
				mContext.startActivity(intent);
			} else {
				ToastUtil.showToast("没有相关位置信息");
			}
			break;
		case R.id.venue_phone:
			intent = new Intent(mContext, MessageDialog.class);
			intent.putExtra(DialogTypeUtil.DialogContent,
					tvActivityPhone.getText());
			FastBlur.getScreen((Activity) mContext);
			intent.putExtra(DialogTypeUtil.DialogType,
					DialogTypeUtil.MessageDialog.MSGTYPE_TELLPHONE);
			mContext.startActivity(intent);
			break;
		case R.id.event_collect:
			if (MyApplication.UserIsLogin) {
				onCollect();
			} else {
				FastBlur.getScreen((Activity) mContext);
				intent = new Intent(mContext, UserDialogActivity.class);
				intent.putExtra(
						DialogTypeUtil.DialogType,
						DialogTypeUtil.ThirdPartyDialogType.THIRDPARTY_FAST_LOGIN);
				mContext.startActivity(intent);
			}
			break;
		case R.id.back_iv:
			activity.finish();
			break;
		default:
			break;
		}
	}

	/**
	 * 收藏
	 */
	private void onCollect() {

		if (isCollect) {
			cancelCollect();
		} else {
			addCollect();
		}

	}

	/**
	 * 添加收藏
	 */
	private void addCollect() {
		CollectUtil.addActivity(mContext, eventInfo.getEventId(),
				new CollectCallback() {

					@Override
					public void onPostExecute(boolean isOk) {
						// TODO Auto-generated method stub
						if (isOk) {
							EventListActivity.setListCollction(1);
							ToastUtil.showToast("收藏成功");
							ivCollect
									.setImageResource(R.drawable.sh_icon_collect_after);
							eventInfo.setEventIsCollect("1");
							isCollect = !isCollect;
						} else {
							ToastUtil.showToast("收藏失败");
						}
					}
				});
	}

	/**
	 * 取消收藏
	 */
	private void cancelCollect() {
		CollectUtil.cancelActivity(mContext, eventInfo.getEventId(),
				new CollectCallback() {

					@Override
					public void onPostExecute(boolean isOk) {
						// TODO Auto-generated method stub
						if (isOk) {
							EventListActivity.setListCollction(0);
							ToastUtil.showToast("取消收藏");
							ivCollect
									.setImageResource(R.drawable.sh_icon_collect_befor);
							eventInfo.setEventIsCollect("0");
							isCollect = !isCollect;
						} else {
							ToastUtil.showToast("操作失败");
						}
					}
				});
	}

}
