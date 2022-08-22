import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';

import 'package:flutter_alarm_background_trigger/flutter_alarm_background_trigger.dart';
import 'package:date_time_picker/date_time_picker.dart';
import 'package:flutter_alarm_background_trigger_example/alarm_service.dart';

void main() {
  WidgetsFlutterBinding.ensureInitialized();

  // initialize Required for alarm events to bind with flutter method channel
  FlutterAlarmBackgroundTrigger.initialize();

  runApp(const MyApp());
}

class MyApp extends StatefulWidget {
  const MyApp({Key? key}) : super(key: key);

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  AlarmItem? _alarmItem;
  DateTime? time;
  List<AlarmItem> alarms = [];

  @override
  void initState() {
    super.initState();
    reloadAlarms();
    AlarmService.instance.onForegroundAlarmEventHandler((alarmItem) {
      reloadAlarms();
    });
  }

  reloadAlarms() {
    AlarmService.instance.getAllAlarms().then((alarmsList) => setState(() {
          alarms = alarmsList;
        }));
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Builder(builder: (context) {
          return Center(
            child: Column(
              mainAxisAlignment: MainAxisAlignment.center,
              crossAxisAlignment: CrossAxisAlignment.center,
              children: [
                Card(
                  margin: const EdgeInsets.all(10),
                  child: Padding(
                    padding: const EdgeInsets.all(8.0),
                    child: Row(
                      children: [
                        Expanded(
                            child: DateTimePicker(
                          type: DateTimePickerType.dateTime,
                          initialValue: '',
                          firstDate: DateTime.now(),
                          lastDate:
                              DateTime.now().add(const Duration(days: 365)),
                          dateLabelText: 'Alarm date time',
                          onChanged: (val) => setState(() {
                            time = DateTime.parse(val);
                          }),
                        )),
                        ElevatedButton(
                            onPressed: createAlarm,
                            child: const Text("Set Alarm"))
                      ],
                    ),
                  ),
                ),
                Expanded(
                  child: ListView.builder(
                    itemCount: alarms.length,
                    itemBuilder: (ctx, idx) => ((AlarmItem alarm) => ListTile(
                          title: Row(
                            children: [
                              Text(alarm.time!.toString()),
                              const SizedBox(width: 5),
                              Chip(
                                padding: EdgeInsets.zero,
                                label: Text(describeEnum(alarm.status),
                                    style: TextStyle(
                                        color: alarm.status == AlarmStatus.DONE
                                            ? Colors.black
                                            : Colors.white,
                                        fontSize: 10)),
                                backgroundColor:
                                    alarm.status == AlarmStatus.DONE
                                        ? Colors.greenAccent
                                        : Colors.redAccent,
                              )
                            ],
                          ),
                          subtitle: Text(
                              "ID: ${alarm.id}, UID: ${alarm.uid}, Payload: ${alarm.payload.toString()}"),
                          trailing: IconButton(
                              onPressed: () async {
                                await AlarmService.instance
                                    .deleteAlarm(alarm.id!);
                                reloadAlarms();
                              },
                              icon:
                                  const Icon(Icons.delete, color: Colors.red)),
                        ))(alarms[idx]),
                  ),
                )
              ],
            ),
          );
        }),
      ),
    );
  }

  void createAlarm() async {
    await AlarmService.instance
        .addAlarm(time!, uid: "TEST UID", payload: {"holy": "Moly"});
    reloadAlarms();
  }
}
