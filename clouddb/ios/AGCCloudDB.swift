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

@objc(AGCCloudDB)

class AGCCloudDB: RCTEventEmitter, AGCCloudDBHanding {
  
  private lazy var viewModel: AGCCloudDBViewModel = AGCCloudDBViewModel();
  
  private var dbZoneList = ConcurrentHashMap<String, AGCCloudDBZone>();
  private var listenerHandlerList = ConcurrentHashMap<String, ConcurrentHashMap<String, AGCCloudDBListenerHandler>>();
  private var dbZone: AGCCloudDBZone? =  nil;
  private var agcCloudDB: AGConnectCloudDB? = nil;
  
  private let cloudDBClassError = NSError.init(domain: "Class cannot be found.", code: 9, userInfo: nil)
  private let cloudDBNullError = NSError.init(domain: "CloudDB is null", code: 10, userInfo: nil)
  private let dbZoneNullError = NSError.init(domain: "CloudDBZone is null, try re-open it", code: 11, userInfo: nil)
  
  @objc func initialize() {
    let errorPointer: NSErrorPointer = nil;
    AGConnectCloudDB.initEnvironment(errorPointer);
    agcCloudDB = AGConnectCloudDB.shareInstance();
  }
  
  @objc func createObjectType(_ resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) {
    guard let agcCloudDB = agcCloudDB else {
      return self.fail(with: cloudDBNullError, reject: reject)
    }
    var errorPointer: NSError? = nil;
    agcCloudDB.createObjectType(AGCCloudDBObjectTypeInfoHelper.obtainObjectTypeInfo(), error: &errorPointer);
    if let error = errorPointer {
      self.fail(with: error, reject: reject)
    } else {
      self.handle(resolve: resolve)
    }
  }
  
  @objc func getCloudDBZoneConfigs(_ resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) {
    guard let agcCloudDB = agcCloudDB else {
      return self.fail(with: cloudDBNullError, reject: reject)
    }
    let array: NSMutableArray = []
    for zoneConfig in agcCloudDB.zoneConfigs() {
      var map: Dictionary<String, Any> = Dictionary<String, Any>()
      map["accessProperty"] = zoneConfig.accessMode.rawValue
      map["capacity"] = zoneConfig.capacity
      map["cloudDBZoneName"] = zoneConfig.zoneName
      map["isPersistenceAvailable"] = zoneConfig.persistence
      map["syncProperty"] = zoneConfig.syncMode.rawValue
      map["isEncrypted"] = zoneConfig.encrypted
      array.add(map)
    }
    self.handle(resolve: resolve, array)
    
  }
  
  @objc func openCloudDBZone(_ map: Dictionary<String, Any>, isAllowToCreate allowed: Bool, id dbZoneId: String, resolver resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) {
    guard let agcCloudDB = agcCloudDB else {
      return self.fail(with: cloudDBNullError, reject: reject)
    }
    if let zoneConfig = handleConfig(map: map) {
      viewModel.openCloudDBZone(cloudDB: agcCloudDB, zoneConfig: zoneConfig, isAllowToCreate: allowed) { [weak self] (dbZone, error) in
        guard let strongSelf = self else { return }
        if let error = error as NSError? {
          strongSelf.fail(with: error, reject: reject)
        }
        if let dbZone =  dbZone as? AGCCloudDBZone {
          strongSelf.dbZoneList[dbZoneId] = dbZone
          strongSelf.handle(resolve: resolve)
        }
      }
    } else {
      self.fail(with: "Config could not be initialized.", reject: reject)
    }
  }
  
  @objc func openCloudDBZone2(_ map: Dictionary<String, Any>, isAllowToCreate allowed: Bool, id dbZoneId: String, resolver resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) {
    guard let agcCloudDB = agcCloudDB else {
      return self.fail(with: cloudDBNullError, reject: reject)
    }
    if let zoneConfig = handleConfig(map: map) {
      viewModel.openCloudDBZone2(cloudDB: agcCloudDB, zoneConfig: zoneConfig, isAllowToCreate: allowed) { [weak self] (dbZone, error) in
        guard let strongSelf = self else { return }
        if let error = error as NSError? {
          strongSelf.fail(with: error, reject: reject)
        }
        if let dbZone =  dbZone as? AGCCloudDBZone {
          strongSelf.dbZoneList[dbZoneId] = dbZone
          strongSelf.handle(resolve: resolve)
        }
      }
    } else {
      self.fail(with: "Config could not be initialized.", reject: reject)
    }
  }
  
