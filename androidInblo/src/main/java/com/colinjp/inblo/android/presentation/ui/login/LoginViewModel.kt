package com.colinjp.inblo.android.presentation.ui.login

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.colinjp.inblo.datasource.network.horse_detail.DropdownData
import com.colinjp.inblo.datasource.network.login.LoginService
import com.colinjp.inblo.datasource.network.login.LoginResponse
import com.colinjp.inblo.interactors.horse_detail.GetHorseStables
import com.colinjp.inblo.interactors.login.LoginUser
import com.colinjp.inblo.interactors.login.LogoutUser
import com.colinjp.inblo.interactors.login.RegisterUser
import com.colinjp.inblo.util.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val loginService: LoginService,
    private val registerUser: RegisterUser,
    private val loginUser: LoginUser,
    private val logoutUser: LogoutUser,
    private val getHorseStables: GetHorseStables
): ViewModel() {


    sealed class LoginEvent {
        data class Success(val loginResponse: LoginResponse): LoginEvent()
        data class Error(val loginErrorMessage: String): LoginEvent()
        data class Loading(val isLoading: Boolean): LoginEvent()
    }



    private val loginChannel = Channel<LoginEvent>()
    val loginFlow = loginChannel.receiveAsFlow()

    private val logoutChannel = Channel<LoginEvent>()
    val logoutFlow = logoutChannel.receiveAsFlow()

    val stables = MutableStateFlow<DataState<List<DropdownData>>>(DataState.Empty)


    fun getStables(){
        viewModelScope.launch(IO) {
            getHorseStables.invoke().onEach { stablesResponse ->
                when(stablesResponse){
                    is DataState.Data -> {
                        stables.value = DataState.Data(stablesResponse.data.data)
                    }
                    DataState.Empty -> {

                    }
                    is DataState.Error -> {
                        //TODO:

                    }
                    is DataState.Loading -> {
                        //TODO:

                    }
                }
            }.launchIn(viewModelScope)
        }
    }

    fun authUser(
        loginName: String,
        password: String
    ){
        viewModelScope.launch(IO) {
            try {

//                loginChannel.send(LoginEvent.Loading(true))
//                val response: LoginResponse = loginService.login(loginName,password)
//                if(response.meta.code == 200){
//                    loginChannel.send(LoginEvent.Success(response))
//                }else{
//                    loginChannel.send(LoginEvent.Error(response.meta.message))
//                }
//                loginChannel.send(LoginEvent.Loading(false))
                loginUser.invoke(loginName, password)
                    .onEach {
                        when (it) {
                            is DataState.Data -> {
                                if (it.data.meta.code == 200) {
                                    loginChannel.send(LoginEvent.Success(it.data))
                                } else {
                                    loginChannel.send(LoginEvent.Error(it.data.meta.message))
                                }
                            }
                            DataState.Empty -> {
                                //
                            }
                            is DataState.Error -> {
                                loginChannel.send(LoginEvent.Error("通信に問題があります。後ほどアクセスしてください。"))
                                Timber.e(it.exception)
                            }
                            is DataState.Loading -> {
                                loginChannel.send(LoginEvent.Loading(it.isLoading))
                            }
                        }
                    }.launchIn(viewModelScope)
            } catch (exception: Exception) {
                Timber.e(exception.message)
            }
        }
    }

    fun logoutUser(
        userId: Int,
    ){
        viewModelScope.launch(IO) {
            try {
//                logoutChannel.send(LoginEvent.Loading(true))
//                val response: LoginResponse = loginService.logout(userId)
//                if(response.meta.code == 200){
//                    logoutChannel.send(LoginEvent.Success(response))
//                }else{
//                    logoutChannel.send(LoginEvent.Error(response.meta.message))
//                }
//                logoutChannel.send(LoginEvent.Loading(false))
                logoutUser.invoke(userId)
                    .onEach {
                        when (it) {
                            is DataState.Data -> {
                                if (it.data.meta.code == 200) {
                                    logoutChannel.send(LoginEvent.Success(it.data))
                                } else {
                                    logoutChannel.send(LoginEvent.Error(it.data.meta.message))
                                }
                            }
                            DataState.Empty -> {
                                //
                            }
                            is DataState.Error -> {
                                logoutChannel.send(LoginEvent.Error("通信に問題があります。後ほどアクセスしてください。"))
                                Timber.e(it.exception)
                            }
                            is DataState.Loading -> {
                                logoutChannel.send(LoginEvent.Loading(it.isLoading))
                            }
                        }
                    }.launchIn(viewModelScope)
            } catch (exception: Exception) {
                Timber.e(exception.message)
            }
        }
    }

    fun registerUser(
        firstName: String,
        lastName: String,
        email: String,
        username: String,
        password: String,
        userType: Int,
        stableCode: String,
//        stableId: Int
    ){
        viewModelScope.launch(IO) {
            try {

                registerUser.invoke(
                    firstName, lastName, email, username, password, userType, stableCode
                ).onEach {
                    when (it) {
                        is DataState.Data -> {
                            if(it.data.meta.code == 400){
                                loginChannel.send(LoginEvent.Error(it.data.meta.message))
                            }else{
                                loginChannel.send(LoginEvent.Success(it.data))
                            }
                        }
                        DataState.Empty -> {

                        }
                        is DataState.Error -> {
                            loginChannel.send(LoginEvent.Error("通信に問題があります。後ほどアクセスしてください。"))
                            Timber.e(it.exception)
                        }
                        is DataState.Loading -> {
                            loginChannel.send(LoginEvent.Loading(it.isLoading))
                        }
                    }
                }.launchIn(viewModelScope)

            } catch (exception: Exception) {
                loginChannel.send(LoginEvent.Error(exception.message.toString()))
                Timber.e(exception)
            }
        }
    }
}