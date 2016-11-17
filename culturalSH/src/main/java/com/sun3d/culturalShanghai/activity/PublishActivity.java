package com.sun3d.culturalShanghai.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Looper;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.R;
import com.sun3d.culturalShanghai.Util.JsonUtil;
import com.sun3d.culturalShanghai.Util.PicImgCompressUtil;
import com.sun3d.culturalShanghai.Util.TextUtil;
import com.sun3d.culturalShanghai.Util.ToastUtil;
import com.sun3d.culturalShanghai.Util.UploadImgUtil;
import com.sun3d.culturalShanghai.dialog.LoadingDialog;
import com.sun3d.culturalShanghai.handler.MoreFilesUploadHandler;
import com.sun3d.culturalShanghai.http.HttpCode;
import com.sun3d.culturalShanghai.http.HttpRequestCallback;
import com.sun3d.culturalShanghai.http.HttpUrlList;
import com.sun3d.culturalShanghai.http.MyHttpRequest;
import com.sun3d.culturalShanghai.image.ImageLoader;
import com.sun3d.culturalShanghai.object.CommentImgeInfo;
import com.sun3d.culturalShanghai.view.FastBlur;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wangmingming on 2016/1/19.
 */
public class PublishActivity extends Activity implements View.OnClickListener, UploadImgUtil.OnUploadProcessListener, AdapterView.OnItemClickListener {
	private String TAG = "PublishActivity";
	private Context mContext;
	private LoadingDialog mLoadingDialog;
	private EditText inputEd;
	private TextView inputEd_num;
	private int toalNum = 120;// 设置最大输入字数
	private String feedimgurl = "";

