package com.davidread.starwarsdatabase.di

import com.davidread.starwarsdatabase.datasource.PeopleRemoteDataSource
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Defines which [Retrofit] and `[...]RemoteDataSource` instances to inject for the entire application.
 */
@Module
class RemoteDataSourceModule {

    @Provides
    fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(SWAPI_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .build()

    @Provides
    fun providePeopleRemoteDataSource(retrofit: Retrofit): PeopleRemoteDataSource =
        retrofit.create(PeopleRemoteDataSource::class.java)

    companion object {
        private const val SWAPI_BASE_URL = "https://swapi.dev/api/"
    }
}