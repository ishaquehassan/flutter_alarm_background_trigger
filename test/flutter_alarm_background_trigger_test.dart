import 'package:flutter_test/flutter_test.dart';
import 'package:flutter_alarm_background_trigger/flutter_alarm_background_trigger.dart';
import 'package:flutter_alarm_background_trigger/flutter_alarm_background_trigger_platform_interface.dart';
import 'package:flutter_alarm_background_trigger/flutter_alarm_background_trigger_method_channel.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

class MockFlutterAlarmBackgroundTriggerPlatform
    with MockPlatformInterfaceMixin
    implements FlutterAlarmBackgroundTriggerPlatform {
  @override
  Future<String?> getPlatformVersion() => Future.value('42');
}

void main() {
  final FlutterAlarmBackgroundTriggerPlatform initialPlatform =
      FlutterAlarmBackgroundTriggerPlatform.instance;

  test('$MethodChannelFlutterAlarmBackgroundTrigger is the default instance',
      () {
    expect(initialPlatform,
        isInstanceOf<MethodChannelFlutterAlarmBackgroundTrigger>());
  });

  test('getPlatformVersion', () async {
    FlutterAlarmBackgroundTrigger flutterAlarmBackgroundTriggerPlugin =
        FlutterAlarmBackgroundTrigger();
    MockFlutterAlarmBackgroundTriggerPlatform fakePlatform =
        MockFlutterAlarmBackgroundTriggerPlatform();
    FlutterAlarmBackgroundTriggerPlatform.instance = fakePlatform;

    expect(
        await flutterAlarmBackgroundTriggerPlugin.getPlatformVersion(), '42');
  });
}
