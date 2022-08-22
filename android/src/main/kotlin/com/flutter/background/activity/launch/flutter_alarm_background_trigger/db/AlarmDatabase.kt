package com.flutter.background.activity.launch.flutter_alarm_background_trigger.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.flutter.background.activity.launch.flutter_alarm_background_trigger.db.coverters.StatusTypeConverter

@Database(entities = [AlarmItem::class], version = 3)
@TypeConverters(StatusTypeConverter::class)
abstract class AlarmDatabase : RoomDatabase() {
    abstract fun alarmsDao(): AlarmDao?
}