package cn.edu.ustc

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import cn.edu.ustc.timeflow.model.StandardScheduler
import cn.edu.ustc.timeflow.model.StandardValuer
import cn.edu.ustc.timeflow.notification.NotificationSystem
import cn.edu.ustc.timeflow.util.DBHelper
import cn.edu.ustc.timeflow.util.SharedPreferenceHelper
import cn.edu.ustc.timeflow.widget.ScheduleWidget
import cn.edu.ustc.timeflow.ui.WebActivity
import com.example.timeflow.R
import com.example.timeflow.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.Calendar

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var progressDialog: AlertDialog


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.appBarMain.toolbar)

        val progressDialogView = LayoutInflater.from(this).inflate(R.layout.process_dialog, null)
        progressDialogView.findViewById<TextView>(R.id.progress_dialog_text).text = getString(R.string.Autoscheduling)
        progressDialog = AlertDialog.Builder(this)
            .setView(progressDialogView)
            .setCancelable(false)
            .create()

        // This is the bottom navigation bar that is displayed on the bottom of the screen.
        val radioGroup = binding.appBarMain.navBar
        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.home -> {
                    findNavController(R.id.nav_host_fragment_content_main)
                        .popBackStack(R.id.dayScheduleFragment, true)
                    findNavController(R.id.nav_host_fragment_content_main)
                        .navigate(R.id.dayScheduleFragment)
                }
                R.id.plan_overview -> {
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

        // This is the drawer layout that is displayed on the left side of the screen.
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navController = findNavController(R.id.nav_host_fragment_content_main)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.dayScheduleFragment, R.id.fragment_plan_overview, R.id.fragment_deadline_list
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)

        // 侧边栏
        val navView: NavigationView = binding.navView
        navView.setupWithNavController(navController)
        // 设置Nav_header
        val headerView = navView.getHeaderView(0)
        val username = headerView.findViewById<TextView>(R.id.textViewName)
        var user = SharedPreferenceHelper.getString(this, "username", "未登录")
        if (user == "") {
            user = "未登录"
        }
        username.text = user
        val email = headerView.findViewById<TextView>(R.id.textViewEmail)
        email.text = "USTC"

        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_info_settings -> {
                    showUpdateCredentialsDialog()
                }
                R.id.nav_about -> {
                    showAboutDialog()
                }
                R.id.nav_help -> {
                    // Handle help click
                }
                R.id.shift_language -> {
                    // Handle shift language click
                }
                R.id.change_startweek -> {
                    showChangeStartWeekDialog()
                }
            }
            drawerLayout.closeDrawers()
            true
        }


    }

    /**
     * 溢出菜单
     */
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        val settings = menu.findItem(R.id.nav_settings)
        settings.setOnMenuItemClickListener {
            val notificationSystem = NotificationSystem(this)
            notificationSystem.updateNotification()
            true
        }

        val update = menu.findItem(R.id.update_online)
        update.setOnMenuItemClickListener {
            val intent = Intent(this, WebActivity::class.java)
            intent.putExtra("tag", "CourseTable")
            startActivity(intent)
            true
        }

        val autoSchedule = menu.findItem(R.id.auto_schedule)
        autoSchedule.setOnMenuItemClickListener {
            progressDialog.show()
            CoroutineScope(Dispatchers.IO).launch {
                // 调用自动排课算法
                val StandardScheduler = StandardScheduler(this@MainActivity, StandardValuer(this@MainActivity))
                StandardScheduler.Schedule(LocalDateTime.now(), LocalDateTime.now().plusDays(7))

                withContext(Dispatchers.Main) {
                    Snackbar.make(binding.root, R.string.AutoScheduled, Snackbar.LENGTH_SHORT)
                        .show()
                    progressDialog.dismiss()
                    // 更新页面显示
                    val navController = findNavController(R.id.nav_host_fragment_content_main)
                    navController.navigate(navController.currentDestination!!.id)
                }
            }
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
        val intent = Intent(this, ScheduleWidget::class.java)
        intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
        val ids = AppWidgetManager.getInstance(application).getAppWidgetIds(
            ComponentName(application, ScheduleWidget::class.java)
        )
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)
        sendBroadcast(intent)

        val notificationSystem = NotificationSystem(this)
        notificationSystem.updateNotification()
    }

    /**
     * 展示更新登录信息弹窗
     */
    private fun showUpdateCredentialsDialog() {
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.dialog_login, null)
        val usernameEditText = dialogLayout.findViewById<EditText>(R.id.username)
        val passwordEditText = dialogLayout.findViewById<EditText>(R.id.password)

        usernameEditText.setText(SharedPreferenceHelper.getString(this, "username", ""))
        passwordEditText.setText(SharedPreferenceHelper.getString(this, "password", ""))

        builder.setView(dialogLayout)
        builder.setTitle("更新登录信息")
        builder.setPositiveButton("确定") { _, _ ->
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()
            SharedPreferenceHelper.saveString(this, "username", username)
            SharedPreferenceHelper.saveString(this, "password", password)

            val intent = Intent(this, WebActivity::class.java)
            intent.putExtra("tag", "login")
            startActivity(intent)
        }
        builder.setNegativeButton("取消") { dialog, which ->
            dialog.dismiss()
        }
        builder.show()
    }

    /**
     * 展示修改起始周弹窗
     */
    private fun showChangeStartWeekDialog() {
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.dialog_change_start_week, null)
        val startWeekTextView = dialogLayout.findViewById<TextView>(R.id.startweek_textview)

        val currentStartWeek = SharedPreferenceHelper.getString(this, "startdate", "未设置")
        startWeekTextView.text = currentStartWeek

        builder.setView(dialogLayout)
        builder.setTitle("修改起始周")
        builder.setPositiveButton("修改") { _, _ ->
            val datePickerDialog = DatePickerDialog(this, { _, year, month, dayOfMonth ->
                val date = LocalDate.of(year, month + 1, dayOfMonth)
                date.minusDays(date.dayOfWeek.value.toLong())
                SharedPreferenceHelper.saveString(this, "startdate", date.toString())
            }, LocalDate.now().year, LocalDate.now().monthValue - 1, LocalDate.now().dayOfMonth)
            datePickerDialog.datePicker.firstDayOfWeek = Calendar.SUNDAY
            datePickerDialog.show()
        }
        builder.setNegativeButton("取消") { dialog, _ ->
            dialog.dismiss()
        }
        builder.show()
    }

    /**
     * 展示关于信息弹窗
     */
    private fun showAboutDialog() {
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.dialog_about, null)

        val appVersionTextView = dialogLayout.findViewById<TextView>(R.id.app_version)
        val versionName = packageManager.getPackageInfo(packageName, 0).versionName
        appVersionTextView.text = "Version: $versionName"

        builder.setView(dialogLayout)
            .setTitle(R.string.about)
            .setPositiveButton(R.string.OK) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}