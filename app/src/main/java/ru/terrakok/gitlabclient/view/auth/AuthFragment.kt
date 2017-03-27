package ru.terrakok.gitlabclient.view.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.view.global.BaseFragment

/**
 * @author Konstantin Tskhovrebov (aka terrakok). Date: 27.03.17
 */
class AuthFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?)
            = inflater.inflate(R.layout.fragment_auth, container, false)
}