//
//  ViewController.swift
//  iOS Sample
//
//  Created by Ilyas Siraev on 17.05.2020.
//

import UIKit
import GitFoxSDK

class ViewController: UIViewController, UITableViewDataSource {
    @IBOutlet private var tableView: UITableView!

    private var projects: [Project] = [] {
        didSet { tableView.reloadData() }
    }

    override func viewDidLoad() {
        super.viewDidLoad()
        tableView.dataSource = self
        tableView.tableFooterView = .init()
    }

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
        GitFox.shared.getProjectsList(page: 0, pageSize: 10) { [weak self] result in
            guard let self = self else { return }
            switch result {
                case .success(let projects):
                    self.projects = projects
                case .failure(let error):
                    print(error.localizedDescription)
            }
        }
    }

    // MARK: - UITableViewDataSource

    func numberOfSections(in tableView: UITableView) -> Int {
        1
    }

    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        projects.count
    }

    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(
            withIdentifier: "ProjectTableViewCell",
            for: indexPath
        ) as! ProjectTableViewCell
        cell.setup(name: projects[indexPath.row].name)
        return cell
    }
}

class ProjectTableViewCell: UITableViewCell {
    @IBOutlet private var nameLabel: UILabel!
    @IBOutlet private var repositoryImageView: UIImageView!

    func setup(name: String) {
        nameLabel.text = name
    }
}
