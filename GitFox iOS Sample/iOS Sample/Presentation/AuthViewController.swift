//
//  AuthViewController.swift
//  iOS Sample
//
//  Created by Ilyas Siraev on 17.05.2020.
//

import UIKit
import WebKit

class AuthViewController: UIViewController, WKNavigationDelegate {
    @IBOutlet private var webView: WKWebView!

    var onAuthorized: (() -> Void)?

    override func viewDidLoad() {
        super.viewDidLoad()
        webView.navigationDelegate = self

        let urlRequest = URLRequest(url: GitFox.shared.oauthUrl)
        webView.load(urlRequest)
    }

    // MARK: - WKNavigationDelegate

    public func webView(
        _ webView: WKWebView,
        decidePolicyFor navigationAction: WKNavigationAction,
        decisionHandler: @escaping (WKNavigationActionPolicy) -> Swift.Void
    ) {
        guard
            let url = navigationAction.request.url,
            GitFox.shared.checkOAuthRedirect(url: url)
        else {
            decisionHandler(.allow)
            return
        }

        decisionHandler(.cancel)
        GitFox.shared.login(oauthRedirect: url) { result in
            switch result {
                case .failure(let error):
                    print(error.localizedDescription)
                case .success:
                    self.onAuthorized?()
            }
        }
    }
}