  @objc func deleteCloudDBZone(_ zoneName: String, resolver resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) {
    guard let agcCloudDB = agcCloudDB else {
      return self.fail(with: cloudDBNullError, reject: reject)
    }
    do {
      try ObjC.delete(agcCloudDB, zoneName: zoneName)
    } catch {
      return self.fail(with: error, reject: reject)
    }
    self.handle(resolve: resolve)
  }
  
  @objc func enableNetwork(_ zoneName: String, resolver resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) {
    guard let agcCloudDB = agcCloudDB else {
      return self.fail(with: cloudDBNullError, reject: reject)
    }
    let result = agcCloudDB.enableNetwork(zoneName)
    if result == true {
      self.handle(resolve: resolve)
    } else {
      self.fail(with: "Failed to enable naturalStore network.", reject: reject)
    }
    
  }
  
  @objc func disableNetwork(_ zoneName: String, resolver resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) {
    guard let agcCloudDB = agcCloudDB else {
      return self.fail(with: cloudDBNullError, reject: reject)
    }
    let result = agcCloudDB.disableNetwork(zoneName)
    if result == true {
      self.handle(resolve: resolve)
    } else {
      self.fail(with: "Failed to enable naturalStore network.", reject: reject)
    }
  }

  @objc func setUserKey(_ userKey: String, reKey userRekey: String, needStrongCheck needStrongCheck: Bool, resolver resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) {
    guard let agcCloudDB = agcCloudDB else {
      return self.fail(with: cloudDBNullError, reject: reject)
    }
    viewModel.setUserKey(cloudDB: agcCloudDB, userKey: userKey, userReKey: userRekey, needStrongCheck: needStrongCheck) { [weak self] _, error   in
      guard let strongSelf = self else { return }
      if let error = error as NSError? {
        strongSelf.fail(with: error, reject: reject)
      } else {
        strongSelf.handle(resolve: resolve, true)
      }
    }
  }
  
  @objc func updateDataEncryptionKey(_ resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) {
    guard let agcCloudDB = agcCloudDB else {
      return self.fail(with: cloudDBNullError, reject: reject)
    }
    viewModel.updateDataEncryptionKey(cloudDB: agcCloudDB) { [weak self] _, error in
      guard let strongSelf = self else { return }
      if let error = error as NSError? {
        strongSelf.fail(with: error, reject: reject)
      } else {
        strongSelf.handle(resolve: resolve, true)
      }
    }
  }
  
  @objc func addEventListener(_ resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) {
    guard let agcCloudDB = agcCloudDB else {
      return self.fail(with: cloudDBNullError, reject: reject)
    }
    agcCloudDB.addEventCallback() { result in
      self.sendEvent(withName: "onEvent", body: result.rawValue)
    }
    self.handle(resolve: resolve)
  }
  
  @objc func addDataEncryptionKeyListener(_ resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) {
    guard let agcCloudDB = agcCloudDB else {
      return self.fail(with: cloudDBNullError, reject: reject)
    }
    agcCloudDB.addDataEncryptionKeyCallback { () -> Bool in
      self.sendEvent(withName: "onDataKeyChange", body: true)
      return true
    }
    self.handle(resolve: resolve)
  }
  
  @objc func closeCloudDBZone(_ id: String, resolver resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) {
    guard let agcCloudDB = agcCloudDB else {
      return self.fail(with: cloudDBNullError, reject: reject)
    }
    guard let dbZone = getZoneFromList(id: id) else {
      return self.fail(with: dbZoneNullError, reject: reject)
    }
    do {
      try ObjC.close(agcCloudDB, dbZone: dbZone)
    } catch {
      return self.fail(with: error, reject: reject)
    }
    self.dbZoneList.removeValue(forKey: id)
    self.handle(resolve: resolve)
  }
  
  @objc func runTransaction(_ transactionsArray: [Any], withId id: String, resolver resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) {
    guard let dbZone = getZoneFromList(id: id) else {
      return self.fail(with: dbZoneNullError, reject: reject)
     }
    var instanceArray = [AGCCloudDBObject]()
    dbZone.runTransaction { [self] (function) -> Bool in
      guard let function = function else { return false }
      for case let transactions in transactionsArray {
        guard let transactions = transactions as? Dictionary<String, Any> else { return false }
          guard let operation = transactions["operation"] as? String else { return false }
          guard let className = transactions["className"] as? String else { return false }
          guard let array = transactions["objectArray"] as? [Any] else { return false }
          guard let clazz = classFromString(className: className) else {
            self.fail(with: cloudDBClassError, reject: reject)
            return false
          }
          for case let object in array {
            guard let object = object as? Dictionary<String, Any> else { return false }
            guard let instance = mapToObject(clazz: clazz, map: object) else {
              return false
            }
            instanceArray.append(instance)
          }
          switch operation {
          case "executeUpsert":
            function.executeUpsert(instanceArray)
            break;
          case "executeDelete":
            function.executeDelete(instanceArray)
            break;
          default:
            break;
          }
      }
      return true
    }onCompleted: { (error) in
      if error != nil {
        self.fail(with: error, reject: reject)
        return
      } else {
        self.handle(resolve: resolve, true)
      }
    }

  }
  
