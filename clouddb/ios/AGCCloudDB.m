/*
 * Copyright 2021-2023. Huawei Technologies Co., Ltd. All rights reserved.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License")
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

#import <Foundation/Foundation.h>
#import <React/RCTBridgeModule.h>
#import <React/RCTEventEmitter.h>

@interface RCT_EXTERN_REMAP_MODULE(AGCCloudDBModule, AGCCloudDB, RCTEventEmitter)

RCT_EXTERN_METHOD(initialize)

RCT_EXTERN_METHOD(createObjectType: (RCTPromiseResolveBlock)resolve
                  rejecter: (RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(deleteCloudDBZone: (NSString)zoneName
                  resolver: (RCTPromiseResolveBlock)resolve
                  rejecter: (RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(openCloudDBZone: (NSDictionary)map
                  isAllowToCreate: (BOOL)allowed
                  id: (NSString)dbZoneId
                  resolver: (RCTPromiseResolveBlock)resolve
                  rejecter: (RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(openCloudDBZone2: (NSDictionary)map
                  isAllowToCreate: (BOOL)allowed
                  id: (NSString)dbZoneId
                  resolver: (RCTPromiseResolveBlock)resolve
                  rejecter: (RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(getCloudDBZoneConfigs: (RCTPromiseResolveBlock)resolve
                  rejecter: (RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(enableNetwork: (NSString)zoneName
                  resolver: (RCTPromiseResolveBlock)resolve
                  rejecter: (RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(disableNetwork: (NSString)zoneName
                  resolver: (RCTPromiseResolveBlock)resolve
                  rejecter: (RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(setUserKey: (NSString)userKey
                  reKey: (NSString)userRekey
                  needStrongCheck: (BOOL)needStrongCheck
                  resolver: (RCTPromiseResolveBlock)resolve
                  rejecter: (RCTPromiseRejectBlock)reject)          

RCT_EXTERN_METHOD(updateDataEncryptionKey: (RCTPromiseResolveBlock)resolve
                  rejecter: (RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(addEventListener: (RCTPromiseResolveBlock)resolve
                  rejecter: (RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(addDataEncryptionKeyListener: (RCTPromiseResolveBlock)resolve
                  rejecter: (RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(closeCloudDBZone: (NSString)id
                  resolver: (RCTPromiseResolveBlock)resolve
                  rejecter: (RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(runTransaction: (NSArray)transactionsArray
                  withId: (NSString)id
                  resolver: (RCTPromiseResolveBlock)resolve
                  rejecter: (RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(executeUpsert: (NSString)className
                  objectArray: (NSArray)array
                  withId: (NSString)id
                  resolver: (RCTPromiseResolveBlock)resolve
                  rejecter: (RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(executeDelete: (NSString)className
                  objectArray: (NSArray)array
                  withId: (NSString)id
                  resolver: (RCTPromiseResolveBlock)resolve
                  rejecter: (RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(executeQuery: (NSDictionary)map
                  policy: (NSInteger)queryPolicy
                  withId: (NSString)id
                  resolver: (RCTPromiseResolveBlock)resolve
                  rejecter: (RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(executeAverageQuery: (NSDictionary)map
                  withFieldName: (NSString)fieldName
                  policy: (NSInteger)queryPolicy
                  withId: (NSString)id
                  resolver: (RCTPromiseResolveBlock)resolve
                  rejecter: (RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(executeSumQuery: (NSDictionary)map
                  withFieldName: (NSString)fieldName
                  policy: (NSInteger)queryPolicy
                  withId: (NSString)id
                  resolver: (RCTPromiseResolveBlock)resolve
                  rejecter: (RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(executeMaximumQuery: (NSDictionary)map
                  withFieldName: (NSString)fieldName
                  policy: (NSInteger)queryPolicy
                  withId: (NSString)id
                  resolver: (RCTPromiseResolveBlock)resolve
                  rejecter: (RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(executeMinimalQuery: (NSDictionary)map
                  withFieldName: (NSString)fieldName
                  policy: (NSInteger)queryPolicy
                  withId: (NSString)id
                  resolver: (RCTPromiseResolveBlock)resolve
                  rejecter: (RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(executeCountQuery: (NSDictionary)map
                  withFieldName: (NSString)fieldName
                  policy: (NSInteger)queryPolicy
                  withId: (NSString)id
                  resolver: (RCTPromiseResolveBlock)resolve
                  rejecter: (RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(executeQueryUnsynced: (NSDictionary)map
                  withId: (NSString)id
                  resolver: (RCTPromiseResolveBlock)resolve
                  rejecter: (RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(executeServerStatusQuery: (NSString)id
                  resolver: (RCTPromiseResolveBlock)resolve
                  rejecter: (RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(subscribeSnapshot: (NSDictionary)map
                  policy: (NSInteger)queryPolicy
                  withId: (NSString)id
                  withListenerId: (NSString)listenerId
                  resolver: (RCTPromiseResolveBlock)resolve
                  rejecter: (RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(removeSubscription: (NSString)id
                  withListenerId: (NSString)listenerId
                  resolver: (RCTPromiseResolveBlock)resolve
                  rejecter: (RCTPromiseRejectBlock)reject)

@end
