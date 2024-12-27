package com.sit.sociallogin.ui

import androidx.lifecycle.viewModelScope
import com.sit.common.base.BaseViewModel
import com.sit.sociallogin.data.remote.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(val repository: Repository) : BaseViewModel() {

    fun getPostListApiCall() {

        viewModelScope.launch(Dispatchers.Main) {
            repository.getPostList()
                .flowOn(Dispatchers.IO)
                .catch { printLogManager.printMsg("MyTag", "Catch: ${it.message}") }
                .collect { printLogManager.printMsg("Api Call Successful") }
        }
    }

}