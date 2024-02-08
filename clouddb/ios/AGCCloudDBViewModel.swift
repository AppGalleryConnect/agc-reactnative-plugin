/*
 * Copyright 2021-2023. Huawei Technologies Co., Ltd. All rights reserved.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License")
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
import AGConnectDatabase

public class AGCCloudDBViewModel {
  
  public typealias CompletionHandler = (_ result: Any?, _ error: Error?) -> Void
  
  func openCloudDBZone2(cloudDB: AGConnectCloudDB, zoneConfig: AGCCloudDBZoneConfig, isAllowToCreate: Bool, completion: @escaping CompletionHandler) {
    cloudDB.openZone2(zoneConfig, allowCreate: isAllowToCreate) { (dbZone, error) in
      if let error = error {
        completion(nil, error)
      }
      completion(dbZone, nil)
    }
  }
  
  func openCloudDBZone(cloudDB: AGConnectCloudDB, zoneConfig: AGCCloudDBZoneConfig, isAllowToCreate: Bool, completion: @escaping CompletionHandler) {
    do {
      let dbZone = try cloudDB.openZone(zoneConfig, allowCreate: isAllowToCreate)
      completion(dbZone, nil)
    } catch {
      completion(nil, error)
    }
  }

  func setUserKey(cloudDB: AGConnectCloudDB, userKey: String, userReKey: String, needStrongCheck: Bool , completion: @escaping CompletionHandler) {
    cloudDB.setUserKey(userKey, userRekey: userReKey, needStrongCheck: needStrongCheck) { error in
      if let error = error {
        completion(nil, error)
      } else {
        completion(nil, nil)
      }
    }
  }
  
  func updateDataEncryptionKey(cloudDB: AGConnectCloudDB, completion: @escaping CompletionHandler) {
    cloudDB.updateDataEncryptionKey() { error in
      if let error = error {
        completion(nil, error)
      } else {
        completion(nil, nil)
      }
    }
  }
  
  func executeUpsert(dbZone: AGCCloudDBZone, instanceArray: [AGCCloudDBObject], completion: @escaping CompletionHandler) {
    dbZone.executeUpsert(instanceArray) { result, error in
      if let error = error {
        completion(nil, error)
      } else {
        completion(result, nil)
      }
    }
  }
  
  func executeDelete(dbZone: AGCCloudDBZone, instanceArray: [AGCCloudDBObject], completion: @escaping CompletionHandler) {
    dbZone.executeDelete(instanceArray) { result, error in
      if let error = error {
        completion(nil, error)
      } else {
        completion(result, nil)
      }
    }
  }
  
  func executeQuery(dbZone: AGCCloudDBZone, query: AGCCloudDBQuery, queryPolicy: AGCCloudDBQueryPolicy, completion: @escaping CompletionHandler) {
    dbZone.execute(query, policy: queryPolicy){ AGCCloudDBSnapshot,error in
      if let error = error {
        completion(nil, error)
      }
      if let snapshot = AGCCloudDBSnapshot {
        completion(snapshot, nil)
      }
    }
  }
  
  func executeQueryAverage(dbZone: AGCCloudDBZone, query: AGCCloudDBQuery, queryPolicy: AGCCloudDBQueryPolicy, fieldName: String, completion: @escaping CompletionHandler) {
    dbZone.executeQueryAverage(query, policy: queryPolicy, field: fieldName) { value ,error in
      if let error = error {
        completion(nil, error)
      } else {
        completion(value, nil)
      }
    }
  }
  
  func executeSumQuery(dbZone: AGCCloudDBZone, query: AGCCloudDBQuery, queryPolicy: AGCCloudDBQueryPolicy, fieldName: String, completion: @escaping CompletionHandler) {
    dbZone.executeQuerySum(query, policy: queryPolicy, field: fieldName) { value ,error in
      if let error = error {
        completion(nil, error)
      } else {
        completion(value, nil)
      }
    }
  }
  
  func executeMaximumQuery(dbZone: AGCCloudDBZone, query: AGCCloudDBQuery, queryPolicy: AGCCloudDBQueryPolicy, fieldName: String, completion: @escaping CompletionHandler) {
    dbZone.executeQueryMaximum(query, policy: queryPolicy, field: fieldName) { value ,error in
      if let error = error {
        completion(nil, error)
      } else {
        completion(value, nil)
      }
    }
  }
  
  func executeMinimalQuery(dbZone: AGCCloudDBZone, query: AGCCloudDBQuery, queryPolicy: AGCCloudDBQueryPolicy, fieldName: String, completion: @escaping CompletionHandler) {
    dbZone.executeQueryMinimum(query, policy: queryPolicy, field: fieldName) { value ,error in
      if let error = error {
        completion(nil, error)
      } else {
        completion(value, nil)
      }
    }
  }
  
  func executeCountQuery(dbZone: AGCCloudDBZone, query: AGCCloudDBQuery, queryPolicy: AGCCloudDBQueryPolicy, fieldName: String, completion: @escaping CompletionHandler) {
    dbZone.executeQueryCount(query, policy: queryPolicy, field: fieldName) { value ,error in
      if let error = error {
        completion(nil, error)
      } else {
        completion(value, nil)
      }
    }
  }
  
    func executeServerStatusQuery(dbZone: AGCCloudDBZone, completion: @escaping CompletionHandler) {
        dbZone.executeServerStatusQuery() { value ,error in
        if let error = error {
          completion(nil, error)
        } else {
          completion(value, nil)
        }
      }
    }
  
  func executeQueryUnsynced(dbZone: AGCCloudDBZone, query: AGCCloudDBQuery, completion: @escaping CompletionHandler) {
    dbZone.executeQueryUnsynced(query) { AGCCloudDBSnapshot ,error in
      if let error = error {
        completion(nil, error)
      }
      if let snapshot = AGCCloudDBSnapshot {
        completion(snapshot, nil)
      }
    }
  }
  
  func subscribeSnapshot(dbZone: AGCCloudDBZone, query: AGCCloudDBQuery, queryPolicy: AGCCloudDBQueryPolicy, completion: @escaping CompletionHandler) -> AGCCloudDBListenerHandler? {
    return dbZone.subscribeSnapshot(with: query, policy: queryPolicy) { AGCCloudDBSnapshot ,error in
      if let error = error {
        completion(nil, error)
      }
      if let snapshot = AGCCloudDBSnapshot {
        completion(snapshot, nil)
      }
    }
  }
}
