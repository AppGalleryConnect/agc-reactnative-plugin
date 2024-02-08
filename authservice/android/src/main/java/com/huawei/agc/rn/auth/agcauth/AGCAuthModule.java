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
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;

import java.util.Map;

/**
 * AGCAuthModule class is the tool class of AGConnectAuth.
 *
 * @since v.1.4.2.301
 */
public class AGCAuthModule extends ReactContextBaseJavaModule {
    private AGCAuthService agcAuthService;

    public AGCAuthModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.agcAuthService = new AGCAuthService(reactContext);
    }

    /**
     * Here we will call this AGCAuthModule so that
     * we can access it through
     * React.NativeModules.AGCAuthModule in RN Side.
     *
     * @return name
     */
    @Override
    public String getName() {
        return "AGCAuthModule";
    }

    /**
     * Returns constant values of {@link AGCAuthModule} to RN Side.
     *
     * @return Constant values of {@link AGCAuthModule}
     */
    @Override
    public Map<String, Object> getConstants() {
        return AGCAuthConstants.getConstants();
    }

    /**
     * Returns current signed in user.
     *
     * @param promise: In the success scenario, current signed in user will be returned, or AGCAuthException will be returned  in the failure scenario.
     */
    @ReactMethod
    public void getUser(Promise promise) {
        agcAuthService.getUser(promise);
    }

    /**
     * Applies for a verification code using a mobile number.
     *
     * @param countryCode: Country/Region code.
     * @param phoneNumber: Mobile number.
     * @param map: Verification code attributes, including the verification code application scenarios (such as registration, sign-in, and password resetting), language of the verification code message, and interval for sending verification codes.
     * @param promise: In the success scenario, VerifyCodeResult will be returned, or AGCAuthException will be returned  in the failure scenario.
     */
    @ReactMethod
    public void requestPhoneVerifyCode(String countryCode, String phoneNumber, ReadableMap map, Promise promise) {
        agcAuthService.requestPhoneVerifyCode(countryCode, phoneNumber, map, promise);
    }

    /**
     * Applies for a verification code using an email address.
     *
     * @param email: Email address.
     * @param map: Verification code attributes, including the verification code application scenarios (such as registration, sign-in, and password resetting), language of the verification code message, and interval for sending verification codes.
     * @param promise: In the success scenario, VerifyCodeResult will be returned, or AGCAuthException will be returned  in the failure scenario.
     */
    @ReactMethod
    public void requestEmailVerifyCode(String email, ReadableMap map, Promise promise) {
        agcAuthService.requestEmailVerifyCode(email, map, promise);
    }

    /**
     * Creates an account using a mobile number.
     *
     * @param countryCode: Country/Region code.
     * @param phoneNumber: Mobile number.
     * @param verificationCode: Verification code.
     * @param password: Password.
     * @param promise: In the success scenario, user will be signed in and returned, or AGCAuthException will be returned  in the failure scenario.
     */
    @ReactMethod
    public void createPhoneUser(String countryCode, String phoneNumber, String verificationCode, String password, Promise promise) {
        agcAuthService.createPhoneUser(countryCode, phoneNumber, verificationCode, password, promise);
    }

    /**
     * Creates an account using an email address.
     *
     * @param email: Email address.
     * @param verificationCode: Verification code.
     * @param password: Password.
     * @param promise: In the success scenario, user will be signed in and returned, or AGCAuthException will be returned  in the failure scenario.
     */
    @ReactMethod
    public void createEmailUser(String email, String verificationCode, String password, Promise promise) {
        agcAuthService.createEmailUser(email, verificationCode, password, promise);
    }

    /**
     * Deletes the current user information and cache information from the AppGallery Connect server.
     *
     * @param promise: Returns true after the function execution is completed.
     */
    @ReactMethod
    public void deleteUser(Promise promise) {
        agcAuthService.deleteUser(promise);
    }

    /**
     * Signs out a user and deletes the user's cached data.
     *
     * @param promise: Returns true after the function execution is completed.
     */
    @ReactMethod
    public void signOut(Promise promise) {
        agcAuthService.signOut(promise);
    }

    /**
     * Signs in a user anonymously.
     *
     * @param promise: In the success scenario, user will be signed in and returned, or AGCAuthException will be returned  in the failure scenario.
     */
    @ReactMethod
    public void signInAnonymously(Promise promise) {
        agcAuthService.signInAnonymously(promise);
    }

    /**
     * Resets a user's password using the mobile number.
     *
     * @param countryCode: Country/Region code.
     * @param phoneNumber: Mobile number.
     * @param newPassword: New password.
     * @param verificationCode: Verification code.
     * @param promise: In the success scenario, true value will be returned, or AGCAuthException will be returned  in the failure scenario.
     */
    @ReactMethod
    public void resetPhonePassword(String countryCode, String phoneNumber, String newPassword, String verificationCode, Promise promise) {
        agcAuthService.resetPhonePassword(countryCode, phoneNumber, newPassword, verificationCode, promise);
    }

    /**
     * Resets a user's password using the email address.
     *
     * @param email: Email address.
     * @param newPassword: New password.
     * @param verificationCode: Verification code.
     * @param promise: In the success scenario, true value will be returned, or AGCAuthException will be returned  in the failure scenario.
     */
    @ReactMethod
    public void resetEmailPassword(String email, String newPassword, String verificationCode, Promise promise) {
        agcAuthService.resetEmailPassword(email, newPassword, verificationCode, promise);
    }

    /**
     * Signs in a user to AppGallery Connect.
     *
     * @param credential: Authentication credential, which must be created using the corresponding Auth Provider type.
     * @param promise: In the success scenario, user will be signed in and returned, or AGCAuthException will be returned  in the failure scenario.
     */
    @ReactMethod
    public void signIn(ReadableMap credential, Promise promise) {
        agcAuthService.signIn(credential, promise);
    }

    /**
     * Adds a token change listener.
     */
    @ReactMethod
    public void addTokenListener(String id) {
        agcAuthService.addTokenListener(id);
    }

    /**
     * Removes the token change listener.
     */
    @ReactMethod
    public void removeTokenListener(String id) {
        agcAuthService.removeTokenListener(id);
    }

    @ReactMethod
    public void addListener(String eventName) {
        // Keep: Required for RN built in Event Emitter Calls.
    }
    @ReactMethod
    public void removeListeners(Integer count) {
        // Keep: Required for RN built in Event Emitter Calls.
    }
}
