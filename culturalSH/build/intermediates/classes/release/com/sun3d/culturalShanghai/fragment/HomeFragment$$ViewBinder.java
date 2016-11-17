// Generated code from Butter Knife. Do not modify!
package com.sun3d.culturalShanghai.fragment;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class HomeFragment$$ViewBinder<T extends com.sun3d.culturalShanghai.fragment.HomeFragment> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131427660, "field 'mAddressTv'");
    target.mAddressTv = finder.castView(view, 2131427660, "field 'mAddressTv'");
    view = finder.findRequiredView(source, 2131427866, "field 'mCityItemGv'");
    target.mCityItemGv = finder.castView(view, 2131427866, "field 'mCityItemGv'");
    view = finder.findRequiredView(source, 2131427425, "field 'mListview'");
    target.mListview = finder.castView(view, 2131427425, "field 'mListview'");
    view = finder.findRequiredView(source, 2131427864, "field 'mGpsTv'");
    target.mGpsTv = finder.castView(view, 2131427864, "field 'mGpsTv'");
    view = finder.findRequiredView(source, 2131427865, "field 'mSelectTv'");
    target.mSelectTv = finder.castView(view, 2131427865, "field 'mSelectTv'");
    view = finder.findRequiredView(source, 2131427867, "field 'mAllAddress'");
    target.mAllAddress = finder.castView(view, 2131427867, "field 'mAllAddress'");
  }

  @Override public void unbind(T target) {
    target.mAddressTv = null;
    target.mCityItemGv = null;
    target.mListview = null;
    target.mGpsTv = null;
    target.mSelectTv = null;
    target.mAllAddress = null;
  }
}
