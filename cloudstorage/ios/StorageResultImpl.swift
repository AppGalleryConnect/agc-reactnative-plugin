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

@objc(StorageResultImpl)

class StorageResultImpl :  NSObject, AGCStorageHanding{
    
    private let UPLOAD_RESULT = "UploadResult"
    private let errorMsg2 = "Not found parameter value."
    
    @objc func getMetadata(_ className: String , storageResultId: String, resolver resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) {
        do {
            try ObjC.catchException {
                if(self.UPLOAD_RESULT == className) {
                    let fileMetadataId = ConcurrentHashMap<String, AGCStorageMetadata>.getUUID(set: AGCCloudStorageMaps.FILE_METADATA_MAP)
                    guard let fileMetadata = AGCCloudStorageMaps.UPLOAD_RESULT_MAP[storageResultId]?.metadata else {
                        self.fail(with: self.errorMsg2, reject: reject)
                        return
                    }
                    AGCCloudStorageMaps.FILE_METADATA_MAP[fileMetadataId] = fileMetadata
                    self.handle(resolve: resolve,  fileMetadataId)
                } else {
                    self.fail(with: "Not Found Task -> Upload Result", reject: reject)
                    return
                }
            }
        } catch {
            return self.fail(with: error, reject: reject)
        }
    }
    
    @objc static func requiresMainQueueSetup() -> Bool {
        return true
    }
}
