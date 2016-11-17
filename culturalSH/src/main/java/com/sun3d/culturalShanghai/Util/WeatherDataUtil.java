package com.sun3d.culturalShanghai.Util;

import java.util.HashMap;
import java.util.Map;

import com.sun3d.culturalShanghai.R;

/**
 * 设置天气icon
 * 
 * @author liningkang
 * 
 */
public class WeatherDataUtil {

	private String[] names = new String[] { "晴", "阴", "多云", "阵雨", "雷阵雨", "小雨", "中雨", "大雨", "暴雨", "大暴雨", "特大暴雨", "阵雪", "中雪", "小雪", "霾", "雷阵雨伴有冰雹", "雨夹雪" };
	private int[] imgs = new int[] { R.drawable.sh_weather_qingtian, R.drawable.sh_weather_yingtian, R.drawable.sh_weather_duoyun,
			R.drawable.sh_weather_zhengyu, R.drawable.sh_weather_lzy, R.drawable.sh_weather_xiaoyu, R.drawable.sh_weather_zhongyu, R.drawable.sh_weather_dayu,
			R.drawable.sh_weather_dayu, R.drawable.sh_weather_baoyu, R.drawable.sh_weather_baoyu, R.drawable.sh_weather_daxue, R.drawable.sh_weather_zhongxue,
			R.drawable.sh_weather_xiaoxue, R.drawable.sh_weather_mai, R.drawable.sh_weather_lzy_bb, R.drawable.sh_weather_yu_xue };
	private int[] imgs_w = new int[] { R.drawable.sh_weather_qingtian_w, R.drawable.sh_weather_yingtian_w, R.drawable.sh_weather_duoyun_w,
			R.drawable.sh_weather_zhengyu_w, R.drawable.sh_weather_lzy_w, R.drawable.sh_weather_xiaoyu_w, R.drawable.sh_weather_zhongyu_w,
			R.drawable.sh_weather_dayu_w, R.drawable.sh_weather_baoyu_w, R.drawable.sh_weather_baoyu_w, R.drawable.sh_weather_baoyu_w,
			R.drawable.sh_weather_daxue_w, R.drawable.sh_weather_zhongxue_w, R.drawable.sh_weather_xiaoxue_w, R.drawable.sh_weather_mai_w,
			R.drawable.sh_weather_lzy_bb_w, R.drawable.sh_weather_yu_xue_w };
	private Map<String, Integer> map = new HashMap<String, Integer>();
	private Map<String, Integer> mapMain = new HashMap<String, Integer>();

	/**
	 * 天气列表
	 * 
	 * @return
	 */
	public Map<String, Integer> setWeather() {
		for (int i = 0; i < names.length; i++) {
			map.put(names[i], imgs[i]);
		}
		return map;
	}

	/**
	 * 首页天气
	 * 
	 * @return
	 */
	public Map<String, Integer> setMainWeather() {
		for (int i = 0; i < names.length; i++) {
			mapMain.put(names[i], imgs_w[i]);
		}
		return mapMain;
	}
}
