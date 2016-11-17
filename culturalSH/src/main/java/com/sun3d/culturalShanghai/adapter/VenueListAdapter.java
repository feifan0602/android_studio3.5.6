package com.sun3d.culturalShanghai.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.R;
import com.sun3d.culturalShanghai.Util.ButtonUtil;
import com.sun3d.culturalShanghai.Util.Options;
import com.sun3d.culturalShanghai.Util.TextUtil;
import com.sun3d.culturalShanghai.handler.Tab_labelHandler;
import com.sun3d.culturalShanghai.object.VenueDetailInfor;
import com.sun3d.culturalShanghai.view.SlideShowView;

public class VenueListAdapter extends BaseAdapter implements OnTouchListener {
	private Map<Integer, View> m = new HashMap<Integer, View>();
	private Context mContext;
	private List<VenueDetailInfor> list;
	private Boolean isShowView = false;
	private Boolean isShowTopPage = false;
	private int onclickindex = 2;
	private Tab_labelHandler mmTab_labelHandler;

	public VenueListAdapter(Context mContext, List<VenueDetailInfor> list) {
		// TODO Auto-generated constructor stub
		this.mContext = mContext;
		this.list = list;
		isShowView = false;
	}

	public void isShowFootView(Boolean isShow) {
		isShowView = isShow;
	}

	public void setList(List<VenueDetailInfor> list) {
		this.list = list;
		this.notifyDataSetChanged();
	}

	public void isVenueMainList(Boolean isShow) {
		isShowTopPage = isShow;
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

	@SuppressLint("NewApi")
	@Override
	public View getView(int arg0, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		convertView = m.get(arg0);
		if (!isShowTopPage) {
			convertView = addMainView(arg0, convertView);
		} else {
			convertView = addAcrivitiTopPage(arg0, convertView);
		}
		m.put(arg0, convertView);
		return convertView;
	}

	private View addAcrivitiTopPage(int arg0, View convertView) {
		if (arg0 > 0) {
			return addMainView(arg0, convertView);
		} else {
			// 这是2016.04.13 修改的 3.6版本
			convertView = addNull(arg0, convertView);
			// MyHandler mMyHandler = null;
			// if (convertView == null) {
			// mMyHandler = new MyHandler();
			// convertView = (LinearLayout) View.inflate(mContext,
			// R.layout.mainactivity_toppage, null);
			// mMyHandler.tab_layout = (LinearLayout) convertView
			// .findViewById(R.id.activity_tablayout);
			// mMyHandler.top_layout = (SlideShowView) convertView
			// .findViewById(R.id.slideshowView);
			// mMyHandler.Tab_labelHandlerm = new Tab_labelHandler(mContext,
			// mMyHandler.tab_layout, 2);
			// convertView.setTag(mMyHandler);
			// } else {
			// mMyHandler = (MyHandler) convertView.getTag();
			// }
			// mMyHandler.top_layout.setVisibility(View.GONE);
			// mMyHandler.tab_layout.setVisibility(View.VISIBLE);
			// switch (onclickindex) {
			// case 0:
			// mMyHandler.Tab_labelHandlerm.setTabNebaty();
			// break;
			// case 2:
			// mMyHandler.Tab_labelHandlerm.setTabHot();
			// break;
			// default:
			// break;
			// }
			// mmTab_labelHandler = mMyHandler.Tab_labelHandlerm;
			// mMyHandler.tab_layout.findViewById(R.id.tab_nebaty_btn)
			// .setOnTouchListener(this);
			// mMyHandler.tab_layout.findViewById(R.id.tab_hot_btn)
			// .setOnTouchListener(this);
			return convertView;
		}

	}

	/**
	 * 点击方法的回掉
	 * 
	 * @param onMyClickListener
	 */
	public void setOnMyClickListener(OnMyClickListener onMyClickListener) {
		mOnMyClickListener = onMyClickListener;
	}

	private OnMyClickListener mOnMyClickListener;

	public interface OnMyClickListener {
		public void onMyClick(View arg0, Tab_labelHandler mTab_labelHandler);
	}

	private class MyHandler {
		private LinearLayout tab_layout;
		private SlideShowView top_layout;
		private Tab_labelHandler Tab_labelHandlerm;
	}

	private View addMainView(int arg0, View arg1) {
		ViewHolder mHolder = null;
		if (arg1 == null) {
			mHolder = new ViewHolder();
			arg1 = View.inflate(mContext,
					R.layout.adapter_venue_fragment_item_update, null);
			mHolder.mName = (TextView) arg1.findViewById(R.id.venue_name);
			mHolder.mComment = (TextView) arg1.findViewById(R.id.venue_comment);
			mHolder.mAdress = (TextView) arg1.findViewById(R.id.venue_adress);
			mHolder.mUrl = (ImageView) arg1.findViewById(R.id.venue_url);
			mHolder.mCommentUrl = (ImageView) arg1
					.findViewById(R.id.venue_head_img);
			mHolder.venue_content = (TextView) arg1
					.findViewById(R.id.venue_content);
			mHolder.mBus = (ImageView) arg1.findViewById(R.id.venue_item_bus);
			mHolder.mMetro = (ImageView) arg1
					.findViewById(R.id.venue_item_metro);
			mHolder.mreserve = (ImageView) arg1
					.findViewById(R.id.venue_item_reserve);
			mHolder.mRating = (RatingBar) arg1.findViewById(R.id.venue_rating);
			mHolder.foot = (TextView) arg1.findViewById(R.id.footview);
			mHolder.commentLayout = (LinearLayout) arg1
					.findViewById(R.id.vneue_comment_layout);
			mHolder.mName.setTypeface(MyApplication.GetTypeFace());
			mHolder.mComment.setTypeface(MyApplication.GetTypeFace());
			mHolder.mAdress.setTypeface(MyApplication.GetTypeFace());
			mHolder.venue_content.setTypeface(MyApplication.GetTypeFace());
			mHolder.foot.setTypeface(MyApplication.GetTypeFace());
			arg1.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) arg1.getTag();
		}
		final VenueDetailInfor vd = list.get(arg0);
		if (vd != null) {
			mHolder.venue_content.setText(vd.getActivitySubject());
			mHolder.mName.setText(vd.getVenueName());
			mHolder.mAdress.setText(vd.getVenueAddress());
			// MyApplication
			// .getInstance()
			// .getImageLoader()
			// .displayImage(TextUtil.getUrlMiddle(vd.getVenueIconUrl()),
			// mHolder.mUrl,
			// Options.getListOptions());
			MyApplication.getInstance().setImageView(
					MyApplication.getContext(),
					TextUtil.getUrlMiddle(vd.getVenueIconUrl()), mHolder.mUrl);
			if (vd.getRemarkUserImgUrl().length() > 0) {
				MyApplication
						.getInstance()
						.getImageLoader()
						.displayImage(
								vd.getRemarkUserImgUrl(),
								mHolder.mCommentUrl,
								Options.getRoundOptions(
										R.drawable.sh_user_sex_man, 10));

			} else {
				if (null != vd.getRemarkUserSex()) {
					if (vd.getRemarkUserSex().trim().equals("2")) {
						mHolder.mCommentUrl
								.setImageResource(R.drawable.sh_user_sex_woman);
					} else {
						mHolder.mCommentUrl
								.setImageResource(R.drawable.sh_user_sex_man);
					}
				} else {
					mHolder.mCommentUrl
							.setImageResource(R.drawable.sh_user_sex_man);
				}
			}
			// MyApplication
			// .getInstance()
			// .getImageLoader()
			// .displayImage(TextUtil.getUrlMiddle(vd.getRemarkUserImgUrl()),
			// mHolder.mCommentUrl,
			// Options.getRoundOptions(R.drawable.sh_user_sex_man, 10));
			if (vd.getVenueComment() != null
					&& vd.getVenueComment().length() > 0) {
				mHolder.commentLayout.setVisibility(View.GONE);
				mHolder.mComment.setText(vd.getVenuePersonName() + " : "
						+ vd.getVenueComment().trim());
			} else {
				mHolder.commentLayout.setVisibility(View.GONE);
			}

			mHolder.mRating.setRating(vd.getVenueRating());
			if (vd.getVenueHasMetro().equals("1")) {
				mHolder.mMetro.setVisibility(View.GONE);
			} else {
				mHolder.mMetro.setVisibility(View.VISIBLE);

			}
			if (vd.getVenueHasBus().equals("1")) {
				mHolder.mBus.setVisibility(View.GONE);
			} else {
				mHolder.mBus.setVisibility(View.VISIBLE);
			}
			if (vd.getVenueIsReserve()) {
				mHolder.mreserve.setVisibility(View.VISIBLE);
			} else {
				mHolder.mreserve.setVisibility(View.GONE);
			}
		}
		if (arg0 == list.size() - 1) {
			if (isShowView) {
				mHolder.foot.setVisibility(View.VISIBLE);
			}
		} else {
			mHolder.foot.setVisibility(View.GONE);
		}
		return arg1;
	}

