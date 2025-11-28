package com.example.huerto.ui.nosotros

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class NosotrosVMFactory(private val app: Application) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NosotrosViewModel(app) as T
    }
}
