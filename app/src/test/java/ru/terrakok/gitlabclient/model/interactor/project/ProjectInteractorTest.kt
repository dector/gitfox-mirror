package ru.terrakok.gitlabclient.model.interactor.project

import io.reactivex.Single
import io.reactivex.observers.TestObserver
import org.junit.Test
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.Mockito.*
import ru.terrakok.gitlabclient.TestSchedulers
import ru.terrakok.gitlabclient.entity.File
import ru.terrakok.gitlabclient.entity.Project
import ru.terrakok.gitlabclient.entity.Visibility
import ru.terrakok.gitlabclient.model.repository.project.ProjectRepository
import ru.terrakok.gitlabclient.model.repository.tools.Base64Tools
import ru.terrakok.gitlabclient.presentation.global.MarkDownConverter

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 28.05.17.
 */
class ProjectInteractorTest {
    private val schedulers = TestSchedulers()
    private val base64Tools = mock(Base64Tools::class.java)

    private val testProject = Project(
        id = 42L,
        description = null,
        defaultBranch = "test_br",
        visibility = Visibility.PUBLIC,
        sshUrlToRepo = null,
        httpUrlToRepo = null,
        webUrl = "https://gitlab.com/terrakok/gitlab-client",
        tagList = null,
        owner = null,
        name = "",
        nameWithNamespace = "",
        path = "",
        pathWithNamespace = "",
        issuesEnabled = false,
        openIssuesCount = 0L,
        mergeRequestsEnabled = false,
        jobsEnabled = false,
        wikiEnabled = false,
        snippetsEnabled = false,
        containerRegistryEnabled = false,
        createdAt = null,
        lastActivityAt = null,
        creatorId = 0L,
        namespace = null,
        permissions = null,
        archived = false,
        avatarUrl = null,
        sharedRunnersEnabled = false,
        forksCount = 0L,
        starCount = 0L,
        runnersToken = null,
        publicJobs = false,
        sharedWithGroups = null,
        onlyAllowMergeIfPipelineSucceeds = false,
        onlyAllowMergeIfAllDiscussionsAreResolved = false,
        requestAccessEnabled = false,
        readmeUrl = "https://gitlab.com/terrakok/gitlab-client/blob/test_br/README.md"
    )

    @Test
    fun get_single_project() {
        val repo = mock(ProjectRepository::class.java)
        `when`(repo.getProject(anyLong())).thenReturn(Single.just(testProject))

        val interactor = ProjectInteractor(repo, schedulers, base64Tools)

        val testObserver: TestObserver<Project> = interactor.getProject(testProject.id).test()
        testObserver.awaitTerminalEvent()

        verify(repo, times(1)).getProject(testProject.id)
        testObserver
            .assertValueCount(1)
            .assertNoErrors()
            .assertValue(testProject)
    }

    @Test
    fun get_project_error() {
        val projectId = 42L
        val error = RuntimeException("test error")

        val repo = mock(ProjectRepository::class.java)
        `when`(repo.getProject(anyLong())).thenReturn(Single.error(error))

        val interactor = ProjectInteractor(repo, schedulers, base64Tools)

        val testObserver: TestObserver<Project> = interactor.getProject(testProject.id).test()
        testObserver.awaitTerminalEvent()

        verify(repo, times(1)).getProject(testProject.id)
        testObserver
            .assertValueCount(0)
            .assertError(error)
    }

    @Test
    fun get_project_readme() {
        val raw = "lorem ipsum"
        val testFileContent = "bG9yZW0gaXBzdW0=" //base64 for raw
        val testFile = File("", "", 0L, "", testFileContent, testProject.defaultBranch, "", "", "")

        val repo = mock(ProjectRepository::class.java)
        `when`(repo.getProjectFile(anyLong(), anyString(), anyString())).thenReturn(Single.just(testFile))

        val base64 = mock(Base64Tools::class.java)
        `when`(base64.decode(anyString())).thenReturn(raw)

        val interactor = ProjectInteractor(repo, schedulers, base64)

        val testObserver: TestObserver<String> = interactor.getProjectReadme(testProject).test()
        testObserver.awaitTerminalEvent()

        verify(repo, times(1)).getProjectFile(projectId, "README.md", branchName)
        verify(base64, times(1)).decode(testFileContent)
        testObserver
            .assertValueCount(1)
            .assertNoErrors()
            .assertValue(raw)
    }

    @Test
    fun get_project_readme_error() {
        val error = RuntimeException("test error")

        val repo = mock(ProjectRepository::class.java)
        `when`(repo.getProjectFile(anyLong(), anyString(), anyString())).thenReturn(Single.error(error))

        val mdConverter = mock(MarkDownConverter::class.java)
        val base64 = mock(Base64Tools::class.java)

        val interactor = ProjectInteractor(repo, schedulers, base64)

        val testObserver: TestObserver<String> = interactor.getProjectReadme(testProject).test()
        testObserver.awaitTerminalEvent()

        verify(repo, times(1)).getProjectFile(projectId, "README.md", branchName)
        verifyZeroInteractions(base64)
        verifyZeroInteractions(mdConverter)
        testObserver
            .assertNoValues()
            .assertError(error)
    }

}