package com.sun3d.culturalShanghai.handler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.R;
import com.sun3d.culturalShanghai.Util.DensityUtil;
import com.sun3d.culturalShanghai.Util.TextUtil;
import com.sun3d.culturalShanghai.view.ScrollViewGridView;

/**
 * 内部标签类，用于对标签的形式改变
 * 
 * @author yangyoutao
 * 
 */
public class LabelHandler {
	private Context mContext;
	private List<String> Labellist = new ArrayList<String>();
	private LabelGridAdapter mLabelGridAdapter;
	private ScrollViewGridView mScrollViewGridView;
	private LinearLayout Maincontancol;
	private Boolean isSHowGrid = false;

	public LabelHandler(Context mContext, LinearLayout contancol, int num, Boolean isSHowGrid) {
		this.mContext = mContext;
		Maincontancol = contancol;
		this.isSHowGrid = isSHowGrid;
		mScrollViewGridView = (ScrollViewGridView) contancol.findViewById(R.id.label_gridview);
		mScrollViewGridView.setNumColumns(num);
		mLabelGridAdapter = new LabelGridAdapter();
		mScrollViewGridView.setAdapter(mLabelGridAdapter);
		if (this.isSHowGrid) {
			mScrollViewGridView.setVisibility(View.VISIBLE);
		} else {
			mScrollViewGridView.setVisibility(View.GONE);
		}
	}

	/**
	 * label初始化
	 * 
	 * @return
	 */
	private TextView getTextView() {
		TextView label = new TextView(mContext);
		label.setSingleLine();
		label.setTextColor(mContext.getResources().getColor(R.color.orange_color));
		label.setBackgroundResource(R.drawable.shape_red_nullcontent_rect);
		label.setPadding(DensityUtil.dip2px(mContext, 10), DensityUtil.dip2px(mContext, 2),
				DensityUtil.dip2px(mContext, 10), DensityUtil.dip2px(mContext, 2));
		LinearLayout.LayoutParams lat = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		lat.rightMargin = 20;
		label.setLayoutParams(lat);
		label.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
		return label;
	}

	/**
	 * 添加行排
	 */
	private void addHorizontalLabel() {
		Log.d("isSHowGrid", isSHowGrid + "");
		Log.d("addHorizontalLabel", "Labellist.size():" + Labellist.size());
		LinearLayout HorizontalLinearLayout = new LinearLayout(mContext);
		LinearLayout.LayoutParams HorizontalLayoutParams = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		HorizontalLayoutParams.bottomMargin = 20;
		HorizontalLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
		HorizontalLinearLayout.setLayoutParams(HorizontalLayoutParams);
		HorizontalLinearLayout.removeAllViews();
		HorizontalLinearLayout.removeAllViewsInLayout();
		addTextViewLabel(HorizontalLinearLayout);
		Maincontancol.addView(HorizontalLinearLayout);

	}

	/**
	 * 行排添加 label
	 * 
	 * @param HorizontalLinearLayout
	 */
	private void addTextViewLabel(LinearLayout HorizontalLinearLayout) {
		for (int i = 0; i < Labellist.size(); i++) {
			Log.d("addTextViewLabel", Labellist.get(i));
			TextView label = getTextView();
			label.setText(Labellist.get(i));
			HorizontalLinearLayout.addView(label);
			if (isNextLine(i + 1, HorizontalLinearLayout)) {
				return;
			}
		}

	}

	/**
	 * 判断是否该换行
	 * 
	 * @param i
	 * @param HorizontalLinearLayout
	 * @return
	 */
	private Boolean isNextLine(int i, LinearLayout HorizontalLinearLayout) {
		if (i > Labellist.size() - 1) {
			Labellist.clear();
			return true;
		}
		LinearLayout newHorizontalLinearLayout = HorizontalLinearLayout;
		TextView label11 = getTextView();
		label11.setText(Labellist.get(i));
		newHorizontalLinearLayout.addView(label11);
		Boolean isNext = false;
		if (TextUtil.getViewWidth(newHorizontalLinearLayout) < MyApplication.getWindowWidth() - 80) {
			Log.d("getViewWidth", TextUtil.getViewWidth(newHorizontalLinearLayout) + "----");
			isNext = false;
		} else {
			Log.d("isNextLine", Labellist.size() + "----i:" + i);
			Labellist = Labellist.subList(i, Labellist.size());
			Log.d("isNextLine", Labellist.size() + "");
			isNext = true;
		}
		newHorizontalLinearLayout.removeView(label11);
		return isNext;

	}

