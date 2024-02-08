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

import static com.huawei.agc.rn.storage.utils.StorageUtils.containsKey;
import static com.huawei.agc.rn.storage.utils.StorageUtils.getAGCRoutePolicy;
import static com.huawei.agc.rn.storage.utils.StorageUtils.getUUID;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableType;
import com.huawei.agc.rn.storage.utils.StorageUtils;
import com.huawei.agconnect.AGConnectInstance;
import com.huawei.agconnect.AGConnectOptions;
import com.huawei.agconnect.AGConnectOptionsBuilder;
import com.huawei.agconnect.cloud.storage.core.AGCStorageManagement;
import com.huawei.agconnect.cloud.storage.core.DownloadTask;
import com.huawei.agconnect.cloud.storage.core.FileMetadata;
import com.huawei.agconnect.cloud.storage.core.ListResult;
import com.huawei.agconnect.cloud.storage.core.StorageException;
import com.huawei.agconnect.cloud.storage.core.StorageReference;
import com.huawei.agconnect.cloud.storage.core.StreamDownloadTask;
import com.huawei.agconnect.cloud.storage.core.UploadTask;
import com.huawei.hmf.tasks.Task;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import javax.annotation.Nonnull;

public class AGCStorageManagementImpl extends ReactContextBaseJavaModule {
    private static String TAG = AGCStorageManagementImpl.class.getSimpleName();
    protected static final Map<String, AGCStorageManagement> STORAGE_MANAGEMENT_MAP = new ConcurrentHashMap<>();
    protected static final Map<String, StorageReference> STORAGE_REFERENCE_MAP = new ConcurrentHashMap<>();
    protected static final Map<String, FileMetadata> FILE_METADATA_MAP = new ConcurrentHashMap<>();
    protected static final Map<String, ListResult> LIST_RESULT_MAP = new ConcurrentHashMap<>();

    protected static final Map<String, UploadTask.UploadResult> UPLOAD_RESULT_MAP = new ConcurrentHashMap<>();
    protected static final Map<String, DownloadTask.DownloadResult> DOWNLOAD_RESULT_MAP = new ConcurrentHashMap<>();
    protected static final Map<String, StreamDownloadTask.StreamDownloadResult> STREAM_DOWNLOAD_RESULT_MAP = new ConcurrentHashMap<>();

    protected static final Map<String, UploadTask> UPLOAD_TASK_MAP = new ConcurrentHashMap<>();
    protected static final Map<String, DownloadTask> DOWNLOAD_TASK_MAP = new ConcurrentHashMap<>();
    protected static final Map<String, StreamDownloadTask> STREAM_DOWNLOAD_TASK_MAP = new ConcurrentHashMap<>();

    protected static final Map<String, Task<ListResult>> TASK_LIST_RESULT_MAP = new ConcurrentHashMap<>();
    protected static final Map<String, Task<FileMetadata>> TASK_FILE_METADATA_MAP = new ConcurrentHashMap<>();
    protected static final Map<String, Task<DownloadTask.DownloadResult>> TASK_DOWNLOAD_RESULT_MAP = new ConcurrentHashMap<>();
    protected static final Map<String, Task<UploadTask.UploadResult>> TASK_UPLOAD_RESULT_MAP = new ConcurrentHashMap<>();
    protected static final Map<String, Task<StreamDownloadTask.StreamDownloadResult>> TASK_STREAM_DOWNLOAD_RESULT_MAP = new ConcurrentHashMap<>();
    protected static final Map<String, Task<byte[]>> TASK_BYTE_MAP = new ConcurrentHashMap<>();
    protected static final Map<String, Task<Uri>> TASK_URI_MAP = new ConcurrentHashMap<>();
    protected static final Map<String, Task<Void>> TASK_DELETE_MAP = new ConcurrentHashMap<>();

    private final ReactApplicationContext reactContext;

    public AGCStorageManagementImpl(@NonNull ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
        Log.d(TAG, "create agcStorageManagement");
    }

    @Nonnull
    @Override
    public String getName() {
        return "AGCStorageManagementModule";
    }


