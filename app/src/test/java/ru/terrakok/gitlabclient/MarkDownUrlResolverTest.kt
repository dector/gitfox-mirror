package ru.terrakok.gitlabclient

import org.junit.Assert
import org.junit.Test
import ru.terrakok.gitlabclient.entity.Project
import ru.terrakok.gitlabclient.entity.Visibility
import ru.terrakok.gitlabclient.model.data.server.MarkDownUrlResolver

/**
 * Created by Eugene Shapovalov (@CraggyHaggy) on 25.10.18.
 */
class MarkDownUrlResolverTest {

    private val markDownUrlResolver = MarkDownUrlResolver()

    private val project = Project(
        1, null, "", Visibility.PUBLIC, "", "", "", null, null, "GitFox", "", "",
        "terrakok/gitlab-client", true, 1, true, true, true, true, true, null, null, 1, null, null, false, null, true,
        1, 1, null, true, null, true, true, true, null
    )

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
}