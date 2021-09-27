package com.molo17.androidcamerax

import androidx.lifecycle.LiveData
import com.molo17.androidcamerax.internal.Event

/**
 * Created by Damiano Giusti on 2019-05-25.
 */
internal interface CameraViewModel {
    fun takenPictureState(): LiveData<Event<PictureResult>>
}