package br.com.edijanio.pokedex.viewmodel

import androidx.lifecycle.*
import br.com.edijanio.pokedex.database.entity.PokemonEntity
import br.com.edijanio.pokedex.repository.PokemonRepository
import br.com.edijanio.pokedex.repository.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PokemonDetailsActivityViewModel(
    private val repository: PokemonRepository
) : ViewModel() {
    private val pokemonLiveData = MutableLiveData<Resource<PokemonEntity?>>()

    fun getPokemonById(pokemonId: Int): LiveData<Resource<PokemonEntity?>> {
        viewModelScope.launch {
            val pokemonDatabase = repository.getPokemonByIdOnDatabase(pokemonId)
            pokemonDatabase.observeForever {
                if (pokemonDatabase.value != null) {
                    pokemonLiveData.postValue(Resource(data = pokemonDatabase.value))
                } else {
                    launch {
                        pokemonLiveData.postValue(repository.getPokemonByIdOnApi(pokemonId).value)
                    }
                }
            }
        }
        return pokemonLiveData
    }

    fun changeFavorite(pokemonReferente: PokemonEntity) {
        CoroutineScope(Dispatchers.IO).launch{
                repository.changeFavorite(pokemonReferente)
        }
    }

}
