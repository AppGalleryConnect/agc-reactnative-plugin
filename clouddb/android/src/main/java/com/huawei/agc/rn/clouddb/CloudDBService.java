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

import androidx.annotation.NonNull;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.huawei.agc.rn.clouddb.utils.CloudDBTaskParameters;
import com.huawei.agc.rn.clouddb.utils.CloudDBUtils;
import com.huawei.agc.rn.clouddb.utils.ReactUtils;
import com.huawei.agconnect.AGConnectInstance;
import com.huawei.agconnect.auth.AGConnectAuth;
import com.huawei.agconnect.cloud.database.AGConnectCloudDB;
import com.huawei.agconnect.cloud.database.CloudDBZone;
import com.huawei.agconnect.cloud.database.CloudDBZoneConfig;
import com.huawei.agconnect.cloud.database.CloudDBZoneObject;
import com.huawei.agconnect.cloud.database.CloudDBZoneObjectList;
import com.huawei.agconnect.cloud.database.CloudDBZoneQuery;
import com.huawei.agconnect.cloud.database.CloudDBZoneSnapshot;
import com.huawei.agconnect.cloud.database.ListenerHandler;
import com.huawei.agconnect.cloud.database.ServerStatus;
import com.huawei.agconnect.cloud.database.Transaction;
import com.huawei.agconnect.cloud.database.exceptions.AGConnectCloudDBException;
import com.huawei.agc.rn.clouddb.model.ObjectTypeInfoHelper;
import com.huawei.hmf.tasks.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class CloudDBService {
    private final ReactApplicationContext reactApplicationContext;

    private final Map<String, CloudDBZone> zoneMap = new ConcurrentHashMap<>();

    private final AGConnectCloudDBException cloudDBIsNull = new AGConnectCloudDBException("CloudDB is null", 10);

    private final Map<String, Map<String, ListenerHandler>> listenerHandlerMap = new ConcurrentHashMap<>();

    private AGConnectCloudDB mCloudDB;

    public CloudDBService(ReactApplicationContext reactApplicationContext) {
        this.reactApplicationContext = reactApplicationContext;
        initAGConnectInstance(reactApplicationContext);
    }

    public void initialize() {
        if (AGConnectInstance.getInstance() == null) {
            AGConnectInstance.initialize(reactApplicationContext);
        }
        mCloudDB = AGConnectCloudDB.getInstance(AGConnectInstance.getInstance(), AGConnectAuth.getInstance());
    }

    public void createObjectType(Promise promise) {
        if (mCloudDB == null) {
            promise.reject(cloudDBIsNull);
            return;
        }
        try {
            ObjectTypeInfoHelper.getObjectTypeInfo();
            mCloudDB.createObjectType(ObjectTypeInfoHelper.getObjectTypeInfo());
            promise.resolve(true);
        } catch (Exception e) {
            promise.reject(e);
        }
    }

    public void openCloudDBZone2(ReadableMap config, Boolean isAllowToCreate, String id, Promise promise) {
        if (mCloudDB == null) {
            promise.reject(cloudDBIsNull);
            return;
        }
        CloudDBZoneConfig mConfig;
        try {
            mConfig = handleConfig(config,promise);
        } catch (AGConnectCloudDBException exception) {
            promise.reject(exception);
            return;
        }
        Task<CloudDBZone> openDBZoneTask = mCloudDB.openCloudDBZone2(mConfig, isAllowToCreate);
        openDBZoneTask.addOnSuccessListener(cloudDBZone -> {
            zoneMap.put(id, cloudDBZone);
            promise.resolve(true);
        }).addOnFailureListener(promise::reject);
    }

    public void getCloudDBZoneConfigs(Promise promise) {
        if (mCloudDB == null) {
            promise.reject(cloudDBIsNull);
            return;
        }
        try {
            List<CloudDBZoneConfig> configList = mCloudDB.getCloudDBZoneConfigs();
            WritableArray wa = Arguments.createArray();
            for (CloudDBZoneConfig config : configList) {
                WritableMap map = CloudDBUtils.configToWM(config);
                wa.pushMap(map);
            }
            promise.resolve(wa);
        } catch (AGConnectCloudDBException e) {
            promise.reject(String.valueOf(e.getCode()), e.getMessage());
        }
    }

    public void openCloudDBZone(ReadableMap config, Boolean isAllowToCreate, String id, Promise promise) {
        if (mCloudDB == null) {
            promise.reject(cloudDBIsNull);
            return;
        }

        try {
            CloudDBZoneConfig mConfig = handleConfig(config,promise);
            CloudDBZone cloudDBZone = mCloudDB.openCloudDBZone(mConfig, isAllowToCreate);
            zoneMap.put(id, cloudDBZone);
            promise.resolve(true);
        } catch (AGConnectCloudDBException e) {
            promise.reject(String.valueOf(e.getCode()), e.getMessage());
        }
    }

    public void deleteCloudDBZone(String zoneName, Promise promise) {
        if (mCloudDB == null) {
            promise.reject(cloudDBIsNull);
            return;
        }
        try {
            mCloudDB.deleteCloudDBZone(zoneName);
            promise.resolve(true);
        } catch (AGConnectCloudDBException e) {
            promise.reject(String.valueOf(e.getCode()), e.getMessage());
        }
    }

    public void enableNetwork(String zoneName, Promise promise) {
        if (mCloudDB == null) {
            promise.reject(cloudDBIsNull);
            return;
        }
        try {
            mCloudDB.enableNetwork(zoneName);
            promise.resolve(true);
        } catch (RuntimeException e) {
            promise.reject(e);
        }
    }

    public void disableNetwork(String zoneName, Promise promise) {
        if (mCloudDB == null) {
            promise.reject(cloudDBIsNull);
            return;
        }
        try {
            mCloudDB.disableNetwork(zoneName);
            promise.resolve(true);
        } catch (RuntimeException e) {
            promise.reject(e);
        }
    }

    public void setUserKey(String userKey, String userReKey, boolean needStrongCheck, Promise promise) {
        if (mCloudDB == null) {
            promise.reject(cloudDBIsNull);
            return;
        }
        mCloudDB.setUserKey(userKey, userReKey,needStrongCheck).addOnSuccessListener(promise::resolve).addOnFailureListener(e -> {
            if (e instanceof AGConnectCloudDBException) {
                promise.reject("" + ((AGConnectCloudDBException) e).getCode(), e.getMessage(), e);
            } else {
                promise.reject(e);
            }
        });
    }

    public void updateDataEncryptionKey(Promise promise) {
        if (mCloudDB == null) {
            promise.reject(cloudDBIsNull);
            return;
        }
        mCloudDB.updateDataEncryptionKey().addOnSuccessListener(promise::resolve).addOnFailureListener(promise::reject);
    }

    public void addEventListener(Promise promise) {
        if (mCloudDB == null) {
            promise.reject(cloudDBIsNull);
            return;
        }
        AGConnectCloudDB.EventListener eventListener = eventType -> reactApplicationContext.getJSModule(
                DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit("onEvent", eventType.ordinal());
        mCloudDB.addEventListener(eventListener);
        promise.resolve(true);
    }

    public void addDataEncryptionKeyListener(Promise promise) {
        if (mCloudDB == null) {
            promise.reject(cloudDBIsNull);
            return;
        }
        AGConnectCloudDB.OnDataEncryptionKeyChangeListener dataListener = () -> {
            reactApplicationContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                    .emit("onDataKeyChange", true);
            return true;
        };
        mCloudDB.addDataEncryptionKeyListener(dataListener);
        promise.resolve(true);
    }

    public void executeServerStatusQuery(String id, Promise promise) {
        try {
            CloudDBZone cloudDBZone = getZoneFromList(id);
            Task<ServerStatus> serverStatusQueryTask =cloudDBZone.executeServerStatusQuery();
            serverStatusQueryTask.addOnSuccessListener(serverStatus -> {
                WritableMap map = CloudDBUtils.serverStatusToWM(serverStatus);
                promise.resolve(map);
            }).addOnFailureListener(e -> {
                promise.reject(e);
            });
        } catch (Exception e) {
            promise.reject(e);
        }
    }

    public void getCloudDBZoneConfig(String id, Promise promise) {
        try {
            CloudDBZone cloudDBZone = getZoneFromList(id);
            WritableMap map = CloudDBUtils.configToWM(cloudDBZone.getCloudDBZoneConfig());
            promise.resolve(map);
        } catch (Exception e) {
            promise.reject(e);
        }
    }


    public void closeCloudDBZone(String id, Promise promise) {
        if (mCloudDB == null) {
            promise.reject(cloudDBIsNull);
            return;
        }
        try {
            CloudDBZone cloudDBZone = getZoneFromList(id);
            mCloudDB.closeCloudDBZone(cloudDBZone);
            zoneMap.remove(id);
            promise.resolve(true);
        } catch (Exception e) {
            promise.reject(e);
        }
    }

    public void executeDelete(String className, ReadableArray objectArray, String id, Promise promise) {
        try {
            CloudDBZone cloudDBZone = getZoneFromList(id);
            List<CloudDBZoneObject> list = objectArrayToList(className, objectArray);
            Task<Integer> deleteTask = cloudDBZone.executeDelete(list);
            deleteTask.addOnSuccessListener(promise::resolve).addOnFailureListener(promise::reject);
        } catch (IllegalAccessException | NoSuchFieldException | AGConnectCloudDBException | InstantiationException | IllegalArgumentException e) {
            promise.reject(e);
        }
    }

    public void executeUpsert(String className, ReadableArray objectArray, String id, Promise promise) {
        try {
            CloudDBZone cloudDBZone = getZoneFromList(id);
            List<CloudDBZoneObject> list = objectArrayToList(className, objectArray);
            Task<Integer> upsertTask = cloudDBZone.executeUpsert(list);
            upsertTask.addOnSuccessListener(promise::resolve).addOnFailureListener(promise::reject);
        } catch (IllegalAccessException | NoSuchFieldException | AGConnectCloudDBException | InstantiationException | IllegalArgumentException e) {
            promise.reject(e);
        }
    }

    private CloudDBTaskParameters createTaskParameters(ReadableMap map, int queryPolicy, String id)
            throws AGConnectCloudDBException, IllegalAccessException, NoSuchFieldException, InstantiationException {
        CloudDBZone cloudDBZone = getZoneFromList(id);
        CloudDBZoneQuery<?> query = queryBuilder(map);
        CloudDBZoneQuery.CloudDBZoneQueryPolicy policy = CloudDBUtils.queryPolicyValueOf(queryPolicy);
        return new CloudDBTaskParameters(cloudDBZone, query, policy);
    }

    public void executeQuery(ReadableMap map, int queryPolicy, String id, Promise promise) {
        CloudDBTaskParameters parameters;
        try {
            parameters = createTaskParameters(map, queryPolicy, id);
        } catch (Exception e) {
            promise.reject(e);
            return;
        }
        Task<? extends CloudDBZoneSnapshot<? extends CloudDBZoneObject>> queryTask = parameters.getZone()
                .executeQuery(parameters.getQuery(), parameters.getPolicy());
        queryTask.addOnSuccessListener(snapshot -> {
            if (snapshot == null) {
                promise.resolve(null);
                return;
            }
            WritableMap wm = Arguments.createMap();
            wm.putBoolean("hasPendingWrites", snapshot.hasPendingWrites());
            wm.putBoolean("isFromCloud", snapshot.isFromCloud());
            try {
                wm.putArray("snapshotObjects", resolveData(snapshot.getSnapshotObjects()));
                wm.putArray("upsertedObjects", resolveData(snapshot.getUpsertedObjects()));
                wm.putArray("deletedObjects", resolveData(snapshot.getDeletedObjects()));
                snapshot.release();
                promise.resolve(wm);
            } catch (AGConnectCloudDBException | IllegalAccessException | NoSuchFieldException e) {
                promise.reject(e);
            }
        }).addOnFailureListener(promise::reject);
    }

    public void executeAverageQuery(ReadableMap map, String fieldName, int queryPolicy, String id, Promise promise) {
        CloudDBTaskParameters parameters;
        try {
            parameters = createTaskParameters(map, queryPolicy, id);
        } catch (Exception e) {
            promise.reject(e);
            return;
        }
        Task<Double> queryTask = parameters.getZone()
                .executeAverageQuery(parameters.getQuery(), fieldName, parameters.getPolicy());
        queryTask.addOnSuccessListener(promise::resolve).addOnFailureListener(promise::reject);
    }

    public void executeSumQuery(ReadableMap map, String fieldName, int queryPolicy, String id, Promise promise) {
        CloudDBTaskParameters parameters;
        try {
            parameters = createTaskParameters(map, queryPolicy, id);
        } catch (Exception e) {
            promise.reject(e);
            return;
        }
        Task<Number> queryTask = parameters.getZone()
                .executeSumQuery(parameters.getQuery(), fieldName, parameters.getPolicy());
        queryTask.addOnSuccessListener(number -> {
            if (number == null) {
                promise.resolve(null);
            } else {
                promise.resolve(number.doubleValue());
            }
        }).addOnFailureListener(promise::reject);
    }

    public void executeMaximumQuery(ReadableMap map, String fieldName, int queryPolicy, String id, Promise promise) {
        CloudDBTaskParameters parameters;
        try {
            parameters = createTaskParameters(map, queryPolicy, id);
        } catch (Exception e) {
            promise.reject(e);
            return;
        }
        Task<Number> queryTask = parameters.getZone()
                .executeMaximumQuery(parameters.getQuery(), fieldName, parameters.getPolicy());
        queryTask.addOnSuccessListener(number -> promise.resolve(number.doubleValue()))
                .addOnFailureListener(promise::reject);
    }

    public void executeMinimalQuery(ReadableMap map, String fieldName, int queryPolicy, String id, Promise promise) {
        CloudDBTaskParameters parameters;
        try {
            parameters = createTaskParameters(map, queryPolicy, id);
        } catch (Exception e) {
            promise.reject(e);
            return;
        }
        Task<Number> queryTask = parameters.getZone()
                .executeMinimalQuery(parameters.getQuery(), fieldName, parameters.getPolicy());
        queryTask.addOnSuccessListener(number -> promise.resolve(number.doubleValue()))
                .addOnFailureListener(promise::reject);
    }

    public void executeCountQuery(ReadableMap map, String fieldName, int queryPolicy, String id, Promise promise) {
        CloudDBTaskParameters parameters;
        try {
            parameters = createTaskParameters(map, queryPolicy, id);
        } catch (Exception e) {
            promise.reject(e);
            return;
        }
      Task<Long> queryTask = parameters.getZone()
            .executeCountQuery(parameters.getQuery(), fieldName, parameters.getPolicy());
        queryTask.addOnSuccessListener(result -> promise.resolve(result.doubleValue()))
            .addOnFailureListener(promise::reject);
    }

    public void executeQueryUnsynced(ReadableMap map, String id, Promise promise) {
        CloudDBZone cloudDBZone;
        CloudDBZoneQuery<?> query;
        try {
            cloudDBZone = getZoneFromList(id);
            query = queryBuilder(map);
        } catch (Exception e) {
            promise.reject(e);
            return;
        }
        Task<? extends CloudDBZoneSnapshot<? extends CloudDBZoneObject>> queryTask = cloudDBZone.executeQueryUnsynced(
                query);
        queryTask.addOnSuccessListener(snapshot -> {
            if (snapshot == null) {
                promise.resolve(null);
                return;
            }
            WritableMap wm = Arguments.createMap();
            wm.putBoolean("isFromCloud", snapshot.isFromCloud());
            wm.putBoolean("hasPendingWrites", snapshot.hasPendingWrites());
            try {
                wm.putArray("upsertedObjects", resolveData(snapshot.getUpsertedObjects()));
                wm.putArray("deletedObjects", resolveData(snapshot.getDeletedObjects()));
                wm.putArray("snapshotObjects", resolveData(snapshot.getSnapshotObjects()));
                snapshot.release();
                promise.resolve(wm);
            } catch (AGConnectCloudDBException | IllegalAccessException | NoSuchFieldException e) {
                promise.reject(e);
            }
        }).addOnFailureListener(promise::reject);
    }

    public void removeSubscription(String id, String listenerId, Promise promise) {
        Map<String, ListenerHandler> zoneListeners = listenerHandlerMap.get(id);
        if (zoneListeners != null) {
            ListenerHandler listenerHandler = zoneListeners.get(listenerId);
            if (listenerHandler != null) {
                listenerHandler.remove();
                zoneListeners.remove(listenerId);
                if (zoneListeners.isEmpty()) {
                    listenerHandlerMap.remove(id);
                } else {
                    /*
                     * Cloud DB RN plugin supports minimum API level of 19.
                     * Map interface's replace function requires minimum API level of 24
                     * In order to replace an object from map, the below code will remove and put new object to map.
                     */
                    listenerHandlerMap.remove(id);
                    listenerHandlerMap.put(id, zoneListeners);
                }
                promise.resolve(null);
            } else {
                promise.reject("Subscription could not be found.");
            }
        } else {
            promise.reject("Subscription could not be found.");
        }
    }

    public void subscribeSnapshot(ReadableMap map, int queryPolicy, String id, String listenerId, Promise promise) {
        CloudDBZone cloudDBZone;
        CloudDBZoneQuery<?> query;
        CloudDBZoneQuery.CloudDBZoneQueryPolicy policy;
        try {
            cloudDBZone = getZoneFromList(id);
            query = queryBuilder(map);
            policy = CloudDBUtils.queryPolicyValueOf(queryPolicy);
        } catch (Exception e) {
            promise.reject(e);
            return;
        }
        try {
            ListenerHandler mRegister = cloudDBZone.subscribeSnapshot(query, policy, (cloudDBZoneSnapshot, e) -> {
                WritableMap wm = Arguments.createMap();
                wm.putString("id", listenerId);
                if (cloudDBZoneSnapshot == null) {
                    WritableMap errorWm = Arguments.createMap();
                    errorWm.putInt("code", e.getCode());
                    errorWm.putString("message", e.getErrMsg());
                    wm.putMap("error", errorWm);
                    reactApplicationContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                            .emit("onSnapshotUpdate", wm);
                } else {
                    try {
                        WritableMap data = Arguments.createMap();
                        data.putBoolean("hasPendingWrites", cloudDBZoneSnapshot.hasPendingWrites());
                        data.putBoolean("isFromCloud", cloudDBZoneSnapshot.isFromCloud());
                        data.putArray("snapshotObjects", resolveData(cloudDBZoneSnapshot.getSnapshotObjects()));
                        data.putArray("upsertedObjects", resolveData(cloudDBZoneSnapshot.getUpsertedObjects()));
                        data.putArray("deletedObjects", resolveData(cloudDBZoneSnapshot.getDeletedObjects()));
                        wm.putMap("data", data);
                        reactApplicationContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                                .emit("onSnapshotUpdate", wm);
                    } catch (AGConnectCloudDBException | NoSuchFieldException | IllegalAccessException exception) {
                        WritableMap errorWm = Arguments.createMap();
                        errorWm.putInt("code", exception instanceof AGConnectCloudDBException
                                ? ((AGConnectCloudDBException) exception).getCode()
                                : -1);
                        errorWm.putString("message", e != null
                                ? ((AGConnectCloudDBException) exception).getErrMsg()
                                : exception.getMessage());
                        wm.putMap("error", errorWm);
                        reactApplicationContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                                .emit("onSnapshotUpdate", wm);
                    } finally {
                        cloudDBZoneSnapshot.release();
                    }
                }
            });
            Map<String, ListenerHandler> zoneListeners = listenerHandlerMap.get(id);
            if (zoneListeners != null) {
                zoneListeners.put(listenerId, mRegister);
                listenerHandlerMap.put(id, zoneListeners);
            } else {
                Map<String, ListenerHandler> zoneListenerMap = new ConcurrentHashMap<>();
                zoneListenerMap.put(listenerId, mRegister);
                listenerHandlerMap.put(id, zoneListenerMap);
            }
        } catch (Exception e) {
            promise.reject(e);
        }
        promise.resolve(true);
    }

    public void runTransaction(ReadableArray transactionArray, String id, Promise promise) {
        Transaction.Function function = transaction -> {
            for (int i = 0; i < transactionArray.size(); i++) {
                ReadableMap transactionMap = transactionArray.getMap(i);
                assert transactionMap != null;
                String operation = transactionMap.getString("operation");
                String className = transactionMap.getString("className");
                ReadableArray objectArray = transactionMap.getArray("objectArray");
                List<CloudDBZoneObject> list;
                switch (Objects.requireNonNull(operation)) {
                    case "executeDelete":
                        try {
                            assert objectArray != null;
                            list = objectArrayToList(className, objectArray);
                        } catch (IllegalAccessException | NoSuchFieldException | InstantiationException | AGConnectCloudDBException e) {
                            return false;
                        }
                        transaction.executeDelete(list);
                        break;
                    case "executeUpsert":
                        try {
                            assert objectArray != null;
                            list = objectArrayToList(className, objectArray);
                        } catch (IllegalAccessException | NoSuchFieldException | InstantiationException | AGConnectCloudDBException e) {
                            return false;
                        }
                        transaction.executeUpsert(list);
                        break;
                    default:
                        break;
                }
            }
            return true;
        };
        try {
            CloudDBZone cloudDBZone = getZoneFromList(id);
            cloudDBZone.runTransaction(function)
                    .addOnSuccessListener(promise::resolve)
                    .addOnFailureListener(promise::reject);
        } catch (Exception e) {
            promise.reject(e);
        }
    }

    // MARK: Private Helper Methods.

    private List<CloudDBZoneObject> objectArrayToList(String className, ReadableArray objectArray)
            throws IllegalAccessException, NoSuchFieldException, InstantiationException, AGConnectCloudDBException {
        List<CloudDBZoneObject> list = new ArrayList<>();
        for (int j = 0; j < objectArray.size(); j++) {
            Map<String, ?> map = ReactUtils.toMap(objectArray.getMap(j));
            CloudDBZoneObject instance = CloudDBUtils.mapToObject(className, map);
            list.add(j, instance);
        }
        return list;
    }

    private CloudDBZone getZoneFromList(String id) throws AGConnectCloudDBException {
        CloudDBZone cloudDBZone = zoneMap.get(id);
        if (cloudDBZone != null) {
            return cloudDBZone;
        }
        throw new AGConnectCloudDBException("CloudDBZone is null, try re-open it", 11);
    }

    @NonNull
    private CloudDBZoneConfig handleConfig(ReadableMap config, Promise promise) throws AGConnectCloudDBException {
        String cloudDBZoneName = config.getString("cloudDBZoneName");
        CloudDBZoneConfig.CloudDBZoneSyncProperty syncProperty = CloudDBUtils.syncPropertyValueOf(
                config.getInt("syncProperty"));
        CloudDBZoneConfig.CloudDBZoneAccessProperty accessProperty = CloudDBUtils.accessPropertyValueOf(
                config.getInt("accessProperty"));
        assert cloudDBZoneName != null;
        CloudDBZoneConfig mConfig = new CloudDBZoneConfig(cloudDBZoneName, syncProperty, accessProperty);
        mConfig.setPersistenceEnabled(config.getBoolean("persistenceEnabled"));
        if (config.getBoolean("persistenceEnabled")) {
            String capacity = config.getString("capacity");
            try {
                long lCapacity = Long.parseLong(capacity);
                mConfig.setCapacity(lCapacity);
            } catch (NumberFormatException ex) {
                promise.reject(ex);
            }
        }
        if (config.getBoolean("isEncrypted")) {
            String key = config.getString("key");
            String reKey = config.getString("reKey");
            mConfig.setEncryptedKey(key, reKey);
        }
        return mConfig;
    }

    private WritableArray resolveData(CloudDBZoneObjectList<? extends CloudDBZoneObject> list)
            throws AGConnectCloudDBException, NoSuchFieldException, IllegalAccessException {
        if (list == null) {
            return Arguments.createArray();
        }
        WritableArray wa = Arguments.createArray();
        while (list.hasNext()) {
            CloudDBZoneObject instance = list.next();
            wa.pushMap(CloudDBUtils.toWM(instance));
        }
        return wa;
    }

    private CloudDBZoneQuery<?> queryBuilder(ReadableMap map)
            throws IllegalAccessException, NoSuchFieldException, InstantiationException, AGConnectCloudDBException {
        Class<? extends CloudDBZoneObject> clazz = CloudDBUtils.getClass(map.getString("className"));
        RNCloudDBQuery rnQuery = new RNCloudDBQuery(clazz);
        return rnQuery.buildQuery(map);
    }

    private void initAGConnectInstance(ReactApplicationContext reactApplicationContext) {
        if (AGConnectInstance.getInstance() == null) {
            AGConnectInstance.initialize(reactApplicationContext);
        }
        AGConnectCloudDB.initialize(reactApplicationContext);
    }
}