package com.flutter.background.activity.launch.flutter_alarm_background_trigger.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update


@Dao
public interface AlarmDao {
    @Query("SELECT * FROM alarm_items ORDER BY time DESC")
    fun getAll(): List<AlarmItem>?

    @Query("SELECT * FROM alarm_items WHERE id IN (:userIds) ORDER BY time DESC")
    fun loadAllByIds(userIds: IntArray?): List<AlarmItem>?

    @Query(
        "SELECT * FROM alarm_items WHERE user_uid = :userUid ORDER BY time DESC"
    )
    fun findByUserUid(userUid: String?): List<AlarmItem>?

    @Query(
        "SELECT * FROM alarm_items WHERE id = :id"
    )
    fun findByUserId(id: Int): AlarmItem?

    @Query(
        "SELECT * FROM alarm_items WHERE time = :time ORDER BY time DESC"
    )
    fun findByTime(time: Long): List<AlarmItem>?

    @Query(
        "SELECT * FROM alarm_items WHERE payload = :payload ORDER BY time DESC"
    )
    fun findByPayload(payload: String?): List<AlarmItem>?

    @Query(
        "SELECT * FROM alarm_items WHERE status = :status ORDER BY time ASC"
    )
    fun findByStatus(status: String): List<AlarmItem>?

    @Insert
    fun insert(alarm: AlarmItem?): Long?

    @Update
    fun update(alarm: AlarmItem?): Int?

    @Delete
    fun delete(alarm: AlarmItem?)
}