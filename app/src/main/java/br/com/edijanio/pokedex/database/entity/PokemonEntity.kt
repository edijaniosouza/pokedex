package br.com.edijanio.pokedex.database.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity
@Parcelize
data class PokemonEntity (
    @PrimaryKey
    val id : Int,
    @ColumnInfo(name="type_1")
    val type1 : String,
    @ColumnInfo(name="type_2")
    val type2 : String? = null,
    val weight : Int,
    val height : Int,
    val name : String,
    val image : String
) : Parcelable