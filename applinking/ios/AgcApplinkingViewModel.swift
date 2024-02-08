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

import AGConnectAppLinking

/// All the AppLinking API's can be reached via AgcApplinkingViewModel class instance.
public class AgcApplinkingViewModel {

  /// **CompletionHandler** is a typealias that provides result and error when the request is completed.
  /// - Parameters:
  ///   - result: Any Object that will be returned when the result comes.
  ///   - error: NSError that will be returned when there is an error.
  public typealias CompletionHandler = (_ result: Any?, _ error: Error?) -> Void

  private var completionHandler: CompletionHandler?
  
  /// Fetches ResolvedLinkData instance with deepLink, clickTimeStamp,
  /// socialTitle, socialDescription, socialImageUrl,
  /// campaignName, campaignMedium,
  /// campaignSource
  /// and minimumAppVersion params.
  /// - Parameter completion: CompletionHandler
  func getAGConnectAppLinkingResolvedLinkData(completion: @escaping CompletionHandler){
    AGCAppLinking.instance().handle { [weak self] (link, error) in
      guard self != nil else {return}
      if error != nil {
        completion(nil, error)
        return
      }
      completion(link,nil)
    }
  }
  
  /// Asynchronously generates a short link with a string-type suffix. You can specify the suffix as a long or short one.
  /// - Parameters:
  ///   - params: NSDictionary instance
  ///   - completion: CompletionHandler
  func buildShortAppLinking(_ params: NSDictionary, completion: @escaping CompletionHandler){
    if let params = (params as? [String : Any]){
      let components = AGCAppLinkingComponents()
      buildLink(components: components, params: params)
      var leng = AGCShortLinkingLength.short
      if let length = params["shortAppLinkingLength"] as? String{
        switch length {
        case "SHORT":
          break
        case "LONG":
          leng = AGCShortLinkingLength.long
          break
        default:
          break
        }
        components.buildShortLink(leng){ (shortLink, error) in
          if let error = error{
            completion(nil,error)
            return
          }
          if let shortUrl = shortLink?.url{
            completion(shortUrl.absoluteString, nil)
          }
        }
        return
      }
      components.buildShortLink { (shortLink, error) in
        if let error = error{
          completion(nil,error)
          return
        }
        if let shortUrl = shortLink?.url{
          completion(shortUrl.absoluteString, nil)
        }
      }
    }
  }
  
  /// Generates a long link Uri.
  /// - Parameter params: NSDictionary instance
  /// - Returns: String
  func buildLongAppLinking(_ params: NSDictionary) -> String{
    if let params = (params as? [String : Any]){
      let components = AGCAppLinkingComponents()
      buildLink(components: components, params: params)
      print(components.buildLongLink().absoluteString)
      return components.buildLongLink().absoluteString
    }
    return ""
  }

  // MARK: - Private Functions

  private func buildLink(components: AGCAppLinkingComponents, params: [String : Any]){
    if let domainUriPrefix = params["domainUriPrefix"] as? String{
      components.uriPrefix = domainUriPrefix
    }

    if let deepLink = params["deepLink"] as? String{
      components.deepLink = deepLink
    }
    
    if let longLink = params["longLink"] as? String{
      components.longLink = longLink
    }
    
    if let socialCardInfo = params["socialCardInfo"] as? NSDictionary{
      buildSocialCardInfo(components: components, socialCardInfo: socialCardInfo)
    }

    if let campaignInfo = params["campaignInfo"] as? NSDictionary{
      buildCampaignInfo(components: components, campaignInfo: campaignInfo)
    }
    
    if let expireMin = params["expireMinute"] as? Int{
      components.expireMinute = expireMin
    }

    if let androidLinkInfo = params["androidLinkInfo"] as? NSDictionary{
      buildAndroidLinkInfo(components: components, androidLinkInfo: androidLinkInfo)
    }

    if let harmonyOSLinkInfo = params["harmonyOSLinkInfo"] as? NSDictionary{
      buildHarmonyOSLinkInfo(components: components, harmonyOSLinkInfo: harmonyOSLinkInfo)
    }

    if let iosLinkInfo = params["IOSLinkInfo"] as? NSDictionary{
      buildiOSLinkInfo(components: components, iosLinkInfo: iosLinkInfo)
    }

    if let iTunesLinkInfo = params["ITunesLinkInfo"] as? NSDictionary{
      buildiTunesLinkInfo(components: components, iTunesLinkInfo: iTunesLinkInfo)
    }

  }

