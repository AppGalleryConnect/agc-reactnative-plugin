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

import { EmitterSubscription } from 'react-native';
import { VerifyCodeAction } from './VerifyCodeSettings';
import AuthProviderType from './provider/AuthProviderType';
import TokenState from './TokenState'

export {
  VerifyCodeAction,
  AuthProviderType,
  TokenState
};

export type TokenSnapShot = (state: number, token: string) => void;

export interface VerifyCodeResult {
  shortestInterval: string;
  validityPeriod: string;
}

export interface UserExtra {
  createTime: string,
  lastSignInTime: string,
}

export interface UserProfile {
  displayName?: string;
  photoUrl?: string;
}

export interface TokenResult {
  token: string
  expirePeriod: string
}

export interface SignInResult {
  user: AGCUser;
}

export interface AGCUser {

  /**
   * the user's uid
   */
  uid: string | null;

  /**
   * the user's email
   */
  email: string | null;

  /**
   * the user's phone
   */
  phone: string | null;

  /**
   * the user's displayName
   */
  displayName: string | null;

  /**
   * the user's photoUrl
   */
  photoUrl: string | null;

  /**
   * user login provider 
   */
  providerId: string | null;

  /**
   * is user's email verified
   */
  emailVerified: boolean;

  /**
   * is user have been set a password
   */
  passwordSetted: boolean;

  /**
   * Checks whether a user is an anonymous user.
   */
  isAnonymous: boolean;

  /**
   * Obtains user information from all supported third-party authentication platforms.
   */
  providerInfo: Array<Object>

  /**
   * link account by credential
   * 
   * @param credential 
   */
  link(credential: Object): Promise<SignInResult>;

  /**
   * unlink some provider
   * 
   * @param provider 
   */
  unlink(provider: number): Promise<SignInResult>;

  /**
   * update user profile
   * 
   * @param userProfile contain displayName and photoUrl
   */
  updateProfile(userProfile: UserProfile): Promise<null>;

  /**
   * update user email
   * 
   * @param email new Email
   * @param verifyCode verifyCode ,you have to make sure that the new email is yours.
   * @param lang optinal,must contain "language" and "country",separated by "_",The default value is "en_US"
   */
  updateEmail(email: string, verifyCode: string, lang?: string): Promise<null>;

  /**
   * update user phone
   * 
   * @param countryCode country code of new Phone
   * @param phoneNumber phone number of new Phone
   * @param verifyCode verifyCode of new Phone,you have to make sure that the new mobile number is yours.
   * @param lang optinal,must contain "language" and "country",separated by "_",The default value is "en_US"
   */
  updatePhone(countryCode: string, phoneNumber: string, verifyCode: string, lang?: string): Promise<null>;

  /**
   * update user password
   * 
   * @param password new password
   * @param verifyCode verifyCode
   * @param provider AuthProvider.Email or AuthProvider.Phone
   */
  updatePassword(password: string, verifyCode: string, provider: number): Promise<null>;

  /**
   * Get token
   * 
   * @param forceRefresh force token to refresh or not.
   */
  getToken(forceRefresh?: boolean): Promise<TokenResult>;

  /**
   * Get extra user information.
   */
  getUserExtra(): Promise<UserExtra>;
}

export class VerifyCodeSettings {
  constructor(action: number, lang?: string, sendInterval?: number)

  /**
   * Sets the verification code type.
   */
  _action: number;

  /**
   * Interval for sending verification codes, in seconds.
   */
  _sendInterval: number;

  /**
   * Language for sending a verification code.
   */
  _lang: string;
}

export default class AGCAuth {
  /**
   * return the singleton of AGCAuth
   */
  static getInstance(): AGCAuth;

  /**
   * signIn with a credential
   * 
   * @param credential you can build credential by some Provider
   */
  signIn(credential: Object): Promise<SignInResult>;

  /**
   * sign in anonymously
   */
  signInAnonymously(): Promise<SignInResult>;

  /**
   * get the current user info,if no user loginIn,return null
   */
  currentUser(): Promise<AGCUser | null>;

  /**
   * signOut the current user
   */
  signOut(): Promise<null>;

  /**
   * Deletes the current user information and cache information from the AppGallery Connect server.
   */
  deleteUser(): Promise<null>;

