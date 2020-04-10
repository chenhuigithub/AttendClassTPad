package com.example.attendclasstpad.aty;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.attendclasstpad.R;
import com.example.attendclasstpad.util.ConstantsUtils;

/**
 * 图片视图
 *
 * @author chenhui 2019.03.27
 */
public class PicViewActivity extends Activity {
    ImageView ivPic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_aty_image);

        ivPic = (ImageView) findViewById(R.id.iv_layout_aty_image);
        ivPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        dealWithExtras();
    }

    /**
     * 处理接手过来的数据
     */
    private void dealWithExtras() {
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            return;
        }

        // 取出大图路径
        String path = bundle.getString(ConstantsUtils.PATH);
        // 显示图片
        Glide.with(this).load(path).error(R.drawable.no_data).into(ivPic);
    }

}
