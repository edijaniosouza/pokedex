package br.com.edijanio.pokedex.repository

data class Resource<T>(
    val data: T?,
    val error: String? = null
)