  /**
   * create a user with email
   * 
   * @param email email
   * @param password set default password when create user,set 'null' means not set default password
   * @param verifyCode verify code from email, request action is @link{VerifyCodeAction.REGISTER_OR_LOGIN}
   */
  createEmailUser(email: string, password: string | null, verifyCode: string): Promise<SignInResult>;

  /**
   * create a user with phone
   * 
   * @param countryCode country code
   * @param phoneNumber phone number
   * @param password set default password when create user,set 'null' means not set default password
   * @param verifyCode verify code from phone, request action is @link{VerifyCodeAction.REGISTER_OR_LOGIN}
   */
  createPhoneUser(countryCode: string, phoneNumber: string, password: string | null, verifyCode: string): Promise<SignInResult>;

  /**
   * add token changed listener
   * 
   * @param listener 
   */
  addTokenListener(listener: TokenSnapShot): EmitterSubscription;

  /**
   * remove token listener
   * 
   * @param subscription 
   */
  removeTokenListener(subscription: EmitterSubscription): void;

  /**
   * reset user password with email
   * 
   * @param email email
   * @param password new password
   * @param verifyCode verify code from email, request action is @link{VerifyCodeAction.RESET_PASSWORD}
   */
  resetPasswordWithEmail(email: string, password: string, verifyCode: string): Promise<null>;

  /**
   * reset user password with phone
   * 
   * @param countryCode country code 
   * @param phoneNumber phone number
   * @param password password
   * @param verifyCode verify code from phone, request action is @link{VerifyCodeAction.RESET_PASSWORD}
   */
  resetPasswordWithPhone(countryCode: string, phoneNumber: string, password: string, verifyCode: string): Promise<null>;
}

// MARK: AuthProviders

export class EmailAuthProvider {

  /**
   * Obtains a credential using an email address and a password.
   * 
   * @param email Email address.
   * @param password Password.
   */
  static credentialWithPassword(email: string, password: string): Object

  /**
   * Obtains a credential using an email address and a verification code.
   * 
   * @param email Email address.
   * @param password Password. If a password is passed, both the password and verification code are verified.
   * @param verifyCode Verification code.
   */
  static credentialWithVerifyCode(email: string, password: string | null, verifyCode: string): Object

  /**
   * Applies for a verification code using an email address.
   * 
   * @param email Email address.
   * @param verifyCodeSettings Verification code attributes, including the verification code application scenarios (such as registration, sign-in, and password resetting), language of the verification code message, and interval for sending verification codes.
   */
  static requestVerifyCode(email: string, verifyCodeSettings: VerifyCodeSettings): Promise<VerifyCodeResult>
}

export class PhoneAuthProvider {

  /**
   * Obtains a credential using a mobile number and password.
   * 
   * @param countryCode Country/Region code.
   * @param phoneNumber Mobile number.
   * @param password Password.
   */
  static credentialWithPassword(countryCode: string, phoneNumber: string, password: string): Object

  /**
   * Obtains a credential using a mobile number and verification code.
   * 
   * @param countryCode Country/Region code.
   * @param phoneNumber Mobile number.
   * @param password Password.
   * @param verifyCode Verification code.
   */
  static credentialWithVerifyCode(countryCode: string, phoneNumber: string, password: string | null, verifyCode: string): Object

  /**
   * Applies for a verification code using a mobile number.
   * 
   * @param countryCode Country/Region code.
   * @param phoneNumber Mobile number.
   * @param verifyCodeSettings Verification code attributes, including the verification code application scenarios (such as registration, sign-in, and password resetting), language of the verification code message, and interval for sending verification codes.
   */
  static requestVerifyCode(countryCode: string, phoneNumber: string, verifyCodeSettings: VerifyCodeSettings): Promise<VerifyCodeResult>
}

export class FacebookAuthProvider {

  /**
   * Builder of the sign-in credential.
   * 
   * @param token Access token obtained after Facebook authorization.
   * @param autoCreateUser Indicates whether to automatically create an account.
   */
  static credential(token: string, autoCreateUser?: boolean): Object
}

export class AppleIDAuthProvider {

