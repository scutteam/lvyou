package com.scutteam.lvyou.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.scutteam.lvyou.R;
import com.scutteam.lvyou.activity.MakeJourneyActivity;
import com.scutteam.lvyou.activity.SimpleHUD.PairProgressHUD;
import com.scutteam.lvyou.adapter.MainFragmentItemAdapter;
import com.scutteam.lvyou.application.LvYouApplication;
import com.scutteam.lvyou.constant.Constants;
import com.scutteam.lvyou.interfaces.ThemeListener;
import com.scutteam.lvyou.model.Advert;
import com.scutteam.lvyou.model.LvYouDest;
import com.scutteam.lvyou.model.LvYouTheme;
import com.scutteam.lvyou.util.DensityUtil;
import com.scutteam.lvyou.widget.me.maxwin.view.XListView;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainFragment extends Fragment implements XListView.IXListViewListener {

    private View view;
    private static MainFragment instance;
    private View mHeaderView;
    private XListView mListView;
    private ViewPager mViewPager;
    public static final int LOAD_VIEW_PAGER_DATA_SUCCESS = 100000;
    public static final int LOAD_LIST_VIEW_DATA_SUCCESS = 100001;
    public static final int LOAD_DATA_FAIL = 100002;
    public static final int REFRESH_VIEW_PAGER_SUCCESS = 100003;
    public static final int START_SCROLL_BANNER = 100004;
    public static final int AUTO_SCROLL_BANNER = 100005;
    public MainFragmentItemAdapter adapter;
    private PagerAdapter pagerAdapter;
    private List<Advert> advertList = new ArrayList<Advert>();
    private List<ImageView> advertImageList = new ArrayList<ImageView>();
    private LinearLayout mLlViewPagerLayout;
    private ThemeListener listener;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case LOAD_VIEW_PAGER_DATA_SUCCESS:
                    setViewPagerUI();
                    //viewPager数据加载完毕,加到listview的headView中
                    mListView.addHeaderView(mHeaderView);
                    PairProgressHUD.dismiss();
                    break;
                case LOAD_LIST_VIEW_DATA_SUCCESS:
                    if (adapter == null) {
                        initAdapter();
                    } else {
                        Log.i("liujie","wocao");
                        refreshAdapter();
                    }
                    break;
                case LOAD_DATA_FAIL:
                    Toast.makeText(getActivity(), "网络异常,请重试", Toast.LENGTH_SHORT).show();
                    break;
                case REFRESH_VIEW_PAGER_SUCCESS:

                    break;
                case START_SCROLL_BANNER:
                    handler.removeMessages(AUTO_SCROLL_BANNER);
                    this.sendEmptyMessageDelayed(AUTO_SCROLL_BANNER, 3000);
                    break;
                case AUTO_SCROLL_BANNER:
                    int totalBannerSize = advertList.size();
                    int currentItem = mViewPager.getCurrentItem();
                    if (advertList.size() <= 1) {
                        mViewPager.setCurrentItem(0);
                    } else {
                        mViewPager.setCurrentItem((currentItem + 1) % totalBannerSize);
                    }
                    //每三秒钟发送一个message，用于切换viewPager中的图片
                    this.sendEmptyMessageDelayed(AUTO_SCROLL_BANNER, 3000);
                    break;
                default:
                    break;
            }
        }
    };
    private List<LvYouDest> destList = new ArrayList<LvYouDest>();
    private List<LvYouTheme> themeList = new ArrayList<LvYouTheme>();
    private List<Integer>themeDestCountList = new ArrayList<Integer>();

    public static MainFragment getInstance() {
        if (instance == null) {
            instance = new MainFragment();
        }
        return instance;
    }

    public void refreshAdapter() {
        adapter.refreshWithThemeListAndDestList(themeList, destList);
        mListView.stopRefresh();
    }

    @Override
    public void onDestroy() {

        handler.removeMessages(AUTO_SCROLL_BANNER);
        handler.removeMessages(START_SCROLL_BANNER);

        super.onDestroy();
    }

    public void setViewPagerUI() {
        for (int i = 0; i < advertList.size(); i++) {
            //ImageView imageView = (ImageView) LayoutInflater.from(getActivity()).inflate(R.layout.view_pager_item,null).findViewById(R.id.iv_item);
            ImageView imageView = new ImageView(getActivity());
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            imageView.setLayoutParams(params);
            imageView.setAdjustViewBounds(true);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);

            ImageLoader.getInstance().displayImage(advertList.get(i).pic, imageView);
            advertImageList.add(imageView);

            ImageView page_view_indicator_image_view = new ImageView(getActivity());
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.rightMargin = 10;
            page_view_indicator_image_view.setImageDrawable(getResources().getDrawable(R.mipmap.tagvewpager_point01));
            page_view_indicator_image_view.setLayoutParams(layoutParams);
            mLlViewPagerLayout.addView(page_view_indicator_image_view);
        }
        pagerAdapter = new PagerAdapter() {
            @Override
            public int getCount() {
                return advertList.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                ((ViewPager) container).addView(advertImageList.get(position));
                return advertImageList.get(position);
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                ((ViewPager) container).removeView(advertImageList.get(position));
            }
        };
        mViewPager.setAdapter(pagerAdapter);
        setSelect(0);

        handler.sendEmptyMessage(AUTO_SCROLL_BANNER);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_main, null);
            initView();
            initHeaderViewAndListViewData();
            initListener();
        }
