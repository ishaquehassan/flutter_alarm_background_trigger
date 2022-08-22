package com.flutter.background.activity.launch.flutter_alarm_background_trigger

import io.flutter.plugin.common.MethodCall

enum class AlarmArgKey {
    TIME,PAYLOAD,UID,ID,STATUS,SCREEN_WAKE_DURATION
}

class AlarmArgs private constructor() {
    var time:Long? = null
    var payload:String? = null
    var uid:String? = null
    var id:Int? = null

    // Extras
    var screenWakeDuration:Long = 1000L

    companion object CompanionFactory {
        private fun <T> MethodCall.alarmArg(argKey: AlarmArgKey) : T?{
            return argument<T>(argKey.name)
        }

        private fun MethodCall.hasAlarmArg(argKey: AlarmArgKey) : Boolean{
            return hasArgument(argKey.name)
        }

        fun fromMethodCall(methodCall: MethodCall) : AlarmArgs {
            val args = AlarmArgs()
            if(methodCall.hasAlarmArg(AlarmArgKey.TIME)){
                args.time = methodCall.alarmArg(AlarmArgKey.TIME)
            }
            if(methodCall.hasAlarmArg(AlarmArgKey.PAYLOAD)){
                args.payload = methodCall.alarmArg(AlarmArgKey.PAYLOAD)
            }
            if(methodCall.hasAlarmArg(AlarmArgKey.UID)){
                args.uid = methodCall.alarmArg(AlarmArgKey.UID)
            }
            if(methodCall.hasAlarmArg(AlarmArgKey.ID)){
                args.id = methodCall.alarmArg(AlarmArgKey.ID)
            }
            if(methodCall.hasAlarmArg(AlarmArgKey.SCREEN_WAKE_DURATION)){
                args.screenWakeDuration = methodCall.alarmArg<Int>(AlarmArgKey.SCREEN_WAKE_DURATION)?.toLong() ?: 1000L
            }
            return args
        }
    }

}