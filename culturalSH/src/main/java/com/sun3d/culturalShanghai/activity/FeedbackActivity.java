package com.sun3d.culturalShanghai.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Looper;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.R;
import com.sun3d.culturalShanghai.Util.AppConfigUtil;
import com.sun3d.culturalShanghai.Util.JsonUtil;
import com.sun3d.culturalShanghai.Util.PicImgCompressUtil;
import com.sun3d.culturalShanghai.Util.TextUtil;
import com.sun3d.culturalShanghai.Util.ToastUtil;
import com.sun3d.culturalShanghai.Util.UploadImgUtil;
import com.sun3d.culturalShanghai.Util.UploadImgUtil.OnUploadProcessListener;
import com.sun3d.culturalShanghai.dialog.LoadingDialog;
import com.sun3d.culturalShanghai.handler.MoreFilesUploadHandler;
import com.sun3d.culturalShanghai.http.HttpCode;
import com.sun3d.culturalShanghai.http.HttpRequestCallback;
import com.sun3d.culturalShanghai.http.HttpUrlList;
import com.sun3d.culturalShanghai.http.MyHttpRequest;
import com.sun3d.culturalShanghai.image.ImageLoader;
import com.sun3d.culturalShanghai.image.ImageLoader.Type;
import com.sun3d.culturalShanghai.object.CommentImgeInfo;
import com.sun3d.culturalShanghai.view.FastBlur;
import com.umeng.analytics.MobclickAgent;

