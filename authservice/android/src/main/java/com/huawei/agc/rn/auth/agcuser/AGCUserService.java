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

package com.huawei.agc.rn.auth.agcuser;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReadableMap;
import com.huawei.agc.rn.auth.utils.Handler;
import com.huawei.agc.rn.auth.utils.ReactUtils;
import com.huawei.agconnect.auth.AGCAuthException;
import com.huawei.agconnect.auth.AGConnectAuth;
import com.huawei.agconnect.auth.AGConnectAuthCredential;
import com.huawei.agconnect.auth.EmailAuthProvider;
import com.huawei.agconnect.auth.FacebookAuthProvider;
import com.huawei.agconnect.auth.GoogleAuthProvider;
import com.huawei.agconnect.auth.GoogleGameAuthProvider;
import com.huawei.agconnect.auth.HWGameAuthProvider;
import com.huawei.agconnect.auth.HwIdAuthProvider;
import com.huawei.agconnect.auth.PhoneAuthProvider;
import com.huawei.agconnect.auth.ProfileRequest;
import com.huawei.agconnect.auth.QQAuthProvider;
import com.huawei.agconnect.auth.SelfBuildProvider;
import com.huawei.agconnect.auth.SignInResult;
import com.huawei.agconnect.auth.TwitterAuthProvider;
import com.huawei.agconnect.auth.WeiboAuthProvider;
import com.huawei.agconnect.auth.WeixinAuthProvider;
import com.huawei.hmf.tasks.Task;

import java.util.Locale;

public class AGCUserService {
    private int userNullErrorCode = AGCAuthException.NOT_SIGN_IN;

    public void updatePhone(String countryCode, String newPhoneNumber, String verificationCode, Promise promise) {
        if (AGConnectAuth.getInstance().getCurrentUser() == null) {
            promise.reject(String.valueOf(userNullErrorCode), AGCAuthException.getMsgByCode(userNullErrorCode));
        } else {
            Task<Void> task = AGConnectAuth.getInstance().getCurrentUser().updatePhone(countryCode, newPhoneNumber, verificationCode);
            Handler.voidHandler(task, promise);
        }
    }

    public void updatePhoneWithLocale(String countryCode, String newPhoneNumber, String verificationCode, String locale, Promise promise) {
        if (AGConnectAuth.getInstance().getCurrentUser() == null) {
            promise.reject(String.valueOf(userNullErrorCode), AGCAuthException.getMsgByCode(userNullErrorCode));
        } else {
            Locale lang = ReactUtils.getLocale(locale);
            Task<Void> task = AGConnectAuth.getInstance().getCurrentUser().updatePhone(countryCode, newPhoneNumber, verificationCode, lang);
            Handler.voidHandler(task, promise);
        }
    }

    public void updateEmail(String newEmail, String verificationCode, Promise promise) {
        if (AGConnectAuth.getInstance().getCurrentUser() == null) {
            promise.reject(String.valueOf(userNullErrorCode), AGCAuthException.getMsgByCode(userNullErrorCode));
        } else {
            Task<Void> task = AGConnectAuth.getInstance().getCurrentUser().updateEmail(newEmail, verificationCode);
            Handler.voidHandler(task, promise);
        }
    }

    public void updateEmailWithLocale(String newEmail, String verificationCode, String locale, Promise promise) {
        if (AGConnectAuth.getInstance().getCurrentUser() == null) {
            promise.reject(String.valueOf(userNullErrorCode), AGCAuthException.getMsgByCode(userNullErrorCode));
        } else {
            Locale lang = ReactUtils.getLocale(locale);
            Task<Void> task = AGConnectAuth.getInstance().getCurrentUser().updateEmail(newEmail, verificationCode, lang);
            Handler.voidHandler(task, promise);
        }
    }

    public void updatePassword(String newPassword, String verificationCode, int provider, Promise promise) {
        if (AGConnectAuth.getInstance().getCurrentUser() == null) {
            promise.reject(String.valueOf(userNullErrorCode), AGCAuthException.getMsgByCode(userNullErrorCode));
        } else {
            Task<Void> task = AGConnectAuth.getInstance().getCurrentUser().updatePassword(newPassword, verificationCode, provider);
            Handler.voidHandler(task, promise);
        }
    }

