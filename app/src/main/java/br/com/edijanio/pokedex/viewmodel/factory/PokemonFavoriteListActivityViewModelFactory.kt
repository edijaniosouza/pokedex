package br.com.edijanio.pokedex.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.edijanio.pokedex.repository.PokemonRepository
import br.com.edijanio.pokedex.viewmodel.PokemonFavoriteListActivityViewModel

class PokemonFavoriteListActivityViewModelFactory(
    private val repository: PokemonRepository
) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return PokemonFavoriteListActivityViewModel(repository) as T
    }

}
