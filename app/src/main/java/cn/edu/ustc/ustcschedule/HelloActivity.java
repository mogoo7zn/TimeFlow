package cn.edu.ustc.ustcschedule;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.timeflow.R;

import java.util.Random;

import cn.edu.ustc.MainActivity;

public class HelloActivity extends AppCompatActivity {

    private static final String[] SLOGANS = {
            "人生天地之间，若白驹过隙，忽然而已",
            "逝者如斯夫，不舍昼夜",
            "圣人不贵尺之壁而重寸之阴，时难得而易失也",
            "惊风飘白日，光景西驰流",
            "志士惜日短，愁人知夜长",
            "人寿几何？逝如朝霜。时无重至，华不再阳",
            "盛年不重来，一日难再晨；及时当勉励，岁月不待人",
            "一年之计在于春，一日之计在于晨",
            "冬者岁之余，夜者日之余，阴雨者时之余",
            "失之东隅，收之桑榆",
            "光景不待人，须叟发成丝",
            "东隅已逝，桑榆非晚",
            "天波易谢，寸暑难留",
            "岁去弦吐箭，忧来蚕抽纶",
            "光阴似箭，日月如梭",
            "年难留，时易损",
            "天可补，海可填，南山可移。日月既往，不可复追",
            "勿谓寸阴短，既过难再获。勿谓一丝微，既绍难再白",
            "志士惜年，贤人惜日，圣人惜时"
//            "Lost time is never found again.",
//            "Time flies over us, but leaves its shadow behind.",
//            "Time is what we want most, but what we use worst.",
//            "Time is the wisest counselor of all.",
//            "Time is the longest distance between two places.",
//            "Time is the school in which we learn, time is the fire in which we burn.",
//            "Time is a great healer, but a poor beautician.",
//            "Time is the coin of your life. You spend it. Do not allow others to spend it for you.",
//            "Time is the most valuable thing that we have, because it is the most irrevocable."
//            "Time is the friend of the wonderful company, the enemy of the mediocre.",
//            "Time is the only thief we can’t get justice against.",

    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hello);

        ImageView largeIcon = findViewById(R.id.large_icon);
        TextView sloganText = findViewById(R.id.slogan_text);
        sloganText.setTypeface(Typeface.createFromAsset(getAssets(), "font/GenYoMinTW-Medium.ttf"));

        // Set a random slogan
        String randomSlogan = getRandomSlogan();
        sloganText.setText(randomSlogan);

        // Delay and start MainActivity
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(HelloActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }, 1500); // 3 seconds delay
    }

    private String getRandomSlogan() {
        Random random = new Random();
        int index = random.nextInt(SLOGANS.length);
        return SLOGANS[index];
    }
}