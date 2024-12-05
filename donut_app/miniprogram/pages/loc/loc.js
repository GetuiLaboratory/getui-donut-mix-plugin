// pages/loc/loc.js

const _locationChangeFn = function (res) {
  console.log('location change', res)
}

Page({

  /**
   * 页面的初始数据
   */
  data: {},
  onLoad(options) {
    wx.getSetting({
      success: (res) => {
        console.log("getSetting ", res)
        if (res.authSetting["scope.userLocation"]) {
          //已授权
          console.log("已授权")
        }
      }
    })
  },
  getLocation() {
    console.log("getLocation")
    let typestr = 'gcj02' // TODO:混淆
    let cb = {
      type: typestr,
      success(res) {
        let la = 'latitude' // TODO:混淆
        let lo = 'longitude' // TODO:混淆
        let s = 'speed' // TODO:混淆
        let ac = 'accuracy' // TODO:混淆
        console.log("loc success ", res[la])
      }
    };
    let f = wx["getLocation"]; // TODO:混淆。后续会将getLocation编码，运行的时候再解码
    f.apply(wx, [cb]);
  },
  getLocation2() {
    console.log("getLocation")

    // 获取定位
    wx.getLocation({
      // type: 'wgs84',
      //工具中定位模拟使用IP定位，可能会有一定误差。且工具目前仅支持 gcj02 坐标。
      type: 'gcj02',
      success(res) {
        const latitude = res.latitude
        const longitude = res.longitude
        const speed = res.speed
        const accuracy = res.accuracy
        console.log("loc success ", res)
      },
      fail(res) {
        console.log("loc fail ", res)
      },
      complete(res) {
        console.log("loc complete ", res)
      }
    })

  },
  startLocationUpdate() {
    console.log("startLocationUpdate")
    //开启定位更新， 多次调用会导致重复回调onLocationChange
    wx.startLocationUpdate({
      success: (res) => {
        console.log(res)
        //注册定位变化回调
        wx.onLocationChange(_locationChangeFn)
      },
      fail: (err) => {
        console.log(res)
      }
    })
  },
  stopLocationUpdate() {
    console.log("stopLocationUpdate")
    wx.stopLocationUpdate({
      success: (res) => {
        console.log(res)
      },
      fail: (err) => {
        console.log(res)
      }
    })
    //或者移除回调
    //wx.offLocationChange(_locationChangeFn)
  },
  getFuzzyLocation() {
    console.log("getFuzzyLocation")
    wx.getFuzzyLocation({
      type: 'wgs84',
      success(res) {
        const latitude = res.latitude
        const longitude = res.longitude
        console.log(res)
      }
    });
  },
  chooseLocation() {
    console.log("chooseLocation")
    wx.chooseLocation({
      success(res) {
        console.log(res)
      },
      fail(res) {
        console.log(res)
      },
      complete(res) {
        console.log(res)
      }
    })
  },
  choosePoi() {
    console.log("choosePoi")
    wx.choosePoi({
      success(res) {
        console.log(res)
      }
    })
  },
  chooseAddress() {
    console.log("chooseAddress")
    wx.chooseAddress({
      //
      success(res) {
        console.log(res)
        console.log(res.userName)
        console.log(res.postalCode)
        console.log(res.provinceName)
        console.log(res.cityName)
        console.log(res.countyName)
        console.log(res.detailInfo)
        console.log(res.nationalCode)
        console.log(res.telNumber)
      }
    })
  },
  // 调起客户端小程序设置界面
  toSetting() {
    console.log("toSetting")
    wx.openSetting({
      success(res) {
        console.log(res)
        if (res.authSetting["scope.userLocation"]) {
          //已授权
        }
      }
    })
  },
  getUserInfo() {
    // 通过 wx.getSetting 查询用户是否已授权头像昵称信息
    wx.getSetting({
      success(res) {
        if (res.authSetting['scope.userInfo']) {
          // 已经授权，可以直接调用 getUserInfo 获取头像昵称
          wx.getUserInfo({
            success: function (res) {
              console.log(res.userInfo)
              console.log(res.userInfo.avatarUrl)
            }
          })
        } else {
          // 否则，先通过 wx.createUserInfoButton 接口发起授权
          let button = wx.createUserInfoButton({
            type: 'text',
            text: '获取用户信息',
            style: {
              left: 10,
              top: 76,
              width: 200,
              height: 40,
              lineHeight: 40,
              backgroundColor: '#ff0000',
              color: '#ffffff',
              textAlign: 'center',
              fontSize: 16,
              borderRadius: 4
            }
          })
          button.onTap((res) => {
            // 用户同意授权后回调，通过回调可获取用户头像昵称信息
            console.log("btn onTap", res)
          })
        }
      }
    })
    return;
    // 必须是在用户已经授权的情况下调用
    wx.getUserInfo({
      success: function (res) {
        // var userInfo = res.userInfo
        // var nickName = userInfo.nickName
        // var avatarUrl = userInfo.avatarUrl
        // var gender = userInfo.gender //性别 0：未知、1：男、2：女
        // var province = userInfo.province
        // var city = userInfo.city
        // var country = userInfo.country
        console.log(res)
      }
    })
  }
})