package com.donut.wxed34e654870cba42

import MainProcessTask
import android.Manifest
import android.app.Activity
import android.app.AppOpsManager
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import com.getui.gs.sdk.GsManager
import com.igexin.sdk.IUserLoggerInterface
import com.igexin.sdk.PushConsts
import com.igexin.sdk.PushManager
import com.igexin.sdk.Tag
import com.igexin.sdk.message.GTCmdMessage
import com.igexin.sdk.message.GTNotificationMessage
import com.igexin.sdk.message.GTTransmitMessage
import com.tencent.luggage.wxa.SaaA.plugin.NativePluginBase
import com.tencent.luggage.wxa.SaaA.plugin.NativePluginInterface
import com.tencent.luggage.wxa.SaaA.plugin.SyncJsApi
import com.tencent.mm.sdk.json.JSONUtils.forEach
import org.json.JSONArray
import org.json.JSONObject
import java.lang.reflect.Method
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible


class PluginManager : NativePluginBase(), NativePluginInterface {

    private var callback: (intent: Intent) -> Unit
    private val TAG = "PluginManager"
    private val mainProcessTask: MainProcessTask = MainProcessTask(Intent())


    companion object {
        const val IDO_GTCID = 200
        const val GT_LOG = -1
        const val GT_NOTIFY_ENABLE = "-2"
    }

    init {
        val callback: (intent: Intent) -> Unit = { intent: Intent ->
            val bundle = intent.extras!!
            Log.d(TAG, " Received data: ${bundle.getInt(PushConsts.CMD_ACTION)}")
            val action = bundle.getInt(PushConsts.CMD_ACTION)

            val map = HashMap<String, Any>()
            when (action) {
                PluginManager.IDO_GTCID -> {
                    map["method"] = "ido_gtcIdCallback"
                    val map2 = HashMap<String, Any>()
                    map2.put("result", bundle.getString("gtcId")!!)
                    map["param"] = map2
                }
                PushConsts.GET_MSG_DATA -> {
                    map["method"] = "onReceiveMessageData"
                    var gtTransmitMessage =
                        intent.getSerializableExtra(PushConsts.KEY_MESSAGE_DATA) as GTTransmitMessage
                    map["param"] = toMap(gtTransmitMessage)
                }
                PushConsts.GET_CLIENTID -> {
                    map["method"] = "onReceiveClientId"
                    map["param"] = bundle.getString(PushConsts.KEY_CLIENT_ID)!!
                }
                PushConsts.GET_DEVICETOKEN -> {
                    map["method"] = "onReceiveDeviceToken"
                    map["param"] = bundle.getString(PushConsts.KEY_DEVICE_TOKEN)!!

                }
                PushConsts.GET_SDKONLINESTATE -> {
                    map["method"] = "onReceiveOnlineState"
                    map["param"] = bundle.getBoolean(PushConsts.KEY_ONLINE_STATE)
                }
                PushConsts.GET_SDKSERVICEPID -> {
                    map["method"] = "onReceiveServicePid"
                    map["param"] = bundle.getInt(PushConsts.KEY_SERVICE_PIT)
                }
                PushConsts.KEY_CMD_RESULT -> {
                    val gtCmdMessage =
                        intent.getSerializableExtra(PushConsts.KEY_CMD_MSG) as GTCmdMessage?
                    map["method"] = "onReceiveCommandResult"
                    map["param"] = toMap(gtCmdMessage!!)
                    Log.d(TAG, " Received data: ${map["param"]}")
                }
                PushConsts.ACTION_NOTIFICATION_ARRIVED -> {
                    map["method"] = "onNotificationMessageArrived"
                    map["param"] =
                        toMap(intent.getSerializableExtra(PushConsts.KEY_NOTIFICATION_ARRIVED) as GTNotificationMessage)
                }
                PushConsts.ACTION_NOTIFICATION_CLICKED -> {
                    map["method"] = "onNotificationMessageClicked"
                    map["param"] =
                        toMap(intent.getSerializableExtra(PushConsts.KEY_NOTIFICATION_CLICKED) as GTNotificationMessage)
                }
                PushConsts.ACTION_NOTIFICATION_ENABLE -> {
                    if (bundle.containsKey(PluginManager.GT_NOTIFY_ENABLE)) {
                        map["method"] = "areNotificationsEnabled"
                        map["param"] = bundle.getBoolean(GT_NOTIFY_ENABLE)
                    }
                }
                PushConsts.ACTION_POPUP_SHOW -> {
//                    onPopupMessageShow(context, bundle.getSerializable(PushConsts.KEY_POPUP_SHOW) as GTPopupMessage?)

                }
                PushConsts.ACTION_POPUP_CLICKED -> {
//                    onPopupMessageClicked(context, bundle.getSerializable(PushConsts.KEY_POPUP_CLICKED) as GTPopupMessage?)
                }
                GT_LOG -> {
                    map["method"] = "debugLog"
                    map["param"] = intent.getStringExtra("log")!!
                }
                else -> {

                }
            }
            if (map["method"] != null) {
                this.sendMiniPluginEvent(map)
            }
        }
        this.callback = callback;
        mainProcessTask.setClientCallback(callback)
        mainProcessTask.execAsync()
    }

