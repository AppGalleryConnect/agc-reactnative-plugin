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
import AGConnectFunction

@objc(AGCCloudFunctions)

/// Provides methods to initialize Cloud Functions Kit and implement analysis functions.
class AGCCloudFunctions: NSObject, AGCCloudFunctionsHanding {
  
  /// All the AGCCloudFunctions API's can be reached via AGCCloudFunctionsViewModel class instance.
  private lazy var viewModel: AGCCloudFunctionsViewModel = AGCCloudFunctionsViewModel()
  
  /// Creates a function callable instance, sets its configuration and calls the cloud functions
  /// - Parameters:
  ///   - triggerIdentifier: HTTP trigger identifier of the cloud function to be called.
  ///   - options: Dictionary that contains AGCFunctionCallable instance configurations and cloud functions parameters
  ///   - resolve: In the success scenario, returns RCTPromiseResolveBlock instance, with request's response.
  ///   - reject: RCTPromiseRejectBlock instance with AGCFunctionError is returned in the failure scenario.
  @objc func call(
    _ triggerIdentifier: String,
    options: Dictionary<String, Any>,
    resolver resolve: @escaping RCTPromiseResolveBlock,
    rejecter reject: @escaping RCTPromiseRejectBlock
  ) {
    AGCCloudFunctionsLog.showInPanel(message: #function, type: .call)
    let functionCallable: AGCFunctionCallable = AGCFunction.getInstance().wrap(triggerIdentifier);
    if let timeout = options["timeout"] as? Double {
      functionCallable.timeoutInterval = timeout
    }
    
    if let params = options["params"] as? Dictionary<String, Any> {
      callFunction(functionCallable: functionCallable, param: params, resolver: resolve, rejecter: reject)
    }else {
      callFunction(functionCallable: functionCallable, resolve: resolve, reject: reject)
    }
  }
  
  // MARK: - Private Helper Functions
  
  /// Calls a function without input parameters.
  /// - Parameters:
  ///   - functionCallable: AGCFunctionCallable instance that is used to call cloud function
  ///   - resolve: In the success scenario, returns RCTPromiseResolveBlock instance, with request's response.
  ///   - reject: RCTPromiseRejectBlock instance with AGCFunctionError is returned in the failure scenario.
  /// - Returns: Void
  private func callFunction(
    functionCallable: AGCFunctionCallable,
    resolve: @escaping RCTPromiseResolveBlock,
    reject: @escaping RCTPromiseRejectBlock
  ){
    viewModel.callFunction(functionCallable: functionCallable){ [weak self] (result, error) in
      guard let strongSelf = self else {return}
      if let error = error as NSError? {
        AGCCloudFunctionsLog.showInPanel(message: #function, type: .fail)
        strongSelf.fail(with: error, reject: reject)
      }
      if let result = result as? String {
        if let data = result.data(using: .utf8){
          do {
            guard let json = try JSONSerialization.jsonObject(with: data, options: []) as? [String : Any] else { return }
            AGCCloudFunctionsLog.showInPanel(message: #function, type: .success)
            strongSelf.handle(resolve: resolve, json)
          } catch {
            AGCCloudFunctionsLog.showInPanel(message: #function, type: .fail)
            strongSelf.fail(with: error, reject: reject)
          }
        }
      }
    }
  }
  
  /// Calls a function with input parameters.
  /// - Parameters:
  ///   - functionCallable: AGCFunctionCallable instance that is used to call cloud function
  ///   - param: Dictionary that contains the input parameter values of a function.
  ///   - resolve: In the success scenario, returns RCTPromiseResolveBlock instance, with request's response.
  ///   - reject: RCTPromiseRejectBlock instance with AGCFunctionError is returned in the failure scenario.
  /// - Returns: Void
  private func callFunction(
    functionCallable: AGCFunctionCallable,
    param: Dictionary<String, Any>,
    resolver resolve: @escaping RCTPromiseResolveBlock,
    rejecter reject: @escaping RCTPromiseRejectBlock
  ){
    viewModel.callFunction(functionCallable: functionCallable, param: param){ [weak self] (result, error) in
      guard let strongSelf = self else {return}
      if let error = error as NSError? {
        AGCCloudFunctionsLog.showInPanel(message: #function, type: .fail)
        strongSelf.fail(with: error, reject: reject)
      }
      if let result = result as? String {
        if let data = result.data(using: .utf8){
          do {
            guard let json = try JSONSerialization.jsonObject(with: data, options: []) as? [String : Any] else { return }
            AGCCloudFunctionsLog.showInPanel(message: #function, type: .success)
            strongSelf.handle(resolve: resolve, json)
          } catch {
            AGCCloudFunctionsLog.showInPanel(message: #function, type: .fail)
            strongSelf.fail(with: error, reject: reject)
          }
        }
      }
    }
  }
  
  /// *requiresMainQueueSetup* must be implemented to use constantsToExport or have implemented an init() method for UIKit components in React Native v0.49+.
  /// - Returns: a Boolean: **true** if the class needed to be initialized on the main thread, **false** if the class can be initialized on a background thread.
  @objc static func requiresMainQueueSetup() -> Bool {
    return true
  }
  
}
