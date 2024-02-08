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

import AGCAuth from './AGCAuth';
import VerifyCodeSettings, { VerifyCodeAction } from './VerifyCodeSettings';
import AuthProviderType from './provider/AuthProviderType';
import PhoneAuthProvider from './provider/PhoneAuthProvider';
import EmailAuthProvider from './provider/EmailAuthProvider';
import FacebookAuthProvider from './provider/FacebookAuthProvider';
import HwIdAuthProvider from './provider/HwIdAuthProvider';
import HWGameAuthProvider from './provider/HWGameAuthProvider';
import WeixinAuthProvider from './provider/WeixinAuthProvider';
import TwitterAuthProvider from './provider/TwitterAuthProvider';
import WeiboAuthProvider from './provider/WeiboAuthProvider';
import QQAuthProvider from './provider/QQAuthProvider';
import GoogleAuthProvider from './provider/GoogleAuthProvider';
import GoogleGameAuthProvider from './provider/GoogleGameAuthProvider';
import AppleIDAuthProvider from './provider/AppleIDAuthProvider';
import AlipayAuthProvider from './provider/AlipayAuthProvider';
import TokenState from './TokenState'
import SelfBuildProvider from './provider/SelfBuildProvider'

export default AGCAuth;

export {
    VerifyCodeSettings,
    VerifyCodeAction,
    AuthProviderType,
    PhoneAuthProvider,
    EmailAuthProvider,
    FacebookAuthProvider,
    HwIdAuthProvider,
    HWGameAuthProvider,
    WeixinAuthProvider,
    TwitterAuthProvider,
    WeiboAuthProvider,
    QQAuthProvider,
    GoogleAuthProvider,
    GoogleGameAuthProvider,
    AppleIDAuthProvider,
    AlipayAuthProvider,
    TokenState,
    SelfBuildProvider,
};