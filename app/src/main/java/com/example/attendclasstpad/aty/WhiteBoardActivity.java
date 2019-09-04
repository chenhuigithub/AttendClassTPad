package com.example.attendclasstpad.aty;

import com.example.attendclasstpad.R;
import com.example.attendclasstpad.fg.TestQuestionsFg;
import com.example.attendclasstpad.view.CustomSurfaceView;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.LinearLayout;

/**
 * 画板(白板绘制)界面
 *
 * @author zhaochenhui_2018.05.25
 */
public class WhiteBoardActivity extends FragmentActivity {
    private CustomSurfaceView sfv;// 画板布局，surfaceView控件

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.board);
        sfv = (CustomSurfaceView) findViewById(R.id.sfv_board);// 画板

        /*
         * sfv.setZOrderMediaOverlay(true);
         * sfv.getHolder().setFormat(PixelFormat.TRANSLUCENT);
         */

        /*
         * sfv.setZOrderOnTop(true);
         * sfv.getHolder().setFormat(PixelFormat.TRANSLUCENT);
         */

    }

}
