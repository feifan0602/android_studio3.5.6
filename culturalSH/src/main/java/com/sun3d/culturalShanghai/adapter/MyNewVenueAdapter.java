package com.sun3d.culturalShanghai.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.R;
import com.sun3d.culturalShanghai.Util.Options;
import com.sun3d.culturalShanghai.fragment.NewVenueFragment;
import com.sun3d.culturalShanghai.object.MyVenueInfo;

public class MyNewVenueAdapter extends BaseAdapter {
	private Context mContext;
	public List<MyVenueInfo> list;

	public MyNewVenueAdapter(Context mContext, List<MyVenueInfo> list) {
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

	public void setList(List<MyVenueInfo> list) {
		this.list = list;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHolder mHolder = null;
		if (arg1 == null) {
			arg1 = View.inflate(mContext, R.layout.fragment_new_venue_item, null);
			mHolder = new ViewHolder();
			mHolder.mClass = (TextView) arg1.findViewById(R.id.my_venue_class);
			mHolder.mCode = (TextView) arg1.findViewById(R.id.my_venue_code);
			mHolder.mCost = (TextView) arg1.findViewById(R.id.my_venue_cost);
			mHolder.mGroup = (TextView) arg1.findViewById(R.id.my_venue_group);
			mHolder.mLocation = (TextView) arg1.findViewById(R.id.my_venue_location);
			mHolder.mNumbar = (TextView) arg1.findViewById(R.id.my_venue_numbar);
			mHolder.mSession = (TextView) arg1.findViewById(R.id.my_venue_session);
			mHolder.mTime = (TextView) arg1.findViewById(R.id.my_venue_time);
			mHolder.mTitle = (TextView) arg1.findViewById(R.id.my_venue_title);
			mHolder.mImage_EWM = (ImageView) arg1.findViewById(R.id.erweima_img);
			mHolder.cancel = (TextView) arg1.findViewById(R.id.cancel);
			mHolder.mImage = (ImageView) arg1.findViewById(R.id.my_venue_img);
			arg1.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) arg1.getTag();
		}
		MyVenueInfo info = list.get(arg0);
		mHolder.mClass.setText(info.getRoomName());// 活动室名称
		mHolder.mCode.setText(info.getRoomOrderNo());// 订单号
		mHolder.mCost.setText(info.getRoomFee());// 费用
		mHolder.mGroup.setText(info.getTuserTeamName());// 团体
		mHolder.mLocation.setText(info.getVenueAddress());// 场馆地址
		mHolder.mNumbar.setText(info.getValidCode());// 验证码
		mHolder.mSession.setText(info.getCurDate() + "  " + info.getOpenPeriod());// 场次
		mHolder.mTime.setText(info.getRoomOrderCreateTime());// 订单时间
		mHolder.mTitle.setText(info.getVenueName());// 场馆名字
		info.setIndex(arg0);
		// 二维码
		MyApplication
				.getInstance()
				.getImageLoader()
				.displayImage(info.getRoomQrcodeUrl(), mHolder.mImage_EWM,
						Options.getListOptions(R.drawable.sh_icon_error_loading));

		MyApplication.getInstance().getImageLoader()
				.displayImage(info.getRoomRicUrl(), mHolder.mImage, Options.getListOptions());
		final MyVenueInfo mInfo = info;
		mHolder.cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				NewVenueFragment.getInstance().goToDialog(mInfo);
			}
		});

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
		private TextView mClass;
		private TextView cancel;
		private ImageView mImage_EWM;
		private ImageView mImage;
	}
}
