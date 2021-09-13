package br.com.edijanio.pokedex.api

import br.com.edijanio.pokedex.model.PokemonModel
import br.com.edijanio.pokedex.model.pokemonInformation.PokemonInfoModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface PokeApiInterface {

    @GET("pokemon")
    fun getPokemon(@Query("offset") offset : Int, @Query("limit") limit : Int = 100) : Call<PokemonModel>

    @GET("pokemon/{id}")
    fun getPokemonByIdOrName(@Path("id") id : String ) : Call<PokemonInfoModel>
}