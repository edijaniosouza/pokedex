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
    private val pokemonLiveData = MediatorLiveData<Resource<PokemonEntity?>>()

    fun getPokemonById(pokemonId: Int): LiveData<Resource<PokemonEntity?>> {

        pokemonLiveData.addSource(repository.getPokemonByIdOnDatabase(pokemonId)) {
            if (it != null) {
                pokemonLiveData.value = Resource(data = it)
            } else {
                viewModelScope.launch {
                    pokemonLiveData.value = repository.getPokemonByIdOnApi(pokemonId).value
                }
            }
        }
        return pokemonLiveData
    }

    fun changeFavorite(pokemonReferente: PokemonEntity) {
        viewModelScope.launch {
            repository.changeFavorite(pokemonReferente)
        }
    }

}
