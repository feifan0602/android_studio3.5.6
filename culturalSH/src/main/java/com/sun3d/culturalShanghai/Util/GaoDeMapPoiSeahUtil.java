package com.sun3d.culturalShanghai.Util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMap.InfoWindowAdapter;
import com.amap.api.maps.AMap.OnMapClickListener;
import com.amap.api.maps.AMap.OnMarkerClickListener;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.NaviPara;
import com.amap.api.navi.model.NaviLatLng;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.R;
import com.sun3d.culturalShanghai.activity.EventDetailsActivity;
import com.sun3d.culturalShanghai.activity.ImageOriginalActivity;
import com.sun3d.culturalShanghai.activity.VenueDetailActivity;
import com.sun3d.culturalShanghai.dialog.LoadingDialog;
import com.sun3d.culturalShanghai.object.EventInfo;
import com.sun3d.culturalShanghai.object.VenueDetailInfor;
import com.sun3d.culturalShanghai.thirdparty.GetTokenPage;

import java.util.List;

public class GaoDeMapPoiSeahUtil implements OnMarkerClickListener, AMap.OnInfoWindowClickListener,
        InfoWindowAdapter, OnMapClickListener, ImageLoadingListener {
    private String TAG = "GaoDeMapPoiSeahUtil";
    private AMap aMap;
    private Context mContext;
    private int gotype = 1;// 1为去活动详情，2为去场馆详情
    private LoadingDialog mLoadingDialog;
    private List<VenueDetailInfor> Venuelist;
    private Marker onclickMarker;

    public GaoDeMapPoiSeahUtil(Context mContext, AMap aMap) {
        this.mContext = mContext;
        this.aMap = aMap;

        mLoadingDialog = new LoadingDialog(mContext);

    }

    /**
     * 添加活动列表或者场馆列表的makrt
     *
     * @param listItem
     * @param Venuelist
     * @param type
     */
    public void addActivityMakrt(List<EventInfo> listItem, List<VenueDetailInfor> Venuelist,
                                 int type) {
        int i = 0;
        aMap.moveCamera(CameraUpdateFactory.zoomTo(13));// 设置地图放大系数
        aMap.setOnMarkerClickListener(this);// 添加点击marker监听事
        aMap.setOnInfoWindowClickListener(this);
        aMap.setInfoWindowAdapter(this);// 添加显示infowindow监听事件
        aMap.setOnMapClickListener(this);
        if (listItem != null) {
            gotype = 1;
            for (EventInfo eif : listItem) {
                try {
                    double lat = Double.parseDouble(eif.getEventLat());
                    double lon = Double.parseDouble(eif.getEventLon());
                    LatLng ll = new LatLng(lat, lon);
                    if (type == 1) {
                        if (i < 9) {
                            aMap.addMarker(
                                    new MarkerOptions()
                                            .position(ll)
                                            .title(eif.getEventName() + "," + eif.getEventId())
                                            .snippet(eif.getEventAddress())
                                            .icon(BitmapDescriptorFactory.fromView(getBitMap(String
                                                    .valueOf(eif.getTagId())))).draggable(true))
                                    .setObject(eif);
                        }
                    } else {
                        aMap.addMarker(
                                new MarkerOptions()
                                        .position(ll)
                                        .title(eif.getEventName() + "," + eif.getEventId())
                                        .snippet(eif.getEventAddress())
                                        .icon(BitmapDescriptorFactory
                                                .fromResource(R.drawable.sh_icon_mapicon_activity))
                                        .draggable(true)).setObject(eif);
                    }
                    i++;
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d(TAG, e.toString());
                }
            }
        } else if (Venuelist != null) {
            gotype = 2;
            for (VenueDetailInfor vdi : Venuelist) {
                try {
                    double lat = Double.parseDouble(vdi.getVenueLat());
                    double lon = Double.parseDouble(vdi.getVenueLon());
                    LatLng ll = new LatLng(lat, lon);
                    if (type == 1) {
                        if (i < 9) {
                            aMap.addMarker(
                                    new MarkerOptions()
                                            .position(ll)
                                            .title(vdi.getVenueName() + "," + vdi.getVenueId())
                                            .snippet(vdi.getVenueAddress())
                                            .icon(BitmapDescriptorFactory.fromView(getBitMap(String
                                                    .valueOf(vdi.getTagId())))).draggable(true))
                                    .setObject(vdi);
                        }
                    } else {
                        aMap.addMarker(
                                new MarkerOptions()
                                        .position(ll)
                                        .title(vdi.getVenueName() + "," + vdi.getVenueId())
                                        .snippet(vdi.getVenueAddress())
                                        .icon(BitmapDescriptorFactory
                                                .fromResource(R.drawable.sh_icon_mapicon_venue))
                                        .draggable(true)).setObject(vdi);
                    }
                    i++;
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d(TAG, e.toString());
                }
            }
        }
    }

    /**
     * 返回自定义图标
     *
     * @param text
     * @return
     */
    public View getBitMap(String text) {
        TextView textview = new TextView(mContext);
        textview.setText("");
        textview.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
        if (gotype == 1) {
            textview.setBackgroundResource(R.drawable.sh_icon_mapicon_activity);
        } else {
            textview.setBackgroundResource(R.drawable.sh_icon_mapicon_venue);
        }
        textview.setTextColor(Color.WHITE);
        return textview;
    }

    /**
     * 检测终端是否存在高德app
     */
    public boolean haveAppliction(String packageName, NaviLatLng mNaviEnd) {
        boolean isApp = GetTokenPage.isApkInstalled(mContext, packageName);
        if (!isApp) {
            ToastUtil.showToast("路线计算失败,请重试");
        }

        if (mNaviEnd != null) {
            LatLng end = new LatLng(mNaviEnd.getLatitude(), mNaviEnd.getLongitude());
            startAMapNavi(end);
        }

        return isApp;
    }

    /**
     * Marker的点击事件
     */
    @Override
    public boolean onMarkerClick(Marker marker) {
        // TODO Auto-generated method stub
        marker.showInfoWindow();
//        marker.setIcon(BitmapDescriptorFactory
//                .fromResource(R.drawable.sh_icon_mapicon_venue));
        onclickMarker = marker;
        return false;
    }

    /*
     * 8 InfoContent的点击事件 (non-Javadoc)
     *
     * @see
     * com.amap.api.maps.AMap.InfoWindowAdapter#getInfoContents(com.amap.api
     * .maps.model.Marker)
     */
    @Override
    public View getInfoContents(Marker arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    private String activityId = null;

    /**
     * Infowindow的点击事件
     */

    @Override
    public View getInfoWindow(final Marker marker) {
        // TODO Auto-generated method stub
        marker.setDraggable(false);
        View InfoWindowView = null;
        if (gotype == 1) {
            InfoWindowView = activityOnInfoWindos(marker);
        } else {
            InfoWindowView = defultMyOnInfoWindos(marker);
        }
        return InfoWindowView;
    }

    /**
     * 默认自定义消息框
     *
     * @param marker
     * @return
     */
    private View defultMyOnInfoWindos(final Marker marker) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.poikeywordsearch_uri, null);
        TextView title = (TextView) view.findViewById(R.id.title);
        String[] str = marker.getTitle().split(",");
        activityId = null;
        if (str != null && str.length > 1) {
            title.setText(str[0]);
            activityId = str[1];
        } else {
            title.setText(marker.getTitle());
        }

        TextView snippet = (TextView) view.findViewById(R.id.snippet);
        snippet.setText(marker.getSnippet());
        ImageButton button = (ImageButton) view.findViewById(R.id.start_amap_app);
        button.setVisibility(View.GONE);
        // 调起高德地图app导航功能
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mContext != null && activityId != null) {
                    goActivityDetail(activityId);
                }
            }
        });
        return view;
    }

    /**
     * 活动自定义消息框
     *
     * @param marker
     * @return
     */

    private View activityOnInfoWindos(Marker marker) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_map_search, null);
        ImageView img_top = (ImageView) view.findViewById(R.id.dialog_map_search_img_top);
        TextView title = (TextView) view.findViewById(R.id.title);
        TextView address = (TextView) view.findViewById(R.id.address);
        TextView time = (TextView) view.findViewById(R.id.time);
        TextView vote = (TextView) view.findViewById(R.id.vote);


        String value = "余票：";
        TextView look = (TextView) view.findViewById(R.id.look);
        final EventInfo eif = (EventInfo) marker.getObject();
        MyApplication.getInstance().getImageLoader()
                .displayImage(TextUtil.getUrlMiddle(eif.getEventIconUrl()), img_top, this);
        if (eif.getActivityIsReservation().equals("1")){
            vote.setVisibility(View.GONE);
        }else {
            vote.setVisibility(View.VISIBLE);
        }
        title.setText(eif.getEventName());
        address.setText("地址：" + eif.getEventAddress());

        time.setText("时间：" +TextUtil.getDate(eif.getActivityStartTime(), eif.getEventEndTime()));

        if (eif.getActivityAbleCount() != null && eif.getActivityAbleCount().length() > 0) {
            vote.setText(value + eif.getActivityAbleCount());
            SpannableStringBuilder builder = new SpannableStringBuilder(vote.getText().toString());
            ForegroundColorSpan redSpan = new ForegroundColorSpan(mContext.getResources().getColor(R.color.orange_color));
            builder.setSpan(redSpan, value.length(), vote.getText().toString().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            vote.setText(builder);
        } else {
            vote.setText("余票：无");
        }
        look.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mContext != null && eif.getEventId() != null) {
                    goActivityDetail(eif.getEventId());
                }

            }
        });
        img_top.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                // TODO Auto-generated method stub
                if (mContext != null && eif.getEventIconUrl() != null) {
                    Intent intent = new Intent(mContext, ImageOriginalActivity.class);
                    intent.putExtra("select_imgs", eif.getEventIconUrl());
                    intent.putExtra("id", 0);
                    mContext.startActivity(intent);
                }
            }
        });
        Log.d("MarkerMarker", eif.getActivityStartTime());
        return view;
    }

    /**
     * 图片合成 内圆
     *
     * @param bitmap
     */
    @SuppressWarnings("deprecation")
    private void addFrameToImage(ImageView img, Bitmap bitmap) // bmp原图(前景)
    // bm资源图片(背景)
    {
        Log.d("addFrameToImage", "addFrameToImage");
        Bitmap roundBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(roundBitmap);
        int color = 0xff424242;
        Paint paint = new Paint();
        // 设置圆形半径
        float radius = bitmap.getWidth() / 50;
        // 绘制圆形
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawCircle(0, 0, radius, paint);// 左上角
        canvas.drawCircle(bitmap.getWidth(), 0, radius, paint);// 右上角
        canvas.drawCircle(0, bitmap.getHeight(), radius, paint);// 左下角
        canvas.drawCircle(bitmap.getWidth(), bitmap.getHeight(), radius, paint);// 右下角
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT));
        canvas.drawBitmap(bitmap, 0, 0, paint);
        if (img != null) {
            img.setImageBitmap(roundBitmap);
        }
    }

    /**
     * 跳转页面
     *
     * @param id
     */
    public void goActivityDetail(String id) {
        if (id.length() > 0) {
            if (gotype == 1) {
                Intent intent = new Intent(mContext, EventDetailsActivity.class);
                intent.putExtra("eventId", id);
                mContext.startActivity(intent);
            } else {
                Intent intent = new Intent(mContext, VenueDetailActivity.class);
                intent.putExtra("venueId", id);
                mContext.startActivity(intent);
            }

        }
    }

    /**
     * 调起高德地图导航功能，如果没安装高德地图，会进入异常，可以在异常中处理，调起高德地图app的下载页面
     */
    public void startAMapNavi(Marker marker) {
        // 构造导航参数
        NaviPara naviPara = new NaviPara();
        // 设置终点位置
        naviPara.setTargetPoint(marker.getPosition());
        // 设置导航策略，这里是避免拥堵
        naviPara.setNaviStyle(4);
        try {
            // 调起高德地图导航
            ToastUtil.showToast("开始导航！");
            AMapUtils.openAMapNavi(naviPara, mContext);
        } catch (com.amap.api.maps.AMapException e) {
            // 如果没安装会进入异常，调起下载页面
            ToastUtil.showToast("您没有高德地图，请先下载才能使用该功能。");
            AMapUtils.getLatestAMapApp(mContext);
        }
    }

    /**
     * 调起高德地图导航功能，如果没安装高德地图，会进入异常，可以在异常中处理，调起高德地图app的下载页面
     */
    public void startAMapNavi(LatLng endlng) {
        // 构造导航参数
        NaviPara naviPara = new NaviPara();
        // 设置终点位置
        naviPara.setTargetPoint(endlng);
        // 设置导航策略，这里是避免拥堵
        naviPara.setNaviStyle(4);
        try {
            // 调起高德地图导航
            ToastUtil.showToast("开始导航！");
            AMapUtils.openAMapNavi(naviPara, mContext);
        } catch (com.amap.api.maps.AMapException e) {
            // 如果没安装会进入异常，调起下载页面
            ToastUtil.showToast("您没有高德地图，请先下载才能使用该功能。");
            AMapUtils.getLatestAMapApp(mContext);
        }

    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        marker.hideInfoWindow();
//        marker.setIcon(BitmapDescriptorFactory
//                .fromResource(R.drawable.sh_icon_mapicon_activity));
    }

    @Override
    public void onMapClick(LatLng arg0) {
        // TODO Auto-generated method stub
        if (onclickMarker != null) {
            onclickMarker.hideInfoWindow();
//            onclickMarker.setIcon(BitmapDescriptorFactory
//                    .fromResource(R.drawable.sh_icon_mapicon_activity));
        }
    }

    @Override
    public void onLoadingCancelled(String arg0, View arg1) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
        // TODO Auto-generated method stub
        Log.d("onLoadingStarted", "onLoadingComplete");
        ImageView img = (ImageView) arg1;
        addFrameToImage(img, arg2);
    }

    @Override
    public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
        // TODO Auto-generated method stub
        ImageView img = (ImageView) arg1;
        Bitmap bit = BitmapFactory.decodeResource(mContext.getResources(),
                R.drawable.sh_icon_error_loading);
        addFrameToImage(img, bit);
    }

    @Override
    public void onLoadingStarted(String arg0, View arg1) {
        // TODO Auto-generated method stub
        Log.d("onLoadingStarted", "onLoadingStarted");

    }
}
