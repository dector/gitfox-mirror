//
//  GitFox.swift
//  iOS Sample
//
//  Created by Ilyas Siraev on 17.05.2020.
//

import GitFoxSDK

class GitFox {
    enum SDKError: Error {
        case error(KotlinException)
    }

    static let shared = GitFox()

    private let sdk = IosSDK(
        oAuthParams: .init(
            endpoint: "https://gitlab.com/",
            appId: "808b7f51c6634294afd879edd75d5eaf55f1a75e7fe5bd91ca8b7140a5af639d",
            appKey: "a9dd39c8d2e781b65814007ca0f8b555d34f79b4d30c9356c38bb7ad9909c6f3",
            redirectUrl: "app://gitlab.client/"
        ),
        isDebug: true
    )

    var oauthUrl: URL {
        URL(string: sdk.getSessionInteractor().oauthUrl)!
    }

    var hasAccount: Bool {
        sdk.getLaunchInteractor().hasAccount
    }

    func checkOAuthRedirect(url: URL) -> Bool {
        sdk.getSessionInteractor().checkOAuthRedirect(url: url.absoluteString)
    }

    func signInToLastSession() {
        sdk.getLaunchInteractor().signInToLastSession()
    }

    func login(
        oauthRedirect: URL,
        completion: @escaping (Result<Void, SDKError>) -> Void
    ) {
        sdk.getSessionInteractor().login(
            oauthRedirect: oauthRedirect.absoluteString
        ) { _, error in
            if let error = error {
                completion(.failure(.error(error)))
            } else {
                completion(.success(()))
            }
        }
    }

    func logout() {
        sdk.getSessionInteractor().logout()
    }

    func getProjectsList(
        page: Int,
        pageSize: Int,
        completion: @escaping (Result<[Project], SDKError>) -> Void
    ) {
        sdk.getProjectInteractor().getProjectsList(
            archived: false,
            visibility: nil,
            orderBy: .name,
            sort: .asc,
            search: nil,
            simple: nil,
            owned: false,
            membership: false,
            starred: true,
            page: Int32(page),
            pageSize: Int32(pageSize)
        ) { projects, error in
            if let error = error {
                completion(.failure(.error(error)))
            } else {
                completion(.success(projects?.compactMap { $0 } ?? []))
            }
        }
    }
}
