package br.com.edijanio.pokedex.activities

import android.os.Bundle
import android.view.Menu
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.DrawableCompat
import br.com.edijanio.pokedex.R
import br.com.edijanio.pokedex.model.pokemonInformation.Pokemon
import br.com.edijanio.pokedex.util.POKEMON_CHAVE
import br.com.edijanio.pokedex.util.changeTypeColor
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_pokemon_details.*

class PokemonDetailsActivity : AppCompatActivity() {

    private val pokemon by lazy {
        intent.extras?.getParcelable<Pokemon>(POKEMON_CHAVE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.card_pokemon)
        config()
    }

    fun config(){
        setSupportActionBar(mab_pokemonDetails)
        fetchData()

        mab_pokemonDetails.setNavigationOnClickListener{ finish() }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.pokemon_details, menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun fetchData(){
        pokemon?.let {pokemonModel ->
            mab_pokemonDetails.title = pokemonModel.name.uppercase()
            Picasso.get().load(pokemonModel.sprites.other.officialArtwork.front_default).into(iv_pokemon)

            tv_typeInfo1.text = pokemonModel.types.get(0).type.name.uppercase()
            val unwrappedDrawable =  tv_typeInfo1.background
            val wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable)
            pokemonModel.types.get(0).type.name.let { it1 -> changeTypeColor(it1, wrappedDrawable) }

            if(pokemonModel.types.size > 1){
                tv_typeInfo2.visibility = VISIBLE
                tv_typeInfo2.text = pokemonModel.types[1].type.name.uppercase()

                val unwrappedDrawable2 =  tv_typeInfo2.background
                val wrappedDrawable2 = DrawableCompat.wrap(unwrappedDrawable2)
                changeTypeColor( pokemonModel.types[1].type.name, wrappedDrawable2)
            }else{
                tv_typeInfo2.visibility = GONE
            }

            val heightValue = pokemonModel.height.toFloat().let { it1 -> convertValues(it1) }
            val weightValue = pokemonModel.weight.toFloat().let { it1 -> convertValues(it1) }
            tv_heightInfo.text = "$heightValue m"
            tv_weightInfo.text = "$weightValue Kg"
        }
    }

    private fun convertValues(value : Float): Float{
        return value / 10
    }
}