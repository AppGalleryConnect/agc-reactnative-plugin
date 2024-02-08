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

public class AGCAuthViewModel {
  
  public typealias CompletionHandler = (_ result: Any?, _ error: Error?) -> Void
  
  func requestPhoneVerifyCode(countryCode: String, phoneNumber: String, settings: AGCVerifyCodeSettings, completion: @escaping CompletionHandler){
    AGCAuth.instance().requestVerifyCode(
      withCountryCode: countryCode,
      phoneNumber: phoneNumber,
      settings: settings
    ).onSuccess(callback: { result in
      completion(result, nil)
    }).onFailure(callback: {error in
      completion(nil, error)
    })
  }
  
  func createPhoneUser(countryCode: String, phoneNumber: String, verificationCode: String, password: String, completion: @escaping CompletionHandler){
    AGCAuth.instance().createUser(withCountryCode: countryCode, phoneNumber: phoneNumber, password: password, verifyCode: verificationCode).onSuccess(callback: { result in
      completion(result, nil)
    }).onFailure(callback: { error in
      completion(nil, error)
    })
  }
  
  func resetPhonePassword(countryCode: String, phoneNumber: String, newPassword: String, verificationCode: String, completion: @escaping CompletionHandler) {
    AGCAuth.instance().resetPassword(withCountryCode: countryCode, phoneNumber: phoneNumber, newPassword: newPassword, verifyCode: verificationCode)
      .onSuccess(callback: { result in
        completion(result, nil)
      }).onFailure(callback: { error in
        completion(nil, error)
      })
  }
  
  func requestEmailVerifyCode(email: String, settings: AGCVerifyCodeSettings, completion: @escaping CompletionHandler) {
    AGCAuth.instance().requestVerifyCode(withEmail: email, settings: settings)
      .onSuccess(callback: { result in
        completion(result, nil)
      }).onFailure(callback: { error in
        completion(nil, error)
      })
  }
  
  func createEmailUser(email: String, verificationCode: String, password: String, completion: @escaping CompletionHandler) {
    AGCAuth.instance().createUser(withEmail: email, password: password, verifyCode: verificationCode)
      .onSuccess(callback: { result in
        completion(result, nil)
      }).onFailure(callback: { error in
        completion(nil, error)
      })
  }
  
  func resetEmailPassword(email: String, newPassword: String, verificationCode: String, completion: @escaping CompletionHandler) {
    AGCAuth.instance().resetPassword(withEmail: email, newPassword: newPassword, verifyCode: verificationCode)
      .onSuccess(callback: { result in
        completion(result, nil)
      }).onFailure(callback: { error in
        completion(nil, error)
      })
  }
  
  func signInAnonymously(completion: @escaping CompletionHandler) {
    AGCAuth.instance().signInAnonymously()
      .onSuccess(callback: { result in
        completion(result, nil)
      }).onFailure(callback: {error in
        completion(nil, error)
      })
  }
  
  func signIn(credential: AGCAuthCredential, completion: @escaping CompletionHandler) {
    AGCAuth.instance().signIn(credential: credential).onSuccess(callback: { result in
      completion(result, nil)
    }).onFailure(callback: { error in
      completion(nil, error)
    })
  }
  
}
