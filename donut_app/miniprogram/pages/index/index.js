// index.js
const defaultAvatarUrl = 'https://mmbiz.qpic.cn/mmbiz/icTdbqWNOwNRna42FI242Lcia07jQodd2FJGIYQfG0LAJGFxM4FbnQP6yfMxBgJ0F3YRqJCJ1aPAK2dQagdusBZg/0'
// import GsIdo from '../../gsido-min-dev.js'
const {
  miniAppPluginId
} = require('../../envList');
//miniprogram/envList.js
Component({
  data: {
    myPlugin: undefined,
    motto: 'Hello World',
    userInfo: {
      avatarUrl: defaultAvatarUrl,
      nickName: '',
    },
    hasUserInfo: false,
    canIUseGetUserProfile: wx.canIUse('getUserProfile'),
    canIUseNicknameComp: wx.canIUse('input.type.nickname'),
  },
  methods: {
    onLoadPlugin() {
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
          //监听native的事件
          plugin.onMiniPluginEvent(listener1)
          //可以设置多个监听
          //plugin.onMiniPluginEvent(listener2)
          this.setData({
            myPlugin: plugin
          })
        },
        fail: (e) => {
          console.log('load plugin fail', e)
        }
      })
    },
    onLoad() {
      // this.onLoadPlugin()
    },

    // 以下是GTSDK SDK示例
    gt_onStartSdk() {
      const {
        myPlugin
      } = this.data;

      const deviceInfo = wx.getDeviceInfo()
      if ("android" === deviceInfo.platform) {
        myPlugin.gt_initialize()
      } else {
        console.log("onStartSdk", myPlugin);
        myPlugin.gt_startSdk({
          'appId': 'xXmjbbab3b5F1m7wAYZoG2',
          'appKey': 'BZF4dANEYr8dwLhj6lRfx2',
          'appSecret': 'yXRS5zRxDt8WhMW8DD8W05'
        })
      }
    },
    launchNotification() {
      const {
        myPlugin
      } = this.data;
      let noti = myPlugin.gt_launchNotification()
      console.log(noti)
    },
    gt_getVersion() {
      const {
        myPlugin
      } = this.data;
      let ver = myPlugin.gt_getVersion()
      console.log(ver)
    },
    // 以下是IDO SDK示例
    ido_onStartSdk() {
      const {
        myPlugin
      } = this.data;
      const deviceInfo = wx.getDeviceInfo()
      if ("android" === deviceInfo.platform) {
        myPlugin.ido_init({"channel":"donut","appid":"djYjSlFVMf6p5YOy2OQUs8"})
      }else{
      console.log("onStartSdk", myPlugin);
      myPlugin.ido_startSdk({
        'appId': 'xXmjbbab3b5F1m7wAYZoG2',
        'channelId': ''
      })}
    },
    ido_getVersion() {
      const {
        myPlugin
      } = this.data;
      let ver = myPlugin.ido_getVersion()
      console.log(ver)
    },
    bindViewTap() {
      wx.navigateTo({
        url: '../logs/logs'
      })
    },
    onChooseAvatar(e) {
      console.info("choose");
      const {
        avatarUrl
      } = e.detail
      const {
        nickName
      } = this.data.userInfo
      this.setData({
        "userInfo.avatarUrl": avatarUrl,
        hasUserInfo: nickName && avatarUrl && avatarUrl !== defaultAvatarUrl,
      })
    },
    onInputChange(e) {
      const nickName = e.detail.value
      const {
        avatarUrl
      } = this.data.userInfo
      this.setData({
        "userInfo.nickName": nickName,
        hasUserInfo: nickName && avatarUrl && avatarUrl !== defaultAvatarUrl,
      })
    },
    getUserProfile(e) {
      // 推荐使用wx.getUserProfile获取用户信息，开发者每次通过该接口获取用户个人信息均需用户确认，开发者妥善保管用户快速填写的头像昵称，避免重复弹窗
      wx.getUserProfile({
        desc: '展示用户信息', // 声明获取用户个人信息后的用途，后续会展示在弹窗中，请谨慎填写
        success: (res) => {
          console.log("getUserProfile " + res)
          this.setData({
            userInfo: res.userInfo,
            hasUserInfo: true
          })
        }
      })
    },
    testOpenID() {
      console.log("request begin");
      wx.request({
        url: 'url', //需要填写后端的url。
        success: function (res) {
          console.log("request success " + res.OpenID);
        },
        complete: function () {
          console.log("request complete");
        }
      })
    },
    toLogin() {
      console.log("login")
      wx.login({
        //成功放回
        success: (res) => {
          console.log(res);
          console.log("login result: " + res.code);
          let code = res.code
        }
      })
    },
  },
})