  @objc func executeUpsert(_ className: String, objectArray array: [Any], withId id: String, resolver resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock){
    guard let dbZone = getZoneFromList(id: id) else {
      return self.fail(with: dbZoneNullError, reject: reject)
     }
    guard let clazz = classFromString(className: className) else {
      return self.fail(with: cloudDBClassError, reject: reject)
    }
    var instanceArray = [AGCCloudDBObject]()
    for case let object in array {
      guard let object = object as? Dictionary<String, Any> else { return self.fail(with: "Casting error.", reject: reject) }
      guard let instance = mapToObject(clazz: clazz, map: object) else { return self.fail(with: "Upsert object failed", reject: reject) }
      instanceArray.append(instance)
    }
    viewModel.executeUpsert(dbZone: dbZone, instanceArray: instanceArray) { [weak self] (result, error) in
      guard let strongSelf = self else { return }
      if let error = error as NSError? {
        strongSelf.fail(with: error, reject: reject)
      }
      if let result = result as? Int {
        strongSelf.handle(resolve: resolve, result)
      }
    }
    
  }
  
  @objc func executeDelete(_ className: String, objectArray array: [Any], withId id: String, resolver resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock){
    guard let dbZone = getZoneFromList(id: id) else {
      return self.fail(with: dbZoneNullError, reject: reject)
     }
    guard let clazz = classFromString(className: className) else {
      return self.fail(with: cloudDBClassError, reject: reject)
    }
    var instanceArray = [AGCCloudDBObject]()
    for case let object in array {
      guard let object = object as? Dictionary<String, Any> else { return self.fail(with: "Casting error.", reject: reject) }
      if let instance = mapToObject(clazz: clazz, map: object) {
        instanceArray.append(instance)
      }
    }
    viewModel.executeDelete(dbZone: dbZone, instanceArray: instanceArray) { [weak self] (result, error) in
      guard let strongSelf = self else { return }
      if let error = error as NSError? {
        strongSelf.fail(with: error, reject: reject)
      }
      if let result = result as? Int {
        strongSelf.handle(resolve: resolve, result)
      }
    }
  }
  
  @objc func executeQuery(_ map: Dictionary<String, Any>, policy queryPolicy: Int, withId id: String, resolver resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) {
    guard let dbZone = getZoneFromList(id: id) else {
      return self.fail(with: dbZoneNullError, reject: reject)
     }
    guard let query = queryBuilder(map: map, reject: reject) as AGCCloudDBQuery? else { return }
    guard let policy = getQueryPolicy(policyValue: queryPolicy, rejecter: reject) else { return }
    viewModel.executeQuery(dbZone: dbZone, query: query, queryPolicy: policy) { [weak self] (snapshot, error) in
      guard let strongSelf = self else { return }
      if let error = error as NSError? {
        strongSelf.fail(with: error, reject: reject)
      }
      if let snapshot = snapshot as? AGCCloudDBSnapshot {
        var dictionary = Dictionary<String, Any?>()
        dictionary["isFromCloud"] = snapshot.isFromCloud()
        dictionary["hasPendingWrites"] = snapshot.hasPendingWrites()
        if let snapshotObjects = self?.resolveSnapshot(array: snapshot.snapshotObjects()) {
          dictionary["snapshotObjects"] = snapshotObjects
        }
        if let deletedObjects = self?.resolveSnapshot(array: snapshot.deletedObjects()) {
          dictionary["deletedObjects"] = deletedObjects
        }
        if let upsertedObjects = self?.resolveSnapshot(array: snapshot.upsertedObjects()) {
          dictionary["upsertedObjects"] = upsertedObjects
        }
        strongSelf.handle(resolve: resolve, dictionary)
      }
    }
  }
  
