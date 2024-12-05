import android.content.Intent
import android.os.*
import android.util.Log
import com.donut.wxed34e654870cba42.IdoOnForegroundChangedListener
import com.donut.wxed34e654870cba42.PluginIntentService
import com.donut.wxed34e654870cba42.PluginManager
import com.getui.gs.ias.core.GsConfig
import com.getui.gs.sdk.GsManager
import com.getui.gs.sdk.IGtcIdCallback
import com.getui.gtc.base.GtcProvider
import com.getui.gtc.base.util.CommonUtil
import com.igexin.sdk.IUserLoggerInterface
import com.igexin.sdk.PushConsts
import com.igexin.sdk.PushManager
import com.tencent.luggage.wxa.SaaA.plugin.NativePluginMainProcessTask
import kotlinx.android.parcel.Parcelize
import org.json.JSONObject

@Parcelize
class MainProcessTask(private var msgCallback: Intent) :
    NativePluginMainProcessTask() {
    private var clientCallback: ((Intent) -> Unit)? = null

    companion object {
        private var hasSetGT = false
        private var TAG: String = "MainProcessTask"
        private var lifecycle = object : IdoOnForegroundChangedListener() {
            override fun onForegroundChanged(isForeground: Boolean) {
                try {
                    Log.d(TAG, "onForegroundChanged isForeground ${isForeground}")
                    GsManager.getInstance().updateAppForegroundState(isForeground)
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
        }
    }

    fun setClientCallback(callback: (data: Intent) -> Unit) {
        this.clientCallback = callback
    }

    override fun parseFromParcel(parcel: Parcel?) {
        // 如果需要获得主进程数据，需要重写parseFromParcel，手动解析Parcel
        this.msgCallback = parcel?.readParcelable(Intent::class.java.classLoader) ?: Intent()
    }

    /**
     * 运行在主进程的逻辑，不建议在主进程进行耗时太长的操作
     */
    override fun runInMainProcess() {
        // 如果需要把主进程的数据回调到小程序进程，就赋值后调用 callback 函数
        when (msgCallback.action) {
            "gt_initialize"->{
                if (!hasSetGT) {
                    Log.d(TAG, "runInMainProcess execute hasSetGT ")
                    hasSetGT = true
                    PushManager.getInstance()
                        .setDebugLogger(GtcProvider.context(), object : IUserLoggerInterface {
                            override fun log(log: String) {
                                Log.e(TAG, log)
                                val intent = Intent()
                                val bundle = Bundle()
                                bundle.putInt(PushConsts.CMD_ACTION, PluginManager.GT_LOG)
                                bundle.putString("log", log)
                                intent.putExtras(bundle)
                                callbackByMainProcess(intent)
                            }
                        })
                    // 如果需要把主进程的数据回调到小程序进程，就赋值后调用 callback 函数
                    PluginIntentService.setMainProcessTask(this);
                }
            }
            "init" -> {
                init(msgCallback)
            }
            "setDebugEnable" -> {
                setDebugEnable(msgCallback)
            }
            "getGtcId" -> {
                getGtcId()
            }
            "onEvent" -> {
                onEvent(msgCallback)
            }
            "onBeginEvent" -> {
                onBeginEvent(msgCallback)
            }
            "onEndEvent" -> {
                onEndEvent(msgCallback)
            }
            "setProfile" -> {
                setProfile(msgCallback)
            }
            "setEventUploadInterval" -> {
                setEventUploadInterval(msgCallback)
            }

            "setEventForceUploadSize" -> {
                setEventForceUploadSize(msgCallback)
            }
            "setProfileUploadInterval" -> {
                setProfileUploadInterval(msgCallback)

            }
            "setProfileForceUploadSize" -> {
                setProfileForceUploadSize(msgCallback)
            }
            "onActivityResumed" -> {
                onActivityResumed(msgCallback)
            }
            "onActivityPaused" -> {
                onActivityPaused(msgCallback)
            }
            "onActivityStopped" -> {
                onActivityStopped(msgCallback)
            }

        }
    }

    public fun callbackByMainProcess(obtain: Intent) {
//        Log.e("MainProcessTask", "callbackByMainProcess")
        msgCallback = obtain
        this.callback()
    }

    /**
     * 运行在小程序进程的逻辑
     */
    override fun runInClientProcess() {
//        Log.e("ClientProcess", "msgCallback:${msgCallback}")
        this.clientCallback?.let { callback ->
            callback(msgCallback)
        }
    }



    fun setIntent(intent: Intent) {
        this.msgCallback = intent
    }


    fun init(intent: Intent) {

        val param = intent.getStringExtra("param")
        val jsonObject = JSONObject(param)
        val appid = if (jsonObject.has("appid")) jsonObject.get("appid") else null
        if (appid != null) {
            GsConfig.setAppId(appid as String)
        }

        GsConfig.setInstallChannel(jsonObject.getString("channel") as String)
        //测试失败
        var callback: IGtcIdCallback = object : IGtcIdCallback {
            override fun onGetGtcId(gtcId: String?) {
                Log.d(TAG, "onGetGtcId:${gtcId}")
                val intent = Intent()
                val bundle = Bundle()
                bundle.putInt(PushConsts.CMD_ACTION,PluginManager.IDO_GTCID);
                bundle.putString("gtcId", gtcId)
                intent.putExtras(bundle)
                callbackByMainProcess(intent)
            }
        }
        GsManager.getInstance().setGtcIdCallback(callback)
        GsManager.getInstance().preInit(GtcProvider.context())
        GsManager.getInstance().init(GtcProvider.context())
    }

    fun setDebugEnable(intent: Intent) {
        val param = intent.getStringExtra("param")
        val jsonObject = JSONObject(param)
        val debugEnable = jsonObject.getInt("debugEnable")
        GsConfig.setDebugEnable(debugEnable == 1)
    }

    fun getGtcId() {
        val gtcId = GsManager.getInstance().getGtcId()
        val intent = Intent()
        val bundle = Bundle()
        bundle.putInt(PushConsts.CMD_ACTION,PluginManager.IDO_GTCID);
        bundle.putString("gtcId", gtcId)
        intent.putExtras(bundle)
        callbackByMainProcess(intent)
    }

    fun onEvent(intent: Intent) {
        val param = intent.getStringExtra("param")
        val jsonObject = JSONObject(param)
        val eventId = jsonObject.getString("eventId")
        val jsonObj =
            if (jsonObject.has("jsonObject")) jsonObject.getJSONObject("jsonObject") else null
        val withExt = if (jsonObject.has("withExt")) jsonObject.getString("withExt") else null
        GsManager.getInstance().onEvent(eventId, jsonObj, withExt)
    }

    fun setProfile(intent: Intent) {
        val param = intent.getStringExtra("param")
        val jsonObject = JSONObject(param)
        val jsonObj = jsonObject.getJSONObject("jsonObject")
        val withExt = if (jsonObject.has("withExt")) jsonObject.getString("withExt") else null
        GsManager.getInstance().setProfile(jsonObj, withExt)
    }

    fun onBeginEvent(intent: Intent) {
        val param = intent.getStringExtra("param")
        val jsonObject = JSONObject(param)
        val eventId = jsonObject.getString("eventId")
        val jsonObj =
            if (jsonObject.has("jsonObject")) jsonObject.getJSONObject("jsonObject") else null
        GsManager.getInstance().onBeginEvent(eventId, jsonObj)
    }

    fun onEndEvent(intent: Intent) {
        val param = intent.getStringExtra("param")
        val jsonObject = JSONObject(param)
        val eventId = jsonObject.getString("eventId")
        val jsonObj =
            if (jsonObject.has("jsonObject")) jsonObject.getJSONObject("jsonObject") else null
        val ext = if (jsonObject.has("ext")) jsonObject.getString("ext") else null
        GsManager.getInstance().onEndEvent(eventId, jsonObj, ext)
    }

    fun setEventUploadInterval(intent: Intent) {
        val param = intent.getStringExtra("param")
        val jsonObject = JSONObject(param)
        GsConfig.setEventUploadInterval(jsonObject.getLong("timeMillis"))
    }

    fun setEventForceUploadSize(intent: Intent) {
        val param = intent.getStringExtra("param")
        val jsonObject = JSONObject(param)
        GsConfig.setEventForceUploadSize(jsonObject.getInt("size"))
    }

    fun setProfileUploadInterval(intent: Intent) {
        val param = intent.getStringExtra("param")
        val jsonObject = JSONObject(param)
        GsConfig.setProfileUploadInterval(jsonObject.getLong("timeMillis"))
    }

    fun setProfileForceUploadSize(intent: Intent) {
        val param = intent.getStringExtra("param")
        val jsonObject = JSONObject(param)
        GsConfig.setProfileForceUploadSize(jsonObject.getInt("size"))
    }

    private fun onActivityResumed(intent: Intent) {
        Log.d(TAG, "onActivityResumed")
        GsManager.getInstance().onActivityResumed()
        lifecycle.onActivityResumed(null)
    }

    private fun onActivityPaused(intent: Intent) {
        Log.i(TAG, "onActivityPaused isForeground ${CommonUtil.isAppForeground()}")
        GsManager.getInstance().onActivityPaused()

        lifecycle.onActivityPaused(null)
        Log.i(TAG, "onActivityPaused after isForeground ${CommonUtil.isAppForeground()}")
    }

    private fun onActivityStopped(intent: Intent) {
        lifecycle.onActivityStopped(null)
    }
}