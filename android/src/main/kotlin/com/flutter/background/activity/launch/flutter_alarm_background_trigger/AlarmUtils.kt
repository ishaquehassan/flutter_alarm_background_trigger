package com.flutter.background.activity.launch.flutter_alarm_background_trigger

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.util.Log
import androidx.room.Room
import com.flutter.background.activity.launch.flutter_alarm_background_trigger.common.serializeToMap
import com.flutter.background.activity.launch.flutter_alarm_background_trigger.db.AlarmDao
import com.flutter.background.activity.launch.flutter_alarm_background_trigger.db.AlarmDatabase
import com.flutter.background.activity.launch.flutter_alarm_background_trigger.db.AlarmItem
import com.flutter.background.activity.launch.flutter_alarm_background_trigger.db.AlarmStatus
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.Result
import org.json.JSONArray
import org.json.JSONObject

class AlarmUtils() {

    constructor(context: Context) : this() {
        db =  Room.databaseBuilder(
            context,
            AlarmDatabase::class.java, "${FlutterAlarmBackgroundTriggerPlugin.PLUGIN_NAME}.db"
        ).allowMainThreadQueries().fallbackToDestructiveMigration().build()
        this.context = context
        alarms = db.alarmsDao()!!
    }

    private lateinit var context: Context
    private lateinit var db: AlarmDatabase
    private lateinit var alarms: AlarmDao

    private fun createPendingIntent(item: AlarmItem, args: AlarmArgs): PendingIntent {
        val flags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        } else {
            PendingIntent.FLAG_UPDATE_CURRENT
        }

        return PendingIntent.getBroadcast(
            context, item.id, Intent(
                context, AlarmBroadcastReceiver::class.java
            ).apply {
                putExtra(AlarmArgKey.PAYLOAD.name,item.payload)
                putExtra(AlarmArgKey.UID.name,item.userUid)
                putExtra(AlarmArgKey.ID.name,item.id)
                putExtra(AlarmArgKey.TIME.name,item.time)
                putExtra(AlarmArgKey.STATUS.name,item.status.name)
                putExtra(AlarmArgKey.SCREEN_WAKE_DURATION.name,args.screenWakeDuration)
            },
            flags
        )
    }

    private fun setAlarm(item: AlarmItem, args: AlarmArgs) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager?
    
        val pendingIntent = createPendingIntent(item, args)
    
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager?.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, item.time!!, pendingIntent)
        } else {
            // On older devices, use setExact for accurate timing (less battery-friendly)
            alarmManager?.setExact(AlarmManager.RTC_WAKEUP, item.time!!, pendingIntent)
        }
    }

    private fun cancelAlarm(item: AlarmItem, args: AlarmArgs) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager?
    
        val pendingIntent = createPendingIntent(item, args)
        
        alarmManager?.cancel(pendingIntent)
    }

    private fun sendSingleAlarm(result: Result,alarm: AlarmItem?) {
        if(alarm == null){
            result.error("NOT_FOUND","Alarm not found","Alarm not found with given params")
            return
        }
        result.success(JSONObject(alarm.serializeToMap()).toString())
    }

    private fun sendListOfAlarms(result: Result,alarms: List<AlarmItem>?) {
        if(alarms == null)
        {
            result.error("NOT_FOUND","Alarms not found","Alarms not found with given params")
            return
        }
        result.success(JSONArray(alarms.map { it.serializeToMap() }.toList()).toString())
    }

    fun addAlarm(args: AlarmArgs,result: Result) {
        val item = alarms.insert(AlarmItem.fromAlarmArgs(args))
        val alarmItem = alarms.findByUserId(item!!.toInt())
        setAlarm(alarmItem!!,args)
        getAlarm(args.apply {
            id = item.toInt()
        },result)
    }

    fun getAlarm(args: AlarmArgs,result: Result) {
        sendSingleAlarm(result,alarms.findByUserId(args.id!!))
    }

    fun getAlarmByTime(args: AlarmArgs,result: Result) {
        sendListOfAlarms(result,alarms.findByTime(args.time!!))
    }

    fun getAlarmByUid(args: AlarmArgs,result: Result) {
        sendListOfAlarms(result,alarms.findByUserUid(args.uid!!))
    }

    fun getAlarmByPayload(args: AlarmArgs,result: Result) {
        sendListOfAlarms(result,alarms.findByPayload(args.payload!!))
    }

    fun getAllAlarms(args: AlarmArgs,result: Result) {
        sendListOfAlarms(result,alarms.getAll())
    }

    fun deleteAlarm(args: AlarmArgs, result: Result) {
        val item = alarms.findByUserId(args.id!!)
        if (item != null) {
            cancelAlarm(item, args)
            alarms.delete(item)
            result.success(true)
        } else {
            result.error("NOT_FOUND", "Alarm not found", null)
        }
    }

    fun deleteAlarmByTime(args: AlarmArgs, result: Result) {
        val affectedAlarms = alarms.findByTime(args.time!!) ?: emptyList()
        affectedAlarms.forEach { cancelAlarm(it, args) }
        val affected = alarms.deleteByTime(args.time!!)
        result.success(affected > 0)
    }

    fun deleteAlarmByUid(args: AlarmArgs, result: Result) {
        val affectedAlarms = alarms.findByUserUid(args.uid!!) ?: emptyList()
        affectedAlarms.forEach { cancelAlarm(it, args) }
        val affected = alarms.deleteByUid(args.uid!!)
        result.success(affected > 0)
    }

    fun deleteAlarmByPayload(args: AlarmArgs, result: Result) {
        val affectedAlarms = alarms.findByPayload(args.payload!!) ?: emptyList()
        affectedAlarms.forEach { cancelAlarm(it, args) }
        val affected = alarms.deleteByPayload(args.payload!!)
        result.success(affected > 0)
    }

    fun deleteAllAlarms(args: AlarmArgs, result: Result) {
        val allAlarms = alarms.getAll() ?: emptyList()
        allAlarms.forEach { cancelAlarm(it, args) }
        val affected = alarms.deleteAll()
        result.success(affected > 0)
    }


    fun initialize(args: AlarmArgs,result: Result){
        onBackgroundActivityLaunch(FlutterAlarmBackgroundTriggerPlugin.channel!!)
    }

    fun onBackgroundActivityLaunch(channel: MethodChannel) {
    val pending = alarms.findByStatus(AlarmStatus.PENDING.name) ?: emptyList()
    if (pending.isEmpty()) return

    val now = System.currentTimeMillis()
    val due = pending.filter { it.time != null && it.time!! <= now }

    if (due.isEmpty()) return

    // Mark due alarms as DONE before sending
    due.forEach { item ->
        item.status = AlarmStatus.DONE
        alarms.update(item)
    }

    sendBackgroundAlarmEvent(channel, due)
}

    private fun sendBackgroundAlarmEvent(channel: MethodChannel,alarms: List<AlarmItem>) {
        channel.invokeMethod(MethodNames.ON_BACKGROUND_ACTIVITY_LAUNCH.name,JSONArray(alarms.map { it.serializeToMap() }.toList()).toString())
    }
}