  @objc func executeAverageQuery(_ map: Dictionary<String, Any>, withFieldName fieldName: String, policy queryPolicy: Int, withId id: String, resolver resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) {
    guard let dbZone = getZoneFromList(id: id) else {
      return self.fail(with: dbZoneNullError, reject: reject)
     }
    guard let query = queryBuilder(map: map, reject:  reject) as AGCCloudDBQuery? else { return }
    guard let policy = getQueryPolicy(policyValue: queryPolicy, rejecter: reject) else { return }
    viewModel.executeQueryAverage(dbZone: dbZone, query: query, queryPolicy: policy, fieldName: fieldName) { [weak self] (result, error) in
      guard let strongSelf = self else { return }
      if let error = error as NSError? {
        strongSelf.fail(with: error, reject: reject)
      }
      if let result = result {
        strongSelf.handle(resolve: resolve, result)
      }
    }
  }
  
  @objc func executeSumQuery(_ map: Dictionary<String, Any>, withFieldName fieldName: String, policy queryPolicy: Int, withId id: String, resolver resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) {
    guard let dbZone = getZoneFromList(id: id) else {
      return self.fail(with: dbZoneNullError, reject: reject)
     }
    guard let query = queryBuilder(map: map, reject: reject) as AGCCloudDBQuery? else { return }
    guard let policy = getQueryPolicy(policyValue: queryPolicy, rejecter: reject) else { return }
    viewModel.executeSumQuery(dbZone: dbZone, query: query, queryPolicy: policy, fieldName: fieldName) { [weak self] (result, error) in
      guard let strongSelf = self else { return }
      if let error = error as NSError? {
        strongSelf.fail(with: error, reject: reject)
      }
      if let result = result {
        strongSelf.handle(resolve: resolve, result)
      }
    }
  }
  
  @objc func executeMaximumQuery(_ map: Dictionary<String, Any>, withFieldName fieldName: String, policy queryPolicy: Int, withId id: String, resolver resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) {
    guard let dbZone = getZoneFromList(id: id) else {
      return self.fail(with: dbZoneNullError, reject: reject)
     }
    guard let query = queryBuilder(map: map, reject: reject) as AGCCloudDBQuery? else { return }
    guard let policy = getQueryPolicy(policyValue: queryPolicy, rejecter: reject) else { return }
    viewModel.executeMaximumQuery(dbZone: dbZone, query: query, queryPolicy: policy, fieldName: fieldName) { [weak self] (result, error) in
      guard let strongSelf = self else { return }
      if let error = error as NSError? {
        strongSelf.fail(with: error, reject: reject)
      }
      if let result = result {
        strongSelf.handle(resolve: resolve, result)
      }
    }
  }
  
  @objc func executeMinimalQuery(_ map: Dictionary<String, Any>, withFieldName fieldName: String, policy queryPolicy: Int, withId id: String, resolver resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) {
    guard let dbZone = getZoneFromList(id: id) else {
      return self.fail(with: dbZoneNullError, reject: reject)
     }
    guard let query = queryBuilder(map: map, reject: reject) as AGCCloudDBQuery? else { return }
    guard let policy = getQueryPolicy(policyValue: queryPolicy, rejecter: reject) else { return }
    viewModel.executeMinimalQuery(dbZone: dbZone, query: query, queryPolicy: policy, fieldName: fieldName) { [weak self] (result, error) in
      guard let strongSelf = self else { return }
      if let error = error as NSError? {
        strongSelf.fail(with: error, reject: reject)
      }
      if let result = result {
        strongSelf.handle(resolve: resolve, result)
      }
    }
  }
  
  @objc func executeCountQuery(_ map: Dictionary<String, Any>, withFieldName fieldName: String, policy queryPolicy: Int, withId id: String, resolver resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) {
    guard let dbZone = getZoneFromList(id: id) else {
      return self.fail(with: dbZoneNullError, reject: reject)
     }
    guard let query = queryBuilder(map: map, reject: reject) as AGCCloudDBQuery? else { return }
    guard let policy = getQueryPolicy(policyValue: queryPolicy, rejecter: reject) else { return }
    viewModel.executeCountQuery(dbZone: dbZone, query: query, queryPolicy: policy, fieldName: fieldName) { [weak self] (result, error) in
      guard let strongSelf = self else { return }
      if let error = error as NSError? {
        strongSelf.fail(with: error, reject: reject)
      }
      if let result = result {
        strongSelf.handle(resolve: resolve, result)
      }
    }
  }
    
