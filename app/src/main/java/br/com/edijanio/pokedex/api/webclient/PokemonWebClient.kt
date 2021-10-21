package br.com.edijanio.pokedex.api.webclient

import br.com.edijanio.pokedex.api.RetroftInstance
import br.com.edijanio.pokedex.api.service.PokeApiService
import br.com.edijanio.pokedex.model.pokemonInformation.Pokemon

class PokemonWebClient(
    private val service: PokeApiService = RetroftInstance().pokeApiService
) {

    suspend fun findPokemonById(
        id: Int,
    ): Pokemon? {
        val request = service.getPokemonById(id)
        if(request.isSuccessful)
            return request.body()
        return null
    }
}