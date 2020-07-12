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

package im.vector.matrix.android.internal.session.widgets.token

import com.zhuinden.monarchy.Monarchy
import im.vector.matrix.android.internal.database.model.ScalarTokenEntity
import im.vector.matrix.android.internal.database.query.where
import im.vector.matrix.android.internal.di.SessionDatabase
import im.vector.matrix.android.internal.util.awaitTransaction
import im.vector.matrix.android.internal.util.fetchCopyMap
import javax.inject.Inject

internal class ScalarTokenStore @Inject constructor(@SessionDatabase private val monarchy: Monarchy) {

    fun getToken(apiUrl: String): String? {
        return monarchy.fetchCopyMap({ realm ->
            ScalarTokenEntity.where(realm, apiUrl).findFirst()
        }, { scalarToken, _ ->
            scalarToken.token
        })
    }

    suspend fun setToken(apiUrl: String, token: String) {
        monarchy.awaitTransaction { realm ->
            val scalarTokenEntity = ScalarTokenEntity(apiUrl, token)
            realm.insertOrUpdate(scalarTokenEntity)
        }
    }

    suspend fun clearToken(apiUrl: String) {
        monarchy.awaitTransaction { realm ->
            ScalarTokenEntity.where(realm, apiUrl).findFirst()?.deleteFromRealm()
        }
    }
}
