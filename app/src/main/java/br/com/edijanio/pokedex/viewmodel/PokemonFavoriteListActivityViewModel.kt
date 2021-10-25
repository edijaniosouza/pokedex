package br.com.edijanio.pokedex.viewmodel

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.edijanio.pokedex.database.entity.PokemonEntity
import br.com.edijanio.pokedex.repository.PokemonRepository
import br.com.edijanio.pokedex.repository.Resource

class PokemonFavoriteListActivityViewModel(
    private val repository: PokemonRepository
) : ViewModel() {

    private val pokemonsLiveData = MediatorLiveData<List<PokemonEntity>?>()

    fun loadOnlyFavorites(): MutableLiveData<List<PokemonEntity>?> {

        pokemonsLiveData.addSource(repository.getFavoritesPokemons()){
            pokemonsLiveData.value = it
        }
        return pokemonsLiveData
    }

}
