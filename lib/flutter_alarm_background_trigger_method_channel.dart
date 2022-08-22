// ignore_for_file: constant_identifier_names

import 'dart:convert';

import 'package:flutter/foundation.dart';
import 'package:flutter_alarm_background_trigger/typedefs.dart';

import 'flutter_alarm_background_trigger_platform_interface.dart';
import 'src/AlarmItem.dart';

enum ChannelMethods {
  // Create
  ADD,

  // Read
  GET,
  GET_BY_TIME,
  GET_BY_UID,
  GET_BY_PAYLOAD,
  GET_ALL,

  // Delete
  DELETE,
  DELETE_BY_TIME,
  DELETE_BY_UID,
  DELETE_BY_PAYLOAD,
  DELETE_ALL,

  // Others
  REQUEST_PERMISSION,
  ON_BACKGROUND_ACTIVITY_LAUNCH,
  INITIALIZE
}

/// An implementation of [FlutterAlarmBackgroundTriggerPlatform] that uses method channels.
class MethodChannelFlutterAlarmBackgroundTrigger
    extends FlutterAlarmBackgroundTriggerPlatform {
  static void initialize() {
    FlutterAlarmBackgroundTriggerPlatform.invokeNativeMethodStatic(
        ChannelMethods.INITIALIZE);
  }

  @override
  void onForegroundAlarmEventHandler(OnForegroundAlarmEvent alarmEvent) {
    methodChannel.setMethodCallHandler((call) {
      if (call.method ==
          describeEnum(ChannelMethods.ON_BACKGROUND_ACTIVITY_LAUNCH)) {
        alarmEvent(AlarmItem.fromJsonList(jsonDecode(call.arguments)));
      }
      return Future.value();
    });
  }

  @override
  Future<bool> requestPermission() async {
    return (await invokeNativeMethod<bool>(
            ChannelMethods.REQUEST_PERMISSION)) ??
        false;
  }

  @override
  Future<AlarmItem> addAlarm(DateTime time,
      {String? uid,
      Map<String, dynamic>? payload,
      Duration? screenWakeDuration}) async {
    var alarm = AlarmItem(
        time: time,
        uid: uid,
        payload: payload,
        screenWakeDuration: screenWakeDuration);
    return AlarmItem.fromJson(jsonDecode(
        await invokeNativeMethod<String>(ChannelMethods.ADD, alarm) ?? "{}"));
  }

  @override
  Future<List<AlarmItem>> getAllAlarms() async {
    return AlarmItem.fromJsonList(jsonDecode(
            (await invokeNativeMethod<String>(ChannelMethods.GET_ALL))!) ??
        []);
  }

  @override
  Future<AlarmItem> getAlarm(int id) async {
    var alarm = AlarmItem(id: id);
    return AlarmItem.fromJson(jsonDecode(
            (await invokeNativeMethod<String>(ChannelMethods.GET, alarm))!) ??
        {});
  }

  @override
  Future<List<AlarmItem>> getAlarmByTime(DateTime time) async {
    var alarm = AlarmItem(time: time);
    return AlarmItem.fromJsonList(jsonDecode((await invokeNativeMethod<String>(
            ChannelMethods.GET_BY_TIME, alarm))!) ??
        []);
  }

  @override
  Future<List<AlarmItem>> getAlarmByUid(String uid) async {
    var alarm = AlarmItem(uid: uid);
    return AlarmItem.fromJsonList(jsonDecode((await invokeNativeMethod<String>(
            ChannelMethods.GET_BY_UID, alarm))!) ??
        []);
  }

  @override
  Future<List<AlarmItem>> getAlarmByPayload(
      Map<String, dynamic> payload) async {
    var alarm = AlarmItem(payload: payload);
    return AlarmItem.fromJsonList(jsonDecode((await invokeNativeMethod<String>(
            ChannelMethods.GET_BY_PAYLOAD, alarm))!) ??
        []);
  }

  @override
  Future<void> deleteAlarm(int id) {
    var alarm = AlarmItem(id: id);
    return invokeNativeMethod<void>(ChannelMethods.DELETE, alarm);
  }

  @override
  Future<void> deleteAlarmsByTime(DateTime dateTime) {
    var alarm = AlarmItem(time: dateTime);
    return invokeNativeMethod<void>(ChannelMethods.DELETE_BY_TIME, alarm);
  }

  @override
  Future<void> deleteAlarmsByUid(String uid) {
    var alarm = AlarmItem(uid: uid);
    return invokeNativeMethod<void>(ChannelMethods.DELETE_BY_UID, alarm);
  }

  @override
  Future<void> deleteAlarmsByPayload(Map<String, dynamic> payload) {
    var alarm = AlarmItem(payload: payload);
    return invokeNativeMethod<void>(ChannelMethods.DELETE_BY_PAYLOAD, alarm);
  }

  @override
  Future<void> deleteAllAlarms() {
    return invokeNativeMethod<void>(ChannelMethods.DELETE_ALL);
  }
}