    public void link(ReadableMap credentialMap, Promise promise) {
        int provider = credentialMap.getInt("provider");
        switch (provider) {
            case AGConnectAuthCredential.HMS_Provider:
                linkHuaweiID(credentialMap, promise);
                break;
            case AGConnectAuthCredential.HWGame_Provider:
                linkHuaweiGame(credentialMap, promise);
                break;
            case AGConnectAuthCredential.WeiXin_Provider:
                linkWeixin(credentialMap, promise);
                break;
            case AGConnectAuthCredential.Facebook_Provider:
                linkFacebook(credentialMap, promise);
                break;
            case AGConnectAuthCredential.Twitter_Provider:
                linkTwitter(credentialMap, promise);
                break;
            case AGConnectAuthCredential.WeiBo_Provider:
                linkWeibo(credentialMap, promise);
                break;
            case AGConnectAuthCredential.QQ_Provider:
                linkQQ(credentialMap, promise);
                break;
            case AGConnectAuthCredential.Google_Provider:
                linkGoogle(credentialMap, promise);
                break;
            case AGConnectAuthCredential.GoogleGame_Provider:
                linkGoogleGames(credentialMap, promise);
                break;
            case AGConnectAuthCredential.SelfBuild_Provider:
                linkSelfBuild(credentialMap, promise);
                break;
            case AGConnectAuthCredential.Email_Provider:
                linkEmail(credentialMap, promise);
                break;
            case AGConnectAuthCredential.Phone_Provider:
                linkPhone(credentialMap, promise);
                break;
            default:
                promise.reject("UNKOWN_PROVIDER", "UNKOWN_PROVIDER");
        }
    }

    public void unlink(int provider, Promise promise) {
        if (AGConnectAuth.getInstance().getCurrentUser() == null) {
            promise.reject(String.valueOf(userNullErrorCode), AGCAuthException.getMsgByCode(userNullErrorCode));
        } else {
            Task<SignInResult> task = AGConnectAuth.getInstance().getCurrentUser().unlink(provider);
            Handler.singInResultHandler(task, promise);
        }
    }

    public void getToken(boolean forceRefresh, Promise promise) {
        if (AGConnectAuth.getInstance().getCurrentUser() == null) {
            promise.reject(String.valueOf(userNullErrorCode), AGCAuthException.getMsgByCode(userNullErrorCode));
        } else {
            AGConnectAuth.getInstance().getCurrentUser().getToken(forceRefresh)
                    .addOnSuccessListener(tokenResult -> {
                        promise.resolve(ReactUtils.tokenToWM(tokenResult));
                    }).addOnFailureListener(e -> {
                Handler.errorHandler(e, promise);
            });
        }
    }

    public void getUserExtra(Promise promise) {
        if (AGConnectAuth.getInstance().getCurrentUser() == null) {
            promise.reject(String.valueOf(userNullErrorCode), AGCAuthException.getMsgByCode(userNullErrorCode));
        } else {
            AGConnectAuth.getInstance().getCurrentUser().getUserExtra()
                    .addOnSuccessListener(agConnectUserExtra -> {
                        promise.resolve(ReactUtils.userExtraToWM(agConnectUserExtra));
                    }).addOnFailureListener(e -> {
                Handler.errorHandler(e, promise);
            });
        }
    }

    public void updateProfile(ReadableMap map, Promise promise) {
        if (AGConnectAuth.getInstance().getCurrentUser() == null) {
            promise.reject(String.valueOf(userNullErrorCode), AGCAuthException.getMsgByCode(userNullErrorCode));
        } else {
            ProfileRequest.Builder builder = new ProfileRequest.Builder();
            if (map.hasKey("displayName")) {
                String displayName = map.getString("displayName");
                builder.setDisplayName(displayName);
            }
            if (map.hasKey("photoUrl")) {
                String photoUrl = map.getString("photoUrl");
                builder.setPhotoUrl(photoUrl);
            }
            ProfileRequest profileRequest = builder.build();
            Task<Void> task = AGConnectAuth.getInstance().getCurrentUser().updateProfile(profileRequest);
            Handler.voidHandler(task, promise);
        }
    }

    private void linkHandler(AGConnectAuthCredential credential, Promise promise) {
        if (AGConnectAuth.getInstance().getCurrentUser() == null) {
            promise.reject(String.valueOf(userNullErrorCode), AGCAuthException.getMsgByCode(userNullErrorCode));
        } else {
            Task<SignInResult> task = AGConnectAuth.getInstance().getCurrentUser().link(credential);
            Handler.singInResultHandler(task, promise);
        }
    }

    private void linkHuaweiID(ReadableMap credentailMap, Promise promise) {
        String accessToken = "";
        if (credentailMap.hasKey("accessToken")) {
            accessToken = credentailMap.getString("accessToken");
        }
        AGConnectAuthCredential credential = HwIdAuthProvider.credentialWithToken(accessToken);
        linkHandler(credential, promise);
    }

    private void linkHuaweiGame(ReadableMap map, Promise promise) {
        HWGameAuthProvider.Builder builder = new HWGameAuthProvider.Builder();
        if (map.hasKey("playerSign")) {
            String playerSign = map.getString("playerSign");
            builder.setPlayerSign(playerSign);
        }
        if (map.hasKey("imageUrl")) {
            String imageUrl = map.getString("imageUrl");
            builder.setImageUrl(imageUrl);
        }
        if (map.hasKey("playerLevel")) {
            int playerLevel = map.getInt("playerLevel");
            builder.setPlayerLevel(playerLevel);
        }
        if (map.hasKey("playerId")) {
            String playerId = map.getString("playerId");
            builder.setPlayerId(playerId);
        }
        if (map.hasKey("displayName")) {
            String displayName = map.getString("displayName");
            builder.setDisplayName(displayName);
        }
        if (map.hasKey("signTs")) {
            String signTs = map.getString("signTs");
            builder.setSignTs(signTs);
        }
        AGConnectAuthCredential credential = builder.build();
        linkHandler(credential, promise);
    }

