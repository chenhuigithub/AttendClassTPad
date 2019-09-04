package com.example.attendclasstpad.aty;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.example.attendclasstpad.R;

/**
 * @author chenhui 2018.06.12
 */
public class LoginActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_aty_login);

        // 登录
        TextView tvLogin = (TextView) findViewById(R.id.tv_login_layout_aty_login);
        tvLogin.setOnClickListener(new Listeners());

        // 记住密码选择框
        final CheckBox cbox = (CheckBox) findViewById(R.id.cbox_remember_layout_aty_login);
        cbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
                if (isChecked) {
                    cbox.setChecked(true);
                } else {
                    cbox.setChecked(false);
                }

            }
        });
    }

    /**
     * 监听
     *
     * @author chenhui
     */
    private class Listeners implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.tv_login_layout_aty_login:
                    // 跳转至选择班级界面
                    Intent intent = new Intent(LoginActivity.this,
                            ChoiceClassActivity.class);
                    startActivity(intent);
                    finish();
                    break;
            }

        }

    }

}
