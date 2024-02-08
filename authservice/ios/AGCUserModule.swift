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
import AGConnectAuth

@objc(AGCUserModule)

/// AGCUserModule class is the tool class of AGCUser.
class AGCUserModule: NSObject, AGCAuthServiceHanding {
  
  /// All the AGCUser API's can be reached via AGCUserViewModel class instance
  private lazy var viewModel: AGCUserViewModel = AGCUserViewModel()
  
  /// Updates the mobile number of the current user.
  /// - Parameters:
  ///   - countryCode: Country/Region code.
  ///   - phoneNumber: New Mobile number.
  ///   - verificationCode: Verification code send to the phone number.
  ///   - resolve: In the success scenario, true value will be returned.
  ///   - reject: In the failure scenario, AGCAuthError will be returned.
  @objc func updatePhone(_ countryCode: String, withPhoneNumber phoneNumber: String, withVerificationCode verificationCode: String, resolver resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) {
    AGCAuthServiceLog.showInPanel(message: #function, type: .call)
    viewModel.updatePhone(countryCode: countryCode, phoneNumber: phoneNumber, verificationCode: verificationCode){ [weak self] (result, error) in
      guard let strongSelf = self else { return }
      if let error = error as NSError? {
        AGCAuthServiceLog.showInPanel(message: #function, type: .fail)
        strongSelf.fail(with: error, reject: reject)
      }else {
        AGCAuthServiceLog.showInPanel(message: #function, type: .success)
        strongSelf.handle(resolve: resolve, nil)
      }
    }
  }
  
  /// Updates the mobile number of the current user.
  /// - Parameters:
  ///   - countryCode: Country/Region code.
  ///   - phoneNumber: New Mobile number.
  ///   - verificationCode: Verification code send to the phone number.
  ///   - locale: Language in which the verification code message is sent.
  ///   - resolve: In the success scenario, true value will be returned.
  ///   - reject: In the failure scenario, AGCAuthError will be returned.
  @objc func updatePhoneWithLocale(_ countryCode: String, withPhoneNumber phoneNumber: String, withVerificationCode verificationCode: String, withLocale locale: String, resolver resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) {
    AGCAuthServiceLog.showInPanel(message: #function, type: .call)
    viewModel.updatePhoneWithLocale(countryCode: countryCode, phoneNumber: phoneNumber, verificationCode: verificationCode, locale: locale) { [weak self] (result, error) in
      guard let strongSelf = self else { return }
      if let error = error as NSError? {
        AGCAuthServiceLog.showInPanel(message: #function, type: .fail)
        strongSelf.fail(with: error, reject: reject)
      }else {
        AGCAuthServiceLog.showInPanel(message: #function, type: .success)
        strongSelf.handle(resolve: resolve, nil)
      }
    }
  }
  
  /// Updates the current user's password.
  /// - Parameters:
  ///   - newPassword: New password.
  ///   - verificationCode: Verification code.
  ///   - provider: Provider type, which is used to differentiate the email address from mobile number.
  ///   - resolve: In the success scenario, true value will be returned.
  ///   - reject: In the failure scenario, AGCAuthError will be returned.
  @objc func updatePassword(_ newPassword: String, withVerificationCode verificationCode: String, withProvider provider: Int, resolver resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) {
    AGCAuthServiceLog.showInPanel(message: #function, type: .call)
    viewModel.updatePassword(newPassword: newPassword, verificationCode: verificationCode, provider: provider) {  [weak self] (result, error) in
      guard let strongSelf = self else { return }
      if let error = error as NSError? {
        AGCAuthServiceLog.showInPanel(message: #function, type: .fail)
        strongSelf.fail(with: error, reject: reject)
      }else {
        AGCAuthServiceLog.showInPanel(message: #function, type: .success)
        strongSelf.handle(resolve: resolve, nil)
      }
    }
  }
  
  /// Updates the current user's email address.
  /// - Parameters:
  ///   - email: New email address.
  ///   - verificationCode: Verification code sent to the email address.
  ///   - resolve: In the success scenario, true value will be returned.
  ///   - reject: In the failure scenario, AGCAuthError will be returned.
  @objc func updateEmail(_ email: String, withVerificationCode verificationCode: String, resolver resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) {
    AGCAuthServiceLog.showInPanel(message: #function, type: .call)
    viewModel.updateEmail(email: email, verificationCode: verificationCode) { [weak self] (result, error) in
      guard let strongSelf = self else { return }
      if let error = error as NSError? {
        AGCAuthServiceLog.showInPanel(message: #function, type: .fail)
        strongSelf.fail(with: error, reject: reject)
      }else {
        AGCAuthServiceLog.showInPanel(message: #function, type: .success)
        strongSelf.handle(resolve: resolve, nil)
      }
    }
  }
  
  /// Updates the current user's email address.
  /// - Parameters:
  ///   - email: New email address.
  ///   - verificationCode: Verification code sent to the email address.
  ///   - locale: Language in which the verification code email is sent.
  ///   - resolve: In the success scenario, true value will be returned.
  ///   - reject: In the failure scenario, AGCAuthError will be returned.
  @objc func updateEmailWithLocale(_ email: String, withVerificationCode verificationCode: String, withLocale locale: String, resolver resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) {
    AGCAuthServiceLog.showInPanel(message: #function, type: .call)
    viewModel.updateEmailWithLocale(email: email, verificationCode: verificationCode, locale: locale) { [weak self] (result, error) in
      guard let strongSelf = self else { return }
      if let error = error as NSError? {
        AGCAuthServiceLog.showInPanel(message: #function, type: .fail)
        strongSelf.fail(with: error, reject: reject)
      }else {
        AGCAuthServiceLog.showInPanel(message: #function, type: .success)
        strongSelf.handle(resolve: resolve, nil)
      }
    }
  }
  
  /// Updates information (profile picture and nickname) for the current user.
  /// - Parameters:
  ///   - profileInfo: Profile information to be modified.
  ///   - resolve: In the success scenario, true value will be returned.
  ///   - reject: In the failure scenario, AGCAuthError will be returned.
  @objc func updateProfile(
    _ profileInfo: Dictionary<String, Any>,
    resolver resolve: @escaping RCTPromiseResolveBlock,
    rejecter reject: @escaping RCTPromiseRejectBlock
  ){
    AGCAuthServiceLog.showInPanel(message: #function, type: .call)
    let profile = AGCProfileRequest()
    if let displayName = profileInfo["displayName"] as? String {
      profile.displayName = displayName
    }
    if let photoUrl = profileInfo["photoUrl"] as? String {
      profile.photoUrl = photoUrl
    }
    viewModel.updateProfile(profileRequest: profile) { [weak self] (result, error) in
      guard let strongSelf = self else { return }
      if let error = error as NSError? {
        AGCAuthServiceLog.showInPanel(message: #function, type: .fail)
        strongSelf.fail(with: error, reject: reject)
      }else {
        AGCAuthServiceLog.showInPanel(message: #function, type: .success)
        strongSelf.handle(resolve: resolve, nil)
      }
    }
  }
  
  /// Obtains the access token of a user from AppGallery Connect.
  /// - Parameters:
  ///   - forceRefresh: Indicates whether to forcibly update the access token of a user.
  ///   - resolve: In the success scenario, TokenResult will be returned.
  ///   - reject: In the failure scenario, AGCAuthError will be returned.
  @objc func getToken(
    _ forceRefresh: Bool,
    resolver resolve: @escaping RCTPromiseResolveBlock,
    rejecter reject: @escaping RCTPromiseRejectBlock
  ) {
    AGCAuthServiceLog.showInPanel(message: #function, type: .call)
    viewModel.getToken(forceRefresh: forceRefresh) { [weak self] (result, error) in
      guard let strongSelf = self else { return }
      if let error = error as NSError? {
        AGCAuthServiceLog.showInPanel(message: #function, type: .fail)
        strongSelf.fail(with: error, reject: reject)
      }
      if let result = result as? AGCToken {
        let response = ReactUtils.tokenToDic(token: result)
        AGCAuthServiceLog.showInPanel(message: #function, type: .success)
        strongSelf.handle(resolve: resolve, response)
      }
    }
  }
  
  /// Obtains UserExtra of the current user.
  /// - Parameters:
  ///   - resolve: In the success scenario, AGCUserExtra will be returned.
  ///   - reject: In the failure scenario, AGCAuthError will be returned.
  @objc func getUserExtra(_ resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) {
    AGCAuthServiceLog.showInPanel(message: #function, type: .call)
    viewModel.getUserExtra(){ [weak self] (result, error) in
      guard let strongSelf = self else { return }
      if let error = error as NSError? {
        AGCAuthServiceLog.showInPanel(message: #function, type: .fail)
        strongSelf.fail(with: error, reject: reject)
      }
      if let result = result as? AGCUserExtra {
        let response = ReactUtils.userExtraToDic(userExtra: result)
        AGCAuthServiceLog.showInPanel(message: #function, type: .success)
        strongSelf.handle(resolve: resolve, response)
      }
    }
  }
  
  /// Unlinks the current user from the linked authentication mode.
  /// - Parameters:
  ///   - provider: Authentication mode to be unlinked.
  ///   - resolve: In the success scenario, current signed in user will be returned.
  ///   - reject: In the failure scenario, AGCAuthError will be returned.
  @objc func unlink(_ provider: Int, resolver resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) {
    AGCAuthServiceLog.showInPanel(message: #function, type: .call)
    if let agcAuthProvider = AGCAuthProviderType.init(rawValue: provider) {
      viewModel.unLink(provider: agcAuthProvider) { [weak self] (result, error) in
        guard let strongSelf = self else { return }
        if let error = error as NSError? {
          AGCAuthServiceLog.showInPanel(message: #function, type: .fail)
          strongSelf.fail(with: error, reject: reject)
        }
        if let result = result as? AGCSignInResult {
          let response = ReactUtils.userToDic(user: result.user)
          AGCAuthServiceLog.showInPanel(message: #function, type: .success)
          strongSelf.handle(resolve: resolve, response)
        }
      }
    }
  }
  
  /// Links a new authentication mode for the current user.
  /// - Parameters:
  ///   - provider: Auth Provider type that will be linked to user.
  ///   - credential: Credential of a new authentication mode.
  ///   - resolve: In the success scenario, current signed in user will be returned.
  ///   - reject: In the failure scenario, AGCAuthError will be returned.
  @objc func link(
    _ credential: Dictionary<String, Any>,
    resolver resolve: @escaping RCTPromiseResolveBlock,
    rejecter reject: @escaping RCTPromiseRejectBlock
  ) {
    AGCAuthServiceLog.showInPanel(message: #function, type: .call)
    guard let provider = credential["provider"] as? Int else { return }
    switch provider {
    case AGCAuthProviderType.phone.rawValue:
      linkPhone(credentailDic: credential, resolve: resolve, reject: reject)
      break
    case AGCAuthProviderType.email.rawValue:
      linkEmail(credentailDic: credential, resolve: resolve, reject: reject)
      break
    case AGCAuthProviderType.apple.rawValue:
      linkApple(credentailDic: credential, resolve: resolve, reject: reject)
      break
    case AGCAuthProviderType.weiXin.rawValue:
      linkWeixin(credentailDic: credential, resolve: resolve, reject: reject)
      break
    case AGCAuthProviderType.facebook.rawValue:
      linkFacebook(credentailDic: credential, resolve: resolve, reject: reject)
      break
    case AGCAuthProviderType.twitter.rawValue:
      linkTwitter(credentailDic: credential, resolve: resolve, reject: reject)
      break
    case AGCAuthProviderType.weiBo.rawValue:
      linkWeibo(credentailDic: credential, resolve: resolve, reject: reject)
      break
    case AGCAuthProviderType.QQ.rawValue:
      linkQQ(credentailDic: credential, resolve: resolve, reject: reject)
      break
    case AGCAuthProviderType.google.rawValue:
      linkGoogle(credentailDic: credential, resolve: resolve, reject: reject)
      break
    case AGCAuthProviderType.selfBuild.rawValue:
      linkSelfBuild(credentailDic: credential, resolve: resolve, reject: reject)
      break
    default:
      AGCAuthServiceLog.showInPanel(message: #function, type: .fail)
      return
    }
  }
  
  // MARK: - Private Helper Functions
  
  private func linkPhone(credentailDic: Dictionary<String, Any>, resolve: @escaping RCTPromiseResolveBlock, reject: @escaping RCTPromiseRejectBlock) {
    var countryCode: String = ""
    var phoneNumber: String = ""
    var verificationCode = ""
    if let code = credentailDic["countryCode"] as? String {
      countryCode = code
    }
    if let number = credentailDic["phoneNumber"] as? String {
      phoneNumber = number
    }
    if let verifyCode = credentailDic["verificationCode"] as? String {
      verificationCode = verifyCode
    }
    let credential = AGCPhoneAuthProvider.credential(withCountryCode: countryCode, phoneNumber: phoneNumber, password: nil, verifyCode: verificationCode)
    linkHandler(credential: credential, resolve: resolve, reject: reject)
  }
  
  private func linkEmail(credentailDic: Dictionary<String, Any>, resolve: @escaping RCTPromiseResolveBlock, reject: @escaping RCTPromiseRejectBlock) {
    var email = ""
    var verificationCode = ""
    if let mail = credentailDic["email"] as? String {
      email = mail
    }
    if let verifyCode = credentailDic["verificationCode"] as? String {
      verificationCode = verifyCode
    }
    let credential = AGCEmailAuthProvider.credential(withEmail: email, password: nil, verifyCode: verificationCode)
    linkHandler(credential: credential, resolve: resolve, reject: reject)
  }
  
  private func linkApple(credentailDic: Dictionary<String, Any>, resolve: @escaping RCTPromiseResolveBlock, reject: @escaping RCTPromiseRejectBlock) {
    var identityToken = ""
    var nonce = ""
    if let token = credentailDic["identityToken"] as? String {
      identityToken = token
    }
    if let random = credentailDic["nonce"] as? String {
      nonce = random
    }
    let credential = AGCAppleIDAuthProvider.credential(withIdentityToken: Data(identityToken.utf8), nonce: nonce)
    linkHandler(credential: credential, resolve: resolve, reject: reject)
  }
  
  private func linkWeixin(credentailDic: Dictionary<String, Any>, resolve: @escaping RCTPromiseResolveBlock, reject: @escaping RCTPromiseRejectBlock) {
    var accessToken = ""
    var openId = ""
    if let token = credentailDic["accessToken"] as? String {
      accessToken = token
    }
    if let id = credentailDic["openId"] as? String {
      openId = id
    }
    let credentail = AGCWeiXinAuthProvider.credential(withToken: accessToken, openId: openId)
    linkHandler(credential: credentail, resolve: resolve, reject: reject)
  }
  
  private func linkFacebook(credentailDic: Dictionary<String, Any>, resolve: @escaping RCTPromiseResolveBlock, reject: @escaping RCTPromiseRejectBlock) {
    var accessToken = ""
    if let token = credentailDic["accessToken"] as? String {
      accessToken = token
    }
    let credentail = AGCFacebookAuthProvider.credential(withToken: accessToken)
    linkHandler(credential: credentail, resolve: resolve, reject: reject)
  }
  
  private func linkTwitter(credentailDic: Dictionary<String, Any>, resolve: @escaping RCTPromiseResolveBlock, reject: @escaping RCTPromiseRejectBlock) {
    var token = ""
    var secret = ""
    if let accessToken = credentailDic["token"] as? String {
      token = accessToken
    }
    if let secretId = credentailDic["secret"] as? String {
      secret = secretId
    }
    let credentail = AGCTwitterAuthProvider.credential(withToken: token, secret: secret)
    linkHandler(credential: credentail, resolve: resolve, reject: reject)
  }
  
  private func linkWeibo(credentailDic: Dictionary<String, Any>, resolve: @escaping RCTPromiseResolveBlock, reject: @escaping RCTPromiseRejectBlock) {
    var token = ""
    var uid = ""
    if let accessToken = credentailDic["token"] as? String {
      token = accessToken
    }
    if let id = credentailDic["uid"] as? String {
      uid = id
    }
    
    let credentail = AGCWeiboAuthProvider.credential(withToken: token, uid: uid)
    linkHandler(credential: credentail, resolve: resolve, reject: reject)
  }
  
  private func linkQQ(credentailDic: Dictionary<String, Any>, resolve: @escaping RCTPromiseResolveBlock, reject: @escaping RCTPromiseRejectBlock) {
    var accessToken = ""
    var openId = ""
    if let token = credentailDic["accessToken"] as? String {
      accessToken = token
    }
    if let id = credentailDic["openId"] as? String {
      openId = id
    }
    let credentail = AGCQQAuthProvider.credential(withToken: accessToken, openId: openId)
    linkHandler(credential: credentail, resolve: resolve, reject: reject)
  }
  
  private func linkGoogle(credentailDic: Dictionary<String, Any>, resolve: @escaping RCTPromiseResolveBlock, reject: @escaping RCTPromiseRejectBlock) {
    var idToken = ""
    if let token = credentailDic["idToken"] as? String {
      idToken = token
    }
    let credentail = AGCGoogleAuthProvider.credential(withToken: idToken)
    linkHandler(credential: credentail, resolve: resolve, reject: reject)
  }
  
  private func linkSelfBuild(credentailDic: Dictionary<String, Any>, resolve: @escaping RCTPromiseResolveBlock, reject: @escaping RCTPromiseRejectBlock) {
    var token = ""
    if let accessToken = credentailDic["token"] as? String {
      token = accessToken
    }
    let credentail = AGCSelfBuildAuthProvider.credential(withToken: token)
    linkHandler(credential: credentail, resolve: resolve, reject: reject)
  }
  
  private func linkHandler(credential: AGCAuthCredential, resolve: @escaping RCTPromiseResolveBlock, reject: @escaping RCTPromiseRejectBlock) {
    viewModel.link(credential: credential) { [weak self] (result, error) in
      guard let strongSelf = self else { return }
      if let error = error as NSError? {
        AGCAuthServiceLog.showInPanel(message: "link", type: .fail)
        strongSelf.fail(with: error, reject: reject)
      }
      if let result = result as? AGCSignInResult {
        let response = ReactUtils.userToDic(user: result.user)
        AGCAuthServiceLog.showInPanel(message: "link", type: .success)
        strongSelf.handle(resolve: resolve, response)
      }
    }
  }
  
  @objc static func requiresMainQueueSetup() -> Bool {
    return true
  }
  
}
