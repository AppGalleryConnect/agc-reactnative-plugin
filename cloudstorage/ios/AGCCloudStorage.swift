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

@objc(AGCCloudStorage)

class AGCCloudStorage: NSObject, AGCStorageHanding {
    
    @objc func getInstance(_ map: Dictionary<String, Any>, resolver resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) {
        
        var storage: AGCStorage = AGCStorage.init()
        
        if (map.isEmpty) {
            storage = AGCStorage.getInstance()
        } else {
            let bucketName = map["bucketName"] as? String ?? ""
            let routePolicy = map["routePolicy"] as? Int ?? -1
            var instance = AGCInstance.init()
            if(routePolicy != -1) {
                let policy = AGCRoutePolicy(rawValue: UInt(routePolicy))
                let config = AGCServicesConfig.init(defaultPlist:())
                config.routePolicy = policy ?? AGCRoutePolicy.unknown
                instance = AGCInstance.getInstance(config)
            }
            if(bucketName != "" && routePolicy != -1) {
                storage = AGCStorage.getInstance(instance, bucketName: bucketName)
            } else if (routePolicy != -1){
                storage = AGCStorage.getInstance(instance)
            } else {
                storage = AGCStorage.getInstanceForBucketName(bucketName)
            }
        }
        
        let storageManagementId = ConcurrentHashMap<String, AGCStorage>.getUUID(set: AGCCloudStorageMaps.STORAGE_MANAGEMENT_MAP)
        AGCCloudStorageMaps.STORAGE_MANAGEMENT_MAP[storageManagementId] = storage
        self.handle(resolve: resolve, storageManagementId);
    }
    
    @objc static func requiresMainQueueSetup() -> Bool {
        return true
    }
}