    @objc func executeServerStatusQuery(_ id: String, resolver resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) {
      guard let dbZone = getZoneFromList(id: id) else {
        return self.fail(with: dbZoneNullError, reject: reject)
       }
        
      viewModel.executeServerStatusQuery(dbZone: dbZone) { [weak self] (result, error) in
        guard let strongSelf = self else { return }
        if let error = error as NSError? {
          strongSelf.fail(with: error, reject: reject)
        }
          if let result = result as? AGCServerStatus  {
              var dictionary = Dictionary<String, Any?>()
              dictionary["serverTimestamp"] = result.serverTimestamp
          strongSelf.handle(resolve: resolve, dictionary)
        }
      }
    }
  
  @objc func executeQueryUnsynced(_ map: Dictionary<String, Any>, withId id: String, resolver resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) {
    guard let dbZone = getZoneFromList(id: id) else {
      return self.fail(with: dbZoneNullError, reject: reject)
     }
    guard let query = queryBuilder(map: map, reject: reject) as AGCCloudDBQuery? else { return }
    viewModel.executeQueryUnsynced(dbZone: dbZone, query: query) { [weak self] (snapshot, error) in
      guard let strongSelf = self else { return }
      if let error = error as NSError? {
        strongSelf.fail(with: error, reject: reject)
      }
      if let snapshot = snapshot as? AGCCloudDBSnapshot {
        var dictionary = Dictionary<String, Any?>()
        dictionary["isFromCloud"] = snapshot.isFromCloud()
        dictionary["hasPendingWrites"] = snapshot.hasPendingWrites()
        if let snapshotObjects = self?.resolveSnapshot(array: snapshot.snapshotObjects()) {
          dictionary["snapshotObjects"] = snapshotObjects
        }
        if let deletedObjects = self?.resolveSnapshot(array: snapshot.deletedObjects()) {
          dictionary["deletedObjects"] = deletedObjects
        }
        if let upsertedObjects = self?.resolveSnapshot(array: snapshot.upsertedObjects()) {
          dictionary["upsertedObjects"] = upsertedObjects
        }
        strongSelf.handle(resolve: resolve, dictionary)
      }
    }
  }
  
  @objc func removeSubscription(_ id: String, withListenerId listenerId: String, resolver resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) {
    if let zoneListeners = listenerHandlerList[id] {
      if let listenerHandler = zoneListeners[listenerId] {
        listenerHandler.remove()
        zoneListeners.removeValue(forKey: listenerId)
        if zoneListeners.isEmpty {
          listenerHandlerList.removeValue(forKey: id)
        } else {
          listenerHandlerList[id] = zoneListeners
        }
        self.handle(resolve: resolve)
      } else {
        return self.fail(with: "Subscription could not be found.", reject: reject)
      }
    } else {
      return self.fail(with: "Subscription could not be found.", reject: reject)
    }
  }
  
  @objc func subscribeSnapshot(_ map: Dictionary<String, Any>, policy queryPolicy: Int, withId id: String, withListenerId listenerId: String, resolver resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) {
    guard let dbZone = getZoneFromList(id: id) else {
      return self.fail(with: dbZoneNullError, reject: reject)
     }
    guard let query = queryBuilder(map: map, reject: reject) as AGCCloudDBQuery? else { return }
    guard let policy = getQueryPolicy(policyValue: queryPolicy, rejecter: reject) else { return }
    let mRegister = viewModel.subscribeSnapshot(dbZone: dbZone, query: query, queryPolicy: policy) { [weak self] (snapshot, error) in
      guard let strongSelf = self else { return }
      var eventData = Dictionary<String, Any?>()
      eventData["id"] = listenerId
      if let error = error as NSError? {
        var errorData = Dictionary<String, Any?>()
        errorData["code"] = error.code
        errorData["message"] = error.description
        eventData["error"] = errorData
        return strongSelf.sendEvent(withName: "onSnapshotUpdate", body: eventData)
      }
      if let snapshot = snapshot as? AGCCloudDBSnapshot {
        var dictionary = Dictionary<String, Any?>()
        dictionary["isFromCloud"] = snapshot.isFromCloud()
        dictionary["hasPendingWrites"] = snapshot.hasPendingWrites()
        if let snapshotObjects = strongSelf.resolveSnapshot(array: snapshot.snapshotObjects()) {
          dictionary["snapshotObjects"] = snapshotObjects
        }
        if let deletedObjects = strongSelf.resolveSnapshot(array: snapshot.deletedObjects()) {
          dictionary["deletedObjects"] = deletedObjects
        }
        if let upsertedObjects = strongSelf.resolveSnapshot(array: snapshot.upsertedObjects()) {
          dictionary["upsertedObjects"] = upsertedObjects
        }
        eventData["data"] = dictionary
        strongSelf.sendEvent(withName: "onSnapshotUpdate", body: eventData)
      }
    }
    if let mRegister = mRegister as AGCCloudDBListenerHandler? {
      if let zoneListeners = self.listenerHandlerList[id] {
        zoneListeners[listenerId] = mRegister
        self.listenerHandlerList[id] = zoneListeners
      } else {
        let listenerDic: ConcurrentHashMap<String, AGCCloudDBListenerHandler> = ConcurrentHashMap.init(dict: [listenerId: mRegister])
        self.listenerHandlerList[id] = listenerDic
      }
      self.handle(resolve: resolve)
    } else {
      return self.fail(with: "subscribeSnapshot failed", reject: reject)
    }
  }
  
