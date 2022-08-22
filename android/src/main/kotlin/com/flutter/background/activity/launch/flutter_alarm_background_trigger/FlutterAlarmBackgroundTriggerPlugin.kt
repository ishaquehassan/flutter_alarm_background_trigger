package com.flutter.background.activity.launch.flutter_alarm_background_trigger

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.PowerManager
import android.provider.Settings
import android.util.Log
import android.view.WindowManager
import androidx.annotation.NonNull
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.embedding.engine.plugins.lifecycle.HiddenLifecycleReference
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.PluginRegistry.ActivityResultListener
import kotlin.reflect.KFunction2

enum class MethodNames {
    // Create
    ADD,

    // Read
    GET, GET_BY_TIME, GET_BY_UID, GET_BY_PAYLOAD, GET_ALL,

    // Delete
    DELETE, DELETE_BY_TIME, DELETE_BY_UID, DELETE_BY_PAYLOAD, DELETE_ALL,

    // Others
    REQUEST_PERMISSION,
    ON_BACKGROUND_ACTIVITY_LAUNCH,
    INITIALIZE
}

/** FlutterAlarmBackgroundTriggerPlugin */
class FlutterAlarmBackgroundTriggerPlugin : FlutterPlugin, MethodCallHandler, ActivityAware,
    ActivityResultListener {
    /// The MethodChannel that will the communication between Flutter and native Android
    ///
    /// This local reference serves to register the plugin with the Flutter Engine and unregister it
    /// when the Flutter Engine is detached from the Activity

    private lateinit var activityBinding: ActivityPluginBinding

    companion object {
        const val PLUGIN_NAME = "flutter_alarm_background_trigger"
        var channel: MethodChannel? = null
        var wakeLock: PowerManager.WakeLock? = null

        @JvmStatic
        fun setWakelock(wl: PowerManager.WakeLock?) {
            wakeLock = wl
        }
    }

    private var alarmUtils: AlarmUtils? = null
    private var methodMap: Map<MethodNames, KFunction2<AlarmArgs, Result, Unit>> = mapOf()

    private val methodArgsRules: Map<MethodNames, (args: AlarmArgs) -> Boolean> = mapOf(
        // Create
        MethodNames.ADD to { it.time != null },

        // Read
        MethodNames.GET to { it.id != null },
        MethodNames.GET_BY_TIME to { it.time != null },
        MethodNames.GET_BY_UID to { it.uid != null },
        MethodNames.GET_BY_PAYLOAD to { it.payload != null },
        MethodNames.GET_ALL to { true },

        // Delete
        MethodNames.DELETE to { it.id != null },
        MethodNames.DELETE_BY_TIME to { it.time != null },
        MethodNames.DELETE_BY_UID to { it.uid != null },
        MethodNames.DELETE_BY_PAYLOAD to { it.payload != null },
        MethodNames.DELETE_ALL to { true },

        // Others
        MethodNames.REQUEST_PERMISSION to { true },
        MethodNames.INITIALIZE to { true },
    )


    override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        channel = MethodChannel(flutterPluginBinding.binaryMessenger, PLUGIN_NAME)
        channel?.setMethodCallHandler(this)
    }

    override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
        val methodName = MethodNames.valueOf(call.method)
        if (methodMap.keys.contains(methodName) && methodArgsRules[methodName]?.invoke(
                AlarmArgs.fromMethodCall(
                    call
                )
            ) == true
        ) {
            methodMap[MethodNames.valueOf(call.method)]?.invoke(
                AlarmArgs.fromMethodCall(call),
                result
            )
        } else {
            result.notImplemented()
        }
    }

    override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
        channel?.setMethodCallHandler(null)
    }

    override fun onAttachedToActivity(binding: ActivityPluginBinding) {
        activityBinding = binding
        alarmUtils = AlarmUtils(activityBinding.activity)
        if(activityBinding.activity.intent.extras?.getBoolean("AlarmBroadcastTrigger") == true){
            val win = activityBinding.activity.window
            win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD)
            win.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON or WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON)
        }
        methodMap = mapOf(
            // Create
            MethodNames.ADD to alarmUtils!!::addAlarm,

            // Read
            MethodNames.GET to alarmUtils!!::getAlarm,
            MethodNames.GET_BY_TIME to alarmUtils!!::getAlarmByTime,
            MethodNames.GET_BY_UID to alarmUtils!!::getAlarmByUid,
            MethodNames.GET_BY_PAYLOAD to alarmUtils!!::getAlarmByPayload,
            MethodNames.GET_ALL to alarmUtils!!::getAllAlarms,

            // Delete
            MethodNames.DELETE to alarmUtils!!::deleteAlarm,
            MethodNames.DELETE_BY_TIME to alarmUtils!!::deleteAlarmByTime,
            MethodNames.DELETE_BY_UID to alarmUtils!!::deleteAlarmByUid,
            MethodNames.DELETE_BY_PAYLOAD to alarmUtils!!::deleteAlarmByPayload,
            MethodNames.DELETE_ALL to alarmUtils!!::deleteAllAlarms,

            // Others
            MethodNames.REQUEST_PERMISSION to ::requestPermission,
            MethodNames.INITIALIZE to alarmUtils!!::initialize,
        )

        handleLifeCycle()
    }

    override fun onDetachedFromActivityForConfigChanges() {}

    override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
        activityBinding = binding
        handleLifeCycle()
    }

    fun handleLifeCycle() {
        (activityBinding.lifecycle as HiddenLifecycleReference)
            .lifecycle
            .addObserver(LifecycleEventObserver { source, event ->
                if(event == Lifecycle.Event.ON_RESUME){
                    if(permissionResult != null){
                        requestPermission(null,null)
                    }
                }
            })
    }

    override fun onDetachedFromActivity() {
        if(wakeLock != null){
            wakeLock?.release()
        }
    }

    private var permissionResult: Result? = null

    private fun requestPermission(args: AlarmArgs?,result: Result?) {
        if(result != null){
            permissionResult = result
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(activityBinding.activity)) {
                val intent = Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + activityBinding.activity.packageName)
                )
                activityBinding.activity.startActivityForResult(intent, 1, null)
            }else{
                permissionResult?.success(true)
                permissionResult = null
            }
        }else{
            permissionResult?.success(true)
            permissionResult = null
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean {
        requestPermission(null,null)
        return true
    }
}
