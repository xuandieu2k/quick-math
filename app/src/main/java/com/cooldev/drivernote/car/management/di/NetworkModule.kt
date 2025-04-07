//package com.dhug.example.di
//
//import com.dhug.example.data.remote.NominatimApiService
//import com.dhug.example.data.remote.OsrmApiService
//import com.dhug.example.data.remote.OverpassApiService
//import dagger.Module
//import dagger.Provides
//import dagger.hilt.InstallIn
//import dagger.hilt.components.SingletonComponent
//import okhttp3.OkHttpClient
//import okhttp3.logging.HttpLoggingInterceptor
//import retrofit2.Retrofit
//import retrofit2.converter.gson.GsonConverterFactory
//import java.util.concurrent.TimeUnit
//import javax.inject.Singleton
//
///**
// * @Author: NGUYEN XUAN DIEU
// * @Date: 31 / 10 / 2024
// */
//@Module
//@InstallIn(SingletonComponent::class)
//object NetworkModule {
//    private const val OVERPASS_BASE_URL = "https://overpass-api.de/api/"
//    private const val OSRM_BASE_URL = "https://router.project-osrm.org/"
//    private const val NOMINATIM_BASE_URL = "https://nominatim.openstreetmap.org/"
//
//    @Provides
//    fun provideBaseUrl(): String = OSRM_BASE_URL
////
////    @Provides
////    fun provideOVERPASSBaseUrl(): String = OVERPASS_BASE_URL
////    fun provideNominatimBaseUrl(): String = NOMINATIM_BASE_URL
//
//    @Provides
//    @Singleton
//    fun provideGsonConverterFactory(): GsonConverterFactory = GsonConverterFactory.create()
//
//    @Provides
//    @Singleton
//    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
//        return HttpLoggingInterceptor().apply {
//            level = HttpLoggingInterceptor.Level.BODY
//        }
//    }
//
//    @Provides
//    @Singleton
//    fun provideOkHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
//        return OkHttpClient.Builder()
//            .addInterceptor(loggingInterceptor)
//            .connectTimeout(30, TimeUnit.SECONDS)
//            .readTimeout(30, TimeUnit.SECONDS)
//            .writeTimeout(30, TimeUnit.SECONDS)
//            .build()
//    }
//
//    @Provides
//    @Singleton
//    fun provideRetrofit(
//        baseUrl: String,
//        gsonConverterFactory: GsonConverterFactory,
//        okHttpClient: OkHttpClient
//    ): Retrofit {
//        return Retrofit.Builder()
//            .baseUrl(baseUrl)
//            .client(okHttpClient)
//            .addConverterFactory(gsonConverterFactory)
//            .build()
//    }
//
//    @Provides
//    @Singleton
//    fun provideOsrmService(retrofit: Retrofit): OsrmApiService =
//        retrofit.create(OsrmApiService::class.java)
//
//    @Provides
//    @Singleton
//    fun provideOverpassService(
//        gsonConverterFactory: GsonConverterFactory,
//        okHttpClient: OkHttpClient
//    ): OverpassApiService {
//        return Retrofit.Builder()
//            .baseUrl(OVERPASS_BASE_URL)
//            .client(okHttpClient)
//            .addConverterFactory(gsonConverterFactory)
//            .build()
//            .create(OverpassApiService::class.java)
//    }
//
//    @Provides
//    @Singleton
//    fun provideNominatimService(
//        gsonConverterFactory: GsonConverterFactory,
//        okHttpClient: OkHttpClient
//    ): NominatimApiService {
//        return Retrofit.Builder()
//            .baseUrl(NOMINATIM_BASE_URL)
//            .client(okHttpClient)
//            .addConverterFactory(gsonConverterFactory)
//            .build()
//            .create(NominatimApiService::class.java)
//    }
//
//    @Provides
//    @Singleton
//    fun providePlaceRepository(
//        apiService: OverpassApiService,
//        nominatimApiService: NominatimApiService,
//    ): PlaceRepository = PlaceRepositoryImpl(apiService, nominatimApiService)
//
//    @Provides
//    @Singleton
//    fun providePlaceUseCase(
//        repository: PlaceRepository
//    ): PlaceUseCase = PlaceUseCase(repository)
//
//    @Provides
//    @Singleton
//    fun provideRouteMapRepository(
//        apiService: OsrmApiService
//    ): RouteMapRepository = RouteMapRepositoryImpl(apiService)
//
//    @Provides
//    @Singleton
//    fun provideRouteMapUseCase(
//        repository: RouteMapRepository
//    ): RouteMapUseCase = RouteMapUseCase(repository)
//}