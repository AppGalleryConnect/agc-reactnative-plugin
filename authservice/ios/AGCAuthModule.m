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

@interface RCT_EXTERN_MODULE(AGCAuthModule, RCTEventEmitter)

RCT_EXTERN_METHOD(getUser:
                  (RCTPromiseResolveBlock)resolve
                  rejecter: (RCTPromiseRejectBlock)reject
                  )

RCT_EXTERN_METHOD(signOut:
                  (RCTPromiseResolveBlock)resolve
                  rejecter: (RCTPromiseRejectBlock)reject
                  )

RCT_EXTERN_METHOD(deleteUser:
                  (RCTPromiseResolveBlock)resolve
                  rejecter: (RCTPromiseRejectBlock)reject
                  )

RCT_EXTERN_METHOD(addTokenListener: (NSString *)id)

RCT_EXTERN_METHOD(removeTokenListener: (NSString *)id)

RCT_EXTERN_METHOD(requestPhoneVerifyCode:
                  (NSString *)countryCode
                  withPhoneNumber: (NSString *)phoneNumber
                  verifyCodeSettings: (NSDictionary *)map
                  resolver: (RCTPromiseResolveBlock)resolve
                  rejecter: (RCTPromiseRejectBlock)reject
                  )

RCT_EXTERN_METHOD(createPhoneUser:
                  (NSString *)countryCode
                  withPhoneNumber: (NSString *)phoneNumber
                  withVerifyCode: (NSString *)verificationCode
                  withPassword: (NSString *)password
                  resolver: (RCTPromiseResolveBlock)resolve
                  rejecter: (RCTPromiseRejectBlock)reject
                  )

RCT_EXTERN_METHOD(resetPhonePassword:
                  (NSString *)countryCode
                  withPhoneNumber: (NSString *)phoneNumber
                  withPassword: (NSString *)newPassword
                  withVerifyCode: (NSString *)verificationCode
                  resolver: (RCTPromiseResolveBlock)resolve
                  rejecter: (RCTPromiseRejectBlock)reject
                  )

RCT_EXTERN_METHOD(requestEmailVerifyCode:
                  (NSString *)email
                  verifyCodeSettings: (NSDictionary *)map
                  resolver: (RCTPromiseResolveBlock)resolve
                  rejecter: (RCTPromiseRejectBlock)reject
                  )

RCT_EXTERN_METHOD(createEmailUser:
                  (NSString *)email
                  withVerifyCode: (NSString *)verificationCode
                  withPassword: (NSString *)password
                  resolver: (RCTPromiseResolveBlock)resolve
                  rejecter: (RCTPromiseRejectBlock)reject
                  )

RCT_EXTERN_METHOD(resetEmailPassword:
                  (NSString *)email
                  withPassword: (NSString *)newPassword
                  withVerifyCode: (NSString *)verificationCode
                  resolver: (RCTPromiseResolveBlock)resolve
                  rejecter: (RCTPromiseRejectBlock)reject
                  )

RCT_EXTERN_METHOD(signInAnonymously:
                  (RCTPromiseResolveBlock)resolve
                  rejecter: (RCTPromiseRejectBlock)reject
                  )

RCT_EXTERN_METHOD(signIn:
                  (NSDictionary *)credential
                  resolver: (RCTPromiseResolveBlock)resolve
                  rejecter: (RCTPromiseRejectBlock)reject
                  )

@end
