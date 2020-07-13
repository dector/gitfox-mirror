//
//  ProjectTableViewCell.swift
//  iOS Sample
//
//  Created by Ilyas Siraev on 24.05.2020.
//

import UIKit
import SDWebImage

class ProjectTableViewCell: UITableViewCell {
    @IBOutlet private var nameLabel: UILabel!
    @IBOutlet private var repositoryImageView: UIImageView!
    @IBOutlet private var starsLabel: UILabel!

    override init(style: UITableViewCell.CellStyle, reuseIdentifier: String?) {
        super.init(style: style, reuseIdentifier: reuseIdentifier)
        selectionStyle = .none
    }

    required init?(coder: NSCoder) {
        super.init(coder: coder)
        selectionStyle = .none
    }

    override func prepareForReuse() {
        super.prepareForReuse()
        repositoryImageView.sd_cancelCurrentImageLoad()
    }

    func setup(name: String, repositoryImageURLString: String?, starCount: Int) {
        nameLabel.text = name
        starsLabel.text = "\(starCount)"
        if let repositoryImageURLString = repositoryImageURLString {
            repositoryImageView.sd_setImage(with: URL(string: repositoryImageURLString), completed: nil)
        }
    }
}
