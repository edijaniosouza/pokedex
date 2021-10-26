package br.com.edijanio.pokedex.viewmodel

import androidx.lifecycle.*
import br.com.edijanio.pokedex.database.entity.PokemonEntity
import br.com.edijanio.pokedex.repository.PokemonRepository
import br.com.edijanio.pokedex.repository.Resource
import kotlinx.coroutines.launch

class PokemonListActivityViewModel(
    private val repository: PokemonRepository
) : ViewModel() {
    fun findAll(): LiveData<Resource<List<PokemonEntity>?>> {
        return repository.findAll()
    }

    fun loadMorePokemons(pokemonId: Int): LiveData<Resource<PokemonEntity?>> {
        val pokemonLiveData = MutableLiveData<Resource<PokemonEntity?>>()
        viewModelScope.launch {

            val pokemonResource = repository.getPokemonByIdOnApi(pokemonId).value
            pokemonResource?.let { resource ->
                pokemonLiveData.postValue(resource)
                resource.data.let { pokemon ->
                    repository.insertPokemonOnDatabase(pokemon)
                }
            }
        }
        return pokemonLiveData
    }

    fun getOnPokemonByNameOrId(nameOrId: String?): MutableLiveData<Resource<List<PokemonEntity>?>> {
        val liveDataSearch = MutableLiveData<Resource<List<PokemonEntity>?>>()
        viewModelScope.launch {
            liveDataSearch.postValue(repository.getPokemonByNameOrId(nameOrId).value)
        }
        return liveDataSearch
    }
}