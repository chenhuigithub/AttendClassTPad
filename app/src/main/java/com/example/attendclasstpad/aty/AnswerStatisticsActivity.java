package com.example.attendclasstpad.aty;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.example.attendclasstpad.R;

/**
 * 答题统计
 *
 * @author chenhui 2019.04.12
 */
public class AnswerStatisticsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        setContentView(R.layout.layout_aty_answer_statistics);

        // 关闭页面
        RelativeLayout rlClose = (RelativeLayout) findViewById(R.id.rl_close_layout_aty_answer_statistics);
        rlClose.setOnClickListener(new Listeners());
    }

    private class Listeners implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.rl_close_layout_aty_answer_statistics:// 关闭页面
                    finish();
                    break;
            }
        }
    }

}
