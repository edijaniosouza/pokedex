package br.com.edijanio.pokedex.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.edijanio.pokedex.database.entity.PokemonEntity
import br.com.edijanio.pokedex.repository.PokemonRepository
import br.com.edijanio.pokedex.repository.Resource
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PokemonDetailsActivityViewModel(
    private val repository: PokemonRepository
) : ViewModel() {
    val liveData = MutableLiveData<Resource<PokemonEntity?>>()
    fun getPokemonById(pokemonId: Int): LiveData<Resource<PokemonEntity?>>{
        viewModelScope.launch {
            val pokemon = repository.getPokemonById(pokemonId)
            withContext(Main){
                liveData.value = pokemon.value
            }
        }
        return liveData
    }

}
