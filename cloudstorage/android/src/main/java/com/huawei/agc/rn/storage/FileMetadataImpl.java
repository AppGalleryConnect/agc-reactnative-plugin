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

import static com.huawei.agc.rn.storage.AGCStorageManagementImpl.STORAGE_MANAGEMENT_MAP;
import static com.huawei.agc.rn.storage.AGCStorageManagementImpl.FILE_METADATA_MAP;
import static com.huawei.agc.rn.storage.AGCStorageManagementImpl.STORAGE_REFERENCE_MAP;
import static com.huawei.agc.rn.storage.utils.StorageUtils.containsKey;
import static com.huawei.agc.rn.storage.utils.StorageUtils.getUUID;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.huawei.agc.rn.storage.utils.StorageUtils;
import com.huawei.agconnect.cloud.storage.core.FileMetadata;

import javax.annotation.Nonnull;

public class FileMetadataImpl extends ReactContextBaseJavaModule {

    public FileMetadataImpl(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Nonnull
    @Override
    public String getName() {
        return "FileMetadata";
    }

    @ReactMethod
    public void newFileMetadata(Promise promise) {
        FileMetadata fileMetadata = new FileMetadata();
        String fileMetadataId = getUUID(FILE_METADATA_MAP.keySet());
        FILE_METADATA_MAP.put(fileMetadataId, fileMetadata);
        promise.resolve(fileMetadataId);
    }

    @ReactMethod
    public void getBucket(String id, final Promise promise) {
        if (!containsKey(FILE_METADATA_MAP.keySet(), id, promise)) {
            return;
        }
        promise.resolve(FILE_METADATA_MAP.get(id).getBucket());
    }

    @ReactMethod
    public void getCTime(String id, final Promise promise) {
        if (!containsKey(FILE_METADATA_MAP.keySet(), id, promise)) {
            return;
        }
        promise.resolve(FILE_METADATA_MAP.get(id).getCTime());
    }

    @ReactMethod
    public void getMTime(String id, final Promise promise) {
        if (!containsKey(FILE_METADATA_MAP.keySet(), id, promise)) {
            return;
        }
        promise.resolve(FILE_METADATA_MAP.get(id).getMTime());
    }

    @ReactMethod
    public void getName(String id, final Promise promise) {
        if (!containsKey(FILE_METADATA_MAP.keySet(), id, promise)) {
            return;
        }
        promise.resolve(FILE_METADATA_MAP.get(id).getName());
    }

    @ReactMethod
    public void getPath(String id, final Promise promise) {
        if (!containsKey(FILE_METADATA_MAP.keySet(), id, promise)) {
            return;
        }
        promise.resolve(FILE_METADATA_MAP.get(id).getPath());
    }

    @ReactMethod
    public void getSize(String id, final Promise promise) {
        if (!containsKey(FILE_METADATA_MAP.keySet(), id, promise)) {
            return;
        }
        promise.resolve(String.valueOf(FILE_METADATA_MAP.get(id).getSize()));
    }

    @ReactMethod
    public void getSHA256Hash(String id, final Promise promise) {
        if (!containsKey(FILE_METADATA_MAP.keySet(), id, promise)) {
            return;
        }
        promise.resolve(FILE_METADATA_MAP.get(id).getSHA256Hash());
    }

    @ReactMethod
    public void getContentType(String id, final Promise promise) {
        if (!containsKey(FILE_METADATA_MAP.keySet(), id, promise)) {
            return;
        }
        promise.resolve(FILE_METADATA_MAP.get(id).getContentType());
    }

    @ReactMethod
    public void getCacheControl(String id, final Promise promise) {
        if (!containsKey(FILE_METADATA_MAP.keySet(), id, promise)) {
            return;
        }
        promise.resolve(FILE_METADATA_MAP.get(id).getCacheControl());
    }

    @ReactMethod
    public void getContentDisposition(String id, final Promise promise) {
        if (!containsKey(FILE_METADATA_MAP.keySet(), id, promise)) {
            return;
        }
        promise.resolve(FILE_METADATA_MAP.get(id).getContentDisposition());
    }

    @ReactMethod
    public void getContentLanguage(String id, final Promise promise) {
        if (!containsKey(FILE_METADATA_MAP.keySet(), id, promise)) {
            return;
        }
        promise.resolve(FILE_METADATA_MAP.get(id).getContentLanguage());
    }

    @ReactMethod
    public void getContentEncoding(String id, final Promise promise) {
        if (!containsKey(FILE_METADATA_MAP.keySet(), id, promise)) {
            return;
        }
        promise.resolve(FILE_METADATA_MAP.get(id).getContentEncoding());
    }

    @ReactMethod
    public void getCustomMetadata(String id, final Promise promise) {
        if (!containsKey(FILE_METADATA_MAP.keySet(), id, promise)) {
            return;
        }
        promise.resolve(StorageUtils.getCustomMetadata(FILE_METADATA_MAP.get(id).getCustomMetadata()));
    }

    @ReactMethod
    public void setSHA256Hash(String id, String sha256, Promise promise) {
        try {
            if (!containsKey(FILE_METADATA_MAP.keySet(), id, promise)) {
                return;
            }
            FILE_METADATA_MAP.get(id).setSHA256Hash(sha256);
            promise.resolve(null);
        } catch (Exception e) {
            StorageUtils.errorHandler(e, promise);
        }
    }

    @ReactMethod
    public void setContentType(String id, String contentType, final Promise promise) {
        try {
            if (!containsKey(FILE_METADATA_MAP.keySet(), id, promise)) {
                return;
            }
            FILE_METADATA_MAP.get(id).setContentType(contentType);
            promise.resolve(null);
        } catch (Exception e) {
            StorageUtils.errorHandler(e, promise);
        }
    }

    @ReactMethod
    public void setCacheControl(String id, String cacheControl, final Promise promise) {
        try {
            if (!containsKey(FILE_METADATA_MAP.keySet(), id, promise)) {
                return;
            }
            FILE_METADATA_MAP.get(id).setCacheControl(cacheControl);
            promise.resolve(null);
        } catch (Exception e) {
            StorageUtils.errorHandler(e, promise);
        }
    }

    @ReactMethod
    public void setContentDisposition(String id, String contentDisposition, final Promise promise) {
        try {
            if (!containsKey(FILE_METADATA_MAP.keySet(), id, promise)) {
                return;
            }
            FILE_METADATA_MAP.get(id).setContentDisposition(contentDisposition);
            promise.resolve(null);
        } catch (Exception e) {
            StorageUtils.errorHandler(e, promise);
        }
    }

    @ReactMethod
    public void setContentEncoding(String id, String contentEncoding, final Promise promise) {
        try {
            if (!containsKey(FILE_METADATA_MAP.keySet(), id, promise)) {
                return;
            }
            FILE_METADATA_MAP.get(id).setContentEncoding(contentEncoding);
            promise.resolve(null);
        } catch (Exception e) {
            StorageUtils.errorHandler(e, promise);
        }
    }

    @ReactMethod
    public void setContentLanguage(String id, String contentLanguage, final Promise promise) {
        try {
            if (!containsKey(FILE_METADATA_MAP.keySet(), id, promise)) {
                return;
            }
            FILE_METADATA_MAP.get(id).setContentLanguage(contentLanguage);
            promise.resolve(null);
        } catch (Exception e) {
            StorageUtils.errorHandler(e, promise);
        }
    }

    @ReactMethod
    public void setCustomMetadata(String id, ReadableMap metaData, final Promise promise) {
        if (!containsKey(FILE_METADATA_MAP.keySet(), id, promise)) {
            return;
        }

        if (metaData == null) {
            return;
        }

        try {
            FILE_METADATA_MAP.get(id).setCustomMetadata(StorageUtils.toMap(metaData));
            promise.resolve(null);
        } catch (Exception e) {
            StorageUtils.errorHandler(e, promise);
        }
    }

    @ReactMethod
    public void getStorageReference(String id, final Promise promise) {
        try {
            if (!containsKey(FILE_METADATA_MAP.keySet(), id, promise)) {
                return;
            }
            String storageManagementId = getUUID(STORAGE_MANAGEMENT_MAP.keySet());
            String storageReferenceId = getUUID(STORAGE_REFERENCE_MAP.keySet());
            STORAGE_REFERENCE_MAP.put(storageReferenceId, FILE_METADATA_MAP.get(id).getStorageReference());
            STORAGE_MANAGEMENT_MAP.put(storageManagementId, FILE_METADATA_MAP.get(id).getStorageReference().getStorage());
            WritableMap wm = Arguments.createMap();

            wm.putString("storageReferenceId", storageReferenceId);
            wm.putString("storageManagementId", storageManagementId);
            promise.resolve(wm);
        } catch (Exception e) {
            StorageUtils.errorHandler(e, promise);
        }
    }

    @ReactMethod
    public void addListener(String eventName) {
    }

    @ReactMethod
    public void removeListeners(Integer count) {
    }
}
