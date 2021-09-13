package br.com.edijanio.pokedex.model.pokemonInformation

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PokemonSprite(
    val other : OtherSprit
) : Parcelable
