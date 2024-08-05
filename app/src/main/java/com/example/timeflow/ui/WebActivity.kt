package com.example.timeflow.ui

import android.os.Bundle
import android.util.Log
import android.webkit.CookieManager
import android.webkit.JavascriptInterface
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import cn.edu.ustc.timeflow.util.CourseTableWebConverter
import cn.edu.ustc.timeflow.util.UstcJWConverter
import com.example.timeflow.R


class WebActivity : AppCompatActivity() {

    val webview by lazy { findViewById<WebView>(R.id.web_view) }
    val cookieManager by lazy { CookieManager.getInstance() }
    var url = "https://jw.ustc.edu.cn/for-std/course-table"
    var cookie = ""
    var title = ""
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
        initWebView()

    }
    class InJavaScriptLocalObj {
        @JavascriptInterface
        fun showSource(html: String) {
            //                view?.loadUrl("javascript:window.java_obj.showSource("
            //                        + "document.getElementsByTagName('html')[0].innerHTML);");
            Log.d("HTML_SOURCE", "showSource: $html")
        }

        @JavascriptInterface
        fun showDescription(str: String) {
            Log.d("HTML_SOURCE", "showDescription: $str")
            val converter : CourseTableWebConverter = UstcJWConverter(str)
        }
    }

    fun initWebView(){
        webview.webViewClient = object : WebViewClient() {

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                //Toast.makeText(this@WebActivity,view?.title,Toast.LENGTH_SHORT).show()
                if(view?.title == "中国科学技术大学统一身份认证系统"){
                    var js =
                        "javascript:document.getElementById('username').value = '$username';javascript:document.getElementById('password').value = '$password';"
                    view.evaluateJavascript(js) { value ->
                        Log.d("WebActivity", "onPageFinished: $value")
                    }
                }
                if (view?.title == "中国科学技术大学综合教务系统") {
                    //跳转到课表页面

                    view.loadUrl("https://jw.ustc.edu.cn/for-std/course-table")
                }
                if(view?.title == "课表"){
                    //获取<table style="margin-top: 15px;" id="lessons" class="table table-bordered table-striped table-hover table-condensed">
                    view.loadUrl("javascript:window.java_obj.showDescription("
                            + "document.getElementById('lessons').innerHTML"
                            + ");")
                }
            }
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                //Toast.makeText(this@WebActivity,url,Toast.LENGTH_SHORT).show()
                if (url != null) {
                    view?.loadUrl(url)
                }
                return true
            }

        }

        webview.addJavascriptInterface(InJavaScriptLocalObj(), "java_obj")
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
//        webview.loadUrl(url)
        webview.loadUrl("https://jw.ustc.edu.cn/home")

    }
}