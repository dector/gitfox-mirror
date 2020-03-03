package ru.terrakok.gitlabclient

import org.junit.Assert
import org.junit.Test
import ru.terrakok.gitlabclient.model.data.server.MarkDownUrlResolver

/**
 * Created by Eugene Shapovalov (@CraggyHaggy) on 25.10.18.
 */
class MarkDownUrlResolverTest {

    private val markDownUrlResolver = MarkDownUrlResolver()
    private val project = TestData.getProject(1)

    @Test
    fun check_markdown_without_image() {
        val body = "no image body"
        val expected = "no image body"
        val resolved = markDownUrlResolver.resolve(body, project)

        Assert.assertEquals(expected, resolved)
    }

    @Test
    fun check_markdown_only_image() {
        val body = "![CragHag](/uploads/69c4ef83b86c66eb3f147915d26c427e/CragHag.png)"
        val expected = "![CragHag](terrakok/gitlab-client/uploads/69c4ef83b86c66eb3f147915d26c427e/CragHag.png)"
        val resolved = markDownUrlResolver.resolve(body, project)

        Assert.assertEquals(expected, resolved)
    }

    @Test
    fun check_markdown_with_text_and_images() {
        val body = "* **Issue**\n" +
            "*  Web:\n" +
            "\n" +
            "![Screen_Shot_2018-09-24_at_23.34.07](/uploads/6e1a6bbed7cba2219624d48f2eef731c/Screen_Shot_2018-09-24_at_23.34.07.png)\n" +
            "\n" +
            "![Screen_Shot_2018-09-24_at_23.34.53](/uploads/a7474cab52c1b53dcddb7a3ce6ac9af3/Screen_Shot_2018-09-24_at_23.34.53.png)\n" +
            "\n" +
            "*  Mobile:\n" +
            "\n" +
            "![device-2018-09-24-234043](/uploads/caf09f19e9cef4f5dab9cfd01c4b66d6/device-2018-09-24-234043.png)\n" +
            "\n" +
            "![device-2018-09-24-234018](/uploads/1f4b871a039e197c9c59b6d2cb89f1e7/device-2018-09-24-234018.png)"
        val expected = "* **Issue**\n" +
            "*  Web:\n" +
            "\n" +
            "![Screen_Shot_2018-09-24_at_23.34.07](terrakok/gitlab-client/uploads/6e1a6bbed7cba2219624d48f2eef731c/Screen_Shot_2018-09-24_at_23.34.07.png)\n" +
            "\n" +
            "![Screen_Shot_2018-09-24_at_23.34.53](terrakok/gitlab-client/uploads/a7474cab52c1b53dcddb7a3ce6ac9af3/Screen_Shot_2018-09-24_at_23.34.53.png)\n" +
            "\n" +
            "*  Mobile:\n" +
            "\n" +
            "![device-2018-09-24-234043](terrakok/gitlab-client/uploads/caf09f19e9cef4f5dab9cfd01c4b66d6/device-2018-09-24-234043.png)\n" +
            "\n" +
            "![device-2018-09-24-234018](terrakok/gitlab-client/uploads/1f4b871a039e197c9c59b6d2cb89f1e7/device-2018-09-24-234018.png)"
        val resolved = markDownUrlResolver.resolve(body, project)

        Assert.assertEquals(expected, resolved)
    }

    @Test
    fun check_markdown_with_images_in_table() {
        val body = "## What does this MR do?\n" +
            "\n" +
            "Make vertical margin of page titles symmetric and aligned to grid.\n" +
            "\n" +
            "## Screenshots\n" +
            "\n" +
            "| Before | After |\n" +
            "| --- | --- |\n" +
            "| ![Screen_Shot_2018-09-24_at_18.08.23](/uploads/01237180208137f436d5f2a561ab1d74/Screen_Shot_2018-09-24_at_18.08.23.png) | ![Screen_Shot_2018-09-24_at_18.54.10](/uploads/1a29578cdac9a266888709f4f9b607fa/Screen_Shot_2018-09-24_at_18.54.10.png) |\n" +
            "\n" +
            "## What are the relevant issue numbers?\n" +
            "\n" +
            "https://gitlab.com/gitlab-org/gitlab-ee/merge_requests/7433#note_103849055\n" +
            "\n" +
            "## Does this MR meet the acceptance criteria?\n" +
            "\n" +
            "- [ ] [Changelog entry](https://docs.gitlab.com/ee/development/changelog.html) added, if necessary\n" +
            "- [ ] [Documentation created/updated](https://docs.gitlab.com/ee/development/documentation/index.html#contributing-to-docs)\n" +
            "- [ ] [Tests added for this feature/bug](https://docs.gitlab.com/ee/development/testing_guide/index.html)\n" +
            "- [ ] Conforms to the [code review guidelines](https://docs.gitlab.com/ee/development/code_review.html)\n" +
            "- [ ] Conforms to the [merge request performance guidelines](https://docs.gitlab.com/ee/development/merge_request_performance_guidelines.html)\n" +
            "- [ ] Conforms to the [style guides](https://gitlab.com/gitlab-org/gitlab-ee/blob/master/CONTRIBUTING.md#style-guides)\n" +
            "- [ ] Conforms to the [database guides](https://docs.gitlab.com/ee/development/README.html#databases-guides)"
        val expected = "## What does this MR do?\n" +
            "\n" +
            "Make vertical margin of page titles symmetric and aligned to grid.\n" +
            "\n" +
            "## Screenshots\n" +
            "\n" +
            "| Before | After |\n" +
            "| --- | --- |\n" +
            "| ![Screen_Shot_2018-09-24_at_18.08.23](terrakok/gitlab-client/uploads/01237180208137f436d5f2a561ab1d74/Screen_Shot_2018-09-24_at_18.08.23.png) | ![Screen_Shot_2018-09-24_at_18.54.10](terrakok/gitlab-client/uploads/1a29578cdac9a266888709f4f9b607fa/Screen_Shot_2018-09-24_at_18.54.10.png) |\n" +
            "\n" +
            "## What are the relevant issue numbers?\n" +
            "\n" +
            "https://gitlab.com/gitlab-org/gitlab-ee/merge_requests/7433#note_103849055\n" +
            "\n" +
            "## Does this MR meet the acceptance criteria?\n" +
            "\n" +
            "- [ ] [Changelog entry](https://docs.gitlab.com/ee/development/changelog.html) added, if necessary\n" +
            "- [ ] [Documentation created/updated](https://docs.gitlab.com/ee/development/documentation/index.html#contributing-to-docs)\n" +
            "- [ ] [Tests added for this feature/bug](https://docs.gitlab.com/ee/development/testing_guide/index.html)\n" +
            "- [ ] Conforms to the [code review guidelines](https://docs.gitlab.com/ee/development/code_review.html)\n" +
            "- [ ] Conforms to the [merge request performance guidelines](https://docs.gitlab.com/ee/development/merge_request_performance_guidelines.html)\n" +
            "- [ ] Conforms to the [style guides](https://gitlab.com/gitlab-org/gitlab-ee/blob/master/CONTRIBUTING.md#style-guides)\n" +
            "- [ ] Conforms to the [database guides](https://docs.gitlab.com/ee/development/README.html#databases-guides)"
        val resolved = markDownUrlResolver.resolve(body, project)

        Assert.assertEquals(expected, resolved)
    }
}
