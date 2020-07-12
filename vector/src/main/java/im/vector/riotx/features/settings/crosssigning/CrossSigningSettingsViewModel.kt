/*
 * Copyright 2020 New Vector Ltd
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
 */
package im.vector.riotx.features.settings.crosssigning

import com.airbnb.mvrx.FragmentViewModelContext
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import im.vector.matrix.android.api.session.Session
import im.vector.matrix.android.api.session.crypto.crosssigning.MXCrossSigningInfo
import im.vector.matrix.android.api.util.Optional
import im.vector.matrix.android.internal.crypto.crosssigning.isVerified
import im.vector.matrix.android.internal.crypto.model.rest.DeviceInfo
import im.vector.matrix.rx.rx
import im.vector.riotx.core.extensions.exhaustive
import im.vector.riotx.core.platform.VectorViewModel
import io.reactivex.Observable
import io.reactivex.functions.BiFunction

class CrossSigningSettingsViewModel @AssistedInject constructor(@Assisted private val initialState: CrossSigningSettingsViewState,
                                                                private val session: Session)
    : VectorViewModel<CrossSigningSettingsViewState, CrossSigningSettingsAction, CrossSigningSettingsViewEvents>(initialState) {

    init {
        Observable.combineLatest<List<DeviceInfo>, Optional<MXCrossSigningInfo>, Pair<List<DeviceInfo>, Optional<MXCrossSigningInfo>>>(
                session.rx().liveMyDeviceInfo(),
                session.rx().liveCrossSigningInfo(session.myUserId),
                BiFunction { myDeviceInfo, mxCrossSigningInfo ->
                    (myDeviceInfo to mxCrossSigningInfo)
                }
        )
                .execute { data ->
                    val crossSigningKeys = data.invoke()?.second?.getOrNull()
                    val xSigningIsEnableInAccount = crossSigningKeys != null
                    val xSigningKeysAreTrusted = session.cryptoService().crossSigningService().checkUserTrust(session.myUserId).isVerified()
                    val xSigningKeyCanSign = session.cryptoService().crossSigningService().canCrossSign()
                    val hasSeveralDevices = data.invoke()?.first?.size ?: 0 > 1

                    copy(
                            crossSigningInfo = crossSigningKeys,
                            xSigningIsEnableInAccount = xSigningIsEnableInAccount,
                            xSigningKeysAreTrusted = xSigningKeysAreTrusted,
                            xSigningKeyCanSign = xSigningKeyCanSign,
                            deviceHasToBeVerified = hasSeveralDevices && (xSigningIsEnableInAccount && !xSigningKeyCanSign),
                            recoveryHasToBeSetUp = !session.sharedSecretStorageService.isRecoverySetup()
                    )
                }
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(initialState: CrossSigningSettingsViewState): CrossSigningSettingsViewModel
    }

    override fun handle(action: CrossSigningSettingsAction) {
        when (action) {
            CrossSigningSettingsAction.SetUpRecovery -> {
                _viewEvents.post(CrossSigningSettingsViewEvents.SetUpRecovery)
            }
            CrossSigningSettingsAction.VerifySession -> {
                _viewEvents.post(CrossSigningSettingsViewEvents.VerifySession)
            }
            CrossSigningSettingsAction.SetupCrossSigning -> {
                _viewEvents.post(CrossSigningSettingsViewEvents.SetupCrossSigning)
            }
        }.exhaustive
    }

    companion object : MvRxViewModelFactory<CrossSigningSettingsViewModel, CrossSigningSettingsViewState> {

        @JvmStatic
        override fun create(viewModelContext: ViewModelContext, state: CrossSigningSettingsViewState): CrossSigningSettingsViewModel? {
            val fragment: CrossSigningSettingsFragment = (viewModelContext as FragmentViewModelContext).fragment()
            return fragment.viewModelFactory.create(state)
        }
    }
}
