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

import { NativeModules } from 'react-native';
const {AGCCloudDBModule} = NativeModules;

export default class AGCCloudDBZoneConfig {
    static CloudDBZoneSyncProperty = {
        CLOUDDBZONE_CLOUD_CACHE: AGCCloudDBModule.CloudDBZoneSyncProperty.CLOUDDBZONE_CLOUD_CACHE,
        CLOUDDBZONE_LOCAL_ONLY: AGCCloudDBModule.CloudDBZoneSyncProperty.CLOUDDBZONE_LOCAL_ONLY,
    }
    static CloudDBZoneAccessProperty = {
        CLOUDDBZONE_PUBLIC: AGCCloudDBModule.CloudDBZoneAccessProperty.CLOUDDBZONE_PUBLIC,
    }

    constructor(cloudDBZoneName, cloudDBZoneSyncProperty, cloudDBZoneAccessProperty) {
        this.cloudDBZoneName = cloudDBZoneName;
        this.accessProperty = cloudDBZoneAccessProperty;
        this.syncProperty = cloudDBZoneSyncProperty;
        this.persistenceEnabled = true;
        this.capacity = "104857600";
        this.isEncrypted = false;
        this.key = "";
        this.reKey = "";
    }

    getCloudDBZoneName() {
        return this.cloudDBZoneName;
    }

    getSyncProperty() {
        return this.syncProperty;
    }

    getAccessProperty() {
        return this.accessProperty;
    }

    isEncrypted() {
        return this.isEncrypted;
    }

    setEncryptedKey(key, reKey) {
        this.isEncrypted = true;
        this.key = key;
        this.reKey = reKey;
    }

    setPersistenceEnabled(enabled) {
        this.persistenceEnabled = enabled;
    }

    getPersistenceEnabled() {
        return this.persistenceEnabled;
    }

    setCapacity(capacity) {
        this.capacity = capacity
    }

    getCapacity() {
        return this.capacity
    }
}