  // MARK: Private Helper Functions
  
  private func getZoneFromList(id: String) -> AGCCloudDBZone? {
    return self.dbZoneList[id]
  }
  
  private func getQueryPolicy(policyValue: Int, rejecter reject: @escaping RCTPromiseRejectBlock) -> AGCCloudDBQueryPolicy? {
    if policyValue == AGCCloudDBQueryPolicy.default.rawValue {
      return AGCCloudDBQueryPolicy.default
    } else if policyValue == AGCCloudDBQueryPolicy.cloud.rawValue {
      return AGCCloudDBQueryPolicy.cloud
    } else if policyValue == AGCCloudDBQueryPolicy.local.rawValue {
      return AGCCloudDBQueryPolicy.local
    } else {
      let error = NSError(domain: "Query from the local or cloud database failed.", code: 5, userInfo: nil)
      self.fail(with: error, reject: reject)
      return nil
    }
  }
  
  private func resolveSnapshot(array: [Any]) -> [Dictionary<String, Any?>]? {
      var dicArray: [Dictionary<String, Any?>] = [];
      for element in array {
        guard let object = element as? AGCCloudDBObject else { return nil }
        let dic = self.convertObjectToDic(object: object)
        dicArray.append(dic)
      }
      return dicArray
  }
  
  private func makeQuery(queryElements: [Dictionary<String, Any>], query: AGCCloudDBQuery, types: Dictionary<String, String>, clazz: AGCCloudDBObject.Type, className: String) {
    for case let queryElement in queryElements {
      guard let operation = queryElement["operation"] as? String else { break }
      if let fieldName = queryElement["fieldName"] as? String {
        if let value = queryElement["value"] {
          let value = setTypeOfValue(value: value, fieldName: fieldName, types: types)
          self.queryAdd(value: value, fieldName: fieldName, operation: operation, query: query, types: types)
        } else {
          queryAdd(onlyFieldName: fieldName, operation: operation, query: query)
        }
      } else {
        if operation == "limit" {
          if let value = queryElement["value"] as? Double {
            if let offset = queryElement["offset"] as? Double {
              query.limit(Int32.init(value), offset: Int32.init(offset))
            } else {
              query.limit(Int32.init(value))
            }
          }
        }
        if let value = queryElement["value"] {
          queryAdd(onlyValue: value, operation: operation, query: query, clazz: clazz)
        }
      }
    }
  }
  
  private func queryBuilder(map: Dictionary<String, Any>, reject: @escaping RCTPromiseRejectBlock ) -> AGCCloudDBQuery? {
    guard let className = map["className"] as? String else {
      self.fail(with: "AGCCloudDBQuery object failure", reject: reject)
      return nil
    }
    guard let clazz = classFromString(className: className) else {
      self.fail(with: cloudDBClassError, reject: reject)
      return nil
    }
    let types = getPropsWithTypes(object: clazz)
    let query = AGCCloudDBQuery.where(clazz)
    guard let queryElements = map["queryElements"] as? [Dictionary<String, Any>] else {
      self.fail(with: "AGCCloudDBQuery object failure", reject: reject)
      return nil
    }
    do {
        try ObjC.catchException {
          self.makeQuery(queryElements: queryElements, query: query, types: types, clazz: clazz, className: className)
        }
    }
    catch {
      if let error = error as NSError? {
        self.fail(with: error, reject: reject)
        return nil
      }
    }
    return query
  }
  
  private func setTypeOfValue(value: Any, fieldName: String, types: Dictionary<String, String>) -> Any {
    if types[fieldName] == "AGCCloudDBText" {
      if let strValue = value as? String {
        let text = AGCCloudDBText.createText(strValue)
        return text
      }
    } else if types[fieldName] == "NSDate" {
      if let timeInterval = value as? Double {
        let date = NSDate(timeIntervalSince1970: timeInterval)
        return date
      }
    } else if types[fieldName] == "NSData" {
      if let str = value as? [UInt8] {
        let nsData = NSData(bytes: str, length: str.count)
        return nsData
      }
    } else if types[fieldName] == "NSNumber<AGCLong>" {
      if let str = value as? String {
        if let number = Int(str) {
          return NSNumber(value: number)
        }
      }
    }
    return value
  }
  
