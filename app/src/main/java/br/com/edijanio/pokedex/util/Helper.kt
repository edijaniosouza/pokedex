package br.com.edijanio.pokedex.util

import android.graphics.Color
import android.graphics.drawable.Drawable
import androidx.core.graphics.drawable.DrawableCompat


fun changeTypeColor(typeField : String, wrappedDrawable : Drawable){
    when(typeField){
            "fire" -> DrawableCompat.setTint(wrappedDrawable, Color.parseColor("#F6953C"))
            "flying" -> DrawableCompat.setTint(wrappedDrawable, Color.parseColor("#9A85EA"))
            "grass" -> DrawableCompat.setTint(wrappedDrawable, Color.parseColor("#3B933F"))
            "water" -> DrawableCompat.setTint(wrappedDrawable, Color.parseColor("#32A4C5"))
            "normal" -> DrawableCompat.setTint(wrappedDrawable, Color.parseColor("#AAAA9E"))
            "bug" -> DrawableCompat.setTint(wrappedDrawable, Color.parseColor("#B8F49A"))
            "poison" -> DrawableCompat.setTint(wrappedDrawable, Color.parseColor("#FF6200EE"))
            "eletric" -> DrawableCompat.setTint(wrappedDrawable, Color.parseColor("#F3FFF636"))
            "ground" -> DrawableCompat.setTint(wrappedDrawable, Color.parseColor("#F3ACA401"))
            "fighting" -> DrawableCompat.setTint(wrappedDrawable, Color.parseColor("#EC0000"))
            "psychic" -> DrawableCompat.setTint(wrappedDrawable, Color.parseColor("#F3E44196"))
            "rock" -> DrawableCompat.setTint(wrappedDrawable, Color.parseColor("#7A560C"))
            "ice" -> DrawableCompat.setTint(wrappedDrawable, Color.parseColor("#FF018786"))
            "ghost" -> DrawableCompat.setTint(wrappedDrawable, Color.parseColor("#643188"))
            "dragon" -> DrawableCompat.setTint(wrappedDrawable, Color.parseColor("#361B93"))
            "dark" -> DrawableCompat.setTint(wrappedDrawable, Color.parseColor("#53310C"))
            "steel" -> DrawableCompat.setTint(wrappedDrawable, Color.parseColor("#7E7E7E"))
            "fairy" -> DrawableCompat.setTint(wrappedDrawable, Color.parseColor("#FCAEFF"))
            else -> DrawableCompat.setTint(wrappedDrawable, Color.GRAY)
        }
    }
