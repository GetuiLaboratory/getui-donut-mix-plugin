Pod::Spec.new do |spec|
  spec.name         = 'MyPlugin'
  spec.version      = '1.0.0'
  spec.summary      = 'Summary of MyPlugin'
  spec.homepage     = 'https://your-framework-website.com'
  spec.author       = { 'Your Name' => 'your@email.com' }
  spec.source       = { :git => 'https://github.com/your/repo.git', :tag => "#{spec.version}" }

  # Set your deployment target
  spec.ios.deployment_target = '8.0'
  

  spec.dependency 'GTSDK'
  spec.dependency 'GCIDOSDK'

  # 所需的framework，多个用逗号隔开
  spec.ios.frameworks = 'SystemConfiguration', 'CFNetwork','CoreTelephony','CoreLocation','AVFoundation','Security','AdSupport'

  # “弱引用”所需的framework，多个用逗号隔开
  spec.ios.weak_frameworks = 'UserNotifications','AppTrackingTransparency','Network'

  # 所需的library，多个用逗号隔开
  spec.ios.libraries = 'z','sqlite3.0','c++','resolv'

  # 是否使用ARC，如果指定具体文件，则具体的问题使用ARC
  spec.requires_arc = true

  spec.swift_versions = ['5']


end