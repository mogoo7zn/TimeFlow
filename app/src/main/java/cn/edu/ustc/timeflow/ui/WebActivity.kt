package cn.edu.ustc.timeflow.ui

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.webkit.CookieManager
import android.webkit.JavascriptInterface
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import cn.edu.ustc.timeflow.converter.CourseTableWebConverter
import cn.edu.ustc.timeflow.util.SharedPreferenceHelper
import cn.edu.ustc.timeflow.converter.UstcJWConverter
import com.example.timeflow.R

class WebActivity : AppCompatActivity() {

    private val webview by lazy { findViewById<WebView>(R.id.web_view) }
    private val cookieManager by lazy { CookieManager.getInstance() }
    private var url = "https://jw.ustc.edu.cn/for-std/course-table"
    private val loginurl = "https://passport.ustc.edu.cn/login"
    private var username = ""
    private var password = ""
    private var tag = "CourseTable"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intent = intent
        tag = intent.getStringExtra("tag") ?: "CourseTable"

        enableEdgeToEdge()
        setContentView(R.layout.activity_web)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        if (SharedPreferenceHelper.getString(this, "username") == null) {
            showLoginDialog()
        }
        username = SharedPreferenceHelper.getString(this, "username") ?: ""
        password = SharedPreferenceHelper.getString(this, "password") ?: ""

        // Check the tag and load the appropriate URL
        if (tag == "login") {
            url = loginurl
            initWebView()
        } else {
            initWebView()
        }
    }



    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebView() {
        webview.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                when (view?.title) {
                    "中国科学技术大学统一身份认证系统" -> {
                        if (username.isEmpty() || password.isEmpty()) {
                            username = SharedPreferenceHelper.getString(this@WebActivity, "username") ?: ""
                            password = SharedPreferenceHelper.getString(this@WebActivity, "password") ?: ""
                        }
                        val js = "javascript:document.getElementById('username').value = '$username';" +
                                "javascript:document.getElementById('password').value = '$password';"
                        view.evaluateJavascript(js) { value -> Log.d("WebActivity", "onPageFinished: $value") }
                    }
                    "中国科学技术大学综合教务系统" -> {
                        val intent = Intent(this@WebActivity, WebActivity::class.java)
                        finish()
                        if (!isWebActivityRunning(this@WebActivity)) {
                            startActivity(intent)
                        }
                    }
                    "课表" -> {
                        view.loadUrl("javascript:window.java_obj.showDescription(" +
                                "document.getElementsByTagName('html')[0].innerHTML);")
                    }
                }
            }

            @Deprecated("Deprecated in Java")
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                url?.let { view?.loadUrl(it) }
                return true
            }
        }

        webview.addJavascriptInterface(InJavaScriptLocalObj(this), "java_obj")
        val webSettings = webview.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            useWideViewPort = true
            loadWithOverviewMode = true
            setSupportZoom(true)
            builtInZoomControls = true
            displayZoomControls = false
            cacheMode = WebSettings.LOAD_DEFAULT
            allowFileAccess = true
            javaScriptCanOpenWindowsAutomatically = true
            loadsImagesAutomatically = true
            defaultTextEncodingName = "utf-8"
        }
        cookieManager.setAcceptCookie(true)
        webview.loadUrl(url)
    }

    class InJavaScriptLocalObj(private val activity: WebActivity) {
        @JavascriptInterface
        fun showSource(html: String) {}

        @JavascriptInterface
        fun showDescription(str: String) {
            val converter: CourseTableWebConverter = UstcJWConverter(str, activity)
            converter.parse()
        }
    }

    private fun showLoginDialog() {
        val builder = AlertDialog.Builder(this)
        val dialogLayout = layoutInflater.inflate(R.layout.dialog_login, null)
        val usernameEditText = dialogLayout.findViewById<EditText>(R.id.username)
        val passwordEditText = dialogLayout.findViewById<EditText>(R.id.password)

        builder.setView(dialogLayout)
            .setTitle("Login")
            .setPositiveButton("OK") { _, _ ->
                val username = usernameEditText.text.toString()
                val password = passwordEditText.text.toString()
                SharedPreferenceHelper.saveString(this, "username", username)
                SharedPreferenceHelper.saveString(this, "password", password)
                webview.loadUrl(url)
            }
            .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    private fun isWebActivityRunning(context: Context): Boolean {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val runningTasks = activityManager.getRunningTasks(Int.MAX_VALUE)
        return runningTasks.any { it.topActivity?.className == WebActivity::class.java.name }
    }
}