    public void getInstance(ReadableMap params, final Promise promise) {
        try {
            String bucketName = "";
            int routePolicy = -1;

            if (StorageUtils.hasValidKey(params, "routePolicy", ReadableType.Number)) {
                routePolicy = params.getInt("routePolicy");
            }
            if (StorageUtils.hasValidKey(params, "bucketName", ReadableType.String)) {
                bucketName = params.getString("bucketName");
            }

            AGCStorageManagement agcStorageManagement = null;

            if (params == null) {
                agcStorageManagement = AGCStorageManagement.getInstance();
            } else {
                if ((routePolicy != -1) && (!Objects.equals(bucketName, ""))) {
                    AGConnectOptions agConnectOptions = new AGConnectOptionsBuilder().setRoutePolicy(getAGCRoutePolicy(routePolicy)).build(reactContext);
                    AGConnectInstance agConnectInstance = AGConnectInstance.buildInstance(agConnectOptions);
                    agcStorageManagement = AGCStorageManagement.getInstance(agConnectInstance, bucketName);
                } else if (!Objects.equals(bucketName, "")) {
                    agcStorageManagement = AGCStorageManagement.getInstance(bucketName);
                } else {
                    agcStorageManagement = AGCStorageManagement.getInstance();
                }
            }
            String storageManagementId = getUUID(STORAGE_MANAGEMENT_MAP.keySet());
            STORAGE_MANAGEMENT_MAP.put(storageManagementId, agcStorageManagement);
            promise.resolve(storageManagementId);
        } catch (RuntimeException e) {
            StorageUtils.errorHandler(e, promise);
            throw e;
        } catch (Exception e) {
            StorageUtils.errorHandler(e, promise);
        }
    }

    @ReactMethod
    public void getStorageReference(String id, String path, final Promise promise) {
        try {
            if (!containsKey(STORAGE_MANAGEMENT_MAP.keySet(), id, promise)) {
                return;
            }
            StorageReference storageReference = path.equals("") ? STORAGE_MANAGEMENT_MAP.get(id).getStorageReference() : STORAGE_MANAGEMENT_MAP.get(id).getStorageReference(path);
            String storageReferenceId = getUUID(STORAGE_REFERENCE_MAP.keySet());
            STORAGE_REFERENCE_MAP.put(storageReferenceId, storageReference);
            promise.resolve(storageReferenceId);
        } catch (Exception e) {
            StorageUtils.errorHandler(e, promise);
        }
    }

    @ReactMethod
    public void getReferenceFromUrl(ReadableMap params, final Promise promise) {
        try {
            String storageManagementId = "";
            String fullUrl = "";
            int routePolicy = -1;

            if (StorageUtils.hasValidKey(params, "storageManagementId", ReadableType.String)) {
                storageManagementId = params.getString("storageManagementId");
            }
            if (!containsKey(STORAGE_MANAGEMENT_MAP.keySet(), storageManagementId, promise)) {
                return;
            }
            if (StorageUtils.hasValidKey(params, "routePolicy", ReadableType.Number)) {
                routePolicy = params.getInt("routePolicy");
            }
            if (StorageUtils.hasValidKey(params, "fullUrl", ReadableType.String)) {
                fullUrl = params.getString("fullUrl");
            }

            StorageReference storageReference = null;

            if ((routePolicy != -1) && (!Objects.equals(fullUrl, ""))) {
                AGConnectOptions agConnectOptions = new AGConnectOptionsBuilder().setRoutePolicy(getAGCRoutePolicy(routePolicy)).build(reactContext);
                AGConnectInstance agConnectInstance = AGConnectInstance.buildInstance(agConnectOptions);
                storageReference = STORAGE_MANAGEMENT_MAP.get(storageManagementId).getReferenceFromUrl(agConnectInstance, fullUrl);
            } else {
                StorageUtils.errorHandler(StorageException.fromErrorStatus (11018), promise);
            }
            String storageReferenceId = getUUID(STORAGE_REFERENCE_MAP.keySet());
            STORAGE_REFERENCE_MAP.put(storageReferenceId, storageReference);
            promise.resolve(storageReferenceId);
        } catch (Exception e) {
            StorageUtils.errorHandler(e, promise);
        }
    }

    @ReactMethod
    public void getMaxUploadTimeout(String id, Promise promise) {
        if (!containsKey(STORAGE_MANAGEMENT_MAP.keySet(), id, promise)) {
            return;
        }
        promise.resolve(String.valueOf(STORAGE_MANAGEMENT_MAP.get(id).getMaxUploadTimeout()));
    }

