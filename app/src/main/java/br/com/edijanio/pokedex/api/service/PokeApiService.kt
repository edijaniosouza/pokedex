package br.com.edijanio.pokedex.api.service

import br.com.edijanio.pokedex.model.PokemonModel
import br.com.edijanio.pokedex.model.pokemonInformation.Pokemon
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface PokeApiService {

    @GET("pokemon")
    suspend fun getAllPokemons() : Response<PokemonModel?>

    @GET("pokemon")
    suspend fun getAllPokemonsByOffset(@Query("offset") offset : Int, @Query("limit") limit : Int): Call<List<PokemonModel>?>

    @GET("pokemon/{id}")
    suspend fun getPokemonById(@Path("id") id: Int): Response<Pokemon?>

    @GET("pokemon/{name}")
    suspend fun getPokemonByName(@Path("name") name: String): Call<Pokemon?>

}