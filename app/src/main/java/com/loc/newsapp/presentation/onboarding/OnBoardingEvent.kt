package com.loc.newsapp.presentation.onboarding

//Esta clase tendrá los eventos que se enviarán desde el UI al ViewModel

sealed class OnBoardingEvent {

    //Un único evento
    object SaveAppEntry: OnBoardingEvent() // SaveAppEntry extiende de OnBoardingEvent

}