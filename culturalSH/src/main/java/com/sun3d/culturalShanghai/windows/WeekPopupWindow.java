package com.sun3d.culturalShanghai.windows;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.sun3d.culturalShanghai.R;
import com.sun3d.culturalShanghai.activity.ThisWeekActivity.WeekPopupCallback;
import com.sun3d.culturalShanghai.adapter.WeekItemAdapter;
import com.sun3d.culturalShanghai.object.UserBehaviorInfo;

public class WeekPopupWindow {
    private PopupWindow popFaceType;
    private WeekItemAdapter mWeekItemAdapter;
    /**
     * 显示筛选
     */
    public void showWeekPopupWindow(Context contec, final View view, final TextView rigthTv, final Drawable drawable,
                                    List<UserBehaviorInfo> mlist,
                                    final WeekPopupCallback callback) {
        rigthTv.setText("确定");
        rigthTv.setCompoundDrawables(null, null, null, null);
        if (popFaceType != null && popFaceType.isShowing()) {
            popFaceType.dismiss();
            rigthTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (rigthTv.getText().equals("确定")) {
                        popFaceType.dismiss();
                    }
                }
            });
        } else if (popFaceType != null && !popFaceType.isShowing()) {
            popFaceType.showAsDropDown(view, 0, 0);

        } else {

            LinearLayout layoutFaceType = (LinearLayout) LayoutInflater.from(contec).inflate(R.layout.pop_weeklist, null);
            ListView menulistFaceType = (ListView) layoutFaceType.findViewById(R.id.menulist);
                mWeekItemAdapter = new WeekItemAdapter(contec, mlist);
                menulistFaceType.setAdapter(mWeekItemAdapter);
                // 点击listview中item的处理
                // final HashMap<Integer, Boolean> mList = new HashMap<>();
                menulistFaceType.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                        // 改变顶部对应TextView值
                        if (arg2 == 0) {
                            WeekItemAdapter.initAll(false);
                            if (WeekItemAdapter.indexSelect.get(0) == true) {
                                WeekItemAdapter.indexSelect.put(0, false);
                            } else {
                                WeekItemAdapter.indexSelect.put(0, true);
                            }

                        } else {
                            WeekItemAdapter.re_init(false);
                            if (WeekItemAdapter.indexSelect.get(arg2) == true) {
                                WeekItemAdapter.indexSelect.put(arg2, false);
                            } else {
                                WeekItemAdapter.indexSelect.put(arg2, true);
                            }
                        }
                        mWeekItemAdapter.setIndexSelect(WeekItemAdapter.indexSelect);
                        // ToastUtil.showToast(mflagList.get(arg2));
                        // 隐藏弹出窗口
                        if (popFaceType != null && popFaceType.isShowing()) {

                        }
                    }
                });



            // 创建弹出窗口
            popFaceType = new PopupWindow(layoutFaceType, view.getWidth(), LayoutParams.MATCH_PARENT);
            ColorDrawable cd = new ColorDrawable(0xb0000000);
            popFaceType.setBackgroundDrawable(cd);
            popFaceType.setAnimationStyle(R.style.PopupAnimation);
            popFaceType.update();
            popFaceType.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
            popFaceType.setTouchable(true); // 设置popupwindow可点击
            popFaceType.setOutsideTouchable(false); // 设置popupwindow外部可点击
            popFaceType.setFocusable(true); // 获取焦点

            // 设置popupwindow的位置（相对tvLeft的位置） (topBarHeight -
            // mSelect.getHeight()) / 2
            popFaceType.showAsDropDown(view, 0, 0);
            if (popFaceType != null) {
                popFaceType.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        rigthTv.setText("筛选");
                        rigthTv.setCompoundDrawables(null, null, drawable, null);
                        callback.referShing();
                    }
                });
            }

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

}
