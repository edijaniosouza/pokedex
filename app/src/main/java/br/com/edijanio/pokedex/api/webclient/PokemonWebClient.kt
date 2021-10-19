package br.com.edijanio.pokedex.api.webclient

import br.com.edijanio.pokedex.api.RetroftInstance
import br.com.edijanio.pokedex.api.service.PokeApiService
import br.com.edijanio.pokedex.model.pokemonInformation.Pokemon
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

const val RESPONSE_ERROR = "Erro na requisição"

class PokemonWebClient(
    private val service: PokeApiService = RetroftInstance().pokeApiService
) {

    private fun <T> request(
        call: Call<T>,
        onSuccess: (pokemons: T?) -> Unit,
        onFailure: (error: String?) -> Unit
    ) {
        call.enqueue(object : Callback<T> {

            override fun onResponse(call: Call<T>, response: Response<T>) {
                if (response.isSuccessful) {
                    onSuccess(response.body())
                } else {
                    onFailure(RESPONSE_ERROR)
                }
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                onFailure(t.message)
            }

        })
    }

    fun findAll(
        onSuccess: (pokemonList : List<Pokemon>?) -> Unit,
        onFailure: (error : String?) -> Unit
    ){
        request(
            service.getAllPokemons(),
            onSuccess,
            onFailure
        )
    }

    fun findAllByOffset(
        offset: Int,
        limit: Int = 20,
        onSuccess: (pokemonList : List<Pokemon>?) -> Unit,
        onFailure: (error : String?) -> Unit
    ){
        request(
            service.getAllPokemonsByOffset(offset, limit),
            onSuccess,
            onFailure
        )
    }

    fun findPokemonById(
        id: Long,
        onSuccess: (pokemonList : Pokemon?) -> Unit,
        onFailure: (error : String?) -> Unit
    ){
        request(
            service.getPokemonById(id),
            onSuccess,
            onFailure
        )
    }

    fun findPokemonByName(
        name: String,
        onSuccess: (pokemonList : Pokemon?) -> Unit,
        onFailure: (error : String?) -> Unit
    ){
        request(
            service.getPokemonByName(name),
            onSuccess,
            onFailure
        )
    }
}