  private func queryAdd(onlyFieldName fieldName: String, operation: String, query: AGCCloudDBQuery) {
    switch operation {
    case "isNull":
      query.isNull(fieldName)
    case "isNotNull":
      query.isNotNull(fieldName)
    case "orderByAsc":
      query.order(byAsc: fieldName)
    case "orderByDesc":
      query.order(byDesc: fieldName)
    default:
      break;
    }
  }
  
  private func queryAdd(onlyValue value: Any, operation: String, query: AGCCloudDBQuery, clazz: AGCCloudDBObject.Type) {
    guard let value = value as? Dictionary<String, Any> else { return }
    guard let object = mapToObject(clazz: clazz, map: value) as AGCCloudDBObject? else { return }
    switch operation {
    case "startAt":
      query.start(at: object)
    case "startAfter":
      query.start(after: object)
    case "endAt":
      query.end(at: object)
    case "endBefore":
      query.end(before: object)
    default:
      break;
    }
  }
  
  private func queryAdd(value: Any, fieldName: String, operation: String, query: AGCCloudDBQuery, types: Dictionary<String, String>){
    switch operation {
    case "equalTo":
      query.equal(to: value, forField: fieldName)
    case "notEqualTo":
      query.notEqual(to: value, forField: fieldName)
    case "greaterThan":
      query.greaterThan(value, forField: fieldName)
    case "greaterThanOrEqualTo":
      query.greaterThanOrEqual(to: value, forField: fieldName)
    case "lessThan":
      query.lessThan(value, forField: fieldName)
    case "lessThanOrEqualTo":
      query.lessThanOrEqual(to: value, forField: fieldName)
    case "_in":
      if let value = handleArrayTypes(value: value, fieldName: fieldName, query: query, types: types) {
        query.inArray(value, forField: fieldName)
      }
    case "beginsWith":
      query.begins(with: value, forField: fieldName)
    case "endsWith":
      query.ends(with: value, forField: fieldName)
    case "contains":
      query.contains(value, forField: fieldName)
    default:
      break;
    }
  }
  
  private func handleArrayTypes(value: Any, fieldName: String, query: AGCCloudDBQuery, types: Dictionary<String, String>) -> [Any]?{
    if let array = value as? [Any] {
      var typedValues: [Any] = []
      for item in array {
        let typedValue = setTypeOfValue(value: item, fieldName: fieldName, types: types)
        typedValues.append(typedValue)
      }
      return typedValues
    }
    return nil
  }
  
  private func mapToObject(clazz: AGCCloudDBObject.Type, map: Dictionary<String, Any>) -> AGCCloudDBObject? {
    let instance = clazz.init()
    let types = getPropsWithTypes(object: clazz)
    for case let (key,value) in map {
      if ((types[key]?.isEmpty) == nil) {
        break;
      }
      let value = setTypeOfValue(value: value, fieldName: key, types: types)
      instance.setValue(value, forKey: key)
    }
    return instance
  }
  
  private func handleConfig(map: Dictionary<String, Any>) -> AGCCloudDBZoneConfig? {
    guard let cloudDBZoneName = map["cloudDBZoneName"] as? String else { return nil }
    guard let syncRaw = map["syncProperty"] as? Int else { return nil }
    guard let syncProperty = AGCCloudDBZoneSyncMode.init(rawValue: syncRaw) else { return nil };
    guard let accessRaw = map["accessProperty"] as? Int else { return nil }
    guard let accessProperty = AGCCloudDBZoneAccessMode.init(rawValue: accessRaw) else { return nil };
    let zoneConfig = AGCCloudDBZoneConfig.init(zoneName: cloudDBZoneName, syncMode: syncProperty, accessMode: accessProperty)
    guard let persistence = map["persistenceEnabled"] as? Bool else { return nil }
    zoneConfig.persistence = persistence
    if (persistence) {
      guard let capacity = map["capacity"] as? Int else { return nil }
      zoneConfig.capacity = capacity
    }
    if (map["isEncrypted"] as? Bool == true) {
      guard let key = map["key"] as? String else { return nil }
      guard let reKey = map["reKey"] as? String else { return nil }
      zoneConfig.setEncryptKey(key, rekey: reKey)
    }
    return zoneConfig
  }
  
