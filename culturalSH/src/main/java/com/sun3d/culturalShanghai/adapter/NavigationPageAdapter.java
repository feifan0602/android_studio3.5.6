/**
 * @author ZhouTanPing E-mail:strong.ping@foxmail.com 
 * @version 创建时间：2015-10-16 下午4:19:48 
 */
package com.sun3d.culturalShanghai.adapter;

import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * @author zhoutanping
 * 
 * 导航页
 *
 */
public class NavigationPageAdapter extends PagerAdapter {

	private List<ImageView> mListViews;  
    
    public NavigationPageAdapter(List<ImageView> mListViews) {  
    	//构造方法，参数是我们的页卡，这样比较方便。  
        this.mListViews = mListViews;
    }  

    @Override  
    public void destroyItem(ViewGroup container, int position, Object object)   {     
        container.removeView(mListViews.get(position));//删除页卡  
    }  


    @Override  
    public Object instantiateItem(ViewGroup container, int position) {  //这个方法用来实例化页卡         
         container.addView(mListViews.get(position), 0);//添加页卡  
         return mListViews.get(position);  
    }  

    @Override  
    public int getCount() {           
        return  mListViews.size();//返回页卡的数量  
    }  
      
    @Override  
    public boolean isViewFromObject(View arg0, Object arg1) {    
    	boolean boo = arg0==arg1;
    	Log.d("TAG1", boo + "");
        return arg0==arg1;//官方提示这样写  
    }  

}
