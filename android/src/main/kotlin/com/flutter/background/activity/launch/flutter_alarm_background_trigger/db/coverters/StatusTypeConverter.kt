package com.flutter.background.activity.launch.flutter_alarm_background_trigger.db.coverters

import androidx.room.TypeConverter
import com.flutter.background.activity.launch.flutter_alarm_background_trigger.db.AlarmStatus

class StatusTypeConverter {

    @TypeConverter
    fun toAlarmStatus(status: String): AlarmStatus = AlarmStatus.valueOf(status)

    @TypeConverter
    fun fromAlarmStatus(status: AlarmStatus): String = status.name
}