	public ScrollViewGridView getScrollViewGridView() {

		return mScrollViewGridView;
	}

	/**
	 * 设置每一行要显示多少列
	 * 
	 * @param num
	 */
	public void setGridViewNum(int num) {
		if (mScrollViewGridView != null) {
			mScrollViewGridView.setNumColumns(num);
		}
	}

	/**
	 * 字符串处理
	 * 
	 * @param labels
	 */
	private void setLabelsString(String labels) {
		if (labels != null && labels.length() > 0) {
			String[] labelArray = labels.split(",");
			if (labelArray != null && labelArray.length > 0) {
				Labellist = new ArrayList<String>();
				Set<String> setsr = new HashSet<String>();
				for (String text : labelArray) {
					Log.d("labelArray", "text:" + text + "---leng:" + text.length());
					setsr.add(text);
				}
				Labellist.addAll(setsr);
				StringLenghComparator sclength = new StringLenghComparator();
				Collections.sort(Labellist, sclength);
			}
		}

	}

	/**
	 * 设置label数据，以字符串形式
	 * 
	 * @param labels
	 */
	public void setLabelsData(String labels) {
		if (labels != null && labels.length() > 0) {
			Maincontancol.setVisibility(View.VISIBLE);
			setLabelsString(labels);

			if (!this.isSHowGrid) {
				while (this.Labellist.size() > 0) {
					addHorizontalLabel();
				}

			}
			if (mLabelGridAdapter != null) {
				mLabelGridAdapter.notifyDataSetChanged();
			}
		} else {
			Maincontancol.setVisibility(View.GONE);
		}

	}

	/**
	 * 设置label数据，以List形式
	 * 
	 * @param Labellist
	 */
	public void setLabelsData(List<String> Labellist) {
		if (Labellist != null && Labellist.size() > 0) {
			Maincontancol.setVisibility(View.VISIBLE);
			Maincontancol.removeAllViews();
			Maincontancol.removeAllViewsInLayout();
			Set<String> setsr = new HashSet<String>();
			for (String str : Labellist) {
				setsr.add(str);
			}
			Labellist = new ArrayList<String>();
			this.Labellist.addAll(setsr);
			StringLenghComparator sclength = new StringLenghComparator();
			Collections.sort(this.Labellist, sclength);

			if (!this.isSHowGrid) {
				while (this.Labellist.size() > 0) {
					addHorizontalLabel();
				}
			}
			if (mLabelGridAdapter != null) {
				mLabelGridAdapter.notifyDataSetChanged();
			}
		} else {
			Maincontancol.setVisibility(View.GONE);
		}

	}

	/**
	 * 内容适配器
	 * 
	 */
	class LabelGridAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			int count = 0;
			if (Labellist.size() >= mScrollViewGridView.getNumColumns()) {
				count = mScrollViewGridView.getNumColumns();
			} else {
				count = Labellist.size();
			}
			return count;
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return Labellist.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			// TODO Auto-generated method stub
			ViewHolder mHolder = null;
			if (arg1 == null) {
				mHolder = new ViewHolder();
				arg1 = View.inflate(mContext, R.layout.tab_label_item_layout, null);
				mHolder.mlabel = (TextView) arg1.findViewById(R.id.tab_label_tv);
				arg1.setTag(mHolder);
			} else {
				mHolder = (ViewHolder) arg1.getTag();
			}
			mHolder.mlabel.setText(Labellist.get(arg0));
			// int mWindowWidth = TextUtil.getViewWidth(mHolder.mlabel);
			// arg1.setLayoutParams(new GridView.LayoutParams(mWindowWidth,
			// LayoutParams.WRAP_CONTENT));
			return arg1;
		}

	}

	/**
	 * 设置内部类
	 * 
	 */
	private class ViewHolder {
		private TextView mlabel;
	}

}
