package com.sun3d.culturalShanghai.windows;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;

import com.sun3d.culturalShanghai.R;
import com.sun3d.culturalShanghai.Util.DensityUtil;
import com.sun3d.culturalShanghai.adapter.CollectionTypeListAdapter;

public class CollectionListWindows {
	public static final int CollectionType = 1;
	public static final int CollectionTime = 2;
	private Context mContext;
	private View mView;
	private PopupWindow popup;
	private ListView mlist;
	private List<String> list;
	private String[] typestr = new String[] { "查看全部", "青铜器", "玉器", "瓷器", "石器", "金银器", "陶瓷器", "珐琅" };
	private String[] Timestr = new String[] { "查看全部", "春秋", "宋朝", "唐朝", "元朝", "明朝", "五代十国",
			"50世纪中叶" };

	public CollectionListWindows(Context mContext) {
		this.mContext = mContext;
	}

	public void showList(LinearLayout mTop, final int type, List<String> mlistset) {
		if (mlistset != null) {
			list = new ArrayList<String>();
			list.add("查看全部");
			list.addAll(mlistset);
		} else {
			list = new ArrayList<String>();
			addData(type);
		}

		mView = View.inflate(mContext, R.layout.collectionlistwindows, null);
		popup = new PopupWindow(mView, LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		mlist = (ListView) mView.findViewById(R.id.collection_listview);
		popup.setFocusable(true);
		popup.setBackgroundDrawable(new BitmapDrawable());
		popup.showAsDropDown(mTop, 0, DensityUtil.dip2px(mContext, 2));
		mlist.setAdapter(new CollectionTypeListAdapter(mContext, list));

		if (null != mPopupWindowShowLisener) {
			mPopupWindowShowLisener.isShow();
		}
		popup.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				// TODO Auto-generated method stub
				if (null != mPopupWindowShowLisener) {
					mPopupWindowShowLisener.isDismiss();
				}
			}
		});
		mlist.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				if (null != mListOnItemClickListener) {
					mListOnItemClickListener.onItemClick(list.get(arg2), type);
					popup.dismiss();
				}
			}
		});
	}

	private void addData(int type) {
		if (type == 1) {
			for (String str : typestr) {
				list.add(str);
			}

		} else {
			for (String str : Timestr) {
				list.add(str);
			}

		}

	}

	private PopupWindowShowLisener mPopupWindowShowLisener;

	public void setonPopupWindowShowLisener(PopupWindowShowLisener popupWindowShowLisener) {
		this.mPopupWindowShowLisener = popupWindowShowLisener;
	}

	public interface PopupWindowShowLisener {
		public void isShow();

		public void isDismiss();
	}

	private ListOnItemClickListener mListOnItemClickListener;

	public void setListOnItemClickListener(ListOnItemClickListener listOnItemClickListener) {
		this.mListOnItemClickListener = listOnItemClickListener;
	}

	public interface ListOnItemClickListener {
		public void onItemClick(String name, int type);
	}

}
