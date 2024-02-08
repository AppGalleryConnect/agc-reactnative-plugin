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
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.huawei.agconnect.AGConnectInstance;

/**
 * AGCCloudFunctionsModule class is the tool class of AGConnectFunction.
 *
 * @since v.1.4.2.301
 */
public class AGCCloudFunctionsModule extends ReactContextBaseJavaModule {
    private CloudFunctionsService cloudFunctionsService;

    public AGCCloudFunctionsModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.cloudFunctionsService = new CloudFunctionsService(reactContext);
        if (AGConnectInstance.getInstance() == null) {
            AGConnectInstance.initialize(reactContext);
        }
    }

    /**
     * Here we will call this AGCCloudFunctionsModule so that
     * we can access it through
     * React.NativeModules.AGCCloudFunctionsModule in RN Side.
     *
     * @return name
     */
    @Override
    public String getName() {
        return "AGCCloudFunctionsModule";
    }

    /**
     * Creates a function callable instance, sets its configuration and calls the cloud functions
     *
     * @param triggerIdentifier: HTTP trigger identifier of the cloud function to be called.
     * @param options: ReadableMap that contains AGCFunctionCallable instance configurations and cloud functions parameters
     * @param promise: In the success scenario, post request response will be returned as WritableMap instance, or AGCFunctionException will be returned in the failure scenario.
     */
    @ReactMethod
    public void call(String triggerIdentifier, ReadableMap options, Promise promise) {
        cloudFunctionsService.call(triggerIdentifier, options, promise);
    }

}
