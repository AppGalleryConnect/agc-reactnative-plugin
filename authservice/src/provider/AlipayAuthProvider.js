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

export default class AlipayAuthProvider {
    constructor() {
        throw new Error('`new AlipayAuthProvider()` is not supported， please use static functions');
    }

    /**
     * Creates a sign-in credential. By default, an account is automatically created.
     *
     * @param {string} authCode authCode obtained after Alipay SDK authorization.
     * @param autoCreateUser Indicates whether to automatically create an account.
     */
    static credential(authCode, autoCreateUser = true) {
        return {
            authCode: authCode,
            autoCreateUser: autoCreateUser,
            provider: AuthProviderType.Alipay,
        };
    }
}