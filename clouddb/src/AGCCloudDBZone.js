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

import { NativeEventEmitter, NativeModules, Platform } from 'react-native';
import AGCCloudDBListenerHandler from './AGCCloudDBListenerHandler';
import AGCCloudDBException from './AGCCloudDBException';
const { AGCCloudDBModule } = NativeModules;

export default class AGCCloudDBZone {
    constructor(id){
        this.id = id
    }

    getCloudDBZoneConfig() {
        if (Platform.OS === "android") {
            return AGCCloudDBModule.getCloudDBZoneConfig(this.id);
        } else {
            throw new Error('`getCloudDBZoneConfig()` is not supported on IOS platform.');
        }
    }

    executeUpsert(className, object) {
        if (object instanceof Array) {
            return AGCCloudDBModule.executeUpsert(className, object, this.id);
        } else {
            let objectArray = [object]
            return AGCCloudDBModule.executeUpsert(className, objectArray, this.id);
        }   
    }

    executeDelete(className, object) {
        if (object instanceof Array) {
            return AGCCloudDBModule.executeDelete(className, object, this.id);
        } else {
            let objectArray = [object]
            return AGCCloudDBModule.executeDelete(className, objectArray, this.id);
        }
        
    }

    executeQuery(query, queryPolicy) {
        return AGCCloudDBModule.executeQuery(query.queryMap, queryPolicy, this.id);
    }

    executeAverageQuery(query, fieldName, queryPolicy) {
        return AGCCloudDBModule.executeAverageQuery(query.queryMap, fieldName, queryPolicy, this.id);
    }

    executeSumQuery(query, fieldName, queryPolicy) {
        return AGCCloudDBModule.executeSumQuery(query.queryMap, fieldName, queryPolicy, this.id);
    }

    executeMaximumQuery(query, fieldName, queryPolicy) {
        return AGCCloudDBModule.executeMaximumQuery(query.queryMap, fieldName, queryPolicy, this.id);
    }

    executeMinimalQuery(query, fieldName, queryPolicy) {
        return AGCCloudDBModule.executeMinimalQuery(query.queryMap, fieldName, queryPolicy, this.id);
    }

    executeCountQuery(query, fieldName, queryPolicy) {
        return AGCCloudDBModule.executeCountQuery(query.queryMap, fieldName, queryPolicy, this.id);
    }

    executeQueryUnsynced(query) {
        return AGCCloudDBModule.executeQueryUnsynced(query.queryMap, this.id);
    }

    executeServerStatusQuery() {
        return AGCCloudDBModule.executeServerStatusQuery(this.id);
    }
    subscribeSnapshot(query, queryPolicy, listener) {
        let eventEmitter = new NativeEventEmitter(AGCCloudDBModule);
        let listenerId = Math.random().toString(36).substr(2, 9) + Date.now();
        let callback = (res) => {
            if (res != null && res.id == listenerId) {
                if (res.data != null) {
                    listener(res.data)
                } else {
                    let error = new AGCCloudDBException(res.error)
                    listener(error)
                }
                
            }
        } 
        let eventListener =  eventEmitter.addListener("onSnapshotUpdate", callback)
        return AGCCloudDBModule.subscribeSnapshot(query.queryMap, queryPolicy, this.id, listenerId)
            .then(() => {
                return new AGCCloudDBListenerHandler(this.id, listenerId, eventListener)
            })
            .catch(error => {
                eventListener.remove()
                throw new AGCCloudDBException(error)
            })
    }

    runTransaction(transaction) {
        return AGCCloudDBModule.runTransaction(transaction.transactions, this.id);
    }
}