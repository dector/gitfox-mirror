import UIKit
import Flutter
import GitFoxSDK

@UIApplicationMain
@objc class AppDelegate: FlutterAppDelegate {
    
    private var sdk: IosSDK!;
    private var sessionInteractor: IosSessionInteractor!;
    
  override func application(
    _ application: UIApplication,
    didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?
  ) -> Bool {
    
    sdk = IosSDK.init(
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
            self.checkOAuthRedirect(call: call, result: result)
        case "login":
            self.login(call: call, result: result)
        case "signInToLastSession":
            self.signInToLastSession(result: result)
        case "retrieveProjectsList":
            self.retrieveProjectsList(result: result)
        case "hasAccount":
            self.hasAccount(result: result)
        case "logout":
            self.logout(result: result)
        default:
            result(FlutterMethodNotImplemented)
        }
    })
    
    GeneratedPluginRegistrant.register(with: self)
    return super.application(application, didFinishLaunchingWithOptions: launchOptions)
  }
    
    // Retrieve OAuth URL
    func getOAuthUrl(result: FlutterResult) {
        guard let oAuthUrl = sessionInteractor?.oauthUrl else {
            result(FlutterMethodNotImplemented)
            return;
        }
        result(oAuthUrl)
    }
    
    // Check OAuth redirect URL
    func checkOAuthRedirect(call: FlutterMethodCall, result: FlutterResult) {
        guard let args = call.arguments else {
            result(FlutterError(code: "-1", message: "iOS could not extract " +
            "flutter arguments in method: (checkOAuthRedirect)", details: nil))
          return
        }
        if let myArgs = args as? [String: Any],
            let url = myArgs["url"] as? String {
            let isRedirected = sessionInteractor.checkOAuthRedirect(url: url)
            result(isRedirected)
        } else {
            result(FlutterError(code: "-1", message: "iOS could not extract " +
            "flutter arguments in method: (checkOAuthRedirect)", details: nil))
        }
    }
    
    // Login
    func login(call: FlutterMethodCall, result: @escaping FlutterResult) {
        guard let args = call.arguments else {
            result(FlutterError(code: "-1", message: "iOS could not extract " +
            "flutter arguments in method: (login)", details: nil))
          return
        }
        if let myArgs = args as? [String: Any],
            let url = myArgs["url"] as? String {
            sessionInteractor.login(oauthRedirect: url, callback: {
                response, err in
                if err == nil {
                    result(true)
                } else {
                    result(FlutterError(code: "-2", message: "Error during login " +
                        "in method (login)", details: nil))
                }
            })
        } else {
            result(FlutterError(code: "-1", message: "iOS could not extract " +
            "flutter arguments in method: (login)", details: nil))
        }
    }
    
    // Check current account status
    func hasAccount(result: FlutterResult) {
        let hasAccount = sdk.getLaunchInteractor().hasAccount
        result(hasAccount)
    }
    
    // Sign in to last session
    func signInToLastSession(result: FlutterResult) {
        sdk.getLaunchInteractor().signInToLastSession()
        result(true)
    }
    
    // User's projects list retrieving
    func retrieveProjectsList(result: @escaping FlutterResult) {
        sdk.getProjectInteractor().getProjectsList(
            archived: false,
            visibility: nil,
            orderBy: .lastActivityAt,
            sort: nil,
            search: nil,
            simple: nil,
            owned: nil,
            membership: nil,
            starred: true,
            page: 0,
            pageSize: 20,
            callback: {
            response, err in
                if err != nil {
                    result(FlutterError(code: "-2", message: "Error during projects list retrieving " +
                        "in method (retrieveProjectsList)", details: nil))
                } else {
                    result(self.convertProjectsToJson(from: response))
                }

        })
    }
    
    func convertProjectsToJson(from items:Array<Project>?) -> String? {
        let jsonCompatibleArray = items?.map { project in
            return [
                "id":project.id,
                "name":project.name,
                "avatarUrl":project.avatarUrl ?? "",
            ]
        } ?? []
        guard let data = try? JSONSerialization.data(withJSONObject: jsonCompatibleArray, options: []) else {
            return nil
        }
        return String(data: data, encoding: String.Encoding.utf8)
    }
    
    // logout
    func logout(result: FlutterResult) {
        let logoutResult = sessionInteractor?.logout()
        result(logoutResult)
    }
}
