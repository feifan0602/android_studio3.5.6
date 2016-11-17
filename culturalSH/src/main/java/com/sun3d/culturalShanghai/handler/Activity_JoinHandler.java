package com.sun3d.culturalShanghai.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.R;
import com.sun3d.culturalShanghai.Util.JsonUtil;
import com.sun3d.culturalShanghai.Util.Options;
import com.sun3d.culturalShanghai.Util.TextUtil;
import com.sun3d.culturalShanghai.Util.ToastUtil;
import com.sun3d.culturalShanghai.activity.ImageOriginalActivity;
import com.sun3d.culturalShanghai.activity.ScanUsersActivity;
import com.sun3d.culturalShanghai.activity.UserDialogActivity;
import com.sun3d.culturalShanghai.dialog.DialogTypeUtil;
import com.sun3d.culturalShanghai.http.HttpCode;
import com.sun3d.culturalShanghai.http.HttpRequestCallback;
import com.sun3d.culturalShanghai.http.HttpUrlList;
import com.sun3d.culturalShanghai.http.MyHttpRequest;
import com.sun3d.culturalShanghai.object.UserPersionSInfo;
import com.sun3d.culturalShanghai.view.FastBlur;

public class Activity_JoinHandler implements OnClickListener {
	private TextView Join_num;
	private TextView Join_WantTo;
	private GridView mGridView;
	private ImageView forwad_img;
	private Context mContext;
	private String activityId;
	private List<UserPersionSInfo> listUsers = new ArrayList<UserPersionSInfo>();
	private GridViewAdapter mGridViewAdapter;
	private int dufaltNum = 6;
	private Boolean isjoin = false;
	private int total_Num = 0;
	private RelativeLayout join_wanTo_bg;

