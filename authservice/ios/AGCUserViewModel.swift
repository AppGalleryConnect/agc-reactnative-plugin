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

public class AGCUserViewModel {
  
  public typealias CompletionHandler = (_ result: Any?, _ error: Error?) -> Void
  private let userNilError = AGCAuthError(domain: "com.huawei.agc.auth", code: 2, userInfo: [NSDebugDescriptionErrorKey: "User Null"])
  
  func updatePhone(countryCode: String, phoneNumber: String, verificationCode: String, completion: @escaping CompletionHandler) {
    if let user = AGCAuth.instance().currentUser {
      user.updatePhone(withCountryCode: countryCode, phoneNumber: phoneNumber, verifyCode: verificationCode)
        .onSuccess(callback: { result in
          completion(result, nil)
        }).onFailure(callback: { error in
          completion(nil, error)
        })
    } else {
      completion(nil, userNilError)
    }
  }
  
  func updatePhoneWithLocale(countryCode: String, phoneNumber: String, verificationCode: String, locale: String, completion: @escaping CompletionHandler) {
    if let user = AGCAuth.instance().currentUser {
      user.updatePhone(withCountryCode: countryCode, phoneNumber: phoneNumber, verifyCode: verificationCode, locale: Locale.init(identifier: locale ))
        .onSuccess(callback: { result in
          completion(result, nil)
        }).onFailure(callback: { error in
          completion(nil, error)
        })
    }else {
      completion(nil, userNilError)
    }
  }
  
  func updatePassword(newPassword: String, verificationCode: String, provider: Int, completion: @escaping CompletionHandler) {
    if let user = AGCAuth.instance().currentUser {
      user.updatePassword(newPassword, verifyCode: verificationCode, provider: provider)
        .onSuccess(callback: { result in
          completion(result, nil)
        }).onFailure(callback: { error in
          completion(nil, error)
        })
    }else {
      completion(nil, userNilError)
    }
  }
  
  func updateEmail(email: String, verificationCode: String, completion: @escaping CompletionHandler) {
    if let user = AGCAuth.instance().currentUser {
      user.updateEmail(email, verifyCode: verificationCode)
        .onSuccess(callback: { result in
          completion(result, nil)
        }).onFailure(callback: { error in
          completion(nil, error)
        })
    }else {
      completion(nil, userNilError)
    }
  }
  
  func updateEmailWithLocale(email: String, verificationCode: String, locale: String, completion: @escaping CompletionHandler) {
    if let user = AGCAuth.instance().currentUser {
      user.updateEmail(email, verifyCode: verificationCode,locale: Locale.init(identifier: locale))
        .onSuccess(callback: { result in
          completion(result, nil)
        }).onFailure(callback: { error in
          completion(nil, error)
        })
    }else {
      completion(nil, userNilError)
    }
  }
  
  func updateProfile(profileRequest: AGCProfileRequest, completion: @escaping CompletionHandler) {
    if let user = AGCAuth.instance().currentUser {
      user.updateProfile(profileRequest)
        .onSuccess(callback: { result in
          completion(result, nil)
        }).onFailure(callback: { error in
          completion(nil, error)
        })
    }else {
      completion(nil, userNilError)
    }
  }
  
  func getToken(forceRefresh: Bool, completion: @escaping CompletionHandler) {
    if let user = AGCAuth.instance().currentUser{
      user.getToken(forceRefresh)
        .onSuccess(callback: { result in
          completion(result, nil)
        }).onFailure(callback: {error in
          completion(nil, error)
        })
    } else {
      completion(nil, userNilError)
    }
  }
  
  func getUserExtra(completion: @escaping CompletionHandler) {
    if let user = AGCAuth.instance().currentUser {
      user.getExtra()
        .onSuccess(callback: { result in
          completion(result, nil)
        }).onFailure(callback: { error in
          completion(nil, error)
        })
    }else {
      completion(nil, userNilError)
    }
  }
  
  func unLink(provider: AGCAuthProviderType, completion: @escaping CompletionHandler) {
    if let user = AGCAuth.instance().currentUser {
      user.unlink(provider)
        .onSuccess(callback: { result in
          completion(result, nil)
        }).onFailure(callback: { error in
          completion(nil, error)
        })
    }else {
      completion(nil, userNilError)
    }
  }
  
  func link(credential: AGCAuthCredential, completion: @escaping CompletionHandler) {
    if let user = AGCAuth.instance().currentUser {
      user.link(credential)
        .onSuccess(callback: { result in
          completion(result, nil)
        }).onFailure(callback: { error in
          completion(nil, error)
        })
    }else {
      completion(nil, userNilError)
    }
  }
  
  
}
