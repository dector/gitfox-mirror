import UIKit
import Flutter
import GitFoxSDK

@UIApplicationMain
@objc class AppDelegate: FlutterAppDelegate {
    
    private var sessionInteractor: IosSessionInteractor!;
    
  override func application(
    _ application: UIApplication,
    didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?
  ) -> Bool {
    
    let sdk = IosSDK.init(
        oAuthParams: OAuthParams.init(
            endpoint: "https://gitlab.com/",
            appId: "808b7f51c6634294afd879edd75d5eaf55f1a75e7fe5bd91ca8b7140a5af639d",
            appKey: "a9dd39c8d2e781b65814007ca0f8b555d34f79b4d30c9356c38bb7ad9909c6f3",
            redirectUrl: "app://gitlab.client/"
       ),
       isDebug: true
    )
    
    sessionInteractor = sdk.getSessionInteractor()

    let controller : FlutterViewController = window?.rootViewController as! FlutterViewController
    let gitfoxChannel = FlutterMethodChannel(name: "gitfox/platform",
                                              binaryMessenger: controller.binaryMessenger)
    gitfoxChannel.setMethodCallHandler({
      (call: FlutterMethodCall, result: @escaping FlutterResult) -> Void in
      
        switch call.method {
        case "getOAuthUrl":
            self.getOAuthUrl(result: result)
        case "checkOAuthRedirect":
            result(FlutterMethodNotImplemented)
        case "login":
            result(FlutterMethodNotImplemented)
        case "signInToLastSession":
            result(FlutterMethodNotImplemented)
        case "retrieveProjectsList":
            result(FlutterMethodNotImplemented)
        case "hasAccount":
            result(FlutterMethodNotImplemented)
        default:
            result(FlutterMethodNotImplemented)
        }
    })
    
    GeneratedPluginRegistrant.register(with: self)
    return super.application(application, didFinishLaunchingWithOptions: launchOptions)
  }
    
    func getOAuthUrl(result: FlutterResult) {
        guard let oAuthUrl = sessionInteractor?.oauthUrl else {
            result(FlutterMethodNotImplemented)
            return;
        }
        result(oAuthUrl)
    }

}
