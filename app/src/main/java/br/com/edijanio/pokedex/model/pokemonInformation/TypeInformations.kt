package br.com.edijanio.pokedex.model.pokemonInformation

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TypeInformations(
    val name : String,
    val url : String
) : Parcelable
