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

package com.fondesa.kpermissions.extension

import android.Manifest
import androidx.lifecycle.Observer
import com.fondesa.kpermissions.PermissionStatus
import com.fondesa.kpermissions.request.PermissionRequest
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.verify
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

/**
 * Tests for LiveData.kt file.
 */
@RunWith(RobolectricTestRunner::class)
class LiveDataKtTest {
    private val request = mock<PermissionRequest>()
    private val listenerCaptor = argumentCaptor<PermissionRequest.Listener>()

    @Test
    fun `When request listener is notified, the observer is notified too`() {
        val values = mutableListOf<List<PermissionStatus>>()
        request.liveData().observeForever { values += it }

        verify(request).addListener(listenerCaptor.capture())

        val listener = listenerCaptor.lastValue
        listener.onPermissionsResult(
            listOf(
                PermissionStatus.Granted(Manifest.permission.SEND_SMS),
                PermissionStatus.Denied.Permanently(Manifest.permission.CALL_PHONE)
            )
        )

        assertEquals(1, values.size)
        assertEquals(
            listOf(
                listOf(
                    PermissionStatus.Granted(Manifest.permission.SEND_SMS),
                    PermissionStatus.Denied.Permanently(Manifest.permission.CALL_PHONE)
                )
            ),
            values
        )

        listener.onPermissionsResult(
            listOf(
                PermissionStatus.Granted(Manifest.permission.SEND_SMS),
                PermissionStatus.Granted(Manifest.permission.CALL_PHONE)
            )
        )

        assertEquals(2, values.size)
        assertEquals(
            listOf(
                listOf(
                    PermissionStatus.Granted(Manifest.permission.SEND_SMS),
                    PermissionStatus.Denied.Permanently(Manifest.permission.CALL_PHONE)
                ),
                listOf(
                    PermissionStatus.Granted(Manifest.permission.SEND_SMS),
                    PermissionStatus.Granted(Manifest.permission.CALL_PHONE)
                )
            ),
            values
        )
    }

    @Test
    fun `When observer is subscribed, the request listener is added`() {
        val liveData = request.liveData()

        verify(request, never()).addListener(any())

        liveData.observeForever {}

        verify(request).addListener(any())

        liveData.observeForever {}

        // The method addListener() is not invoked again.
        verify(request).addListener(any())
    }

    @Test
    fun `When observer is disposed, the request listener is removed`() {
        val liveData = request.liveData()
        val observer1 = Observer<List<PermissionStatus>> { }
        val observer2 = Observer<List<PermissionStatus>> { }
        liveData.observeForever(observer1)
        liveData.observeForever(observer2)

        verify(request).addListener(listenerCaptor.capture())

        liveData.removeObserver(observer1)

        verify(request, never()).removeListener(any())

        liveData.removeObserver(observer2)

        verify(request).removeListener(listenerCaptor.lastValue)
    }
}
