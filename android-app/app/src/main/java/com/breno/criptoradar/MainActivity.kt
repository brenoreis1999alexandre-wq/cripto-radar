package com.breno.criptoradar

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.FrameLayout
import androidx.webkit.WebViewAssetLoader
import androidx.webkit.WebViewClientCompat

/** App Android do Cripto Radar; a interface existente roda dentro do pacote, sem barra do navegador. */
class MainActivity : Activity() {
    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = Color.rgb(10, 14, 24)
        window.navigationBarColor = Color.rgb(10, 14, 24)

        val container = FrameLayout(this).apply { setBackgroundColor(Color.rgb(10, 14, 24)) }
        webView = WebView(this)
        webView.setBackgroundColor(Color.rgb(10, 14, 24))
        webView.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            loadsImagesAutomatically = true
            mixedContentMode = WebSettings.MIXED_CONTENT_NEVER_ALLOW
            allowFileAccess = false
            allowContentAccess = false
            cacheMode = WebSettings.LOAD_DEFAULT
            // Honor the HTML device-width viewport and fit layout to the phone's available width.
            useWideViewPort = true
            loadWithOverviewMode = false
            textZoom = 100
            setSupportZoom(false)
            builtInZoomControls = false
            displayZoomControls = false
        }

        val assetLoader = WebViewAssetLoader.Builder()
            .addPathHandler("/assets/", WebViewAssetLoader.AssetsPathHandler(this))
            .build()
        webView.webViewClient = object : WebViewClientCompat() {
            override fun shouldInterceptRequest(view: WebView, request: WebResourceRequest) =
                assetLoader.shouldInterceptRequest(request.url)

            override fun onPageFinished(view: WebView, url: String?) {
                super.onPageFinished(view, url)
                view.post {
                    val cssWidth = (view.width / resources.displayMetrics.density).toInt().coerceAtLeast(320)
                    val script = """
                        (function() {
                          var width = ${cssWidth};
                          var viewport = document.querySelector('meta[name="viewport"]');
                          if (viewport) viewport.setAttribute('content', 'width=' + width + ', initial-scale=1');
                          document.documentElement.style.setProperty('width', width + 'px', 'important');
                          document.documentElement.style.setProperty('max-width', width + 'px', 'important');
                          document.body.style.setProperty('width', width + 'px', 'important');
                          document.body.style.setProperty('max-width', width + 'px', 'important');
                          document.documentElement.style.overflowX = 'hidden';
                          document.body.style.overflowX = 'hidden';
                          document.documentElement.scrollLeft = 0;
                          document.body.scrollLeft = 0;
                          window.scrollTo(0, 0);
                          window.dispatchEvent(new Event('resize'));
                        })();
                    """.trimIndent()
                    view.evaluateJavascript(script, null)
                }
            }

            override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
                val uri = request.url
                if (uri.host == "appassets.androidplatform.net") return false
                if (uri.scheme == "https" || uri.scheme == "http") {
                    startActivity(Intent(Intent.ACTION_VIEW, uri))
                }
                return true
            }
        }
        webView.webChromeClient = WebChromeClient()
        container.addView(webView, FrameLayout.LayoutParams(-1, -1))
        setContentView(container)
        webView.setInitialScale(100)
        webView.loadUrl("https://appassets.androidplatform.net/assets/index.html")
    }

    @Deprecated("Deprecated in Android 13; kept for Android 6 compatibility")
    override fun onBackPressed() {
        if (::webView.isInitialized && webView.canGoBack()) webView.goBack() else super.onBackPressed()
    }

    override fun onDestroy() {
        if (::webView.isInitialized) {
            (webView.parent as? android.view.ViewGroup)?.removeView(webView)
            webView.destroy()
        }
        super.onDestroy()
    }
}
