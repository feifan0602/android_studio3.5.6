package com.sun3d.culturalShanghai.image;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sun3d.culturalShanghai.R;

public class MyAdapter extends CommonAdapter<String> {
	/**
	 * 显示选中图片信息
	 */
	private TextView showSelect;
	/**
	 * 用户选择的图片，存储为图片的完整路径
	 */
	private List<String> mSelectedImage = new LinkedList<String>();

	/**
	 * 文件夹路径
	 */
	private String mDirPath;

	/**
	 * 选中最大张数
	 */
	private int maxSelect = 3;

	public MyAdapter(Context context, List<String> mDatas, int itemLayoutId, String dirPath,
			TextView tv, int MaxSelect, List<String> mSelectedImage) {
		super(context, mDatas, itemLayoutId);
		this.mDirPath = dirPath;
		this.showSelect = tv;
		this.maxSelect = MaxSelect;
		this.mSelectedImage.clear();
		if (mSelectedImage != null) {
			this.mSelectedImage = mSelectedImage;
		}
	}

	/**
	 * 返回选中图片的列表
	 * 
	 * @return
	 */
	public List<String> getmSelectedImage() {
		return mSelectedImage;
	}

	@Override
	public void convert(final ViewHolder helper, final String item) {
		// 设置no_pic
		helper.setImageResource(R.id.id_item_image, R.drawable.sh_icon_error_loading);
		// 设置no_selected
		helper.setImageResource(R.id.id_item_select, R.drawable.picture_unselected);

		File imageFile = new File(mDirPath + "/" + item);

		// 设置图片
		 helper.setImageByUrl(R.id.id_item_image, mDirPath + "/" + item);

		final ImageView mImageView = helper.getView(R.id.id_item_image);
		final ImageView mSelect = helper.getView(R.id.id_item_select);
		
		mImageView.setColorFilter(null);
		// 设置ImageView的点击事件
		mImageView.setOnClickListener(new OnClickListener() {
			// 选择，则将图片变暗，反之则反之
			@Override
			public void onClick(View v) {

				// 已经选择过该图片
				if (mSelectedImage.contains(mDirPath + "/" + item)) {
					mSelectedImage.remove(mDirPath + "/" + item);
					mSelect.setImageResource(R.drawable.picture_unselected);
					mImageView.setColorFilter(null);
				} else
				// 未选择该图片
				{
					if (mSelectedImage.size() < maxSelect) {
						mSelectedImage.add(mDirPath + "/" + item);
						mSelect.setImageResource(R.drawable.pictures_selected);
						mImageView.setColorFilter(Color.parseColor("#77000000"));
					} else {
						Toast.makeText(mContext, "最多选择" + maxSelect + "张", Toast.LENGTH_SHORT)
								.show();
					}
					showSelect.setText("选中：" + mSelectedImage.size() + "/" + maxSelect + "张");
				}

			}
		});

		/**
		 * 已经选择过的图片，显示出选择过的效果
		 */
		if (mSelectedImage.contains(mDirPath + "/" + item)) {
			mSelect.setImageResource(R.drawable.pictures_selected);
			mImageView.setColorFilter(Color.parseColor("#77000000"));
		}

	}

}
