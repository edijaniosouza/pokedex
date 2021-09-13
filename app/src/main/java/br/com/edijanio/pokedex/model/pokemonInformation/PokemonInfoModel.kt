package br.com.edijanio.pokedex.model.pokemonInformation

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class PokemonInfoModel(
    val id : Int,
    val types : List<PokemonType>,
    val weight : Int,
    val height : Int,
    val name : String,
    val sprites : PokemonSprite
) : Parcelable
