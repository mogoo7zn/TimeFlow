package cn.edu.ustc

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import cn.edu.ustc.timeflow.widget.ScheduleWidget
import cn.edu.ustc.timeflow.widget.TaskWidgetService
import cn.edu.ustc.ui.WebActivity
import com.example.timeflow.R
import com.example.timeflow.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar

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
                    findNavController(R.id.nav_host_fragment_content_main).navigate(R.id.dayScheduleFragment)
                }

                R.id.plan_overview -> {
                    //Toast.makeText(this, "plan_overview", Toast.LENGTH_SHORT).show()
                    findNavController(R.id.nav_host_fragment_content_main).navigate(R.id.fragment_plan_overview)
                }

                R.id.deadline_list -> {
                    findNavController(R.id.nav_host_fragment_content_main).navigate(R.id.fragment_deadline_list)
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
}