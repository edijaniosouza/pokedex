package br.com.edijanio.pokedex.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.edijanio.pokedex.R
import br.com.edijanio.pokedex.adapter.RecyclerAdapterMain
import br.com.edijanio.pokedex.database.AppDatabase
import br.com.edijanio.pokedex.repository.PokemonRepository
import br.com.edijanio.pokedex.viewmodel.PokemonFavoriteListActivityViewModel
import br.com.edijanio.pokedex.viewmodel.factory.PokemonFavoriteListActivityViewModelFactory
import kotlinx.android.synthetic.main.activity_pokemon_favorite_list.*


class PokemonFavoriteListActivity : AppCompatActivity() {

    private val adapter by lazy {
        RecyclerAdapterMain(context = this)
    }
    private val layoutManager by lazy {
        LinearLayoutManager(this)
    }

    private val viewModel by lazy {
        val repository = PokemonRepository(AppDatabase.getInstance(this).pokemonDAO)
        val factory = PokemonFavoriteListActivityViewModelFactory(repository)
        ViewModelProvider(this, factory)
            .get(PokemonFavoriteListActivityViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pokemon_favorite_list)
        startConfig()
        loadFavorites()

        materialAppBar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun startConfig() {
        recycleView_main.layoutManager = layoutManager
        recycleView_main.adapter = adapter
    }

    private fun loadFavorites() {
        viewModel.loadOnlyFavorites().observe(this, {listPokemons ->
            listPokemons?.let{
                adapter.update(listPokemons)
            }
        })
    }
}