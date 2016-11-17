package com.sun3d.culturalShanghai.handler;

import java.math.BigDecimal;
import java.util.Comparator;

public class StringLenghComparator implements Comparator<String> {

	/**
	 * 如果o1小于o2,返回一个负数;如果o1大于o2，返回一个正数;如果他们相等，则返回0;
	 */
	@Override
	public int compare(String o1, String o2) {
		BigDecimal bg1 = new BigDecimal(o1.length());
		BigDecimal bg2 = new BigDecimal(o2.length());
		int acceptRow1 = bg1.intValue();
		int acceptRow2 = bg2.intValue();
		if (acceptRow1 < acceptRow2) {
			return 1;
		}
		return -1;
	}
}