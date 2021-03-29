package com.honor.cityselectdemo.CitySelect;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.honor.cityselectdemo.CitySelect.adapter.CityListAdapter;
import com.honor.cityselectdemo.CitySelect.bean.AreasBean;
import com.honor.cityselectdemo.CitySelect.bean.City;
import com.honor.cityselectdemo.CitySelect.bean.CityPickerBean;
import com.honor.cityselectdemo.CitySelect.bean.LocateState;
import com.honor.cityselectdemo.CitySelect.util.GsonUtil;
import com.honor.cityselectdemo.CitySelect.util.PinyinUtils;
import com.honor.cityselectdemo.CitySelect.util.ReadAssetsFileUtil;
import com.honor.cityselectdemo.CitySelect.widget.SideLetterBar;
import com.honor.cityselectdemo.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;

public class CityPickerActivity extends FragmentActivity {
    private ListView mListView;
    private SideLetterBar mLetterBar;
    private CityListAdapter mCityAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cp_activity_city_list);
        initView();
        initData();
//        getLocation();
    }

    protected void initView() {
        mListView = findViewById(R.id.listview_all_city);
        TextView overlay = findViewById(R.id.tv_letter_overlay);
        mLetterBar = findViewById(R.id.side_letter_bar);
        mLetterBar.setOverlay(overlay);
        mLetterBar.setOnLetterChangedListener(new SideLetterBar.OnLetterChangedListener() {
            @Override
            public void onLetterChanged(String letter) {
                int position = mCityAdapter.getLetterPosition(letter);
                mListView.setSelection(position);
            }
        });
        mCityAdapter = new CityListAdapter(this);
        mListView.setAdapter(mCityAdapter);
    }

    public void getCityData() {
        String json = ReadAssetsFileUtil.getJson(this, "city.json");
        CityPickerBean bean = GsonUtil.getBean(json, CityPickerBean.class);
        HashSet<City> citys = new HashSet<>();
        for (AreasBean areasBean : bean.data.areas) {
            String name = areasBean.name.replace("　", "");
            citys.add(new City(areasBean.id, name, PinyinUtils.getPinYin(name), areasBean.is_hot == 1));
            for (AreasBean.ChildrenBeanX childrenBeanX : areasBean.children) {
                citys.add(new City(childrenBeanX.id, childrenBeanX.name, PinyinUtils.getPinYin(childrenBeanX.name), childrenBeanX.is_hot == 1));
            }
        }
        //set转换list
        ArrayList<City> cities = new ArrayList<>(citys);
        //按照字母排序
        Collections.sort(cities, new Comparator<City>() {
            @Override
            public int compare(City city, City t1) {
                return city.getPinyin().compareTo(t1.getPinyin());
            }
        });
        mCityAdapter.setData(cities);
    }

    protected void initData() {
        getCityData();
        mCityAdapter.setOnCityClickListener(new CityListAdapter.OnCityClickListener() {
            @Override
            public void onCityClick(String name) {//选择城市
                Toast.makeText(CityPickerActivity.this, name, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLocateClick() {//点击定位按钮
//                mCityAdapter.updateLocateState(LocateState.LOCATING, null);
            }
        });
    }






}
