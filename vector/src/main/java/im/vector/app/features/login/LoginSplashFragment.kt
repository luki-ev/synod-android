/*
 * Copyright 2019 New Vector Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package im.vector.app.features.login

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import im.vector.app.databinding.FragmentLoginSplashBinding

import javax.inject.Inject

/**
 * In this screen, the user is viewing an introduction to what he can do with this application
 *
 * Changed for Synod.im: Skip server selection using code from LoginServerSelectionFragment.
 *
 * @see getStarted
 * @see updateWithState
 * @see LoginServerSelectionFragment.updateWithState
 */
class LoginSplashFragment @Inject constructor(
        private val vectorPreferences: VectorPreferences
) : AbstractLoginFragment<FragmentLoginSplashBinding>() {

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentLoginSplashBinding {
        return FragmentLoginSplashBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
    }

    private fun setupViews() {
        views.loginSplashSubmit.setOnClickListener { getStarted() }

        if (BuildConfig.DEBUG || vectorPreferences.developerMode()) {
            views.loginSplashVersion.isVisible = true
            @SuppressLint("SetTextI18n")
            views.loginSplashVersion.text = "Version : ${BuildConfig.VERSION_NAME}\n" +
                    "Branch: ${BuildConfig.GIT_BRANCH_NAME}\n" +
                    "Build: ${BuildConfig.BUILD_NUMBER}"
        }
    }

    private fun getStarted() {
        // upstream: loginViewModel.handle(LoginAction.PostViewEvent(LoginViewEvents.OpenServerSelection))
        // Synod.im: skip server selection
        loginViewModel.handle(LoginAction.UpdateHomeServer(getString(R.string.matrix_org_server_url).ensureTrailingSlash()))
    }

    override fun resetViewModel() {
        // Nothing to do
    }

    override fun updateWithState(state: LoginViewState) {
        if (state.loginMode != LoginMode.Unknown) {
            // Skip server selection (copied from LoginServerSelectionFragment.updateWithState)
            loginViewModel.handle(LoginAction.PostViewEvent(LoginViewEvents.OnLoginFlowRetrieved))
        }
    }
}
