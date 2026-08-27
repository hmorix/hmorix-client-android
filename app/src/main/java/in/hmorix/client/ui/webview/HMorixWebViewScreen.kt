package in.hmorix.client.ui.webview

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.webkit.*
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import in.hmorix.client.HMorixApp
import in.hmorix.client.ui.theme.*

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun HMorixWebViewScreen(
    url: String,
    onBack: () -> Unit
) {
    var webView: WebView? by remember { mutableStateOf(null) }
    var progress by remember { mutableStateOf(0) }
    var isLoading by remember { mutableStateOf(true) }

    BackHandler {
        if (webView?.canGoBack() == true) {
            webView?.goBack()
        } else {
            onBack()
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(ObsidianBg)) {
        AndroidView(
            factory = { context ->
                WebView(context).apply {
                    settings.apply {
                        javaScriptEnabled = true
                        domStorageEnabled = true
                        databaseEnabled = true
                        useWideViewPort = true
                        loadWithOverviewMode = true
                        cacheMode = WebSettings.LOAD_DEFAULT
                        userAgentString = "Mozilla/5.0 (Linux; Android 14; HMorix-Client-APK) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0 Mobile Safari/537.36"
                    }

                    // Sync auth cookies
                    val session = HMorixApp.instance.sessionManager.sessionCookie
                    if (!session.isNullOrEmpty()) {
                        CookieManager.getInstance().apply {
                            setAcceptCookie(true)
                            setCookie("https://hmorix.in", "hm_session=$session; Path=/; Secure; SameSite=Lax")
                        }
                    }

                    webViewClient = object : WebViewClient() {
                        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                            isLoading = true
                        }

                        override fun onPageFinished(view: WebView?, url: String?) {
                            isLoading = false
                        }
                    }

                    webChromeClient = object : WebChromeClient() {
                        override fun onProgressChanged(view: WebView?, newProgress: Int) {
                            progress = newProgress
                        }
                    }

                    loadUrl(url)
                    webView = this
                }
            },
            modifier = Modifier.fillMaxSize()
        )

        if (isLoading && progress < 100) {
            LinearProgressIndicator(
                progress = { progress / 100f },
                color = ElectricLime,
                trackColor = ObsidianBg,
                modifier = Modifier.fillMaxWidth().height(3.dp)
            )
        }
    }
}
