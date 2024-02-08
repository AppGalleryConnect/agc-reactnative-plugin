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
import AGCAuthException from './AGCAuthException';
import SignInResult from './SignInResult';

const { AGCUserModule } = NativeModules;

export default class AGCUser {
  constructor(user) {
    this.user = user
  }

  get phone() {
    return this.user.phoneNumber
  }

  get isAnonymous() {
    return this.user.isAnonymous
  }

  get uid() {
    return this.user.uid
  }

  get providerId() {
    return this.user.providerId
  }

  get photoUrl() {
    return this.user.photoUrl
  }

  get passwordSetted() {
    return this.user.passwordSetted
  }

  get emailVerified() {
    return this.user.emailVerified
  }

  get email() {
    return this.user.email
  }

  get displayName() {
    return this.user.displayName
  }

  get providerInfo() {
    return this.user.providerInfo
  }

  link(credential) {
    return AGCUserModule.link(credential)
      .then(response => {
        this.user = response
        return new SignInResult(new AGCUser(response))
      }).catch(error => {
        throw new AGCAuthException(error)
      })
  }

  unlink(provider) {
    return AGCUserModule.unlink(provider)
      .then(response => {
        this.user = response
        return new SignInResult(new AGCUser(response))
      }).catch(error => {
        throw new AGCAuthException(error)
      })
  }

  updateProfile(userProfile) {
    return AGCUserModule.updateProfile(userProfile)
  }

  updateEmail(newEmail, verificationCode, lang) {
    if (lang) {
      return AGCUserModule.updateEmailWithLocale(newEmail, verificationCode, lang)
    } else {
      return AGCUserModule.updateEmail(newEmail, verificationCode)
    }
  }

  updatePhone(countryCode, newPhoneNumber, verificationCode, lang) {
    if (lang) {
      return AGCUserModule.updatePhoneWithLocale(countryCode, newPhoneNumber, verificationCode, lang)
    } else {
      return AGCUserModule.updatePhone(countryCode, newPhoneNumber, verificationCode)
    }
  }

  updatePassword(newPassword, verificationCode, provider) {
    return AGCUserModule.updatePassword(newPassword, verificationCode, provider)
  }

  getToken(forceRefresh = false) {
    return AGCUserModule.getToken(forceRefresh)
  }

  getUserExtra() {
    return AGCUserModule.getUserExtra()
  }


}