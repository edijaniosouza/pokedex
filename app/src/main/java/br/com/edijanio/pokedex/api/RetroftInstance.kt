package br.com.edijanio.pokedex.api

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetroftInstance {

    private val pokeApiUrl = "https://pokeapi.co/api/v2/"

    private fun retrofit(): Retrofit = Retrofit
        .Builder()
        .baseUrl(pokeApiUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()


    fun api(): PokeApiInterface = retrofit().create(PokeApiInterface::class.java)




}