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

package com.huawei.agc.rn.applinking.viewmodel;

import android.net.Uri;

import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.huawei.agc.rn.applinking.AgcAppLinkingModule;
import com.huawei.agc.rn.applinking.presenter.AgcAppLinkingContract;
import com.huawei.agc.rn.applinking.util.AgcAppLinkingDataUtils;
import com.huawei.agconnect.applinking.AppLinking;
import com.huawei.agconnect.applinking.ShortAppLinking;
import com.huawei.agconnect.exception.AGCException;

/**
 * AgcAppLinkingViewModel works as a mediator between {@link AgcAppLinkingContract.Presenter} and {@link AgcAppLinkingModule}.
 *
 * <p>
 * It fetches data from the {@link AppLinking}, formats and returns to the {@link AgcAppLinkingModule}.
 *
 * @since v.1.2.0
 */
public class AgcAppLinkingViewModel implements AgcAppLinkingContract.Presenter {

    /**
     * Asynchronously generates a short link with a string-type suffix. You can specify the suffix as a long or short one.
     *
     * @param readableMap     ReadableMap instance that will get domainUriPrefix, deepLink and optionally socialCardInfo, campaignInfo, androidLinkInfo, expireMinute, linkingPreviewType.
     * @param resultListener: In the success scenario, {@link AgcAppLinkingContract.ResultListener<WritableMap>} instance, with shortLink and testUrl params, is returned, or {@link AGCException} is returned in the failure scenario.
     */
    @Override
    public void buildShortAppLinking(final ReadableMap readableMap, final AgcAppLinkingContract.ResultListener<WritableMap> resultListener) {

        final ShortAppLinking.LENGTH shortAppLinkingLength = readableMap.hasKey("shortAppLinkingLength")
            ? ShortAppLinking.LENGTH.valueOf(readableMap.getString("shortAppLinkingLength"))
            : ShortAppLinking.LENGTH.SHORT;

        createAppLinkingWithInfo(readableMap).buildShortAppLinking(shortAppLinkingLength)
            .addOnSuccessListener(shortAppLinking -> {
                resultListener.onSuccess(AgcAppLinkingDataUtils.createShortLink(shortAppLinking));
            })
            .addOnFailureListener(exception -> {
                resultListener.onFail(exception);
            });
    }


    /**
     * Generates a long link Uri.
     *
     * @param readableMap     ReadableMap instance that will get domainUriPrefix, deepLink and optionally socialCardInfo, campaignInfo, androidLinkInfo, expireMinute, linkingPreviewType.
     * @param resultListener: In the success scenario, {@link AgcAppLinkingContract.ResultListener<String>} instance, with shortLink and testUrl params, is returned, or {@link AGCException} is returned in the failure scenario.
     */
    @Override
    public void buildLongAppLinking(final ReadableMap readableMap, final AgcAppLinkingContract.ResultListener<String> resultListener) {
        resultListener.onSuccess(createAppLinkingWithInfo(readableMap).buildAppLinking().getUri().toString());
    }

    // MARK: - Private Methods

    private AppLinking.SocialCardInfo createSocialCardInfo(final ReadableMap readableMap) {
        final AppLinking.SocialCardInfo.Builder builder = AppLinking.SocialCardInfo.newBuilder();

        if (readableMap.hasKey(("description"))) {
            builder.setDescription(readableMap.getString("description"));
        }
        if (readableMap.hasKey("imageUrl")) {
            builder.setImageUrl(readableMap.getString("imageUrl"));
        }
        if (readableMap.hasKey("title")) {
            builder.setTitle(readableMap.getString("title"));
        }

        return builder.build();
    }

    private AppLinking.CampaignInfo createCampaignInfo(final ReadableMap readableMap) {
        final AppLinking.CampaignInfo.Builder builder = AppLinking.CampaignInfo.newBuilder();

        if (readableMap.hasKey(("medium"))) {
            builder.setMedium(readableMap.getString("medium"));
        }
        if (readableMap.hasKey("name")) {
            builder.setName(readableMap.getString("name"));
        }
        if (readableMap.hasKey("source")) {
            builder.setSource(readableMap.getString("source"));
        }

        return builder.build();
    }

    private AppLinking.AndroidLinkInfo createAndroidLinkInfo(final ReadableMap readableMap) {
        final AppLinking.AndroidLinkInfo.Builder builder;

        if (readableMap.hasKey("packageName")) {
            builder = AppLinking.AndroidLinkInfo.newBuilder(readableMap.getString("packageName"));
        } else {
            builder = AppLinking.AndroidLinkInfo.newBuilder();
        }
        if (readableMap.hasKey(("androidDeepLink"))) {
            builder.setAndroidDeepLink(readableMap.getString("androidDeepLink"));
        }
        if (readableMap.hasKey("openType")) {
            builder.setOpenType(AppLinking.AndroidLinkInfo.AndroidOpenType.valueOf(readableMap.getString("openType")));
        }
        if ("CustomUrl".equals(readableMap.getString("openType")) && readableMap.hasKey("fallbackUrl")) {
            builder.setFallbackUrl(readableMap.getString("fallbackUrl"));
        }
        if (readableMap.hasKey("minimumVersion")) {
            builder.setMinimumVersion(readableMap.getInt("minimumVersion"));
        }

        return builder.build();
    }

