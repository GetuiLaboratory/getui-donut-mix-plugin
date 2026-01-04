//#import <Foundation/Foundation.h>
//#import <UIKit/UIKit.h>
//#import "WeAppNativePlugin.framework/WeAppNativePlugin.h"
//#import "GetuiIdoSdkPlugin.h"
//#import <GTCountSDK/GTCountSDK.h>
//
//__attribute__((constructor))
//static void initPlugin() {
//    [GetuiIdoSdkPlugin registerPluginAndInit:[[GetuiIdoSdkPlugin alloc] init]];
//};
//
//@interface GetuiIdoSdkPlugin()<GTCountSDKDelegate> {
//}
//@end
//
//@implementation GetuiIdoSdkPlugin
//
//// 声明插件ID
//WEAPP_DEFINE_PLUGIN_ID(wxed34e654870cba42)
//
//WEAPP_EXPORT_PLUGIN_METHOD_SYNC(ido_startSdk, @selector(startSdk:))
//WEAPP_EXPORT_PLUGIN_METHOD_SYNC(ido_setDebugEnable, @selector(setDebugEnable:))
//WEAPP_EXPORT_PLUGIN_METHOD_SYNC(ido_onBeginEvent, @selector(onBeginEvent:))
//WEAPP_EXPORT_PLUGIN_METHOD_SYNC(ido_onEndEvent, @selector(onEndEvent:))
//WEAPP_EXPORT_PLUGIN_METHOD_SYNC(ido_trackCountEvent, @selector(trackCountEvent:))
//WEAPP_EXPORT_PLUGIN_METHOD_SYNC(ido_setProfile, @selector(setProfile:))
//WEAPP_EXPORT_PLUGIN_METHOD_SYNC(ido_getGtcId, @selector(getGtcId:))
//WEAPP_EXPORT_PLUGIN_METHOD_SYNC(ido_setUserId, @selector(setUserId:))
//WEAPP_EXPORT_PLUGIN_METHOD_SYNC(ido_setApplicationGroupIdentifier, @selector(setApplicationGroupIdentifier:))
//WEAPP_EXPORT_PLUGIN_METHOD_SYNC(ido_setEventUploadInterval, @selector(setEventUploadInterval:))
//WEAPP_EXPORT_PLUGIN_METHOD_SYNC(ido_setEventForceUploadSize, @selector(setEventForceUploadSize:))
//WEAPP_EXPORT_PLUGIN_METHOD_SYNC(ido_setProfileUploadInterval, @selector(setProfileUploadInterval:))
//WEAPP_EXPORT_PLUGIN_METHOD_SYNC(ido_setProfileForceUploadSize, @selector(setProfileForceUploadSize:))
//WEAPP_EXPORT_PLUGIN_METHOD_SYNC(ido_getVersion, @selector(getVersion))
//
//// 声明插件同步方法
////WEAPP_EXPORT_PLUGIN_METHOD_SYNC(mySyncFunc, @selector(mySyncFunc:))
//
//// 声明插件异步方法
////WEAPP_EXPORT_PLUGIN_METHOD_ASYNC(myAsyncFuncwithCallback, @selector(myAsyncFunc:withCallback:))
//
//
//// 插件初始化方法，在注册插件后会被自动调用
//- (void)initPlugin {
//    NSLog(@"IDOSDK>>>initPlugin");
//}
//
//- (void)sendMsg:(NSString*)method params:(id)params {
//    [self sendMiniPluginEvent:@{ @"method": method, @"param":params}];
//}
//
//- (id)startSdk:(NSDictionary *)param {
//    NSLog(@"IDOSDK>>>startSdk appId:%@ channelId:%@",param[@"appId"],param[@"channelId"]);
//    [GTCountSDK startSDKWithAppId:param[@"appId"] withChannelId:param[@"channelId"] delegate:self];
//    return @(1);
//}
//
//- (id)setDebugEnable:(NSDictionary *)param {
//    BOOL debugEnable = param[@"debugEnable"];
//    NSLog(@"\n>>>IDOSDK setDebugEnable:%@", @(debugEnable));
//    [GTCountSDK setDebugEnable:debugEnable];
//    return @(1);
//}
//
//- (id)onBeginEvent:(NSDictionary *)param {
//    NSString *eventId = param[@"eventId"];
//    NSLog(@"\n>>>IDOSDK onBeginEvent,eventId : %@", eventId);
//    [GTCountSDK trackCustomKeyValueEventBegin:eventId];
//    return @(1);
//}
//
//- (id)onEndEvent:(NSDictionary *)param {
//    NSString *eventId = param[@"eventId"];
//    NSMutableDictionary *dictionary = param[@"jsonObject"];
//    NSString *ext = param[@"withExt"];
//    
//    NSLog(@"\n>>>IDOSDK eventEndWithArg, eventId:%@, args:%@", eventId, dictionary);
//    if (dictionary && [dictionary isKindOfClass:[NSDictionary class]] &&
//        dictionary.count > 0) {
//        [GTCountSDK trackCustomKeyValueEventEnd:eventId withArgs:dictionary withExt:ext];
//    } else {
//        [GTCountSDK trackCustomKeyValueEventEnd:eventId withArgs:nil withExt:ext];
//    }
//    return @(1);
//}
//
//- (id)trackCountEvent:(NSDictionary *)param {
//    NSString *eventId = param[@"eventId"];
//    NSMutableDictionary *dictionary = param[@"jsonObject"];
//    NSString *ext = param[@"withExt"];
//    
//    NSLog(@"\n>>>IDOSDK trackCountEvent,eventId : %@, args : %@", eventId, dictionary);
//    if (dictionary && [dictionary isKindOfClass:[NSDictionary class]] &&
//        dictionary.count > 0) {
//        [GTCountSDK trackCountEvent:eventId withArgs:dictionary withExt:ext];
//    } else {
//        [GTCountSDK trackCountEvent:eventId withArgs:nil withExt:ext];
//    }
//    return @(1);
//}
//
//- (id)setProfile:(NSDictionary *)param {
//    NSMutableDictionary *dictionary = param[@"jsonObject"];
//    NSString *ext = param[@"withExt"];
//    NSLog(@"\n>>>IDOSDK clickProfileSet, property:%@", dictionary);
//    [GTCountSDK setProfile:dictionary withExt:ext];
//    return @(1);
//}
//
//- (id)getGtcId:(NSDictionary *)param {
//    NSString *gtcid = [GTCountSDK gtcid];
//    NSLog(@"\n>>>IDOSDK getGtcId:%@", gtcid);
//    return gtcid?:@"";
//}
//
//- (id)setUserId:(NSDictionary *)param {
//    NSString *userid = param[@"userid"];
//    [[GTCountSDK sharedInstance] setUserId:userid];
//    NSLog(@"\n>>>IDOSDK setUserId:%@", userid);
//    return @(1);
//}
//
//- (id)setApplicationGroupIdentifier:(NSDictionary *)param {
//    NSString *identifier = param[@"identifier"];
//    NSLog(@"\n>>>IDOSDK setApplicationGroupIdentifier, identifier : %@", identifier);
//    [GTCountSDK setApplicationGroupIdentifier:identifier];
//    return @(1);
//}
//
//- (id)setEventUploadInterval:(NSDictionary *)param {
//    NSNumber *timeMillisNumber = param[@"timeMillis"];
//    if (timeMillisNumber != nil && [timeMillisNumber isKindOfClass:[NSNumber class]]) {
//        NSInteger timeMillis = [timeMillisNumber integerValue];
//        NSLog(@"\n>>>IDOSDK setEventUploadInterval, timeMillis : %ld", (long) timeMillis);
//        [GTCountSDK sharedInstance].profileUploadInterval = timeMillis;
//    }
//    return @(1);
//}
//
//- (id)setEventForceUploadSize:(NSDictionary *)param {
//    NSNumber *size = param[@"size"];
//    if (size != nil && [size isKindOfClass:[NSNumber class]]) {
//        NSInteger _size = [size integerValue];
//        NSLog(@"\n>>>IDOSDK setEventUploadInterval, size : %ld", _size);
//        [GTCountSDK sharedInstance].eventForceUploadSize = _size;
//    }
//    return @(1);
//}
//
//- (id)setProfileUploadInterval:(NSDictionary *)param {
//    NSNumber *timeMillisNumber = param[@"timeMillis"];
//    if (timeMillisNumber != nil && [timeMillisNumber isKindOfClass:[NSNumber class]]) {
//        NSInteger timeMillis = [timeMillisNumber integerValue];
//        NSLog(@"\n>>>IDOSDK setProfileUploadInterval, timeMillis : %ld", (long) timeMillis);
//        [GTCountSDK sharedInstance].profileUploadInterval = timeMillis;
//    }
//    return @(1);
//}
//
//- (id)setProfileForceUploadSize:(NSDictionary *)param {
//    NSNumber *size = param[@"size"];
//    if (size != nil && [size isKindOfClass:[NSNumber class]]) {
//        NSInteger _size = [size integerValue];
//        NSLog(@"\n>>>IDOSDK setProfileForceUploadSize, size : %ld", _size);
//        [GTCountSDK sharedInstance].profileForceUploadSize = _size;
//    }
//    return @(1);
//}
//
//- (id)getVersion {
//    NSLog(@"IDOSDK>>>getVersion");
//    return [GTCountSDK sdkVersion];
//}
//
////MARK: - Delegate
//
//- (void)GTCountSDKDidReceiveGtcid:(NSString *)gtcid error:(NSError*)error {
//    NSLog(@"\n>>>IDOSDK GTCountSDKDidReceiveGtcid, gtcid:%@ error:%@", gtcid, error);
//    NSDictionary *dic = @{@"result":gtcid?:@"", @"error":error ? error.description:@""};
//    [self sendMsg:@"gtcIdCallback" params:dic];
//}
//
//@end
