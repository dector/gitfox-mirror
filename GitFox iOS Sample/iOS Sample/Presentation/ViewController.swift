//
//  ViewController.swift
//  iOS Sample
//
//  Created by Ilyas Siraev on 17.05.2020.
//

import UIKit
import GitFoxSDK

class ViewController: UIViewController {
    override func viewDidLoad() {
        super.viewDidLoad()

//        let sdk = IosSDK.init(
//            oAuthParams: OAuthParams.init(
//                endpoint: "https://gitlab.com/",
//                appId: "appId",
//                appKey: "appKey",
//                redirectUrl: "redirectUrl"
//            ),
//            isDebug: true
//        )
//        sdk.getSessionInteractor().loginOnCustomServer(
//            serverPath: "https://gitlab.com/",
//            token: "hGy4YVEBssU6sH_4hz3f"
//        ) { result, err in
//            if err == nil {
//                sdk.getProjectInteractor().getProject(id: 2977308) { result, err in
//                    if let project = result {
//                        print(project)
//                    } else {
//                        print("error: " + err!.message!)
//                    }
//                }
//            } else {
//                print("error: " + err!.message!)
//            }
//        }
    }

    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        presentAuthViewController()
    }

    private func presentAuthViewController() {
        let storyboard = UIStoryboard(name: "Main", bundle: nil)
        let viewController = storyboard.instantiateViewController(identifier: "AuthViewController")
        present(viewController, animated: true, completion: nil)
    }
}
