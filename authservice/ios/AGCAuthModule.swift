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

@objc(AGCAuthModule)

/// AGCAuthModule class is the tool class of AGCAuth.
class AGCAuthModule: RCTEventEmitter, AGCAuthServiceHanding {

  /// All the AGCAuth API's can be reached via AGCAuthViewModel class instance
  private lazy var viewModel: AGCAuthViewModel = AGCAuthViewModel()
  private var listenerList = [String: (AGCTokenSnapshot) -> ()]()

  /// Returns current signed in user.
  /// - Parameters:
  ///   - resolve: In the success scenario, current signed in user will be returned
  ///   - reject: In the failure scenario, AGCAuthError will be returned.
  @objc func getUser(_ resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) {
    AGCAuthServiceLog.showInPanel(message: #function, type: .call)
    if let user = AGCAuth.instance().currentUser {
      AGCAuthServiceLog.showInPanel(message: #function, type: .success)
      self.handle(resolve: resolve, ReactUtils.userToDic(user: user))
    }else {
      self.handle(resolve: resolve, nil)
    }
  }

  /// Adds a token change listener.
  /// - Parameters:
  ///   - resolve: Returns true after the function execution is completed.
  ///   - reject: In the failure scenario, AGCAuthError will be returned.
  @objc func addTokenListener(_ id: String) {
    AGCAuthServiceLog.debug(#function){
      let myListener = { (tokenSnapshot:AGCTokenSnapshot) in
        if let _ = tokenSnapshot.token {
          self.sendEvent(withName: "onTokenChanged", body: ReactUtils.tokenSnapshotToDic(tokenSnapshot: tokenSnapshot))
        }
      }
      self.listenerList[id] = myListener
      AGCAuth.instance().addTokenListener(myListener)
      AGCAuthServiceLog.showInPanel(message: #function, type: .success)
    }
  }

  /// Removes the token change listener.
  /// - Parameters:
  ///   - resolve: Returns true after the function execution is completed.
  ///   - reject: In the failure scenario, AGCAuthError will be returned.
  @objc func removeTokenListener(_ id: String) {
    AGCAuthServiceLog.debug(#function) {
      if let listener = listenerList[id] {
        listenerList.removeValue(forKey: id);
        AGCAuth.instance().removeTokenListener(listener)
      }
    }
  }

  /// Signs out a user and deletes the user's cached data.
  /// - Parameters:
  ///   - resolve: Returns true after the function execution is completed.
  ///   - reject: In the failure scenario, AGCAuthError will be returned.
  @objc func signOut(_ resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) {
    AGCAuthServiceLog.debug(#function) {
      AGCAuth.instance().signOut();
      self.handle(resolve: resolve, nil)
    }
  }

  /// Deletes the current user information and cache information from the AppGallery Connect server.
  /// - Parameters:
  ///   - resolve: Returns true after the function execution is completed.
  ///   - reject: In the failure scenario, AGCAuthError will be returned.
  @objc func deleteUser(_ resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) {
    AGCAuthServiceLog.debug(#function) {
      AGCAuth.instance().deleteUser()
      self.handle(resolve: resolve, nil)
    }
  }

  /// Applies for a verification code using a mobile number.
  /// - Parameters:
  ///   - countryCode: Country/Region code.
  ///   - phoneNumber: Mobile number.
  ///   - map: Verification code attributes, including the verification code application scenarios (such as registration, sign-in, and password resetting), language of the verification code message, and interval for sending verification codes.
  ///   - resolve: In the success scenario, VerifyCodeResult will be returned.
  ///   - reject: In the failure scenario, AGCAuthError will be returned.
  @objc func requestPhoneVerifyCode(
    _ countryCode: String,
    withPhoneNumber phoneNumber: String,
    verifyCodeSettings map: Dictionary<String, Any>,
    resolver resolve: @escaping RCTPromiseResolveBlock,
    rejecter reject: @escaping RCTPromiseRejectBlock
  ) {
    AGCAuthServiceLog.showInPanel(message: #function, type: .call)
    var locale = ""
    var sendInterval = 30
    guard let action = map["action"] as? Int else { return }
    if let lang = map["locale"] as? String {
      locale = lang
    }
    if let interval = map["sendInterval"] as? Int {
      sendInterval = interval
    }

    let setting = AGCVerifyCodeSettings.init(action:AGCVerifyCodeAction(rawValue: action) ?? AGCVerifyCodeAction.registerLogin, locale:Locale.init(identifier: locale), sendInterval:sendInterval)

    viewModel.requestPhoneVerifyCode(countryCode: countryCode, phoneNumber: phoneNumber, settings: setting) { [weak self] (result, error) in
      guard let strongSelf = self else { return }
      if let error = error as NSError? {
        AGCAuthServiceLog.showInPanel(message: #function, type: .fail)
        strongSelf.fail(with: error, reject: reject)
      }
      if let result = result as? AGCVerifyCodeResult {
        let response = ReactUtils.verifyCodeToDic(result: result)
        AGCAuthServiceLog.showInPanel(message: #function, type: .success)
        strongSelf.handle(resolve: resolve, response)
      }
    }
  }

  /// Creates an account using a mobile number.
  /// - Parameters:
  ///   - countryCode: Country/Region code.
  ///   - phoneNumber: Mobile number.
  ///   - verificationCode: Verification code.
  ///   - password: Password.
  ///   - resolve: In the success scenario, user will be signed in and returned.
  ///   - reject: In the failure scenario, AGCAuthError will be returned.
  @objc func createPhoneUser(
    _ countryCode: String,
    withPhoneNumber phoneNumber: String,
    withVerifyCode verificationCode: String,
    withPassword password: String,
    resolver resolve: @escaping RCTPromiseResolveBlock,
    rejecter reject: @escaping RCTPromiseRejectBlock
  ) {
    AGCAuthServiceLog.showInPanel(message: #function, type: .call)
    viewModel.createPhoneUser(countryCode: countryCode, phoneNumber: phoneNumber, verificationCode: verificationCode, password: password){ [weak self] (result, error) in
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

  /// Resets a user's password using the mobile number.
  /// - Parameters:
  ///   - countryCode: Country/Region code.
  ///   - phoneNumber: Mobile number.
  ///   - newPassword:  New password.
  ///   - verificationCode: Verification code.
  ///   - resolve: In the success scenario, true value will be returned.
  ///   - reject: In the failure scenario, AGCAuthError will be returned.
  @objc func resetPhonePassword(
    _ countryCode: String,
    withPhoneNumber phoneNumber: String,
    withPassword newPassword: String,
    withVerifyCode verificationCode: String,
    resolver resolve: @escaping RCTPromiseResolveBlock,
    rejecter reject: @escaping RCTPromiseRejectBlock
  ) {
    AGCAuthServiceLog.showInPanel(message: #function, type: .call)
    viewModel.resetPhonePassword(countryCode: countryCode, phoneNumber: phoneNumber, newPassword: newPassword, verificationCode: verificationCode) { [weak self] (result, error) in
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

  /// Applies for a verification code using an email address.
  /// - Parameters:
  ///   - email: Email address.
  ///   - map: Verification code attributes, including the verification code application scenarios (such as registration, sign-in, and password resetting), language of the verification code message, and interval for sending verification codes.
  ///   - resolve: In the success scenario, VerifyCodeResult will be returned.
  ///   - reject: In the failure scenario, AGCAuthError will be returned.
  @objc func requestEmailVerifyCode(
    _ email: String,
    verifyCodeSettings map: Dictionary<String, Any>,
    resolver resolve: @escaping RCTPromiseResolveBlock,
    rejecter reject: @escaping RCTPromiseRejectBlock
  ) {
    AGCAuthServiceLog.showInPanel(message: #function, type: .call)
    var locale = ""
    var sendInterval = 30
    guard let action = map["action"] as? Int else { return }
    if let lang = map["locale"] as? String {
      locale = lang
    }
    if let interval = map["sendInterval"] as? Int {
      sendInterval = interval
    }

    let setting = AGCVerifyCodeSettings.init(action:AGCVerifyCodeAction(rawValue: action) ?? AGCVerifyCodeAction.registerLogin, locale:Locale.init(identifier: locale), sendInterval:sendInterval)
    viewModel.requestEmailVerifyCode(email: email, settings: setting) { [weak self] (result, error) in
      guard let strongSelf = self else { return }
      if let error = error as NSError? {
        AGCAuthServiceLog.showInPanel(message: #function, type: .fail)
        strongSelf.fail(with: error, reject: reject)
      }
      if let result = result as? AGCVerifyCodeResult {
        let response = ReactUtils.verifyCodeToDic(result: result)
        AGCAuthServiceLog.showInPanel(message: #function, type: .success)
        strongSelf.handle(resolve: resolve, response)
      }
    }
  }

  /// Creates an account using an email address.
  /// - Parameters:
  ///   - email: Email address.
  ///   - verificationCode: Verification code.
  ///   - password: Password.
  ///   - resolve: In the success scenario, user will be signed in and returned.
  ///   - reject: In the failure scenario, AGCAuthError will be returned.
  @objc func createEmailUser(
    _ email: String,
    withVerifyCode verificationCode: String,
    withPassword password: String,
    resolver resolve: @escaping RCTPromiseResolveBlock,
    rejecter reject: @escaping RCTPromiseRejectBlock
  ) {
    AGCAuthServiceLog.showInPanel(message: #function, type: .call)
    viewModel.createEmailUser(email: email, verificationCode: verificationCode, password: password){ [weak self] (result, error) in
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

  /// Resets a user's password using the email address.
  /// - Parameters:
  ///   - email: Email address.
  ///   - newPassword: New password.
  ///   - verificationCode: Verification code.
  ///   - resolve: In the success scenario, true value will be returned.
  ///   - reject: In the failure scenario, AGCAuthError will be returned.
  @objc func resetEmailPassword(
    _ email: String,
    withPassword newPassword: String,
    withVerifyCode verificationCode: String,
    resolver resolve: @escaping RCTPromiseResolveBlock,
    rejecter reject: @escaping RCTPromiseRejectBlock
  ) {
    AGCAuthServiceLog.showInPanel(message: #function, type: .call)
    viewModel.resetEmailPassword(email: email, newPassword: newPassword, verificationCode: verificationCode){ [weak self] (result, error) in
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

  /// Signs in a user anonymously.
  /// - Parameters:
  ///   - resolve: In the success scenario, user will be signed in and returned.
  ///   - reject: In the failure scenario, AGCAuthError will be returned.
  @objc func signInAnonymously(_ resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) {
    AGCAuthServiceLog.showInPanel(message: #function, type: .call)
    viewModel.signInAnonymously(){ [weak self] (result, error) in
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

  /// Signs in a user to AppGallery Connect.
  /// - Parameters:
  ///   - provider: Auth Provider type that user will be used to sign in
  ///   - credential: Authentication credential, which must be created using the corresponding Auth Provider type.
  ///   - resolve: In the success scenario, user will be signed in and returned.
  ///   - reject: In the failure scenario, AGCAuthError will be returned.
  @objc func signIn(
    _ credential: Dictionary<String, Any>,
    resolver resolve: @escaping RCTPromiseResolveBlock,
    rejecter reject: @escaping RCTPromiseRejectBlock
  ) {
    AGCAuthServiceLog.showInPanel(message: #function, type: .call)
    guard let provider = credential["provider"] as? Int else { return }
    switch provider {
    case AGCAuthProviderType.phone.rawValue:
      signInWithPhone(credentailDic: credential, resolve: resolve, reject: reject)
      break
    case AGCAuthProviderType.email.rawValue:
      signInWithEmail(credentailDic: credential, resolve: resolve, reject: reject)
      break
    case AGCAuthProviderType.apple.rawValue:
      signInWithApple(credentailDic: credential, resolve: resolve, reject: reject)
      break
    case AGCAuthProviderType.weiXin.rawValue:
      signInWithWeixin(credentailDic: credential, resolve: resolve, reject: reject)
      break
    case AGCAuthProviderType.facebook.rawValue:
      signInWithFacebook(credentailDic: credential, resolve: resolve, reject: reject)
      break
    case AGCAuthProviderType.twitter.rawValue:
      signInWithTwitter(credentailDic: credential, resolve: resolve, reject: reject)
      break
    case AGCAuthProviderType.weiBo.rawValue:
      signInWithWeibo(credentailDic: credential, resolve: resolve, reject: reject)
      break
    case AGCAuthProviderType.QQ.rawValue:
      signInWithQQ(credentailDic: credential, resolve: resolve, reject: reject)
      break
    case AGCAuthProviderType.google.rawValue:
      signInWithGoogle(credentailDic: credential, resolve: resolve, reject: reject)
      break
    case AGCAuthProviderType.selfBuild.rawValue:
      signInWithSelfBuild(credentailDic: credential, resolve: resolve, reject: reject)
      break
    case AGCAuthProviderType.alipay.rawValue:
      signInWithAlipay(credentailDic: credential, resolve: resolve, reject: reject)
      break
    default:
      AGCAuthServiceLog.showInPanel(message: #function, type: .fail)
      let unkownProviderError: NSError = NSError(domain: "PROVIDER_IS_NOT_SUPPORTED", code: -1, userInfo: nil)
      self.fail(with: unkownProviderError, reject: reject)
      return
    }
  }

  // MARK: - Private Helper Functions

  private func signInWithPhone(credentailDic: Dictionary<String, Any>, resolve: @escaping RCTPromiseResolveBlock, reject: @escaping RCTPromiseRejectBlock) {
    if(credentailDic.keys.contains("verificationCode")){
      signInPhoneWithVerification(credentailDic: credentailDic, resolve: resolve, reject: reject)
    }else{
      signInPhoneWithPassword(credentailDic: credentailDic, resolve: resolve, reject: reject)
    }
  }

  private func signInPhoneWithPassword(credentailDic: Dictionary<String, Any>, resolve: @escaping RCTPromiseResolveBlock, reject: @escaping RCTPromiseRejectBlock) {
    var countryCode: String = ""
    var phoneNumber: String = ""
    var password: String = ""
    if let code = credentailDic["countryCode"] as? String {
      countryCode = code
    }
    if let number = credentailDic["phoneNumber"] as? String {
      phoneNumber = number
    }
    if let pass = credentailDic["password"] as? String {
      password = pass
    }
    let credential = AGCPhoneAuthProvider.credential(withCountryCode: countryCode, phoneNumber: phoneNumber, password: password)
    signInHandler(credential: credential, resolve: resolve, reject: reject)
  }

  private func signInPhoneWithVerification(credentailDic: Dictionary<String, Any>, resolve: @escaping RCTPromiseResolveBlock, reject: @escaping RCTPromiseRejectBlock) {
    var countryCode: String = ""
    var phoneNumber: String = ""
    var password: String?
    var verificationCode = ""
    if let code = credentailDic["countryCode"] as? String {
      countryCode = code
    }
    if let number = credentailDic["phoneNumber"] as? String {
      phoneNumber = number
    }
    if let pass = credentailDic["password"] as? String {
      password = pass
    }
    if let verifyCode = credentailDic["verificationCode"] as? String {
      verificationCode = verifyCode
    }
    let credential = AGCPhoneAuthProvider.credential(withCountryCode: countryCode, phoneNumber: phoneNumber, password: password, verifyCode: verificationCode)
    signInHandler(credential: credential, resolve: resolve, reject: reject)
  }

  private func signInWithEmail(credentailDic: Dictionary<String, Any>, resolve: @escaping RCTPromiseResolveBlock, reject: @escaping RCTPromiseRejectBlock) {
    if(credentailDic.keys.contains("verificationCode")){
      signInEmailWithVerification(credentailDic: credentailDic, resolve: resolve, reject: reject)
    }else{
      signInEmailWithPassword(credentailDic: credentailDic, resolve: resolve, reject: reject)
    }
  }

  private func signInEmailWithPassword(credentailDic: Dictionary<String, Any>, resolve: @escaping RCTPromiseResolveBlock, reject: @escaping RCTPromiseRejectBlock) {
    var email = ""
    var password: String = ""
    if let mail = credentailDic["email"] as? String {
      email = mail
    }
    if let pass = credentailDic["password"] as? String {
      password = pass
    }
    let credential = AGCEmailAuthProvider.credential(withEmail: email, password: password)
    signInHandler(credential: credential, resolve: resolve, reject: reject)
  }

  private func signInEmailWithVerification(credentailDic: Dictionary<String, Any>, resolve: @escaping RCTPromiseResolveBlock, reject: @escaping RCTPromiseRejectBlock) {
    var email = ""
    var password: String?
    var verificationCode = ""
    if let mail = credentailDic["email"] as? String {
      email = mail
    }
    if let pass = credentailDic["password"] as? String {
      password = pass
    }
    if let verifyCode = credentailDic["verificationCode"] as? String {
      verificationCode = verifyCode
    }
    let credential = AGCEmailAuthProvider.credential(withEmail: email, password: password, verifyCode: verificationCode)
    signInHandler(credential: credential, resolve: resolve, reject: reject)
  }

  private func signInWithApple(credentailDic: Dictionary<String, Any>, resolve: @escaping RCTPromiseResolveBlock, reject: @escaping RCTPromiseRejectBlock) {
    var identityToken = ""
    var nonce = ""
    var autoCreateUser = true
    if let token = credentailDic["identityToken"] as? String {
      identityToken = token
    }
    if let random = credentailDic["nonce"] as? String {
      nonce = random
    }
    if let createUser = credentailDic["autoCreateUser"] as? Bool {
      autoCreateUser = createUser
    }

    let credential = AGCAppleIDAuthProvider.credential(withIdentityToken: Data(identityToken.utf8), nonce: nonce, autoCreateUser: autoCreateUser)
    signInHandler(credential: credential, resolve: resolve, reject: reject)
  }

  private func signInWithWeixin(credentailDic: Dictionary<String, Any>, resolve: @escaping RCTPromiseResolveBlock, reject: @escaping RCTPromiseRejectBlock) {
    var accessToken = ""
    var openId = ""
    var autoCreateUser = true
    if let token = credentailDic["accessToken"] as? String {
      accessToken = token
    }
    if let id = credentailDic["openId"] as? String {
      openId = id
    }
    if let createUser = credentailDic["autoCreateUser"] as? Bool {
      autoCreateUser = createUser
    }
    let credentail = AGCWeiXinAuthProvider.credential(withToken: accessToken, openId: openId, autoCreateUser: autoCreateUser)
    signInHandler(credential: credentail, resolve: resolve, reject: reject)
  }

  private func signInWithFacebook(credentailDic: Dictionary<String, Any>, resolve: @escaping RCTPromiseResolveBlock, reject: @escaping RCTPromiseRejectBlock) {
    var accessToken = ""
    var autoCreateUser = true
    if let token = credentailDic["accessToken"] as? String {
      accessToken = token
    }
    if let createUser = credentailDic["autoCreateUser"] as? Bool {
      autoCreateUser = createUser
    }
    let credentail = AGCFacebookAuthProvider.credential(withToken: accessToken, autoCreateUser: autoCreateUser)
    signInHandler(credential: credentail, resolve: resolve, reject: reject)
  }

  private func signInWithTwitter(credentailDic: Dictionary<String, Any>, resolve: @escaping RCTPromiseResolveBlock, reject: @escaping RCTPromiseRejectBlock) {

    var version : Int = 0
    var autoCreateUser = true

    if let credentailVersion = credentailDic["version"] as? Int {
      version = credentailVersion
    }

    if version == 1 {
      var token = ""
      var secret = ""
      if let accessToken = credentailDic["token"] as? String {
        token = accessToken
      }
      if let secretId = credentailDic["secret"] as? String {
        secret = secretId
      }
      if let createUser = credentailDic["autoCreateUser"] as? Bool {
        autoCreateUser = createUser
      }
      let credentail = AGCTwitterAuthProvider.credential(withToken: token, secret: secret, autoCreateUser: autoCreateUser)
      signInHandler(credential: credentail, resolve: resolve, reject: reject)
    }
    else{
      var clientId = ""
      var authCode = ""
      var codeVerifier = ""
      var redirectUrl = ""
      if let id = credentailDic["clientId"] as? String {
        clientId = id
      }
      if let code = credentailDic["authCode"] as? String {
        authCode = code
      }
      if let verifier = credentailDic["codeVerifier"] as? String {
        codeVerifier = verifier
      }
      if let url = credentailDic["redirectUrl"] as? String {
        redirectUrl = url
      }
      if let createUser = credentailDic["autoCreateUser"] as? Bool {
        autoCreateUser = createUser
      }
      var authParam = AGCTwitterAuthParam(clientId: clientId, authCode: authCode, codeVerifier: codeVerifier, redirectUrl: redirectUrl)
      let credentail = AGCTwitterAuthProvider.credential(with: authParam, autoCreateUser: autoCreateUser)
      signInHandler(credential: credentail, resolve: resolve, reject: reject)
    }


  }

  private func signInWithWeibo(credentailDic: Dictionary<String, Any>, resolve: @escaping RCTPromiseResolveBlock, reject: @escaping RCTPromiseRejectBlock) {
    var token = ""
    var uid = ""
    var autoCreateUser = true
    if let accessToken = credentailDic["token"] as? String {
      token = accessToken
    }
    if let id = credentailDic["uid"] as? String {
      uid = id
    }
    if let createUser = credentailDic["autoCreateUser"] as? Bool {
      autoCreateUser = createUser
    }
    let credentail = AGCWeiboAuthProvider.credential(withToken: token, uid: uid, autoCreateUser: autoCreateUser)
    signInHandler(credential: credentail, resolve: resolve, reject: reject)
  }

  private func signInWithQQ(credentailDic: Dictionary<String, Any>, resolve: @escaping RCTPromiseResolveBlock, reject: @escaping RCTPromiseRejectBlock) {
    var accessToken = ""
    var openId = ""
    var autoCreateUser = true
    if let token = credentailDic["accessToken"] as? String {
      accessToken = token
    }
    if let id = credentailDic["openId"] as? String {
      openId = id
    }
    if let createUser = credentailDic["autoCreateUser"] as? Bool {
      autoCreateUser = createUser
    }
    let credentail = AGCQQAuthProvider.credential(withToken: accessToken, openId: openId, autoCreateUser: autoCreateUser)
    signInHandler(credential: credentail, resolve: resolve, reject: reject)
  }

  private func signInWithGoogle(credentailDic: Dictionary<String, Any>, resolve: @escaping RCTPromiseResolveBlock, reject: @escaping RCTPromiseRejectBlock) {
    var idToken = ""
    var autoCreateUser = true
    if let token = credentailDic["idToken"] as? String {
      idToken = token
    }
    if let createUser = credentailDic["autoCreateUser"] as? Bool {
      autoCreateUser = createUser
    }
    let credentail = AGCGoogleAuthProvider.credential(withToken: idToken, autoCreateUser: autoCreateUser)
    signInHandler(credential: credentail, resolve: resolve, reject: reject)
  }

  private func signInWithSelfBuild(credentailDic: Dictionary<String, Any>, resolve: @escaping RCTPromiseResolveBlock, reject: @escaping RCTPromiseRejectBlock) {
    var token = ""
    var autoCreateUser = true
    if let accessToken = credentailDic["token"] as? String {
      token = accessToken
    }
    if let createUser = credentailDic["autoCreateUser"] as? Bool {
      autoCreateUser = createUser
    }
    let credentail = AGCSelfBuildAuthProvider.credential(withToken: token, autoCreateUser: autoCreateUser)
    signInHandler(credential: credentail, resolve: resolve, reject: reject)
  }

  private func signInWithAlipay(credentailDic: Dictionary<String, Any>, resolve: @escaping RCTPromiseResolveBlock, reject: @escaping RCTPromiseRejectBlock) {
    var authCode = ""
    var autoCreateUser = true
    if let code = credentailDic["authCode"] as? String {
      authCode = code
    }
    if let createUser = credentailDic["autoCreateUser"] as? Bool {
      autoCreateUser = createUser
    }
    let credentail = AGCAlipayAuthProvider.credential(withAuthCode: authCode, autoCreateUser: autoCreateUser)
    signInHandler(credential: credentail, resolve: resolve, reject: reject)
  }


  private func signInHandler(credential: AGCAuthCredential, resolve: @escaping RCTPromiseResolveBlock, reject: @escaping RCTPromiseRejectBlock) {
    viewModel.signIn(credential: credential){ [weak self] (result, error) in
      guard let strongSelf = self else { return }
      if let error = error as NSError? {
        AGCAuthServiceLog.showInPanel(message: "signIn", type: .fail)
        strongSelf.fail(with: error, reject: reject)
      }
      if let result = result as? AGCSignInResult {
        let response = ReactUtils.userToDic(user: result.user)
        AGCAuthServiceLog.showInPanel(message: "signIn", type: .success)
        strongSelf.handle(resolve: resolve, response)
      }
    }
  }

  override func supportedEvents() -> [String]! {
    return ["onTokenChanged"]
  }

  override func constantsToExport() -> [AnyHashable : Any]! {
    let actions = [
      "ACTION_REGISTER_LOGIN": AGCVerifyCodeAction.registerLogin.rawValue,
      "ACTION_RESET_PASSWORD": AGCVerifyCodeAction.resetPassword.rawValue
    ]
    let providers = [
      "ANONYMOUS": AGCAuthProviderType.anonymous.rawValue,
      "HMS_Provider": AGCAuthProviderType.HMS.rawValue,
      "Facebook_Provider": AGCAuthProviderType.facebook.rawValue,
      "Twitter_Provider": AGCAuthProviderType.twitter.rawValue,
      "WeiXin_Provider": AGCAuthProviderType.weiXin.rawValue,
      "HWGame_Provider": AGCAuthProviderType.hwGame.rawValue,
      "QQ_Provider": AGCAuthProviderType.QQ.rawValue,
      "WeiBo_Provider": AGCAuthProviderType.weiBo.rawValue,
      "Google_Provider": AGCAuthProviderType.google.rawValue,
      "GoogleGame_Provider": AGCAuthProviderType.googleGame.rawValue,
      "SelfBuild_Provider": AGCAuthProviderType.selfBuild.rawValue,
      "Phone_Provider": AGCAuthProviderType.phone.rawValue,
      "Email_Provider": AGCAuthProviderType.email.rawValue,
      "Apple_Provider": AGCAuthProviderType.apple.rawValue,
      "Alipay_Provider": AGCAuthProviderType.alipay.rawValue
    ]
    return [
      "action": actions,
      "provider": providers
    ]
  }
  
  override static func requiresMainQueueSetup() -> Bool {
    return true
  }
  
}
