### 目录介绍
- donut_app: 多端应用Demo
- gt_ido: gtsdk+idosdk 多端插件工程


### iOS集成注意事项：

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

