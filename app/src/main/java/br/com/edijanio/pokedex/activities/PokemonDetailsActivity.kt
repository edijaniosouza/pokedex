package br.com.edijanio.pokedex.activities

import android.os.Bundle
import android.view.Menu
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.DrawableCompat
import androidx.lifecycle.ViewModelProvider
import br.com.edijanio.pokedex.R
import br.com.edijanio.pokedex.database.AppDatabase
import br.com.edijanio.pokedex.database.entity.PokemonEntity
import br.com.edijanio.pokedex.repository.PokemonRepository
import br.com.edijanio.pokedex.util.POKEMON_CHAVE
import br.com.edijanio.pokedex.util.changeTypeColor
import br.com.edijanio.pokedex.viewmodel.PokemonDetailsActivityViewModel
import br.com.edijanio.pokedex.viewmodel.factory.PokemonDetailsActivityViewModelFactory
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_pokemon_details.*

class PokemonDetailsActivity : AppCompatActivity() {

    private val pokemonId by lazy {
        intent.extras?.getInt(POKEMON_CHAVE, 0)
    }

    private val viewModel by lazy {
        val repository = PokemonRepository(AppDatabase.getInstance(this).pokemonDAO)
        val factory = PokemonDetailsActivityViewModelFactory(repository)
        ViewModelProvider(this, factory)
            .get(PokemonDetailsActivityViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pokemon_details)
        config()
    }

    private fun config() {
        setSupportActionBar(mab_pokemonDetails)
        pokemonId?.let { fetchData(it) }
        mab_pokemonDetails.setNavigationOnClickListener { finish() }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.pokemon_details, menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun fetchData(id : Int) {

        val pokemon = viewModel.getPokemonById(id)
        pokemon.observe(this, {
            setContent(it.data)
            it.error.let {errorMessage ->
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setContent(pokemon: PokemonEntity?) {
        pokemon?.let { pokemonModel ->
            mab_pokemonDetails.title = pokemonModel.name.uppercase()
            Picasso.get().load(pokemonModel.image).into(iv_pokemon)

            changeBackgroundColor(pokemonModel, tv_typeInfo1, pokemonModel.type1)
            configVisibilityOfTypes(pokemonModel)
            setHeightAndWeightView(pokemonModel)
        }
    }

    private fun setHeightAndWeightView(pokemonModel: PokemonEntity) {
        val heightValue = pokemonModel.height.toFloat().let { it1 -> convertValues(it1) }
        val weightValue = pokemonModel.weight.toFloat().let { it1 -> convertValues(it1) }
        tv_heightInfo.text = "$heightValue m"
        tv_weightInfo.text = "$weightValue Kg"
    }

    private fun configVisibilityOfTypes(pokemonModel: PokemonEntity) {
        if (pokemonModel.type2 != null) {
            tv_typeInfo2.visibility = VISIBLE
            changeBackgroundColor(pokemonModel, tv_typeInfo2, pokemonModel.type2)
        } else {
            tv_typeInfo2.visibility = GONE
        }
    }

    private fun changeBackgroundColor(pokemonModel: PokemonEntity, tv_type: TextView, type: String) {
        tv_type.text = type.uppercase()
        val unwrappedDrawable = tv_type.background
        val wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable)
        changeTypeColor(type, wrappedDrawable)
    }

    private fun convertValues(value: Float): Float {
        return value / 10
    }
}