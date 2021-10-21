package br.com.edijanio.pokedex.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.edijanio.pokedex.api.webclient.PokemonWebClient
import br.com.edijanio.pokedex.database.dao.PokemonDAO
import br.com.edijanio.pokedex.database.entity.PokemonEntity
import br.com.edijanio.pokedex.model.pokemonInformation.Pokemon
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PokemonRepository(
    private val dao: PokemonDAO,
    private val webClient: PokemonWebClient = PokemonWebClient()
) {

    private val pokemonsLiveData = MutableLiveData<Resource<List<PokemonEntity>?>>()

    fun findAll(
        listSize: Int
    ): LiveData<Resource<List<PokemonEntity>?>> {
        val coroutineScopeIO = CoroutineScope(IO)
        coroutineScopeIO.launch {

            val result = dao.findAll()
            withContext(Main) {
                pokemonsLiveData.value = Resource(data = result)
            }

            //TODO: Fazer funcionar sem internet
            findOnAPI(
                listSize,
                onFailure = {mensageError ->
                    pokemonsLiveData.value = Resource(data = null, error = mensageError)
                }
            )

        }
        return pokemonsLiveData
    }

    private suspend fun findOnAPI(
        quantidadeDePokemons: Int,
        onFailure: (error: String) -> Unit
    ) {
        for (n in 1..quantidadeDePokemons) {
            val pokemon = webClient.findPokemonById(id = n)
            if (pokemon != null) {
                internalSave(pokemon)
                withContext(Main) {
                    pokemonsLiveData.value = Resource(data = null)
                }
            } else {
                withContext(Main) {
                    onFailure("\"Falha ao buscar pokemon\"")
//                    pokemonsLiveData.value =
//                        Resource(data = null, error = "Falha ao buscar pokemon")
                }
            }

        }
    }

    private suspend fun internalSave(
        pokemon: Pokemon?
    ) {
        if (pokemon != null) {
            createPokemonEntity(pokemon).also { pokemonEntity ->
                dao.insert(pokemonEntity)
            }
        }
    }

    suspend fun getPokemonById(
        pokemonId: Int
    ): LiveData<Resource<PokemonEntity?>> {
        val liveDataSearch = MutableLiveData<Resource<PokemonEntity?>>()

        val pokemonSearched = dao.findById(pokemonId)
        if (pokemonSearched != null) liveDataSearch.value = Resource(data = pokemonSearched)
        else {
            val pokemonWebSearched = webClient.findPokemonById(pokemonId)
            if (pokemonWebSearched != null) {
                val pokemonEntity = createPokemonEntity(pokemonWebSearched)
                liveDataSearch.value = Resource(data = pokemonEntity)
            }
        }
        return liveDataSearch
    }

    private fun createPokemonEntity(pokemonWebSearched: Pokemon) =
        PokemonEntity(
            height = pokemonWebSearched.height,
            id = pokemonWebSearched.id,
            image = pokemonWebSearched.sprites.other.officialArtwork.front_default,
            name = pokemonWebSearched.name,
            type1 = pokemonWebSearched.types[0].type.name,
            weight = pokemonWebSearched.weight
        )

}