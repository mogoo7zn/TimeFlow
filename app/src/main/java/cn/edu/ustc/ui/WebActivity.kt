package cn.edu.ustc.ui

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
import cn.edu.ustc.timeflow.util.CourseTableWebConverter
import cn.edu.ustc.timeflow.util.SharedPreferenceHelper
import cn.edu.ustc.timeflow.util.UstcJWConverter
import com.example.timeflow.R


class WebActivity : AppCompatActivity() {

    val webview by lazy { findViewById<WebView>(R.id.web_view) }
    val cookieManager by lazy { CookieManager.getInstance() }
    var url = "https://jw.ustc.edu.cn/for-std/course-table"
    var username = ""
    var password = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_web)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        if(SharedPreferenceHelper.getString(this, "username") == null){
            showLoginDialog()
        }
        username = SharedPreferenceHelper.getString(this, "username") ?: ""
        password = SharedPreferenceHelper.getString(this, "password") ?: ""
        initWebView()

    }

    //初始化WebView
    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebView(){
        webview.webViewClient = object : WebViewClient() {

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)

                if(view?.title == "中国科学技术大学统一身份认证系统"){
                    if (username == "" || password == "") {
                        username = SharedPreferenceHelper.getString(this@WebActivity, "username") ?: ""
                        password = SharedPreferenceHelper.getString(this@WebActivity, "password") ?: ""
                    }
                    val js =
                        "javascript:document.getElementById('username').value = '$username';javascript:document.getElementById('password').value = '$password';"
                    view.evaluateJavascript(js) { value ->
                        Log.d("WebActivity", "onPageFinished: $value")
                    }
                }
                if (view?.title == "中国科学技术大学综合教务系统") {
                    val intent = Intent(this@WebActivity, WebActivity::class.java)
                    finish()
                    //检测WebActivity是否已经打开, 如果已经打开则不再打开
                    if(!isWebActivityRunning(this@WebActivity)){
                        startActivity(intent)
                    }
                }
                if(view?.title == "课表"){
                    //获取<table style="margin-top: 15px;" id="lessons" class="table table-bordered table-striped table-hover table-condensed">
//                    view.loadUrl("javascript:window.java_obj.showDescription("
//                            + "document.getElementById('lessons').innerHTML"
//                            + ");")
                    view.loadUrl("javascript:window.java_obj.showDescription("
                            + "document.getElementsByTagName('html')[0].innerHTML);");
                }
            }
            @Deprecated("Deprecated in Java")
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {

                if (url != null) {
                    view?.loadUrl(url)
                }
                return true
            }

        }

        webview.addJavascriptInterface(InJavaScriptLocalObj(this), "java_obj")
        //声明WebSettings子类
        val webSettings: WebSettings = webview.getSettings()

        webSettings.javaScriptEnabled = true
        webSettings.domStorageEnabled = true

        webSettings.useWideViewPort = true //将图片调整到适合webview的大小
        webSettings.loadWithOverviewMode = true // 缩放至屏幕的大小

        webSettings.setSupportZoom(true) //支持缩放，默认为true。是下面那个的前提。
        webSettings.builtInZoomControls = true //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.displayZoomControls = false //隐藏原生的缩放控件


        webSettings.cacheMode = WebSettings.LOAD_DEFAULT //设置缓存
        webSettings.allowFileAccess = true //设置可以访问文件
        webSettings.javaScriptCanOpenWindowsAutomatically = true //支持通过JS打开新窗口
        webSettings.loadsImagesAutomatically = true //支持自动加载图片
        webSettings.defaultTextEncodingName = "utf-8" //设置编码格式

        cookieManager.setAcceptCookie(true)
        webview.loadUrl(url)

    }
    class InJavaScriptLocalObj (private val activity: WebActivity) {
        @JavascriptInterface
        fun showSource(html: String) {
            //                view?.loadUrl("javascript:window.java_obj.showSource("
            //                        + "document.getElementsByTagName('html')[0].innerHTML);");
            Log.d("HTML_SOURCE", "showSource: $html")
        }

        @JavascriptInterface
        fun showDescription(str: String) {
            Log.d("HTML_SOURCE", "showDescription: $str")
            val converter : CourseTableWebConverter = UstcJWConverter(str, activity)
            converter.parse()
        }
    }
    //显示登录对话框
    private fun showLoginDialog() {
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.dialog_login, null)
        val usernameEditText = dialogLayout.findViewById<EditText>(R.id.username)
        val passwordEditText = dialogLayout.findViewById<EditText>(R.id.password)

        builder.setView(dialogLayout)
        builder.setTitle("Login")
        builder.setPositiveButton("OK") { dialog, which ->
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()
            SharedPreferenceHelper.saveString(this, "username", username)
            SharedPreferenceHelper.saveString(this, "password", password)
            // Reload the WebView with the new credentials
            webview.loadUrl(url)
        }
        builder.setNegativeButton("Cancel") { dialog, which ->
            dialog.dismiss()
        }
        builder.show()
    }

    //检测WebActivity是否已经打开
    fun isWebActivityRunning(context: Context): Boolean {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val runningTasks = activityManager.getRunningTasks(Int.MAX_VALUE)

        for (task in runningTasks) {
            if (task.topActivity?.className == WebActivity::class.java.name) {
                return true
            }
        }
        return false
    }

}