import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';
import 'package:flutter_alarm_background_trigger/src/AlarmItem.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

import 'alarm_methods.dart';
import 'flutter_alarm_background_trigger_method_channel.dart';

abstract class FlutterAlarmBackgroundTriggerPlatform extends PlatformInterface
    with AlarmMethods {
  /// Constructs a FlutterAlarmBackgroundTriggerPlatform.
  FlutterAlarmBackgroundTriggerPlatform() : super(token: _token);

  static final Object _token = Object();

  static FlutterAlarmBackgroundTriggerPlatform _instance =
      MethodChannelFlutterAlarmBackgroundTrigger();

  final methodChannel = const MethodChannel('flutter_alarm_background_trigger');

  /// The default instance of [FlutterAlarmBackgroundTriggerPlatform] to use.
  ///
  /// Defaults to [MethodChannelFlutterAlarmBackgroundTrigger].
  static FlutterAlarmBackgroundTriggerPlatform get instance => _instance;

  /// Platform-specific implementations should set this with their own
  /// platform-specific class that extends [FlutterAlarmBackgroundTriggerPlatform] when
  /// they register themselves.
  static set instance(FlutterAlarmBackgroundTriggerPlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  Future<T?> invokeNativeMethod<T>(ChannelMethods method, [AlarmItem? alarm]) {
    return instance.methodChannel
        .invokeMethod<T>(describeEnum(method), alarm?.toMap());
  }

  static Future<T?> invokeNativeMethodStatic<T>(ChannelMethods method,
      [AlarmItem? alarm]) {
    return instance.invokeNativeMethod(method, alarm);
  }
}