  private func classFromString(className: String) -> AGCCloudDBObject.Type? {
    let cls = NSClassFromString(className) as? AGCCloudDBObject.Type
    return cls
    
  }
  
  private func convertObjectToDic(object: AGCCloudDBObject) -> [String: Any?] {
    var dic: [String: Any?] = [:]
    let types = getPropsWithTypes(object: object.classForCoder)
    for type in types {
      if type.value == "AGCCloudDBText" {
        if let cloudDBText = object.value(forKey: type.key) as? AGCCloudDBText {
          dic[type.key] = cloudDBText.text
        }
      } else if type.value == "NSDate" {
        if let cloudDBDate = object.value(forKey: type.key) as? NSDate {
          dic[type.key] = cloudDBDate.timeIntervalSince1970
        }
      } else if type.value == "NSData" {
        if let cloudDBData = object.value(forKey: type.key) as? NSData {
          var array: [Double] = []
          for byte in cloudDBData {
            array.append(Double(byte))
          }
          dic[type.key] = array
        }
      } else if type.value == "NSNumber<AGCLong>" {
        if let numberValue = object.value(forKey: type.key) as? NSNumber {
          dic[type.key] = "\(numberValue)"
        }
      } else {
        dic[type.key] = object.value(forKey: type.key)
      }
    }
    return dic
  }
  
  
  private func getPropNames (object: AnyClass) -> [String] {
    var outCount : UInt32 = 0
    let properties = class_copyPropertyList(object.self, &outCount)
    var propertiesArray: [String] = []
    for i : UInt32 in 0..<outCount
    {
      let strKey : NSString = NSString(cString: property_getName(properties![Int(i)]), encoding: String.Encoding.utf8.rawValue)!
      propertiesArray.append(strKey as String)
    }
    return propertiesArray
  }
  
  private func getPropsWithTypes (object: AnyClass) -> Dictionary<String, String> {
    var outCount : UInt32 = 0
    let properties = class_copyPropertyList(object.self, &outCount)
    var propertiesArray: Dictionary<String, String> = [:]
    for i : UInt32 in 0..<outCount
    {
      let strKey : NSString = NSString(cString: property_getName(properties![Int(i)]), encoding: String.Encoding.utf8.rawValue)!
      let typeDef: String = NSString(cString: property_getAttributes(properties![Int(i)])!, encoding: String.Encoding.utf8.rawValue)! as String
      let typeName = typeDef.split(separator: "\"")
      propertiesArray[strKey as String] = String(typeName[1])
    }
    return propertiesArray
  }
  
  /// *requiresMainQueueSetup* must be implemented to use constantsToExport or have implemented an init() method for UIKit components in React Native v0.49+.
  /// - Returns: a Boolean: **true** if the class needed to be initialized on the main thread, **false** if the class can be initialized on a background thread.
  @objc override static func requiresMainQueueSetup() -> Bool {
    return true
  }
  
  @objc override func constantsToExport() -> [AnyHashable : Any]! {
    let CloudDBZoneSyncProperty = [
      "CLOUDDBZONE_CLOUD_CACHE": AGCCloudDBZoneSyncMode.cloudCache.rawValue,
      "CLOUDDBZONE_LOCAL_ONLY": AGCCloudDBZoneSyncMode.localOnly.rawValue
    ]

    let CloudDBZoneAccessProperty = [
      "CLOUDDBZONE_PUBLIC": AGCCloudDBZoneAccessMode.public.rawValue
    ]

    let CloudDBZoneQueryPolicy = [
      "POLICY_QUERY_DEFAULT": AGCCloudDBQueryPolicy.default.rawValue,
      "POLICY_QUERY_FROM_CLOUD_ONLY": AGCCloudDBQueryPolicy.cloud.rawValue,
      "POLICY_QUERY_FROM_LOCAL_ONLY": AGCCloudDBQueryPolicy.local.rawValue
    ]
    
    let CloudDBEventType = [
      "USER_KEY_CHANGED": AGCCloudDBEventType.userKeyChanged.rawValue
    ]

    return [
      "CloudDBZoneSyncProperty": CloudDBZoneSyncProperty,
      "CloudDBZoneAccessProperty": CloudDBZoneAccessProperty,
      "CloudDBZoneQueryPolicy": CloudDBZoneQueryPolicy,
      "CloudDBEventType": CloudDBEventType
    ]
  }
  
  override func supportedEvents() -> [String]! {
    return ["onEvent", "onDataKeyChange","onSnapshotUpdate"]
  }
}

