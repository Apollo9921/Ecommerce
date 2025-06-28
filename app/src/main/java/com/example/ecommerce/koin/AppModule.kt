package com.example.ecommerce.koin

import com.example.ecommerce.networking.instance.ProductInstance
import com.example.ecommerce.utils.network.ConnectivityObserver
import com.example.ecommerce.utils.network.NetworkConnectivityObserver
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val appModule = module {
    single { ProductInstance.api }

    single<ProductRepository> { ProductRepositoryImpl(get()) }

    single<ConnectivityObserver> {
        NetworkConnectivityObserver(androidContext())
    }
}