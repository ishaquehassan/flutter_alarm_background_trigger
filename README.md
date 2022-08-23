# Flutter background alarm trigger

A flutter plugin for Android to launch app from background at specific time just like stock alarm app in Android

#### Installation

```yaml
flutter_alarm_background_trigger: ^1.0.0
# or
flutter pub add flutter_alarm_background_trigger
```

#### Initialization

```dart
void main() {
  // Very important to call before initialize since it 
  // ensures the binding is available and ready before 
  // any native call
  WidgetsFlutterBinding.ensureInitialized();

  // initialize Required for alarm events to bind with flutter method channel
  FlutterAlarmBackgroundTrigger.initialize();

  runApp(const MyApp());
}

```

#### Create instance

```dart
var alarmPlugin = FlutterAlarmBackgroundTrigger();
```

#### Set Alarm

```dart
alarmPlugin.addAlarm(
      // Required
      DateTime.now().add(Duration(seconds: 10)),

      //Optional
      uid: "YOUR_APP_ID_TO_IDENTIFY",
      payload: {"YOUR_EXTRA_DATA":"FOR_ALARM"},

      // screenWakeDuration: For how much time you want 
      // to make screen awake when alarm triggered
      screenWakeDuration: Duration(minutes: 1)
  )
```

#### Receive event when alarm trigger

```dart
alarmPlugin.requestPermission().then((isGranted){
  if(isGranted){
    alarmPlugin.onForegroundAlarmEventHandler((alarm){
      // Perform your action here such as navigation
      // This event will be triggered on both cases, 
      // when app is in foreground or background!
      print(alarm.id)
    })
  }
})
```

## Additional methods

#### Request permission to draw over other apps

```dart
Future<bool> requestPermission()
```

#### Add

```dart
Future<AlarmItem> addAlarm(
  DateTime time, 
  {
    String? uid, 
    Map<String, dynamic>? payload, 
    Duration screenWakeDuration
  }
)
```

#### Get alarm by uid

```dart
Future<List<AlarmItem>> getAlarmByUid(String uid)
```

#### Get all scheduled alarms

```dart
Future<List<AlarmItem>> getAllAlarms()
```

#### Get single alarm

```dart
Future<AlarmItem> getAlarm(int id)
```

#### Get alarm by payload

```dart
Future<List<AlarmItem>> getAlarmByPayload(Map<String, dynamic> payload)
```

#### Get alarm by time

```dart
Future<List<AlarmItem>> getAlarmByTime(DateTime time)
```

#### Alarm trigger event

```dart
void onForegroundAlarmEventHandler(OnForegroundAlarmEvent alarmEvent)
```

#### Delete single alarm

```dart
Future<void> deleteAlarm(int id)
```

#### Delete by payload

```dart
Future<void> deleteAlarmsByPayload(Map<String, dynamic> payload)
```

#### Delete by time

```dart
Future<void> deleteAlarmsByTime(DateTime dateTime)
```

#### Delete by uid

```dart
Future<void> deleteAlarmsByUid(String uid)
```

#### Delete all scheduled alarms

```dart
Future<void> deleteAllAlarms()
```