import 'package:flutter_alarm_background_trigger/alarm_methods.dart';
import 'package:flutter_alarm_background_trigger/flutter_alarm_background_trigger_method_channel.dart';
import 'package:flutter_alarm_background_trigger/src/AlarmItem.dart';
import 'package:flutter_alarm_background_trigger/typedefs.dart';
import 'flutter_alarm_background_trigger_platform_interface.dart';

export 'package:flutter_alarm_background_trigger/src/AlarmItem.dart';

class FlutterAlarmBackgroundTrigger implements AlarmMethods {
  static void initialize() =>
      MethodChannelFlutterAlarmBackgroundTrigger.initialize();

  @override
  void onForegroundAlarmEventHandler(OnForegroundAlarmEvent alarmEvent) {
    FlutterAlarmBackgroundTriggerPlatform.instance
        .onForegroundAlarmEventHandler(alarmEvent);
  }

  @override
  Future<bool> requestPermission() =>
      FlutterAlarmBackgroundTriggerPlatform.instance.requestPermission();

  @override
  Future<AlarmItem> addAlarm(DateTime time,
          {String? uid,
          Map<String, dynamic>? payload,
          Duration? screenWakeDuration}) =>
      FlutterAlarmBackgroundTriggerPlatform.instance.addAlarm(time,
          uid: uid, payload: payload, screenWakeDuration: screenWakeDuration);

  @override
  Future<void> deleteAlarm(int id) =>
      FlutterAlarmBackgroundTriggerPlatform.instance.deleteAlarm(id);

  @override
  Future<void> deleteAlarmsByPayload(Map<String, dynamic> payload) =>
      FlutterAlarmBackgroundTriggerPlatform.instance
          .deleteAlarmsByPayload(payload);

  @override
  Future<void> deleteAlarmsByTime(DateTime dateTime) =>
      FlutterAlarmBackgroundTriggerPlatform.instance
          .deleteAlarmsByTime(dateTime);

  @override
  Future<void> deleteAlarmsByUid(String uid) =>
      FlutterAlarmBackgroundTriggerPlatform.instance.deleteAlarmsByUid(uid);

  @override
  Future<void> deleteAllAlarms() =>
      FlutterAlarmBackgroundTriggerPlatform.instance.deleteAllAlarms();

  @override
  Future<AlarmItem> getAlarm(int id) =>
      FlutterAlarmBackgroundTriggerPlatform.instance.getAlarm(id);

  @override
  Future<List<AlarmItem>> getAlarmByPayload(Map<String, dynamic> payload) =>
      FlutterAlarmBackgroundTriggerPlatform.instance.getAlarmByPayload(payload);

  @override
  Future<List<AlarmItem>> getAlarmByTime(DateTime time) =>
      FlutterAlarmBackgroundTriggerPlatform.instance.getAlarmByTime(time);

  @override
  Future<List<AlarmItem>> getAlarmByUid(String uid) =>
      FlutterAlarmBackgroundTriggerPlatform.instance.getAlarmByUid(uid);

  @override
  Future<List<AlarmItem>> getAllAlarms() =>
      FlutterAlarmBackgroundTriggerPlatform.instance.getAllAlarms();
}
