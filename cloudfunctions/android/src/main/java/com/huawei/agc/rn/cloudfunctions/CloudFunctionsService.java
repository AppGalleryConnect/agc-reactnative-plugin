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

package com.huawei.agc.rn.cloudfunctions;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.huawei.agc.rn.cloudfunctions.utils.ReactUtils;
import com.huawei.agc.rn.cloudfunctions.utils.TimeUnitConverter;
import com.huawei.agconnect.AGConnectInstance;
import com.huawei.agconnect.function.AGCFunctionException;
import com.huawei.agconnect.function.AGConnectFunction;
import com.huawei.agconnect.function.FunctionCallable;
import com.huawei.agconnect.function.FunctionResult;
import com.huawei.hmf.tasks.Task;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

public class CloudFunctionsService {
    public CloudFunctionsService(ReactApplicationContext reactApplicationContext) {
        initAGConnectInstance(reactApplicationContext);
    }

    /**
     * Creates a function callable instance, sets its configuration and calls the cloud functions
     *
     * @param triggerIdentifier: HTTP trigger identifier of the cloud function to be called.
     * @param options: ReadableMap that contains AGCFunctionCallable instance configurations and cloud functions parameters
     * @param promise: In the success scenario, post request response will be returned as WritableMap instance, or AGCFunctionException will be returned in the failure scenario.
     */
    public void call(String triggerIdentifier, ReadableMap options, Promise promise) {
        FunctionCallable functionCallable = AGConnectFunction.getInstance().wrap(triggerIdentifier);
        if (options.hasKey("timeout")) {
            long timeout = (long) options.getDouble("timeout");
            if(options.hasKey("timeUnit")) {
                TimeUnit timeUnit = TimeUnitConverter.getTimeUnit(options.getInt("timeUnit"));
                functionCallable.setTimeout(timeout, timeUnit);
            }else {
                functionCallable.setTimeout(timeout, TimeUnit.SECONDS);
            }
        }

        if(options.hasKey("params")){
            ReadableMap map = options.getMap("params");
            callFunction(functionCallable, map, promise);
        }else {
            callFunction(functionCallable, promise);
        }
    }

    // MARK: - Private Helper Methods

    /**
     * Creates Task<FunctionResult> without input parameters.
     *
     * @param functionCallable: AGCFunctionCallable instance that is used to call cloud function
     * @param promise: In the success scenario, post request response will be returned as WritableMap instance, or AGCFunctionException will be returned in the failure scenario.
     */
    private void callFunction(FunctionCallable functionCallable, Promise promise) {
        Task<FunctionResult> task = functionCallable.call();
        taskHandler(task, promise);
    }

    /**
     * Creates Task<FunctionResult> with input parameters.
     *
     * @param functionCallable: AGCFunctionCallable instance that is used to call cloud function
     * @param map: ReadableMap instance that contains input parameter values of the function.
     * @param promise: In the success scenario, post request response will be returned as WritableMap instance, or AGCFunctionException will be returned in the failure scenario.
     */
    private void callFunction(FunctionCallable functionCallable, ReadableMap map, Promise promise) {
        Task<FunctionResult> task = functionCallable.call(ReactUtils.toMap(map));
        taskHandler(task, promise);
    }

    /**
     * Handles Task<FunctionResult> onCompleteListener.
     *
     * @param task: Task<FunctionResult> instance that will be handled.
     * @param promise: In the success scenario, post request response will be returned as WritableMap instance, or AGCFunctionException will be returned in the failure scenario.
     */
    private void taskHandler(Task<FunctionResult> task, Promise promise) {
        task.addOnCompleteListener(functionResultTask -> {
            if (functionResultTask.isSuccessful()) {
                String value = functionResultTask.getResult().getValue();
                try {
                    JSONObject json = new JSONObject(value);
                    WritableMap response = ReactUtils.toWM(json);
                    promise.resolve(response);
                } catch (JSONException e) {
                    promise.reject(e.getMessage());
                }
            } else {
                Exception e = functionResultTask.getException();
                if (e instanceof AGCFunctionException) {
                    AGCFunctionException functionException = (AGCFunctionException) e;
                    String message = functionException.getMessage();
                    promise.reject(message);
                }
            }
        });
    }

    /**
     * Initialize AGConnect Instance
     */
    private void initAGConnectInstance(ReactApplicationContext reactApplicationContext) {
        if (AGConnectInstance.getInstance() == null) {
            AGConnectInstance.initialize(reactApplicationContext);
        }
    }
}
