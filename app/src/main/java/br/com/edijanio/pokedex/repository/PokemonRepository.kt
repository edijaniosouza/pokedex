package br.com.edijanio.pokedex.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.edijanio.pokedex.api.webclient.PokemonWebClient
import br.com.edijanio.pokedex.database.dao.PokemonDAO
import br.com.edijanio.pokedex.model.pokemonInformation.Pokemon
import kotlinx.coroutines.runBlocking

class PokemonRepository(
    private val dao: PokemonDAO,
    private val webClient: PokemonWebClient
) {

    private val pokemonsLiveData = MutableLiveData<Resource<List<Pokemon>?>>()

    fun findAll(): LiveData<Resource<List<Pokemon>?>> {
        webClient.findAll(
            onSuccess = { pokemonList ->
                pokemonList?.let {
                    saveOnDatabase(pokemonList)
                }
            },
            onFailure = { errorMessage ->
                pokemonsLiveData.value = Resource(null, errorMessage)
            })

        return pokemonsLiveData
    }

    private fun saveOnDatabase(pokemonList: List<Pokemon>) {
        runBlocking { dao.insertPokemons(pokemonList) }
    }

}