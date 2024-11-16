package cn.edu.ustc.timeflow.ui

import android.app.UiModeManager
import android.content.Intent
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import cn.edu.ustc.MainActivity
import cn.edu.ustc.timeflow.model.StandardScheduler
import cn.edu.ustc.timeflow.model.StandardValuer
import cn.edu.ustc.timeflow.util.DBHelper
import cn.edu.ustc.timeflow.util.SharedPreferenceHelper
import com.example.timeflow.R
import java.time.LocalDateTime
import java.util.Random

class HelloActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hello)

        val largeIcon = findViewById<ImageView>(R.id.large_icon)
        val sloganText = findViewById<TextView>(R.id.slogan_text)
        sloganText.typeface = Typeface.createFromAsset(assets, "font/GenYoMinTW-Medium.ttf")

        // Set a random slogan
        val randomSlogan = randomSlogan
        sloganText.text = randomSlogan

        // Delay and start MainActivity
        Handler().postDelayed({
            val intent = Intent(this@HelloActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 1500) // 3 seconds delay

        // 切换为浅色模式
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val uiModeManager = getSystemService(UI_MODE_SERVICE) as UiModeManager
            uiModeManager.setApplicationNightMode(UiModeManager.MODE_NIGHT_NO)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        // 初次使用
        if (!SharedPreferenceHelper.getBoolean(this, "notFirst", false)) {
            SharedPreferenceHelper.saveBoolean(this, "notFirst", true)

            // 读取系统语言， 生成对应示例数据
            if (resources.configuration.locale.language == "zh") {
                DBHelper(this).generateSample_zh()
            } else {
                DBHelper(this).generateSample_en()
            }

            val standardScheduler = StandardScheduler(this, StandardValuer(this))
            standardScheduler.Schedule(LocalDateTime.now(), LocalDateTime.now().plusDays(7))

            val permissions = arrayOf(
                android.Manifest.permission.INTERNET,
                android.Manifest.permission.POST_NOTIFICATIONS,
                android.Manifest.permission.SCHEDULE_EXACT_ALARM,
                android.Manifest.permission.SET_ALARM,
                android.Manifest.permission.RECEIVE_BOOT_COMPLETED
            )
            requestPermissions(permissions, 0)
        }
    }

    private val randomSlogan: String
        get() {
            val random = Random()
            val index = random.nextInt(SLOGANS.size)
            return SLOGANS[index]
        }

    companion object {
        private val SLOGANS = arrayOf(
            "人生天地之间\n若白驹过隙\n忽然而已",
            "逝者如斯夫，不舍昼夜",
            "早安，卷王",
            "科·里·科·气",
            "既入我科，勿负韶华",
            "圣人不贵尺之壁\n而重寸之阴\n时难得而易失也",
            "惊风飘白日\n光景西驰流",
            "志士惜日短\n愁人知夜长",
            "人寿几何？逝如朝霜\n时无重至，华不再阳",
            "盛年不重来\n一日难再晨\n及时当勉励\n岁月不待人",
            "一年之计在于春\n一日之计在于晨",
            "冬者岁之余\n夜者日之余\n阴雨者时之余",
            "失之东隅，收之桑榆",
            "光景不待人\n须叟发成丝",
            "东隅已逝，桑榆非晚",
            "天波易谢，寸暑难留",
            "岁去弦吐箭\n忧来蚕抽纶",
            "光阴似箭，日月如梭",
            "年难留，时易损",
            "天可补\n海可填\n南山可移\n日月既往，不可复追",
            "勿谓寸阴短，既过难再获\n勿谓一丝微，既绍难再白",
            "志士惜年\n贤人惜日\n圣人惜时",
            "机会无限\n但时间不是"
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


        )
    }
}