    @ReactMethod
    public void getMaxDownloadTimeout(String id, Promise promise) {
        if (!containsKey(STORAGE_MANAGEMENT_MAP.keySet(), id, promise)) {
            return;
        }
        promise.resolve(String.valueOf(STORAGE_MANAGEMENT_MAP.get(id).getMaxDownloadTimeout()));
    }

    @ReactMethod
    public void getMaxRequestTimeout(String id, Promise promise) {
        if (!containsKey(STORAGE_MANAGEMENT_MAP.keySet(), id, promise)) {
            return;
        }
        promise.resolve(String.valueOf(STORAGE_MANAGEMENT_MAP.get(id).getMaxRequestTimeout()));
    }

    @ReactMethod
    public void getRetryTimes(String id, Promise promise) {
        if (!containsKey(STORAGE_MANAGEMENT_MAP.keySet(), id, promise)) {
            return;
        }
        promise.resolve(STORAGE_MANAGEMENT_MAP.get(id).getRetryTimes());
    }

    @ReactMethod
    public void getArea(String id, Promise promise) {
        if (!containsKey(STORAGE_MANAGEMENT_MAP.keySet(), id, promise)) {
            return;
        }
        promise.resolve(STORAGE_MANAGEMENT_MAP.get(id).getArea());
    }

    @ReactMethod
    public void setMaxUploadTimeout(String id, String maxUploadTimeout, Promise promise) {
        try {
            if (!containsKey(STORAGE_MANAGEMENT_MAP.keySet(), id, promise)) {
                return;
            }
            STORAGE_MANAGEMENT_MAP.get(id).setMaxUploadTimeout(Long.parseLong(maxUploadTimeout));
            promise.resolve(null);
        } catch (Exception e) {
            StorageUtils.errorHandler(e, promise);
        }
    }

    @ReactMethod
    public void setMaxDownloadTimeout(String id, String maxDownloadTimeout, Promise promise) {
        try {
            if (!containsKey(STORAGE_MANAGEMENT_MAP.keySet(), id, promise)) {
                return;
            }
            STORAGE_MANAGEMENT_MAP.get(id).setMaxDownloadTimeout(Long.parseLong(maxDownloadTimeout));
            promise.resolve(null);
        } catch (Exception e) {
            StorageUtils.errorHandler(e, promise);
        }
    }

    @ReactMethod
    public void setMaxRequestTimeout(String id, String maxRequestTimeout, Promise promise) {
        try {
            if (!containsKey(STORAGE_MANAGEMENT_MAP.keySet(), id, promise)) {
                return;
            }
            STORAGE_MANAGEMENT_MAP.get(id).setMaxRequestTimeout(Long.parseLong(maxRequestTimeout));
            promise.resolve(null);
        } catch (Exception e) {
            StorageUtils.errorHandler(e, promise);
        }
    }

    @ReactMethod
    public void setRetryTimes(String id, int retryTimes, Promise promise) {
        try {
            if (!containsKey(STORAGE_MANAGEMENT_MAP.keySet(), id, promise)) {
                return;
            }
            STORAGE_MANAGEMENT_MAP.get(id).setRetryTimes(retryTimes);
            promise.resolve(null);
        } catch (Exception e) {
            StorageUtils.errorHandler(e, promise);
        }
    }