public class FeedbackActivity extends Activity implements OnClickListener,
		OnUploadProcessListener, OnItemClickListener {
	private Context mContext;
	private RelativeLayout mTitle;
	/**
	 * true 表示可以返回 false 表示不能返回 图片
	 */
	private Boolean back_img_bool = true;
	private EditText inputEd;
	private TextView inputEd_num;
	private int toalNum = 120;// 设置最大输入字数
	private TextView mSend;
	private LoadingDialog mLoadingDialog;
	private RelativeLayout mSelect;

	private PopupWindow popFaceType;
	private View layoutFaceType;
	private ListView menulistFaceType;
	private List<Map<String, String>> listFaceType;
	private int uploadProcess = 0;
	private String typeId;
	private String feedimgurl = "";
	private final String mPageName = "FeedbackActivity";
	private GridView mGridView;
	private List<CommentImgeInfo> imglist = new ArrayList<CommentImgeInfo>();
	private GridViewAdapter mGridViewAdapter;
	private String selectImgs = "";
	private String modeType = "3";
	private ImageView left_iv;
	private TextView middle_tv;
	private TextView text;
	private TextView title_tv0, feedback_confirm;

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onPageStart(mPageName);
		MobclickAgent.onResume(mContext);
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(mPageName);
		MobclickAgent.onPause(mContext);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mContext = this;
		MyApplication.getInstance().addActivitys(this);
		setContentView(R.layout.activity_feedback);
		mLoadingDialog = new LoadingDialog(mContext);
		initView();
	}

	private void initView() {
		left_iv = (ImageView) findViewById(R.id.left_iv);
		left_iv.setOnClickListener(this);
		middle_tv = (TextView) findViewById(R.id.middle_tv);
		middle_tv.setTypeface(MyApplication.GetTypeFace());
		middle_tv.setText("帮助与反馈");

		// mTitle = (RelativeLayout) findViewById(R.id.title);
		// mTitle.findViewById(R.id.title_left).setOnClickListener(this);
		// TextView title = (TextView) mTitle.findViewById(R.id.title_content);
		// title.setVisibility(View.VISIBLE);
		// title.setText(mContext.getResources().getString(R.string.feedback_title));
		// mTitle.findViewById(R.id.title_right).setVisibility(View.GONE);
		// mSend = (TextView) mTitle.findViewById(R.id.title_send);
		// mSend.setVisibility(View.GONE);
		// mSend.setOnClickListener(this);
		mGridView = (GridView) findViewById(R.id.gridview);
		mGridView.setNumColumns(3);
		CommentImgeInfo initCI = new CommentImgeInfo();
		initCI.setIsImagePic(false);
		imglist.add(initCI);
		mGridViewAdapter = new GridViewAdapter();
		mGridView.setAdapter(mGridViewAdapter);
		mGridView.setOnItemClickListener(this);
		mSelect = (RelativeLayout) findViewById(R.id.feedback_select);
		inputEd = (EditText) findViewById(R.id.feedback_content);
		inputEd.setTypeface(MyApplication.GetTypeFace());
		listFaceType = new ArrayList<Map<String, String>>();
		title_tv0 = (TextView) findViewById(R.id.title_tv0);
		title_tv0.setTypeface(MyApplication.GetTypeFace());
		feedback_confirm = (TextView) findViewById(R.id.feedback_confirm);
		feedback_confirm.setTypeface(MyApplication.GetTypeFace());
		feedback_confirm.setOnClickListener(this);
		mSelect.setOnClickListener(this);

		inputEd_num = (TextView) findViewById(R.id.feedback_content_num);
		inputEd_num.setText("剩余" + toalNum + "字");
		inputEd.setFilters(new InputFilter[] { new InputFilter.LengthFilter(
				toalNum) });
		inputEd.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence charsequence, int i, int j,
					int k) {
				// TODO Auto-generated method stub
				int nowNum = toalNum
						- (int) TextUtil.calculateWeiboLength(charsequence);
				inputEd_num.setText("剩余" + nowNum + "字");
				if (nowNum <= 0) {
					ToastUtil.showToast("您已超过限制的字数！");
				}

			}

			@Override
			public void beforeTextChanged(CharSequence charsequence, int i,
					int j, int k) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable editable) {
				// TODO Auto-generated method stub

			}
		});
		getFeedBackType();
		UploadImgUtil.getInstance().setOnUploadProcessListener(this);
	}

	class GridViewAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return imglist.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(final int arg0, View arg1, ViewGroup arg2) {
			// TODO Auto-generated method stub
			if (arg0 == imglist.size() - 1 && arg0 < 9) {
				arg1 = LayoutInflater.from(mContext).inflate(
						R.layout.comment_imgitem_last, null);
				ImageView img = (ImageView) arg1.findViewById(R.id.imageView1);
			} else {
				arg1 = LayoutInflater.from(mContext).inflate(
						R.layout.comment_imgitem, null);
				ImageView img = (ImageView) arg1.findViewById(R.id.imageView1);
				ImageView img_2 = (ImageView) arg1
						.findViewById(R.id.imageView2);
				ImageView delete = (ImageView) arg1.findViewById(R.id.delete);
				if (imglist.size() == 1) {
					delete.setVisibility(View.GONE);
					img.setVisibility(View.GONE);
					img_2.setVisibility(View.VISIBLE);
					// img.setImageResource(R.drawable.wff_zhaoxiangji);
				} else if (arg0 >= 9) {
					delete.setVisibility(View.GONE);
					img.setVisibility(View.VISIBLE);
					img_2.setVisibility(View.GONE);
				} else {
					delete.setVisibility(View.VISIBLE);
					img.setVisibility(View.VISIBLE);
					img_2.setVisibility(View.GONE);
				}

				delete.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (imglist.size() != 0) {
							imglist.remove(arg0);
							notifyDataSetChanged();
						}
					}
				});

				CommentImgeInfo initCI = imglist.get(arg0);
				if (initCI.getIsImagePic()) {
					img.setScaleType(ScaleType.CENTER_CROP);
					if (ImageLoader.getInstance() == null) {
						ImageLoader.getInstance(3, Type.LIFO);
					}
					Bitmap bit = ImageLoader.getBitmapFromLruCache(initCI
							.getLocalhostPath());
					if (bit == null) {
						bit = PicImgCompressUtil.get100Bitmap(initCI
								.getLocalhostPath());
					}
					img.setImageBitmap(bit);
				}
			}
			return arg1;
		}
	}

	private void getFeedBackType() {
		MyHttpRequest.onHttpPostParams(HttpUrlList.Feedback.FEEDBACK_TYPE_URL,
				null, new HttpRequestCallback() {

					@Override
					public void onPostExecute(int statusCode, String resultStr) {
						// TODO Auto-generated method stub
						mLoadingDialog.cancelDialog();
						if (HttpCode.HTTP_Request_Success_CODE == statusCode) {
							listFaceType = JsonUtil
									.getFeedBackTypeList(resultStr);
							if (listFaceType == null
									|| listFaceType.size() <= 0) {
								ToastUtil.showToast("没有反馈类别数据。");
							}
						} else {
							ToastUtil.showToast(resultStr);
						}

					}
				});
	}

	/**
	 * 发送反馈信息
	 */
	private void onSend() {
		feedimgurl = "";
		if (imglist != null && imglist.size() > 0) {
			for (CommentImgeInfo cii : imglist) {
				if (cii.getIsImagePic()) {
					feedimgurl = feedimgurl + ";" + cii.getServerUrl();
				}
			}
			if (feedimgurl.trim().length() > 0) {
				feedimgurl = feedimgurl.substring(1, feedimgurl.length());
			}
		}

		Map<String, String> params = new HashMap<String, String>();
		params.put(HttpUrlList.HTTP_USER_ID,
				MyApplication.loginUserInfor.getUserId());
		params.put(HttpUrlList.Feedback.CONTENT, inputEd.getText().toString());
		// params.put("feedType", typeId);
		params.put("feedImgUrl", feedimgurl);
		MyHttpRequest.onHttpPostParams(HttpUrlList.Feedback.FEEDBACK_URL,
				params, new HttpRequestCallback() {
					@Override
					public void onPostExecute(int statusCode, String resultStr) {
						// TODO Auto-generated method stub
						if (JsonUtil.getJsonStatus(resultStr) == HttpCode.serverCode.DATA_Success_CODE) {
							ToastUtil.showToast("谢谢您的宝贵意见!");
							finish();
						} else {
							ToastUtil.showToast("网络异常");
						}
						mLoadingDialog.cancelDialog();
					}
				});
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		Intent intent = null;
		switch (arg0.getId()) {
		case R.id.title_left:
			finish();
			break;
		case R.id.title_send:
			onSend();
			break;
		case R.id.left_iv:
			finish();
			break;
		case R.id.feedback_confirm:
			if (TextUtils.isEmpty(inputEd.getText())) {
				ToastUtil.showToast("请输入您的宝贵意见");
				mLoadingDialog.cancelDialog();
				return;
			}
			/*
			 * if (TextUtils.isEmpty(typeId)) { ToastUtil.showToast("请选择反馈类型");
			 * mLoadingDialog.cancelDialog(); return; }
			 */
			mLoadingDialog.startDialog("");
			uploadProcess = 0;
			upLoadimg();
			break;
		case R.id.feedback_select:
			/*
			 * if (listFaceType.size() > 0) { popWin(); } else {
			 * mLoadingDialog.startDialog(""); getFeedBackType(); }
			 */
			
			intent = new Intent(mContext, AboutInfoActivity.class);
			intent.putExtra(HttpUrlList.WebUrl.WEB_URL, "1");
			startActivity(intent);
			break;

		default:
			break;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onActivityResult(int, int,
	 * android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case AppConfigUtil.RESULT_LOCAT_CODE:// 拍照返回 key:path
			String path = data.getStringExtra("path");
			Log.d("path", path);
			if (path != null) {
				Log.d("path1", path);
				Bitmap btm = BitmapFactory.decodeFile(path);
				if (btm != null) {
					Log.d("path2", path);
				}
			}
			break;
		case 30:

			if (selectImgs.length() != 0 && back_img_bool == false) {
				selectImgs = "";
			}
			selectImgs = data.getStringExtra("select_imgs");
			back_img_bool = false;
			if (selectImgs != null && selectImgs.length() != 0) {
				setImgData(selectImgs);
			}
			// selectImgs = data.getStringExtra("select_imgs");
			// Log.d("selectImgs", selectImgs);
			// setImgData(selectImgs);
			break;
		default:
			break;
		}
	}

	/**
	 * 处理返回的图片信息
	 * 
	 * @param imgs
	 */
	private void setImgData(String imgs) {
		String[] imgArray = imgs.split(",");
		if (imgs.trim().length() > 0 && imgArray != null && imgArray.length > 0
				&& imgArray.length <= 9) {
			imglist.clear();
			for (int i = 0; i < imgArray.length; i++) {
				CommentImgeInfo initCI = new CommentImgeInfo();
				initCI.setId(i);
				initCI.setLocalhostPath(imgArray[i]);
				initCI.setIsImagePic(true);
				imglist.add(initCI);
			}
			if (imgArray.length < 0) {
				CommentImgeInfo initCINull = new CommentImgeInfo();
				initCINull.setIsImagePic(false);
				imglist.add(initCINull);
			}
			CommentImgeInfo initCINull = new CommentImgeInfo();
			initCINull.setIsImagePic(false);
			imglist.add(initCINull);
		} else {
			imglist.clear();
			CommentImgeInfo initCINull = new CommentImgeInfo();
			initCINull.setIsImagePic(false);
			imglist.add(initCINull);
		}
		mGridViewAdapter.notifyDataSetChanged();
	}

	/**
	 * 开始上传图片
	 */
	private void upLoadimg() {
		if (imglist.size() == 0 | uploadProcess >= imglist.size()) {
			Log.d("feedback", "onSend");
			onSend();
			return;
		}
		CommentImgeInfo initCINull = imglist.get(uploadProcess);
		if (initCINull.getIsImagePic() && initCINull.getLocalhostPath() != null
				&& initCINull.getLocalhostPath().length() > 0) {
			MoreFilesUploadHandler.uploadFile(initCINull.getLocalhostPath(),
					modeType);
		} else {
			uploadProcess += 1;
			upLoadimg();
		}

	}

	public void popWin() {
		if (popFaceType != null && popFaceType.isShowing()) {
			popFaceType.dismiss();
		} else {
			layoutFaceType = getLayoutInflater().inflate(R.layout.pop_menulist,
					null);
			menulistFaceType = (ListView) layoutFaceType
					.findViewById(R.id.menulist);
			SimpleAdapter listAdapter = new SimpleAdapter(
					FeedbackActivity.this, listFaceType, R.layout.pop_menuitem,
					new String[] { "item" }, new int[] { R.id.menuitem });
			menulistFaceType.setAdapter(listAdapter);
			text = (TextView) findViewById(R.id.feedback_select_text);
			text.setTypeface(MyApplication.GetTypeFace());
			// 点击listview中item的处理
			menulistFaceType
					.setOnItemClickListener(new AdapterView.OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> arg0, View arg1,
								int arg2, long arg3) {
							// 改变顶部对应TextView值
							String strItem = listFaceType.get(arg2).get("item");
							text.setText(strItem);
							typeId = listFaceType.get(arg2).get("id");
							// 隐藏弹出窗口
							if (popFaceType != null && popFaceType.isShowing()) {
								popFaceType.dismiss();
							}
						}
					});

			// 创建弹出窗口
			// 窗口内容为layoutLeft，里面包含一个ListView
			// 窗口宽度跟tvLeft一样
			popFaceType = new PopupWindow(layoutFaceType, mSelect.getWidth(),
					LayoutParams.WRAP_CONTENT);

			ColorDrawable cd = new ColorDrawable(-0000);
			popFaceType.setBackgroundDrawable(cd);
			popFaceType.setAnimationStyle(R.style.PopupAnimation);
			popFaceType.update();
			popFaceType.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
			popFaceType.setTouchable(true); // 设置popupwindow可点击
			popFaceType.setOutsideTouchable(true); // 设置popupwindow外部可点击
			popFaceType.setFocusable(true); // 获取焦点

			// 设置popupwindow的位置（相对tvLeft的位置） (topBarHeight -
			// mSelect.getHeight()) / 2
			int topBarHeight = mSelect.getBottom();
			popFaceType.showAsDropDown(mSelect, 0, 0);

			popFaceType.setTouchInterceptor(new View.OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// 如果点击了popupwindow的外部，popupwindow也会消失
					if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
						popFaceType.dismiss();
						return true;
					}
					return false;
				}
			});
		}
	}

	@Override
	public void onUploadDone(int responseCode, String message) {
		// TODO Auto-generated method stub
		Log.d("feedback", message);
		Looper.prepare();
		int code = JsonUtil.getJsonStatus(message);
		if (code == HttpCode.serverCode.DATA_Success_CODE) {
			imglist.get(uploadProcess).setServerUrl(JsonUtil.JsonMSG);
			uploadProcess += 1;
			upLoadimg();
		} else {
			ToastUtil.showToast(JsonUtil.JsonMSG);
			mLoadingDialog.cancelDialog();
		}
		Looper.loop();
	}

	@Override
	public void onUploadProcess(int uploadSize) {
		// TODO Auto-generated method stub

	}

	@Override
	public void initUpload(int fileSize) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		CommentImgeInfo initCI = imglist.get(arg2);
		Intent intent = new Intent();
		if (imglist.size() == 1 || arg2 == imglist.size() - 1) {
			if (imglist.size() - 1 >= 9) {

			} else {

				if (imglist.size() >= 1) {
					selectImgs = "";
					for (int i = 0; i < imglist.size(); i++) {
						if (imglist.get(i).getLocalhostPath() != null
								&& imglist.get(i).getLocalhostPath() != "") {
							selectImgs += imglist.get(i).getLocalhostPath()
									+ ",";
						}

					}
					if (selectImgs.length() != 0) {
						selectImgs = selectImgs.substring(0,
								selectImgs.length() - 1);
					}

				}

				FastBlur.getScreen((Activity) mContext);
				intent.setClass(mContext, CameraChooseActivity.class);
				intent.putExtra("select_imgs", selectImgs);
				startActivityForResult(intent, 20);
			}

		}
		// if (initCI.getIsImagePic()) {
		// intent.setClass(mContext, ImageOriginalActivity.class);
		// intent.putExtra("id", arg2);
		// intent.putExtra("select_imgs", selectImgs);
		// startActivityForResult(intent, 20);
		// } else {
		// FastBlur.getScreen((Activity) mContext);
		// intent.setClass(mContext, CameraChooseActivity.class);
		// intent.putExtra("select_imgs", selectImgs);
		// startActivityForResult(intent, 20);
		// }
	}

}
