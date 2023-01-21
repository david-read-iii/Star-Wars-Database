package com.davidread.starwarsdatabase.di

import com.davidread.starwarsdatabase.datasource.*
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

    @Provides
    fun provideFilmsRemoteDataSource(retrofit: Retrofit): FilmsRemoteDataSource =
        retrofit.create(FilmsRemoteDataSource::class.java)

    @Provides
    fun provideStarshipsRemoteDataSource(retrofit: Retrofit): StarshipsRemoteDataSource =
        retrofit.create(StarshipsRemoteDataSource::class.java)

    @Provides
    fun provideVehiclesRemoteDataSource(retrofit: Retrofit): VehiclesRemoteDataSource =
        retrofit.create(VehiclesRemoteDataSource::class.java)

    @Provides
    fun provideSpeciesRemoteDataSource(retrofit: Retrofit): SpeciesRemoteDataSource =
        retrofit.create(SpeciesRemoteDataSource::class.java)

    @Provides
    fun providePlanetsRemoteDataSource(retrofit: Retrofit): PlanetsRemoteDataSource =
        retrofit.create(PlanetsRemoteDataSource::class.java)

    companion object {
        private const val SWAPI_BASE_URL = "https://swapi.dev/api/"
    }
}