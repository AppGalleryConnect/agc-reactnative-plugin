/*
 * Copyright (c) 2020-2023. Huawei Technologies Co., Ltd. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

#import "Crash.h"
#import <React/RCTLog.h>
#import <AGConnectCrash/AGConnectCrash.h>

@implementation Crash

RCT_EXPORT_MODULE(RNCrash)

RCT_EXPORT_METHOD(testIt)
{
    dispatch_async(dispatch_get_main_queue(),^{
         [[AGCCrash sharedInstance] testIt];
    });
}

RCT_EXPORT_METHOD(enableCrashCollection:(BOOL)enable
                 resolver:(RCTPromiseResolveBlock)resolve
                 rejecter:(RCTPromiseRejectBlock)reject)
{
    [[AGCCrash sharedInstance] enableCrashCollection:enable];
    resolve([NSNull null]);
}

RCT_EXPORT_METHOD(setUserId:(NSString *)userId
                 resolver:(RCTPromiseResolveBlock)resolve
                 rejecter:(RCTPromiseRejectBlock)reject)
{
    [[AGCCrash sharedInstance] setUserId:userId];
    resolve([NSNull null]);
}

RCT_EXPORT_METHOD(setCustomKey:(NSString *)key
                 value:(id)value
                 resolver:(RCTPromiseResolveBlock)resolve
                 rejecter:(RCTPromiseRejectBlock)reject)
{
    [[AGCCrash sharedInstance] setCustomValue:value forKey:key];
    resolve([NSNull null]);
}

RCT_EXPORT_METHOD(log:(NSString *)message
                 resolver:(RCTPromiseResolveBlock)resolve
                 rejecter:(RCTPromiseRejectBlock)reject)
{
    [[AGCCrash sharedInstance] log:message];
    resolve([NSNull null]);
}

RCT_EXPORT_METHOD(logWithLevel:(int)level 
                 message:(NSString *)message
                 resolver:(RCTPromiseResolveBlock)resolve
                 rejecter:(RCTPromiseRejectBlock)reject)
{
    [[AGCCrash sharedInstance] logWithLevel:level message:message];
    resolve([NSNull null]);
}

RCT_EXPORT_METHOD(recordError:(NSDictionary *)jsErrorDict
                    resolver:(RCTPromiseResolveBlock)resolve
                    rejecter:(RCTPromiseRejectBlock)reject)
{
   [self record:jsErrorDict isFatal:NO];
    resolve([NSNull null]);
}
RCT_EXPORT_METHOD(recordFatalError:(NSDictionary *)jsErrorDict
                    resolver:(RCTPromiseResolveBlock)resolve
                    rejecter:(RCTPromiseRejectBlock)reject)
{
   [self record:jsErrorDict isFatal:YES];
    resolve([NSNull null]);
}

RCT_EXPORT_METHOD(record:(NSDictionary *)jsErrorDict isFatal:(BOOL)isFatal){
    NSString *message = jsErrorDict[@"message"];
    NSDictionary *frames = jsErrorDict[@"frames"];
    NSString *stack = @"";
    for (NSDictionary *frame in frames) {
        stack = [stack stringByAppendingFormat:@"%@(%@)\n",frame[@"function"] , frame[@"file"]];
    }
    BOOL isPromiseReject = [jsErrorDict[@"isPromiseReject"] boolValue];
    NSString *name = @"JavaScriptError";
    if (isPromiseReject) {
        name = @"PromiseRejectError";
    }
    name = [name stringByAppendingFormat:@":%@",message];
    AGCExceptionModel *exception = [[AGCExceptionModel alloc] initWithName:name reason:message stackTrace:stack];
    exception.isFatal =isFatal;
    dispatch_async(dispatch_get_main_queue(),^{
        if(isFatal) {
            [[AGCCrash sharedInstance] recordFatalExceptionModel:exception];
        }else {
            [[AGCCrash sharedInstance] recordExceptionModel:exception];
        }
        
    });
}

@end
