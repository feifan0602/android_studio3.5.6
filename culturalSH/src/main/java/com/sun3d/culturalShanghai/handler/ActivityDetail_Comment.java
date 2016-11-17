package com.sun3d.culturalShanghai.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.R;
import com.sun3d.culturalShanghai.Util.JsonUtil;
import com.sun3d.culturalShanghai.Util.Options;
import com.sun3d.culturalShanghai.Util.ToastUtil;
import com.sun3d.culturalShanghai.activity.EventReserveActivity;
import com.sun3d.culturalShanghai.activity.MoreCommentActivity;
import com.sun3d.culturalShanghai.activity.UserDialogActivity;
import com.sun3d.culturalShanghai.activity.WriteCommentActivity;
import com.sun3d.culturalShanghai.adapter.CommetnListAdapter;
import com.sun3d.culturalShanghai.dialog.DialogTypeUtil;
import com.sun3d.culturalShanghai.http.HttpCode;
import com.sun3d.culturalShanghai.http.HttpRequestCallback;
import com.sun3d.culturalShanghai.http.HttpUrlList;
import com.sun3d.culturalShanghai.http.MyHttpRequest;
import com.sun3d.culturalShanghai.object.CommentInfor;
import com.sun3d.culturalShanghai.object.UserPersionSInfo;
import com.sun3d.culturalShanghai.view.FastBlur;
import com.sun3d.culturalShanghai.view.ScrollViewGridView;
import com.sun3d.culturalShanghai.view.ScrollViewListView;

/**
 * 活动评论
 * 
 * @author yangyoutao
 * 
 */
public class ActivityDetail_Comment implements OnClickListener {
	private LinearLayout content;
	private Context mContext;
	private ScrollViewGridView mLikeGridView;
	private ScrollViewListView mListView;
	private GridViewAdapter mGridViewAdapter;
	private Activity activity;
	private String eventInfoId;
	private String activityType = "2";// 活动评论type
	private List<CommentInfor> myCommentList;
	private CommetnListAdapter mCommentAdapter;
	private List<UserPersionSInfo> listUsers;
	private LinearLayout mCommentLayout;
	private TextView tvMoreComment;
	private boolean isWant;
	private TextView mIsWant;
	private TextView comment_num;
	private TextView zanguo_tv;
	private ImageView fenge;
	private RelativeLayout zanguo_ll;
	private ImageView top_fenge;

	public LinearLayout getContent() {
		return content;
	}

	public ActivityDetail_Comment(Activity activity, Context context,
			String eventInfoId) {
		this.mContext = context;
		this.activity = activity;
		this.eventInfoId = eventInfoId;

		content = (LinearLayout) LayoutInflater.from(mContext).inflate(
				R.layout.activity_commentlayout, null);
		top_fenge = (ImageView) content.findViewById(R.id.top_fenge);
		zanguo_ll = (RelativeLayout) content.findViewById(R.id.zanguo_ll);
		comment_num = (TextView) content.findViewById(R.id.comment_num);
		mLikeGridView = (ScrollViewGridView) content
				.findViewById(R.id.like_gridview);
		mListView = (ScrollViewListView) content
				.findViewById(R.id.comment_listview);
		mCommentLayout = (LinearLayout) content
				.findViewById(R.id.activity_comment_edit);
		content.findViewById(R.id.add).setOnClickListener(this);
		mIsWant = (TextView) content.findViewById(R.id.activity_comment_zan_tv);
		mIsWant.setOnClickListener(this);
		content.findViewById(R.id.activity_comment_tv).setOnClickListener(this);
		zanguo_tv = (TextView) content.findViewById(R.id.zanguo_tv);
		fenge = (ImageView) content.findViewById(R.id.fenge);
		tvMoreComment = (TextView) content.findViewById(R.id.more_comment);
		comment_num.setTypeface(MyApplication.GetTypeFace());
		mIsWant.setTypeface(MyApplication.GetTypeFace());
		zanguo_tv.setTypeface(MyApplication.GetTypeFace());
		tvMoreComment.setTypeface(MyApplication.GetTypeFace());

		tvMoreComment.setOnClickListener(this);
		myCommentList = new ArrayList<CommentInfor>();
		listUsers = new ArrayList<UserPersionSInfo>();
		mGridViewAdapter = new GridViewAdapter(listUsers);
		mCommentAdapter = new CommetnListAdapter(context, myCommentList, false);
		mLikeGridView.setAdapter(mGridViewAdapter);
		mListView.setAdapter(mCommentAdapter);
		mCommentLayout.setVisibility(View.VISIBLE);
		getJoinUsers();
		getBrowseCount();
		getCommtentList();
	}

	public void isWant(boolean isWant) {
		this.isWant = isWant;
		if (isWant) {
			mIsWant.setText("取消");
		} else {
			mIsWant.setText("点赞");
		}
	}

	public void getCommtentData() {
		if (myCommentList != null) {
			myCommentList.clear();
		}
		getCommtentList();
	}

	/**
	 * 点赞实时更新
	 */
	public void getUserData() {
		if (listUsers != null) {
			listUsers.clear();
		}
		getJoinUsers();
		getBrowseCount();
	}

