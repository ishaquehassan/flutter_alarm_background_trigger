package com.flutter.background.activity.launch.flutter_alarm_background_trigger

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.POWER_SERVICE
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.os.PowerManager


class AlarmBroadcastReceiver : BroadcastReceiver() {
override fun onReceive(context: Context?, intent: Intent?) {
    if (context == null) return

    // Notify Flutter (if the channel is ready)
    FlutterAlarmBackgroundTriggerPlugin.channel?.let { ch ->
        AlarmUtils(context).onBackgroundActivityLaunch(ch)
    }

    // Start the main activity if we have an intent
    val launchIntent = context.packageManager.getLaunchIntentForPackage(context.packageName)
    launchIntent?.apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent?.extras?.let { putExtras(it) }
        putExtra("AlarmBroadcastTrigger", true)
        context.startActivity(this)
    }

    // Acquire a timed partial wake lock (keeps CPU on; screen on is handled via window flags)
    val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager?
    val wakeLock = powerManager?.newWakeLock(
        PowerManager.PARTIAL_WAKE_LOCK or PowerManager.ACQUIRE_CAUSES_WAKEUP,
        "${context.applicationContext.packageName}.WakeLock"
    ) ?: return

    FlutterAlarmBackgroundTriggerPlugin.setWakelock(wakeLock)
    val defaultDuration = 1000L
    val duration = intent?.getLongExtra(AlarmArgKey.SCREEN_WAKE_DURATION.name, defaultDuration) ?: defaultDuration
    wakeLock.acquire(duration)
}
}