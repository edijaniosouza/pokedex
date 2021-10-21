package br.com.edijanio.pokedex.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.edijanio.pokedex.database.entity.PokemonEntity
import br.com.edijanio.pokedex.model.pokemonInformation.Pokemon
import br.com.edijanio.pokedex.repository.PokemonRepository
import br.com.edijanio.pokedex.repository.Resource
import kotlinx.coroutines.launch

class PokemonListActivityViewModel(
    private val repository: PokemonRepository
) : ViewModel() {
    private val pokemonsLiveData = MutableLiveData<Resource<List<PokemonEntity>?>>()

    fun findAll(): LiveData<Resource<List<PokemonEntity>?>> {
        viewModelScope.launch {
            pokemonsLiveData.postValue(Resource(data = repository.findAll(20).value))
        }
        return pokemonsLiveData
    }
}