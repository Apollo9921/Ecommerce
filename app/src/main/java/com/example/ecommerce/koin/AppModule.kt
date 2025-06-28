package com.example.ecommerce.koin

import androidx.room.Room
import com.example.ecommerce.networking.instance.ProductInstance
import com.example.ecommerce.networking.viewModel.ProductViewModel
import com.example.ecommerce.room.AppDatabase
import com.example.ecommerce.utils.network.ConnectivityObserver
import com.example.ecommerce.utils.network.NetworkConnectivityObserver
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single {
        Room.databaseBuilder(
            androidApplication(),
            AppDatabase::class.java,
            "ecommerce_database"
        )
            .fallbackToDestructiveMigration(false)
            .build()
    }

    factory { get<AppDatabase>().productDao() }

    single { ProductInstance.api }

    single<ProductRepository> { ProductRepositoryImpl(get()) }

    single<ConnectivityObserver> {
        NetworkConnectivityObserver(androidContext())
    }

    viewModel { ProductViewModel(get(), get(), get()) }
}