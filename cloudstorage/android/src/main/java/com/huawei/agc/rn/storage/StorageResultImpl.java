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

import static com.huawei.agc.rn.storage.AGCStorageManagementImpl.FILE_METADATA_MAP;
import static com.huawei.agc.rn.storage.AGCStorageManagementImpl.STREAM_DOWNLOAD_RESULT_MAP;
import static com.huawei.agc.rn.storage.AGCStorageManagementImpl.UPLOAD_RESULT_MAP;
import static com.huawei.agc.rn.storage.utils.StorageUtils.containsKey;
import static com.huawei.agc.rn.storage.utils.StorageUtils.getUUID;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.huawei.agc.rn.storage.utils.StorageUtils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import javax.annotation.Nonnull;

public class StorageResultImpl extends ReactContextBaseJavaModule {

    public StorageResultImpl(@NonNull ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Nonnull
    @Override
    public String getName() {
        return "StorageResult";
    }

    @ReactMethod
    public void getMetadata(String resultType, String resultId, final Promise promise) {
        if ("UploadResult".equals(resultType)) {
            if (containsKey(UPLOAD_RESULT_MAP.keySet(), resultId, promise)) {
                return;
            }
            String fileMetadataId = getUUID(FILE_METADATA_MAP.keySet());
            FILE_METADATA_MAP.put(fileMetadataId, UPLOAD_RESULT_MAP.get(resultId).getMetadata());
            promise.resolve(fileMetadataId);
        } else {
            StorageUtils.errorHandler(new Exception("Not found result"), promise);
        }
    }

    @ReactMethod
    public void getStream(String resultType, String resultId, Promise promise) {
        try {
            if ("StreamDownloadResult".equals(resultType)) {
                if (!containsKey(STREAM_DOWNLOAD_RESULT_MAP.keySet(), resultId, promise)) {
                    return;
                }
                InputStream is = STREAM_DOWNLOAD_RESULT_MAP.get(resultId).getStream();
                ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                int nRead;
                byte[] data = new byte[1024];

                while ((nRead = is.read(data, 0, data.length)) != -1) {
                    buffer.write(data, 0, nRead);
                }
                promise.resolve(buffer.toString("UTF-8"));
                buffer.flush();

            } else {
                StorageUtils.errorHandler(new Exception("Not found result"), promise);
            }
        } catch (RuntimeException e) {
            StorageUtils.errorHandler(e, promise);
            throw e;
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
