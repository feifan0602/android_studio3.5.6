package com.sun3d.culturalShanghai.adapter;

import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

public class MainViewPagerAdapter extends PagerAdapter { 
    private List<View> list;  
  
    public MainViewPagerAdapter(List<View> list) {  
        this.list = list;  
    }  
  
    @Override  
    public int getCount() {  
    	return Integer.MAX_VALUE;
    }  
  
    @Override  
    public boolean isViewFromObject(View arg0, Object arg1) {  
        return arg0 == arg1;  
    }  
  
    @Override  
    public void destroyItem(ViewGroup container, int position, Object object) {  
//    	((ViewPager)container).removeView(list.get(position % list.size()));  
    }  
  
    @Override  
    public Object instantiateItem(ViewGroup container, int position) {  
    	try{
    		((ViewPager)container).addView(list.get(position % list.size()), 0);
//    		container.removeView(list.get(position % list.size()));
    	}catch(Exception e){
    		e.printStackTrace();
    	}
        return list.get(position % list.size());  
    }  
  
  }
