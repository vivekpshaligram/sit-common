package com.sit.sociallogin.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sit.common.utils.PrintLog
import com.sit.sociallogin.data.remote.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(val repository: Repository) : ViewModel() {

    fun getPostListApiCall() {

        viewModelScope.launch(Dispatchers.Main) {
            repository.getPostList()
                .flowOn(Dispatchers.IO)
                .catch { PrintLog.printMsg("MyTag", "Catch: ${it.message}") }
                .collect { PrintLog.printMsg("Api Call Successful")}
        }
    }
}