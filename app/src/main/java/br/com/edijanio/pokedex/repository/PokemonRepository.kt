package br.com.edijanio.pokedex.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import br.com.edijanio.pokedex.api.webclient.PokemonWebClient
import br.com.edijanio.pokedex.database.dao.PokemonDAO
import br.com.edijanio.pokedex.database.entity.PokemonEntity
import br.com.edijanio.pokedex.model.pokemonInformation.Pokemon
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import java.net.UnknownHostException

const val COMMUNICATION_ERROR = "Erro na comunicação com servidor"

class PokemonRepository(
    private val dao: PokemonDAO,
    private val webClient: PokemonWebClient = PokemonWebClient()
) {
    private val liveDataHandler = MediatorLiveData<Resource<List<PokemonEntity>?>>()
    fun findAll(): LiveData<Resource<List<PokemonEntity>?>> {
        liveDataHandler.addSource(getAllPokemonsOnDatabase()) {
            liveDataHandler.value = Resource(it)
        }

        CoroutineScope(IO).launch {
            findOnAPI()
        }
        return liveDataHandler
    }

    private fun getAllPokemonsOnDatabase(): LiveData<List<PokemonEntity>?> = dao.findAll()

    private suspend fun findOnAPI() {
        for (n in 1..20) {
            try {
                val pokemon = webClient.findPokemonById(n)
                if (pokemon.isSuccessful) {
                    internalSave(pokemon.body())
                } else {
                    Log.e("api", COMMUNICATION_ERROR)
                    liveDataHandler.postValue(Resource(data = null, error = COMMUNICATION_ERROR))
                }
            } catch (e: UnknownHostException) {
                Log.e("api", "exception: ${e.message}")
                liveDataHandler.postValue(Resource(data = null, error = COMMUNICATION_ERROR))
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

    suspend fun getPokemonByIdOnApi(
        pokemonId: Int
    ): LiveData<Resource<PokemonEntity?>> {
        val liveDataSearch = MutableLiveData<Resource<PokemonEntity?>>()
        try {
            val pokemonWebSearched = webClient.findPokemonById(pokemonId)
            if (pokemonWebSearched.isSuccessful) {
                val pokemonEntity = pokemonWebSearched.let { pokemonResponse ->
                    pokemonResponse.body()?.let { pokemon ->
                        createPokemonEntity(pokemon)
                    }
                }
                liveDataSearch.value = Resource(data = pokemonEntity)
            } else {
                liveDataSearch.value = Resource(data = null, error = COMMUNICATION_ERROR)
            }

        } catch (e: UnknownHostException) {
            liveDataSearch.value = Resource(data = null, error = COMMUNICATION_ERROR)
        }
        return liveDataSearch
    }

    fun getPokemonByIdOnDatabase(pokemonId: Int) = dao.findById(pokemonId)

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
            weight = pokemonWebSearched.weight,
        )

    suspend fun insertPokemonOnDatabase(pokemon: PokemonEntity?) {
        if (pokemon != null) {
            dao.insert(pokemon)
        }
    }

    suspend fun changeFavorite(pokemon: PokemonEntity) {
        pokemon.isFavorite = !pokemon.isFavorite
        dao.updatePokemon(pokemon)
    }

    fun getFavoritesPokemons() = dao.findOnlyFavorites(true)

    suspend fun getPokemonByNameOrId(nameOrId: String?): MutableLiveData<Resource<List<PokemonEntity>?>> {
        val liveDataSearch = MutableLiveData<Resource<List<PokemonEntity>?>>()
        if (nameOrId != null) {
            try {
                val apiResponse = webClient.findPokemonByIdOrName(nameOrId)
                if (apiResponse.isSuccessful) {
                    val responseBody = apiResponse.body()
                    if (responseBody?.name != null) {
                        val pokemonEntity = createPokemonEntity(responseBody)
                        liveDataSearch.value = Resource(data = listOf(pokemonEntity))
                    }
                } else if(apiResponse.code() == 404){
                    liveDataSearch.value = Resource(data = null, error = "Pokemon não encontrado")
                }
                else{
                    liveDataSearch.value = Resource(data = null, error = COMMUNICATION_ERROR)
                }

            } catch (e: UnknownHostException) {
                liveDataSearch.value = Resource(data = null, error = COMMUNICATION_ERROR)
            }
        }

        return liveDataSearch
    }
}
