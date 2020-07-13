# GitFox
#### This repository contains several parts:
 - Multiplatform SDK for creating clients on various platforms (Android, iOS and browser)
 - Android application GitFox:
   * https://play.google.com/store/apps/details?id=com.gitlab.terrakok.gitfox
   * https://f-droid.org/ru/packages/com.gitlab.terrakok.gitfox/
 - iOS sample of using SDK with Swift

#### SDK
GitFox SDK is library for creating clients application for GitLab servers.
This is some supported features:
 - OAuth authorization on GitLab.com
 - authotrization with private token on any GitLab server
 - multi account and switching bitween them
 - getting information about projects, users, issues, todo's, merge requests and etc.
 - sending commentaries
 - and more

**Request and vote for new functionality!**

#### Build
**Build Android library**  
Debug AAR: `./gradlew :sdk:assembleDebug`  
Release AAR: `./gradlew :sdk:assembleRelease`  
You can find **aar** library here: `./sdk/build/outputs/aar`  

**Build iOS framework**  
For iOS simulator: `./gradlew :sdk:packForXcode`  
For iOS device: `SDK_NAME=iphoneos ./gradlew :sdk:packForXcode`  
You can find **framework** here: `./sdk/build/xcode-frameworks`  

**Build js webpack library**  
For development: `./gradlew :sdk:jsBrowserDevelopmentWebpack`  
For production: `./gradlew :sdk:jsBrowserProductionWebpack`  
You can find **sdk.js** here: `./sdk/build/distributions`  

#### OAuth Authorization sample for Android:
```kotlin
val sdk = SDK.create(
    context,
    oAuthParams = OAuthParams(
        "https://gitlab.com/",
        "appId",
        "appKey",
        "redirectUrl"
    ),
    isDebug = BuildConfig.DEBUG
)

val sessionInteractor = sdk.getSessionInteractor()

webView.webViewClient = object : WebViewClient() {
    override fun shouldOverrideUrlLoading(
        view: WebView,
        request: WebResourceRequest
    ): Boolean {
        val url = request.url.toString()
        if (sessionInteractor.checkOAuthRedirect(url)) {
            launch {
                sessionInteractor.login(url)
                //at the moment you are logged in
                val p = sdk.getProjectInteractor().getProject(2977308)
                println(p)
            }
            return true
        } else {
            view.loadUrl(url)
            return false
        }
    }
}
//start oauth authorization
webView.loadUrl(sessionInteractor.oauthUrl)
```

#### Private token authorization for iOS:
```swift
import GitFoxSDK

let sdk = IosSDK.init(
    oAuthParams: OAuthParams.init(
        endpoint: "https://gitlab.com/",
        appId: "appId",
        appKey: "appKey",
        redirectUrl: "redirectUrl"
   ),
   isDebug: true
)

sdk.getSessionInteractor().loginOnCustomServer(
    serverPath: "https://gitlab.com/",
    token: "put real private token!"
) { result, err in
    if err == nil {
        //at the moment you are logged in
        sdk.getProjectInteractor().getProject(id: 2977308) { result, err in
            if let project = result {
                print(project)
            } else {
                print("error: " + err!.message!)
            }
        }
    } else {
        print("error: " + err!.message!)
    }
}
```

#### Private token authorization for JavaScript:
```javascript
const SDK = new sdk.gitfox.JsSDK(
  new sdk.gitfox.entity.app.session.OAuthParams(
    "https://gitlab.com/", "", "", ""
  ),
  true
);
SDK.getSessionInteractor().loginOnCustomServer(
	"https://gitlab.com/", "put real private token!"
).then( () => {
  SDK.getProjectInteractor().getProject(2977308).then( (project, err) => alert(project) );
});
```
