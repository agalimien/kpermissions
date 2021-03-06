/*
 * Copyright (c) 2020 Giorgio Antonioli
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

package com.fondesa.kpermissions.request.runtime.nonce

@Deprecated("If you are using the status API, use PermissionRequest.send() sending a new request instead.")
/**
 * Used to request some permissions again.
 * A [PermissionNonce] can be used only one time.
 */
interface PermissionNonce {

    /**
     * Request the permissions related to this [PermissionNonce] again.
     *
     * @throws PermissionNonceUsedException if this method is invoke more than one time
     * on the same instance of [PermissionNonce].
     */
    @Deprecated("If you are using the status API, use PermissionRequest.send() sending a new request instead.")
    fun use()
}
