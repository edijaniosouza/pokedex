package br.com.edijanio.pokedex.api.webclient

import br.com.edijanio.pokedex.api.RetroftInstance
import br.com.edijanio.pokedex.api.service.PokeApiService

class PokemonWebClient(
    private val service: PokeApiService = RetroftInstance().pokeApiService
) {
    suspend fun findPokemonById(id: Int) = service.getPokemonById(id)
    suspend fun findPokemonByIdOrName(nameOrId: String) = service.getPokemonByNameOrId(nameOrId)
}