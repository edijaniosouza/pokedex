package br.com.edijanio.pokedex.repository

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import br.com.edijanio.pokedex.api.webclient.PokemonWebClient
import br.com.edijanio.pokedex.database.dao.PokemonDAO
import br.com.edijanio.pokedex.database.entity.PokemonEntity
import br.com.edijanio.pokedex.model.pokemonInformation.Pokemon
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.UnknownHostException

class PokemonRepository(
    private val dao: PokemonDAO,
    private val webClient: PokemonWebClient = PokemonWebClient()
) {
    private val liveData = MutableLiveData<List<PokemonEntity>?>()

    suspend fun findAll(): LiveData<List<PokemonEntity>?> {
        getAllPokemonsOnDatabase().observeForever({
            liveData.value = it
        })
        findOnAPI()
        return liveData
    }

    private fun getAllPokemonsOnDatabase(): LiveData<List<PokemonEntity>?> = dao.findAll()

    private suspend fun findOnAPI() {
        for (n in 1..20) {
            try {
                val pokemon = webClient.findPokemonById(n)
                if (pokemon.isSuccessful) {
                    internalSave(pokemon.body())
                } else {
                    Log.d("teste", "Erro na comunicação")
                }
            } catch (e: UnknownHostException) {
                Log.d("teste", "erro: $e")
                // liveData.value = null
            }
        }

    }

    private suspend fun internalSave(
        pokemon: Pokemon?
    ) {
        val convertedPokemon = pokemon?.let { createPokemonEntity(it) }
        if (convertedPokemon != null) {
            dao.insert(convertedPokemon)
        }
    }

    suspend fun getPokemonById(
        pokemonId: Int
    ): LiveData<Resource<PokemonEntity?>> {
        val liveDataSearch = MutableLiveData<Resource<PokemonEntity?>>()

        val pokemonSearched = getPokemonByIdOnDatabase(pokemonId)
        if (pokemonSearched.value != null) liveDataSearch.value =
            Resource(data = pokemonSearched.value)
        else {
            val pokemonWebSearched = webClient.findPokemonById(pokemonId)
            val pokemonEntity = pokemonWebSearched.let {
                it.body()?.let { it1 ->
                    createPokemonEntity(
                        it1
                    )
                }
            }
            liveDataSearch.value = Resource(data = pokemonEntity)

        }
        return liveDataSearch
    }

    private fun getPokemonByIdOnDatabase(pokemonId: Int): MutableLiveData<PokemonEntity?> {
        val listLiveData = MutableLiveData<PokemonEntity?>()
        val requestPokemon = dao.findById(pokemonId)
        listLiveData.value = requestPokemon.value
        return listLiveData
    }

    private fun createPokemonEntity(pokemonWebSearched: Pokemon) =
        PokemonEntity(
            height = pokemonWebSearched.height,
            id = pokemonWebSearched.id,
            image = pokemonWebSearched.sprites.other.officialArtwork.front_default,
            name = pokemonWebSearched.name,
            type1 = pokemonWebSearched.types[0].type.name,
            type2 = if (pokemonWebSearched.types.size > 1) {
                pokemonWebSearched.types[1].type.name
            } else null,
            weight = pokemonWebSearched.weight
        )

}