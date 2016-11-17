package com.sun3d.culturalShanghai.Util;

import android.widget.TextView;
import android.widget.Toast;

import com.sun3d.culturalShanghai.MyApplication;

/**
 * 传说中的吐司
 *
 * @author yangyoutao
 */
public class ToastUtil {
    private static Toast toast;
    private static TextView mText;

    public static void showToast(String str) {

        if (toast == null) {
            com.sun3d.culturalShanghai.Util.Toast.makeText(
                    MyApplication.getContext(), str,
                    com.sun3d.culturalShanghai.Util.Toast.LENGTH_LONG).show();
        }
//            toast = new Toast(MyApplication.getContext());
//            toast.setGravity(Gravity.CENTER, 0, 0);
////			toast.setGravity(Gravity.CENTER, 0, 200);
//            toast.setDuration(1000);
//            View view = View.inflate(MyApplication.getContext(), R.layout.layout_toast, null);
//            mText = (TextView) view.findViewById(R.id.toast_content);
//            toast.setView(view);
//            mText.setText(str);
//            // toast = Toast.makeText(MyApplication.getContext(), str,
//            // Toast.LENGTH_SHORT);
//        } else {
//            mText.setText(str);
//        }
//        toast.show();
    }

    public static void cancelToast() {
        if (toast != null) {
            toast.cancel();
        }
    }

}
