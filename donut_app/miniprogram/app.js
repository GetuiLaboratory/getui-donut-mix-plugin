// app.js
// const GsIdo = require('./gsido-min-dev.js')
// const GtPush = require('./gtpush-min-dev.js')
const {
  miniAppPluginId
} = require('./envList');
App({
  onLaunch() {
    // 展示本地存储能力
    const logs = wx.getStorageSync('logs') || []
    logs.unshift(Date.now())
    wx.setStorageSync('logs', logs)
    console.log("onLoadPlugin");
    const listener1 = (param) => {
      console.log('onMiniPluginEvent listener:', param)
    }

    const listener2 = (param) => {
      console.log('onMiniPluginEvent listener2:', param)
    }

    wx.miniapp.loadNativePlugin({
      pluginId: miniAppPluginId,
      success: (plugin) => {
        console.log('load plugin success', plugin)
        
        let noti = plugin.gt_launchNotification()
        console.log(noti)
        
        //监听native的事件
        plugin.onMiniPluginEvent(listener1)

        //可以设置多个监听
        //plugin.onMiniPluginEvent(listener2)
        // this.setData({
        //   myPlugin: plugin
        // })
        this.globalData.myPlugin = plugin
      },
      fail: (e) => {
        console.log('load plugin fail', e)
      }
    })


    // 登录
    wx.login({
      success: res => {
        // 发送 res.code 到后台换取 openId, sessionKey, unionId
      }
    })
  },
  globalData: {
    userInfo: null,
    myPlugin: null,
  }
})
