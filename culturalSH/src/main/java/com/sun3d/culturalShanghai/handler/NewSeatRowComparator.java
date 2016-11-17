package com.sun3d.culturalShanghai.handler;

import java.util.Comparator;

import com.sun3d.culturalShanghai.seat.CH_seatInfo;

public class NewSeatRowComparator implements Comparator<CH_seatInfo> {

	/**
	 * 如果o1小于o2,返回一个负数;如果o1大于o2，返回一个正数;如果他们相等，则返回0;
	 */
	@Override
	public int compare(CH_seatInfo o1, CH_seatInfo o2) {
		int acceptRow1 = o1.getRaw();
		int acceptRow2 = o2.getRaw();

		// 对日期字段进行升序，如果欲降序可采用before方法
		if (acceptRow1 > acceptRow2) {
			return 1;
		} else if (acceptRow1 == acceptRow2) {
			if (o1.getColumn() > o2.getColumn()) {
				return 1;
			}
		}
		return -1;
	}
	
}
