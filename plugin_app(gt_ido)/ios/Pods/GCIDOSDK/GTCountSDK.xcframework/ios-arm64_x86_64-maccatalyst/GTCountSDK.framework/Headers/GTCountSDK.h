//
//  GTCountSDK.h
//  GTCountSDK
//
//  Created by GeTui on 2017/7/18.
//  Copyright © 2017年 GeTui. All rights reserved.
//
//  Version : GSIDO-IOS-2.0.7.0

#import <Foundation/Foundation.h>


@protocol GTCountSDKDelegate <NSObject>

@optional
- (void)GTCountSDKDidReceiveGtcid:(NSString *)gtcid error:(NSError*)error;
@end


@interface GTCountSDK : NSObject

/**
 设置会话时长（默认 30000 ms,单位毫秒），在应用退到后台后停留时间超过会话时长，视为一次新的应用会话。
 */
@property (nonatomic, assign) NSInteger sessionTime;

/**
 设置有效活跃时长，活跃时长满足[min, max]的才会上报服务器记录
 minAppActiveDuration默认 0 ms, maxAppActiveDuration默认 12*3600*1000 ms, 单位毫秒
 */
@property (nonatomic, assign) NSInteger minAppActiveDuration;
@property (nonatomic, assign) NSInteger maxAppActiveDuration;


// 累计条数到达ForceUploadSize 或 距离上一次汇报时间间隔超过UploadInterval后，将触发上传流程

// trackCustomKeyValueEvent和trackCountEvent的上报间隔， 默认10000毫秒，即10秒。
// 传入的参数如果小于默认值，则按照默认值生效。
@property (nonatomic, assign) NSInteger eventUploadInterval;

// trackCustomKeyValueEvent和trackCountEvent 的上报条数阈值，默认30
@property (nonatomic, assign) NSInteger eventForceUploadSize;

// Profile上报间隔， 默认5000毫秒，即5秒
// 传入的参数如果小于默认值，则按照默认值生效。
@property (nonatomic, assign) NSInteger profileUploadInterval;

// Profile上报条数阈值，默认5
@property (nonatomic, assign) NSInteger profileForceUploadSize;

// 设置用户id
@property (nonatomic, copy) NSString *userId;

// 同步生成gtcid， 默认NO：异步生成，初始化sdk后，直接可获取gtcid， YES：同步生成，首次安装后需要等待服务端响应后才能获取gtcid
// add at 2.0.7.0
@property (nonatomic, assign) BOOL syncGenerateGtcid;

/**
 SDK 实例，用于获取和设置 SDK 相关配置属性。
 示例，设置上报策略：[[GTCountSDK sharedInstance] setSessionTime:30*1000];
 */
+ (instancetype)sharedInstance;

/// gtcid可用于用户画像标签的查询。
+ (NSString*)gtcid;

/**
 *  设置 App Groups Id (如有使用 iOS Extension SDK，请设置该值)
 */
+ (void)setApplicationGroupIdentifier:(NSString*)identifier;

/**
 初始化启动接口 
 
 @param appId 开发者后台生成的 appId
 @param channelId 渠道名称，默认为 'appstore'
 @param delegate delegate
 */
+ (void)startSDKWithAppId:(NSString *)appId withChannelId:(NSString *)channelId delegate:(id<GTCountSDKDelegate>)delegate;

/**
 记录自定义事件的开始
 注意：需要跟 trackCustomKeyValueEventEnd:withArgs: 配对使用
 若多次调用则以最后一次开始的时间为准
 
 @param eventId 自定义事件 Id，需要在个数后台配置才能生效
 */
+ (void)trackCustomKeyValueEventBegin:(NSString *)eventId;

/**
 记录自定义事件的结束
 注意：需要跟 trackCustomKeyValueEventBegin:withArgs: 配对使用
 若多次调用则以第一次结束的时间为准
 
 @param eventId 自定义事件 Id，需要在个数后台配置才能生效
 @param args 自定义属性，key 必须为 NSString 类型（注意：不能以$开头），value 仅支持NSNumber, BOOL, NSString, NSDate 4 种类型。
 @param ext 自定义ext字段内容。
 */
+ (void)trackCustomKeyValueEventEnd:(NSString *)eventId withArgs:(NSDictionary *)args withExt:(NSString *)ext;

/**
 计数事件统计
 
 @param eventId 事件 Id，需要在个数后台配置才能生效
 @param args 自定义属性，key 必须为 NSString 类型（注意：不能以$开头），value 仅支持NSNumber, BOOL, NSString, NSDate 4 种类型
 @param ext 自定义ext字段内容。
 */
+ (void)trackCountEvent:(NSString *)eventId withArgs:(NSDictionary *)args withExt:(NSString *)ext;

/**
 用户属性设置
 
 @param profiles 用户属性参数，key 必须为 NSString 类型，value 仅支持NSNumber, BOOL, NSString, NSDate 4 种类型
 @param ext 自定义ext字段内容。
 
 */
+ (void)setProfile:(NSDictionary *)profiles withExt:(NSString *)ext;
+ (void)setProfile:(NSDictionary *)profiles;

/// 对应GTSDK应用内弹窗回调的展示埋点
/// - Parameter dic: 弹窗数据
+ (void)popupDidShow:(NSDictionary *)dic;

/// 对应GTSDK应用内弹窗回调的点击埋点
/// - Parameter dic: 弹窗数据
+ (void)popupDidClick:(NSDictionary *)dic;

/**
 是否启动开发者模式，在该模式下数据会实时上传，并且将会打印相关的数据上传信息
 
 @param isDebug 是否开启
 */
+ (void)setDebugEnable:(BOOL)isDebug;

+ (NSString *)sdkVersion;

@end
