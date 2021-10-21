package br.com.edijanio.pokedex.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.edijanio.pokedex.R
import br.com.edijanio.pokedex.adapter.RecyclerAdapterMain
import br.com.edijanio.pokedex.database.AppDatabase
import br.com.edijanio.pokedex.database.entity.PokemonEntity
import br.com.edijanio.pokedex.model.pokemonInformation.Pokemon
import br.com.edijanio.pokedex.repository.PokemonRepository
import br.com.edijanio.pokedex.util.POKEMON_CHAVE
import br.com.edijanio.pokedex.viewmodel.PokemonListActivityViewModel
import br.com.edijanio.pokedex.viewmodel.factory.PokemonListActivityViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*

const val LOAD_ERROR = "Erro ao carregar lista"

class PokemonListActivity : AppCompatActivity() {

    private val adapter by lazy {
        RecyclerAdapterMain(context = this)
    }
    private val layoutManager by lazy {
        LinearLayoutManager(this)
    }
    private val viewModel by lazy {
        val repository = PokemonRepository(AppDatabase.getInstance(this).pokemonDAO)
        val factory = PokemonListActivityViewModelFactory(repository)
        ViewModelProvider(this, factory)
            .get(PokemonListActivityViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        startConfiguration()
    }

    private fun startConfiguration() {
        setSupportActionBar(materialAppBar)
        recycleViewConfigurations()
        loadPokemons()
    }

    private fun recycleViewConfigurations() {
        Log.d("pokemonDetails", "ue1..")
        recycleView_main.layoutManager = layoutManager
        recycleView_main.adapter = adapter
        adapter.onItemClicked = ::openPokemonDetails
    }

    private fun openPokemonDetails(pokemonId: PokemonEntity) {
        val intent = Intent(this, PokemonDetailsActivity::class.java)
        intent.putExtra(POKEMON_CHAVE, pokemonId.id)
        startActivity(intent)
    }

    private fun loadPokemons() {
        viewModel.findAll().observe(this, { pokemonList ->
            pokemonList.data?.let {list ->
                adapter.update(data = list)
            }
            pokemonList.error?.let {
                Toast.makeText(this, LOAD_ERROR, Toast.LENGTH_SHORT).show()
            }
        })


    }


}

