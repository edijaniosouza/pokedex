package br.com.edijanio.pokedex.model

data class PokemonModel(
    val count: Int,
    val next: String,
    val previous: String,
    val results: List<Result>
)