  private func buildSocialCardInfo(components: AGCAppLinkingComponents, socialCardInfo: NSDictionary){
    if let socialDescription = socialCardInfo["description"] as? String{
      components.socialDescription = socialDescription
    }
    if let socialImageUrl = socialCardInfo["imageUrl"] as? String{
      components.socialImageUrl = socialImageUrl
    }
    if let socialTitle = socialCardInfo["title"] as? String{
      components.socialTitle = socialTitle
    }
  }

  private func buildCampaignInfo(components: AGCAppLinkingComponents, campaignInfo: NSDictionary){

    if let campaignName = campaignInfo["medium"] as? String{
      components.campaignName = campaignName
    }
    if let campaignMedium = campaignInfo["name"] as? String{
      components.campaignMedium = campaignMedium
    }
    if let campaignSource = campaignInfo["source"] as? String{
      components.campaignSource = campaignSource
    }
  }

  private func buildAndroidLinkInfo(components: AGCAppLinkingComponents, androidLinkInfo: NSDictionary){
    if let packageName = androidLinkInfo["packageName"] as? String{
      components.androidPackageName = packageName
    }
    if let androidDeepLink = androidLinkInfo["androidDeepLink"] as? String{
      components.androidDeepLink = androidDeepLink
    }
    if let openType = androidLinkInfo["openType"] as? String{
      switch openType {
      case "AppGallery":
        components.androidOpenType = AGCLinkingAndroidOpenType.appGallery
      case "LocalMarket":
        components.androidOpenType = AGCLinkingAndroidOpenType.localMarket
      case "CustomUrl":
        components.androidOpenType = AGCLinkingAndroidOpenType.customURL
      default:
        components.androidOpenType = AGCLinkingAndroidOpenType.appGallery
      }
    }
    if let fallbackUrl = androidLinkInfo["fallbackUrl"] as? String{
      components.androidFallbackUrl = fallbackUrl
    }
  }

  private func buildHarmonyOSLinkInfo(components: AGCAppLinkingComponents, harmonyOSLinkInfo: NSDictionary){
    if let packageName = harmonyOSLinkInfo["harmonyOSPackageName"] as? String{
        components.harmonyOSPackageName = packageName
    }
    if let harmonyOSDeepLink = harmonyOSLinkInfo["harmonyOSDeepLink"] as? String{
        components.harmonyOSDeepLink = harmonyOSDeepLink
    }
    if let fallbackUrl = harmonyOSLinkInfo["fallbackUrl"] as? String{
        components.harmonyOSFallbackUrl = fallbackUrl
    }
 }

  private func buildiOSLinkInfo(components: AGCAppLinkingComponents, iosLinkInfo: NSDictionary){
    if let iosBundleId = iosLinkInfo["iosBundleId"] as? String{
      components.iosBundleId = iosBundleId
    }
    if let iosDeepLink = iosLinkInfo["iosDeepLink"] as? String{
      components.iosDeepLink = iosDeepLink
    }
    if let iosFallbackUrl = iosLinkInfo["iosFallbackUrl"] as? String{
      components.iosFallbackUrl = iosFallbackUrl
    }
    if let ipadBundleId = iosLinkInfo["ipadBundleId"] as? String{
      components.ipadBundleId = ipadBundleId
    }
    if let ipadFallbackUrl = iosLinkInfo["ipadFallbackUrl"] as? String{
      components.ipadFallbackUrl = ipadFallbackUrl
    }
  }

  private func buildiTunesLinkInfo(components: AGCAppLinkingComponents, iTunesLinkInfo: NSDictionary){
    if let iTunesConnectMediaType = iTunesLinkInfo["iTunesConnectMediaType"] as? String{
      components.iTunesConnectMediaType = iTunesConnectMediaType
    }
    if let iTunesConnectAffiliateToken = iTunesLinkInfo["iTunesConnectAffiliateToken"] as? String{
      components.iTunesConnectAffiliateToken = iTunesConnectAffiliateToken
    }
    if let iTunesConnectProviderToken = iTunesLinkInfo["iTunesConnectProviderToken"] as? String{
      components.iTunesConnectProviderToken = iTunesConnectProviderToken
    }
    if let iTunesConnectCampaignToken = iTunesLinkInfo["iTunesConnectCampaignToken"] as? String{
      components.iTunesConnectCampaignToken = iTunesConnectCampaignToken
    }
  }
}

extension AgcApplinkingViewModel{
  fileprivate func postData(result: Any?) {
    if let comp = completionHandler {
      comp(result, nil)
    }
  }

  fileprivate func postError(error: NSError?) {
    if let comp = completionHandler {
      comp(nil, error)
    }
  }
}
