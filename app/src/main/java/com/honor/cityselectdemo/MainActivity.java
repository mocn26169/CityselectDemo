package com.honor.cityselectdemo;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.github.promeg.pinyinhelper.Pinyin;
import com.github.promeg.pinyinhelper.PinyinMapDict;
import com.github.promeg.tinypinyin.lexicons.android.cncity.CnCityDict;
import com.honor.cityselectdemo.CitySelect.CityPickerActivity;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 添加中文城市词典
        Pinyin.init(Pinyin.newConfig().with(CnCityDict.getInstance(this)));

        // 添加自定义词典
        Pinyin.init(Pinyin.newConfig()
                .with(new PinyinMapDict() {
                    @Override
                    public Map<String, String[]> mapping() {
                        HashMap<String, String[]> map = new HashMap<String, String[]>();
                        map.put("长治",  new String[]{"CHANG", "ZHI"});
                        map.put("长沙",  new String[]{"CHANG", "SHA"});
                        map.put("长春",  new String[]{"CHANG", "CHUN"});
                        map.put("重庆",  new String[]{"CHONG", "QING"});

                        return map;
                    }
                }));
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, CityPickerActivity.class));
            }
        });


    }
}
