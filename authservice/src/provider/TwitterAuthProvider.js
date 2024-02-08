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

import AuthProviderType from './AuthProviderType';

export default class TwitterAuthProvider {
    constructor() {
        throw new Error('`new TwitterAuthProvider()` is not supported， please use static functions');
    }

    /**
     * Creates a sign-in credential. By default, an account is automatically created.
     *
     * @param {string} token Token obtained after Twitter authorization
     * @param {string} secret Secret obtained from Twitter
     * @param autoCreateUser Indicates whether to automatically create an account.
     */
    static credential(token, secret, autoCreateUser = true) {
        return {
            token: token,
            secret: secret,
            autoCreateUser: autoCreateUser,
            provider: AuthProviderType.Twitter,
            version: 1
        };
    }

    /**
     * Creates a credential for sign-in authentication.
     *
     * @param {TwitterAuthParam} twitterAuthParam Object that contains Twitter account sign-in parameters.
     * @param autoCreateUser Indicates whether to automatically create an account.
     */
    static credentialWithAuthCode(twitterAuthParam, autoCreateUser = true) {
        return {
            twitterAuthParam: twitterAuthParam,
            autoCreateUser: autoCreateUser,
            provider: AuthProviderType.Twitter,
            version: 2
        };
    }
}

export class TwitterAuthParam {

    /**
     * Builder of the object that contains Twitter 2.0 account sign-in parameters.
     *
     * @param {string} clientId Client ID obtained after Twitter authorization.
     * @param {string} authCode Authentication code obtained from Twitter.
     * @param {string} codeVerifier Code verifier passed for requiring Twitter OAuth 2.0 authorization.
     * @param {string} redirectUrl Redirection URL.
     */
    constructor(clientId, authCode, codeVerifier, redirectUrl) {
        this.clientId = clientId;
        this.authCode = authCode;
        this.codeVerifier = codeVerifier;
        this.redirectUrl = redirectUrl;
    }

}