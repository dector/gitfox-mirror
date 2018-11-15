package ru.terrakok.gitlabclient.presentation.files

import android.os.Bundle

class ProjectFileDestination {

    var defaultPath: String = ""
        private set
    var branchName: String = ""
        private set
    val paths = arrayListOf<String>()

    interface OnProjectFileDestinationListener {
        fun onMoveForward(fromRoot: Boolean)
        fun onMoveBack(fromRoot: Boolean)
        fun onBranchChange(branchName: String)
    }

    private lateinit var onProjectFileDestinationListener: OnProjectFileDestinationListener

    fun setOnProjectFileDestinationListener(onProjectFileDestinationListener: OnProjectFileDestinationListener) {
        this.onProjectFileDestinationListener = onProjectFileDestinationListener
    }

    fun init(defaultPath: String, branchName: String) {
        this.defaultPath = defaultPath
        this.branchName = branchName
    }

    fun saveState(outState: Bundle) {
        outState.putString(KEY_DEFAULT_PATH, defaultPath)
        outState.putString(KEY_BRANCH_NAME, branchName)
        outState.putStringArrayList(KEY_PATHS, paths)
    }

    fun restoreState(savedState: Bundle) {
        defaultPath = savedState.getString(KEY_DEFAULT_PATH)!!
        branchName = savedState.getString(KEY_BRANCH_NAME)!!
        paths.addAll(savedState.getStringArrayList(KEY_PATHS)!!)
    }

    fun isInitiated() = defaultPath.isNotEmpty() && branchName.isNotEmpty() && paths.isNotEmpty()

    fun moveToRoot() {
        val fromRoot = isInitiated() && !isInRoot()
        if (!isInitiated()) {
            paths.add(ROOT_PATH)
        }
        onProjectFileDestinationListener.onMoveForward(fromRoot)
    }

    fun moveForward(path: String) {
        val fromRoot = isInRoot()
        paths.add(path)
        onProjectFileDestinationListener.onMoveForward(fromRoot)
    }

    fun moveBack() {
        val fromRoot = isInRoot()
        if (!fromRoot) {
            paths.removeAt(paths.lastIndex)
        }
        onProjectFileDestinationListener.onMoveBack(fromRoot)
    }

    fun isInRoot() = paths.size == 1

    fun changeBranch(branchName: String) {
        if (this.branchName != branchName) {
            this.branchName = branchName
            this.paths.clear()
            this.paths.add(ROOT_PATH)
            onProjectFileDestinationListener.onBranchChange(branchName)
        }
    }

    companion object {
        private const val ROOT_PATH = ""

        private const val KEY_BRANCH_NAME = "key branch name"
        private const val KEY_PATHS = "key paths"
        private const val KEY_DEFAULT_PATH = "key default path"
    }
}