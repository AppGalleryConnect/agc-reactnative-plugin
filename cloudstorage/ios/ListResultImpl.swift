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
import AGConnectStorage

@objc(ListResultImpl)

class ListResultImpl:  NSObject, AGCStorageHanding {
    
    private let errorMsg = "Not found ListResultImpl instance."
    private let errorMsg2 = "Not found parameter value."
    
    @objc func getFileList(_ listResultId: String , resolver resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) {
        do {
            try ObjC.catchException {
                guard let listResult = AGCCloudStorageMaps.LIST_RESULT_MAP[listResultId] else {
                    self.fail(with: self.errorMsg, reject: reject)
                    return
                }
                
                let storageManagementId = ConcurrentHashMap<String, AGCStorage>.getUUID(set: AGCCloudStorageMaps.STORAGE_MANAGEMENT_MAP)
                let fileList = listResult.fileList
                AGCCloudStorageMaps.STORAGE_MANAGEMENT_MAP[storageManagementId] = fileList.first?.storage
                var storageReferenceIdList = [String]()
                for mFile in fileList {
                    let storageReferencetId = ConcurrentHashMap<String, AGCStorageReference>.getUUID(set: AGCCloudStorageMaps.STORAGE_REFERENCE_MAP)
                    AGCCloudStorageMaps.STORAGE_REFERENCE_MAP[storageReferencetId] = mFile
                    storageReferenceIdList.append(storageReferencetId)
                }
                var mArray = [String: Any]()
                mArray["storageManagementId"] = storageManagementId
                mArray["storageReferenceIdArray"] = storageReferenceIdList
                self.handle(resolve:resolve, mArray)
            }
        } catch {
            return self.fail(with: error, reject: reject)
        }
    }
    
    @objc func getDirList(_ listResultId: String, resolver resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock)  {
        do {
            try ObjC.catchException {
                guard let listResult = AGCCloudStorageMaps.LIST_RESULT_MAP[listResultId] else {
                    self.fail(with: self.errorMsg, reject: reject)
                    return
                }
                
                let dirList = listResult.dirList
                let storageManagementId = ConcurrentHashMap<String, AGCStorage>.getUUID(set: AGCCloudStorageMaps.STORAGE_MANAGEMENT_MAP)
                AGCCloudStorageMaps.STORAGE_MANAGEMENT_MAP[storageManagementId] = dirList.first?.storage
                var storageReferenceIdList = [String]()
                for mDir in dirList {
                    let storageReferencetId = ConcurrentHashMap<String, AGCStorageReference>.getUUID(set: AGCCloudStorageMaps.STORAGE_REFERENCE_MAP)
                    AGCCloudStorageMaps.STORAGE_REFERENCE_MAP[storageReferencetId] = mDir
                    storageReferenceIdList.append(storageReferencetId)
                }
                
                var mArray = [String: Any]()
                mArray["storageManagementId"] = storageManagementId
                mArray["storageReferenceIdArray"] = storageReferenceIdList
                self.handle(resolve:resolve, mArray)
            }
        } catch {
            return self.fail(with: error, reject: reject)
        }
    }
    
    @objc func getPageMarker(_ listResultId: String, resolver resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock)  {
        do {
            try ObjC.catchException {
                guard let listResult = AGCCloudStorageMaps.LIST_RESULT_MAP[listResultId] else {
                    self.fail(with: self.errorMsg, reject: reject)
                    return
                }
                
                let pageMarker = listResult.pageMarker
                self.handle(resolve:resolve, pageMarker)
            }
        } catch {
            return self.fail(with: error, reject: reject)
        }
    }
    
    @objc static func requiresMainQueueSetup() -> Bool {
        return true
    }
}

