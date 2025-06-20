# 第三方消息推送插件使用指南

### 目录介绍
- donut_app: 多端应用Demo
- gt_ido: gtsdk+idosdk 多端插件工程


## 一、iOS 对接指南

### 1. 配置插件

- 在微信开发者工具，前往`project.miniapp.json`，点击右上角切换到 json 模式，然后按照将下方内容添加`project.miniapp.json`

```json
"mini-plugin": {
  "ios": [
    {
      "open": true,
        "pluginId": "wxed34e654870cba42",
        "pluginVersion": "0.1.3",
        "loadWhenStart": true,
        "isFromLocal": false,
        "appexProfiles": { //如果需要配置通知扩展
          "NSE": {
            "enable": true,
            "bundleID": "com.xxx.app.NotificationService", //自行申请对应证书和描述文件
            "profilePath": "Dev_comxxxappNotificationService.mobileprovision",
            "distributeProfilePath": "Dev_comxxxappNotificationService.mobileprovision"
          }
        }
    },
  ]
```


### 其他注意事项：

project.miniapp.json文件中，需要设置Info.plist自定义键值，customInfoPlist设置如下：
```js
 "mini-ios": {
    "sdkVersion": "1.5.0",
    "toolkitVersion": "0.0.9",
    "customInfoPlist": {
      "GT_MinimumOSVersion": "11"
    },
}
```
- 注意：iOS多端应用需要使用微信开发者工具1.06.2412042版本及以上，才能支持自定义Info.plist自定义键值




