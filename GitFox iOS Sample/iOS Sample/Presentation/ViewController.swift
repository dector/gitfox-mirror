//
//  ViewController.swift
//  iOS Sample
//
//  Created by Ilyas Siraev on 17.05.2020.
//

import UIKit
import GitFoxSDK

class ViewController: UIViewController, UITableViewDataSource, UITableViewDelegate {
    @IBOutlet private var tableView: UITableView!
    @IBOutlet private var loginButton: UIButton!

    private lazy var logoutButton: UIBarButtonItem = {
        let button = UIBarButtonItem(
            title: "Logout",
            style: .plain,
            target: self,
            action: #selector(didTapLogoutButton)
        )
        return button
    }()

    private var projects: [Project] = [] {
        didSet { tableView.reloadData() }
    }

    override func viewDidLoad() {
        super.viewDidLoad()
        setupUI()
        checkSessionAndUpdateUI()
    }

    private func setupUI() {
        title = "GitFox iOS Sample"
        navigationItem.rightBarButtonItem = logoutButton

        loginButton.layer.borderColor = UIColor.blue.cgColor
        loginButton.layer.borderWidth = 1
        loginButton.layer.cornerRadius = 8
        loginButton.setTitleColor(.blue, for: .normal)
        loginButton.backgroundColor = .white

        tableView.dataSource = self
        tableView.delegate = self
        tableView.tableFooterView = .init()
    }

    private func checkSessionAndUpdateUI() {
        if GitFox.shared.hasAccount {
            updateLoginButton(isHidden: true)
            updateLogoutButton(isHidden: false)
            updateTableView(isHidden: false)
            GitFox.shared.signInToLastSession()
            loadProjects()
        } else {
            updateLoginButton(isHidden: false)
            updateLogoutButton(isHidden: true)
            updateTableView(isHidden: true)
        }
    }

    private func updateLoginButton(isHidden: Bool) {
        loginButton.isHidden = isHidden
    }

    private func updateTableView(isHidden: Bool) {
        tableView.isHidden = isHidden
    }

    private func updateLogoutButton(isHidden: Bool) {
        if isHidden {
            navigationItem.rightBarButtonItem = nil
        } else {
            navigationItem.rightBarButtonItem = logoutButton
        }
    }

    private func presentAuthViewController() {
        let storyboard = UIStoryboard(name: "Main", bundle: nil)
        let viewController = storyboard.instantiateViewController(
            identifier: "AuthViewController"
        ) as! AuthViewController
        viewController.onAuthorized = { [weak self] in
            guard let self = self else { return }
            self.presentedViewController?.dismiss(animated: true, completion: nil)
            self.checkSessionAndUpdateUI()
        }
        present(viewController, animated: true, completion: nil)
    }

    private var isLoading = false
    private var currentPage = 1
    private var loadAllProjects = false

    private func loadProjects() {
        guard !isLoading, !loadAllProjects else { return }
        isLoading = true
        GitFox.shared.getProjectsList(page: currentPage, pageSize: 10) { [weak self] result in
            guard let self = self else { return }
            switch result {
                case .success(let projects):
                    self.projects += projects
                    if !projects.isEmpty {
                        self.currentPage += 1
                    } else {
                        self.loadAllProjects = true
                    }
                case .failure(let error):
                    print(error.localizedDescription)
            }
            self.isLoading = false
        }
    }

    @IBAction private func didTapLoginButton() {
        presentAuthViewController()
    }

    @objc
    private func didTapLogoutButton() {
        GitFox.shared.logout()
        checkSessionAndUpdateUI()
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
        let project = projects[indexPath.row]
        cell.setup(
            name: project.name,
            repositoryImageURLString: project.avatarUrl,
            starCount: project.starCount as! Int
        )
        return cell
    }

    // MARK: - UITableViewDelegate

    func tableView(_ tableView: UITableView, willDisplay cell: UITableViewCell, forRowAt indexPath: IndexPath) {
        if indexPath.row >= projects.count - 5 {
            loadProjects()
        }
    }
}
