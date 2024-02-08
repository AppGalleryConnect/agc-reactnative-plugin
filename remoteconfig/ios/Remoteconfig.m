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

#import "Remoteconfig.h"
#import <React/RCTLog.h>
#import <AGConnectRemoteConfig/AGConnectRemoteConfig.h>

@implementation Remoteconfig

static NSString *const RNAGCErrorDomain = @"RNAGCErrorDomain";

RCT_EXPORT_MODULE(RNRemoteConfig)

RCT_EXPORT_METHOD(applyDefault:(NSDictionary<NSString*, id>*) defaults
                 resolver:(RCTPromiseResolveBlock)resolve
                 rejecter:(RCTPromiseRejectBlock)reject)
{
    [[AGCRemoteConfig sharedInstance] applyDefaults:defaults];
    resolve([NSNull null]);
}

RCT_EXPORT_METHOD(setCustomAttributes:(NSDictionary<NSString*, id>*) defaults
                 resolver:(RCTPromiseResolveBlock)resolve
                 rejecter:(RCTPromiseRejectBlock)reject)
{
    [[AGCRemoteConfig sharedInstance] setCustomAttributes:defaults];
    resolve([NSNull null]);
}

RCT_EXPORT_METHOD(getCustomAttributes:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject)
{
    NSDictionary<NSString*, id> *result = [[AGCRemoteConfig sharedInstance] getCustomAttributes];
    resolve(result);
}

RCT_EXPORT_METHOD(applyLastFetched:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject)
{
    AGCConfigValues* values = [[AGCRemoteConfig sharedInstance] loadLastFetched];
    [[AGCRemoteConfig sharedInstance] apply:values];
    resolve([NSNull null]);
}

RCT_EXPORT_METHOD(fetchDefault:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject)
{
    HMFTask<AGCConfigValues *> *task = [[AGCRemoteConfig sharedInstance] fetch];
    [[task addOnSuccessCallback:^(AGCConfigValues *result) {
        resolve([NSNull null]);
    }] addOnFailureCallback:^(NSError *error) {
        if ([error isKindOfClass:AGCRemoteConfigError.class]) {
           AGCRemoteConfigError *agcError = (AGCRemoteConfigError*)error;
           NSString *code = [NSString stringWithFormat:@"%ld", agcError.code];
           NSNumber *throttleEndTime = [NSNumber numberWithInt:agcError.throttleEndTime*1000];
           NSMutableDictionary *userInfo = [NSMutableDictionary dictionary];
           [userInfo setValue:code forKey:@"code"];
           [userInfo setValue:agcError.localizedDescription forKey:@"message"];
           [userInfo setValue:throttleEndTime forKey:@"throttleEndTime"];
           NSError *newErrorWithUserIffo = [NSError errorWithDomain:RNAGCErrorDomain code:agcError.code userInfo:userInfo];
           reject(code, agcError.localizedDescription, newErrorWithUserIffo);
       } else {
           reject([NSString stringWithFormat:@"%ld", error.code], error.localizedDescription, error);
       }
    }];
}

RCT_EXPORT_METHOD(fetch:(NSInteger)intervalSeconds
                 resolver:(RCTPromiseResolveBlock)resolve
                 rejecter:(RCTPromiseRejectBlock)reject)
{
    HMFTask<AGCConfigValues *> *task = [[AGCRemoteConfig sharedInstance] fetch:intervalSeconds];
    [[task addOnSuccessCallback:^(AGCConfigValues * _Nullable result) {
        resolve([NSNull null]);
    }] addOnFailureCallback:^(NSError * _Nonnull error) {
        if ([error isKindOfClass:AGCRemoteConfigError.class]) {
           AGCRemoteConfigError *agcError = (AGCRemoteConfigError*)error;
           NSString *code = [NSString stringWithFormat:@"%ld", agcError.code];
           NSNumber *throttleEndTime = [NSNumber numberWithInt:agcError.throttleEndTime*1000];
           NSMutableDictionary *userInfo = [NSMutableDictionary dictionary];
           [userInfo setValue:code forKey:@"code"];
           [userInfo setValue:agcError.localizedDescription forKey:@"message"];
           [userInfo setValue:throttleEndTime forKey:@"throttleEndTime"];
           NSError *newErrorWithUserIffo = [NSError errorWithDomain:RNAGCErrorDomain code:agcError.code userInfo:userInfo];
           reject(code, agcError.localizedDescription, newErrorWithUserIffo);
       } else {
           reject([NSString stringWithFormat:@"%ld", error.code], error.localizedDescription, error);
       }
    }];
}

RCT_EXPORT_METHOD(getValue:(NSString*)key
                  resolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject)
{
    resolve([[AGCRemoteConfig sharedInstance] valueAsString:key]);
}

RCT_EXPORT_METHOD(getSource:(NSString*)key
                  resolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject)
{
    AGCRemoteConfigSource source = [[AGCRemoteConfig sharedInstance] getSource:key];
    NSNumber *result = [NSNumber numberWithInteger:source];
    resolve(result);
}

RCT_EXPORT_METHOD(getMergedAll:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject)
{
    NSDictionary<NSString*, id> *result = [[AGCRemoteConfig sharedInstance] getMergedAll];
    resolve(result);
}

RCT_EXPORT_METHOD(clearAll:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject)
{
    [[AGCRemoteConfig sharedInstance] clearAll];
    resolve([NSNull null]);
}

@end
