package com.project.topengbali

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.project.topengbali.ui.camera.CameraViewModel
import com.project.topengbali.ui.result.ResultViewModel

class ViewModelFactory : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(CameraViewModel::class.java)) {
            return CameraViewModel() as T
        }
        if (modelClass.isAssignableFrom(ResultViewModel::class.java)) {
            return ResultViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}