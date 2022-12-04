package com.xecoding.portfolio.data.remote

import kotlinx.coroutines.flow.Flow

interface ConnectivityObserver {

    fun networkState(): Flow<ConnectionStatus>

    enum class ConnectionStatus {
        Available, Unavailable
    }

}