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

#import <Foundation/Foundation.h>
#import <React/RCTBridgeModule.h>
#import <React/RCTEventEmitter.h>

@interface RCT_EXTERN_REMAP_MODULE(StorageTask,StorageTaskImpl, RCTEventEmitter)


RCT_EXTERN_METHOD(addOnCanceledListener: (NSString)className
                  storageTaskId: (NSString) storageTaskId
                  eventKey: (NSString) eventKey
                  resolver: (RCTPromiseResolveBlock)resolve
                  rejecter: (RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(addOnCompleteListener: (NSString)className
                  storageTaskId: (NSString) storageTaskId
                  eventKey: (NSString) eventKey
                  resolver: (RCTPromiseResolveBlock)resolve
                  rejecter: (RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(addOnFailureListener: (NSString)className
                  storageTaskId: (NSString) storageTaskId
                  eventKey: (NSString) eventKey
                  resolver: (RCTPromiseResolveBlock)resolve
                  rejecter: (RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(addOnSuccessListener: (NSString)className
                  storageTaskId: (NSString) storageTaskId
                  eventKey: (NSString) eventKey
                  resolver: (RCTPromiseResolveBlock)resolve
                  rejecter: (RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(addOnPausedListener: (NSString)className
                  storageTaskId: (NSString) storageTaskId
                  eventKey: (NSString) eventKey
                  resolver: (RCTPromiseResolveBlock)resolve
                  rejecter: (RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(addOnProgressListener: (NSString)className
                  storageTaskId: (NSString) storageTaskId
                  eventKey: (NSString) eventKey
                  resolver: (RCTPromiseResolveBlock)resolve
                  rejecter: (RCTPromiseRejectBlock)reject)


RCT_EXTERN_METHOD(cancel: (NSString)className
                  storageTaskId: (NSString)storageTaskId
                  resolver: (RCTPromiseResolveBlock)resolve
                  rejecter: (RCTPromiseRejectBlock)reject)


RCT_EXTERN_METHOD(isCanceled: (NSString)className
                  storageTaskId: (NSString)storageTaskId
                  resolver: (RCTPromiseResolveBlock)resolve
                  rejecter: (RCTPromiseRejectBlock)reject)


RCT_EXTERN_METHOD(isComplete: (NSString)className
                  storageTaskId: (NSString)storageTaskId
                  resolver: (RCTPromiseResolveBlock)resolve
                  rejecter: (RCTPromiseRejectBlock)reject)


RCT_EXTERN_METHOD(isSuccessful: (NSString)className
                  storageTaskId: (NSString)storageTaskId
                  resolver: (RCTPromiseResolveBlock)resolve
                  rejecter: (RCTPromiseRejectBlock)reject)


RCT_EXTERN_METHOD(isInProgress: (NSString)className
                  storageTaskId: (NSString)storageTaskId
                  resolver: (RCTPromiseResolveBlock)resolve
                  rejecter: (RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(isPaused: (NSString)className
                  storageTaskId: (NSString)storageTaskId
                  resolver: (RCTPromiseResolveBlock)resolve
                  rejecter: (RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(pause: (NSString)className
                  storageTaskId: (NSString)storageTaskId
                  resolver: (RCTPromiseResolveBlock)resolve
                  rejecter: (RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(resume: (NSString)className
                  storageTaskId: (NSString)storageTaskId
                  resolver: (RCTPromiseResolveBlock)resolve
                  rejecter: (RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(removeOnCanceledListener: (NSString)className
                  storageTaskId: (NSString) storageTaskId
                  eventKey: (NSString) eventKey
                  resolver: (RCTPromiseResolveBlock)resolve
                  rejecter: (RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(removeOnCompleteListener: (NSString)className
                  storageTaskId: (NSString) storageTaskId
                  eventKey: (NSString) eventKey
                  resolver: (RCTPromiseResolveBlock)resolve
                  rejecter: (RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(removeOnFailureListener: (NSString)className
                  storageTaskId: (NSString) storageTaskId
                  eventKey: (NSString) eventKey
                  resolver: (RCTPromiseResolveBlock)resolve
                  rejecter: (RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(removeOnSuccessListener: (NSString)className
                  storageTaskId: (NSString) storageTaskId
                  eventKey: (NSString) eventKey
                  resolver: (RCTPromiseResolveBlock)resolve
                  rejecter: (RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(removeOnPausedListener: (NSString)className
                  storageTaskId: (NSString) storageTaskId
                  eventKey: (NSString) eventKey
                  resolver: (RCTPromiseResolveBlock)resolve
                  rejecter: (RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(removeOnProgressListener: (NSString)className
                  storageTaskId: (NSString) storageTaskId
                  eventKey: (NSString) eventKey
                  resolver: (RCTPromiseResolveBlock)resolve
                  rejecter: (RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(addSupportedEvent: (NSString)eventKey
                  resolver: (RCTPromiseResolveBlock)resolve
                  rejecter: (RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(removeSupportedEvent: (NSString)eventKey
                  resolver: (RCTPromiseResolveBlock)resolve
                  rejecter: (RCTPromiseRejectBlock)reject)
@end