    private void linkWeixin(ReadableMap credentialMap, Promise promise) {
        String accessToken = "";
        String openId = "";
        if (credentialMap.hasKey("accessToken")) {
            accessToken = credentialMap.getString("accessToken");
        }
        if (credentialMap.hasKey("openId")) {
            openId = credentialMap.getString("openId");
        }
        AGConnectAuthCredential credential = WeixinAuthProvider.credentialWithToken(accessToken, openId);
        linkHandler(credential, promise);
    }

    private void linkFacebook(ReadableMap credentialMap, Promise promise) {
        String accessToken = "";
        if (credentialMap.hasKey("accessToken")) {
            accessToken = credentialMap.getString("accessToken");
        }
        AGConnectAuthCredential credential = FacebookAuthProvider.credentialWithToken(accessToken);
        linkHandler(credential, promise);
    }

    private void linkTwitter(ReadableMap credentialMap, Promise promise) {
        String token = "";
        String secret = "";
        if (credentialMap.hasKey("token")) {
            token = credentialMap.getString("token");
        }
        if (credentialMap.hasKey("secret")) {
            secret = credentialMap.getString("secret");
        }
        AGConnectAuthCredential credential = TwitterAuthProvider.credentialWithToken(token, secret);
        linkHandler(credential, promise);
    }

    private void linkWeibo(ReadableMap credentialMap, Promise promise) {
        String token = "";
        String uid = "";
        if (credentialMap.hasKey("token")) {
            token = credentialMap.getString("token");
        }
        if (credentialMap.hasKey("uid")) {
            uid = credentialMap.getString("uid");
        }
        AGConnectAuthCredential credential = WeiboAuthProvider.credentialWithToken(token, uid);
        linkHandler(credential, promise);
    }

    private void linkQQ(ReadableMap credentialMap, Promise promise) {
        String accessToken = "";
        String openId = "";
        if (credentialMap.hasKey("accessToken")) {
            accessToken = credentialMap.getString("accessToken");
        }
        if (credentialMap.hasKey("openId")) {
            openId = credentialMap.getString("openId");
        }
        AGConnectAuthCredential credential = QQAuthProvider.credentialWithToken(accessToken, openId);
        linkHandler(credential, promise);
    }

    private void linkGoogle(ReadableMap credentialMap, Promise promise) {
        String idToken = "";
        if (credentialMap.hasKey("idToken")) {
            idToken = credentialMap.getString("idToken");
        }
        AGConnectAuthCredential credential = GoogleAuthProvider.credentialWithToken(idToken);
        linkHandler(credential, promise);
    }

    private void linkGoogleGames(ReadableMap credentialMap, Promise promise) {
        String serverAuthCode = "";
        if (credentialMap.hasKey("serverAuthCode")) {
            serverAuthCode = credentialMap.getString("serverAuthCode");
        }
        AGConnectAuthCredential credential = GoogleGameAuthProvider.credentialWithToken(serverAuthCode);
        linkHandler(credential, promise);
    }

    private void linkSelfBuild(ReadableMap credentialMap, Promise promise) {
        String token = "";
        if (credentialMap.hasKey("token")) {
            token = credentialMap.getString("token");
        }
        AGConnectAuthCredential credential = SelfBuildProvider.credentialWithToken(token);
        linkHandler(credential, promise);
    }

    private void linkEmail(ReadableMap credentialMap, Promise promise) {
        String email = "";
        String verificationCode = "";
        if (credentialMap.hasKey("email")) {
            email = credentialMap.getString("email");
        }
        if (credentialMap.hasKey("verificationCode")) {
            verificationCode = credentialMap.getString("verificationCode");
        }
        AGConnectAuthCredential credential = EmailAuthProvider.credentialWithVerifyCode(email, null, verificationCode);
        linkHandler(credential, promise);
    }

    private void linkPhone(ReadableMap credentailMap, Promise promise) {
        String countryCode = "";
        String phoneNumber = "";
        String verificationCode = "";
        if (credentailMap.hasKey("countryCode")) {
            countryCode = credentailMap.getString("countryCode");
        }
        if ( credentailMap.hasKey("phoneNumber")) {
            phoneNumber = credentailMap.getString("phoneNumber");
        }
        if (credentailMap.hasKey("verificationCode")) {
            verificationCode = credentailMap.getString("verificationCode");
        }
        AGConnectAuthCredential credential = PhoneAuthProvider.credentialWithVerifyCode(countryCode, phoneNumber, null, verificationCode);
        linkHandler(credential, promise);
    }
}
