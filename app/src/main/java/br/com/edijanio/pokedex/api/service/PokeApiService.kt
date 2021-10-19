package br.com.edijanio.pokedex.api.service

import br.com.edijanio.pokedex.model.pokemonInformation.Pokemon
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface PokeApiService {

    @GET("pokemon")
    fun getAllPokemons() : Call<List<Pokemon>?>

    @GET("pokemon")
    fun getAllPokemonsByOffset(@Query("offset") offset : Int, @Query("limit") limit : Int): Call<List<Pokemon>?>

    @GET("pokemon/{id}")
    fun getPokemonById(@Path("id") id: Long): Call<Pokemon?>

    @GET("pokemon/{name}")
    fun getPokemonByName(@Path("name") id: String): Call<Pokemon?>

}