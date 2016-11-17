package com.sun3d.culturalShanghai.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.sun3d.culturalShanghai.R;
import com.sun3d.culturalShanghai.Util.JsonUtil;
import com.sun3d.culturalShanghai.Util.ToastUtil;
import com.sun3d.culturalShanghai.adapter.CommetnListAdapter;
import com.sun3d.culturalShanghai.http.HttpCode;
import com.sun3d.culturalShanghai.http.HttpRequestCallback;
import com.sun3d.culturalShanghai.http.HttpUrlList;
import com.sun3d.culturalShanghai.http.MyHttpRequest;
import com.sun3d.culturalShanghai.object.CommentInfor;

public class VenueCommentFragment extends Fragment {
	private List<CommentInfor> list;
	private CommetnListAdapter mCommetnListAdapter;
	private ListView mListView;
	private String venueId;
	private int page = 0;
	private View footView;
	private TextView mText;

	@Override
	public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_venue_comment, null);
		mListView = (ListView) view.findViewById(R.id.comment_listview);
		mText = (TextView) view.findViewById(R.id.venue_comment_text);
		mListView.setFocusable(false);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		initData();

	}

	/**
	 * 设置venueid
	 * 
	 * @param venueId
	 */
	public void setVenueId(String venueId) {
		this.venueId = venueId;
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		list = new ArrayList<CommentInfor>();
		mCommetnListAdapter = new CommetnListAdapter(getActivity(), list, true);
		footView = View.inflate(getActivity(), R.layout.list_foot, null);
		mListView.addFooterView(footView);
		mListView.setAdapter(mCommetnListAdapter);
		footView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				page = page + HttpUrlList.HTTP_NUM;
				getData(page);
			}
		});
		getData(0);
	}

	/**
	 * 父类通知获取数据
	 * 
	 * @param mList
	 */
	public void onGetData() {
		list.clear();
		getData(0);
	}

	/*
	 * 设置数据
	 */

	public void setData(List<CommentInfor> mList) {
		if (mList.size() > 0) {
			list.addAll(mList);
			mCommetnListAdapter.setList(list);
			mText.setVisibility(View.GONE);
		} else {
			if (list.size() == 0) {
				mText.setVisibility(View.VISIBLE);
			}
		}
		if (list.size() < 20) {
			mListView.removeFooterView(footView);
		}
	}

	/**
	 * 获取数据
	 */
	private void getData(int page) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("moldId", venueId);
		params.put("type", "1");
		params.put("pageIndex", String.valueOf(page));
		params.put("pageNum", HttpUrlList.HTTP_NUM + "");
		MyHttpRequest.onHttpPostParams(HttpUrlList.Comment.User_CommentList, params, new HttpRequestCallback() {

			@Override
			public void onPostExecute(int statusCode, String resultStr) {
				// TODO Auto-generated method stub
				if (HttpCode.HTTP_Request_Success_CODE == statusCode) {
					List<CommentInfor> mList = JsonUtil.getCommentInforFromString(resultStr);
					setData(mList);
				} else {
					ToastUtil.showToast(resultStr);
				}
			}
		});
	}
}
