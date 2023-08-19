// ignore_for_file: constant_identifier_names

import 'dart:convert';

import 'package:flutter/foundation.dart';

enum AlarmStatus { PENDING, DONE }

enum AlarmArgKey { TIME, PAYLOAD, UID, ID, SCREEN_WAKE_DURATION }

class AlarmItem {
  int? id;
  DateTime? time;
  Map<String, dynamic>? payload;
  String? uid;
  AlarmStatus status = AlarmStatus.PENDING;

  // Extras
  Duration? screenWakeDuration;

  AlarmItem(
      {this.id,
      this.time,
      this.payload,
      this.uid,
      this.status = AlarmStatus.PENDING,
      this.screenWakeDuration});

  factory AlarmItem.fromJson(Map<String, dynamic> data) {
    return AlarmItem(
      id: data['id'] ?? data['a'],
      time: DateTime.fromMillisecondsSinceEpoch(data['time'] ?? data['b']),
      payload: data['payload'] != null ? jsonDecode(data['payload']) : null,
      uid: data['userUid'],
      status: AlarmStatus.values.firstWhere(
        (e) => describeEnum(e) == (data['status'] ?? data['f']),
      ),
      screenWakeDuration:
          Duration(milliseconds: data['screenWakeDuration'] ?? data['g']),
    );
  }

  static List<AlarmItem> fromJsonList(List<dynamic> list) {
    List<AlarmItem> alarmItems = [];
    alarmItems.addAll(list.map((e) => AlarmItem.fromJson(e)));
    return alarmItems;
  }

  Map<String, dynamic> toMap() {
    Map<String, dynamic> map = {};
    map[describeEnum(AlarmArgKey.ID)] = id;
    map[describeEnum(AlarmArgKey.TIME)] = time?.millisecondsSinceEpoch;
    map[describeEnum(AlarmArgKey.PAYLOAD)] =
        payload != null ? jsonEncode(payload) : null;
    map[describeEnum(AlarmArgKey.UID)] = uid;
    map[describeEnum(AlarmArgKey.SCREEN_WAKE_DURATION)] =
        screenWakeDuration?.inMilliseconds;
    return map;
  }
}
