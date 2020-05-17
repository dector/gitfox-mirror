//
//  GitFox.swift
//  iOS Sample
//
//  Created by Ilyas Siraev on 17.05.2020.
//

import GitFoxSDK

class GitFox {
    static let shared = IosSDK(
        oAuthParams: .init(
            endpoint: "https://gitlab.com/",
            appId: "808b7f51c6634294afd879edd75d5eaf55f1a75e7fe5bd91ca8b7140a5af639d",
            appKey: "a9dd39c8d2e781b65814007ca0f8b555d34f79b4d30c9356c38bb7ad9909c6f3",
            redirectUrl: "app://gitlab.client/"
        ),
        isDebug: true
    )
}
