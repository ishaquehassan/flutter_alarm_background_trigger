import 'package:flutter_alarm_background_trigger/flutter_alarm_background_trigger.dart';
import 'package:flutter_alarm_background_trigger/typedefs.dart';

class AlarmService {
  static AlarmService? _instance;
  final FlutterAlarmBackgroundTrigger _flutterAlarmPlugin;
  AlarmService._(this._flutterAlarmPlugin);
  static AlarmService get instance =>
      _instance ??= AlarmService._(FlutterAlarmBackgroundTrigger());

  Future<AlarmItem> addAlarm(DateTime time,
      {String? uid,
      Map<String, dynamic>? payload,
      Duration? screenWakeDuration}) async {
    return _flutterAlarmPlugin.addAlarm(time,
        uid: uid, payload: payload, screenWakeDuration: screenWakeDuration);
  }

  Future<List<AlarmItem>> getAllAlarms() async {
    return _flutterAlarmPlugin.getAllAlarms();
  }

  Future<AlarmItem> getAlarm(int id) async {
    return _flutterAlarmPlugin.getAlarm(id);
  }

  Future<List<AlarmItem>> getAlarmByUid(String uid) async {
    return _flutterAlarmPlugin.getAlarmByUid(uid);
  }

  Future<List<AlarmItem>> getAlarmByTime(DateTime dateTime) async {
    return _flutterAlarmPlugin.getAlarmByTime(dateTime);
  }

  Future<List<AlarmItem>> getAlarmByPayload(
      Map<String, dynamic> payload) async {
    return _flutterAlarmPlugin.getAlarmByPayload(payload);
  }

  Future<void> deleteAllAlarms() async {
    return _flutterAlarmPlugin.deleteAllAlarms();
  }

  Future<void> deleteAlarm(int id) async {
    return _flutterAlarmPlugin.deleteAlarm(id);
  }

  Future<void> deleteAlarmByUid(String uid) async {
    return _flutterAlarmPlugin.deleteAlarmsByUid(uid);
  }

  Future<void> deleteAlarmByTime(DateTime dateTime) async {
    return _flutterAlarmPlugin.deleteAlarmsByTime(dateTime);
  }

  Future<void> deleteAlarmByPayload(Map<String, dynamic> payload) async {
    return _flutterAlarmPlugin.deleteAlarmsByPayload(payload);
  }

  Future<bool> requestPermission() async {
    return _flutterAlarmPlugin.requestPermission();
  }

  Future<void> onForegroundAlarmEventHandler(
      OnForegroundAlarmEvent onAlarmReceive) async {
    return _flutterAlarmPlugin.onForegroundAlarmEventHandler(onAlarmReceive);
  }
}
