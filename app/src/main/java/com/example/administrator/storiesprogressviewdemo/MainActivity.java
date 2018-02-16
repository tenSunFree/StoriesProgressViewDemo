package com.example.administrator.storiesprogressviewdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import jp.shts.android.storiesprogressview.StoriesProgressView;

public class MainActivity extends AppCompatActivity implements StoriesProgressView.StoriesListener {

    private static final int PROGRESS_COUNT = 4;

    private StoriesProgressView storiesProgressView;
    private ImageView image;

    private int counter = 0;
    private final int[] resources = new int[]{
            R.drawable.sloth,
            R.drawable.persian_cat2,
            R.drawable.raccoon2,
            R.drawable.dwarf_rabbit2
    };

    private final long[] durations = new long[]{
            2000L, 2000L, 2000L, 2000L
    };

    long pressTime = 0L;
    long limit = 500L;

    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {

                case MotionEvent.ACTION_DOWN:
                    pressTime = System.currentTimeMillis();                                             // 从1970年1月1日到现在的毫秒数
                    storiesProgressView.pause();                                                    // 將進度跳到下一個部分
                    return false;

                case MotionEvent.ACTION_UP:

                    /** 如果按太久 將得到return true, 表示什麼都不做; 反之, 則會執行相關方法 */
                    long now = System.currentTimeMillis();                                              // 从1970年1月1日到现在的毫秒数
                    storiesProgressView.resume();                                                   // 將進度返回到上一個部分
                    return limit < now - pressTime;                                                 // 判斷是否按太久
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);                         // 设置全屏
        setContentView(R.layout.activity_main);

        storiesProgressView = (StoriesProgressView) findViewById(R.id.stories);
        storiesProgressView.setStoriesCount(PROGRESS_COUNT);
        storiesProgressView.setStoriesCountWithDurations(durations);
        storiesProgressView.setStoriesListener(this);
        storiesProgressView.startStories();

        image = (ImageView) findViewById(R.id.image);
        image.setImageResource(resources[counter]);

        // bind reverse view
        View reverse = findViewById(R.id.reverse);
        reverse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storiesProgressView.reverse();
            }
        });
        reverse.setOnTouchListener(onTouchListener);

        // bind skip view
        View skip = findViewById(R.id.skip);
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storiesProgressView.skip();
            }
        });
        skip.setOnTouchListener(onTouchListener);
    }

    @Override
    public void onNext() {
        image.setImageResource(resources[++counter]);
    }

    @Override
    public void onPrev() {

        if ((counter - 1) < 0) return;
        image.setImageResource(resources[--counter]);
    }

    @Override
    public void onComplete() {
    }

    @Override
    protected void onDestroy() {
        // Very important !
        storiesProgressView.destroy();
        super.onDestroy();
    }
}
