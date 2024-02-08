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

import {NativeModules, NativeEventEmitter} from 'react-native';
import AGCUser from './AGCUser';
import AGCAuthException from './AGCAuthException';
import SignInResult from './SignInResult';

const {AGCAuthModule} = NativeModules;

export default class AGCAuth {
    constructor() {
        this.eventEmitter = new NativeEventEmitter(AGCAuthModule)
        this.listenerArray = [];
    }

    static getInstance() {
        if (!this.instance) {
            this.instance = new AGCAuth();
        }
        return this.instance;
    }

    signIn(credential) {
        return AGCAuthModule.signIn(credential)
            .then(response => {
                return new SignInResult(new AGCUser(response))
            }).catch(error => {
                throw new AGCAuthException(error)
            })
    }

    signInAnonymously() {
        return AGCAuthModule.signInAnonymously()
            .then(response => {
                return new SignInResult(new AGCUser(response))
            }).catch(error => {
                throw new AGCAuthException(error)
            })
    }

    currentUser() {
        return AGCAuthModule.getUser().then(response => {
            if (response) {
                return new AGCUser(response)
            }
            return null
        }).catch(error => {
            throw new AGCAuthException(error)
        })
    }

    signOut() {
        return AGCAuthModule.signOut()
    }

    deleteUser() {
        return AGCAuthModule.deleteUser()
    }

    createEmailUser(email, password, verifyCode) {
        return AGCAuthModule.createEmailUser(email, verifyCode, password)
            .then(response => {
                return new SignInResult(new AGCUser(response))
            }).catch(error => {
                throw new AGCAuthException(error)
            })
    }

    createPhoneUser(countryCode, phoneNumber, password, verifyCode) {
        return AGCAuthModule.createPhoneUser(countryCode, phoneNumber, verifyCode, password)
            .then(response => {
                return new SignInResult(new AGCUser(response))
            }).catch(error => {
                throw new AGCAuthException(error)
            })
    }

    addTokenListener(listener) {
        let eventListener = this.eventEmitter.addListener('onTokenChanged', listener)
        let id = "_" + Math.random().toString(36).substr(2, 9)
        this.listenerArray[id] = eventListener;
        AGCAuthModule.addTokenListener(id);
        return eventListener;
    }

    removeTokenListener(subscription) {
        if (subscription) {
            let id = Object.keys(this.listenerArray).find(key => this.listenerArray[key] === subscription);
            subscription.remove();
            AGCAuthModule.removeTokenListener(id.toString());
        }
    }

    resetPasswordWithEmail(email, password, verifyCode) {
        return AGCAuthModule.resetEmailPassword(email, password, verifyCode)
    }

    resetPasswordWithPhone(countryCode, phoneNumber, password, verifyCode) {
        return AGCAuthModule.resetPhonePassword(countryCode, phoneNumber, password, verifyCode)
    }


    static requestVerifyCodeWithEmail(email, verifyCodeSettings) {
        if (!verifyCodeSettings) {
            throw new Error('verifyCodeSettings can not be null');
        }
        const settings = {
            action: verifyCodeSettings.action,
            sendInterval: verifyCodeSettings.sendInterval,
            locale: verifyCodeSettings.lang
        }
        return AGCAuthModule.requestEmailVerifyCode(email, settings)
    }

    static requestVerifyCodeWithPhone(countryCode, phoneNumber, verifyCodeSettings) {
        if (!verifyCodeSettings) {
            throw new Error('verifyCodeSettings can not be null');
        }
        const settings = {
            action: verifyCodeSettings.action,
            locale: verifyCodeSettings.lang,
            sendInterval: verifyCodeSettings.sendInterval
        }
        return AGCAuthModule.requestPhoneVerifyCode(countryCode, phoneNumber, settings);
    }

}