    override fun getPluginID(): String {
        Log.d(TAG, "getPluginID")
        return BuildConfig.PLUGIN_ID
    }

    @SyncJsApi(methodName = "gt_initialize")
    fun initialize(data: JSONObject?, activity: Activity) {
        Log.d(TAG, "initialize")
        val intent = Intent("gt_initialize")
        this.mainProcessTask.setIntent(intent)
        this.mainProcessTask.execAsync()
        PushManager.getInstance().initialize(activity.applicationContext);
        requestNotifyPermission(data, activity);
    }

    @SyncJsApi(methodName = "gt_areNotificationsEnabled")
    fun areNotificationsEnabled(data: JSONObject?, activity: Activity): Boolean {
        return areNotificationsEnabled(activity);
    }

    @SyncJsApi(methodName = "gt_requestNotifyPermission")
    fun requestNotifyPermission(data: JSONObject?, activity: Activity) {
        val callback = this.callback;
        this.requestPermission(
            activity,
            arrayOf(Manifest.permission.POST_NOTIFICATIONS)
        ) { permissions, grantResults ->
            if (permissions.contains(Manifest.permission.POST_NOTIFICATIONS) && grantResults != null && grantResults.size > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED
            ) {
                val intent = Intent()
                val bundle = Bundle()
                bundle.putInt(PushConsts.CMD_ACTION, PushConsts.ACTION_NOTIFICATION_ENABLE)
                bundle.putBoolean(PluginManager.GT_NOTIFY_ENABLE, true)
                intent.putExtras(bundle)
                callback(intent)
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    Log.d(TAG,"requestPermissions")
                    activity.requestPermissions(arrayOf(Manifest.permission.POST_NOTIFICATIONS),1);
                }
            }
        }
    }


    @SyncJsApi(methodName = "gt_turnOnPush")
    fun turnOnPush(data: JSONObject?, activity: Activity) {
        PushManager.getInstance().turnOnPush(activity.applicationContext);
    }

    @SyncJsApi(methodName = "gt_turnOffPush")
    fun turnOffPush(data: JSONObject?, activity: Activity) {
        PushManager.getInstance().turnOffPush(activity.applicationContext);
    }

    @SyncJsApi(methodName = "gt_isPushTurnedOn")
    fun isPushTurnedOn(data: JSONObject?, activity: Activity): Boolean {
        return PushManager.getInstance().isPushTurnedOn(activity.applicationContext);
    }

    @SyncJsApi(methodName = "gt_getVersion")
    fun getVersion(data: JSONObject?, activity: Activity): String {
        return PushManager.getInstance().getVersion(activity.applicationContext);
    }

    @SyncJsApi(methodName = "gt_queryTag")
    fun queryTag(obj: JSONObject, activity: Activity) {
        PushManager.getInstance().queryTag(activity.applicationContext, obj["sn"] as String);
    }

    @SyncJsApi(methodName = "gt_setTag")
    fun setTag(obj: JSONObject, activity: Activity) {
        if (obj["tags"] == null) {
            return
        }
        val tags = obj["tags"] as JSONArray
        val array = mutableListOf<Tag>()
        tags.forEach<String> { s ->
            val tag = Tag()
            tag.name = s
            array.add(tag)
        }
        val toTypedArray = array.toTypedArray()

        PushManager.getInstance()
            .setTag(activity.applicationContext, toTypedArray, obj["sn"] as String);
    }

    //Context context, int beginHour, int duration
    @SyncJsApi(methodName = "gt_setSilentTime")
    fun setSilentTime(obj: JSONObject, activity: Activity) {
        PushManager.getInstance().setSilentTime(
            activity.applicationContext,
            obj["beginHour"] as Int,
            obj["duration"] as Int
        );
    }

    @SyncJsApi(methodName = "gt_setHeartbeatInterval")
    fun setHeartbeatInterval(obj: JSONObject, activity: Activity) {
        PushManager.getInstance().setHeartbeatInterval(
            activity.applicationContext, obj["interval"] as
                    Int
        );
    }

    @SyncJsApi(methodName = "gt_sendFeedbackMessage")
    fun sendFeedbackMessage(obj: JSONObject, activity: Activity) {
        PushManager.getInstance().sendFeedbackMessage(
            activity.applicationContext, obj["taskid"] as String, obj["messageid"] as String,
            obj["actionid"] as Int
        );
    }

    @SyncJsApi(methodName = "gt_getClientid")
    fun getClientid(obj: JSONObject?, activity: Activity): String {
        return PushManager.getInstance().getClientid(activity.applicationContext);
    }

    @SyncJsApi(methodName = "gt_bindAlias")
    fun bindAlias(obj: JSONObject, activity: Activity) {
        val sn =
            if (obj.has("sn")) obj["sn"] as String else "bindAlias" + System.currentTimeMillis()
        PushManager.getInstance()
            .bindAlias(activity.applicationContext, obj["alias"] as String, sn);
    }

    @SyncJsApi(methodName = "gt_unBindAlias")
    fun unBindAlias(obj: JSONObject, activity: Activity) {
        val isSelf = if (obj.has("isSelf")) obj["isSelf"] as Boolean else true
        val sn =
            if (obj.has("sn")) obj["sn"] as String else "unBindAlias_" + System.currentTimeMillis()
        PushManager.getInstance().unBindAlias(
            activity.applicationContext,
            obj["alias"] as String,
            isSelf, sn
        );
    }

    //    @SyncJsApi(methodName = "setDebugLogger")
    fun setDebugLogger(loggerInterface: IUserLoggerInterface, activity: Activity) {
        PushManager.getInstance().setDebugLogger(activity.applicationContext, loggerInterface);
    }

    @SyncJsApi(methodName = "gt_setLocalBadge")
    fun setBadgeNum(obj: JSONObject, activity: Activity) {
        PushManager.getInstance().setBadgeNum(activity.applicationContext, obj["badge"] as Int);
    }

    @SyncJsApi(methodName = "gt_openNotification")
    fun openNotification(obj: JSONObject?, activity: Activity) {
        PushManager.getInstance().openNotification(activity.applicationContext);
    }

    fun toMap(bean: Any): Map<String, Any?> {
        val memberProperties = bean::class.memberProperties
        val hashMap = HashMap<String, Any?>()
        for (property in memberProperties) {
            property.isAccessible = true
            // 获取属性的名称和值
            val name = property.name
            var value: Any? = null
            if ("tags".equals(name)) {
                val array = JSONArray()
                for (tag in property.getter.call(bean) as Array<Tag>) {
                    array.put(tag.name)
                }
                value = array
            } else {
                value = property.getter.call(bean)
            }
            hashMap.put(name, value ?: "")
            Log.d(TAG, "toMap  ${bean::class.simpleName} ${name} : ${value}")

        }
        return hashMap
    }


    @SyncJsApi(methodName = "ido_init")
    fun init(data: JSONObject, activity: Activity) {
        val intent = Intent("init")
        intent.putExtra("param", data.toString())
        this.mainProcessTask.setIntent(intent)
        this.mainProcessTask.execAsync()
    }

    @SyncJsApi(methodName = "ido_setDebugEnable")
    fun setDebugEnable(data: JSONObject, activity: Activity) {
        try {
            val intent = Intent("setDebugEnable")
            intent.putExtra("param", data.toString())
            this.mainProcessTask.setIntent(intent)
            this.mainProcessTask.execAsync()
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    @SyncJsApi(methodName = "ido_getGtcId")
    fun getGtcId(data: JSONObject?, activity: Activity) {
        try {
            val intent = Intent("getGtcId")
            this.mainProcessTask.setIntent(intent)
            this.mainProcessTask.execAsync()
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }


    @SyncJsApi(methodName = "ido_trackCountEvent")
    fun onEvent(data: JSONObject, activity: Activity) {
        try {
            val intent = Intent("onEvent")
            intent.putExtra("param", data.toString())
            this.mainProcessTask.setIntent(intent)
            this.mainProcessTask.execAsync()
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    @SyncJsApi(methodName = "ido_onBeginEvent")
    fun onBeginEvent(data: JSONObject, activity: Activity) {
        try {
            val intent = Intent("onBeginEvent")
            intent.putExtra("param", data.toString())
            this.mainProcessTask.setIntent(intent)
            this.mainProcessTask.execAsync()
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }


    /**
     * 计时统计结束
     *  eventId：自定义事件 Id ，用于标识事件的唯一
     *  map: 自定义属性，用于扩展统计需求
     */

    @SyncJsApi(methodName = "ido_onEndEvent")
    fun onEndEvent(data: JSONObject, activity: Activity) {
        try {
            val intent = Intent("onEndEvent")
            intent.putExtra("param", data.toString())
            this.mainProcessTask.setIntent(intent)
            this.mainProcessTask.execAsync()
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    /**
     * 用户属性设置
     *  map: 自定义用户属性，用于扩展统计需求
     */


    @SyncJsApi(methodName = "ido_setProfile")
    fun setProfile(data: JSONObject, activity: Activity) {
        try {
            val intent = Intent("setProfile")
            intent.putExtra("param", data.toString())
            this.mainProcessTask.setIntent(intent)
            this.mainProcessTask.execAsync()
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }


    /**
     * 设置计数事件上传频率
     * timeMillis：设置的eventUploadInterval值，单位毫秒。
     * 默认值为10秒
     */
    @SyncJsApi(methodName = "ido_setEventUploadInterval")
    fun setEventUploadInterval(data: JSONObject, activity: Activity) {
        try {
            val intent = Intent("setEventUploadInterval")
            intent.putExtra("param", data.toString())
            this.mainProcessTask.setIntent(intent)
            this.mainProcessTask.execAsync()
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    /**
     * 设置计数事件事件强制上传条数
     * size：设置计数事件的强制上传条数eventForceUploadSize
     * 默认数量为30条；
     */
    @SyncJsApi(methodName = "ido_setEventForceUploadSize")
    fun setEventForceUploadSize(data: JSONObject, activity: Activity) {
        try {
            val intent = Intent("setEventForceUploadSize")
            intent.putExtra("param", data.toString())
            this.mainProcessTask.setIntent(intent)
            this.mainProcessTask.execAsync()
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    /**
     * 设置用户属性事件上传频率
     * timeMillis：单位毫秒。设置用户属性事件传频率profileUploadInterval
     * 默认值为5秒
     */
//    IdoFlutter().setProfileUploadInterval(Long timeMillis)

    @SyncJsApi(methodName = "ido_setProfileUploadInterval")
    fun setProfileUploadInterval(data: JSONObject, activity: Activity) {
        try {
            val intent = Intent("setProfileUploadInterval")
            intent.putExtra("param", data.toString())
            this.mainProcessTask.setIntent(intent)
            this.mainProcessTask.execAsync()
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    /**
     * 设置用户属性事件强制上传条数
     * size 设置用户属性事件的强制上传条数profileForceUploadSize
     * 默认数量为5条
     */


    @SyncJsApi(methodName = "ido_setProfileForceUploadSize")
    fun setProfileForceUploadSize(data: JSONObject, activity: Activity) {
        try {
            val intent = Intent("setProfileForceUploadSize")
            intent.putExtra("param", data.toString())
            this.mainProcessTask.setIntent(intent)
            this.mainProcessTask.execAsync()
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    @SyncJsApi(methodName = "ido_getVersion")
    fun ido_getVersion(data: JSONObject, activity: Activity): String {
        return GsManager.getInstance().version
    }

    fun areNotificationsEnabled(context: Context): Boolean {
        try {
            val CHECK_OP_NO_THROW = "checkOpNoThrow"
            val OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION"
            if (Build.VERSION.SDK_INT >= 24) {
                val mNotificationManager =
                    context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                val mtd: Method =
                    NotificationManager::class.java.getDeclaredMethod("areNotificationsEnabled")
                return mtd.invoke(mNotificationManager) as Boolean
            } else if (Build.VERSION.SDK_INT >= 19) {
                val appOps = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
                val appInfo: ApplicationInfo = context.applicationInfo
                val pkg: String = context.applicationContext.packageName
                val uid: Int = appInfo.uid
                val appOpsClass = Class.forName(AppOpsManager::class.java.name)
                val checkOpNoThrowMethod: Method = appOpsClass.getMethod(
                    CHECK_OP_NO_THROW,
                    Int::class.javaPrimitiveType,
                    Int::class.javaPrimitiveType,
                    String::class.java
                )
                val opPostNotificationValue = appOpsClass.getDeclaredField(OP_POST_NOTIFICATION)
                val value = opPostNotificationValue.get(Int::class.javaPrimitiveType) as Int
                return checkOpNoThrowMethod.invoke(
                    appOps,
                    value,
                    uid,
                    pkg
                ) as Int == AppOpsManager.MODE_ALLOWED
            }
        } catch (e: Throwable) {
            Log.e(TAG, "Error checking notifications enabled", e)
        }

        return true
    }
}