  /**
   * Builder of the sign-in credential.
   * 
   * @param identityToken JWT returned after Apple ID sign-in authorization.
   * @param nonce Random character string in the authorization request.
   * @param autoCreateUser Indicates whether to automatically create an account.
   */
  static credential(identityToken: string, nonce: string, autoCreateUser?: boolean): Object
}

export class GoogleAuthProvider {

  /**
   * Builder of the sign-in credential.
   * 
   * @param idToken ID token obtained after Google authorization.
   * @param autoCreateUser Indicates whether to automatically create an account.
   */
  static credential(idToken: string, autoCreateUser?: boolean): Object
}

export class GoogleGameAuthProvider {

  /**
   * Builder of the sign-in credential.
   * 
   * @param serverAuthCode Server authentication code obtained after Google Play Games authorization.
   * @param autoCreateUser Indicates whether to automatically create an account.
   */
  static credential(serverAuthCode: string, autoCreateUser?: boolean): Object
}

export class HWGameAuthProvider {

  /**
   * Generates a credential.
   * 
   * @param playerSign Sign-in signature of the player.
   * @param playerId Player ID.
   * @param displayName Player nickname.
   * @param imageUrl Profile picture URL of the player.
   * @param playerLevel Player level.
   * @param signTs Timestamp obtained from Huawei game server.
   * @param autoCreateUser Indicates whether to automatically create an account.
   */
  static credential(
    playerSign: string,
    playerId: string,
    displayName: string,
    imageUrl: string,
    playerLevel: number,
    signTs: string,
    autoCreateUser?: boolean
  ): Object
}

export class HwIdAuthProvider {

  /**
   * Builder of the sign-in credential.
   * 
   * @param accessToken Token obtained after HMS Core SDK authorization. 
   * @param autoCreateUser Indicates whether to automatically create an account. 
   */
  static credential(accessToken: string, autoCreateUser?: boolean): Object
}

export class QQAuthProvider {

  /**
   * Builder of the sign-in credential.
   * 
   * @param accessToken Token obtained after QQ SDK authorization.
   * @param openId OpenID obtained after QQ SDK authorization.
   * @param autoCreateUser Indicates whether to automatically create an account. 
   */
  static credential(accessToken: string, openId: string, autoCreateUser?: boolean): Object
}

export class SelfBuildProvider {

  /**
   * Builder of the sign-in credential.
   * 
   * @param token JWT obtained after authorization by the self-owned account server.
   * @param autoCreateUser Indicates whether to automatically create an account. 
   */
  static credential(token: string, autoCreateUser?: boolean): Object
}

export class TwitterAuthProvider {

  /**
   * Builder of the sign-in credential.
   * 
   * @param token Token obtained after Twitter authorization.
   * @param secret Secret obtained from Twitter.
   * @param autoCreateUser Indicates whether to automatically create an account. 
   */
  static credential(token: string, secret: string, autoCreateUser?: boolean): Object

  /**
   * Creates a credential for sign-in authentication.
   *
   * @param {TwitterAuthParam} twitterAuthParam Object that contains Twitter account sign-in parameters.
   * @param autoCreateUser Indicates whether to automatically create an account.
   */
  static credentialWithAuthCode(twitterAuthParam, autoCreateUser = true) : Object
}

export class WeiboAuthProvider {

  /**
   * Builder of the sign-in credential.
   * 
   * @param token Token obtained after Weibo SDK authorization.
   * @param uid UID obtained after Weibo SDK authorization.
   * @param autoCreateUser Indicates whether to automatically create an account. 
   */
  static credential(token: string, uid: string, autoCreateUser?: boolean): Object
}

export class WeixinAuthProvider {

  /**
   * Builder of the sign-in credential.
   * 
   * @param accessToken Token obtained after WeChat SDK authorization.
   * @param openId OpenID obtained after WeChat SDK authorization.
   * @param autoCreateUser Indicates whether to automatically create an account. 
   */
  static credential(accessToken: string, openId: string, autoCreateUser?: boolean): Object
}

export class AlipayAuthProvider {

  /**
   * Builder of the sign-in credential.
   *
   * @param authCode authCode obtained after Alipay SDK authorization.
   * @param autoCreateUser Indicates whether to automatically create an account.
   */
  static credential(authCode: string, autoCreateUser?: boolean): Object
}