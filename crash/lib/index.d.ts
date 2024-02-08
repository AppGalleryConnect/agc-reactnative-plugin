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

export enum LogLevel {
  /**
   * debug 日志级别
   */
  DEBUG = 2,

  /**
   * info日志级别
   */
  INFO = 3,

  /**
   * warn 日志级别
   */
  WARN = 4,

  /**
   * error日志级别 
   */
  ERROR = 5
}

export class AGCCrash {
  /**
   * 设置是否收集&上报应用的Crash的信息
   * @param enable true：收集&上报，false：不收集&不上报
   */
  static enableCrashCollection(enable: boolean): Promise<null>;

  /**
   * 创造一个crash异常，用于开发者调试
   * 此异常在主线程触发，并非JS线程
   */
  static testIt(): void;

  /**
   * 设置用户id
   * @param userId 用户的id
   */
  static setUserId(userId: string): Promise<null>;

  /**
   * 设置用户自定义状态
   * @param key 自定义状态的key值
   * @param value 自定义状态的value值
   */
  static setCustomKey(key: string, value: string | number | boolean): Promise<null>;

  /**
   * 设置用户自定义日志，级别为默认的INFO级别
   * @param message 自定义日志内容
   */
  static log(message: string): Promise<null>;

  /**
   * 设置用户自定义日志
   * @param logLevel 自定义日志的级别
   * @param message 自定义日志内容
   */
  static logWithLevel(logLevel: LogLevel, message: string): Promise<null>;

}