	private class ViewHolder {
		private ImageView mUrl;
		private ImageView mMetro;
		private ImageView mBus;
		private ImageView mreserve;
		private ImageView mCommentUrl;
		private TextView mName;
		private TextView mAdress;
		private TextView mComment;
		private RatingBar mRating;
		private TextView foot;
		private LinearLayout commentLayout;
		private TextView venue_content;
	}

	private float index_X, index_Y;

	private void onTabOnclick(View view) {
		if (ButtonUtil.isDelayClick()) {
			switch (view.getId()) {
			case R.id.tab_nebaty_btn:
				onclickindex = 0;
				break;
			case R.id.tab_start_btn:
				onclickindex = 1;
				break;
			case R.id.tab_hot_btn:
				onclickindex = 2;
				break;
			default:
				break;
			}
			if (mOnMyClickListener != null) {
				mOnMyClickListener.onMyClick(view, mmTab_labelHandler);
			}
		}
	}

	@Override
	public boolean onTouch(View arg0, MotionEvent arg1) {
		// TODO Auto-generated method stub
		switch (arg1.getAction()) {
		case MotionEvent.ACTION_DOWN:
			index_X = arg1.getRawX();
			index_Y = arg1.getRawY();
			break;
		case MotionEvent.ACTION_UP:
			if (Math.abs(arg1.getRawY() - index_Y) < 20) {
				if (Math.abs(arg1.getRawX() - index_X) < 20) {
					onTabOnclick(arg0);
				}
			}

			break;

		default:
			break;
		}
		return true;
	}

	private View addNull(int arg0, View convertView) {
		ViewHolder_Frist mHolder = null;
		if (convertView == null) {
			mHolder = new ViewHolder_Frist();
			convertView = View.inflate(mContext,
					R.layout.adapter_event_null_list_item, null);
			mHolder.big_img = (ImageView) convertView
					.findViewById(R.id.big_img);
			mHolder.small_img_left = (ImageView) convertView
					.findViewById(R.id.small_img_left);
			mHolder.small_img_right = (ImageView) convertView
					.findViewById(R.id.small_img_right);
			convertView.setTag(mHolder);
		} else {
			mHolder = (ViewHolder_Frist) convertView.getTag();
		}

		return convertView;
	}

	class ViewHolder_Frist {
		ImageView big_img;
		ImageView small_img_left;
		ImageView small_img_right;
	}

}
