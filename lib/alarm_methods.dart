import 'package:flutter_alarm_background_trigger/typedefs.dart';

import 'src/AlarmItem.dart';

class AlarmMethods {
  void onForegroundAlarmEventHandler(OnForegroundAlarmEvent alarmEvent) {
    throw UnimplementedError(
        'onForegroundAlarmEventHandler(OnForegroundAlarmEvent) has not been implemented.');
  }

  Future<bool> requestPermission() {
    throw UnimplementedError('requestPermission() has not been implemented.');
  }

  Future<AlarmItem> addAlarm(DateTime time,
      {String? uid,
      Map<String, dynamic>? payload,
      Duration? screenWakeDuration}) {
    throw UnimplementedError(
        'addAlarm(DateTime,[String?,Map<String, dynamic>?]) has not been implemented.');
  }

  Future<List<AlarmItem>> getAllAlarms() {
    throw UnimplementedError('getAllAlarms() has not been implemented.');
  }

  Future<AlarmItem> getAlarm(int id) {
    throw UnimplementedError('getAlarm(int) has not been implemented.');
  }

  Future<List<AlarmItem>> getAlarmByTime(DateTime time) {
    throw UnimplementedError(
        'getAlarmByTime(DateTime) has not been implemented.');
  }

  Future<List<AlarmItem>> getAlarmByUid(String uid) {
    throw UnimplementedError('getAlarmByUid(String) has not been implemented.');
  }

  Future<List<AlarmItem>> getAlarmByPayload(Map<String, dynamic> payload) {
    throw UnimplementedError(
        'getAlarmByPayload(Map<String, dynamic>) has not been implemented.');
  }

  Future<void> deleteAlarm(int id) {
    throw UnimplementedError('deleteAlarm(int) has not been implemented.');
  }

  Future<void> deleteAlarmsByTime(DateTime dateTime) {
    throw UnimplementedError(
        'deleteAlarmsByTime(DateTime) has not been implemented.');
  }

  Future<void> deleteAlarmsByUid(String uid) {
    throw UnimplementedError(
        'deleteAlarmsByUid(String) has not been implemented.');
  }

  Future<void> deleteAlarmsByPayload(Map<String, dynamic> payload) {
    throw UnimplementedError(
        'deleteAlarmsByPayload(Map<String, dynamic>) has not been implemented.');
  }

  Future<void> deleteAllAlarms() {
    throw UnimplementedError('deleteAllAlarms() has not been implemented.');
  }

  Future<void> moveToBackground() {
    throw UnimplementedError('moveToBackground() has not been implemented.');
  }
}