//        else {
//           for(int i = 0 ; i <themeDestCountList.size() ;i++) {
//               themeList.get(i).dest_num = themeDestCountList.get(i);
//           }
//            adapter.notifyDataSetChanged();
//        }
        return view;
    }

    public void initHeaderViewAndListViewData() {
        mHeaderView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_main_head_view, null);
        mViewPager = (ViewPager) mHeaderView.findViewById(R.id.viewPager);
        RelativeLayout.LayoutParams param = (RelativeLayout.LayoutParams) mViewPager.getLayoutParams();
        param.width = DensityUtil.getScreenWidthPx(LvYouApplication.getInstance());
        param.height = param.width * Constants.Config.IMAGE_HEIGHT / Constants.Config.IMAGE_WIDTH;
        mViewPager.setLayoutParams(param);
        mLlViewPagerLayout = (LinearLayout) mHeaderView.findViewById(R.id.ll_view_pager_layout);

        PairProgressHUD.showLoading(getActivity(),"请稍等");
        initViewPagerData();
        initListViewData();
    }

    public void initListViewData() {
        initThemeData();//等theme的数据获取完后 获取dest的数据
    }

    public void initThemeData() {
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("user-agent", "Android");
        client.get(getActivity(), Constants.URL + "main/theme.home_list.json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    JSONArray dataArray = response.getJSONArray("data");
                    themeList = LvYouTheme.insertWithArray(dataArray);
                    
                    for(int i = 0 ; i < themeList.size() ; i++) {
                        LvYouTheme theme = themeList.get(i);
                        themeDestCountList.add(theme.dest_num);
                    }

                    initDestData();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);

                Message message = Message.obtain();
                message.what = LOAD_DATA_FAIL;
                handler.sendMessage(message);
            }
        });
    }

    public void initDestData() {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(getActivity(), Constants.URL + "main/dest.home_list.json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                try {
                    JSONArray dataArray = response.getJSONArray("data");
                    destList = LvYouDest.insertWithArray(dataArray);

                    Message message = Message.obtain();
                    message.what = LOAD_LIST_VIEW_DATA_SUCCESS;
                    handler.sendMessage(message);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);

                Message message = Message.obtain();
                message.what = LOAD_DATA_FAIL;
                handler.sendMessage(message);
            }
        });
    }

    public void initAdapter() {
        adapter = new MainFragmentItemAdapter(LvYouApplication.getInstance(), destList, themeList);
        mListView.setAdapter(adapter);
    }

    public void initViewPagerData() {
        //等api接口出来 实现这个部分 生成ViewPager数据，目前先用死数据

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(getActivity(), Constants.URL + "main/advert.mobile_list.json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                try {
                    int code = response.getInt("code");
                    if (code == 0) {
                        JSONArray dataArray = response.getJSONArray("data");
                        advertList = Advert.insertWithArray(dataArray);

                        if (advertList != null && advertList.size() > 0) {
                            handler.sendEmptyMessage(LOAD_VIEW_PAGER_DATA_SUCCESS);
                        }
                    } else {
                        Toast.makeText(getActivity(), response.getString("msg"), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }

    public void initView() {
        mListView = (XListView) view.findViewById(R.id.listView);
        mListView.setPullLoadEnable(false);
    }

    public void setListener(ThemeListener Llistener) {
        listener = Llistener;
    }

    public void initListener() {
        mListView.setXListViewListener(this);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setSelect(position);
                mViewPager.setCurrentItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mViewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                handler.sendEmptyMessage(START_SCROLL_BANNER);
                return false;
            }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int new_position = position - 1 - 1;
                if (new_position < themeList.size()) {
                    LvYouTheme theme = themeList.get(new_position); //减去一个viewpager 和一个xListviewHeader
                    listener.onThemeItenClick(theme);
                } else if (new_position >= themeList.size()) {
                    int dest_position = new_position - themeList.size();

                    Intent intent = new Intent();
                    intent.putExtra("destination_id", destList.get(dest_position).dest_id);
                    intent.setClass(getActivity(), MakeJourneyActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    public void setSelect(int position) {
        for (int i = 0; i < mLlViewPagerLayout.getChildCount(); i++) {
            ((ImageView) mLlViewPagerLayout.getChildAt(i)).setImageDrawable(getResources().getDrawable(R.mipmap.tagvewpager_point01));
        }
        ((ImageView) mLlViewPagerLayout.getChildAt(position)).setImageDrawable(getResources().getDrawable(R.mipmap.tagvewpager_point02));
    }

    @Override
    public void onRefresh() {
        refresh();
    }

    @Override
    public void onLoadMore() {
        mListView.stopLoadMore();
    }

    public void refresh() {
        refreshViewPagerData();
        refreshListViewData();
    }

    public void refreshViewPagerData() {

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(getActivity(), Constants.URL + "main/advert.mobile_list.json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                
                

                Log.e("response", response.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);

                Log.e("response_fail", responseString);
            }
        });

        Message message = Message.obtain();
        message.what = REFRESH_VIEW_PAGER_SUCCESS;
        handler.sendMessage(message);
    }

    public void refreshListViewData() {
        initListViewData();
    }
}
