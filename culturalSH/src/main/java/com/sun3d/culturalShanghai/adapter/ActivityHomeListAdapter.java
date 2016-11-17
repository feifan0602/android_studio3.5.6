package com.sun3d.culturalShanghai.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.R.integer;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.alibaba.fastjson.JSONArray;
import com.amap.api.mapcore.util.co;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.R;
import com.sun3d.culturalShanghai.Util.AppConfigUtil;
import com.sun3d.culturalShanghai.Util.ButtonUtil;
import com.sun3d.culturalShanghai.Util.DensityUtil;
import com.sun3d.culturalShanghai.Util.Options;
import com.sun3d.culturalShanghai.Util.TextUtil;
import com.sun3d.culturalShanghai.activity.ActivityDetailActivity;
import com.sun3d.culturalShanghai.activity.ActivityMap;
import com.sun3d.culturalShanghai.activity.BannerWebView;
import com.sun3d.culturalShanghai.activity.EventListActivity;
import com.sun3d.culturalShanghai.activity.MyLoveActivity;
import com.sun3d.culturalShanghai.activity.ThisWeekActivity;
import com.sun3d.culturalShanghai.activity.VenueDetailActivity;
import com.sun3d.culturalShanghai.activity.VenueListActivity;
import com.sun3d.culturalShanghai.animation.ContainerAnimation;
import com.sun3d.culturalShanghai.fragment.ActivityFragment;
import com.sun3d.culturalShanghai.handler.ActivityTabHandler;
import com.sun3d.culturalShanghai.handler.ActivityTabHandler.TabCallback;
import com.sun3d.culturalShanghai.object.ActivityConditionInfo;
import com.sun3d.culturalShanghai.object.EventInfo;
import com.sun3d.culturalShanghai.object.ScreenInfo;
import com.sun3d.culturalShanghai.object.UserBehaviorInfo;
import com.sun3d.culturalShanghai.object.Wff_BannerInfo;
import com.sun3d.culturalShanghai.object.Wff_VenuePopuwindow;
import com.sun3d.culturalShanghai.view.HorizontalListView;
import com.sun3d.culturalShanghai.view.SlideShowView;

@SuppressLint("UseSparseArrays")
public class ActivityHomeListAdapter extends BaseAdapter implements OnTouchListener, OnClickListener
{
	private String TAG = "ActivityHomeListAdapter";
	private Map<Integer, View> m = new HashMap<Integer, View>();
	private Context mContext;
	private List<EventInfo> list;
	private List<EventInfo> list_banner;
	private List<EventInfo> list_frist;
	private Boolean isShowView = false;
	private Boolean isShowTopPage = false;
	private View topView = null;
	private int onclickindex = 2;
	public Boolean isFrast = true;
	private PullToRefreshListView contanView = null;
	private int count;
	private String Activity_theme = "";
	private String Activity_themeId = "";
	private String Activity_type = "";
	private String Activity_typeId = "cf719729422c497aa92abdd47acdaa56";
	private ViewHolder_Frist vhf = null;
	private ViewHolder_Advertisement vha = null;
	private ViewHolder vh = null;
	private ViewHolder_Main vh_main = null;
	private static final int TYPE_COUNT = 5;// item类型的总数
	private static final int TYPE_BANNER = 0;// 廣告類型
	private static final int TYPE_MAIN = 1;// 主類型
	private static final int TYPE_FRIST = 2;// 顶部三个广告
	private static final int TYPE_NULL = 3;// 空的布局
	private static final int TYPE_BANNERLAST = 4;// 空的布局
	private int currentType;// 当前item类型
	private boolean top_banner = false;
	private int banner_pos = 0;// 广告占位 所导致的数据的缺失
	private int num;
	private ActivityFragment af;
	private PopupWindow pw;
	private HomePopuWindowAdapter hpwa;
	private Wff_VenuePopuwindow wvpw;
	private ArrayList<Wff_VenuePopuwindow> list_area_z;
	private ArrayList<Wff_VenuePopuwindow> list_area;
	private int frist_num = 0;
	private String shopping_areas = "全部商区";
	private String preface = "智能排序";
	private String sieve = "筛选";
	private TextView shopping_areas_tv;
	private TextView preface_tv;
	private TextView sieve_tv;
	private EventInfo eventInfo;
	public LinearLayout filterLayout;
	private int mType = 1;

