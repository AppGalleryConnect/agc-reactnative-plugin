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

package com.huawei.agc.rn.applinking.util;

import android.util.Log;

import com.facebook.react.bridge.WritableMap;
import com.huawei.agconnect.applinking.ResolvedLinkData;
import com.huawei.agconnect.applinking.ShortAppLinking;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

/**
 * {@link AgcAppLinkingDataUtils} simply converts Objects into a WritableMap instances, so that RN Side can read the objects with proper keys.
 *
 * @since v.1.2.0
 */
public enum AgcAppLinkingDataUtils {
    INSTANCE;
    public static final String TAG = AgcAppLinkingDataUtils.class.getSimpleName();


    public synchronized static WritableMap createShortLink(ShortAppLinking shortAppLinking) {
        Log.d(TAG, "call -> createShortLink");

        WritableMap writableMap = null;
        try {
            JSONObject result = new JSONObject();
            result.put("shortLink", shortAppLinking.getShortUrl().toString());
            result.put("testUrl", shortAppLinking.getTestUrl().toString());
            writableMap = MapUtils.toWritableMap(result);
        } catch (JSONException e) {
            Log.e(TAG, Objects.requireNonNull(e.getMessage()));
        }
        return writableMap;
    }

    public synchronized static WritableMap createLongLink(ResolvedLinkData resolvedLinkData) {
        Log.d(TAG, "call -> createLongLink");

        WritableMap writableMap = null;
        try {
            JSONObject result = new JSONObject();
            result.put("deepLink", resolvedLinkData.getDeepLink().toString());
            result.put("clickTimeStamp", resolvedLinkData.getClickTimestamp());
            result.put("socialTitle", resolvedLinkData.getSocialTitle());
            result.put("socialDescription", resolvedLinkData.getSocialDescription());
            result.put("socialImageUrl", resolvedLinkData.getSocialImageUrl());
            result.put("campaignName", resolvedLinkData.getCampaignName());
            result.put("campaignMedium", resolvedLinkData.getCampaignMedium());
            result.put("campaignSource", resolvedLinkData.getCampaignSource());
            result.put("minimumAppVersion", resolvedLinkData.getMinimumAppVersion());
            writableMap = MapUtils.toWritableMap(result);
        } catch (JSONException e) {
            Log.e(TAG, Objects.requireNonNull(e.getMessage()));
        }
        return writableMap;
    }
}
