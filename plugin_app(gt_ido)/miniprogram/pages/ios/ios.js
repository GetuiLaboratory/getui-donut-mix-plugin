// pages/ios/ios.js
const {
  miniAppPluginId
} = require('../../constant');

Page({
  /**
   * 页面的初始数据
   */
  data: {
    myPlugin: undefined,
    quickStartContents: [
      '在「设置」->「安全设置」中手动开启多端插件服务端口',
      '在「工具栏」->「运行设备」中选择 iOS 点击「运行」，快速准备运行环境',
      '在打开的 Xcode 中点击「播放」运行原生工程',
      '保持开发者工具开启，修改小程序代码和原生代码仅需在 Xcode 中点击「播放」查看效果',
    ]
  },

  onLoadPlugin() {
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
  // 以下是IDO SDK示例
  gt_onStartSdk() {
    const {
      myPlugin
    } = this.data;
    console.log("onStartSdk", myPlugin);
    myPlugin.gt_startSdk({
      'appId': 'xXmjbbab3b5F1m7wAYZoG2',
      'appKey': 'BZF4dANEYr8dwLhj6lRfx2',
      'appSecret': 'yXRS5zRxDt8WhMW8DD8W05'
    })
  },
  setPushMode() {
    const {
      myPlugin
    } = this.data;
    myPlugin.gt_setPushMode({
      'mode': 1
    })
  },
  runBackgroundEnable() {
    const {
      myPlugin
    } = this.data;
    myPlugin.gt_runBackgroundEnable({
      'enable': 1
    }) //1 or 0
  },
  bindAlias() {
    const {
      myPlugin
    } = this.data;
    myPlugin.gt_bindAlias({
      'alias': 'superman',
      'sn': '0001'
    })
  },
  unbindAlias() {
    const {
      myPlugin
    } = this.data;
    myPlugin.gt_unbindAlias({
      'alias': 'superman',
      'sn': '0001'
    })
  },
  setTags() {
    const {
      myPlugin
    } = this.data;
    myPlugin.gt_setTags({
      'tags': ['t1', 't2', 't2']
    })
  },

  setBadge() {
    const {
      myPlugin
    } = this.data;
    myPlugin.gt_setBadge({
      'badge': 3
    })
  },

  setLocalBadge() {
    const {
      myPlugin
    } = this.data;
    myPlugin.gt_setLocalBadge({
      'badge': 4
    })
  },

  registerActivityToken() {
    const {
      myPlugin
    } = this.data;
    myPlugin.gt_registerActivityToken({
      'aid': '1234',
      'token': 'token1',
      'sn': '0001'
    })
  },

  registerPushToStartToken() {
    const {
      myPlugin
    } = this.data;
    myPlugin.gt_registerPushToStartToken({
      'attribute': 'MyAttribute',
      'token': 'token2',
      'sn': '0002'
    })
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
    console.log("onStartSdk", myPlugin);
    myPlugin.ido_startSdk({
      'appId': 'xXmjbbab3b5F1m7wAYZoG2',
      'channelId': ''
    })
  },
  setDebugEnable() {
    const {
      myPlugin
    } = this.data;
    myPlugin.ido_setDebugEnable({
      'debugEnable': 1
    })
  },
  getGtcId() {
    const {
      myPlugin
    } = this.data;
    console.log('gtcid=', myPlugin.ido_getGtcId());
  },
  onBeginEvent() {
    const {
      myPlugin
    } = this.data;
    myPlugin.ido_onBeginEvent({
      'eventId': '001'
    })
  },
  onEndEvent() {
    const {
      myPlugin
    } = this.data;
    myPlugin.ido_onEndEvent({
      'eventId': '001',
      'jsonObject': {
        'name': 'superman',
        'age': 18
      },
      'withExt': 'this is ext string'
    })
  },
  trackCountEvent() {
    const {
      myPlugin
    } = this.data;
    myPlugin.ido_trackCountEvent({
      'eventId': '002',
      'jsonObject': {
        'name': 'superman2',
        'age': 19
      },
      'withExt': 'this is ext string2'
    })
  },

  setProfile() {
    const {
      myPlugin
    } = this.data;
    myPlugin.ido_setProfile({
      'jsonObject': {
        'property1': 'value1',
        'property2': 100
      },
      'withExt':'this is ext string3'
    })
  },

  setApplicationGroupIdentifier() {
    const {
      myPlugin
    } = this.data;
    myPlugin.ido_setApplicationGroupIdentifier({
      'identifier': 'group.ent.com.getui.www'
    })
  },

  setEventUploadInterval() {
    const {
      myPlugin
    } = this.data;
    myPlugin.ido_setEventUploadInterval({
      'timeMillis': 5000
    })
  },

  setEventForceUploadSize() {
    const {
      myPlugin
    } = this.data;
    myPlugin.ido_setEventForceUploadSize({
      'size': 30
    })
  },
  setProfileUploadInterval() {
    const {
      myPlugin
    } = this.data;
    myPlugin.ido_setProfileUploadInterval({
      'timeMillis':5000
    })
  },
  setProfileForceUploadSize() {
    const {
      myPlugin
    } = this.data;
     myPlugin.ido_setProfileForceUploadSize({
       'size':5
     })

  },
  ido_getVersion() {
    const {
      myPlugin
    } = this.data;
    let ver = myPlugin.ido_getVersion()
    console.log(ver)
  },
  onUsePlugin() {
    const {
      myPlugin
    } = this.data
    if (!myPlugin) {
      console.log('plugin is undefined')
      return
    }
    const ret = myPlugin.mySyncFunc({
      a: 'hello',
      b: [1, 2]
    })
    console.log('mySyncFunc ret:', ret)

    myPlugin.myAsyncFuncwithCallback({
      a: 'hello',
      b: [1, 2]
    }, (ret) => {
      console.log('myAsyncFuncwithCallback ret:', ret)
    })
  },

  copyLink() {
    wx.setClipboardData({
      data: 'https://dev.weixin.qq.com/docs/framework/dev/plugin/iosPlugin.html',
    })
  }
})