    public void clearReference(String referenceKey, String referenceName, Promise promise) {
        switch (referenceName) {
            case "STORAGE_MANAGEMENT_MAP":
                if (!containsKey(STORAGE_MANAGEMENT_MAP.keySet(), referenceKey, promise)) {
                    return;
                }
                STORAGE_MANAGEMENT_MAP.remove(referenceKey);
                break;
            case "STORAGE_REFERENCE_MAP":
                if (!containsKey(STORAGE_REFERENCE_MAP.keySet(), referenceKey, promise)) {
                    return;
                }
                STORAGE_REFERENCE_MAP.remove(referenceKey);
                break;
            case "FILE_METADATA_MAP":
                if (!containsKey(FILE_METADATA_MAP.keySet(), referenceKey, promise)) {
                    return;
                }
                FILE_METADATA_MAP.remove(referenceKey);
                break;
            case "LIST_RESULT_MAP":
                if (!containsKey(LIST_RESULT_MAP.keySet(), referenceKey, promise)) {
                    return;
                }
                LIST_RESULT_MAP.remove(referenceKey);
                break;
            case "UPLOAD_RESULT_MAP":
                if (!containsKey(UPLOAD_RESULT_MAP.keySet(), referenceKey, promise)) {
                    return;
                }
                UPLOAD_RESULT_MAP.remove(referenceKey);
                break;
            case "DOWNLOAD_RESULT_MAP":
                if (!containsKey(DOWNLOAD_RESULT_MAP.keySet(), referenceKey, promise)) {
                    return;
                }
                DOWNLOAD_RESULT_MAP.remove(referenceKey);
                break;
            case "STREAM_DOWNLOAD_RESULT_MAP":
                if (!containsKey(STREAM_DOWNLOAD_RESULT_MAP.keySet(), referenceKey, promise)) {
                    return;
                }
                STREAM_DOWNLOAD_RESULT_MAP.remove(referenceKey);
                break;
            case "UPLOAD_TASK_MAP":
                if (!containsKey(UPLOAD_TASK_MAP.keySet(), referenceKey, promise)) {
                    return;
                }
                UPLOAD_TASK_MAP.remove(referenceKey);
                break;
            case "DOWNLOAD_TASK_MAP":
                if (!containsKey(DOWNLOAD_TASK_MAP.keySet(), referenceKey, promise)) {
                    return;
                }
                DOWNLOAD_TASK_MAP.remove(referenceKey);
                break;
            case "DELETE_TASK_MAP":
                if (!containsKey(TASK_DELETE_MAP.keySet(), referenceKey, promise)) {
                    return;
                }
                TASK_DELETE_MAP.remove(referenceKey);
                break;
            case "STREAM_DOWNLOAD_TASK_MAP":
                if (!containsKey(STREAM_DOWNLOAD_TASK_MAP.keySet(), referenceKey, promise)) {
                    return;
                }
                STREAM_DOWNLOAD_TASK_MAP.remove(referenceKey);
                break;
            case "TASK_LIST_RESULT_MAP":
                if (!containsKey(TASK_LIST_RESULT_MAP.keySet(), referenceKey, promise)) {
                    return;
                }
                TASK_LIST_RESULT_MAP.remove(referenceKey);
                break;
            case "TASK_FILE_METADATA_MAP":
                if (!containsKey(TASK_FILE_METADATA_MAP.keySet(), referenceKey, promise)) {
                    return;
                }
                TASK_FILE_METADATA_MAP.remove(referenceKey);
                break;
            case "TASK_DOWNLOAD_RESULT_MAP":
                if (!containsKey(TASK_DOWNLOAD_RESULT_MAP.keySet(), referenceKey, promise)) {
                    return;
                }
                TASK_DOWNLOAD_RESULT_MAP.remove(referenceKey);
                break;
            case "TASK_UPLOAD_RESULT_MAP":
                if (!containsKey(TASK_UPLOAD_RESULT_MAP.keySet(), referenceKey, promise)) {
                    return;
                }
                TASK_UPLOAD_RESULT_MAP.remove(referenceKey);
                break;
            case "TASK_STREAM_DOWNLOAD_RESULT_MAP":
                if (!containsKey(TASK_STREAM_DOWNLOAD_RESULT_MAP.keySet(), referenceKey, promise)) {
                    return;
                }
                TASK_STREAM_DOWNLOAD_RESULT_MAP.remove(referenceKey);
                break;
            case "TASK_BYTE_MAP":
                if (!containsKey(TASK_BYTE_MAP.keySet(), referenceKey, promise)) {
                    return;
                }
                TASK_BYTE_MAP.remove(referenceKey);
                break;
            case "TASK_URI_MAP":
                if (!containsKey(TASK_URI_MAP.keySet(), referenceKey, promise)) {
                    return;
                }
                TASK_URI_MAP.remove(referenceKey);
                break;
            default:
                StorageUtils.errorHandler(new Exception("Not found reference name."), promise);
                return;
        }
        promise.resolve(true);
    }

    @ReactMethod
    public void addListener(String eventName) {
    }

    @ReactMethod
    public void removeListeners(Integer count) {
    }
}
