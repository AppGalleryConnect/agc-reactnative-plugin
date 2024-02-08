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

package com.huawei.agc.rn.auth.agcauth;

import com.huawei.agconnect.auth.AGConnectAuthCredential;
import com.huawei.agconnect.auth.VerifyCodeSettings;

import java.util.HashMap;
import java.util.Map;

public class AGCAuthConstants {
    public static  Map<String, Object> getConstants() {
        Map<String, Object> constants = new HashMap<>();
        constants.put("action", getActions());
        constants.put("provider", getProviders());
        return constants;
    }

    private static Map<String, Object> getProviders() {
        Map<String, Object> map = new HashMap<>();
        map.put("ANONYMOUS", AGConnectAuthCredential.Anonymous);
        map.put("HMS_Provider", AGConnectAuthCredential.HMS_Provider);
        map.put("Facebook_Provider", AGConnectAuthCredential.Facebook_Provider);
        map.put("Twitter_Provider", AGConnectAuthCredential.Twitter_Provider);
        map.put("WeiXin_Provider", AGConnectAuthCredential.WeiXin_Provider);
        map.put("HWGame_Provider", AGConnectAuthCredential.HWGame_Provider);
        map.put("QQ_Provider", AGConnectAuthCredential.QQ_Provider);
        map.put("WeiBo_Provider", AGConnectAuthCredential.WeiBo_Provider);
        map.put("Google_Provider", AGConnectAuthCredential.Google_Provider);
        map.put("GoogleGame_Provider", AGConnectAuthCredential.GoogleGame_Provider);
        map.put("SelfBuild_Provider", AGConnectAuthCredential.SelfBuild_Provider);
        map.put("Phone_Provider", AGConnectAuthCredential.Phone_Provider);
        map.put("Email_Provider", AGConnectAuthCredential.Email_Provider);
        map.put("Apple_Provider", 13);
        map.put("Alipay_Provider", AGConnectAuthCredential.Alipay_Provider);
        return map;
    }

    private static Map<String, Object> getActions() {
        Map<String, Object> actions = new HashMap<>();
        actions.put("ACTION_REGISTER_LOGIN", VerifyCodeSettings.ACTION_REGISTER_LOGIN);
        actions.put("ACTION_RESET_PASSWORD", VerifyCodeSettings.ACTION_RESET_PASSWORD);
        return actions;
    }
}
