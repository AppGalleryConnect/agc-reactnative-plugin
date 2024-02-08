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

import AGCCloudDBException from '@hw-agconnect/react-native-clouddb/src/AGCCloudDBException';
import AGCCloudDBZone from './AGCCloudDBZone';
import { NativeEventEmitter, NativeModules } from 'react-native';
const { AGCCloudDBModule } = NativeModules;

export default class AGCCloudDB {
    
    constructor() {
        this.instance = AGCCloudDBModule.initialize();
    }

    static getInstance() {
        if (!this.instance) {
            return new AGCCloudDB()
        }
        return this.instance
    }

    closeCloudDBZone(cloudDBZone) {
        return AGCCloudDBModule.closeCloudDBZone(cloudDBZone.id);
    }

    createObjectType() {
        return AGCCloudDBModule.createObjectType();
    }

    getCloudDBZoneConfigs() {
        return AGCCloudDBModule.getCloudDBZoneConfigs();
    }

    openCloudDBZone(cloudDBZoneConfig, isAllowToCreate) {
        let id = Math.random().toString(36).substr(2, 9) + Date.now();
        return AGCCloudDBModule.openCloudDBZone(cloudDBZoneConfig, isAllowToCreate, id)
            .then(response => {
                return new AGCCloudDBZone(id);
            })
            .catch(error => {
                throw new AGCCloudDBException(error)
            })
    }

    openCloudDBZone2(cloudDBZoneConfig, isAllowToCreate) {
        let id = Math.random().toString(36).substr(2, 9) + Date.now();
        return AGCCloudDBModule.openCloudDBZone2(cloudDBZoneConfig, isAllowToCreate, id)
            .then(response => {
                return new AGCCloudDBZone(id);
            })
            .catch(error => {
                throw new AGCCloudDBException(error)
            })
    }

    deleteCloudDBZone(zoneName) {
        return AGCCloudDBModule.deleteCloudDBZone(zoneName);
    }

    enableNetwork(zoneName) {
        return AGCCloudDBModule.enableNetwork(zoneName);
    }

    disableNetwork(zoneName) {
        return AGCCloudDBModule.disableNetwork(zoneName);
    }

    setUserKey(userKey, userReKey, needStrongCheck) {
        return AGCCloudDBModule.setUserKey(userKey, userReKey,needStrongCheck);
    }

    updateDataEncryptionKey() {
        return AGCCloudDBModule.updateDataEncryptionKey();
    }

    addEventListener(listener) {
        let eventEmitter = new NativeEventEmitter(AGCCloudDBModule);
        let eventListener = eventEmitter.addListener("onEvent", listener)
        return AGCCloudDBModule.addEventListener();
    }

    addDataEncryptionKeyListener(listener) {
        let eventEmitter = new NativeEventEmitter(AGCCloudDBModule);
        let eventListener = eventEmitter.addListener("onDataKeyChange", listener)
        return AGCCloudDBModule.addDataEncryptionKeyListener();
    }
}