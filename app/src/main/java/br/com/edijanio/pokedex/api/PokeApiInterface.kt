package br.com.edijanio.pokedex.api

import br.com.edijanio.pokedex.model.PokemonModel
import retrofit2.Call
import retrofit2.http.GET


interface PokeApiInterface {

    @GET("pokemon")
    fun getPokemon() : Call<PokemonModel>
}