/*
 * Copyright (c) 2020 New Vector Ltd
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

package im.vector.riotx.features.powerlevel

import im.vector.matrix.android.api.query.QueryStringValue
import im.vector.matrix.android.api.session.events.model.EventType
import im.vector.matrix.android.api.session.events.model.toModel
import im.vector.matrix.android.api.session.room.Room
import im.vector.matrix.android.api.session.room.model.PowerLevelsContent
import im.vector.matrix.rx.mapOptional
import im.vector.matrix.rx.rx
import im.vector.matrix.rx.unwrap
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

class PowerLevelsObservableFactory(private val room: Room) {

    fun createObservable(): Observable<PowerLevelsContent> {
        return room.rx()
                .liveStateEvent(EventType.STATE_ROOM_POWER_LEVELS, QueryStringValue.NoCondition)
                .observeOn(Schedulers.computation())
                .mapOptional { it.content.toModel<PowerLevelsContent>() }
                .unwrap()
    }
}
