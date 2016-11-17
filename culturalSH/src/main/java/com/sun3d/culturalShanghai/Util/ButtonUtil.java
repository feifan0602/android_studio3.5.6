package com.sun3d.culturalShanghai.Util;

/**
 * 关于Button的优化类
 * 
 * @author yangyoutao
 * 
 */
public class ButtonUtil {
	private static long lastClickTime;
	private static int delayTimes = 1500;// 一秒之内重复点击

	/**
	 * 防止用户多次点击
	 * 
	 * @return
	 */
	public synchronized static Boolean isDelayClick() {
		long time = System.currentTimeMillis();
		if (time - lastClickTime < delayTimes) {
			ToastUtil.showToast("亲，你点的太快啦。休息一下吧！");
			return false;
		}
		lastClickTime = time;
		return true;
	}
}