	public Activity_JoinHandler(Context context, LinearLayout contancrl, String activityId) {
		this.mContext = context;
		this.activityId = activityId;
		Join_num = (TextView) contancrl.findViewById(R.id.join_num);
		Join_WantTo = (TextView) contancrl.findViewById(R.id.join_add);
		join_wanTo_bg = (RelativeLayout) contancrl.findViewById(R.id.join_add_layout);
		mGridView = (GridView) contancrl.findViewById(R.id.join_user_grid);
		mGridView.setFocusable(false);
		forwad_img = (ImageView) contancrl.findViewById(R.id.join_user_img);
		forwad_img.setOnClickListener(this);
		Join_WantTo.setOnClickListener(this);
		mGridViewAdapter = new GridViewAdapter();
		mGridView.setAdapter(mGridViewAdapter);
		mGridView.setNumColumns(dufaltNum);
		if (listUsers.size() < dufaltNum) {
			forwad_img.setVisibility(View.GONE);
		} else {
			forwad_img.setVisibility(View.VISIBLE);
		}

		mGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				if (listUsers.get(arg2).getHeadUrl().length() <= 0) {
					ToastUtil.showToast("默认头像无法放大显示！");
					return;
				}
				Intent intent = new Intent(mContext, ImageOriginalActivity.class);
				intent.putExtra("select_imgs", listUsers.get(arg2).getHeadUrl());
				intent.putExtra("id", 0);
				mContext.startActivity(intent);
			}
		});
	}

	class GridViewAdapter extends BaseAdapter {
		private LayoutInflater mInflater;

		public GridViewAdapter() {
			mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);// LayoutInflater.from(mContext);
		}

		@Override
		public int getCount() {
			return listUsers.size();
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.head_layout, null);
				holder.mImage = (ImageView) convertView.findViewById(R.id.personal_head);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			if ("1".equals(MyApplication.loginUserInfor.getUserSex())) {
				MyApplication.getInstance().getImageLoader()
						.displayImage(listUsers.get(position).getHeadUrl(), holder.mImage, Options.getRoundOptions(R.drawable.sh_user_sex_man, 10));
			} else {
				MyApplication.getInstance().getImageLoader()
						.displayImage(listUsers.get(position).getHeadUrl(), holder.mImage, Options.getRoundOptions(R.drawable.sh_user_sex_woman, 10));
			}
			return convertView;
		}

		private class ViewHolder {
			private ImageView mImage;
		}

	}

	/**
	 * 设置报名数据
	 */
	public void setUserListDate(List<UserPersionSInfo> listUsers) {
		this.listUsers = listUsers;
		total_Num = Integer.parseInt(UserPersionSInfo.totalNum);
		Join_num.setText(total_Num + "");
		mGridView.setNumColumns(dufaltNum);
		if (listUsers.size() < dufaltNum) {
			// forwad_img.setVisibility(View.GONE);
		} else {
			forwad_img.setVisibility(View.VISIBLE);
		}
		forwad_img.setVisibility(View.VISIBLE);
		mGridViewAdapter.notifyDataSetChanged();
	}

	public void setIsJoin(Boolean isjoin) {
		this.isjoin = isjoin;
	}

	public void setWantToGray() {
		Join_WantTo.setTextColor(mContext.getResources().getColor(R.color.white_color));
		setDrawaleRight(Join_WantTo, R.drawable.sh_activity_like_n);
		join_wanTo_bg.setBackgroundResource(R.drawable.shape_activity_want_rcet_p);
	}

	public void setWantToRed() {
		Join_WantTo.setTextColor(mContext.getResources().getColor(R.color.red_color));
		setDrawaleRight(Join_WantTo, R.drawable.sh_activity_like_p);
		join_wanTo_bg.setBackgroundResource(R.drawable.shape_activity_want_rcet);
	}

	/**
	 * 设置左边图片
	 */
	private void setDrawaleRight(TextView view, int id) {
		Drawable drawable = mContext.getResources().getDrawable(id);
		drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); // 设置边界
		view.setCompoundDrawables(drawable, null, null, null);//
	}

	private void addUsers() {
		UserPersionSInfo mUserPersionSInfo = new UserPersionSInfo();
		mUserPersionSInfo.setBithryDay(TextUtil.getDateBriahty(MyApplication.loginUserInfor.getUserBirth()));
		mUserPersionSInfo.setHeadUrl(MyApplication.loginUserInfor.getUserHeadImgUrl());
		mUserPersionSInfo.setName(MyApplication.loginUserInfor.getUserNickName());
		if ("1".equals(MyApplication.loginUserInfor.getUserSex())) {
			mUserPersionSInfo.setSex("男");
		} else {
			mUserPersionSInfo.setSex("女");
		}
		this.listUsers.add(0, mUserPersionSInfo);
		if (this.listUsers.size() > dufaltNum) {
			this.listUsers.remove(dufaltNum);
		}
		mGridViewAdapter.notifyDataSetChanged();
	}

	/**
	 * 我想报名
	 */
	private void goToJoin() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("activityId", this.activityId);
		params.put("userId", MyApplication.loginUserInfor.getUserId());
		MyHttpRequest.onHttpPostParams(HttpUrlList.EventUrl.WANTTO_URL, params, new HttpRequestCallback() {

			@Override
			public void onPostExecute(int statusCode, String resultStr) {
				// TODO Auto-generated method stub
				if (statusCode == HttpCode.HTTP_Request_Success_CODE) {
					int code = JsonUtil.getJsonStatus(resultStr);
					if (code == HttpCode.serverCode.DATA_Success_CODE) {
//						ToastUtil.showToast("提交成功");
						setWantToGray();
						total_Num += 1;
						Join_num.setText(total_Num + "");
						addUsers();
						isjoin = true;
					} else {
						ToastUtil.showToast(JsonUtil.JsonMSG);
					}

				} else {
					ToastUtil.showToast(resultStr);
				}

			}

		});
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.join_add:// 我想参加
			if (MyApplication.UserIsLogin) {
				if (!isjoin) {
					goToJoin();
				} else {
					ToastUtil.showToast("您已经点过了哟");
					break;
				}

			} else {
				FastBlur.getScreen((Activity) mContext);
				Intent intent = new Intent(mContext, UserDialogActivity.class);
				intent.putExtra(DialogTypeUtil.DialogType, DialogTypeUtil.ThirdPartyDialogType.THIRDPARTY_FAST_LOGIN);
				mContext.startActivity(intent);
			}

			break;
		case R.id.join_user_img:// 参看所有用户头像
			Intent intent = new Intent(mContext, ScanUsersActivity.class);
			intent.putExtra("activityId", this.activityId);
			mContext.startActivity(intent);
			break;
		default:
			break;
		}
	}

}