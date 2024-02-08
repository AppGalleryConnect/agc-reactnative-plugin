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

/**
 * value值的来源
 */
export enum SOURCE {
  /**
   * 获取的value值是类型默认值
   */
  STATIC = 0,

  /**
   * 获取的value值是本地默认值
   */
  DEFAULT = 1,

  /**
   * 获取的value值是云端默认值
   */
  REMOTE = 2
}

export interface ConfigError {
  /**
   * 错误码
   */
  code: string;

  /**
   * 错误消息
   */
  message: string;

  /**
   * 限流时间
   */
  throttleEndTime: number | null;
}

export class AGCRemoteConfig {
  /**
   * 设置默认参数
   * 
   * @param map 参数的map列表
   */
  static applyDefault(map: Map<string, string | number | boolean>): Promise<null>;

  /**
   * 使最近一次从云端拉取的配置生效
   */
  static applyLastFetched(): Promise<null>;

  /**
   * 从云端拉取最新的配置数据
   * 
   * @param intervalSeconds 拉取间隔，单位为秒。默认的拉取间隔为12小时。
   */
  static fetch(intervalSeconds: undefined | string): Promise<null>;

  /**
   * 返回key对应的value值
   * 
   * @param key 参数key值 
   */
  static getValue(key: string): Promise<string>;

  /**
   * 返回key对应的来源值
   * 
   * @param key 数key值 
   */
  static getSource(key: string): Promise<SOURCE>;

  /**
   * 返回所有的配置参数
   */
  static getMergedAll(): Promise<Map<string, string | number | boolean>>;

  /**
   * 清空所有的配置参数
   */
  static clearAll(): Promise<null>;

  /**
   * 设置开发者模式
   * 此接口仅对android平台适用
   * 
   * @param isDeveloperMode 
   */
  static setDeveloperMode(isDeveloperMode: boolean): Promise<null>;

  /**
   * 获取自定义参数
   */
  static getCustomAttributes(): Promise<Map<string, string>>;

  /**
   * 设置自定义属性参数
   */
   static setCustomAttributes(customAttributes: Map<string, string>): Promise<null>;
}