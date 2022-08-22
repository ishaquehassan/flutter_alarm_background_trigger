package com.flutter.background.activity.launch.flutter_alarm_background_trigger

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.POWER_SERVICE
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.os.PowerManager


class AlarmBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if(FlutterAlarmBackgroundTriggerPlugin.channel != null){
            AlarmUtils(context!!).apply {
                onBackgroundActivityLaunch(FlutterAlarmBackgroundTriggerPlugin.channel!!)
            }
        }
        context?.startActivity(Intent().apply {
            setPackage(context.packageName)
            setClass(context, Class.forName("${context.applicationContext.packageName}.MainActivity"))
            flags = FLAG_ACTIVITY_NEW_TASK
            putExtras(intent?.extras!!)
            putExtra("AlarmBroadcastTrigger",true)
        })
        val powerManager = context?.getSystemService(POWER_SERVICE) as PowerManager?
        val wakeLock = powerManager!!.newWakeLock(
            PowerManager.PARTIAL_WAKE_LOCK or PowerManager.ACQUIRE_CAUSES_WAKEUP,
            "${context?.applicationContext?.packageName}.WakeLock"
        )
        FlutterAlarmBackgroundTriggerPlugin.setWakelock(wakeLock)
        val defaultDuration = 1000L
        wakeLock.acquire(intent?.getLongExtra(AlarmArgKey.SCREEN_WAKE_DURATION.name,defaultDuration) ?: defaultDuration)
    }
}