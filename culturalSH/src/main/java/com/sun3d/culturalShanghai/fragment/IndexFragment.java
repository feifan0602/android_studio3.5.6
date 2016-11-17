package com.sun3d.culturalShanghai.fragment;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.sun3d.culturalShanghai.R;
import com.sun3d.culturalShanghai.adapter.IndexHorListAdapter;
import com.sun3d.culturalShanghai.object.UserBehaviorInfo;
import com.sun3d.culturalShanghai.view.HorizontalListView;

public class IndexFragment extends Fragment {
	private View view;
	private HorizontalListView mHorListView;
	private List<UserBehaviorInfo> mHorList;
	private IndexHorListAdapter mHorAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = View.inflate(getActivity(), R.layout.fragment_index, null);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		initView();
		initData();
	}

	private void initView() {
		mHorListView = (HorizontalListView) view.findViewById(R.id.index_hor_listview);
	}

	private void initData() {
		mHorList = new ArrayList<UserBehaviorInfo>();
		mHorAdapter = new IndexHorListAdapter(getActivity(), mHorList);
		mHorListView.setAdapter(mHorAdapter);
		mHorListView.setOnItemClickListener(new OnItemClickListener() {
			int position = -1;

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				if (position >= 0) {
					mHorList.get(position).setSelect(false);
				}
				mHorList.get(arg2).setSelect(true);
				position = arg2;
				mHorAdapter.notifyDataSetChanged();
			}
		});
	}
}
