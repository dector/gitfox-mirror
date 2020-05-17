//
//  ViewController.swift
//  iOS Sample
//
//  Created by Ilyas Siraev on 17.05.2020.
//

import UIKit

class ViewController: UIViewController {
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)

        if GitFox.shared.hasAccount {
            GitFox.shared.signInToLastSession()
            loadProjects()
        } else {
            presentAuthViewController()
        }
    }

    private func presentAuthViewController() {
        let storyboard = UIStoryboard(name: "Main", bundle: nil)
        let viewController = storyboard.instantiateViewController(
            identifier: "AuthViewController"
        ) as! AuthViewController
        viewController.onAuthorized = { [weak self] in
            guard let self = self else { return }
            self.loadProjects()
            self.presentedViewController?.dismiss(animated: true, completion: nil)
        }
        present(viewController, animated: true, completion: nil)
    }

    private func loadProjects() {
        GitFox.shared.getProjectsList(page: 0, pageSize: 10) { result in
            switch result {
                case .success(let projects):
                    projects.forEach { print($0.name) }
                case .failure(let error):
                    print(error.localizedDescription)
            }
        }
    }
}
