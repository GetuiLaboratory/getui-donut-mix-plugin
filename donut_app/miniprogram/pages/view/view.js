Page({
  onShareTimeline() {
    return {
      title: 'view',
      query: "name=onShareTimeline",
      imageUrl: "https://www.baidu.com/img/flexible/logo/pc/result@2.png"
    }
  },
  onShareAppMessage() {
    console.log("aaaa")
    return {
      title: 'view',
      path: 'pages/view/view?name=onShareAppMessage'
    }
  },
  data: {
    theme: 'light',
    queryData: ''
  },
  onShow() {

  },
  onReady() {
    console.log("onready" + getCurrentPages())
    if (0) {

      var pages = getCurrentPages()
      console.log(getCurrentPages())
      var obj = pages[pages.length - 1]
      var path = obj.route
      console.log(obj)
      console.log(path)
      console.log(__wxConfig)
      console.log(__wxConfig)
    }
    //var title = this.getPageTitle(path)
    // console.log("title=", __wxConfig.page["packageComponent/pages/view/view/view.html"].window.navigationBarTitleText)

    // var pages = getCurrentPages()
    //  var latestPage = pages[pages.length - 1]
    //  var route = latestPage.route 
    //  var title = __wxConfig.page[route].window.navigationBarTitleText

    console.log(getCurrentPages())
    //页面传值
    // var name = getCurrentPages()[1].options["name"]
    // console.log("name=", name)


    //屏蔽分享
    //wx.hideShareMenu()
  },
  testShare: function () {
    let path = 'page/component/index?postId=' + "akak123"
    wx.navigateTo({
      url: path,
    })
  },
  onLoad(query) {
    console.log("sys view.js onLoad", query)
    // let name = query.name
    let str = JSON.stringify(query)
    this.setData({
      queryData: str
    })
    
    // this.setData({
    //   theme: wx.getSystemInfoSync().theme || 'light'
    // })
    wx.setNavigationBarTitle({
      title: 'WX API设置的标题',
      success: (res) => {},
      fail: (res) => {},
      complete: (res) => {},
    })

    // if (wx.onThemeChange) {
    //   wx.onThemeChange(({
    //     theme
    //   }) => {
    //     this.setData({
    //       theme
    //     })
    //   })
    // }
  }
})