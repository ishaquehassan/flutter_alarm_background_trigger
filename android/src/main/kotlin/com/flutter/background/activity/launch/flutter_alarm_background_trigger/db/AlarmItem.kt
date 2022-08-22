package com.flutter.background.activity.launch.flutter_alarm_background_trigger.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.flutter.background.activity.launch.flutter_alarm_background_trigger.AlarmArgs
import kotlin.reflect.KClass


@Entity(tableName = "alarm_items")
class AlarmItem {
    @PrimaryKey(autoGenerate = true)
    var id = 0

    @ColumnInfo(name = "time")
    var time: Long? = null

    @ColumnInfo(name = "payload")
    var payload: String? = null

    @ColumnInfo(name = "user_uid")
    var userUid: String? = null

    @ColumnInfo(name = "created_at")
    var createdAt: Long? = null

    @ColumnInfo(name = "status")
    var status: AlarmStatus = AlarmStatus.PENDING

    @ColumnInfo(name = "screen_wake_duration")
    var screenWakeDuration: Long = 1000

    companion object CompanionFactory {
        fun fromAlarmArgs(args: AlarmArgs) = AlarmItem().apply {
            time = args.time
            payload = args.payload
            userUid = args.uid
            id = args.id ?: 0
            createdAt = System.currentTimeMillis()
        }
    }
}