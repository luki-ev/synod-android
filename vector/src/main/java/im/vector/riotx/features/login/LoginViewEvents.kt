/*
 * Copyright 2019 New Vector Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package im.vector.riotx.features.login

import im.vector.matrix.android.api.auth.registration.FlowResult
import im.vector.riotx.core.platform.VectorViewEvents

/**
 * Transient events for Login
 */
sealed class LoginViewEvents : VectorViewEvents {
    data class Loading(val message: CharSequence? = null) : LoginViewEvents()
    data class Failure(val throwable: Throwable) : LoginViewEvents()

    data class RegistrationFlowResult(val flowResult: FlowResult, val isRegistrationStarted: Boolean) : LoginViewEvents()
    object OutdatedHomeserver : LoginViewEvents()

    // Navigation event

    object OpenServerSelection : LoginViewEvents()
    object OnServerSelectionDone : LoginViewEvents()
    object OnLoginFlowRetrieved : LoginViewEvents()
    object OnSignModeSelected : LoginViewEvents()
    object OnForgetPasswordClicked : LoginViewEvents()
    object OnResetPasswordSendThreePidDone : LoginViewEvents()
    object OnResetPasswordMailConfirmationSuccess : LoginViewEvents()
    object OnResetPasswordMailConfirmationSuccessDone : LoginViewEvents()

    data class OnSendEmailSuccess(val email: String) : LoginViewEvents()
    data class OnSendMsisdnSuccess(val msisdn: String) : LoginViewEvents()

    data class OnWebLoginError(val errorCode: Int, val description: String, val failingUrl: String) : LoginViewEvents()
}
