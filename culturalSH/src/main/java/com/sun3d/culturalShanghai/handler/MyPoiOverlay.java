package com.sun3d.culturalShanghai.handler;

import java.util.List;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.overlay.PoiOverlay;
import com.amap.api.services.core.PoiItem;
import com.sun3d.culturalShanghai.R;

/**
 * 地图改变覆盖物，高德地图覆盖物的重写
 * 
 * @author yangyoutao
 * 
 */

public class MyPoiOverlay extends PoiOverlay {
	private BitmapDescriptor bdptor = BitmapDescriptorFactory
			.fromResource(R.drawable.sh_icon_mapcar);

	public MyPoiOverlay(AMap amap, List<PoiItem> list) {
		super(amap, list);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 重写覆盖物的方法，替换系统的方法
	 */
	@Override
	protected BitmapDescriptor getBitmapDescriptor(int i) {
		// TODO Auto-generated method stub
		return bdptor;
	}
	

}
