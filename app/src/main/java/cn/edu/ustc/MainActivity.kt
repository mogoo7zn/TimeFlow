package cn.edu.ustc

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import cn.edu.ustc.timeflow.model.SimpleScheduler
import cn.edu.ustc.timeflow.model.StandardScheduler
import cn.edu.ustc.timeflow.model.StandardValuer
import cn.edu.ustc.timeflow.util.DBHelper
import cn.edu.ustc.timeflow.util.SharedPreferenceHelper
import cn.edu.ustc.timeflow.util.getActivity
import cn.edu.ustc.timeflow.widget.ScheduleWidget
import cn.edu.ustc.ui.WebActivity
import com.example.timeflow.R
import com.example.timeflow.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.Calendar

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        //This is the bottom navigation bar that is displayed on the bottom of the screen.
        val radioGroup = binding.appBarMain.navBar


        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.home -> {
                    //Toast.makeText(this, "home", Toast.LENGTH_SHORT).show()
                    findNavController(R.id.nav_host_fragment_content_main)
                        .popBackStack(R.id.dayScheduleFragment, true)
                    findNavController(R.id.nav_host_fragment_content_main)
                        .navigate(R.id.dayScheduleFragment)

                }

                R.id.plan_overview -> {
                    //Toast.makeText(this, "plan_overview", Toast.LENGTH_SHORT).show()
                    findNavController(R.id.nav_host_fragment_content_main)
                        .popBackStack(R.id.fragment_plan_overview, true)
                    findNavController(R.id.nav_host_fragment_content_main)
                        .navigate(R.id.fragment_plan_overview)
                }

                R.id.deadline_list -> {
                    findNavController(R.id.nav_host_fragment_content_main)
                        .popBackStack(R.id.fragment_deadline_list, true)
                    findNavController(R.id.nav_host_fragment_content_main)
                        .navigate(R.id.fragment_deadline_list)
                }
            }
        }

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.dayScheduleFragment, R.id.fragment_plan_overview, R.id.fragment_deadline_list
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_info_settings -> {
                    showUpdateCredentialsDialog()
                    true
                }
                R.id.nav_about -> {
                    // Handle about click
                    true
                }
                R.id.nav_help -> {
                    // Handle help click
                    true
                }
                R.id.shift_language -> {
                    // Handle shift language click
                    true
                }
                R.id.change_startweek -> {
                    showChangeStartWeekDialog()
                    true
                }
                else -> false
            }
            drawerLayout.closeDrawers()
            true
        }



        //初次使用
        if(!SharedPreferenceHelper.getBoolean(this, "notFirst", false)){

            SharedPreferenceHelper.saveBoolean(this, "notFirst", true)
            DBHelper(this).generateSample()

//            val SimpleScheduler = SimpleScheduler(this, StandardValuer(this))
//            SimpleScheduler.getTimeTable(LocalDateTime.now(), LocalDateTime.now().plusDays(7))
            var StandardScheduler = StandardScheduler(this, StandardValuer(this))
            StandardScheduler.Schedule(LocalDateTime.now(), LocalDateTime.now().plusDays(7))
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        val update = menu.findItem(R.id.update_online)
        update.setOnMenuItemClickListener {
            //打开WebActivity
            val intent = android.content.Intent(this, WebActivity::class.java)
            startActivity(intent)
            true
        }

        val AutoSchedule = menu.findItem(R.id.auto_schedule)
        AutoSchedule.setOnMenuItemClickListener {

            //TODO: 调用自动排课算法
//            var SimpleScheduler = SimpleScheduler(this, StandardValuer(this))
//            SimpleScheduler.Schedule(LocalDateTime.now(), LocalDateTime.now().plusDays(7))
            var StandardScheduler = StandardScheduler(this, StandardValuer(this))
            StandardScheduler.Schedule(LocalDateTime.now(), LocalDateTime.now().plusDays(7))

            Snackbar.make(binding.root, "自动安排成功", Snackbar.LENGTH_SHORT).show()

            // 更新页面显示
            val navController = findNavController(R.id.nav_host_fragment_content_main)
            navController.navigate(navController.currentDestination!!.id)

            true
        }
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onPause() {
        super.onPause()
        //更新widget
        val intent = Intent(this, ScheduleWidget::class.java)
        intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
        val ids = AppWidgetManager.getInstance(application).getAppWidgetIds(
            ComponentName(application, ScheduleWidget::class.java)
        )
        Log.d("MainActivity", "onPause: ${ids.size}")
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)
        sendBroadcast(intent)


    }

    private fun showUpdateCredentialsDialog() {
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.dialog_login, null)
        val usernameEditText = dialogLayout.findViewById<EditText>(R.id.username)
        val passwordEditText = dialogLayout.findViewById<EditText>(R.id.password)

        // Pre-fill the EditTexts with the current username and password
        usernameEditText.setText(SharedPreferenceHelper.getString(this, "username", ""))
        passwordEditText.setText(SharedPreferenceHelper.getString(this, "password", ""))

        builder.setView(dialogLayout)
        builder.setTitle("Update Credentials")
        builder.setPositiveButton("OK") { dialog, which ->
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()
            SharedPreferenceHelper.saveString(this, "username", username)
            SharedPreferenceHelper.saveString(this, "password", password)

            // Optionally, reload the WebView or perform other actions with the new credentials
            // Todo: reload and check if the credentials are correct
        }
        builder.setNegativeButton("Cancel") { dialog, which ->
            dialog.dismiss()
        }
        builder.show()
    }

    private fun showChangeStartWeekDialog() {
    val builder = AlertDialog.Builder(this)
    val inflater = layoutInflater
    val dialogLayout = inflater.inflate(R.layout.dialog_change_start_week, null)
    val startWeekTextView = dialogLayout.findViewById<TextView>(R.id.startweek_textview)

    // Display the current start week
    val currentStartWeek = SharedPreferenceHelper.getString(this, "startdate", "Not set")
    startWeekTextView.text = currentStartWeek

    builder.setView(dialogLayout)
    builder.setTitle("Change Start Week")
    builder.setPositiveButton("修改") { dialog, which ->
        // Open DatePicker to select new start week
        val datePickerDialog = DatePickerDialog(this, { _, year, month, dayOfMonth ->
            // 转换为当周的周日
            val date = LocalDate.of(year, month + 1, dayOfMonth)
            date.minusDays(date.dayOfWeek.value.toLong())

            SharedPreferenceHelper.saveString(this,"startdate", date.toString())
        }, LocalDate.now().year, LocalDate.now().monthValue - 1, LocalDate.now().dayOfMonth)
        // 设为周日开始
        datePickerDialog.datePicker.firstDayOfWeek =Calendar.SUNDAY
        datePickerDialog.show()
    }
    builder.setNegativeButton("取消") { dialog, which ->
        dialog.dismiss()
    }
    builder.show()
}
}

