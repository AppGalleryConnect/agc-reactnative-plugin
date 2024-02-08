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

package com.huawei.agc.rn.applinking;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.huawei.agc.rn.applinking.presenter.AgcAppLinkingContract;
import com.huawei.agc.rn.applinking.util.AgcAppLinkingDataUtils;
import com.huawei.agc.rn.applinking.viewmodel.AgcAppLinkingViewModel;
import com.huawei.agconnect.AGConnectInstance;
import com.huawei.agconnect.applinking.AGConnectAppLinking;
import com.huawei.agconnect.applinking.AppLinking;
import com.huawei.agconnect.applinking.ResolvedLinkData;
import com.huawei.agconnect.exception.AGCException;

import java.lang.ref.WeakReference;

/**
 * AgcAppLinkingModule class is the tool class of {@link AppLinking}.
 *
 * @since v.1.2.0
 */
public class AgcAppLinkingModule extends ReactContextBaseJavaModule {

    private final AgcAppLinkingContract.Presenter viewModel;

    public AgcAppLinkingModule(ReactApplicationContext reactContext) {
        super(reactContext);

        WeakReference<ReactContext> weakContext = new WeakReference<>(reactContext);
        viewModel = new AgcAppLinkingViewModel();
        if (AGConnectInstance.getInstance() == null) {
            AGConnectInstance.initialize(weakContext.get());
        }
    }

    /**
     * Here we will call this AgcAppLinking so that
     * we can access it through
     * React.NativeModules.AgcAppLinking in RN Side.
     *
     * @return name
     */
    @Override
    public String getName() {
        return "AgcAppLinking";
    }

    /**
     * Asynchronously generates a short link with a string-type suffix. You can specify the suffix as a long or short one.
     *
     * @param readableMap ReadableMap instance that will get domainUriPrefix, deepLink and socialCardInfo, campaignInfo, androidLinkInfo, expireMinute, linkingPreviewType.
     * @param promise:    In the success scenario, {@link WritableMap} instance, with shortLink and testUrl params, is returned, or {@link AGCException} is returned in the failure scenario.
     */
    @ReactMethod
    public void buildShortAppLinking(final ReadableMap readableMap, final Promise promise) {
        viewModel.buildShortAppLinking(readableMap, new AgcAppLinkingModule.AppLinkingResultHandler(promise));
    }

    /**
     * Generates a long link Uri.
     *
     * @param readableMap ReadableMap instance that will get domainUriPrefix, deepLink and socialCardInfo, campaignInfo, androidLinkInfo, expireMinute, linkingPreviewType.
     * @param promise:    In the success scenario, {@link WritableMap} instance, with shortLink and testUrl params, is returned, or {@link AGCException} is returned in the failure scenario.
     */
    @ReactMethod
    public void buildLongAppLinking(final ReadableMap readableMap, final Promise promise) {
        viewModel.buildLongAppLinking(readableMap, new AgcAppLinkingModule.AppLinkingResultHandler(promise));
    }

    /**
     * Fetches ResolvedLinkData instance with deepLink, clickTimeStamp, socialTitle, socialDescription, socialImageUrl, campaignName, campaignMedium, campaignSource and minimumAppVersion params.
     *
     * @param promise: In the success scenario, {@link WritableMap} instance, with {@link ResolvedLinkData} params, is returned, or {@link AGCException} is returned in the failure scenario.
     */
    @ReactMethod
    public void getAGConnectAppLinkingResolvedLinkData(final Promise promise) {
        AGConnectAppLinking.getInstance().getAppLinking(getCurrentActivity()).addOnSuccessListener(resolvedLinkData -> promise.resolve(AgcAppLinkingDataUtils.createLongLink(resolvedLinkData))).addOnFailureListener(error -> promise.reject(error));
    }

    /* Private Inner Class */

    /**
     * AppLinkingResultHandler static nested class is a helper class for reaching {@link AgcAppLinkingContract.ResultListener}.
     */
    private static final class AppLinkingResultHandler implements AgcAppLinkingContract.ResultListener {

        private Promise promise;

        AppLinkingResultHandler(final Promise promise) {
            this.promise = promise;
        }

        @Override
        public void onSuccess(Object result) {
            promise.resolve(result);
        }

        @Override
        public void onFail(Exception exception) {
            promise.reject(exception);
        }
    }
}
