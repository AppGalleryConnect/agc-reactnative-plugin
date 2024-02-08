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

package com.huawei.agc.rn.applinking.presenter;

import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.huawei.agc.rn.applinking.viewmodel.AgcAppLinkingViewModel;
import com.huawei.agconnect.applinking.AppLinking;
import com.huawei.agconnect.exception.AGCException;

/**
 * AgcAppLinkingContract defines a blueprint of {@link AppLinking} methods that will be exposed to RN Side.
 *
 * @since v.1.2.0
 */
public interface AgcAppLinkingContract {

    /**
     * Defines blueprints of {@link AgcAppLinkingViewModel} methods
     */
    interface Presenter {
        /**
         * Asynchronously generates a short link with a string-type suffix. You can specify the suffix as a long or short one.
         *
         * @param readableMap     ReadableMap instance that will get domainUriPrefix, deepLink and optionally socialCardInfo, campaignInfo, androidLinkInfo, expireMinute, linkingPreviewType.
         * @param resultListener: In the success scenario, {@link AgcAppLinkingContract.ResultListener<WritableMap>} instance, with shortLink and testUrl params, is returned, or {@link AGCException} is returned in the failure scenario.
         */
        void buildShortAppLinking(final ReadableMap readableMap, final AgcAppLinkingContract.ResultListener<WritableMap> resultListener);

        /**
         * Generates a long link Uri.
         *
         * @param readableMap     ReadableMap instance that will get domainUriPrefix, deepLink and optionally socialCardInfo, campaignInfo, androidLinkInfo, expireMinute, linkingPreviewType.
         * @param resultListener: In the success scenario, {@link AgcAppLinkingContract.ResultListener<String>} instance, with shortLink and testUrl params, is returned, or {@link AGCException} is returned in the failure scenario.
         */
        void buildLongAppLinking(final ReadableMap readableMap, final AgcAppLinkingContract.ResultListener<String> resultListener);

    }

    /**
     * ResultListener
     *
     * @param <T>: Generic Instance.
     */
    interface ResultListener<T> {
        /**
         * Presents the success scenario, Generic result instance is returned.
         *
         * @param result: Result instance.
         */
        void onSuccess(T result);

        /**
         * Presents the failure scenario, Exception instance is returned.
         *
         * @param exception: Exception instance.
         */
        void onFail(Exception exception);
    }
}
