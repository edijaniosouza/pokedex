package br.com.edijanio.pokedex.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import br.com.edijanio.pokedex.database.entity.PokemonEntity
import br.com.edijanio.pokedex.model.pokemonInformation.Pokemon
import br.com.edijanio.pokedex.repository.PokemonRepository
import br.com.edijanio.pokedex.repository.Resource

class PokemonListActivityViewModel(
    private val repository: PokemonRepository
) : ViewModel() {

    fun findAll(): LiveData<Resource<List<PokemonEntity>?>> {
        return repository.findAll(20)
    }
}