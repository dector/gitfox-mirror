package ru.terrakok.gitlabclient.presentation.project.files

import android.os.Bundle

class ProjectFileDestination {

    var defaultPath: String = ""
        private set
    var branchName: String = ""
        private set
    val paths = arrayListOf<String>()

    interface Callback {
        fun onMoveForward(fromRoot: Boolean)
        fun onMoveBack(fromRoot: Boolean)
        fun onBranchChange(branchName: String)
    }

    private var callback: Callback? = null

    fun setCallback(callback: Callback) {
        this.callback = callback
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
        if (!isInitiated()) {
            paths.add(ROOT_PATH)
        }
        callback?.onMoveForward(true)
    }

    fun moveForward(path: String) {
        val fromRoot = isInRoot()
        paths.add(path)
        callback?.onMoveForward(fromRoot)
    }

    fun moveBack() {
        val fromRoot = isInRoot()
        if (!fromRoot) {
            paths.removeAt(paths.lastIndex)
        }
        callback?.onMoveBack(fromRoot)
    }

    fun isInRoot() = paths.size <= 1

    fun changeBranch(newBranchName: String) {
        if (branchName != newBranchName) {
            branchName = newBranchName
            paths.clear()
            paths.add(ROOT_PATH)
            callback?.onBranchChange(newBranchName)
        }
    }

    companion object {
        private const val ROOT_PATH = ""

        private const val KEY_BRANCH_NAME = "key branch name"
        private const val KEY_PATHS = "key paths"
        private const val KEY_DEFAULT_PATH = "key default path"
    }
}