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

package com.huawei.agc.rn.clouddb;

import com.huawei.agconnect.AGConnectInstance;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;

import java.util.Map;

import javax.annotation.Nonnull;

/**
 * AGCCloudFunctionsModule class is the tool class of AGConnectFunction.
 *
 * @since v.1.4.2.301
 */
public class AGCCloudDBModule extends ReactContextBaseJavaModule {
    private final CloudDBService cloudDBService;

    public AGCCloudDBModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.cloudDBService = new CloudDBService(reactContext);
        if (AGConnectInstance.getInstance() == null) {
            AGConnectInstance.initialize(reactContext);
        }
    }

    @Nonnull
    @Override
    public String getName() {
        return "AGCCloudDBModule";
    }

    @Override
    public Map<String, Object> getConstants() {
        return AGCCloudDBConstants.getConstants();
    }

    @ReactMethod
    public void initialize() {
        cloudDBService.initialize();
    }

    @ReactMethod
    public void createObjectType(Promise promise) {
        cloudDBService.createObjectType(promise);
    }

    @ReactMethod
    public void getCloudDBZoneConfigs(Promise promise) {
        cloudDBService.getCloudDBZoneConfigs(promise);
    }

    @ReactMethod
    public void openCloudDBZone2(ReadableMap config, Boolean isAllowToCreate, String id, Promise promise) {
        cloudDBService.openCloudDBZone2(config, isAllowToCreate, id, promise);
    }

    @Deprecated
    @ReactMethod
    public void openCloudDBZone(ReadableMap config, Boolean isAllowToCreate, String id, Promise promise) {
        cloudDBService.openCloudDBZone(config, isAllowToCreate, id, promise);
    }

    @ReactMethod
    public void addEventListener(Promise promise) {
        cloudDBService.addEventListener(promise);
    }

    @ReactMethod
    public void addDataEncryptionKeyListener(Promise promise) {
        cloudDBService.addDataEncryptionKeyListener(promise);
    }

    @ReactMethod
    public void subscribeSnapshot(ReadableMap map, int queryPolicy, String id, String listenerId, Promise promise) {
        cloudDBService.subscribeSnapshot(map, queryPolicy, id, listenerId, promise);
    }

    @ReactMethod
    public void removeSubscription(String id, String listenerId, Promise promise) {
        cloudDBService.removeSubscription(id, listenerId, promise);
    }

    @ReactMethod
    public void closeCloudDBZone(String id, Promise promise) {
        cloudDBService.closeCloudDBZone(id, promise);
    }

    @ReactMethod
    public void deleteCloudDBZone(String id, Promise promise) {
        cloudDBService.deleteCloudDBZone(id, promise);
    }

    @ReactMethod
    public void enableNetwork(String zoneName, Promise promise) {
        cloudDBService.enableNetwork(zoneName, promise);
    }

    @ReactMethod
    public void disableNetwork(String zoneName, Promise promise) {
        cloudDBService.disableNetwork(zoneName, promise);
    }

    @ReactMethod
    public void updateDataEncryptionKey(Promise promise) {
        cloudDBService.updateDataEncryptionKey(promise);
    }
    
    @ReactMethod
    public void setUserKey(String userKey, String userReKey, boolean needStrongCheck, Promise promise) {
        cloudDBService.setUserKey(userKey, userReKey,needStrongCheck, promise);
    }

    @ReactMethod
    public void runTransaction(ReadableArray transactionArray, String id, Promise promise) {
        cloudDBService.runTransaction(transactionArray, id, promise);
    }

    @ReactMethod
    public void executeUpsert(String className, ReadableArray objectArray, String id, Promise promise) {
        cloudDBService.executeUpsert(className, objectArray, id, promise);
    }

    @ReactMethod
    public void executeQuery(ReadableMap map, int queryPolicy, String id, Promise promise) {
        cloudDBService.executeQuery(map, queryPolicy, id, promise);
    }

    @ReactMethod
    public void executeAverageQuery(ReadableMap map, String fieldName, int queryPolicy, String id, Promise promise) {
        cloudDBService.executeAverageQuery(map, fieldName, queryPolicy, id, promise);
    }

    @ReactMethod
    public void executeSumQuery(ReadableMap map, String fieldName, int queryPolicy, String id, Promise promise) {
        cloudDBService.executeSumQuery(map, fieldName, queryPolicy, id, promise);
    }

    @ReactMethod
    public void executeMaximumQuery(ReadableMap map, String fieldName, int queryPolicy, String id, Promise promise) {
        cloudDBService.executeMaximumQuery(map, fieldName, queryPolicy, id, promise);
    }

    @ReactMethod
    public void executeMinimalQuery(ReadableMap map, String fieldName, int queryPolicy, String id, Promise promise) {
        cloudDBService.executeMinimalQuery(map, fieldName, queryPolicy, id, promise);
    }

    @ReactMethod
    public void executeCountQuery(ReadableMap map, String fieldName, int queryPolicy, String id, Promise promise) {
        cloudDBService.executeCountQuery(map, fieldName, queryPolicy, id, promise);
    }

    @ReactMethod
    public void executeQueryUnsynced(ReadableMap map, String id, Promise promise) {
        cloudDBService.executeQueryUnsynced(map, id, promise);
    }

    @ReactMethod
    public void getCloudDBZoneConfig(String id, Promise promise) {
        cloudDBService.getCloudDBZoneConfig(id, promise);
    }

    @ReactMethod
    public void executeDelete(String className, ReadableArray objectArray, String id, Promise promise) {
        cloudDBService.executeDelete(className, objectArray, id, promise);
    }

    @ReactMethod
    public void executeServerStatusQuery(String id,Promise promise) {
        cloudDBService.executeServerStatusQuery(id,promise);
    }

    @ReactMethod
    public void addListener(String eventName) {
    }

    @ReactMethod
    public void removeListeners(Integer count) {
    }

}
