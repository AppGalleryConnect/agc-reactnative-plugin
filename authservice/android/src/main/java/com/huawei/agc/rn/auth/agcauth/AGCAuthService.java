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

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.huawei.agc.rn.auth.utils.Handler;
import com.huawei.agc.rn.auth.utils.ReactUtils;
import com.huawei.agconnect.AGConnectInstance;
import com.huawei.agconnect.auth.AGConnectAuth;
import com.huawei.agconnect.auth.AGConnectAuthCredential;
import com.huawei.agconnect.auth.AGConnectUser;
import com.huawei.agconnect.auth.AlipayAuthProvider;
import com.huawei.agconnect.auth.EmailAuthProvider;
import com.huawei.agconnect.auth.EmailUser;
import com.huawei.agconnect.auth.FacebookAuthProvider;
import com.huawei.agconnect.auth.GoogleAuthProvider;
import com.huawei.agconnect.auth.GoogleGameAuthProvider;
import com.huawei.agconnect.auth.HWGameAuthProvider;
import com.huawei.agconnect.auth.HwIdAuthProvider;
import com.huawei.agconnect.auth.PhoneAuthProvider;
import com.huawei.agconnect.auth.PhoneUser;
import com.huawei.agconnect.auth.QQAuthProvider;
import com.huawei.agconnect.auth.SelfBuildProvider;
import com.huawei.agconnect.auth.SignInResult;
import com.huawei.agconnect.auth.TwitterAuthParam;
import com.huawei.agconnect.auth.TwitterAuthProvider;
import com.huawei.agconnect.auth.VerifyCodeResult;
import com.huawei.agconnect.auth.VerifyCodeSettings;
import com.huawei.agconnect.auth.WeiboAuthProvider;
import com.huawei.agconnect.auth.WeixinAuthProvider;
import com.huawei.agconnect.core.service.auth.OnTokenListener;
import com.huawei.hmf.tasks.Task;
import com.huawei.hmf.tasks.TaskExecutors;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class AGCAuthService {
    private final Map<String, OnTokenListener> listenerList = new HashMap<>();
    private final ReactApplicationContext reactContext;

    public AGCAuthService(ReactApplicationContext reactContext) {
        initAGConnectInstance(reactContext);
        this.reactContext = reactContext;
    }

    public void getUser(Promise promise) {
        if (AGConnectAuth.getInstance().getCurrentUser() == null) {
            promise.resolve(null);
        } else {
            AGConnectUser user = AGConnectAuth.getInstance().getCurrentUser();
            promise.resolve(ReactUtils.userToWM(user));
        }
    }

    public void deleteUser(Promise promise) {
        AGConnectAuth.getInstance().deleteUser();
        promise.resolve(null);
    }

    public void signOut(Promise promise) {
        AGConnectAuth.getInstance().signOut();
        promise.resolve(null);
    }

    public void signInAnonymously(Promise promise) {
        Task<SignInResult> task = AGConnectAuth.getInstance().signInAnonymously();
        Handler.singInResultHandler(task, promise);
    }

    public void resetPhonePassword(String countryCode, String phoneNumber, String newPassword, String verificationCode, Promise promise) {
        Task<Void> task = AGConnectAuth.getInstance().resetPassword(countryCode, phoneNumber, newPassword, verificationCode);
        Handler.voidHandler(task, promise);
    }

    public void resetEmailPassword(String email, String newPassword, String verificationCode, Promise promise) {
        Task<Void> task = AGConnectAuth.getInstance().resetPassword(email, newPassword, verificationCode);
        Handler.voidHandler(task, promise);
    }

    public void addTokenListener(String id) {
        OnTokenListener mTokenListener = tokenSnapshot -> {
            WritableMap wm = ReactUtils.tokenSnapshotToWM(tokenSnapshot);
            reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit("onTokenChanged", wm);
        };
        listenerList.put(id, mTokenListener);
        AGConnectAuth.getInstance().addTokenListener(mTokenListener);
    }

    public void removeTokenListener(String id) {
        if (!listenerList.isEmpty() && listenerList.get(id) != null) {
            OnTokenListener mTokenListener = listenerList.get(id);
            listenerList.remove(id);
            AGConnectAuth.getInstance().removeTokenListener(mTokenListener);
        }
    }

    public void requestPhoneVerifyCode(String countryCode, String phoneNumber, ReadableMap map, Promise promise) {
        VerifyCodeSettings.Builder builder = new VerifyCodeSettings.Builder();
        if (map.hasKey("action")) {
            int action = map.getInt("action");
            builder.action(action);
        }
        if (map.hasKey("locale") && !map.isNull("locale")) {
            String localeStr = map.getString("locale");
            Locale locale = ReactUtils.getLocale(localeStr);
            builder.locale(locale);
        }
        if (map.hasKey("sendInterval")) {
            int sendInterval = map.getInt("sendInterval");
            builder.sendInterval(sendInterval);
        }
        VerifyCodeSettings settings = builder.build();
        Task<VerifyCodeResult> task = AGConnectAuth.getInstance().requestVerifyCode(countryCode, phoneNumber, settings);
        task.addOnSuccessListener(TaskExecutors.uiThread(), (VerifyCodeResult verifyCodeResult) -> {
            WritableMap result = ReactUtils.verifyCodeResultToWM(verifyCodeResult);
            promise.resolve(result);
        }).addOnFailureListener(TaskExecutors.uiThread(), (Exception e) -> Handler.errorHandler(e, promise)
        );
    }

    public void requestEmailVerifyCode(String email, ReadableMap map, Promise promise) {
        VerifyCodeSettings.Builder builder = new VerifyCodeSettings.Builder();
        if (map.hasKey("action")) {
            int action = map.getInt("action");
            builder.action(action);
        }
        if (map.hasKey("sendInterval")) {
            int sendInterval = map.getInt("sendInterval");
            builder.sendInterval(sendInterval);
        }
        if (map.hasKey("locale") && !map.isNull("locale")) {
            String localeStr = map.getString("locale");
            Locale locale = ReactUtils.getLocale(localeStr);
            builder.locale(locale);
        }
        VerifyCodeSettings settings = builder.build();
        Task<VerifyCodeResult> task = AGConnectAuth.getInstance().requestVerifyCode(email, settings);
        task.addOnSuccessListener(TaskExecutors.uiThread(), (VerifyCodeResult verifyCodeResult) -> {
            WritableMap result = ReactUtils.verifyCodeResultToWM(verifyCodeResult);
            promise.resolve(result);
        }).addOnFailureListener(TaskExecutors.uiThread(), (Exception e) -> Handler.errorHandler(e, promise));
    }

    public void createPhoneUser(String countryCode, String phoneNumber, String verificationCode, String password, Promise promise) {
        PhoneUser phoneUser = new PhoneUser.Builder()
                .setCountryCode(countryCode)
                .setPhoneNumber(phoneNumber)
                .setPassword(password)
                .setVerifyCode(verificationCode)
                .build();
        Task<SignInResult> task = AGConnectAuth.getInstance().createUser(phoneUser);
        Handler.singInResultHandler(task, promise);
    }

    public void createEmailUser(String email, String verificationCode, String password, Promise promise) {
        EmailUser emailUser = new EmailUser.Builder()
                .setEmail(email)
                .setVerifyCode(verificationCode)
                .setPassword(password)
                .build();
        Task<SignInResult> task = AGConnectAuth.getInstance().createUser(emailUser);
        Handler.singInResultHandler(task, promise);
    }

    public void signIn(ReadableMap credentialMap, Promise promise) {
        int provider = credentialMap.getInt("provider");
        switch (provider) {
            case AGConnectAuthCredential.HMS_Provider:
                signInWithHuaweiID(credentialMap, promise);
                break;
            case AGConnectAuthCredential.HWGame_Provider:
                signInHuaweiGame(credentialMap, promise);
                break;
            case AGConnectAuthCredential.WeiXin_Provider:
                signInWithWeixin(credentialMap, promise);
                break;
            case AGConnectAuthCredential.Facebook_Provider:
                signInWithFacebook(credentialMap, promise);
                break;
            case AGConnectAuthCredential.Twitter_Provider:
                signInWithTwitter(credentialMap, promise);
                break;
            case AGConnectAuthCredential.WeiBo_Provider:
                signInWithWeibo(credentialMap, promise);
                break;
            case AGConnectAuthCredential.QQ_Provider:
                signInWithQQ(credentialMap, promise);
                break;
            case AGConnectAuthCredential.Google_Provider:
                signInWithGoogle(credentialMap, promise);
                break;
            case AGConnectAuthCredential.GoogleGame_Provider:
                signInWithGoogleGames(credentialMap, promise);
                break;
            case AGConnectAuthCredential.SelfBuild_Provider:
                signInWithSelfBuild(credentialMap, promise);
                break;
            case AGConnectAuthCredential.Email_Provider:
                signInWithEmail(credentialMap, promise);
                break;
            case AGConnectAuthCredential.Phone_Provider:
                signInWithPhone(credentialMap, promise);
                break;
            case AGConnectAuthCredential.Alipay_Provider:
                signInWithAlipay(credentialMap, promise);
                break;
            default:
                promise.reject("-1", "PROVIDER_IS_NOT_SUPPORTED");
        }
    }

    private void signInWithEmail(ReadableMap credentialMap, Promise promise) {
        if (credentialMap.hasKey("verificationCode")) {
            signInEmailWithVerification(credentialMap, promise);
        } else {
            signInEmailWithPassword(credentialMap, promise);
        }
    }

    private void signInEmailWithVerification(ReadableMap credentialMap, Promise promise) {
        String email = "";
        String password = null;
        String verificationCode = "";
        if (credentialMap.hasKey("email")) {
            email = credentialMap.getString("email");
        }
        if (credentialMap.hasKey("password")) {
            password = credentialMap.getString("password");
        }
        if (credentialMap.hasKey("verificationCode")) {
            verificationCode = credentialMap.getString("verificationCode");
        }
        AGConnectAuthCredential credential = EmailAuthProvider.credentialWithVerifyCode(email, password, verificationCode);
        signInTask(credential, promise);
    }

    private void signInEmailWithPassword(ReadableMap credentialMap, Promise promise) {
        String email = "";
        String password = "";
        if (credentialMap.hasKey("email")) {
            email = credentialMap.getString("email");
        }
        if (credentialMap.hasKey("password")) {
            password = credentialMap.getString("password");
        }
        AGConnectAuthCredential credential = EmailAuthProvider.credentialWithPassword(email, password);
        signInTask(credential, promise);
    }

    private void signInWithPhone(ReadableMap credentialMap, Promise promise) {
        if (credentialMap.hasKey("verificationCode")) {
            signInPhoneWithVerification(credentialMap, promise);
        } else {
            signInPhoneWithPassword(credentialMap, promise);
        }
    }

    private void signInPhoneWithVerification(ReadableMap credentailMap, Promise promise) {
        String countryCode = "";
        String phoneNumber = "";
        String password = null;
        String verificationCode = "";
        if (credentailMap.hasKey("countryCode")) {
            countryCode = credentailMap.getString("countryCode");
        }
        if (credentailMap.hasKey("phoneNumber")) {
            phoneNumber = credentailMap.getString("phoneNumber");
        }
        if (credentailMap.hasKey("password")) {
            password = credentailMap.getString("password");
        }
        if (credentailMap.hasKey("verificationCode")) {
            verificationCode = credentailMap.getString("verificationCode");
        }
        AGConnectAuthCredential credential = PhoneAuthProvider.credentialWithVerifyCode(countryCode, phoneNumber, password, verificationCode);
        signInTask(credential, promise);
    }

    private void signInPhoneWithPassword(ReadableMap credentailMap, Promise promise) {
        String countryCode = "";
        String phoneNumber = "";
        String password = "";
        if (credentailMap.hasKey("countryCode")) {
            countryCode = credentailMap.getString("countryCode");
        }
        if (credentailMap.hasKey("phoneNumber")) {
            phoneNumber = credentailMap.getString("phoneNumber");
        }
        if (credentailMap.hasKey("password")) {
            password = credentailMap.getString("password");
        }
        AGConnectAuthCredential credential = PhoneAuthProvider.credentialWithPassword(countryCode, phoneNumber, password);
        signInTask(credential, promise);
    }

    private void signInWithHuaweiID(ReadableMap credentialMap, Promise promise) {
        String accessToken = "";
        boolean autoCreateUser = true;
        if (credentialMap.hasKey("accessToken")) {
            accessToken = credentialMap.getString("accessToken");
        }
        if (credentialMap.hasKey("autoCreateUser")) {
            autoCreateUser = credentialMap.getBoolean("autoCreateUser");
        }
        AGConnectAuthCredential credential = HwIdAuthProvider.credentialWithToken(accessToken, autoCreateUser);
        signInTask(credential, promise);
    }

    private void signInHuaweiGame(ReadableMap map, Promise promise) {
        HWGameAuthProvider.Builder builder = new HWGameAuthProvider.Builder();
        if (map.hasKey("playerSign")) {
            String playerSign = map.getString("playerSign");
            builder.setPlayerSign(playerSign);
        }
        if (map.hasKey("playerId")) {
            String playerId = map.getString("playerId");
            builder.setPlayerId(playerId);
        }
        if (map.hasKey("displayName")) {
            String displayName = map.getString("displayName");
            builder.setDisplayName(displayName);
        }
        if (map.hasKey("imageUrl")) {
            String imageUrl = map.getString("imageUrl");
            builder.setImageUrl(imageUrl);
        }
        if (map.hasKey("playerLevel")) {
            int playerLevel = map.getInt("playerLevel");
            builder.setPlayerLevel(playerLevel);
        }
        if (map.hasKey("signTs")) {
            String signTs = map.getString("signTs");
            builder.setSignTs(signTs);
        }
        if (map.hasKey("autoCreateUser")) {
            boolean autoCreateUser = map.getBoolean("autoCreateUser");
            builder.setAutoCreateUser(autoCreateUser);
        }
        AGConnectAuthCredential credential = builder.build();
        signInTask(credential, promise);
    }

    private void signInWithWeixin(ReadableMap credentialMap, Promise promise) {
        String accessToken = "";
        String openId = "";
        boolean autoCreateUser = true;
        if (credentialMap.hasKey("accessToken")) {
            accessToken = credentialMap.getString("accessToken");
        }
        if (credentialMap.hasKey("openId")) {
            openId = credentialMap.getString("openId");
        }
        if (credentialMap.hasKey("autoCreateUser")) {
            autoCreateUser = credentialMap.getBoolean("autoCreateUser");
        }
        AGConnectAuthCredential credential = WeixinAuthProvider.credentialWithToken(accessToken, openId, autoCreateUser);
        signInTask(credential, promise);
    }

    private void signInWithFacebook(ReadableMap credentialMap, Promise promise) {
        String accessToken = "";
        boolean autoCreateUser = true;
        if (credentialMap.hasKey("accessToken")) {
            accessToken = credentialMap.getString("accessToken");
        }
        if (credentialMap.hasKey("autoCreateUser")) {
            autoCreateUser = credentialMap.getBoolean("autoCreateUser");
        }
        AGConnectAuthCredential credential = FacebookAuthProvider.credentialWithToken(accessToken, autoCreateUser);
        signInTask(credential, promise);
    }

    private void signInWithTwitter(ReadableMap credentialMap, Promise promise) {

        boolean autoCreateUser = true;
        if (credentialMap.hasKey("autoCreateUser")) {
            autoCreateUser = credentialMap.getBoolean("autoCreateUser");
        }
        AGConnectAuthCredential credential;
        if (credentialMap.getInt("version") == 1) {
            String token = "";
            String secret = "";
            if (credentialMap.hasKey("token")) {
                token = credentialMap.getString("token");
            }
            if (credentialMap.hasKey("secret")) {
                secret = credentialMap.getString("secret");
            }

            credential = TwitterAuthProvider.credentialWithToken(token, secret, autoCreateUser);
        } else {
            TwitterAuthParam twitterAuthParam = null;
            if (credentialMap.hasKey("twitterAuthParam")) {
                twitterAuthParam = authParamHandler(Objects.requireNonNull(credentialMap.getMap("twitterAuthParam")));
            }
            credential = TwitterAuthProvider.credentialWithAuthCode(twitterAuthParam, autoCreateUser);
        }

        signInTask(credential, promise);
    }

    private TwitterAuthParam authParamHandler(ReadableMap authParamMap) {
        String clientId = authParamMap.getString("clientId");
        String authCode = authParamMap.getString("authCode");
        String codeVerifier = authParamMap.getString("codeVerifier");
        String redirectUrl = authParamMap.getString("redirectUrl");
        return new TwitterAuthParam(clientId, authCode, codeVerifier, redirectUrl);
    }

    private void signInWithWeibo(ReadableMap credentialMap, Promise promise) {
        String token = "";
        String uid = "";
        boolean autoCreateUser = true;
        if (credentialMap.hasKey("token")) {
            token = credentialMap.getString("token");
        }
        if (credentialMap.hasKey("uid")) {
            uid = credentialMap.getString("uid");
        }
        if (credentialMap.hasKey("autoCreateUser")) {
            autoCreateUser = credentialMap.getBoolean("autoCreateUser");
        }
        AGConnectAuthCredential credential = WeiboAuthProvider.credentialWithToken(token, uid, autoCreateUser);
        signInTask(credential, promise);
    }

    private void signInWithQQ(ReadableMap credentialMap, Promise promise) {
        String openId = "";
        String accessToken = "";
        boolean autoCreateUser = true;
        if (credentialMap.hasKey("openId")) {
            openId = credentialMap.getString("openId");
        }
        if (credentialMap.hasKey("accessToken")) {
            accessToken = credentialMap.getString("accessToken");
        }
        if (credentialMap.hasKey("autoCreateUser")) {
            autoCreateUser = credentialMap.getBoolean("autoCreateUser");
        }
        AGConnectAuthCredential credential = QQAuthProvider.credentialWithToken(accessToken, openId, autoCreateUser);
        signInTask(credential, promise);
    }

    private void signInWithGoogle(ReadableMap credentialMap, Promise promise) {
        String idToken = "";
        boolean autoCreateUser = true;
        if (credentialMap.hasKey("idToken")) {
            idToken = credentialMap.getString("idToken");
        }
        if (credentialMap.hasKey("autoCreateUser")) {
            autoCreateUser = credentialMap.getBoolean("autoCreateUser");
        }
        AGConnectAuthCredential credential = GoogleAuthProvider.credentialWithToken(idToken, autoCreateUser);
        signInTask(credential, promise);
    }

    private void signInWithGoogleGames(ReadableMap credentialMap, Promise promise) {
        String serverAuthCode = "";
        boolean autoCreateUser = true;
        if (credentialMap.hasKey("serverAuthCode")) {
            serverAuthCode = credentialMap.getString("serverAuthCode");
        }
        if (credentialMap.hasKey("autoCreateUser")) {
            autoCreateUser = credentialMap.getBoolean("autoCreateUser");
        }
        AGConnectAuthCredential credential = GoogleGameAuthProvider.credentialWithToken(serverAuthCode, autoCreateUser);
        signInTask(credential, promise);
    }

    private void signInWithSelfBuild(ReadableMap credentialMap, Promise promise) {
        String token = "";
        boolean autoCreateUser = true;
        if (credentialMap.hasKey("token")) {
            token = credentialMap.getString("token");
        }
        if (credentialMap.hasKey("autoCreateUser")) {
            autoCreateUser = credentialMap.getBoolean("autoCreateUser");
        }
        AGConnectAuthCredential credential = SelfBuildProvider.credentialWithToken(token, autoCreateUser);
        signInTask(credential, promise);
    }

    private void signInWithAlipay(ReadableMap credentialMap, Promise promise) {
        String authCode = "";
        boolean autoCreateUser = true;
        if (credentialMap.hasKey("authCode")) {
            authCode = credentialMap.getString("authCode");
        }
        if (credentialMap.hasKey("autoCreateUser")) {
            autoCreateUser = credentialMap.getBoolean("autoCreateUser");
        }
        AGConnectAuthCredential credential = AlipayAuthProvider.credentialWithAuthCode(authCode, autoCreateUser);
        signInTask(credential, promise);
    }

    private void signInTask(AGConnectAuthCredential credential, Promise promise) {
        Task<SignInResult> task = AGConnectAuth.getInstance().signIn(credential);
        Handler.singInResultHandler(task, promise);
    }

    private void initAGConnectInstance(ReactApplicationContext reactApplicationContext) {
        if (AGConnectInstance.getInstance() == null) {
            AGConnectInstance.initialize(reactApplicationContext);
        }
    }
}
