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

import static com.huawei.agc.rn.storage.AGCStorageManagementImpl.TASK_DELETE_MAP;
import static com.huawei.agc.rn.storage.AGCStorageManagementImpl.DOWNLOAD_TASK_MAP;
import static com.huawei.agc.rn.storage.AGCStorageManagementImpl.FILE_METADATA_MAP;
import static com.huawei.agc.rn.storage.AGCStorageManagementImpl.STORAGE_REFERENCE_MAP;
import static com.huawei.agc.rn.storage.AGCStorageManagementImpl.STREAM_DOWNLOAD_TASK_MAP;
import static com.huawei.agc.rn.storage.AGCStorageManagementImpl.TASK_BYTE_MAP;
import static com.huawei.agc.rn.storage.AGCStorageManagementImpl.TASK_FILE_METADATA_MAP;
import static com.huawei.agc.rn.storage.AGCStorageManagementImpl.TASK_LIST_RESULT_MAP;
import static com.huawei.agc.rn.storage.AGCStorageManagementImpl.TASK_URI_MAP;
import static com.huawei.agc.rn.storage.AGCStorageManagementImpl.UPLOAD_TASK_MAP;
import static com.huawei.agc.rn.storage.utils.StorageUtils.containsKey;
import static com.huawei.agc.rn.storage.utils.StorageUtils.getUUID;
import static com.huawei.agc.rn.storage.utils.StorageUtils.isEmpty;

import android.net.Uri;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.WritableArray;
import com.huawei.agc.rn.storage.utils.StorageUtils;
import com.huawei.agconnect.cloud.storage.core.DownloadTask;
import com.huawei.agconnect.cloud.storage.core.FileMetadata;
import com.huawei.agconnect.cloud.storage.core.ListResult;
import com.huawei.agconnect.cloud.storage.core.StreamDownloadTask;
import com.huawei.agconnect.cloud.storage.core.UploadTask;
import com.huawei.hmf.tasks.Task;

import java.io.File;
import java.util.List;

import javax.annotation.Nonnull;

public class StorageReferenceImpl extends ReactContextBaseJavaModule {

