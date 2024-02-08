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
import { NativeModules } from 'react-native';

const { AGCAuthModule } = NativeModules;

export default AuthProviderType = {
  No_Support: -1,
  Anonymous: AGCAuthModule.provider.ANONYMOUS,
  HMS: AGCAuthModule.provider.HMS_Provider,
  Facebook: AGCAuthModule.provider.Facebook_Provider,
  Twitter: AGCAuthModule.provider.Twitter_Provider,
  WeiXin: AGCAuthModule.provider.WeiXin_Provider,
  hwGame: AGCAuthModule.provider.HWGame_Provider,
  QQ: AGCAuthModule.provider.QQ_Provider,
  Weibo: AGCAuthModule.provider.WeiBo_Provider,
  Google: AGCAuthModule.provider.Google_Provider,
  GoogleGame: AGCAuthModule.provider.GoogleGame_Provider,
  SelfBuild: AGCAuthModule.provider.SelfBuild_Provider,
  Phone: AGCAuthModule.provider.Phone_Provider,
  Email: AGCAuthModule.provider.Email_Provider,
  Apple: AGCAuthModule.provider.Apple_Provider,
  Alipay: AGCAuthModule.provider.Alipay_Provider,
}