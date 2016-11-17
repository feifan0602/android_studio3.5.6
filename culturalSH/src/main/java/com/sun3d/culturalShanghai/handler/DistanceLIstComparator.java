package com.sun3d.culturalShanghai.handler;

import java.math.BigDecimal;
import java.util.Comparator;

import com.sun3d.culturalShanghai.object.EventInfo;

public class DistanceLIstComparator implements Comparator<EventInfo> {

	/**
	 * 如果o1小于o2,返回一个负数;如果o1大于o2，返回一个正数;如果他们相等，则返回0;
	 */
	@Override
	public int compare(EventInfo o1, EventInfo o2) {
		BigDecimal bg1 = new BigDecimal(o1.getDistance());
		BigDecimal bg2 = new BigDecimal(o2.getDistance());
		int acceptRow1 = bg1.intValue();
		int acceptRow2 = bg2.intValue();

		// 对日期字段进行升序，大雨升序
		if (acceptRow1 > acceptRow2) {
			return 1;
		}
		return -1;
	}
}