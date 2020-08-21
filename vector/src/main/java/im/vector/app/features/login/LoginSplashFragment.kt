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

import butterknife.OnClick
import im.vector.app.R
import javax.inject.Inject

/**
 * In this screen, the user is viewing an introduction to what he can do with this application
 *
 * Changed for Synod.im: Skip server selection using code from LoginServerSelectionFragment.
 */
class LoginSplashFragment @Inject constructor() : AbstractLoginFragment() {

    override fun getLayoutResId() = R.layout.fragment_login_splash

    @OnClick(R.id.loginSplashSubmit)
    fun getStarted() {
        loginViewModel.handle(LoginAction.UpdateHomeServer(getString(R.string.matrix_org_server_url)))
    }

    override fun resetViewModel() {
        // Nothing to do
    }

    override fun updateWithState(state: LoginViewState) {
        if (state.loginMode != LoginMode.Unknown) {
            // LoginFlow for matrix.org has been retrieved
            loginViewModel.handle(LoginAction.PostViewEvent(LoginViewEvents.OnLoginFlowRetrieved(state.loginMode == LoginMode.Sso)))
        }
    }
}
