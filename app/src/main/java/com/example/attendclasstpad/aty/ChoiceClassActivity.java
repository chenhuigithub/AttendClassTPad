package com.example.attendclasstpad.aty;

import java.util.ArrayList;
import java.util.List;

import com.example.attendclasstpad.R;
import com.example.attendclasstpad.adapter.ClassNameAdapter;
import com.example.attendclasstpad.model.Classes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

/**
 * 选择班级
 *
 * @author chenhui 2018.06.12
 */
public class ChoiceClassActivity extends Activity {
    private ClassNameAdapter adapter;// 班级名称列表适配器
    private List<Classes> classList;// 班级列表

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_aty_choice_class);

        GridView gdvClass = (GridView) findViewById(R.id.gdv_layout_aty_choice_class);
        gdvClass.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Intent intent = new Intent(ChoiceClassActivity.this,
                        MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // 测试数据，chenhui 2018.06.13
        classList = new ArrayList<Classes>();
        Classes classes = new Classes();
        classList.add(classes);
        classList.add(classes);
        classList.add(classes);
        classList.add(classes);
        classList.add(classes);
        classList.add(classes);
        classList.add(classes);
        classList.add(classes);
        classList.add(classes);

        adapter = new ClassNameAdapter(this, classList);
        gdvClass.setAdapter(adapter);
    }

}
