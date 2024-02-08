# React-Native AGC Applinking

---

## Contents

  - [1. Introduction](#1-introduction)
  - [2. Installation Guide](#2-installation-guide)
    - [Creating a Project in App Gallery Connect](#creating-a-project-in-appgallery-connect)
    - [Enabling App Linking](#enabling-app-linking)
    - [Integrating the React-Native AppLinking Plugin](#integrating-the-react-native-applinking-plugin)
        - [Android App Development](#android-app-development)
            - [Integrating the React-Native AGC AppLinking into the Android Studio](#integrating-the-react-native-agc-applinking-into-the-android-studio)
        - [iOS App Development](#ios-app-development)
            - [Integrating the React-Native AGC AppLinking into the Xcode Project](#integrating-the-react-native-agc-applinking-into-the-xcode-project)
  - [3. API Reference](#3-api-reference)
    - [AgcAppLinking](#agcapplinking)
    - [Constants](#constants)
        - [ShortAppLinkingLengthConstants](#shortapplinkinglengthconstants)
        - [AppLinkingLinkingPreviewTypeConstants](#applinkinglinkingpreviewtypeconstants)
        - [AppLinkingAndroidLinkInfoAndroidOpenTypeConstants](#applinkingandroidlinkinfoandroidopentypeconstants)
  - [4. Configuration and Description](#4-configuration-and-description)
  - [5. Sample Project](#5-sample-project)
  - [6. Licensing and Terms](#7-licensing-and-terms)

---

## 1. Introduction

This module enables communication between **HUAWEI AppLinking Kit** and React Native platform. It exposes all functionality provided by **HUAWEI AppLinking Kit** which allows you to create cross-platform links that can work as defined regardless of whether your app has been installed by a user. A link created in App Linking can be distributed through multiple channels to users. When a user taps the link, the user will be redirected to the specified in-app content. In App Linking, you can create both long and short links.

To identity the source of a user, you can set tracing parameters when creating a link of App Linking to trace traffic sources. By analyzing the link performance of each traffic source based on the tracing parameters, you can find the platform that can achieve the best promotion effect for your app.

---

## 2. Installation Guide

Before you get started, you must register as a HUAWEI Developer and complete identity verification on the [HUAWEI Developer](https://developer.huawei.com/consumer/en/) website. For details, please refer to [Register a HUAWEI ID](https://developer.huawei.com/consumer/en/doc/start/registration-and-verification-0000001053628148).

### Creating a Project in AppGallery Connect

Creating an app in AppGallery Connect is required in order to communicate with the Huawei services. To create an app, perform the following steps:

**Step 1.** Sign in to [AppGallery Connect](https://developer.huawei.com/consumer/en/service/josp/agc/index.html)  and select **My projects**.

**Step 2.** Select your project from the project list or create a new one by clicking the **Add Project** button.

**Step 3.** Go to **Project Setting** > **General information**, and click **Add app**.
If an app exists in the project and you need to add a new one, expand the app selection area on the top of the page and click **Add app**.

**Step 4.** On the **Add app** page, enter the app information, and click **OK**.

### Enabling App Linking

You need to enable App Linking before using it. If you have enabled it, skip this step.

**Step 1.** In [AppGallery Connect](https://developer.huawei.com/consumer/en/service/josp/agc/index.html), click *My projects*.

**Step 2.** Find your project from the project list and click the app for which you need to enable App Linking on the project card.

**Step 3:** Go to **Growing > App Linking**. 

**Step 4:** Click **Enable now** in the upper right corner. 

**Step 5:** Click the **URL prefix** tab, and then click **Add URL prefix** to set a URL prefix.

**Step 6:** Click **Next**. A message is displayed, indicating that the URL has been verified.

For further details, please refer to [Enabling App Linking](https://developer.huawei.com/consumer/en/doc/development/AppGallery-connect-Guides/agc-applinking-getStarted#h1-1602644653245).

### Integrating the React-Native AppLinking Plugin

Before using **@hw-agconnect/react-native-applinking**, ensure that the ReactNative development environment has been installed.

### Install via NPM

```
npm i @hw-agconnect/react-native-applinking
```

#### Android App Development

#### Integrating the React-Native AGC AppLinking into the Android Studio

- Add the AppGallery Connect configuration file of the app to your Android Studio project.

    **Step 1:** Sign in to [AppGallery Connect](https://developer.huawei.com/consumer/en/service/josp/agc/index.html) and click **My projects**.
    
    **Step 2:** Find your **app** project and click the app.
    
    **Step 3:** Go to **Project Setting > App information**. In the **App information** area, download the **agconnect-services.json** file.
    
    **Step 4:** Copy the **agconnect-services.json** file to the app's root directory of your project.
    
- Open the **build.gradle** file in the **android** directory of your React Native project. Navigate into **buildscript**, configure the Maven repository address and agconnect plugin.

    ```groovy
    buildscript {    
    repositories {        
        google()        
        jcenter()        
        maven { url 'https://developer.huawei.com/repo/' }   
    }    

    dependencies {        
        /*          
         * <Other dependencies>        
         */   
        classpath 'com.huawei.agconnect:agcp:1.4.2.300'    
    }
    }
    ```

- Go to **allprojects** then configure the Maven repository address.

    ```groovy
    allprojects {
    repositories {
        /*          
         * <Other repositories>        
         */  
        maven { url 'https://developer.huawei.com/repo/' }
    }
    }
    ```

- Open the **build.gradle** file in the **android/app** directory of your React Native project.

    Set your package name in **defaultConfig** > **applicationId** and set **minSdkVersion** to **19** or **higher**.

    ```groovy
    defaultConfig {
     applicationId "<package_name>"
     minSdkVersion 19
     /*
      * <Other configurations>
      */
    }
    ```
    
    **Package name must match with the _package_name_ entry in _agconnect-services.json_ file.**

- Configure the signature file.
    
    ```gradle
    android {
        /*
         * <Other configurations>
         */

        signingConfigs {
            config {
                storeFile file('<keystore_file>.jks')
                storePassword '<keystore_password>'
                keyAlias '<key_alias>'
                keyPassword '<key_password>'
            }
        }

        buildTypes {
            debug {
                signingConfig signingConfigs.config
            }
            release {
                signingConfig signingConfigs.config
            }
        }
    }
    ```
    
- To use deep links to receive data, you need to add the following configuration to the activity for processing links. Set **android:host** to the domain name in the **deepLink** and **android:scheme** to the custom scheme. When a user taps a link containing this deep link, your app uses this activity to process the link.
   ```xml
   <!-- AndroidManifest.xml. -->
   <intent-filter>
       <action android:name="android.intent.action.VIEW" />
       <category android:name="android.intent.category.DEFAULT" />
       <category android:name="android.intent.category.BROWSABLE" />
       <!-- Add the custom domain name and scheme -->
       <data android:host="<DeepLink_Host>" android:scheme="https" />
   </intent-filter>
   ```
- Make sure you do not specify Android launch mode in your ReactNative project AndroidManifest.xml, in case you have **android:launchMode="singleTask"** in your project, AppLinking will not work properly so remove that line from your AndroidManifest.xml. 

- In the end, your AndroidManifest.xml should look like below:

- ```xml
  <!-- AndroidManifest.xml. --> 
  <activity
      android:name="com.huawei.agc.rn.applinking.exampleAndroid.MainActivity"
      android:label="@string/app_name"
      android:configChanges="keyboard|keyboardHidden|orientation|screenSize|uiMode"
      android:windowSoftInputMode="adjustResize">
      <intent-filter>
         <action android:name="android.intent.action.MAIN" />
         <category android:name="android.intent.category.LAUNCHER" />
      </intent-filter>
      <intent-filter>
         <action android:name="android.intent.action.VIEW" />
         <category android:name="android.intent.category.DEFAULT" />
         <category android:name="android.intent.category.BROWSABLE" />
         <!-- Add the custom domain name and scheme -->
         <data android:host="huawei.com" android:scheme="https" />
     </intent-filter>
  </activity>
  ```

#### iOS App Development

#### Integrating the React-Native AGC AppLinking into the Xcode Project

- Navigate into your project directory and run below command.

    ```
    [project_path]> cd ios/ && pod install
    ```

- Add the AppGallery Connect configuration file of the app to your Xcode project.

    **Step 1:** Sign in to [AppGallery Connect](https://developer.huawei.com/consumer/en/service/josp/agc/index.html) and click **My projects**.
    
    **Step 2:** Find your **app** project and click the app.
    
    **Step 3:** Go to **Project Setting > App information**. In the **App information** area, download the **agconnect-services.plist** file.
    
    **Step 4:** Copy the **agconnect-services.plist** file to the app's root directory of your Xcode project.
    
    Before obtaining the **agconnect-services.plist** file, ensure that you have enabled HUAWEI AppLinking. For details, please refer to [Enabling App Linking](#enabling-app-linking).
    
    If you have made any changes on the Project Setting page, such as setting the data storage location and enabling or managing APIs, you need to download the latest **agconnect-services.plist** file and replace the existing file in the app's root directory.
    
- Initialize the AGConnectCore SDK.

    After the **agconnect-services.plist** file is imported successfully, initialize the AGConnect Core SDK using the config API in AppDelegate. Then, add the applinking configuration in AppDelegate file.
    
    
    Swift sample code for initialization in **AppDelegate.swift**:
    
    
    ```swift
  import AGConnectCore

  @UIApplicationMain
  class AppDelegate: UIResponder, UIApplicationDelegate {
    ...
    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?) -> Bool {
        ...
        // Initializate the AGCInstance SDK using the config API in AppDelegate
        AGCInstance.startUp()
        AGCAppLinking.instance().handle({ link, error in
            if let link = link {
                let userInfo = ["deepLink" : link]
                NotificationCenter.default.post(name: NSNotification.Name("AgcApplinkingNotification"), object: nil, userInfo: userInfo)
            }
        })
        
        return true
    }
    ```


    Objective-C sample code for initialization in **AppDelegate.m**:


    ```objc
    #import "AppDelegate.h"
    ...
    #import <AGConnectCore/AGConnectCore.h>


    @implementation AppDelegate

    - (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions
    {
        ...
        // Initializate the AGCInstance SDK using the config API in AppDelegate
        [AGCInstance startUp];
        [[AGCAppLinking sharedInstance] handleAppLinking:^(AGCResolvedLink *
        _Nullable link, NSError * _Nullable error) {
        if (link) {
        NSDictionary *userInfo = [NSDictionary dictionaryWithObject:link forKey:@"deepLink"];
        [[NSNotificationCenter defaultCenter] postNotificationName: @"AgcApplinkingNotification" object:nil userInfo:userInfo];
    }
    }];
        return YES;
    }
    ...
    @end
    ```

- All the React-Native AGC AppLinking plugin implementations are written in **swift**. 

    Make sure your **Xcode target -> Swift Compiler - General tab** includes **Objective-C Bridging Header** and **Objective-C Generated Interface Header Name** like below:

    <img src="../example/.docs/ios/_ObjC_Header.png"> 

---

## 3. API Reference

## Module Overview

| Module        | Description|
| ------------- | -------------------------------------------- |
| [AgcAppLinking](#agcapplinking)  | Provides methods to initialize AppLinking Kit and implement applinking functions. |

## AgcAppLinking

### Public Method Summary

| Methods                                           | Return Type      | Definition                                                   |
| ------------------------------------------------- | ---------------- | ------------------------------------------------------------ |
| [AgcAppLinking.getAGConnectAppLinkingResolvedLinkData()](#agcapplinkinggetagconnectapplinkingresolvedlinkdata)       | Promise\<object> | Fetches ResolvedLinkData instance with deepLink, clickTimeStamp,socialTitle, socialDescription, socialImageUrl,campaignName, campaignMedium,campaignSource and minimumAppVersion params. |
| [AgcAppLinking.buildShortAppLinking(object)](#agcapplinkingbuildshortapplinkingobject)                                 | Promise\<object> | Asynchronously generates a short link with a string-type suffix. You can specify the suffix as a long or short one. |
| [AgcAppLinking.buildLongAppLinking(object)](#agcapplinkingbuildlongapplinkingobject)                               | Promise\<string> | Generates a long link Uri. |


## Public Methods

### AgcAppLinking.getAGConnectAppLinkingResolvedLinkData()

Fetches ResolvedLinkData instance with deepLink, clickTimeStamp,socialTitle, socialDescription, socialImageUrl,campaignName, campaignMedium,campaignSource and minimumAppVersion params.

###### Return Type

| Return | Definition |
|-------------------------------|-----------------------------|
|Promise\<object> | If the operation is successful, promise will resolve successfully. Otherwise it throws an error.|

###### Call Example

```js
import {Alert} from "react-native";

/**
 * Fetches ResolvedLinkData instance with deepLink, clickTimeStamp, socialTitle, 
 * socialDescription, socialImageUrl, campaignName, campaignMedium, campaignSource and minimumAppVersion params.
 */
AgcAppLinking.getAGConnectAppLinkingResolvedLinkData().then(result => {
    Alert.alert("[getAGConnectAppLinkingResolvedLinkData] " + JSON.stringify(result));
}).catch((err) => {
    Alert.alert("[getAGConnectAppLinkingResolvedLinkData] Error/Exception: " + JSON.stringify(err));
});
```

###### Example Result

```js
{
    campaignMedium = NAME;
    campaignName = MEDIUM;
    campaignSource = SOURCE;
    clickTimeStamp = "2020-12-01 07:40:15 +0000";
    deepLink = "huawei.com://testId=107";
    socialDescription = "SOME_DESCRIPTION";
    socialImageUrl = "https://www-file.huawei.com/-/media/corporate/images/home/logo/huawei_logo.png";
    socialTitle = "SOME_TITLE";
}
```

### AgcAppLinking.buildShortAppLinking(object)

Asynchronously generates a short link with a string-type suffix. You can specify the suffix as a long or short one.

###### Parameters

| Name    | Type                  | Description            |
| ------- | --------------------- | ---------------------- |
| object   | `object` | Refers to parameters that will get domainUriPrefix, deepLink and socialCardInfo, campaignInfo, androidLinkInfo, expireMinute, linkingPreviewType. |

###### Return Type

| Return | Definition |
|-------------------------------|-----------------------------|
|Promise\<object> | If the operation is successful, promise will resolve successfully. Otherwise it throws an error.|

###### Call Example

```js
import {Alert, Clipboard} from "react-native";

/** socialCardInfo object is optional. **/
const socialCardInfo = {
    "description": "SOME_DESCRIPTION",
    "title": "SOME_TITLE",
    "imageUrl": "https://www-file.huawei.com/-/media/corporate/images/home/logo/huawei_logo.png"
}

/** campaignInfo object is optional. **/
const campaignInfo = {
    "medium": "MEDIUM",
    "name": "NAME",
    "source": "SOURCE"
}

/** androidLinkInfo object is optional. **/
const androidLinkInfo = {
    "packageName": "com.huawei.agc.rn.applinking.exampleAndroid",
    "androidDeepLink": "https://huawei.com/",
    "openType": AgcAppLinking.AppLinkingAndroidLinkInfoAndroidOpenTypeConstants.APP_GALLERY
}
/** harmonyOSLinkInfo object is optional. **/
const harmonyOSLinkInfo = {
    "harmonyOSPackageName": "com.huawei.agc.rn.applinking.exampleAndroid",
    "harmonyOSDeepLink": "https://huawei.com/",
    "fallbackUrl": "https://huawei.com/"
}
/** IOSLinkInfo object is optional. **/
const IOSLinkInfo = {
    "iosBundleId": "com.huawei.agc.rn.applinking.exampleAndroid",
    "iosDeepLink": "huawei.com://testId=107"
}

/** ITunesLinkInfo object is optional. **/
const ITunesLinkInfo = {
    "iTunesConnectMediaType": "iTunesConnectMediaType",
    "iTunesConnectAffiliateToken": "iTunesConnectAffiliateToken"
}

/** domainUriPrefix & deepLink fields are mandatory in building short link, other params are optional **/
const object = {
    "shortAppLinkingLength": AgcAppLinking.ShortAppLinkingLengthConstants.LONG,
    "domainUriPrefix": "https://applinkingrndemo1.dre.agconnect.link",
    "deepLink": "huawei.com://testId=107",
    "socialCardInfo": socialCardInfo,
    "campaignInfo": campaignInfo,
    "androidLinkInfo": androidLinkInfo,
    "harmonyOSLinkInfo": harmonyOSLinkInfo,
    "IOSLinkInfo": IOSLinkInfo,
    "ITunesLinkInfo": ITunesLinkInfo,
    "previewType": AgcAppLinking.AppLinkingLinkingPreviewTypeConstants.APP_INFO
}
/**
 * Asynchronously generates a short link with a string-type suffix. You can specify the suffix as a long or short one.
 */
AgcAppLinking.buildShortAppLinking(object).then(result => {
    Alert.alert(
        "[buildShortAppLinking] ",
        JSON.stringify(result),
        [
            {
                text: "Copy Short Link", onPress: () => {
                        Clipboard.setString(result.shortLink)
                }
            }
        ],
        {cancelable: true}
    );
}).catch((err) => {
    Alert.alert("[buildShortAppLinking] Error/Exception: " + JSON.stringify(err));
});

```
###### Example Result

```js
{
    shortLink = "https://applinkingrndemo1.dre.agconnect.link/pEKxmykiXd7BnOATm";
}
```

### AgcAppLinking.buildLongAppLinking(object)

Generates a long link Uri.

###### Parameters

| Name    | Type                  | Description            |
| ------- | --------------------- | ---------------------- |
| object   | `object` | Refers to parameters that will get domainUriPrefix, deepLink and socialCardInfo, campaignInfo, androidLinkInfo, expireMinute, linkingPreviewType. |

###### Return Type

| Return | Definition |
|-------------------------------|-----------------------------|
|Promise\<string> | If the operation is successful, promise will resolve successfully. Otherwise it throws an error.|

###### Call Example

```js
import {Alert} from "react-native";

const object = {
    "shortAppLinkingLength": AgcAppLinking.ShortAppLinkingLengthConstants.SHORT,
    "domainUriPrefix": "https://applinkingrndemo1.dre.agconnect.link",
    "deepLink": "https://huawei.com/agc"
}
/**
 * Generates a long link Uri.
 */
AgcAppLinking.buildLongAppLinking(object).then(result => {
    Alert.alert("[buildLongAppLinking] " + JSON.stringify(result));
    this.createCustomView("buildLongAppLinking :  ", JSON.stringify(result) + "")
}).catch((err) => {
    Alert.alert("[buildLongAppLinking] Error/Exception: " + JSON.stringify(err));
});
```

## Constants

##### ShortAppLinkingLengthConstants

- Specifies whether the string-type suffix of a short link is long or short. This class is used to create a short link.

| Field | Type   | Description                                                                                  |
| ----- | ------ | -------------------------------------------------------------------------------------------- |
| SHORT | string | A short link uses a short string-type suffix containing four or more characters as required. |
| LONG  | string | A short link uses a long string-type suffix containing 17 characters.                        |

##### AppLinkingLinkingPreviewTypeConstants

- Preview page style of a link of App Linking.

| Field       | Type   | Description                                                                        |
| ----------- | ------ | ---------------------------------------------------------------------------------- |
| APP_INFO    | string | Displays the preview page with app information.                                    |
| SOCIAL_INFO | string | Displays the preview page with the card of a link displayed during social sharing. |

##### AppLinkingAndroidLinkInfoAndroidOpenTypeConstants

- Action triggered when a link is tapped but the target app is not installed.

| Field        | Type   | Description                                                |
| ------------ | ------ | ---------------------------------------------------------- |
| APP_GALLERY  | string | Displays the app details page on AppGallery.               |
| LOCAL_MARKET | string | Displays the app details page in local app market.         |
| CUSTOM_URL   | string | Displays the app details page using the fallbackUrl field. |


---

## 4. Configuration and Description

[Enabling App Linking](https://developer.huawei.com/consumer/en/doc/development/AppGallery-connect-Guides/agc-applinking-getStarted-ios)

### Accessing Analytics Kit

To use analytics feature, 

- Navigate into your /android/app/build.gradle and add build dependencies in the dependencies section.
   
    ```
    dependencies {
        implementation 'com.huawei.hms:hianalytics:5.1.0.301'
    }
    ```
- Navigate into your /ios file and edit the Podfile file to add the pod dependency 'HiAnalytics'
    
    - Example Podfile file:

        ```
        # Pods for AGCAppLinkingDemo
        pod 'HiAnalytics'
        ```
    
    - Run pod install to install the pods.
    
       ```
       $ pod install
       ```
    
    - Initialize the Analytics SDK using the config API in AppDelegate in iOS platform.

        Sample code for initialization in AppDelegate.m:
    
        ```
        #import "AppDelegate.h"
        #import <HiAnalytics/HiAnalytics.h>
 
        @interface AppDelegate ()
 
        @end
 
        @implementation AppDelegate
        ...
        // Customize the service logic after app launch.
        - (BOOL)Application:(UIApplication *)Application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions {
        // Initialize the Analytics SDK.
        [HiAnalytics config];   
         return YES;
        }
        ...
        @end
        ```
    
    For further information please refer to [Analytics Kit Service Guide](https://developer.huawei.com/consumer/en/doc/development/HMSCore-Guides/introduction-0000001050745149).
    

### Configuring Obfuscation Scripts

Before building the APK, configure obfuscation scripts to prevent the AppGallery Connect SDK from being obfuscated. If obfuscation arises, the AppGallery Connect SDK may not function properly.

```
-ignorewarnings
-keepattributes *Annotation*
-keepattributes Exceptions
-keepattributes InnerClasses
-keepattributes Signature
-keep class com.hianalytics.android.**{*;}
-keep class com.huawei.updatesdk.**{*;}
-keep class com.huawei.hms.**{*;}
-keep class com.huawei.agc.**{*;}
-keep class com.huawei.agconnect.**{*;}
-repackageclasses
```


---

## 5. Sample Project

This plugin includes a demo project in the [example](example) folder, there you can find more usage examples.

---

## 6. Licensing and Terms

AGC React-Native Plugin is licensed under [Apache 2.0 license](LICENCE)

