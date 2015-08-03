package com.scutteam.lvyou.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.scutteam.lvyou.R;
import com.scutteam.lvyou.constant.Constants;
import com.scutteam.lvyou.util.sortlistview.CharacterParser;
import com.scutteam.lvyou.util.sortlistview.ClearEditText;
import com.scutteam.lvyou.util.sortlistview.PinyinComparator;
import com.scutteam.lvyou.util.sortlistview.SideBar;
import com.scutteam.lvyou.util.sortlistview.SortAdapter;
import com.scutteam.lvyou.util.sortlistview.SortModel;
import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SelectBeginPlaceActivity extends Activity {
    private ListView sortListView;
    private SideBar sideBar;
    private TextView dialog;
    private SortAdapter adapter;
    private ClearEditText mClearEditText;
    private Context mContext;
    private ArrayList<String> schools;

    /**
     * 汉字转换成拼音的类
     */
    private CharacterParser characterParser;
    private List<SortModel> SourceDateList;

    /**
     * 根据拼音来排列ListView里面的数据类
     */
    private PinyinComparator pinyinComparator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = SelectBeginPlaceActivity.this;
        setContentView(R.layout.activity_choose_begin_place);
        initViews();
    }

    private void initViews() {
        TextView title = (TextView) findViewById(R.id.center_text);
        TextView backIcon = (TextView) findViewById(R.id.left_icon);
        title.setText("学校列表");
        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectBeginPlaceActivity.this.finish();
            }
        });

        schools = new ArrayList<String>();

        //实例化汉字转拼音类
        characterParser = CharacterParser.getInstance();

        pinyinComparator = new PinyinComparator();

        sideBar = (SideBar) findViewById(R.id.sidrbar);
        dialog = (TextView) findViewById(R.id.dialog);
        sideBar.setTextView(dialog);

        //设置右侧触摸监听
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                //该字母首次出现的位置
                int position = adapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    sortListView.setSelection(position);
                }

            }
        });

        sortListView = (ListView) findViewById(R.id.country_lvcountry);
        sortListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (null != ((SortModel) adapter.getItem(position)).getName()){
                    Intent intent = new Intent(mContext, MakeJourneyActivity.class);
                    intent.putExtra("begin_place", ((SortModel) adapter.getItem(position)).getName());
                    ((Activity) mContext).setResult(MakeJourneyActivity.SBP_REQUEST_CODE, intent);
                    finish();
                }
            }
        });

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(mContext, Constants.URL + "main/place.gz_hot.json",
                new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if(0 == response.optInt("code")){
                    JSONArray datas = response.optJSONObject("data").optJSONArray("placeList");
                    for(int i = 0; i < datas.length(); i++){
                        try {
                            String showText = null;
                            JSONObject school = datas.getJSONObject(i);
                            String schoolName = school.optString("school");
                            if(null != schoolName) {
                                String schoolArea = school.optString("area");
                                if (null != schoolArea) {
                                    showText = schoolName + "(" + schoolArea +  ")";
                                }else{
                                    showText = schoolName;
                                }
                            }
                            schools.add(showText);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    ((Activity)mContext).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showSchools();
                        }
                    });
                }else{
                    Toast.makeText(mContext, response.optString("msg"), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Toast.makeText(mContext, "网络连接失败，请重试", Toast.LENGTH_SHORT).show();
            }
        });

        Toast.makeText(mContext, "数据加载中", Toast.LENGTH_SHORT).show();

        mClearEditText = (ClearEditText) findViewById(R.id.filter_edit);

        //根据输入框输入值的改变来过滤搜索
        mClearEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
                filterData(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        showSchools();
    }

    private void showSchools(){
        if(null != schools) {
            String[] temps = new String[schools.size()];
            for (int i = 0; i < schools.size(); i++) {
                temps[i] = schools.get(i);
            }
            SourceDateList = filledData(temps);

            // 根据a-z进行排序源数据
            Collections.sort(SourceDateList, pinyinComparator);
            adapter = new SortAdapter(this, SourceDateList);
            sortListView.setAdapter(adapter);
        }
    }
    /**
     * 为ListView填充数据
     *
     * @param date
     * @return
     */
    private List<SortModel> filledData(String[] date) {
        List<SortModel> mSortList = new ArrayList<SortModel>();

        for (int i = 0; i < date.length; i++) {
            SortModel sortModel = new SortModel();
            sortModel.setName(date[i]);
            //汉字转换成拼音
            String pinyin = characterParser.getSelling(date[i]);
            String sortString = pinyin.substring(0, 1).toUpperCase();

            // 正则表达式，判断首字母是否是英文字母
            if (sortString.matches("[A-Z]")) {
                sortModel.setSortLetters(sortString.toUpperCase());
            } else {
                sortModel.setSortLetters("#");
            }

            mSortList.add(sortModel);
        }
        return mSortList;

    }

    /**
     * 根据输入框中的值来过滤数据并更新ListView
     *
     * @param filterStr
     */
    private void filterData(String filterStr) {
        List<SortModel> filterDateList = new ArrayList<SortModel>();

        if (TextUtils.isEmpty(filterStr)) {
            filterDateList = SourceDateList;
        } else {
            filterDateList.clear();
            for (SortModel sortModel : SourceDateList) {
                String name = sortModel.getName();
                if (name.indexOf(filterStr.toString()) != -1 || characterParser.getSelling(name).startsWith(filterStr.toString())) {
                    filterDateList.add(sortModel);
                }
            }
        }

        // 根据a-z进行排序
        Collections.sort(filterDateList, pinyinComparator);
        adapter.updateListView(filterDateList);
    }

}

