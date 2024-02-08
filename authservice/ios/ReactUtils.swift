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

class ReactUtils {
  
  static func verifyCodeToDic(result: AGCVerifyCodeResult) -> Dictionary<String, String> {
    var map: Dictionary<String, String> = Dictionary<String,String>()
    map["shortestInterval"] = result.shortestInterval
    map["validityPeriod"] =  result.validityPeriod
    return map
  }
  
  static func userToDic(user: AGCUser) -> Dictionary<String, Any> {
    var map: Dictionary<String, Any> = Dictionary<String, Any>()
    map["displayName"] = user.displayName
    map["emailVerified"] = user.emailVerified
    map["email"] = user.email
    map["isAnonymous"] = user.isAnonymous
    map["passwordSetted"] = user.passwordSetted
    map["phoneNumber"] = user.phone
    map["photoUrl"] = user.photoUrl
    map["providerId"] = user.providerId
    map["uid"] = user.uid
    map["providerInfo"] = user.providerInfo
    return map
  }
  
  static func tokenToDic(token: AGCToken) -> Dictionary<String, Any> {
    var map: Dictionary<String, Any> = Dictionary<String, Any>()
    map["token"] = token.tokenString
    map["expirePeriod"] = token.expiration
    return map
  }
  
  static func tokenSnapshotToDic(tokenSnapshot: AGCTokenSnapshot) -> Dictionary<String, Any> {
    var map: Dictionary<String, Any> = Dictionary<String, Any>()
    map["state"] = tokenSnapshot.state.rawValue
    map["token"] = tokenSnapshot.token
    return map
  }
  
  static func userExtraToDic(userExtra: AGCUserExtra) -> Dictionary<String, Any> {
    var map: Dictionary<String, Any> = Dictionary<String, Any>()
    map["createTime"] = userExtra.createTime
    map["lastSignInTime"] = userExtra.lastSignInTime
    return map
  }
  
}
