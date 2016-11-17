package com.sun3d.culturalShanghai.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.R;
import com.sun3d.culturalShanghai.object.PersonalInfo;

/**
 * 与我相关的信息
 * 
 * @author zhoutanping
 * */
public class PersonalRelevanceAdapter extends BaseAdapter {
	public String TAG = "PersonalRelevanceAdapter";
	private Context mContext;
	private View view, view1;
	private static final int TYPE_COUNT = 2;// item类型的总数
	private List<PersonalInfo> mList;
	/**
	 * 这是中间的分隔符
	 */
	private static final int TYPE_ONE = 0;
	/**
	 * 这是个人内容
	 */
	private static final int TYPE_TWO = 1;// 这是个人内容
	private int currentType;// 当前item类型

	// private int width = 0;

	public PersonalRelevanceAdapter(Context mContext, List<PersonalInfo> mList) {
		// TODO Auto-generated constructor stub
		this.mContext = mContext;
		this.mList = mList;
		// this.width = (windowWidth - 20) / 3;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public int getItemViewType(int position) {
		if (mList.get(position).getInfo() == 1) {
			return TYPE_ONE;// 这是中间分隔符
		} else if (mList.get(position).getInfo() == -1) {
			return TYPE_TWO;// 这是个人内容
		} else {
			return 100;
		}
	}

	@Override
	public int getViewTypeCount() {
		return TYPE_COUNT;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		PersonalInfo personal = mList.get(position);

		currentType = getItemViewType(position);
		if (currentType == TYPE_ONE) {
			ViewHolder1 vh1 = null;
			if (convertView == null) {
				vh1 = new ViewHolder1();
				view = LayoutInflater.from(mContext).inflate(
						R.layout.adapter_personal_line, null);

				view.setTag(vh1);
				convertView = view;
			} else {
				vh1 = (ViewHolder1) convertView.getTag();
			}

		} else if (currentType == TYPE_TWO) {
			ViewHolder vh = null;
			if (convertView == null) {
				view1 = LayoutInflater.from(mContext).inflate(
						R.layout.adapter_personal_relevance, null);
				vh = new ViewHolder();
				vh.mTitle = (TextView) view1.findViewById(R.id.listpsron);
				vh.status = (TextView) view1.findViewById(R.id.status);
				vh.img = (ImageView) view1
						.findViewById(R.id.listpsron_img_handel);
				vh.mTitle.setTypeface(MyApplication.GetTypeFace());
				vh.status.setTypeface(MyApplication.GetTypeFace());
				view1.setTag(vh);
				convertView = view1;
			} else {
				vh = (ViewHolder) convertView.getTag();
			}

			Drawable drawable = mContext.getResources().getDrawable(
					personal.getIcon());
			// drawable.setBounds(0, 0, drawable.getMinimumWidth(),
			// drawable.getMinimumHeight());
			vh.img.setImageDrawable(drawable);
			vh.status.setText(personal.getStatus());
			vh.mTitle.setText(personal.getTitle());
		}

		return convertView;
	}

	public class ViewHolder {
		TextView mTitle;
		ImageView img;
		TextView status;
	}

	public class ViewHolder1 {
		TextView mTitle;
		ImageView img;
	}
}
