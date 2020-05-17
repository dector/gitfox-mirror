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

    private lazy var sessionInteractor = GitFox.shared.getSessionInteractor()

    override func viewDidLoad() {
        super.viewDidLoad()
        webView.navigationDelegate = self

        let urlRequest = URLRequest(url: URL(string: sessionInteractor.oauthUrl)!)
        webView.load(urlRequest)
    }

    private func dismiss() {
        dismiss(animated: true, completion: nil)
    }

    // MARK: - WKNavigationDelegate

    public func webView(
        _ webView: WKWebView,
        decidePolicyFor navigationAction: WKNavigationAction,
        decisionHandler: @escaping (WKNavigationActionPolicy) -> Swift.Void
    ) {
        guard
            let url = navigationAction.request.url,
            sessionInteractor.checkOAuthRedirect(url: url.absoluteString)
        else {
            decisionHandler(.allow)
            return
        }

        decisionHandler(.cancel)
        sessionInteractor.login(oauthRedirect: url.absoluteString) { [weak self] unit, exception in
            guard let self = self else { return }
            if let exception = exception {
                fatalError(exception.debugDescription)
            }
            self.dismiss()
        }
    }
}
