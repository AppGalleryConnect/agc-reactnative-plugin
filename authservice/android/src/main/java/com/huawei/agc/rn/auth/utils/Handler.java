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

import com.facebook.react.bridge.Promise;
import com.huawei.agconnect.auth.AGCAuthException;
import com.huawei.agconnect.auth.AGConnectUser;
import com.huawei.agconnect.auth.SignInResult;
import com.huawei.hmf.tasks.Task;

public class Handler {
    public static void voidHandler(Task<Void> task, Promise promise) {
        task.addOnSuccessListener(aVoid -> {
            promise.resolve(null);
        }).addOnFailureListener(e -> {
            errorHandler(e, promise);
        });
    }

    public static void singInResultHandler(Task<SignInResult> task, Promise promise) {
        task.addOnSuccessListener(signInResult -> {
            AGConnectUser user = signInResult.getUser();
            promise.resolve(ReactUtils.userToWM(user));
        }).addOnFailureListener(e -> {
            errorHandler(e, promise);
        });
    }

    public static void errorHandler(Exception e, Promise promise) {
        if (e instanceof AGCAuthException) {
            String message = ((AGCAuthException) e).getErrMsg();
            String code = String.valueOf(((AGCAuthException) e).getCode());
            promise.reject(code, message);
        }
        promise.reject(e);
    }
}
