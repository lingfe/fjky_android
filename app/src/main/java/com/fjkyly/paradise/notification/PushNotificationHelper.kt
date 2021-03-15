package com.fjkyly.paradise.notification

import android.app.Notification
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.fjkyly.paradise.HomeActivity
import com.fjkyly.paradise.R
import com.fjkyly.paradise.base.App

/**
 * 显示聊天消息
 *
 * @param context 上下文
 * @param id 通知的唯一ID
 * @param title 标题
 * @param text 正文文本
 */
fun notifyMessage(
    context: Context,
    id: Int,
    title: String?,
    text: String?,
    intent: Intent = Intent(context, HomeActivity::class.java)
) {
    val builder =
        NotificationCompatUtil.createNotificationBuilder(context, MESSAGE, title, text, intent)
    // 默认情况下，通知的文字内容会被截断以放在一行。如果您想要更长的通知，可以使用 setStyle() 添加样式模板来启用可展开的通知。
    builder.setStyle(NotificationCompat.BigTextStyle().bigText(text))
    NotificationCompatUtil.notify(context, id, buildDefaultConfig(builder));
}

/**
 * 显示@提醒消息
 *
 * @param context 上下文
 * @param id 通知的唯一ID
 * @param title 标题
 * @param text 正文文本
 */
fun notifyMention(
    context: Context,
    id: Int,
    title: String?,
    text: String?,
    intent: Intent = Intent(context, HomeActivity::class.java)
) {
    val builder = NotificationCompatUtil.createNotificationBuilder(
        context,
        MENTION,
        title,
        text,
        intent
    )
    // 默认情况下，通知的文字内容会被截断以放在一行。如果您想要更长的通知，可以使用 setStyle() 添加样式模板来启用可展开的通知。
    builder.setStyle(NotificationCompat.BigTextStyle().bigText(text))
    NotificationCompatUtil.notify(context, id, buildDefaultConfig(builder));
}

/**
 * 显示系统通知
 *
 * @param context 上下文
 * @param id 通知的唯一ID
 * @param title 标题
 * @param text 正文文本
 */
fun notifyNotice(
    context: Context,
    id: Int,
    title: String?,
    text: String?,
    intent: Intent = Intent(context, HomeActivity::class.java)
) {
    val builder =
        NotificationCompatUtil.createNotificationBuilder(context, NOTICE, title, text, intent)
    NotificationCompatUtil.notify(context, id, buildDefaultConfig(builder));
}

/**
 * 显示音视频通话
 *
 * @param context 上下文
 * @param id 通知的唯一ID
 * @param title 标题
 * @param text 正文文本
 */
fun notifyCall(
    context: Context,
    id: Int,
    title: String?,
    text: String?,
    intent: Intent = Intent(context, HomeActivity::class.java)
) {
    val builder =
        NotificationCompatUtil.createNotificationBuilder(context, CALL, title, text, intent)
    NotificationCompatUtil.notify(context, id, buildDefaultConfig(builder));
}

/**
 * 构建应用通知的默认配置
 *
 * @param builder 构建器
 */
private fun buildDefaultConfig(builder: NotificationCompat.Builder): Notification {
    builder.setSmallIcon(R.mipmap.ic_launcher)
    return builder.build()
}

/** 通知渠道-聊天消息(重要性级别-高：发出声音) */
@RequiresApi(Build.VERSION_CODES.N)
private val MESSAGE = NotificationCompatUtil.Channel(
    channelId = "MESSAGE",
    name = App.appContext.getString(R.string.channel_message),
    importance = NotificationManager.IMPORTANCE_DEFAULT
)

/** 通知渠道-@提醒消息(重要性级别-紧急：发出提示音，并以浮动通知的形式显示 & 锁屏显示 & 振动0.25s )*/
@RequiresApi(Build.VERSION_CODES.N)
private val MENTION = NotificationCompatUtil.Channel(
    channelId = "MENTION",
    name = App.appContext.getString(R.string.channel_mention),
    importance = NotificationManager.IMPORTANCE_HIGH,
    lockScreenVisibility = NotificationCompat.VISIBILITY_PUBLIC,
    vibrate = longArrayOf(0, 250)
)

/** 通知渠道-系统通知(重要性级别-中：无提示音) */
@RequiresApi(Build.VERSION_CODES.N)
private val NOTICE = NotificationCompatUtil.Channel(
    channelId = "NOTICE",
    name = App.appContext.getString(R.string.channel_notice),
    importance = NotificationManager.IMPORTANCE_LOW
)

/** 通知渠道-音视频通话(重要性级别-紧急：发出提示音，并以浮动通知的形式显示 & 锁屏显示 & 振动4s停2s再振动4s ) */
@RequiresApi(Build.VERSION_CODES.N)
private val CALL = NotificationCompatUtil.Channel(
    channelId = "CALL",
    name = App.appContext.getString(R.string.channel_call),
    importance = NotificationManager.IMPORTANCE_HIGH,
    lockScreenVisibility = NotificationCompat.VISIBILITY_PUBLIC,
    vibrate = longArrayOf(0, 4000, 2000, 4000),
    sound = Uri.parse("android.resource://" + App.appContext.packageName + "/" + R.raw.iphone)
)