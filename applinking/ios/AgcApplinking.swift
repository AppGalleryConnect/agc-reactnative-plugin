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
import AGConnectAppLinking

@objc(AgcAppLinking)

/// Provides methods to initialize AppLinking Kit and implement applinking functions.
class AgcAppLinking: RCTEventEmitter, AGCAppLinkingHandling{
  
  /// All the AgcAppLinking API's can be reached via AgcAppLinkingViewModel class instance.
  private lazy var viewModel: AgcApplinkingViewModel = AgcApplinkingViewModel()
  var promise:RCTPromiseResolveBlock?
  var data: NSDictionary?
  
  override init() {
    super.init()
    NotificationCenter.default.addObserver(self, selector: #selector(self.showResolvedLinkData(_:)), name: NSNotification.Name(rawValue: "AgcApplinkingNotification"), object: nil)
  }
  
  
  /// Fetches ResolvedLinkData instance with deepLink, clickTimeStamp,
  /// socialTitle, socialDescription, socialImageUrl,
  /// campaignName, campaignMedium,
  /// campaignSource
  /// and minimumAppVersion params.
  /// - Parameters:
  ///   - resolve: In the success scenario, returns RCTPromiseResolveBlock instance, with ResolvedLinkData params.
  ///   - reject: RCTPromiseRejectBlock instance with AGCException is returned in the failure scenario.
  /// - Returns: Void
  @objc func getAGConnectAppLinkingResolvedLinkData(_ resolve: @escaping RCTPromiseResolveBlock,rejecter reject: @escaping RCTPromiseRejectBlock) -> Void {
    AGCAppLinkingLog.showInPanel(message: #function, type: .call)
    self.promise = resolve
    if let data = self.data {
      AGCAppLinkingLog.showInPanel(message: #function, type: .success)
      self.handle(resolve: resolve, data)
      self.promise = nil
      return
    }
  }
  
  
  /// Asynchronously generates a short link with a string-type suffix. You can specify the suffix as a long or short one.
  /// - Parameters:
  ///   - params: Refers to paramaters that will get domainUriPrefix, deepLink and optionally socialCardInfo, campaignInfo, androidLinkInfo, expireMinute, linkingPreviewType.
  ///   - resolve: In the success scenario, returns RCTPromiseResolveBlock instance, with shortLink and testUrl params.
  ///   - reject: RCTPromiseRejectBlock instance with AGCException is returned in the failure scenario.
  /// - Returns: Void
  @objc func buildShortAppLinking(_ params: NSDictionary, resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) -> Void {
    AGCAppLinkingLog.showInPanel(message: #function, type: .call)
    
    viewModel.buildShortAppLinking(params) { [weak self] (result, error) in
      guard let strongSelf = self else {return}
      if let error = error {
        strongSelf.fail(with: error, reject: reject)
        AGCAppLinkingLog.showInPanel(message: #function, type: .fail)
        return
      }
      if let result = result{
        let dictParams: NSMutableDictionary? = [
          "shortLink" : result
        ]
        AGCAppLinkingLog.showInPanel(message: #function, type: .success)
        print(dictParams)
        strongSelf.handle(resolve: resolve, dictParams)
      }
    }
  }
  
  
  /// Generates a long link Uri.
  /// - Parameters:
  ///   - params: Refers to paramaters that will get domainUriPrefix, deepLink and optionally socialCardInfo, campaignInfo, androidLinkInfo, expireMinute, linkingPreviewType.
  ///   - resolve: In the success scenario, returns RCTPromiseResolveBlock instance, with shortLink and testUrl params.
  ///   - reject: RCTPromiseRejectBlock instance with AGCException is returned in the failure scenario.
  /// - Returns: Void
  @objc func buildLongAppLinking(_ params: NSDictionary, resolve: @escaping RCTPromiseResolveBlock, rejecter reject: RCTPromiseRejectBlock) -> Void {
    AGCAppLinkingLog.debug(#function) {
      handle(resolve: resolve, viewModel.buildLongAppLinking(params))
    }
  }
  
  /// Fetches ResolvedLinkData instance with deepLink, clickTimeStamp,
  /// socialTitle, socialDescription, socialImageUrl,
  /// campaignName, campaignMedium,
  /// campaignSource
  /// and minimumAppVersion params.
  /// - Parameters:
  ///   - notification: In the success scenario, returns RCTPromiseResolveBlock instance, with ResolvedLinkData params.with reject: RCTPromiseRejectBlock instance with AGCException is returned in the failure scenario.
  @objc func showResolvedLinkData(_ notification: NSNotification) {
    print(notification.userInfo ?? "")
    if let dict = notification.userInfo as NSDictionary? {
      print("in showResolvedLinkData")
      print(dict)
      //EVENT EMITTER
      if let agcInstance = dict["deepLink"] as? AGCResolvedLink{
        let dictParams: NSMutableDictionary? = [
          "deepLink" : agcInstance.deepLink,
          "clickTimeStamp" : agcInstance.clickTime,
          "socialTitle" : agcInstance.socialTitle ?? "",
          "socialDescription" : agcInstance.socialDescription ?? "",
          "socialImageUrl" : agcInstance.socialImageUrl ?? "",
          "campaignName" : agcInstance.campaignName ?? "",
          "campaignMedium" : agcInstance.campaignMedium ?? "",
          "campaignSource" : agcInstance.campaignSource ?? ""
        ]
        print(dictParams)
        self.data = dictParams
      }else{
        self.data = dict
      }
      
      if self.promise != nil{
        self.handle(resolve: self.promise!, self.data)
        self.promise = nil
      }
    }
  }
  
  
  deinit {
    NotificationCenter.default.removeObserver(self)
  }
  
  /// **supportedEvents** must be implemented to return an array of event names that we can listen to.
  /// - Returns: Array of Strings.
  override func supportedEvents() -> [String]! {
    return ["onResolvedLinkData"]
  }
  
  /// **requiresMainQueueSetup** must be implemented to use constantsToExport or have implemented an init() method for UIKit components in React Native v0.49+.
  /// - Returns: a Boolean: **true** if the class needed to be initialized on the main thread, **false** if the class can be initialized on a background thread.
  @objc override static func requiresMainQueueSetup() -> Bool {
    return false
  }
}
