package com.loc.newsapp.presentation.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.loc.newsapp.domain.usecases.app_entry.AppEntryUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

//Vamos a inyectar los casos de uso

@HiltViewModel
class OnBoardingViewModel @Inject constructor(
    private val appEntryUseCases: AppEntryUseCases
) : ViewModel() {

    /*
    Las funciones son privadas, excepto la de onEvent.
    Esto es una mezcla de MVI y MVVM.
    Esto lo hacemos para no usar el ViewModel directamente en la pantalla,
    de esta manera exponemos los eventos.
    Esto lo hace testeable y hacer preview de la pantalla sin problemas.
    */

    fun onEvent(event: OnBoardingEvent) { //recibir un evento de OnBoardingEvent
        when (event) { //comprobar que tipo de evento es, aunque en este caso solo hay uno
            is OnBoardingEvent.SaveAppEntry -> {
                saveAppEntry()
            }
        }
    }

    private fun saveAppEntry() {
        viewModelScope.launch {
            appEntryUseCases.saveAppEntry()
        }
    }


}