package br.com.edijanio.pokedex.model.pokemonInformation

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class OtherSprit(
    @SerializedName("official-artwork")
    val officialArtwork : OtherImages
) : Parcelable
