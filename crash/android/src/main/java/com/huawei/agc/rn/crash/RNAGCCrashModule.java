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

package com.huawei.agc.rn.crash;

import android.os.Handler;
import android.os.Looper;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.huawei.agconnect.AGConnectInstance;
import com.huawei.agconnect.crash.AGConnectCrash;

import java.util.Objects;

public class RNAGCCrashModule extends ReactContextBaseJavaModule {
    private final ReactApplicationContext reactContext;
    private AGConnectCrash instance;

    public RNAGCCrashModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
        if (AGConnectInstance.getInstance() == null) {
            AGConnectInstance.initialize(reactContext);
        }
        instance = AGConnectCrash.getInstance();
    }

    @Override
    public String getName() {
        return "RNCrash";
    }

    @ReactMethod
    public void testIt() {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                instance.testIt(reactContext);
            }
        });
    }

    @ReactMethod
    public void enableCrashCollection(boolean enable, Promise promise) {
        instance.enableCrashCollection(enable);
        promise.resolve(null);
    }

    @ReactMethod
    public void setUserId(String userId, Promise promise) {
        instance.setUserId(userId);
        promise.resolve(null);
    }

    @ReactMethod
    public void setCustomKey(String key, String value, Promise promise) {
        instance.setCustomKey(key, value);
        promise.resolve(null);
    }

    @ReactMethod
    public void logWithLevel(int level, String message, Promise promise) {
        instance.log(level, message);
        promise.resolve(null);
    }

    @ReactMethod
    public void log(String message, Promise promise) {
        instance.log(message);
        promise.resolve(null);
    }

    @ReactMethod
    public void recordError(ReadableMap errorMap, Promise promise) {
        record(errorMap, false);
        promise.resolve(null);
    }

    @ReactMethod
    public void recordFatalError(ReadableMap errorMap, Promise promise) {
        record(errorMap, true);
        promise.resolve(null);
    }

    private void record(ReadableMap errorMap, boolean fatal) {
        String message = errorMap.getString("message");
        ReadableArray stackFrames = Objects.requireNonNull(errorMap.getArray("frames"));
        boolean isPromiseReject = errorMap.getBoolean("isPromiseReject");
        Exception exception = isPromiseReject ? new PromiseRejectError(message) : new JavaScriptError(message);
        StackTraceElement[] stackTraceElements = new StackTraceElement[stackFrames.size()];
        for (int i = 0; i < stackFrames.size(); i++) {
            ReadableMap stackFrame = Objects.requireNonNull(stackFrames.getMap(i));
            String function = stackFrame.getString("function");
            String file = stackFrame.getString("file");
            stackTraceElements[i] = new StackTraceElement("", function, file, -1);
        }
        exception.setStackTrace(stackTraceElements);
        if (fatal) {
            instance.recordFatalException(exception);
        } else {
            instance.recordException(exception);
        }
    }

    @ReactMethod
    public void addListener(String eventName) {
    }
    @ReactMethod
    public void removeListeners(Integer count) {
    }

}