	private int uploadProcess = 0;
	private GridView mGridView;
	private List<CommentImgeInfo> imglist = new ArrayList<CommentImgeInfo>();
	private GridViewAdapter mGridViewAdapter;
	private String selectImgs = "";

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
		setContentView(R.layout.activity_publish);
		MyApplication.getInstance().addActivitys(this);
		mContext = this;
		mLoadingDialog = new LoadingDialog(mContext);
		initView();
	}

	private void initView() {
		setTitle();

		inputEd = (EditText) findViewById(R.id.comment_inputcontent);
		inputEd_num = (TextView) findViewById(R.id.comment__content_num);
		inputEd_num.setText("剩余" + toalNum + "字");
		inputEd.setFilters(new InputFilter[] { new InputFilter.LengthFilter(toalNum) });
		inputEd.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence charsequence, int i, int j, int k) {
				// TODO Auto-generated method stub
				int nowNum = toalNum - (int) TextUtil.calculateWeiboLength(charsequence);
				inputEd_num.setText("剩余" + nowNum + "字");
				if (nowNum <= 0) {
					ToastUtil.showToast("您已超过限制的字数！");
				}

			}

			@Override
			public void beforeTextChanged(CharSequence charsequence, int i, int j, int k) {
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
			arg1 = LayoutInflater.from(mContext).inflate(R.layout.publish_imgitem, null);
			ImageView img = (ImageView) arg1.findViewById(R.id.imageView1);
			ViewGroup.LayoutParams para = img.getLayoutParams();
			Log.d(" dm.widthPixels", MyApplication.getWindowWidth() + "\n" + MyApplication.getWindowHeight());
			para.width = MyApplication.getWindowWidth() / 4;
			para.height = para.width;

			img.setLayoutParams(para);
			TextView delete = (TextView) arg1.findViewById(R.id.publish_item_delete);

			CommentImgeInfo initCI = imglist.get(arg0);
			if (initCI.getIsImagePic()) {
				delete.setVisibility(View.VISIBLE);
				img.setScaleType(ImageView.ScaleType.CENTER_CROP);
				if (ImageLoader.getInstance() == null) {
					ImageLoader.getInstance(3, ImageLoader.Type.LIFO);
				}
				Bitmap bit = ImageLoader.getBitmapFromLruCache(initCI.getLocalhostPath());
				if (bit == null) {
					bit = PicImgCompressUtil.get100Bitmap(initCI.getLocalhostPath());
				}
				img.setImageBitmap(bit);
			} else {
				delete.setVisibility(View.GONE);
			}

			delete.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					imglist.remove(arg0);
					selectImgs = "";
					if (imglist.size() > 1) {
						for (CommentImgeInfo cii : imglist) {
							if (cii.getLocalhostPath() != null) {
								selectImgs = selectImgs + "," + cii.getLocalhostPath();
							}
						}
						selectImgs = selectImgs.substring(1, selectImgs.length());
					} else {
						selectImgs = "";
					}
					setImgData(selectImgs);
					Log.d("imglistimglist", selectImgs);
					// mGridViewAdapter.notifyDataSetChanged();
				}
			});
			return arg1;
		}
	}

	/**
	 * 设置标题
	 */
	private void setTitle() {
		RelativeLayout mTitle = (RelativeLayout) findViewById(R.id.title);
		mTitle.findViewById(R.id.title_left).setOnClickListener(this);
		findViewById(R.id.title_right).setVisibility(View.GONE);
		// mSend.setOnClickListener(this);
		TextView title = (TextView) mTitle.findViewById(R.id.title_content);
		title.setText("我要发布");
		title.setVisibility(View.VISIBLE);
	}

	/**
	 * 上传图片
	 */
	private void upLoadimg() {
		if (imglist.size() == 0 | uploadProcess >= imglist.size()) {
			Log.d("feedback", "onSend");
			commitComment();
			return;
		}
		CommentImgeInfo initCINull = imglist.get(uploadProcess);
		if (initCINull.getIsImagePic() && initCINull.getLocalhostPath() != null && initCINull.getLocalhostPath().length() > 0) {
			MoreFilesUploadHandler.uploadFile(initCINull.getLocalhostPath(), "3");
		} else {
			uploadProcess += 1;
			upLoadimg();
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
		// params.put("commentType", CommentType);
		// params.put("commentRkId", CommentTypeId);
		params.put("commentImgUrl", feedimgurl);
		MyHttpRequest.onHttpPostParams(HttpUrlList.Comment.User_CommitComment, params, new HttpRequestCallback() {

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
						ToastUtil.showToast("添加评论失败!" + JsonUtil.JsonMSG);
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
			selectImgs = data.getStringExtra("select_imgs");
			Log.d("selectImgs", selectImgs);
			setImgData(selectImgs);
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
		if (imgs.trim().length() > 0 && imgArray != null && imgArray.length > 0) {
			imglist.clear();
			for (int i = 0; i < imgArray.length; i++) {
				CommentImgeInfo initCI = new CommentImgeInfo();
				initCI.setId(i);
				initCI.setLocalhostPath(imgArray[i]);
				initCI.setIsImagePic(true);
				imglist.add(initCI);
			}
			if (imgArray.length < 6) {
				CommentImgeInfo initCINull = new CommentImgeInfo();
				initCINull.setIsImagePic(false);
				imglist.add(initCINull);
			}

		} else {
			imglist.clear();
			CommentImgeInfo initCINull = new CommentImgeInfo();
			initCINull.setIsImagePic(false);
			imglist.add(initCINull);
		}
		mGridViewAdapter.notifyDataSetChanged();
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
			upLoadimg();
		} else {
			ToastUtil.showToast(JsonUtil.JsonMSG);
			mLoadingDialog.cancelDialog();
		}
		Looper.loop();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_left:
			finish();
			break;
		case R.id.activity_publish_ok:

			break;
		case R.id.activity_publish_cancel:
			finish();
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		CommentImgeInfo initCI = imglist.get(position);
		Intent intent = new Intent();
		if (initCI.getIsImagePic()) {
			intent.setClass(mContext, ImageOriginalActivity.class);
			intent.putExtra("id", position);
			intent.putExtra("select_imgs", selectImgs);
			startActivityForResult(intent, 20);
		} else {
			FastBlur.getScreen((Activity) mContext);
			intent.setClass(mContext, CameraChooseActivity.class);
			intent.putExtra("select_imgs", selectImgs);
			startActivityForResult(intent, 20);
		}
	}

	@Override
	public void onUploadProcess(int uploadSize) {

	}

	@Override
	public void initUpload(int fileSize) {

	}

}
