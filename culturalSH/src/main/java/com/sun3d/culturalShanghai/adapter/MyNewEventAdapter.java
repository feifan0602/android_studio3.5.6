package com.sun3d.culturalShanghai.adapter;

import java.util.List;

import net.tsz.afinal.core.Arrays;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.R;
import com.sun3d.culturalShanghai.Util.Options;
import com.sun3d.culturalShanghai.Util.ToastUtil;
import com.sun3d.culturalShanghai.activity.ImageOriginalActivity;
import com.sun3d.culturalShanghai.activity.MoreCommentActivity;
import com.sun3d.culturalShanghai.dialog.LoadingDialog;
import com.sun3d.culturalShanghai.fragment.NewEventFragment;
import com.sun3d.culturalShanghai.object.MyActivityBookInfo;

public class MyNewEventAdapter extends BaseAdapter {
	private Context mContext;
	public List<MyActivityBookInfo> list;
	private LoadingDialog mLoadingDialog;

	public MyNewEventAdapter(Context mContext, List<MyActivityBookInfo> list) {
		// TODO Auto-generated constructor stub
		this.mContext = mContext;
		this.list = list;
		mLoadingDialog = new LoadingDialog(mContext);
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

	public void setData(List<MyActivityBookInfo> list) {
		this.list = list;
		this.notifyDataSetChanged();
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHolder mHolder = null;
		if (arg1 == null) {
			arg1 = View.inflate(mContext, R.layout.fragment_new_event_item, null);
			mHolder = new ViewHolder();
			mHolder.mCode = (TextView) arg1.findViewById(R.id.my_event_code);
			mHolder.mCost = (TextView) arg1.findViewById(R.id.my_event_cost);
			mHolder.mGroup = (TextView) arg1.findViewById(R.id.my_event_num);
			mHolder.mLocation = (TextView) arg1.findViewById(R.id.my_event_location);
			mHolder.mNumbar = (TextView) arg1.findViewById(R.id.my_event_numbar);
			mHolder.mSession = (TextView) arg1.findViewById(R.id.my_event_session);
			mHolder.mTime = (TextView) arg1.findViewById(R.id.my_event_time);
			mHolder.mTitle = (TextView) arg1.findViewById(R.id.my_event_title);
			mHolder.mImage = (ImageView) arg1.findViewById(R.id.my_event_img);
			mHolder.mImage_EWM = (ImageView) arg1.findViewById(R.id.erweima_img);
			mHolder.mNum = (TextView) arg1.findViewById(R.id.comment_num);
			mHolder.cancel = (TextView) arg1.findViewById(R.id.cancel);
			mHolder.comment = (TextView) arg1.findViewById(R.id.look_comment);
			mHolder.seat = (GridView) arg1.findViewById(R.id.my_event_seat);
			mHolder.seatlayout = (RelativeLayout) arg1.findViewById(R.id.my_event_seatlayout);
			arg1.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) arg1.getTag();
		}
		mHolder.seatlayout.setVisibility(View.GONE);
		final MyActivityBookInfo info = list.get(arg0);
		mHolder.mCode.setText(info.getOrderNumber());// 订单编号
		if (info.getOrderPrice() <= 0) {
			mHolder.mCost.setText("免费");// 费用
		} else {
			mHolder.mCost.setText("" + info.getOrderPrice());// 费用
		}

		mHolder.mGroup.setText(info.getOrderVotes());// 团体
		mHolder.mLocation.setText(info.getActivityAddress());// 地址
		mHolder.mNumbar.setText(info.getOrderValidateCode());// 人数
		mHolder.mSession.setText(info.getActivityEventDateTime());// 场次
		mHolder.mTime.setText(info.getOrderTime());// 订单时间
		mHolder.mTitle.setText(info.getActivityName());// 活动名称
		mHolder.mNum.setText("共有" + info.getCommentCount() + "条评论");
		info.setIndex(arg0);
		final MyActivityBookInfo mInfo = info;
		MyApplication.getInstance().getImageLoader()
				.displayImage(info.getActivityIconUrl(), mHolder.mImage, Options.getListOptions());
		MyApplication
				.getInstance()
				.getImageLoader()
				.displayImage(info.getActivityQrcodeUrl(), mHolder.mImage_EWM,
						Options.getListOptions(R.drawable.sh_icon_error_loading));
		// 点击二维码放大
		mHolder.mImage_EWM.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (info.getActivityQrcodeUrl().length() <= 0) {
					ToastUtil.showToast("默认头像无法放大显示！");
					return;
				}
				Intent intent = new Intent();
				intent.setClass(mContext, ImageOriginalActivity.class);
				intent.putExtra("select_imgs", info.getActivityQrcodeUrl());
				intent.putExtra("id", 0);
				mContext.startActivity(intent);
			}
		});
		// 取消活动
		mHolder.cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// 取消
				Log.i("MyNewEventAdapter", mInfo.toString());
				int isBoo = Arrays.toString(mInfo.getoSummary()).indexOf("true");

				if (isBoo != -1) {
					NewEventFragment.getInstance().goToDialog(mInfo);
				} else {
					ToastUtil.showToast("请选择要取消的座位");
				}
			}
		});
		mHolder.comment.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(mContext, MoreCommentActivity.class);
				intent.putExtra("Id", info.getActivityId());
				intent.putExtra("type", "2");
				mContext.startActivity(intent);
			}
		});

		if (info.getIsSeatOnline() && info.getActivitySeats() != null
				&& info.getActivitySeats().trim().length() > 0) {
			mHolder.seatlayout.setVisibility(View.VISIBLE);
//			MyNewSeatAdapter seat = new MyNewSeatAdapter(mContext, list, arg0);
			if (info.getoSummary().length > 0) {
//				mHolder.seat.setAdapter(seat);
			}
		} else {
			mHolder.seatlayout.setVisibility(View.GONE);
		}
		return arg1;
	}

	class ViewHolder {
		private TextView mTitle;
		private TextView mTime;
		private TextView mSession;
		private TextView mNumbar;
		private TextView mLocation;
		private TextView mGroup;
		private TextView mCost;
		private TextView mCode;
		private ImageView mImage;
		private ImageView mImage_EWM;
		private TextView mNum;
		private TextView cancel;
		private TextView comment;
		private GridView seat;
		private RelativeLayout seatlayout;
	}
}