    private AppLinking.HarmonyOSLinkInfo createHarmonyOSLinkInfo(final  ReadableMap readableMap) {
        final AppLinking.HarmonyOSLinkInfo.Builder builder = AppLinking.HarmonyOSLinkInfo.newBuilder();
        if (readableMap.hasKey("harmonyOSPackageName")) {
            builder.setHarmonyOSPackageName(readableMap.getString("harmonyOSPackageName"));
        }
        if (readableMap.hasKey("harmonyOSDeepLink")) {
            builder.setHarmonyOSDeepLink(readableMap.getString("harmonyOSDeepLink"));
        }
        if(readableMap.hasKey("fallbackUrl")) {
            builder.setFallbackUrl(readableMap.getString("fallbackUrl"));
        }

        return builder.build();
    }

    private AppLinking.IOSLinkInfo createIOSLinkInfo(final ReadableMap wholeMap, final ReadableMap iosMap) {
        final AppLinking.IOSLinkInfo.Builder builder = AppLinking.IOSLinkInfo.newBuilder();

        if (iosMap.hasKey(("iosBundleId"))) {
            builder.setBundleId(iosMap.getString("iosBundleId"));
        }

        if (iosMap.hasKey(("iosDeepLink"))) {
            builder.setIOSDeepLink(iosMap.getString("iosDeepLink"));
        }

        if (iosMap.hasKey(("iosFallbackUrl"))) {
            builder.setFallbackUrl(iosMap.getString("iosFallbackUrl"));
        }

        if (iosMap.hasKey(("ipadBundleId"))) {
            builder.setIPadBundleId(iosMap.getString("ipadBundleId"));
        }

        if (iosMap.hasKey(("ipadFallbackUrl"))) {
            builder.setIPadFallbackUrl(iosMap.getString("ipadFallbackUrl"));
        }

        if (wholeMap.hasKey("ITunesLinkInfo")) {
            builder.setITunesConnectCampaignInfo(createITunesLinkInfo(wholeMap.getMap("ITunesLinkInfo")));
        }

        return builder.build();
    }

    private AppLinking.ITunesConnectCampaignInfo createITunesLinkInfo(final ReadableMap readableMap) {
        final AppLinking.ITunesConnectCampaignInfo.Builder builder = AppLinking.ITunesConnectCampaignInfo.newBuilder();

        if (readableMap.hasKey(("iTunesConnectMediaType"))) {
            builder.setMediaType(readableMap.getString("iTunesConnectMediaType"));
        }

        if (readableMap.hasKey("iTunesConnectAffiliateToken")) {
            builder.setAffiliateToken(readableMap.getString("iTunesConnectAffiliateToken"));
        }

        if (readableMap.hasKey("iTunesConnectProviderToken")) {
            builder.setProviderToken(readableMap.getString("iTunesConnectProviderToken"));
        }

        if (readableMap.hasKey("iTunesConnectCampaignToken")) {
            builder.setCampaignToken(readableMap.getString("iTunesConnectCampaignToken"));
        }

        return builder.build();
    }

    private AppLinking.Builder createAppLinkingWithInfo(final ReadableMap readableMap) {
        final AppLinking.Builder builder = AppLinking.newBuilder();

        if (readableMap.hasKey("domainUriPrefix")) {
            builder.setUriPrefix(readableMap.getString("domainUriPrefix"));
        }
        if (readableMap.hasKey("deepLink")) {
            builder.setDeepLink(Uri.parse(readableMap.getString("deepLink")));
        }
        if (readableMap.hasKey("socialCardInfo")) {
            builder.setSocialCardInfo(createSocialCardInfo(readableMap.getMap("socialCardInfo")));
        }
        if (readableMap.hasKey("longLink")) {
            builder.setLongLink(Uri.parse(readableMap.getString("longLink")));
        }
        if (readableMap.hasKey("campaignInfo")) {
            builder.setCampaignInfo(createCampaignInfo(readableMap.getMap("campaignInfo")));
        }
        if (readableMap.hasKey("androidLinkInfo")) {
            builder.setAndroidLinkInfo(createAndroidLinkInfo(readableMap.getMap("androidLinkInfo")));
        }
        if (readableMap.hasKey("harmonyOSLinkInfo")) {
            builder.setHarmonyOSLinkInfo(createHarmonyOSLinkInfo(readableMap.getMap("harmonyOSLinkInfo")));
        }
        if (readableMap.hasKey("IOSLinkInfo")) {
            builder.setIOSLinkInfo(createIOSLinkInfo(readableMap, readableMap.getMap("IOSLinkInfo")));
        }

        if (readableMap.hasKey("previewType")) {
            String previewTypeS = readableMap.getString("previewType");
            if (previewTypeS.equals("AppInfo")) {
                builder.setPreviewType(AppLinking.LinkingPreviewType.AppInfo);
            } else {
                builder.setPreviewType(AppLinking.LinkingPreviewType.SocialInfo);
            }
        }

        if (readableMap.hasKey("expireMinute")) {
            builder.setExpireMinute(readableMap.getInt("expireMinute"));
        }
        if (readableMap.hasKey("linkingPreviewType")) {
            builder.setPreviewType(AppLinking.LinkingPreviewType.valueOf(readableMap.getString("linkingPreviewType")));
        }

        return builder;
    }
}
