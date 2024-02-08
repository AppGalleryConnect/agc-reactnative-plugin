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

import Foundation

///Promise AGCStorageHanding blueprint and declerations.
protocol AGCStorageHanding {
  func handle(resolve: @escaping RCTPromiseResolveBlock, _ isSuccess: Bool?, _ message: String?)
  func handle<T>(resolve: RCTPromiseResolveBlock, _ instance: T)
  func fail(with error: Error?, reject: @escaping RCTPromiseRejectBlock)
}

extension AGCStorageHanding {
  func handle(resolve: @escaping RCTPromiseResolveBlock, _ isSuccess: Bool?=true, _ message: String?=nil) {
    AGCStoragePromise.shared.resolve(resolve: resolve, isSuccess, message)
  }
  
  func handle(resolve: RCTPromiseResolveBlock) {
    AGCStoragePromise.shared.resolve(resolve: resolve)
  }
  
  func handle<T>(resolve: RCTPromiseResolveBlock, _ instance: T) {
    AGCStoragePromise.shared.resolve(resolve: resolve, instance: instance)
  }
  
  func fail(with error: Error?, reject: @escaping RCTPromiseRejectBlock){
    AGCStoragePromise.shared.reject(reject: reject, error: error)
  }
  
  func fail(with error: NSError, reject: @escaping RCTPromiseRejectBlock){
    AGCStoragePromise.shared.reject(reject: reject, error: error)
  }
  
  func fail(with message: String, reject: @escaping RCTPromiseRejectBlock) {
    AGCStoragePromise.shared.reject(reject: reject, errorMessage: message)
  }
}

class AGCStoragePromise {
  static let shared = AGCStoragePromise()
  
  private init() { }
  
  func resolve<T>(resolve: RCTPromiseResolveBlock, instance: T?) {
    resolve(instance)
  }
  
  func resolve(resolve: RCTPromiseResolveBlock) {
    resolve(NSNull())
  }
  
  func resolve(resolve: @escaping RCTPromiseResolveBlock, _ isSuccess: Bool?, _ message: String?) {
    resolve(NSNull())
  }
  
  func reject(reject: RCTPromiseRejectBlock, error: Error?) {
    reject("-1", error.debugDescription, error)
  }
  
  func reject(reject: RCTPromiseRejectBlock, error: NSError) {
    reject(String(error.code), error.description, error)
  }
  
  func reject(reject: RCTPromiseRejectBlock, errorMessage: String) {
    let error = NSError(domain: errorMessage, code: -1, userInfo: nil)
    reject(String(error.code), errorMessage.description, error)
  }
  

  
}


