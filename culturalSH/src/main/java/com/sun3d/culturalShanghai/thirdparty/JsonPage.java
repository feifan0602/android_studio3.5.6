/*
 * 官网地站:http://www.mob.com
 * 技术支持QQ: 4006852216
 * 官方微信:ShareSDK   （如果发布新版本的话，我们将会第一时间通过微信将版本更新内容推送给您。如果使用过程中有任何问题，也可以通过微信与我们取得联系，我们将会在24小时内给予回复）
 *
 * Copyright (c) 2013年 mob.com. All rights reserved.
 */

package com.sun3d.culturalShanghai.thirdparty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import android.view.View;

/** Json数据显示页面 */
public class JsonPage {

	public static String setData(final HashMap<String, Object> data) {
		return format("", data);
	}

	public void onClick(View v) {
	}

	@SuppressWarnings("unchecked")
	private static String format(String sepStr, HashMap<String, Object> map) {
		StringBuffer sb = new StringBuffer();
		sb.append("{\n");
		String mySepStr = sepStr + "\t";
		int i = 0;
		for (Entry<String, Object> entry : map.entrySet()) {
			if (i > 0) {
				sb.append(",\n");
			}
			sb.append(mySepStr).append('\"').append(entry.getKey()).append("\":");
			Object value = entry.getValue();
			if (value instanceof HashMap<?, ?>) {
				sb.append(format(mySepStr, (HashMap<String, Object>) value));
			} else if (value instanceof ArrayList<?>) {
				sb.append(format(mySepStr, (ArrayList<Object>) value));
			} else if (value instanceof String) {
				sb.append('\"').append(value).append('\"');
			} else {
				sb.append(value);
			}
			i++;
		}
		sb.append('\n').append(sepStr).append('}');
		return sb.toString();
	}

	@SuppressWarnings("unchecked")
	private static String format(String sepStr, ArrayList<Object> list) {
		StringBuffer sb = new StringBuffer();
		sb.append("[\n");
		String mySepStr = sepStr + "\t";
		int i = 0;
		for (Object value : list) {
			if (i > 0) {
				sb.append(",\n");
			}
			sb.append(mySepStr);
			if (value instanceof HashMap<?, ?>) {
				sb.append(format(mySepStr, (HashMap<String, Object>) value));
			} else if (value instanceof ArrayList<?>) {
				sb.append(format(mySepStr, (ArrayList<Object>) value));
			} else if (value instanceof String) {
				sb.append('\"').append(value).append('\"');
			} else {
				sb.append(value);
			}
			i++;
		}
		sb.append('\n').append(sepStr).append(']');
		return sb.toString();
	}

}
