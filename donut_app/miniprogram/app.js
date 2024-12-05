// app.js
// const GsIdo = require('./gsido-min-dev.js')
// const GtPush = require('./gtpush-min-dev.js')
App({
  onLaunch() {
    // 展示本地存储能力
    const logs = wx.getStorageSync('logs') || []
    logs.unshift(Date.now())
    wx.setStorageSync('logs', logs)

    // wx 预调用
    // wx.GSIDOLazyActions = []
    // wx.GSIDOLazyActions.push({key:'setDebugEnable', args:[true]})

    // GsIdo.setDebugEnable(9);
    // GsIdo.init({
    //   //统计sdk appid
    //   gsAppid: 'xXmjbbab3b5F1m7wAYZoG2'
    //   // gsAppid: 'A888n2pctF4BRpX7km8888'
    // });

    // GsIdo.setUserId("123")
    // console.log("gtcid=",GsIdo.gtcid())



  //   GtPush.setSocketServer({
  //     url: 'wss://wshzn.getui.net:5223/nws',
  //     key: 'MHwwDQYJKoZIhvcNAQEBBQADawAwaAJhAJp1rROuvBF7sBSnvLaesj2iFhMcY8aXyLvpnNLKs2wjL3JmEnyr++SlVa35liUlzi83tnAFkn3A9GB7pHBNzawyUkBh8WUhq5bnFIkk2RaDa6+5MpG84DEv52p7RR+aWwIDAQAB',
  //     keyId: '69d747c4b9f641baf4004be4297e9f3b'
  // });

  // GtPush.setDebugMode(true)
  // GtPush.init({
  //     appid: 'xXmjbbab3b5F1m7wAYZoG2',
  //     onClientId: function (res) {
  //         console.log('gtsdk onclientid', JSON.stringify(res))
         
  //     },
  //     onlineState: function (res) {
  //         console.log('gtsdk onlineState', JSON.stringify(res))
          
  //     },
  //     onPushMsg: function (res) {
  //         var msg = JSON.stringify(res)
  //         console.log('gtsdk onPushMsg', msg)
  //         var data = $("#messages").text() + "onPushMsg: " + msg + "\n"
        
  //     },
  //     onError: function (res) {
  //         console.log('gtsdk onError', JSON.stringify(res))
  //         var data = $("#messages").text() + "onError: " + res + "\n"
  //     }
  // })


    // 登录
    wx.login({
      success: res => {
        // 发送 res.code 到后台换取 openId, sessionKey, unionId
      }
    })
  },
  globalData: {
    userInfo: null
  }
})
