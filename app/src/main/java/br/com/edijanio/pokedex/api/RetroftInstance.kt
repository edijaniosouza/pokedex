package br.com.edijanio.pokedex.api

import br.com.edijanio.pokedex.api.service.PokeApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val POKEAPI_URL = "https://pokeapi.co/api/v2/"

class RetroftInstance {

    private val client by lazy {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BASIC
        OkHttpClient.Builder()
            .addInterceptor(logging)
            .build();
    }

    private val retrofit by lazy {
        Retrofit
            .Builder()
            .baseUrl(POKEAPI_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    val pokeApiService: PokeApiService by lazy {
        retrofit.create(PokeApiService::class.java)
    }

}