	public ActivityHomeListAdapter(Context mContext, List<EventInfo> list, boolean isShowAddress, ActivityFragment af)
	{
		// TODO Auto-generated constructor stub
		this.af = af;
		this.mContext = mContext;
		this.list = list;
		isShowView = false;
		topView = null;
		filterLayout = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.home_sieve, null);
		filterLayout.setOnClickListener(this);

	}

	public ActivityHomeListAdapter(ActivityFragment fragment, Context mContext, List<EventInfo> list,
			boolean isShowAddress)
	{
		// TODO Auto-generated constructor stub
		this.af = fragment;
		this.mContext = mContext;
		this.list = list;
		isShowView = false;
		topView = null;
		filterLayout = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.home_sieve, null);
		filterLayout.setOnClickListener(this);
	}

	public int getOnclickindex()
	{
		return onclickindex;
	}

	public void setOnclickindex(int onclickindex)
	{
		this.onclickindex = onclickindex;
	}

	public void setBannerIsRefresh(boolean isrefresh)
	{
		if (isrefresh)
		{
			topView = null;
		}
	}

	public void setFragment(ActivityFragment act_af)
	{
		this.af = act_af;
	};

	public void addBannerContanView(PullToRefreshListView view)
	{
		contanView = view;
	}

	public void isShowFootView(Boolean isShow)
	{
		isShowView = isShow;
	}

	public void isActivityMainList(Boolean isShow)
	{
		isShowTopPage = isShow;
	}

	@Override
	public int getCount()
	{
		num = list.size();
		return num;
	}

	@Override
	public Object getItem(int arg0)
	{
		// TODO Auto-generated method stub
		return list.get(arg0);

	}

	@Override
	public long getItemId(int arg0)
	{
		// TODO Auto-generated method stub
		return arg0;
	}

	public void setList(List<EventInfo> list)
	{
		Log.i(TAG, "setList: "+list.size());
		this.list = list;
		this.notifyDataSetChanged();
	}

	public void activityCount(int count)
	{
		this.count = count;
	}

	@Override
	public View getView(int arg0, View convertView, ViewGroup arg2)
	{

		// TODO Auto-generated method stub
		currentType = getItemViewType(arg0);
		Log.i(TAG, "getView: ==  "+currentType);
		if (currentType == TYPE_BANNER)
		{
			convertView = addBannerView(arg0, convertView);
		} else if (currentType == TYPE_FRIST)
		{
			convertView = addFristView(arg0, convertView);
		} else if (currentType == TYPE_MAIN)
		{
			/**
			 * 原先的布局
			 */
			// convertView = addMainView(arg0, convertView);
			/**
			 * 3.5.4 重新布局
			 */
			convertView = addMainView_new(arg0, convertView);

		} else if (currentType == TYPE_NULL)
		{
			convertView = addNull(arg0, convertView);
		} else if (currentType == TYPE_BANNERLAST)
		{
			convertView = addMainLastView(banner_pos, convertView);
		}
		return convertView;
	}

	private View addMainView_new(int arg0, View convertView)
	{

		Log.i(TAG, "--in addMainView_new");
		if (convertView == null)
		{

			vh_main = new ViewHolder_Main();
			convertView = View.inflate(mContext, R.layout.activity_fragment_adapter_item, null);
			// vh_main.sieve_ll = (LinearLayout)
			// convertView.findViewById(R.id.Sieve);
			vh_main.ll = (LinearLayout) convertView.findViewById(R.id.ll);
			vh_main.distance_tv = (TextView) convertView.findViewById(R.id.distance_tv);
			vh_main.img = (ImageView) convertView.findViewById(R.id.img);
			vh_main.top_left_img = (ImageView) convertView.findViewById(R.id.top_left_img);
			vh_main.buttom_right_tv = (TextView) convertView.findViewById(R.id.buttom_right);
			vh_main.name_tv = (TextView) convertView.findViewById(R.id.name_tv);
			vh_main.address_tv = (TextView) convertView.findViewById(R.id.address_tv);
			vh_main.item_subject = (TextView) convertView.findViewById(R.id.item_subject);
			vh_main.item_subject1 = (TextView) convertView.findViewById(R.id.item_subject1);
			vh_main.item_subject2 = (TextView) convertView.findViewById(R.id.item_subject2);

			vh_main.distance_tv.setTypeface(MyApplication.GetTypeFace());
			vh_main.buttom_right_tv.setTypeface(MyApplication.GetTypeFace());

			vh_main.name_tv.setTypeface(MyApplication.GetTypeFace());
			vh_main.address_tv.setTypeface(MyApplication.GetTypeFace());
			vh_main.item_subject.setTypeface(MyApplication.GetTypeFace());
			vh_main.item_subject1.setTypeface(MyApplication.GetTypeFace());
			vh_main.item_subject2.setTypeface(MyApplication.GetTypeFace());
			convertView.setTag(vh_main);
		} else
		{
			vh_main = (ViewHolder_Main) convertView.getTag();
		}
		final EventInfo eventInfo = list.get(arg0);
		if (vh_main.activityId == null || (vh_main.activityId != null && !vh_main.activityId.equals(eventInfo.getActivityId().toString())))
		{//内容相同跳过
			
			vh_main.activityId = eventInfo.getActivityId();
			vh_main.name_tv.setText(eventInfo.getEventName());
			if (eventInfo.getEventEndTime() != null && !eventInfo.getActivityStartTime().equals(eventInfo.getEventEndTime()))
			{
				String start = eventInfo.getActivityStartTime().replaceAll("-", ".").substring(5, 10);
				String end = eventInfo.getEventEndTime().replaceAll("-", ".").substring(5, 10);
				if (!eventInfo.getActivityLocationName().equals(""))
				{
					vh_main.address_tv.setText(start + "-" + end + " | " + eventInfo.getActivityLocationName());
				} else
				{
					vh_main.address_tv.setText(start + "-" + end + " | " + eventInfo.getActivityAddress());
				}

			} else
			{
				String start = eventInfo.getActivityStartTime().replaceAll("-", ".").substring(5, 10);
				if (!eventInfo.getActivityLocationName().equals(""))
				{
					vh_main.address_tv.setText(start + " | " + eventInfo.getActivityLocationName());
				} else
				{
					vh_main.address_tv.setText(start + " | " + eventInfo.getActivityAddress());
				}

			}

			if ((list_banner != null && list_banner.size() != 0 && arg0 == 1) || ((list_banner == null || list_banner.size() == 0) && arg0 == 0))
			{

				FrameLayout fl = (FrameLayout) filterLayout.getParent();

				if (fl != null)
				{
					fl.removeView(filterLayout);
				}
				FrameLayout top = (FrameLayout) convertView.findViewById(R.id.top);
				top.addView(filterLayout);
				LayoutParams laytout = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, DensityUtil.dip2px(mContext, 30));
				laytout.topMargin = DensityUtil.dip2px(mContext, 20);
				laytout.gravity = Gravity.CENTER_HORIZONTAL;
				filterLayout.setLayoutParams(laytout);

				shopping_areas_tv = (TextView) convertView.findViewById(R.id.shopping_areas);
				preface_tv = (TextView) convertView.findViewById(R.id.preface);
				sieve_tv = (TextView) convertView.findViewById(R.id.sieve);
				shopping_areas_tv.setOnClickListener(this);
				preface_tv.setOnClickListener(this);
				sieve_tv.setOnClickListener(this);

				shopping_areas_tv.setTypeface(MyApplication.GetTypeFace());
				preface_tv.setTypeface(MyApplication.GetTypeFace());
				sieve_tv.setTypeface(MyApplication.GetTypeFace());

			}

			MyApplication.getInstance().getImageLoader().displayImage(TextUtil.getUrlMiddle(eventInfo.getEventIconUrl()), vh_main.img, Options.getRoundOptions(0));
			vh_main.img.setOnClickListener(new OnClickListener()
			{

				@Override
				public void onClick(View arg0)
				{
					Intent intent = new Intent(mContext, ActivityDetailActivity.class);
					intent.putExtra("eventId", eventInfo.getEventId());
					MyApplication.currentCount = -1;
					MyApplication.spike_Time = -1;
					MyApplication.total_availableCount = -1;
					mContext.startActivity(intent);
				}
			});

			if (eventInfo.getActivityIsReservation().equals("2"))
			{
				vh_main.top_left_img.setVisibility(View.VISIBLE);
				if (eventInfo.getSpikeType() == 1)
				{
					vh_main.top_left_img.setImageResource(R.drawable.icon_miao);
				} else
				{
					vh_main.top_left_img.setImageResource(R.drawable.icon_ding);
				}
			} else
			{
				vh_main.top_left_img.setVisibility(View.GONE);
			}
			if (eventInfo.getActivityIsFree() == 1)
			{
				vh_main.buttom_right_tv.setText("免费");
			} else
			{
				if (eventInfo.getActivityPrice().equals("null"))
				{
					vh_main.buttom_right_tv.setText("免费");
				} else
				{
					if (eventInfo.getPriceType() == 0)
					{
						vh_main.buttom_right_tv.setText(eventInfo.getActivityPrice() + "元/起");
					} else
					{
						vh_main.buttom_right_tv.setText(eventInfo.getActivityPrice() + "元/人");
					}
				}

			}
			if (eventInfo.getTagName() != null && eventInfo.getTagName() != "")
			{
				vh_main.item_subject.setVisibility(View.VISIBLE);
				vh_main.item_subject.setText(eventInfo.getTagName());
			} else
			{
				vh_main.item_subject.setVisibility(View.GONE);
			}
			if (eventInfo.getTagNameList().size() == 1)
			{
				vh_main.item_subject1.setVisibility(View.VISIBLE);
				vh_main.item_subject2.setVisibility(View.GONE);
				vh_main.item_subject1.setText(eventInfo.getTagNameList().get(0));
			} else if (eventInfo.getTagNameList().size() > 1)
			{
				vh_main.item_subject1.setVisibility(View.VISIBLE);
				vh_main.item_subject2.setVisibility(View.VISIBLE);
				vh_main.item_subject1.setText(eventInfo.getTagNameList().get(0));
				vh_main.item_subject2.setText(eventInfo.getTagNameList().get(1));
			} else
			{
				vh_main.item_subject1.setVisibility(View.GONE);
				vh_main.item_subject2.setVisibility(View.GONE);
			}
			if (top_banner)
			{
				if (arg0 == 1 && frist_num == 0)
				{
					// vh_main.sieve_ll.setVisibility(View.VISIBLE);
					// vh_main.shopping_areas_tv.setOnClickListener(this);
					// vh_main.preface_tv.setOnClickListener(this);
					// vh_main.sieve_tv.setOnClickListener(this);
					// vh_main.shopping_areas_tv.setText(shopping_areas);
					// vh_main.preface_tv.setText(preface);
					// vh_main.sieve_tv.setText(sieve);
					top_banner = false;
				} else
				{
					// vh_main.sieve_ll.setVisibility(View.GONE);
				}
			} else
			{
				if (arg0 == 0 && frist_num == 0)
				{
					// vh_main.sieve_ll.setVisibility(View.VISIBLE);
					// vh_main.shopping_areas_tv.setOnClickListener(this);
					// vh_main.preface_tv.setOnClickListener(this);
					// vh_main.sieve_tv.setOnClickListener(this);
					// vh_main.shopping_areas_tv.setText(shopping_areas);
					// vh_main.preface_tv.setText(preface);
					// vh_main.sieve_tv.setText(sieve);
					top_banner = false;
				} else
				{
					// vh_main.sieve_ll.setVisibility(View.GONE);
				}
			}
			if (mType != 5)
			{
				vh_main.distance_tv.setVisibility(View.GONE);
			} else
			{
				vh_main.distance_tv.setVisibility(View.VISIBLE);
				vh_main.distance_tv.setText(eventInfo.getDistance_str());
			}
		}
		return convertView;
	}

	public void setType(int type)
	{
		this.mType = type;
	}

	private View addMainLastView(int arg0, View convertView)
	{
		if (convertView == null)
		{
			vh = new ViewHolder();
			convertView = View.inflate(mContext, R.layout.adapter_event_list_item, null);
			vh.img = (ImageView) convertView.findViewById(R.id.item_img);
			vh.title = (TextView) convertView.findViewById(R.id.item_title);
			vh.time = (TextView) convertView.findViewById(R.id.item_time);
			vh.label = (ImageView) convertView.findViewById(R.id.item_label);
			vh.address = (TextView) convertView.findViewById(R.id.item_address);
			vh.type = (ImageView) convertView.findViewById(R.id.item_type);
			vh.value = (TextView) convertView.findViewById(R.id.item_value);
			vh.foot = (TextView) convertView.findViewById(R.id.footview);
			vh.tiketcount = (TextView) convertView.findViewById(R.id.item_tiketcount);
			vh.tiketcountLayout = (LinearLayout) convertView.findViewById(R.id.item_tiketcount_layout);
			vh.item_toplayout = (LinearLayout) convertView.findViewById(R.id.item_toplayout);
			vh.item_mapaddress = (RelativeLayout) convertView.findViewById(R.id.item_mapaddress);
			vh.yupiao = (ImageView) convertView.findViewById(R.id.item_yupiao);
			vh.item_subject = (TextView) convertView.findViewById(R.id.item_subject);
			vh.item_price = (TextView) convertView.findViewById(R.id.item_price);
			vh.item_price_danwei = (TextView) convertView.findViewById(R.id.item_price_danwei);
			convertView.setTag(vh);
		} else
		{
			vh = (ViewHolder) convertView.getTag();
		}
		if (list.size() == 0)
		{

		} else
		{

			final EventInfo eventInfo = list.get(arg0);
			if (eventInfo.getActivityIsHot() == 0)
			{
				// 非热门
				vh.type.setVisibility(View.GONE);
			} else
			{
				vh.type.setVisibility(View.GONE);
				// 热门
				// vh.type.setBackgroundResource(R.drawable.hot_img);
			}

			// if ("".equals(eventInfo.getActivitySubject())
			// || eventInfo.getActivitySubject() == null) {
			// mHolder.type.setVisibility(View.GONE);
			// } else {
			// mHolder.type.setBackgroundResource(R.drawable.new_img);
			// }
			vh.tiketcountLayout.setVisibility(View.VISIBLE);
			vh.address.setVisibility(View.VISIBLE);
			vh.item_mapaddress.setVisibility(View.VISIBLE);
			vh.tiketcount.setText(eventInfo.getActivityAbleCount());
			vh.item_subject.setText(eventInfo.getActivitySubject());
			if (eventInfo.getActivityIsFree() == 2)
			{
				// 表示不免费

				if (eventInfo.getEventPrice() != null && !eventInfo.getEventPrice().equals("0"))
				{
					vh.item_price.setText(eventInfo.getEventPrice());
					vh.item_price_danwei.setVisibility(View.VISIBLE);
				} else
				{
					vh.item_price_danwei.setVisibility(View.GONE);
					vh.item_price.setText("收费");
				}

			} else
			{
				vh.item_price_danwei.setVisibility(View.GONE);
				// 表示免费
				vh.item_price.setText("免费");
			}

			if (eventInfo.getActivityIsReservation().equals("1"))
			{
				vh.tiketcountLayout.setVisibility(View.GONE);
			} else
			{
				vh.tiketcountLayout.setVisibility(View.VISIBLE);
			}
			if (eventInfo.getEventPrice().equals("0") | eventInfo.getEventPrice().length() == 0)
			{
				vh.value.setText("免费");
				vh.value.setCompoundDrawables(null, null, null, null);
			} else
			{
				vh.value.setText("收费");
			}

			// 1：过期
			//
			// if (_activityModel.activityPast == 0)//未过期
			// {
			// if (_activityModel.activityIsReservation == 2)//可预订
			// {
			// if ( _activityModel.activityAbleCount)
			// {
			// _ticketConditonView.image = [UIImage
			// imageNamed:@"sh_haveTicket_icon.png"];
			// }
			// else
			// {
			// _ticketConditonView.image = [UIImage
			// imageNamed:@"sh_orderBookeUp.png"];
			// }
			// }
			// else//不可预订
			// {
			// _ticketConditonView.image = nil;
			// }
			// }
			// else//已过期
			// {
			// _ticketConditonView.image = [UIImage
			// imageNamed:@"sh_didFinish_icon.png"];
			// }

			if (null == eventInfo.getActivityPast())
			{
				getActivityPast = "0";
			} else
			{
				getActivityPast = eventInfo.getActivityPast();
			}
			// 右上角的图标
			vh.yupiao.setVisibility(View.GONE);
			if ("0".equals(getActivityPast))
			{
				if ("2".equals(eventInfo.getActivityIsReservation()))
				{
					if ("0".equals(eventInfo.getActivityAbleCount()))
					{
						vh.yupiao.setBackgroundResource(R.drawable.sh_dingwan);
					} else
					{
						vh.yupiao.setBackgroundResource(R.drawable.sh_yupiao);
					}
				} else
				{
					vh.yupiao.setVisibility(View.GONE);
				}
			} else
			{
				vh.yupiao.setBackgroundResource(R.drawable.sh_out_data);
			}
			// activityIsHot
			String activityStartTime = eventInfo.getActivityStartTime();
			activityStartTime = activityStartTime.replaceAll("-", "");
			Long starttime = Long.valueOf(activityStartTime);
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
			String str = formatter.format(curDate);
			str = str.replaceAll("-", "");
			long now_time = Long.valueOf(str);
			if (eventInfo.getActivityRecommend().equals("Y"))
			{// 推荐
				// vh.label.setImageResource(R.drawable.sh_icon_event_list_hot_icon);
			} else if (eventInfo.getActivityIsHot() == 1)
			{// 最热
				// vh.label.setImageResource(R.drawable.sh_icon_event_list_hot_icon);
			} else if (now_time - starttime <= 1)
			{
				vh.label.setImageResource(R.drawable.sh_icon_event_list_new_icon);
			} else
			{
				vh.label.setVisibility(View.GONE);
			}
			vh.item_toplayout.setTag(eventInfo);
			vh.item_mapaddress.setTag(eventInfo);
			if (eventInfo.getEventEndTime() != null && !eventInfo.getActivityStartTime().equals(eventInfo.getEventEndTime()))
			{
				String start = eventInfo.getActivityStartTime().replaceAll("-", ".").substring(5, 10);
				String end = eventInfo.getEventEndTime().replaceAll("-", ".").substring(5, 10);
				vh.time.setText(start + "-" + end);
			} else
			{
				String start = eventInfo.getActivityStartTime().replaceAll("-", ".").substring(5, 10);
				vh.time.setText(start);
			}

			// mHolder.time.setText("日期： " +
			// TextUtil.getDate(eventInfo.getActivityStartTime(),
			// eventInfo.getEventEndTime()));
			vh.title.setText(eventInfo.getEventName());
			vh.address.setText(eventInfo.getActivityLocationName());
			// if (eventInfo.getActivitySite().equals("")
			// || null == eventInfo.getActivitySite()) {
			//
			// } else {
			// vh.address.setText(eventInfo.getActivitySite());
			// }
			// MyApplication.getInstance().getImageLoader().displayImage(TextUtil.getUrlMiddle(eventInfo.getEventIconUrl()),
			// mHolder.img, Options.getRoundOptions(10));
			MyApplication.getInstance().getImageLoader().displayImage(TextUtil.getUrlMiddle(eventInfo.getEventIconUrl()), vh.img, Options.getRoundOptions(0));
			// MyApplication.getInstance().setImageView(
			// MyApplication.getContext(),
			// TextUtil.getUrlMiddle(eventInfo.getEventIconUrl()), vh.img,
			// Options.getRoundOptions(0));

			vh.item_toplayout.setOnClickListener(new OnClickListener()
			{

				@Override
				public void onClick(View arg0)
				{
					// TODO Auto-generated method stub
					onclick(arg0);
				}
			});
			vh.item_mapaddress.setOnClickListener(new OnClickListener()
			{

				@Override
				public void onClick(View arg0)
				{
					// TODO Auto-generated method stub
					onclick(arg0);
				}
			});
			if (arg0 == list.size() - 1)
			{
				if (isShowView)
				{
					vh.foot.setVisibility(View.VISIBLE);
				}
			} else
			{
				vh.foot.setVisibility(View.GONE);
			}
		}
		return convertView;
	}

	public void setBannerList(List<EventInfo> list_banner)
	{
		this.list_banner = list_banner;
		this.notifyDataSetChanged();
	}

	public void setList_area(ArrayList<Wff_VenuePopuwindow> list)
	{
		this.list_area = list;
	}

	@Override
	public int getItemViewType(int position)
	{
		// TODO Auto-generated method stub
		if (list_banner != null && position % 5 == 4 && list_banner.size() != 0 && position < 21 && list_banner.get(0).getList() != null && list_banner.get(0).getList().length() != 0 && list_banner.get(0).getList().length() * 5 > position)
		{
			// Log.i("ceshi", "查看数据 广告" + position);

			return TYPE_BANNER;// 每隔三个有一个广告类型
		} else if (position == 0 && list_banner != null && list_banner.size() != 0 && list_banner.get(0).getIsContainActivtiyAdv() == 1)
		{
			// Log.i("ceshi", "查看数据 头部广告" + position);
			top_banner = true;
			return TYPE_FRIST;// 广告类型
		}

		else if (list.size() != 0 && list.get(position).getActivityIsReservation() != null)
		{
			// Log.i("ceshi", "查看数据 主类型" + position);
			return TYPE_MAIN;// 主类型
		} else if (banner_pos != 0)
		{
			// Log.i("ceshi", "查看数据 多出來 " + position);
			banner_pos = 0;
			return TYPE_BANNERLAST;
		} else
		{
			// Log.i("ceshi", "查看数据 空布局" + position);
			return TYPE_NULL;// 空的
		}

	}

	@Override
	public int getViewTypeCount()
	{
		return TYPE_COUNT;
	}

	ContainerAnimation ca = new ContainerAnimation();

	@SuppressLint("NewApi")
	private View addAcrivitiTopPage(int arg0, View convertView)
	{
		// 这是第二项开始
		if (arg0 > 1)
		{
			int pos = (arg0 + 1) / 3;
			clear();
			// 这里是广告位
			if (list != null && list.size() != 0)
			{

				if (pos <= list.get(0).getList().length())
				{
					if (list.get(0).getIsContainActivtiyAdv() == 1)
					{
						// 表示有广告 每隔几行添加广告
						if (arg0 % 3 == 3)
						{
							org.json.JSONArray json_arr = list.get(0).getList();
							if (pos > json_arr.length())
							{
								convertView = addNull(arg0, convertView);
							} else
							{
								convertView = addBannerView(arg0, convertView);
							}

						} else
						{
							convertView = addMainView_new(arg0, convertView);
						}
					}
				} else
				{
					convertView = addMainView_new(arg0, convertView);
				}

			} else
			{
				convertView = addMainView_new(arg0, convertView);
			}

		} else
		{

			if (arg0 == 0)
			{
				clear();
				/**
				 * 这个是viewpager的布局 3.5版本进行更改 一张大图和两张小图的布局文件
				 */
				if (list != null && list.size() != 0)
				{
					if (list.get(0).getIsContainActivtiyAdv() == 1)
					{
						// 表示有广告
						convertView = addFristView(arg0, convertView);
					} else
					{
						convertView = addNull(arg0, convertView);
					}
				} else
				{
					convertView = addNull(arg0, convertView);
				}
			} else
			{
				convertView = addNull(arg0, convertView);
			}

		}
		return convertView;
		// /**
		// * 这个是viewpager的布局 3.5版本进行更改 这是顶部文件
		// */
		// MyHandler mMyHandler = null;
		// /**
		// * 这个是横向的listview 和两个imageview 的布局
		// */
		// if (convertView == null) {
		// mMyHandler = new MyHandler();
		// convertView = (LinearLayout) View.inflate(mContext,
		// R.layout.index_tab_host, null);
		// mMyHandler.mIvAddType = (ImageView) convertView
		// .findViewById(R.id.activity_add_type);
		// mMyHandler.mTvWeek = (TextView) convertView
		// .findViewById(R.id.activity_week_tv);
		// mMyHandler.mTvMap = (TextView) convertView
		// .findViewById(R.id.activity_map_tv);
		// mMyHandler.mCount = (TextView) convertView
		// .findViewById(R.id.tab_count);
		// convertView.setTag(mMyHandler);
		// } else {
		// //
		// mMyHandler = (MyHandler) convertView.getTag();
		// }
		// if (count == 0) {
		// mMyHandler.mCount.setVisibility(View.GONE);
		// } else {
		// mMyHandler.mCount.setVisibility(View.VISIBLE);
		// mMyHandler.mCount.setText(count + "");
		// }
		// // if(MyApplication.getInstance().getmTabHandler()!=null){
		// //
		// // }
		// new ActivityTabHandler(mContext, convertView, new TabCallback() {
		//
		// @Override
		// public void setTab(UserBehaviorInfo info) {
		// // TODO Auto-generated method stub
		// Message msg = new Message();
		// msg.obj = info;
		// msg.arg1 = 1001;
		// MyApplication.getInstance().getActivityHandler()
		// .sendMessage(msg);
		// }
		// });
		// mMyHandler.mIvAddType.setOnClickListener(this);
		// mMyHandler.mTvWeek.setOnClickListener(this);
		// mMyHandler.mTvMap.setOnClickListener(this);

		// MyHandler mMyHandler = null;
		//
		// if (topView == null) {
		// mMyHandler = new MyHandler();
		// topView = (LinearLayout) View.inflate(mContext,
		// R.layout.adapter_index_tab_host, null);
		// mMyHandler.top_layout = (SlideShowView) topView
		// .findViewById(R.id.slideshowView);
		// topView.setTag(mMyHandler);
		// } else {
		// mMyHandler = (MyHandler) topView.getTag();
		// }
		// mMyHandler.top_layout.setContianOnEvent(topView);
		// return topView;
		// else {
		// MyHandler mMyHandler = null;
		// /**
		// * 这个是横向的listview 和两个imageview 的布局
		// */
		// if (convertView == null) {
		// mMyHandler = new MyHandler();
		// convertView = (LinearLayout) View.inflate(mContext,
		// R.layout.index_tab_host, null);
		// mMyHandler.mIvAddType = (ImageView) convertView
		// .findViewById(R.id.activity_add_type);
		// mMyHandler.mTvWeek = (TextView) convertView
		// .findViewById(R.id.activity_week_tv);
		// mMyHandler.mTvMap = (TextView) convertView
		// .findViewById(R.id.activity_map_tv);
		// mMyHandler.mCount = (TextView) convertView
		// .findViewById(R.id.tab_count);
		// convertView.setTag(mMyHandler);
		// } else {
		// mMyHandler = (MyHandler) convertView.getTag();
		// }
		// if (count == 0) {
		// mMyHandler.mCount.setVisibility(View.GONE);
		// } else {
		// mMyHandler.mCount.setVisibility(View.VISIBLE);
		// mMyHandler.mCount.setText(count + "");
		// }
		// // if(MyApplication.getInstance().getmTabHandler()!=null){
		// //
		// // }
		// new ActivityTabHandler(mContext, convertView, new TabCallback() {
		//
		// @Override
		// public void setTab(UserBehaviorInfo info) {
		// // TODO Auto-generated method stub
		// Message msg = new Message();
		// msg.obj = info;
		// msg.arg1 = 1001;
		// MyApplication.getInstance().getActivityHandler()
		// .sendMessage(msg);
		// }
		// });
		// mMyHandler.mIvAddType.setOnClickListener(this);
		// mMyHandler.mTvWeek.setOnClickListener(this);
		// mMyHandler.mTvMap.setOnClickListener(this);
		// return convertView;
		// }

	}

	/**
	 * 每隔几行的imageview
	 * 
	 * @param arg0
	 * @param convertView
	 * @return
	 */
	private View addBannerView(int arg0, View convertView)
	{

		if (convertView == null)
		{
			vha = new ViewHolder_Advertisement();
			convertView = View.inflate(mContext, R.layout.adapter_event_advertisement_list_item, null);
			vha.img = (ImageView) convertView.findViewById(R.id.img);
			convertView.setTag(vha);
		} else
		{
			vha = (ViewHolder_Advertisement) convertView.getTag();
		}
		// banner_pos = arg0;
		int position = arg0 % 4;
		// if (position >= list_banner.size()) {
		//
		// } else {
		org.json.JSONArray json_arr = list_banner.get(0).getList();

		try
		{
			String img_url = json_arr.getJSONObject(position).getString("advertImgUrl");
			final String advert_Url = json_arr.getJSONObject(position).getString("advertUrl");

			if (img_url != "" && img_url != null)
			{
				vha.img.setVisibility(View.VISIBLE);
				MyApplication.getInstance().setImageView(MyApplication.getContext(), TextUtil.getUrlSmall_750_250(img_url), vha.img);
				vha.img.setOnClickListener(new OnClickListener()
				{

					@Override
					public void onClick(View arg0)
					{
						Intent intent = new Intent();
						intent.setClass(mContext, BannerWebView.class);
						intent.putExtra("url", advert_Url);
						mContext.startActivity(intent);
					}
				});

			}

		} catch (Exception e)
		{
			e.printStackTrace();
		}

		// }

		return convertView;
	}

	private View addNull(int arg0, View convertView)
	{
		Log.i("ceshi", "空了 ===  " + arg0);
		convertView = View.inflate(mContext, R.layout.adapter_event_null_list_item, null);

		return convertView;
	}

	private View addFristView(int arg0, View convertView)
	{
		if (convertView == null)
		{
			vhf = new ViewHolder_Frist();
			convertView = View.inflate(mContext, R.layout.adapter_event_frist_list_item, null);
			vhf.big_img = (ImageView) convertView.findViewById(R.id.big_img);
			vhf.small_img_left = (ImageView) convertView.findViewById(R.id.small_img_left);
			vhf.small_img_right = (ImageView) convertView.findViewById(R.id.small_img_right);
			convertView.setTag(vhf);
		} else
		{
			vhf = (ViewHolder_Frist) convertView.getTag();
		}
		String big_img = list_banner.get(arg0).getAdvBannerFImgUrl();
		String left_img = list_banner.get(arg0).getAdvBannerSImgUrl();
		String right_img = list_banner.get(arg0).getAdvBannerLImgUrl();
		/**
		 * 这个是点击的url 参数
		 */
		final String advBannerFUrl = list_banner.get(arg0).getAdvBannerFUrl();
		final String advBannerSUrl = list_banner.get(arg0).getAdvBannerSUrl();
		final String advBannerLUrl = list_banner.get(arg0).getAdvBannerLUrl();
		/**
		 * 判断是否是内链还是外链 0 表示为内链 1表示为外链
		 */
		final int advBannerFIsLink = list_banner.get(arg0).getAdvBannerFIsLink();
		final int advBannerLIsLink = list_banner.get(arg0).getAdvBannerLIsLink();
		final int advBannerSIsLink = list_banner.get(arg0).getAdvBannerSIsLink();
		/**
		 * 判断内链类型 0 活动列表 1活动详情 2 场馆列表 3 场馆详情
		 */
		final int advBannerFLinkType = list_banner.get(arg0).getAdvBannerFLinkType();
		final int advBannerLLinkType = list_banner.get(arg0).getAdvBannerLLinkType();
		final int advBannerSLinkType = list_banner.get(arg0).getAdvBannerSLinkType();

		if (big_img != "" && big_img != null && left_img != "" && left_img != null && right_img != "" && right_img != null)
		{
			// mHolder.big_img.setBackgroundResource(R.drawable.ic_launcher);
			// MyApplication.getInstance().getImageLoader()
			// .displayImage(big_img, mHolder.big_img);

			MyApplication.getInstance().setImageView(MyApplication.getContext(), TextUtil.getUrlSmall_750_250(big_img), vhf.big_img);
			MyApplication.getInstance().setImageView(MyApplication.getContext(), TextUtil.getUrlSmall_748_310(left_img), vhf.small_img_left);
			MyApplication.getInstance().setImageView(MyApplication.getContext(), TextUtil.getUrlSmall_748_310(right_img), vhf.small_img_right);
		}
		vhf.big_img.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View arg0)
			{

				if (advBannerFIsLink == 0)
				{
					MyApplication.selectImg(mContext, advBannerFLinkType, advBannerFUrl);
				} else
				{
					MyApplication.selectWeb(mContext, advBannerFUrl);
				}
			}

		});
		vhf.small_img_left.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View arg0)
			{
				if (advBannerSIsLink == 0)
				{
					MyApplication.selectImg(mContext, advBannerSLinkType, advBannerSUrl);
				} else
				{
					MyApplication.selectWeb(mContext, advBannerSUrl);
				}

			}
		});

		vhf.small_img_right.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View arg0)
			{
				if (advBannerLIsLink == 0)
				{
					MyApplication.selectImg(mContext, advBannerLLinkType, advBannerLUrl);
				} else
				{
					MyApplication.selectWeb(mContext, advBannerLUrl);
				}

			}
		});

		return convertView;
	}

	/**
	 * 这个是ViewHolder
	 * 
	 * @author wenff
	 * 
	 */
	private class MyHandler
	{
		private SlideShowView top_layout;
		private HorizontalListView mHorListView;
		private ImageView mIvAddType;
		private TextView mTvWeek;
		private TextView mTvMap;
		private TextView mCount;
	}

	private String getActivityPast = "";

	class ViewHolder
	{
		TextView shopping_areas;
		TextView preface;
		TextView sieve;
		LinearLayout sieve_ll;
		ImageView img;
		TextView item_price_danwei;
		TextView item_price;
		TextView item_subject;
		TextView item_subject1;
		TextView item_subject2;
		TextView title;
		TextView time;
		ImageView type;
		ImageView label;
		ImageView yupiao;
		TextView address;
		TextView value;
		TextView tiketcount;
		LinearLayout item_toplayout;
		LinearLayout tiketcountLayout;
		RelativeLayout item_mapaddress;
		TextView foot;
	}

	class ViewHolder_Frist
	{
		ImageView big_img;
		ImageView small_img_left;
		ImageView small_img_right;
	}

	class ViewHolder_Advertisement
	{
		ImageView img;
	}

	private void onclick(View arg0)
	{
		if (ButtonUtil.isDelayClick())
		{
			EventInfo eventInfo = (EventInfo) arg0.getTag();
			Intent intent = new Intent(mContext, ActivityDetailActivity.class);
			// Intent intent = new Intent(mContext, EventDetailsActivity.class);
			/**
			 * 这里我写死ID
			 */
			intent.putExtra("eventId", eventInfo.getEventId());
			MyApplication.currentCount = -1;
			MyApplication.spike_Time = -1;
			MyApplication.total_availableCount = -1;
			// intent.putExtra("eventId", "5b9a7960ee994ca29ca4e0958e9c61bc");
			mContext.startActivity(intent);
		}
	}

	private void onTabOnclick(View view)
	{
		if (ButtonUtil.isDelayClick())
		{
			switch (view.getId())
			{
			case R.id.tab_nebaty_btn:
				onclickindex = 0;
				break;
			case R.id.tab_start_btn:
				onclickindex = 1;
				break;
			case R.id.tab_hot_btn:
				onclickindex = 2;
				break;
			default:
				break;
			}
		}
	}

	private float index_X, index_Y;

	@Override
	public boolean onTouch(View arg0, MotionEvent arg1)
	{
		// TODO Auto-generated method stub
		switch (arg1.getAction())
		{
		case MotionEvent.ACTION_DOWN:
			index_X = arg1.getRawX();
			index_Y = arg1.getRawY();
			break;
		case MotionEvent.ACTION_UP:
			if (Math.abs(arg1.getRawY() - index_Y) < 20)
			{
				if (Math.abs(arg1.getRawX() - index_X) < 20)
				{
					if (arg0.getId() == R.id.tab_nebaty_btn | arg0.getId() == R.id.tab_start_btn | arg0.getId() == R.id.tab_hot_btn)
					{
						onTabOnclick(arg0);
					} else
					{
						onclick(arg0);
					}

				}
			}

			break;

		default:
			break;
		}
		return true;
	}

	@Override
	public void onClick(View arg0)
	{
		// TODO Auto-generated method stub
		Intent intent = null;
		switch (arg0.getId())
		{
		case R.id.activity_week_tv:
			activityCount(0);
			notifyDataSetChanged();
			intent = new Intent(mContext, ThisWeekActivity.class);
			mContext.startActivity(intent);
			break;
		case R.id.activity_map_tv:
			intent = new Intent(mContext, ActivityMap.class);
			intent.putExtra("ListType", "1");
			intent.putExtra("Activity_themeId", Activity_themeId);
			intent.putExtra("Activity_typeId", Activity_typeId);
			intent.putExtra("Activity_themetype", Activity_theme);
			intent.putExtra("Activity_typetype", Activity_type);
			mContext.startActivity(intent);
			break;
		case R.id.activity_add_type:
			intent = new Intent(mContext, MyLoveActivity.class);
			mContext.startActivity(intent);
			break;
		case R.id.shopping_areas:
			af.showPopuwindow(1, 1, filterLayout);
			break;
		case R.id.preface:
			af.showPopuwindow(2, 1, filterLayout);
			break;
		case R.id.sieve:
			af.showPopuwindow(3, 1, filterLayout);
			break;
		default:
			break;
		}

	}

	public void clear()
	{
		vhf = null;
		vha = null;
		vh = null;
	}

	public void setSieveVisible(int num, String text)
	{
		// 1 商区 2 智能排序 3 免费与否
		if (num == 1)
		{
			shopping_areas = text;

			shopping_areas_tv.setText(text);

		} else if (num == 2)
		{

			preface_tv.setText(text);
			preface = text;
		} else if (num == 3)
		{
			sieve_tv.setText(text);
			sieve = text;
		} else
		{

		}
		frist_num = 0;
		notifyDataSetChanged();
		// vh.sieve_ll.setVisibility(View.VISIBLE);
	}

	public void setSieveGone()
	{
		frist_num = 1;
		notifyDataSetChanged();
	}

	class ViewHolder_Main
	{
		String activityId;
		TextView distance_tv;

		ImageView img;
		ImageView top_left_img;
		TextView buttom_right_tv;
		TextView name_tv;
		TextView address_tv;
		TextView item_subject;
		TextView item_subject1;
		TextView item_subject2;
		LinearLayout ll;
		// LinearLayout sieve_ll;

	}
}