    public StorageReferenceImpl(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Nonnull
    @Override
    public String getName() {
        return "StorageReferenceModule";
    }

    @ReactMethod
    public void child(String storageReferenceId, String objectPath, Promise promise) {
        if (!containsKey(STORAGE_REFERENCE_MAP.keySet(), storageReferenceId, promise)) {
            return;
        }
        try {
            String childId = getUUID(STORAGE_REFERENCE_MAP.keySet());
            STORAGE_REFERENCE_MAP.put(childId, STORAGE_REFERENCE_MAP.get(storageReferenceId).child(objectPath));
            promise.resolve(childId);
        } catch (Exception e) {
            StorageUtils.errorHandler(e, promise);
        }
    }

    @ReactMethod
    public void getParent(String storageReferenceId, Promise promise) {
        if (!containsKey(STORAGE_REFERENCE_MAP.keySet(), storageReferenceId, promise)) {
            return;
        }
        try {
            String parentId = getUUID(STORAGE_REFERENCE_MAP.keySet());
            STORAGE_REFERENCE_MAP.put(parentId, STORAGE_REFERENCE_MAP.get(storageReferenceId).getParent());
            promise.resolve(parentId);
        } catch (Exception e) {
            StorageUtils.errorHandler(e, promise);
        }
    }

    @ReactMethod
    public void getRoot(String storageReferenceId, Promise promise) {
        if (!containsKey(STORAGE_REFERENCE_MAP.keySet(), storageReferenceId, promise)) {
            return;
        }
        try {
            String rootId = getUUID(STORAGE_REFERENCE_MAP.keySet());
            STORAGE_REFERENCE_MAP.put(rootId, STORAGE_REFERENCE_MAP.get(storageReferenceId).getRoot());
            promise.resolve(rootId);
        } catch (Exception e) {
            StorageUtils.errorHandler(e, promise);
        }
    }

    @ReactMethod
    public void getBucket(String storageReferenceId, Promise promise) {
        if (!containsKey(STORAGE_REFERENCE_MAP.keySet(), storageReferenceId, promise)) {
            return;
        }
        promise.resolve(STORAGE_REFERENCE_MAP.get(storageReferenceId).getBucket());
    }

    @ReactMethod
    public void getName(String storageReferenceId, Promise promise) {
        if (!containsKey(STORAGE_REFERENCE_MAP.keySet(), storageReferenceId, promise)) {
            return;
        }
        promise.resolve(STORAGE_REFERENCE_MAP.get(storageReferenceId).getName());
    }

    @ReactMethod
    public void getPath(String storageReferenceId, Promise promise) {
        if (!containsKey(STORAGE_REFERENCE_MAP.keySet(), storageReferenceId, promise)) {
            return;
        }
        promise.resolve(STORAGE_REFERENCE_MAP.get(storageReferenceId).getPath());
    }

    @ReactMethod
    public void getFileMetadata(String storageReferenceId, Promise promise) {
        String taskFileMetadataId = getUUID(TASK_FILE_METADATA_MAP.keySet());
        if (!containsKey(STORAGE_REFERENCE_MAP.keySet(), storageReferenceId, promise)) {
            return;
        }
        TASK_FILE_METADATA_MAP.put(taskFileMetadataId,
                STORAGE_REFERENCE_MAP.get(storageReferenceId).getFileMetadata());
        promise.resolve(taskFileMetadataId);
    }

    @ReactMethod
    public void updateFileMetadata(String storageReferenceId, String metadataId, Promise promise) {
        if (!containsKey(STORAGE_REFERENCE_MAP.keySet(), storageReferenceId, promise)) {
            return;
        }
        if (!containsKey(FILE_METADATA_MAP.keySet(), metadataId, promise)) {
            return;
        }
        FileMetadata fileMetadata = FILE_METADATA_MAP.get(metadataId);
        String taskFileMetadataId = getUUID(TASK_FILE_METADATA_MAP.keySet());
        TASK_FILE_METADATA_MAP.put(taskFileMetadataId,
                STORAGE_REFERENCE_MAP.get(storageReferenceId).updateFileMetadata(fileMetadata));
        promise.resolve(taskFileMetadataId);
    }

    @ReactMethod
    public void delete(String storageReferenceId, Promise promise) {
        if (!containsKey(STORAGE_REFERENCE_MAP.keySet(), storageReferenceId, promise)) {
            return;
        }
        Task<Void> taskDelete = STORAGE_REFERENCE_MAP.get(storageReferenceId).delete();
        String taskDeleteId = getUUID(TASK_DELETE_MAP.keySet());
        TASK_DELETE_MAP.put(taskDeleteId, taskDelete);
        promise.resolve(taskDeleteId);
    }

    @ReactMethod
    public void list(String storageReferenceId, int max, String marker, final Promise promise) {
        if (!containsKey(STORAGE_REFERENCE_MAP.keySet(), storageReferenceId, promise)) {
            return;
        }

        try {
            Task<ListResult> listResult;
            if (!isEmpty(marker)) {
                listResult = STORAGE_REFERENCE_MAP.get(storageReferenceId).list(max, marker);
            } else {
                listResult = STORAGE_REFERENCE_MAP.get(storageReferenceId).list(max);
            }
            String taskListResultId = getUUID(TASK_LIST_RESULT_MAP.keySet());
            TASK_LIST_RESULT_MAP.put(taskListResultId, listResult);
            promise.resolve(taskListResultId);
        } catch (Exception e) {
            StorageUtils.errorHandler(e, promise);
        }
    }

    @ReactMethod
    public void listAll(String storageReferenceId, final Promise promise) {
        if (!containsKey(STORAGE_REFERENCE_MAP.keySet(), storageReferenceId, promise)) {
            return;
        }
        Task<ListResult> listResult;
        listResult = STORAGE_REFERENCE_MAP.get(storageReferenceId).listAll();
        String taskListResultId = getUUID(TASK_LIST_RESULT_MAP.keySet());
        TASK_LIST_RESULT_MAP.put(taskListResultId, listResult);
        promise.resolve(taskListResultId);
    }

    @ReactMethod
    public void putFile(String storageReferenceId, String path, String fileMetadataId, String offset, final Promise promise) {
        if (!containsKey(STORAGE_REFERENCE_MAP.keySet(), storageReferenceId, promise)) {
            return;
        }
        try {
            File file = new File(path);
            UploadTask uploadTask;
            if ((!isEmpty(fileMetadataId)) && (isEmpty(path))) {
                if (!containsKey(FILE_METADATA_MAP.keySet(), fileMetadataId, promise)) {
                    return;
                }
                uploadTask = STORAGE_REFERENCE_MAP.get(storageReferenceId).putFile(file,
                        FILE_METADATA_MAP.get(fileMetadataId), Long.parseLong(offset));
            } else if (!isEmpty(fileMetadataId)) {
                if (!containsKey(FILE_METADATA_MAP.keySet(), fileMetadataId, promise)) {
                    return;
                }
                uploadTask = STORAGE_REFERENCE_MAP.get(storageReferenceId).putFile(file,
                        FILE_METADATA_MAP.get(fileMetadataId));
            } else {
                uploadTask = STORAGE_REFERENCE_MAP.get(storageReferenceId).putFile(file);
            }
            String uploadTaskId = getUUID(UPLOAD_TASK_MAP.keySet());
            UPLOAD_TASK_MAP.put(uploadTaskId, uploadTask);
            promise.resolve(uploadTaskId);
        } catch (Exception e) {
            StorageUtils.errorHandler(e, promise);
        }
    }

    @ReactMethod
    public void putBytes(String storageReferenceId, ReadableArray paramBytes, String fileMetadataId, String offset, Promise promise) {

        if (!containsKey(STORAGE_REFERENCE_MAP.keySet(), storageReferenceId, promise)) {
            return;
        }
        try {
            byte[] bytes = new byte[paramBytes.size()];

            for (int idx = 0; idx < paramBytes.size(); idx++) {
                bytes[idx] = (byte) paramBytes.getInt(idx);
            }

            UploadTask uploadTask;
            if (!containsKey(FILE_METADATA_MAP.keySet(), fileMetadataId, promise)) {
                return;
            }
            if (!isEmpty(offset) && isEmpty(fileMetadataId)) {
                uploadTask = STORAGE_REFERENCE_MAP.get(storageReferenceId).putBytes(bytes,
                        FILE_METADATA_MAP.get(fileMetadataId), Long.parseLong(offset));
            } else {
                uploadTask = STORAGE_REFERENCE_MAP.get(storageReferenceId).putBytes(bytes,
                        FILE_METADATA_MAP.get(fileMetadataId));
            }
            String uploadTaskId = getUUID(UPLOAD_TASK_MAP.keySet());
            UPLOAD_TASK_MAP.put(uploadTaskId, uploadTask);
            promise.resolve(uploadTaskId);
        } catch (Exception e) {
            StorageUtils.errorHandler(e, promise);
        }
    }

    @ReactMethod
    public void getFile(String storageReferenceId, String destFile, String destUri, final Promise promise) {
        if (!containsKey(STORAGE_REFERENCE_MAP.keySet(), storageReferenceId, promise)) {
            return;
        }
        try {
            DownloadTask downloadTask;
            if (!isEmpty(destFile)) {
                File file = new File(destFile);
                downloadTask = STORAGE_REFERENCE_MAP.get(storageReferenceId).getFile(file);
            } else if (!isEmpty(destUri)) {
                Uri uri = Uri.parse(destUri);
                downloadTask = STORAGE_REFERENCE_MAP.get(storageReferenceId).getFile(uri);
            } else {
                StorageUtils.errorHandler(new Exception("At least one parameter must be passed."), promise);
                return;
            }
            String downloadTaskId = getUUID(DOWNLOAD_TASK_MAP.keySet());
            DOWNLOAD_TASK_MAP.put(downloadTaskId, downloadTask);
            promise.resolve(downloadTaskId);
        } catch (Exception e) {
            StorageUtils.errorHandler(e, promise);

        }
    }

    @ReactMethod
    public void getStream(String storageReferenceId, final Promise promise) {
        if (!containsKey(STORAGE_REFERENCE_MAP.keySet(), storageReferenceId, promise)) {
            return;
        }
        try {
            StreamDownloadTask streamDownloadTask;
            streamDownloadTask = STORAGE_REFERENCE_MAP.get(storageReferenceId).getStream();
            String streamDownloadTaskId = getUUID(STREAM_DOWNLOAD_TASK_MAP.keySet());
            STREAM_DOWNLOAD_TASK_MAP.put(streamDownloadTaskId, streamDownloadTask);
            promise.resolve(streamDownloadTaskId);
        } catch (Exception e) {
            StorageUtils.errorHandler(e, promise);
        }
    }

    @ReactMethod
    public void getBytes(String storageReferenceId, String maxDownloadBytes, Promise promise) {
        if (!containsKey(STORAGE_REFERENCE_MAP.keySet(), storageReferenceId, promise)) {
            return;
        }
        try {
            Task<byte[]> taskByte = STORAGE_REFERENCE_MAP.get(storageReferenceId).getBytes(Long.parseLong(maxDownloadBytes));
            String taskByteId = getUUID(TASK_BYTE_MAP.keySet());
            TASK_BYTE_MAP.put(taskByteId, taskByte);
            promise.resolve(taskByteId);
        } catch (Exception e) {
            StorageUtils.errorHandler(e, promise);
        }
    }

    @ReactMethod
    public void getDownloadUrl(String storageReferenceId, Promise promise) {
        if (!containsKey(STORAGE_REFERENCE_MAP.keySet(), storageReferenceId, promise)) {
            return;
        }
        Task<Uri> taskUri = STORAGE_REFERENCE_MAP.get(storageReferenceId).getDownloadUrl();
        String taskUriId = getUUID(TASK_URI_MAP.keySet());
        TASK_URI_MAP.put(taskUriId, taskUri);
        promise.resolve(taskUriId);
    }

    @ReactMethod
    public void getActiveUploadTasks(String storageReferenceId, Promise promise) {
        if (!containsKey(STORAGE_REFERENCE_MAP.keySet(), storageReferenceId, promise)) {
            return;
        }
        List<UploadTask> listUploadTask = STORAGE_REFERENCE_MAP.get(storageReferenceId).getActiveUploadTasks();
        WritableArray wa = Arguments.createArray();
        String uploadTaskId;
        for (UploadTask uploadTask : listUploadTask) {
            uploadTaskId = getUUID(UPLOAD_TASK_MAP.keySet());
            UPLOAD_TASK_MAP.put(uploadTaskId, uploadTask);
            wa.pushString(uploadTaskId);
        }
        promise.resolve(wa);
    }

    @ReactMethod
    public void getActiveDownloadTasks(String storageReferenceId, Promise promise) {
        if (!containsKey(STORAGE_REFERENCE_MAP.keySet(), storageReferenceId, promise)) {
            return;
        }
        List<DownloadTask> listDownloadTask = STORAGE_REFERENCE_MAP.get(storageReferenceId).getActiveDownloadTasks();
        String downloadTaskId;
        WritableArray wa = Arguments.createArray();
        for (DownloadTask downloadTask : listDownloadTask) {
            downloadTaskId = getUUID(DOWNLOAD_TASK_MAP.keySet());
            DOWNLOAD_TASK_MAP.put(downloadTaskId, downloadTask);
            wa.pushString(downloadTaskId);
        }
        promise.resolve(wa);
    }

    @ReactMethod
    public void compareTo(String storageReferenceId, String otherStorageReferenceId, Promise promise) {
        if (!containsKey(STORAGE_REFERENCE_MAP.keySet(), storageReferenceId, promise)) {
            StorageUtils.errorHandler(new Exception("Storage Reference Not Found"), promise);
            return;
        }
        if (!containsKey(STORAGE_REFERENCE_MAP.keySet(), otherStorageReferenceId, promise)) {
            StorageUtils.errorHandler(new Exception("Other Storage Reference Not Found"), promise);
            return;
        }
        promise.resolve(STORAGE_REFERENCE_MAP.get(storageReferenceId).compareTo(STORAGE_REFERENCE_MAP.get(otherStorageReferenceId)));
    }

    @ReactMethod
    public void toString(String storageReferenceId, Promise promise) {
        if (!containsKey(STORAGE_REFERENCE_MAP.keySet(), storageReferenceId, promise)) {
            return;
        }
        promise.resolve(STORAGE_REFERENCE_MAP.get(storageReferenceId).toString());
    }

    @ReactMethod
    public void equals(String storageReferenceId, String otherStorageReferenceId, Promise promise) {
        if (!containsKey(STORAGE_REFERENCE_MAP.keySet(), storageReferenceId, promise)) {
            StorageUtils.errorHandler(new Exception("Storage Reference Not Found"), promise);
            return;
        }
        if (!containsKey(STORAGE_REFERENCE_MAP.keySet(), otherStorageReferenceId, promise)) {
            StorageUtils.errorHandler(new Exception("Other Storage Reference Not Found"), promise);
            return;
        }
        promise.resolve(STORAGE_REFERENCE_MAP.get(storageReferenceId).equals(STORAGE_REFERENCE_MAP.get(otherStorageReferenceId)));
    }

    @ReactMethod
    public void hashCode(String storageReferenceId, Promise promise) {
        if (!containsKey(STORAGE_REFERENCE_MAP.keySet(), storageReferenceId, promise)) {
            return;
        }
        promise.resolve(STORAGE_REFERENCE_MAP.get(storageReferenceId).hashCode());
    }

    @ReactMethod
    public void addListener(String eventName) {
    }

    @ReactMethod
    public void removeListeners(Integer count) {
    }
}

