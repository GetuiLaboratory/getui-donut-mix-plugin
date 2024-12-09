// pages/android/android.js
const { miniAppPluginId } = require('../../constant');

Page({
  /**
   * 页面的初始数据
   */
  data: {
    myPlugin: undefined,
    quickStartContents: [
      '在「设置」->「安全设置」中手动开启多端插件服务端口',
      '在「工具栏」->「运行设备」中选择 Android 点击「运行」，快速准备运行环境',
      '在打开的 Android Stuido 中点击「播放」运行原生工程',
      '保持开发者工具开启，修改小程序代码和原生代码仅需在 Android Stuido 中点击「播放」查看效果',
    ]
  },

  onLoadPlugin() {
    const listener = (param) => {
      console.log('onMiniPluginEvent listener '+JSON.stringify(param))
      switch(param["method"]){
          case "onReceiveClientId":
              this.sdkMethod()
            break
      }
    }

    wx.miniapp.loadNativePlugin({
      pluginId: miniAppPluginId,
      success: (plugin) => {
        console.log('load plugin success', plugin)
        plugin.onMiniPluginEvent(listener)
        this.setData({
          myPlugin: plugin
        })
        //plugin.gt_openNotification()
      },
      fail: (e) => {
        console.log('load plugin fail', e)
      }
    })
  },

 
  sdkMethod(){
    const { myPlugin } = this.data
    var  state =  myPlugin.gt_areNotificationsEnabled()
    console.log('plugin notify state '+state)
    // myPlugin.gt_setTag({"tags":["a","b","c"],"sn":"1111"})
    // myPlugin.gt_queryTag({"sn":"ddd"})
   var id =  myPlugin.gt_getClientid()
   console.log('plugin getClientid '+id)
  //  myPlugin.gt_turnOffPush()
  //  myPlugin.gt_turnOnPush()
   var isPushTurnedOn = myPlugin.gt_isPushTurnedOn()
   console.log('plugin isPushTurnedOn '+isPushTurnedOn)
  //  myPlugin. gt_bindAlias({"alias":"12345"})
  //  myPlugin. gt_unBindAlias({"alias":"12345"})
  //  myPlugin. gt_setLocalBadge({"badge":2})
  //  myPlugin.gt_sendFeedbackMessage({"taskid":"dddd",
  // "messageid":"ddddd",actionid:90002})
  },
  copyLink() {
    wx.setClipboardData({
      data: 'https://dev.weixin.qq.com/docs/framework/dev/plugin/androidPlugin.html',
    })
  },

  onUseIdoPlugin() {
    const { myPlugin } = this.data
    if (!myPlugin) {
      console.log('plugin is undefined')
      return
    }
    myPlugin.ido_setDebugEnable({"debugEnable":1})
    
    myPlugin.ido_setEventUploadInterval({
      'timeMillis': 5000
    }) 
  
    myPlugin.ido_setEventForceUploadSize({
      'size': 30
    }) 
  
    myPlugin.ido_setProfileUploadInterval({
      'timeMillis':5000
    })
    
    myPlugin.ido_setProfileForceUploadSize({
       'size':5
    })
    
    myPlugin.ido_init({"channel":"donut","appid":"djYjSlFVMf6p5YOy2OQUs8"})
    const gtcid =  myPlugin.ido_getGtcId()
    console.log('getGtcId '+gtcid)
    let ver = myPlugin.ido_getVersion()
    console.log(ver)

    myPlugin.ido_trackCountEvent(
      {"eventId":"idididid","jsonObject":{"a":1,"b":"你好"},"withExt":"dddddddd"})

      myPlugin.ido_setProfile({'jsonObject': {
        'property1': 'value1',
        'property2': 100
      },
      'withExt':'this is ext string3'})
    
      myPlugin.ido_onBeginEvent({"eventId":"qqqq","jsonObject":{"aaa":"ddddd"}})
      myPlugin.ido_onEndEvent({"eventId":"qqqq","jsonObject":{"aaa":"ddddd"}})
  
    },

    onUsePlugin() {
      this.onUseIdoPlugin()
      const { myPlugin } = this.data
      if (!myPlugin) {
        console.log('plugin is undefined')
        return
      }
      myPlugin.gt_initialize()
      console.log('initialize')
    },
})