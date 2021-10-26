package br.com.edijanio.pokedex.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.edijanio.pokedex.R
import br.com.edijanio.pokedex.adapter.RecyclerAdapterMain
import br.com.edijanio.pokedex.database.AppDatabase
import br.com.edijanio.pokedex.database.entity.PokemonEntity
import br.com.edijanio.pokedex.repository.PokemonRepository
import br.com.edijanio.pokedex.repository.Resource
import br.com.edijanio.pokedex.util.POKEMON_CHAVE
import br.com.edijanio.pokedex.viewmodel.PokemonListActivityViewModel
import br.com.edijanio.pokedex.viewmodel.factory.PokemonListActivityViewModelFactory
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.materialAppBar
import kotlinx.android.synthetic.main.activity_main.recycleView_main
import kotlinx.android.synthetic.main.activity_pokemon_details.*
import kotlinx.android.synthetic.main.activity_pokemon_favorite_list.*

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
    private var isSearching = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        startConfiguration()
    }

    private fun startConfiguration() {
        recycleViewConfigurations()
        loadPokemons()
        endlessScroll()
        menuClickListener()
    }

    private fun menuClickListener() {
        materialAppBar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.favorite_list -> {
                    val intent = Intent(this, PokemonFavoriteListActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.search_menu -> {
                    searchEngine()
                    true
                }
                else -> {
                    false
                }
            }
        }
    }

    private fun searchEngine() {
        val menuItem = materialAppBar.menu.findItem(R.id.search_menu)
        val searchView = menuItem.actionView as SearchView
        searchView.queryHint = "Search for pokemon id or name"

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (adapter.itemCount == 0) {
                    viewModel.getOnPokemonByNameOrId(newText)
                        .observe(this@PokemonListActivity, { pokemonList ->
                            handleUpdateAdapter(pokemonList)
                        })
                }
                adapter.getFilter().filter(newText)
                return true
            }
        })

        menuItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
                isSearching = true
                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                loadPokemons()
                isSearching = false
                return true
            }

        })

    }

    private fun endlessScroll() {
        recycleView_main.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val lastItem = layoutManager.findLastCompletelyVisibleItemPosition()
                val listSize = adapter.itemCount

                if (lastItem >= listSize - 6 && !isSearching) {
                    loadMorePokemonsViewModel(listSize)
                }
            }
        })

    }

    private fun loadMorePokemonsViewModel(listSize: Int) {
        viewModel.loadMorePokemons(listSize + 1)
            .observe(this@PokemonListActivity, { resource ->
                resource?.data?.let { pokemon ->
                    adapter.add(pokemon)
                }
                resource?.error?.let {
//                    Toast.makeText(
//                        this@PokemonListActivity,
//                        "Erro ao carregar pokemon",
//                        Toast.LENGTH_SHORT
//                    ).show()
                }
            })
    }

    private fun recycleViewConfigurations() {
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
            handleUpdateAdapter(pokemonList)
        })
    }

    private fun handleUpdateAdapter(pokemonList: Resource<List<PokemonEntity>?>) {
        if (!pokemonList.data.isNullOrEmpty()) {
            adapter.update(data = pokemonList.data)
        } else if (!pokemonList.error.isNullOrEmpty()) {
            Snackbar.make(recycleView_main, pokemonList.error, Snackbar.LENGTH_LONG).show()
        } else {
            Snackbar.make(recycleView_main, LOAD_ERROR, Snackbar.LENGTH_LONG).show()
        }
    }


}

