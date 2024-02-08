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

package com.huawei.agc.rn.config;

import android.util.Log;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.huawei.agconnect.AGConnectInstance;
import com.huawei.agconnect.exception.AGCException;
import com.huawei.agconnect.remoteconfig.AGCConfigException;
import com.huawei.agconnect.remoteconfig.AGConnectConfig;
import com.huawei.agconnect.remoteconfig.ConfigValues;
import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hmf.tasks.Task;
import com.huawei.hmf.tasks.TaskExecutors;

import java.util.HashMap;
import java.util.Map;

public class RNAGCConfigModule extends ReactContextBaseJavaModule {
    private static final String TAG = "RNAGCConfigModule";
    private final ReactApplicationContext reactContext;
    private AGConnectConfig instance;

    public RNAGCConfigModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
        if (AGConnectInstance.getInstance() == null) {
            AGConnectInstance.initialize(reactContext);
        }
        instance = AGConnectConfig.getInstance();
    }

    @Override
    public String getName() {
        return "RNRemoteConfig";
    }

    @ReactMethod
    public void applyDefault(ReadableMap readableMap) {
        if (readableMap != null) {
            Map<String, Object> map = readableMap.toHashMap();
            instance.applyDefault(readableMap.toHashMap());
        }
    }

    @ReactMethod
    public void applyLastFetched() {
        instance.apply(instance.loadLastFetched());
    }

    @ReactMethod
    public void fetchDefault(Promise promise) {
        fetch2Java(instance.fetch(), promise);
    }

    @ReactMethod
    public void fetch(double intervalSeconds, Promise promise) {
        fetch2Java(instance.fetch((long) intervalSeconds), promise);
    }

    private void fetch2Java(Task<ConfigValues> task, Promise promise) {
        task.addOnSuccessListener(TaskExecutors.immediate(), new OnSuccessListener<ConfigValues>() {
            @Override
            public void onSuccess(ConfigValues configValues) {
                promise.resolve(null);
            }
        }).addOnFailureListener(TaskExecutors.immediate(), new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                Log.e(TAG, "call func fetch error:" + e);
                if (e instanceof AGCConfigException) {
                    int code = ((AGCConfigException) e).getCode();
                    long throttleTime = ((AGCConfigException) e).getThrottleEndTimeMillis();
                    Map<String, Object> map = new HashMap<>();
                    map.put("throttleEndTime", throttleTime);
                    map.put("code", code);
                    map.put("message", e.getMessage());
                    WritableMap writableMap = Arguments.makeNativeMap(map);
                    promise.reject(String.valueOf(code),
                            e.getMessage(),
                            e,
                            writableMap);
                } else if (e instanceof AGCException) {
                    promise.reject(String.valueOf(((AGCException) e).getCode()),
                            ((AGCException) e).getErrMsg(),
                            e);
                } else {
                    promise.reject("", "", e);
                }
            }
        });
    }

    @ReactMethod
    public void getValue(String key, Promise promise) {
        promise.resolve(instance.getValueAsString(key));
    }

    @ReactMethod
    public void getSource(String key, Promise promise) {
        promise.resolve(instance.getSource(key).ordinal());
    }

    @ReactMethod
    public void getMergedAll(Promise promise) {
        Map<String, Object> map = instance.getMergedAll();
        WritableMap resultMap = Arguments.makeNativeMap(map);
        promise.resolve(resultMap);
    }

    @ReactMethod
    public void clearAll() {
        instance.clearAll();
    }

    @ReactMethod
    public void setDeveloperMode(boolean isDeveloperMode) {
        instance.setDeveloperMode(isDeveloperMode);
    }

    /// 返回获取自定义属性
    @ReactMethod
    public void getCustomAttributes(Promise promise) {
        Map map = instance.getCustomAttributes(); 
        WritableMap resultMap = Arguments.makeNativeMap(map);
        promise.resolve(resultMap);
    }


    /// 设置自定义属性参数
    @ReactMethod
    public void setCustomAttributes(ReadableMap customAttributes) {
         if (customAttributes != null) {
             Map<String, Object> map = customAttributes.toHashMap();
             Map<String, String> hashMap = new HashMap(map);
            instance.setCustomAttributes(hashMap);
        }
    }

    @ReactMethod
    public void addListener(String eventName) {
    }
    @ReactMethod
    public void removeListeners(Integer count) {
    }
}
