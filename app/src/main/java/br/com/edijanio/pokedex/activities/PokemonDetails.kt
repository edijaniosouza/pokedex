package br.com.edijanio.pokedex.activities

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.marginEnd
import br.com.edijanio.pokedex.R
import br.com.edijanio.pokedex.databinding.ActivityPokemonDetailsBinding
import br.com.edijanio.pokedex.model.pokemonInformation.PokemonInfoModel
import br.com.edijanio.pokedex.util.changeTypeColor
import com.squareup.picasso.Picasso

class PokemonDetails : AppCompatActivity() {
    private lateinit var binding : ActivityPokemonDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPokemonDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        intent.extras?.getParcelable<PokemonInfoModel>("POKEMON_DETAILS").let {
            binding.materialAppBar.title = it?.name?.uppercase()
            Picasso.get().load(it?.sprites?.other?.officialArtwork?.front_default).into(binding.ivPokemon)


            binding.tvTypeInfo1.text = it?.types?.get(0)?.type?.name?.uppercase()
            val unwrappedDrawable =  binding.tvTypeInfo1.background
            val wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable)
            it?.types?.get(0)?.type?.name?.let { it1 -> changeTypeColor(it1, wrappedDrawable) }

            if(it?.types?.size!! > 1){
                binding.tvTypeInfo2.visibility = VISIBLE
                binding.tvTypeInfo2.text = it.types[1].type.name.uppercase()

                val unwrappedDrawable2 =  binding.tvTypeInfo2.background
                val wrappedDrawable2 = DrawableCompat.wrap(unwrappedDrawable2)
                changeTypeColor( it.types[1].type.name, wrappedDrawable2)
            }else{
                binding.tvTypeInfo2.visibility = GONE
            }

            val heightValue = it.height.toFloat().let { it1 -> convertValues(it1) }
            val weightValue = it.weight.toFloat().let { it1 -> convertValues(it1) }
            binding.tvHeightInfo.text = "$heightValue m"
            binding.tvWeightInfo.text = "$weightValue Kg"
        }

        binding.materialAppBar.setNavigationOnClickListener{
            finish()
        }

    }

    private fun convertValues(value : Float): Float{
        return value / 10
    }
}