	/**
	 * 获取评论数据
	 */
	private void getCommtentList() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("moldId", eventInfoId);
		params.put("type", activityType);
		params.put("pageIndex", "0");
		params.put("pageNum", "30");

		MyHttpRequest.onHttpPostParams(HttpUrlList.Comment.User_CommentList,
				params, new HttpRequestCallback() {

					@Override
					public void onPostExecute(int statusCode, String resultStr) {
						// TODO Auto-generated method stub
						if (HttpCode.HTTP_Request_Success_CODE == statusCode) {
							myCommentList = JsonUtil
									.getCommentInforFromString(resultStr);
							if (myCommentList.size() > 0) {
								mCommentAdapter.setList(myCommentList);
								mCommentAdapter.notifyDataSetChanged();
								if (myCommentList.size() == 10) {
									tvMoreComment.setVisibility(View.GONE);
								}
								comment_num.setVisibility(View.VISIBLE);
								String text = "共 " + myCommentList.size()
										+ " 条 评 论";
								fenge.setVisibility(View.VISIBLE);
								comment_num.setText(MyApplication
										.getTextColor(text));
							} else {
								fenge.setVisibility(View.GONE);
								comment_num.setVisibility(View.GONE);
								tvMoreComment.setVisibility(View.GONE);
							}

						} else {
							// ToastUtil.showToast(resultStr);
						}
					}
				});
	}

	/**
	 * 浏览数
	 */

	private void getBrowseCount() {
		//
		Map<String, String> params = new HashMap<String, String>();
		params.put("activityId", eventInfoId);
		MyHttpRequest.onHttpPostParams(
				HttpUrlList.EventUrl.WFF_APPCMSACTIVITYBROWSECOUNT, params,
				new HttpRequestCallback() {

					@Override
					public void onPostExecute(int statusCode, String resultStr) {
						// TODO Auto-generated method stub
						if (statusCode == HttpCode.HTTP_Request_Success_CODE) {
							try {
								JSONObject json = new JSONObject(resultStr);
								int data = json.getInt("data");

								if (listUsers.size() == 0) {
									zanguo_ll.setVisibility(View.GONE);
									zanguo_tv.setVisibility(View.GONE);
									fenge.setVisibility(View.GONE);
								} else {
									zanguo_tv.setVisibility(View.VISIBLE);
									// fenge.setVisibility(View.VISIBLE);
								}
								String text = null;
								if (data > 50) {
									text = "共 " + UserPersionSInfo.totalNum
											+ " 人 赞 过 " + "," + data
											+ " 人 浏 览 过";
								} else {
									text = "共 " + UserPersionSInfo.totalNum
											+ " 人 赞 过 ";
								}

								zanguo_tv.setText(MyApplication
										.getTextColor(text));

							} catch (Exception e) {
								e.printStackTrace();
							}

						} else {
							// ToastUtil.showToast(resultStr);
						}

					}

				});
	}

	/**
	 * 获取报名用户 也就是点赞数
	 */
	private void getJoinUsers() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("activityId", eventInfoId);
		params.put("pageIndex", "0");
		params.put("pageNum", "30");
		MyHttpRequest.onHttpPostParams(HttpUrlList.EventUrl.WANTTO_USERS_URL,
				params, new HttpRequestCallback() {

					@Override
					public void onPostExecute(int statusCode, String resultStr) {
						// TODO Auto-generated method stub
						if (statusCode == HttpCode.HTTP_Request_Success_CODE) {
							listUsers = JsonUtil.getJoinUserList(resultStr);
							if (UserPersionSInfo.totalNum.equals("0")) {
								zanguo_ll.setVisibility(View.GONE);
								zanguo_tv.setVisibility(View.GONE);
								fenge.setVisibility(View.GONE);
								top_fenge.setVisibility(View.GONE);
								mLikeGridView.setVisibility(View.GONE);
							} else {
								top_fenge.setVisibility(View.VISIBLE);
								zanguo_ll.setVisibility(View.VISIBLE);
								zanguo_tv.setVisibility(View.VISIBLE);
								mLikeGridView.setVisibility(View.VISIBLE);
								String text = "共 " + UserPersionSInfo.totalNum
										+ " 人 赞 过 ";
								zanguo_tv.setText(MyApplication
										.getTextColor(text));
							}
							mGridViewAdapter.setUserList(listUsers);
							mGridViewAdapter.notifyDataSetChanged();
						} else {
							// ToastUtil.showToast(resultStr);
						}

					}

				});
	}

	/**
	 * 我想报名
	 */
	private void goToJoin() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("activityId", eventInfoId);
		params.put("userId", MyApplication.loginUserInfor.getUserId());
		MyHttpRequest.onHttpPostParams(HttpUrlList.EventUrl.WANTTO_URL, params,
				new HttpRequestCallback() {

					@Override
					public void onPostExecute(int statusCode, String resultStr) {
						// TODO Auto-generated method stub
						if (statusCode == HttpCode.HTTP_Request_Success_CODE) {
							int code = JsonUtil.getJsonStatus(resultStr);
							if (code == HttpCode.serverCode.DATA_Success_CODE) {
								// ToastUtil.showToast("提交成功");
								mIsWant.setText("取消");
								isWant = !isWant;
								if (listUsers != null) {
									listUsers.clear();
								}
								getJoinUsers();
							} else {
								ToastUtil.showToast("已赞");
							}

						} else {
							// ToastUtil.showToast(resultStr);
						}

					}

				});
	}

	private void quxiaoJoin() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("activityId", eventInfoId);
		params.put("userId", MyApplication.loginUserInfor.getUserId());
		//
		MyHttpRequest.onHttpPostParams(HttpUrlList.EventUrl.WANTNOT_URL,
				params, new HttpRequestCallback() {

					@Override
					public void onPostExecute(int statusCode, String resultStr) {
						// TODO Auto-generated method stub
						if (statusCode == HttpCode.HTTP_Request_Success_CODE) {
							int code = JsonUtil.getJsonStatus(resultStr);
							if (code == 0) {
								mIsWant.setText("点赞");
								isWant = !isWant;
								if (listUsers != null) {
									listUsers.clear();
								}
								getJoinUsers();
							}
						}
					}
				});
	}

	class GridViewAdapter extends BaseAdapter {
		private List<UserPersionSInfo> listUsers;

		public GridViewAdapter(List<UserPersionSInfo> listUsers) {
			// TODO Auto-generated constructor stub
			this.listUsers = listUsers;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return this.listUsers.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return this.listUsers.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		public void setUserList(List<UserPersionSInfo> listUsers) {
			this.listUsers = listUsers;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			// TODO Auto-generated method stub
			ViewHolder mHolder = null;
			if (arg1 == null) {
				mHolder = new ViewHolder();
				arg1 = View.inflate(mContext, R.layout.likeitemlayout, null);
				mHolder.show = (TextView) arg1.findViewById(R.id.text);
				mHolder.img_url = (ImageView) arg1.findViewById(R.id.img);
				arg1.setTag(mHolder);
			} else {
				mHolder = (ViewHolder) arg1.getTag();
			}
			UserPersionSInfo info = this.listUsers.get(arg0);
			if ("女".equals(info.getSex())) {
				MyApplication
						.getInstance()
						.getImageLoader()
						.displayImage(
								info.getHeadUrl(),
								mHolder.img_url,
								Options.getRoundOptions(
										R.drawable.sh_user_sex_woman, 5));
			} else {
				MyApplication
						.getInstance()
						.getImageLoader()
						.displayImage(
								info.getHeadUrl(),
								mHolder.img_url,
								Options.getRoundOptions(
										R.drawable.sh_user_sex_man, 5));
			}
			Log.i("ceshi", "看看数据" + info.getHeadUrl());
			if (arg0 == 23) {
				mHolder.show.setVisibility(View.VISIBLE);
				mHolder.img_url.setVisibility(View.GONE);
			} else {
				mHolder.show.setVisibility(View.GONE);
				mHolder.img_url.setVisibility(View.VISIBLE);
			}

			return arg1;
		}

		class ViewHolder {
			private TextView show;
			private ImageView img_url;

		}
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		Intent intent = null;
		switch (arg0.getId()) {
		case R.id.add:
			if (mCommentLayout.getVisibility() == View.VISIBLE) {
				mCommentLayout.setVisibility(View.GONE);
			} else {
				mCommentLayout.setVisibility(View.VISIBLE);
			}
			break;
		case R.id.activity_comment_tv:
			mCommentLayout.setVisibility(View.GONE);
			if (MyApplication.UserIsLogin) {
				intent = new Intent(mContext, WriteCommentActivity.class);
				intent.putExtra("Id", eventInfoId);
				intent.putExtra("type", activityType);
				activity.startActivityForResult(intent,
						HttpCode.ServerCode_Comment.CODE_BACK);
			} else {
				FastBlur.getScreen((Activity) mContext);
				intent = new Intent(mContext, UserDialogActivity.class);
				intent.putExtra(
						DialogTypeUtil.DialogType,
						DialogTypeUtil.ThirdPartyDialogType.THIRDPARTY_FAST_LOGIN);
				mContext.startActivity(intent);
			}
			break;
		case R.id.activity_comment_zan_tv:
			mCommentLayout.setVisibility(View.GONE);
			if (MyApplication.UserIsLogin) {
				if (isWant) {
					quxiaoJoin();
				} else {
					goToJoin();
				}
			} else {
				FastBlur.getScreen((Activity) mContext);
				intent = new Intent(mContext, UserDialogActivity.class);
				intent.putExtra(
						DialogTypeUtil.DialogType,
						DialogTypeUtil.ThirdPartyDialogType.THIRDPARTY_FAST_LOGIN);
				mContext.startActivity(intent);
			}

			break;
		case R.id.more_comment:
			intent = new Intent(mContext, MoreCommentActivity.class);
			intent.putExtra("Id", eventInfoId);
			intent.putExtra("type", activityType);
			mContext.startActivity(intent);
			break;
		default:
			break;
		}
	}
}
