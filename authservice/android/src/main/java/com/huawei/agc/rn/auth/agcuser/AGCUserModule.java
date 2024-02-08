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
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;

import javax.annotation.Nonnull;

/**
 * AGCUserModule class is the tool class of AGConnectUser.
 *
 * @since v.1.4.2.301
 */
public class AGCUserModule extends ReactContextBaseJavaModule {
    private AGCUserService agcUserService = new AGCUserService();

    public AGCUserModule(@Nonnull ReactApplicationContext reactContext) {
        super(reactContext);
    }

    /**
     * Here we will call this AGCUserModule so that
     * we can access it through
     * React.NativeModules.AGCUserModule in RN Side.
     *
     * @return name
     */
    @Override
    public String getName() {
        return "AGCUserModule";
    }

    /**
     * Updates the mobile number of the current user.
     *
     * @param countryCode: Country/Region code.
     * @param newPhoneNumber: New Mobile number.
     * @param verificationCode: Verification code send to the phone number.
     * @param promise: In the success scenario, true value will be returned, or AGCAuthException will be returned  in the failure scenario.
     */
    @ReactMethod
    public void updatePhone(String countryCode, String newPhoneNumber, String verificationCode, Promise promise) {
        agcUserService.updatePhone(countryCode, newPhoneNumber, verificationCode, promise);
    }

    /**
     * Updates the mobile number of the current user.
     *
     * @param countryCode: Country/Region code.
     * @param newPhoneNumber: New Mobile number.
     * @param verificationCode: Verification code send to the phone number.
     * @param locale: Language in which the verification code message is sent.
     * @param promise: In the success scenario, true value will be returned, or AGCAuthException will be returned  in the failure scenario.
     */
    @ReactMethod
    public void updatePhoneWithLocale(String countryCode, String newPhoneNumber, String verificationCode, String locale, Promise promise) {
        agcUserService.updatePhoneWithLocale(countryCode, newPhoneNumber, verificationCode, locale, promise);
    }

    /**
     * Updates the current user's password.
     *
     * @param newPassword: New password.
     * @param verificationCode: Verification code.
     * @param provider: Provider type, which is used to differentiate the email address from mobile number.
     * @param promise: In the success scenario, true value will be returned, or AGCAuthException will be returned  in the failure scenario.
     */
    @ReactMethod
    public void updatePassword(String newPassword, String verificationCode, int provider, Promise promise) {
        agcUserService.updatePassword(newPassword, verificationCode, provider, promise);
    }

    /**
     * Updates the current user's email address.
     *
     * @param newEmail: New email address.
     * @param verificationCode: Verification code sent to the email address.
     * @param promise: In the success scenario, true value will be returned, or AGCAuthException will be returned  in the failure scenario.
     */
    @ReactMethod
    public void updateEmail(String newEmail, String verificationCode, Promise promise) {
        agcUserService.updateEmail(newEmail, verificationCode, promise);
    }

    /**
     * Updates the current user's email address.
     *
     * @param newEmail: New email address
     * @param verificationCode: Verification code sent to the email address.
     * @param locale: Language in which the verification code email is sent.
     * @param promise: In the success scenario, true value will be returned, or AGCAuthException will be returned  in the failure scenario.
     */
    @ReactMethod
    public void updateEmailWithLocale(String newEmail, String verificationCode, String locale, Promise promise) {
        agcUserService.updateEmailWithLocale(newEmail, verificationCode, locale, promise);
    }

    /**
     * Unlinks the current user from the linked authentication mode.
     *
     * @param provider: Authentication mode to be unlinked.
     * @param promise: In the success scenario, current signed in user will be returned, or AGCAuthException will be returned  in the failure scenario.
     */
    @ReactMethod
    public void unlink(int provider, Promise promise) {
        agcUserService.unlink(provider, promise);
    }

    /**
     * Obtains the access token of a user from AppGallery Connect.
     *
     * @param forceRefresh: Indicates whether to forcibly update the access token of a user.
     * @param promise: In the success scenario, TokenResult will be returned, or AGCAuthException will be returned  in the failure scenario.
     */
    @ReactMethod
    public void getToken(boolean forceRefresh, Promise promise) {
        agcUserService.getToken(forceRefresh, promise);
    }

    /**
     * Obtains UserExtra of the current user.
     *
     * @param promise: In the success scenario, AGCUserExtra will be returned, or AGCAuthException will be returned  in the failure scenario.
     */
    @ReactMethod
    public void getUserExtra(Promise promise) {
        agcUserService.getUserExtra(promise);
    }

    /**
     * Updates information (profile picture and nickname) for the current user.
     *
     * @param map: Profile information to be modified.
     * @param promise: In the success scenario, true value will be returned, or AGCAuthException will be returned  in the failure scenario.
     */
    @ReactMethod
    public void updateProfile(ReadableMap map, Promise promise) {
        agcUserService.updateProfile(map, promise);
    }

    /**
     * Links a new authentication mode for the current user.
     *
     * @param credential: Credential of a new authentication mode.
     * @param promise: In the success scenario, current signed in user will be returned, or AGCAuthException will be returned  in the failure scenario.
     */
    @ReactMethod
    public void link(ReadableMap credential, Promise promise) {
        agcUserService.link(credential, promise);
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
