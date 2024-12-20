#个推SDK donut插件集成文档


# **概述**

个推运营工具SDK在 [donut平台](https://dev.weixin.qq.com)的插件示例。开发者需要将插件源码上传到donut插件管理库中使用。

流程： 开发者需要先加载插件，后调用插件具体API，比如startSdk


## android API说明：
### Android appid配置：

```js
// 厂商不使用也要留空字符串
如:
 "mini-android": {
    "manifestPlaceholders": {
      "GETUI_APPID": "个推appid",
      "HUAWEI_APP_ID": "华为id",
      "XIAOMI_APP_ID": "小米id",
      "XIAOMI_APP_KEY": "小米key",
      "OPPO_APP_KEY": "oppo key",
      "OPPO_APP_SECRET": "oppo secret",
      "VIVO_APP_ID": "vivo id",
      "VIVO_APP_KEY": "vivo key",
      "MEIZU_APP_ID": "meizu id",
      "MEIZU_APP_KEY": "meizu key",
      "HONOR_APP_ID": "honor"
    },
 }
```



## iOS 使用说明：
插件桥接了原生SDK API，原生SDK API具体说明可参考[官网文档中心 iOS API](https://docs.getui.com/ido/mobile/ios/api/)

### 插件js api说明：
```js
usage() { 

	  //加载插件
    wx.miniapp.loadNativePlugin({
      pluginId: miniAppPluginId,
      success: (plugin) => {
        console.log('load plugin success', plugin)
        //监听原生sdk向js发送的事件 （lisenner定义后续说明）
        plugin.onMiniPluginEvent(listener)
        this.setData({
          myPlugin: plugin
        })
      },
      fail: (e) => {
        console.log('load plugin fail', e)
      }
    });
    
    const {
      myPlugin
    } = this.data;
    
  
    myPlugin.setApplicationGroupIdentifier({
      'identifier': 'group.ent.com.getui.www'
    })

    myPlugin.setEventUploadInterval({
      'timeMillis': 5000
    }) 
  
    myPlugin.setEventForceUploadSize({
      'size': 30
    }) 
  
    myPlugin.setProfileUploadInterval({
      'timeMillis':5000
    })
    
    myPlugin.setProfileForceUploadSize({
       'size':5
    })
    //上述对sdk的属性配置，需要在startsdk之前调用。
     
    //启动sdk
    myPlugin.startSdk({
      'appId': 'xXmjbbab3b5F1m7wAYZoG2',
      'channelId': ''
    })
		
		//日志开关
    myPlugin.setDebugEnable({
      'debugEnable': 1
    })

	 	//获取gtcid
    console.log('gtcid=', myPlugin.getGtcId());
 
  	//计时事件-开始
    myPlugin.onBeginEvent({
      'eventId': '001'
    })

		//计时事件-结束
    myPlugin.onEndEvent({
      'eventId': '001',
      'jsonObject': {
        'name': 'superman',
        'age': 18
      },
      'withExt': 'this is ext string'
    })
    
    //计数事件
    myPlugin.trackCountEvent({
      'eventId': '002',
      'jsonObject': {
        'name': 'superman2',
        'age': 19
      },
      'withExt': 'this is ext string2'
    })
    
    //设置用户属性
    myPlugin.setProfile({
      'jsonObject': {
        'property1': 'value1',
        'property2': 100
      },
      'withExt':'this is ext string3'
    })


    //获取原生sdk版本号
    let ver = myPlugin.getVersion()
    console.log(ver)

```

### 插件回调说明：

```js
const listener = (param) => {
      console.log('onMiniPluginEvent listener:', param)
      //监听事件总览：
			/*
			method:gtcIdCallback
			param:{"result":"gtcidxxx","error":""}
			说明：
			gtcid回调
			*/
    }
```





