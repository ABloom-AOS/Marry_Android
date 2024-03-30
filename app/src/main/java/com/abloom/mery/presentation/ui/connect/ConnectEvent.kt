package com.abloom.mery.presentation.ui.connect

sealed interface ConnectEvent {
    data object ConnectFail : ConnectEvent
}
