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

package com.huawei.agc.rn.storage;

import androidx.annotation.NonNull;

import static com.huawei.agc.rn.storage.AGCStorageManagementImpl.LIST_RESULT_MAP;
import static com.huawei.agc.rn.storage.AGCStorageManagementImpl.STORAGE_MANAGEMENT_MAP;
import static com.huawei.agc.rn.storage.AGCStorageManagementImpl.STORAGE_REFERENCE_MAP;
import static com.huawei.agc.rn.storage.utils.StorageUtils.containsKey;
import static com.huawei.agc.rn.storage.utils.StorageUtils.getUUID;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.huawei.agc.rn.storage.utils.StorageUtils;
import com.huawei.agconnect.cloud.storage.core.StorageReference;

import java.util.List;

import javax.annotation.Nonnull;

public class ListResultImpl extends ReactContextBaseJavaModule {

    public ListResultImpl(@NonNull ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Nonnull
    @Override
    public String getName() {
        return "ListResult";
    }

    @ReactMethod
    public void getFileList(String id, final Promise promise) {
        try {
            if (!containsKey(LIST_RESULT_MAP.keySet(), id, promise)) {
                return;
            }
            List<StorageReference> storageReferenceList = LIST_RESULT_MAP.get(id).getFileList();
            if (storageReferenceList.size() == 0) {
                StorageUtils.errorHandler(new Exception("File List Empty"), promise);
                return;
            }
            promise.resolve(storageReferenceListToWm(storageReferenceList));
        } catch (Exception e) {
            StorageUtils.errorHandler(e, promise);
        }
    }

    @ReactMethod
    public void getDirList(String id, final Promise promise) {
        try {
            if (!containsKey(LIST_RESULT_MAP.keySet(), id, promise)) {
                return;
            }
            List<StorageReference> storageReferenceList = LIST_RESULT_MAP.get(id).getDirList();
            if (storageReferenceList.size() == 0) {
                StorageUtils.errorHandler(new Exception("Directory List Empty"), promise);
                return;
            }
            promise.resolve(storageReferenceListToWm(storageReferenceList));
        } catch (Exception e) {
            StorageUtils.errorHandler(e, promise);
        }
    }

    @ReactMethod
    public void getPageMarker(String id, final Promise promise) {
        try {
            promise.resolve(LIST_RESULT_MAP.get(id).getPageMarker());
        } catch (Exception e) {
            StorageUtils.errorHandler(e, promise);
        }
    }

    public static WritableMap storageReferenceListToWm(List<StorageReference> storageReferenceList) {
        String storageManagementId = getUUID(STORAGE_MANAGEMENT_MAP.keySet());
        STORAGE_MANAGEMENT_MAP.put(storageManagementId, storageReferenceList.get(0).getStorage());
        WritableMap wm = Arguments.createMap();
        wm.putString("storageManagementId", storageManagementId);
        WritableArray wa = Arguments.createArray();
        for (StorageReference ref : storageReferenceList) {
            String storageReferenceId = getUUID(STORAGE_REFERENCE_MAP.keySet());
            STORAGE_REFERENCE_MAP.put(storageReferenceId, ref);
            wa.pushString(storageReferenceId);
        }
        wm.putArray("storageReferenceIdArray", wa);
        return wm;
    }

    @ReactMethod
    public void addListener(String eventName) {
    }

    @ReactMethod
    public void removeListeners(Integer count) {
    }
}
