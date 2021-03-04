package com.fjkyly.paradise.ui.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;

import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;

import java.util.Map;

@SuppressLint("SetJavaScriptEnabled")
public class X5WebView extends WebView {

    private boolean isInit = false;

    public X5WebView(Context context) {
        super(context);
        init();
    }

    public X5WebView(Context context, boolean b) {
        super(context, b);
        init();
    }

    public X5WebView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public X5WebView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init();
    }

    public X5WebView(Context context, AttributeSet attributeSet, int i, boolean b) {
        super(context, attributeSet, i, b);
        init();
    }

    public X5WebView(Context context, AttributeSet attributeSet, int i, Map<String, Object> map, boolean b) {
        super(context, attributeSet, i, map, b);
        init();
    }

    private void init() {
        if (isInit) {
            return;
        }
        initView();
    }

    private void initView() {
        WebSettings settings = getSettings();
        //设置加载总览模式
        settings.setLoadWithOverviewMode(true);
        //设置内置缩放控件
        settings.setBuiltInZoomControls(true);
        //设置使用广角端口
        settings.setUseWideViewPort(true);
        //设置支持缩放
        settings.setSupportZoom(true);
        //设置Javascript可以自动打开Window窗口
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        //设置缓存模式
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        //设置地理位置启用
        settings.setGeolocationEnabled(true);
        //设置Dom存储已启用
        settings.setDomStorageEnabled(true);
        //设置可以访问文件
        settings.setAllowFileAccess(true);
        //设置数据库启用
        settings.setDatabaseEnabled(true);
        //设置启用Javascript
        settings.setJavaScriptEnabled(true);
        //设置应用缓存最大大小
        settings.setAppCacheMaxSize(Long.MAX_VALUE);
        //设置插件状态
        settings.setPluginState(WebSettings.PluginState.ON_DEMAND);
        //设置支持多个Window窗口
        settings.setSupportMultipleWindows(false);
        //设置布局算法
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        // 对于刘海屏机器如果WebView被遮挡会自动padding
        //getSettingsExtension().setDisplayCutoutEnable(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        isInit = true;
    }
}
