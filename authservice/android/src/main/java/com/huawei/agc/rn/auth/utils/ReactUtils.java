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

package com.huawei.agc.rn.auth.utils;

import android.text.TextUtils;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.huawei.agconnect.auth.AGConnectUser;
import com.huawei.agconnect.auth.AGConnectUserExtra;
import com.huawei.agconnect.auth.TokenResult;
import com.huawei.agconnect.auth.VerifyCodeResult;
import com.huawei.agconnect.core.service.auth.TokenSnapshot;

import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ReactUtils {
    public static WritableMap verifyCodeResultToWM(VerifyCodeResult verifyCodeResult) {
        WritableMap map = Arguments.createMap();
        map.putString("shortestInterval", verifyCodeResult.getShortestInterval());
        map.putString("validityPeriod", verifyCodeResult.getValidityPeriod());
        return map;
    }

    public static WritableMap userToWM(AGConnectUser user) {
        WritableMap userMap = Arguments.createMap();
        userMap.putString("phoneNumber", user.getPhone());
        userMap.putString("displayName", user.getDisplayName());
        userMap.putString("email", user.getEmail());
        userMap.putInt("emailVerified", user.getEmailVerified());
        userMap.putInt("passwordSetted", user.getPasswordSetted());
        userMap.putString("photoUrl", user.getPhotoUrl());
        userMap.putString("providerId", user.getProviderId());
        userMap.putString("uid", user.getUid());
        userMap.putBoolean("isAnonymous", user.isAnonymous());

        List<Map<String, String>> providerInfo = user.getProviderInfo();
        if (providerInfo != null && providerInfo.size() > 0) {
            WritableArray providerInfoArray = Arguments.createArray();
            for (int i = 0; i < providerInfo.size(); i++) {
                Map<String, String> map = providerInfo.get(i);
                if (map != null) {
                    WritableMap writableMap = Arguments.createMap();
                    for (Map.Entry<String, String> entry : map.entrySet()) {
                        writableMap.putString(entry.getKey(), entry.getValue());
                    }
                    providerInfoArray.pushMap(writableMap);
                }
            }
            userMap.putArray("providerInfo", providerInfoArray);
        }

        return userMap;
    }

    public static WritableMap tokenToWM(TokenResult tokenResult) {
        WritableMap map = Arguments.createMap();
        map.putString("token", tokenResult.getToken());
        map.putDouble("expirePeriod", tokenResult.getExpirePeriod());
        return map;
    }

    public static WritableMap tokenSnapshotToWM(TokenSnapshot tokenSnapshot) {
        WritableMap map = Arguments.createMap();
        map.putInt("state", tokenSnapshot.getState().ordinal());
        map.putString("token", tokenSnapshot.getToken());
        return map;
    }

    public static WritableMap userExtraToWM(AGConnectUserExtra agConnectUserExtra) {
        WritableMap map = Arguments.createMap();
        map.putString("createTime", agConnectUserExtra.getCreateTime());
        map.putString("lastSignInTime", agConnectUserExtra.getLastSignInTime());
        return map;
    }

    public static Locale getLocale(String lang) {
        Locale locale = null;
        if (!TextUtils.isEmpty(lang)) {
            String[] locales = lang.split("_");
            if (locales.length == 2) {
                if (!TextUtils.isEmpty(locales[0]) && !TextUtils.isEmpty(locales[1])) {
                    locale = new Locale(locales[0], locales[1]);
                }
            }
        }
        return locale;
    }
}
