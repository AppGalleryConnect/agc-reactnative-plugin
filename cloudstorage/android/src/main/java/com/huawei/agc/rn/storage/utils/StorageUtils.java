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

package com.huawei.agc.rn.storage.utils;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableMapKeySetIterator;
import com.facebook.react.bridge.ReadableType;
import com.facebook.react.bridge.WritableMap;
import com.huawei.agconnect.AGCRoutePolicy;
import com.huawei.agconnect.cloud.storage.core.DownloadTask;
import com.huawei.agconnect.cloud.storage.core.StorageException;
import com.huawei.agconnect.cloud.storage.core.StreamDownloadTask;
import com.huawei.agconnect.cloud.storage.core.UploadTask;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class StorageUtils {

    private static final String TAG = StorageUtils.class.getSimpleName();

    public static WritableMap uploadResultToWm(UploadTask.UploadResult uploadResult, String uploadResultId) {
        WritableMap wm = Arguments.createMap();
        wm.putString("bytesTransferred", String.valueOf(uploadResult.getBytesTransferred()));
        wm.putString("totalByteCount", String.valueOf(uploadResult.getTotalByteCount()));
        wm.putString("uploadResultId", uploadResultId);
        return wm;
    }

    public static WritableMap downloadResultToWm(DownloadTask.DownloadResult downloadResult, String downloadResultId) {
        WritableMap wm = Arguments.createMap();
        wm.putString("bytesTransferred", String.valueOf(downloadResult.getBytesTransferred()));
        wm.putString("totalByteCount", String.valueOf(downloadResult.getTotalByteCount()));
        wm.putString("downloadResultId", downloadResultId);
        return wm;
    }

    public static WritableMap streamDownloadResultToWm(StreamDownloadTask.StreamDownloadResult streamDownloadResult, String streamDownloadResultId) {
        WritableMap wm = Arguments.createMap();
        wm.putString("bytesTransferred", String.valueOf(streamDownloadResult.getBytesTransferred()));
        wm.putString("totalByteCount", String.valueOf(streamDownloadResult.getTotalByteCount()));
        wm.putString("streamDownloadResultId", streamDownloadResultId);
        return wm;
    }

    public static WritableMap getCustomMetadata(Map<String, String> customMetadata) {
        WritableMap wm = Arguments.createMap();
        if (!(customMetadata == null || customMetadata.isEmpty())) {
            for (Map.Entry<String, String> entry : customMetadata.entrySet()) {
                wm.putString(entry.getKey(), entry.getValue());
            }
        }
        return wm;
    }

    public static AGCRoutePolicy getAGCRoutePolicy(int value) {
        AGCRoutePolicy agcRoutePolicy;
        switch (value) {
            case 1:
                agcRoutePolicy = AGCRoutePolicy.CHINA;
                break;
            case 2:
                agcRoutePolicy = AGCRoutePolicy.GERMANY;
                break;
            case 3:
                agcRoutePolicy = AGCRoutePolicy.RUSSIA;
                break;
            case 4:
                agcRoutePolicy = AGCRoutePolicy.SINGAPORE;
                break;
            default:
                agcRoutePolicy = AGCRoutePolicy.UNKNOWN;
        }
        return agcRoutePolicy;
    }

    public static String getUUID(Set<String> set) {
        String uuid = UUID.randomUUID().toString();
        while (set.contains(uuid)) {
            uuid = UUID.randomUUID().toString();
        }
        return uuid;
    }

    public static boolean containsKey(Set<String> set, String key, Promise promise) {
        if (key == null) {
            return false;
        }
        if (set.contains(key)) {
            return true;
        }
        StorageUtils.errorHandler(new Exception("Not Found Instance."), promise);
        return false;
    }

    public static Map<String, String> toMap(ReadableMap readableMap) {
        Map<String, String> map = new HashMap<>();
        ReadableMapKeySetIterator iterator = readableMap.keySetIterator();

        while (iterator.hasNextKey()) {
            String key = iterator.nextKey();
            ReadableType type = readableMap.getType(key);

            switch (type) {
                case Boolean:
                    map.put(key, String.valueOf(readableMap.getBoolean(key)));
                    break;
                case Number:
                    map.put(key, String.valueOf(readableMap.getDouble(key)));
                    break;
                case String:
                    map.put(key, readableMap.getString(key));
                    break;
                case Null:
                default:
                    map.put(key, null);
                    break;
            }
        }
        return map;
    }

    public static boolean hasValidKey(ReadableMap rm, String key, ReadableType type) {
        if (rm == null) {
            return false;
        }
        return rm.hasKey(key) && rm.getType(key) == type;
    }

    public static void errorHandler(Exception e, Promise promise) {
        if (e instanceof StorageException) {
            String message = ((StorageException) e).getErrMsg();
            String code = String.valueOf(((StorageException) e).getCode());
            promise.reject(code, message);
        }
        promise.reject("11001", e.getMessage());
    }

    public static WritableMap errorHandlerWithoutPromise(Exception e) {
        WritableMap wm = Arguments.createMap();
        if (e instanceof StorageException) {
            String message = ((StorageException) e).getErrMsg();
            String code = String.valueOf(((StorageException) e).getCode());
            wm.putString(code, message);
            return wm;
        }
        wm.putString("11001", e.getMessage());
        return (wm);
    }

    public static boolean isEmpty(String value) {
        return value == null || value.isEmpty() || value.trim().isEmpty();
    }
}
