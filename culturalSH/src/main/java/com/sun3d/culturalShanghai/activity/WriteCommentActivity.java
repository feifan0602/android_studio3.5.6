package com.sun3d.culturalShanghai.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Looper;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.R;
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

/**
 * 这是评论的界面
 * 
 * @author wenff
 * 
 */
public class WriteCommentActivity extends Activity implements OnClickListener,
		OnUploadProcessListener, OnItemClickListener {
	private String TAG = "WriteCommentActivity";
	private Context mContext;
	private EditText inputEd;
	private TextView inputEd_num;
	private int toalNum = 500;// 设置最大输入字数
	private TextView middle_tv;
	/**
	 * true 表示可以返回 false 表示不能返回 图片
	 */
	private Boolean back_img_bool = true;
	private TextView mSend;
	/**
	 * 1-场馆 2-活动 3-藏品 6-团体
	 */
	private String CommentType = "";
	private String CommentTypeId = "";
	private LoadingDialog mLoadingDialog;
	private String loadingText = "评论中";

	private RelativeLayout commit;
	private String feedimgurl = "";
	private int uploadProcess = 0;
	private GridView mGridView;
	private List<CommentImgeInfo> imglist = new ArrayList<CommentImgeInfo>();
	private GridViewAdapter mGridViewAdapter;
	private String selectImgs = "";
	private RatingBar mRatingBar;
	private String modeType = "3";
	private ImageView left_iv;
	private TextView right_tv;

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onPageStart(TAG);
		MobclickAgent.onResume(mContext);
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(TAG);
		MobclickAgent.onPause(mContext);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_write_comment);
		MyApplication.getInstance().addActivitys(this);
		CommentTypeId = this.getIntent().getExtras().getString("Id");
		CommentType = this.getIntent().getExtras().getString("type");
		mContext = this;
		mLoadingDialog = new LoadingDialog(mContext);

		init();
	}

	/**
	 * 初始化
	 */
	private void init() {
		setTitle();
		mRatingBar = (RatingBar) findViewById(R.id.venue_ratingbar);
		middle_tv = (TextView) findViewById(R.id.middle_tv);
		middle_tv.setText("发布评论");
		left_iv = (ImageView) findViewById(R.id.left_iv);
		left_iv.setImageResource(R.drawable.back);
		left_iv.setOnClickListener(this);
		right_tv = (TextView) findViewById(R.id.right_tv);
		right_tv.setText("发布");
		right_tv.setOnClickListener(this);
		LinearLayout mRatingBarLayout = (LinearLayout) findViewById(R.id.ratingbar_layout);
		inputEd = (EditText) findViewById(R.id.comment_inputcontent);
		commit = (RelativeLayout) findViewById(R.id.comment_reserve);
		commit.setOnClickListener(this);
		inputEd_num = (TextView) findViewById(R.id.comment__content_num);
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
		mGridView = (GridView) findViewById(R.id.gridview);
		mGridView.setNumColumns(3);
		CommentImgeInfo initCI = new CommentImgeInfo();
		initCI.setIsImagePic(false);
		imglist.add(initCI);
		mGridViewAdapter = new GridViewAdapter();
		mGridView.setAdapter(mGridViewAdapter);
		mGridView.setOnItemClickListener(this);
		UploadImgUtil.getInstance().setOnUploadProcessListener(this);
		if ("2".equals(CommentType)) {
			mRatingBarLayout.setVisibility(View.GONE);
		} else {
			mRatingBarLayout.setVisibility(View.GONE);
		}

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

	/**
	 * 设置标题
	 */
	private void setTitle() {
		// RelativeLayout mTitle = (RelativeLayout) findViewById(R.id.title);
		// mTitle.findViewById(R.id.title_left).setOnClickListener(this);
		// findViewById(R.id.title_right).setVisibility(View.GONE);
		// mSend = (TextView) mTitle.findViewById(R.id.title_send);
		// mSend.setVisibility(View.GONE);
		// // mSend.setOnClickListener(this);
		// TextView title = (TextView) mTitle.findViewById(R.id.title_content);
		// title.setText("我要点评");
		// title.setVisibility(View.VISIBLE);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.write_comment, menu);
		return true;
	}

	/**
	 * 上传图片
	 */
	private void upLoadimg(String modeType) {
		if (imglist.size() == 0 | uploadProcess >= imglist.size()) {
			Log.d("feedback", "onSend");
			commitComment();
			return;
		}
		CommentImgeInfo initCINull = imglist.get(uploadProcess);
		if (initCINull.getIsImagePic() && initCINull.getLocalhostPath() != null
				&& initCINull.getLocalhostPath().length() > 0) {
			MoreFilesUploadHandler.uploadFile(initCINull.getLocalhostPath(),
					modeType);
		} else {
			uploadProcess += 1;
			upLoadimg(modeType);
		}

	}

	/**
	 * 提交评论
	 */
	private void commitComment() {
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
		params.put("commentUserId", MyApplication.loginUserInfor.getUserId());
		params.put("commentRemark", inputEd.getText().toString());
		params.put("commentType", CommentType);
		params.put("commentRkId", CommentTypeId);
		params.put("commentImgUrl", feedimgurl);
		params.put("commentStar", mRatingBar.getRating() + "");
		MyHttpRequest.onHttpPostParams(HttpUrlList.Comment.User_CommitComment,
				params, new HttpRequestCallback() {

					@Override
					public void onPostExecute(int statusCode, String resultStr) {
						// TODO Auto-generated method stub
						mLoadingDialog.cancelDialog();
						int code = JsonUtil.getJsonStatus(resultStr);
						if (code == 0) {
							ToastUtil.showToast("添加评论成功。");
							setResult(HttpCode.ServerCode_Comment.CODE_BACK);
							finish();
						} else {
							if (code == 10107) {
								ToastUtil.showToast("添加评论失败!您已经评论超过五次。");
							} else {
								ToastUtil.showToast("添加评论失败!"
										+ JsonUtil.JsonMSG);
							}

						}
					}
				});
	}

	/**
	 * 拍照回调
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case 30:
			Log.i("ceshi", "看看回掉的数据==  " + selectImgs.length()
					+ "  selectImgs  ==  " + selectImgs + "  back_img_bool== "
					+ back_img_bool);
			if (selectImgs.length() != 0 && back_img_bool == false) {
				Log.i("ceshi", "空了吗");
				selectImgs = "";
			}
			selectImgs = data.getStringExtra("select_imgs");
			back_img_bool = false;
			if (selectImgs != null && selectImgs.length() != 0) {
				setImgData(selectImgs);
			}
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

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.title_left:// 关闭
			finish();
			break;
		case R.id.comment_reserve:// 预定
			if (TextUtils.isEmpty(inputEd.getText().toString())
					|| inputEd.getText().toString().trim().length() == 0) {
				ToastUtil.showToast("请输入评论内容");
				return;
			}

			if (!"2".equals(CommentType)) {
				float rating = mRatingBar.getRating();
				if (rating == 0) {
					ToastUtil.showToast("请输入评分等级");
					return;
				}
			}
			feedimgurl = "";
			uploadProcess = 0;
			mLoadingDialog.startDialog("评论中");
			upLoadimg(modeType);
			break;
		case R.id.left_iv:
			finish();
			break;
		case R.id.right_tv:
			if (TextUtils.isEmpty(inputEd.getText().toString())
					|| inputEd.getText().toString().trim().length() == 0) {
				ToastUtil.showToast("请输入评论内容");
				return;
			}
			if (inputEd.getText().toString().trim().length() <= 3) {
				ToastUtil.showToast("不能少于4个字哟");
				return;
			}
			// if (!"2".equals(CommentType)) {
			// float rating = mRatingBar.getRating();
			// if (rating == 0) {
			// ToastUtil.showToast("请输入评分等级");
			// return;
			// }
			// }
			feedimgurl = "";
			uploadProcess = 0;
			mLoadingDialog.startDialog("评论中");
			upLoadimg(modeType);
			break;
		default:
			break;
		}
	}

	/**
	 * 下载完成
	 */
	@Override
	public void onUploadDone(int responseCode, String message) {
		// TODO Auto-generated method stub
		Log.d("wirtecomment", message);
		Looper.prepare();
		int code = JsonUtil.getJsonStatus(message);
		if (code == HttpCode.serverCode.DATA_Success_CODE) {
			imglist.get(uploadProcess).setServerUrl(JsonUtil.JsonMSG);
			uploadProcess += 1;
			upLoadimg(modeType);
		} else {
			ToastUtil.showToast(JsonUtil.JsonMSG);
			mLoadingDialog.cancelDialog();
		}
		Looper.loop();
	}

	/**
	 * 下载中，下载进度
	 */
	@Override
	public void onUploadProcess(int uploadSize) {
		// TODO Auto-generated method stub

	}

	/**
	 * 下载开始，统计文件大小
	 */
	@Override
	public void initUpload(int fileSize) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		CommentImgeInfo initCI = imglist.get(arg2);
		Intent intent = new Intent();
		// if (initCI.getIsImagePic()) {
		// intent.setClass(mContext, ImageOriginalActivity.class);
		// intent.putExtra("id", arg2);
		// intent.putExtra("select_imgs", selectImgs);
		// startActivityForResult(intent, 20);
		// } else {
		Log.i("ceshi", "imglist.size()===  " + imglist.size());
		// if (imglist.size() == 1 || arg2 == imglist.size() - 1) {
		// if (imglist.size() == 1) {
		// selectImgs="";
		// Log.i("ceshi", "看看传进去的数据====   "+ selectImgs);
		// FastBlur.getScreen((Activity) mContext);
		// intent.setClass(mContext, CameraChooseActivity.class);
		// intent.putExtra("select_imgs", selectImgs);
		// startActivityForResult(intent, 20);
		// }
		if (imglist.size() - 1 >= 9) {

		} else {

			if (imglist.size() >= 1) {
				selectImgs = "";
				for (int i = 0; i < imglist.size(); i++) {
					if (imglist.get(i).getLocalhostPath() != null
							&& imglist.get(i).getLocalhostPath() != "") {
						selectImgs += imglist.get(i).getLocalhostPath() + ",";
					}

				}
				if (selectImgs.length() != 0) {
					selectImgs = selectImgs.substring(0, selectImgs.length() - 1);
				}
			
			}
			Log.i("ceshi", "看看传进去的数据====   " + selectImgs);
			FastBlur.getScreen((Activity) mContext);
			intent.setClass(mContext, CameraChooseActivity.class);
			intent.putExtra("select_imgs", selectImgs);
			startActivityForResult(intent, 20);
		}

		// }

		// }
	}
}
