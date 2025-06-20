//
//  NotificationService.m
//  NotificationService
//
//  Created by ak on 16/10/10.
//  Copyright © 2016年 Gexin Interactive (Beijing) Network Technology Co.,LTD. All rights reserved.
//

#import "NotificationService.h"
#import <GTExtensionSDK/GeTuiExtSdk.h>
//#import "ApnsHelper.h"

@interface NotificationService ()
@property (nonatomic, strong) void (^contentHandler)(UNNotificationContent *contentToDeliver);
@property (nonatomic, strong) UNMutableNotificationContent *bestAttemptContent;
@end

@implementation NotificationService

- (void)didReceiveNotificationRequest:(UNNotificationRequest *)request withContentHandler:(void (^)(UNNotificationContent *_Nonnull))contentHandler {
    self.contentHandler = contentHandler;
    self.bestAttemptContent = [request.content mutableCopy];
    
    //[ 测试代码 ] TODO：语音播报
//    float cnt = 123;  //读取apns中播报信息
//    NSString *name = [ApnsHelper makeMp3FromExt:cnt];
//    UNNotificationSound *sound = [UNNotificationSound soundNamed:name];
//    self.bestAttemptContent.sound = sound;
    NSLog(@"GTSDK [Ext] WillIn >>> %@", self.bestAttemptContent.title);
    // [ 测试代码 ] TODO:用户可以在这里处理通知样式的修改，eg:修改标题，开发阶段可以用于判断是否运行通知扩展
    //self.bestAttemptContent.title = [NSString stringWithFormat:@"%@ [WillIn]", self.bestAttemptContent.title];
    
    // [ GTSDK ]：设置Group标识, APP中也要设置[GeTuiSdk setApplicationGroupIdentifier:];
    // 用于回执统计
    //[GeTuiExtSdk setApplicationGroupIdentifier:@"group.ent.com.getui.demo"];

    // [ GTSDK ] 统计APNs到达情况和多媒体推送支持接口, 建议使用该接口
    [GeTuiExtSdk handelNotificationServiceRequest:request withAttachmentsComplete:^(NSArray *attachments, NSArray *errors) {
        // [ 测试代码 ] TODO：日志打印，如果APNs处理有错误，可以在这里查看相关错误详情
        //NSLog(@"处理个推APNs展示遇到错误：%@", errors);
        NSLog(@"GTSDK [Ext] Success >>> %@", self.bestAttemptContent.title);
        // [ 测试代码 ] TODO：用户可以在这里处理通知样式的修改，eg:修改标题，开发阶段可以用于判断是否运行通知扩展
        //self.bestAttemptContent.title = [NSString stringWithFormat:@"%@ [Success]", self.bestAttemptContent.title];
        
        self.bestAttemptContent.attachments = attachments;      // 设置通知中的多媒体附件
        self.contentHandler(self.bestAttemptContent);           // 展示推送的回调处理需要放到个推回执完成的回调中
    }];
}

- (void)serviceExtensionTimeWillExpire {
    // [ GTSDK ] 销毁SDK，释放资源
//    [GeTuiExtSdk destory];
    
    // [ 测试代码 ] TODO：测试时查看超时情况，eg:修改标题，开发阶段可以用于判断是否运行通知扩展
    //self.bestAttemptContent.title = [NSString stringWithFormat:@"%@ [Timeout]", self.bestAttemptContent.title];
    
    // Called just before the extension will be terminated by the system.
    // Use this as an opportunity to deliver your "best attempt" at modified content, otherwise the original push payload will be used.
    self.contentHandler(self.bestAttemptContent);
}

//- (NSString *)pluginId {
//    return @"wxed34e654870cba42";
//}
//- (nullable NSString *) getAppKey {
//    NSBundle *frameworkBundle = [NSBundle bundleForClass:[NotificationService class]];
//    NSURL *url = [frameworkBundle URLForResource:@"MiniPlugin" withExtension:@"bundle"];
//    if (url) {
//        NSBundle *resourceBundle = [NSBundle bundleWithURL:url];
//        NSString *jsonFilePath = [resourceBundle pathForResource: [self pluginId] ofType:@"json"];
//        if (jsonFilePath) {
//            NSData *jsonData = [NSData dataWithContentsOfFile:jsonFilePath];
//            if (jsonData) {
//                // 读取文件内容
//                NSError *error;
//                // 将JSON数据转换为NSDictionary
//                NSDictionary *jsonDict = [NSJSONSerialization JSONObjectWithData:jsonData options:kNilOptions error:&error];
//                
//                // 检查转换是否成功
//                if (!jsonDict) {
//                    NSLog(@"Error parsing JSON: %@", error);
//                    return nil;
//                }
//                
//                NSString *appKey = jsonDict[@"appKey"];
//                
//                if (appKey) {
//                    return appKey;
//                }
//            }
//        }
//    }
//    
//    return nil;
//}

@end
