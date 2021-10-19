package br.com.edijanio.pokedex.activities

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.edijanio.pokedex.R
import br.com.edijanio.pokedex.adapter.RecyclerAdapterMain
import br.com.edijanio.pokedex.api.RetroftInstance
import br.com.edijanio.pokedex.model.PokemonModel
import br.com.edijanio.pokedex.model.pokemonInformation.Pokemon
import br.com.edijanio.pokedex.util.POKEMON_CHAVE
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PokemonListActivity : AppCompatActivity() {

    private val adapter by lazy {
        RecyclerAdapterMain(context = this)
    }
    private var isSearch = false
    private val retrofit = RetroftInstance()
    private var offset = 0
    private var limit = 20

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        startConfiguration()
    }

    private fun startConfiguration() {
        setSupportActionBar(materialAppBar)
        recycleViewConfigurations()
        getPokemons()

    }

    private fun recycleViewConfigurations() {
        val divisor = DividerItemDecoration(this, LinearLayoutManager.VERTICAL)
        rvPokemonList.addItemDecoration(divisor)
        rvPokemonList.adapter = adapter
        adapter.onItemClicked = {
            openPokemonDetails(it)
        }
    }

    private fun openPokemonDetails(pokemon: Pokemon) {
        val intent= Intent(this, PokemonDetailsActivity::class.java)
        intent.putExtra(POKEMON_CHAVE, pokemon)
        startActivity(intent)
    }

//    private fun changeSearchState() {
//        if (isSearch) {
//            offset = 0
//            isSearch = false
//        } else isSearch = true
//
//    }

//    private fun endLessScroll(rv : RecyclerView){
//        rv.addOnScrollListener(object: RecyclerView.OnScrollListener(){
//            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                super.onScrolled(recyclerView, dx, dy)
//                val currentLastVisible = (layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
//                if(currentLastVisible > (pokemonInfo.size - 2) && !isSearch){
//                    offset += 20
//                    getPokemons()
//                }
//            }
//        })
//    }

//    val searchArray = mutableListOf<Pokemon>()
//
//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        super.onCreateOptionsMenu(menu)
//        menuInflater.inflate(R.menu.main, menu)
//
//        val search = menu?.findItem(R.id.search_menu)
//        val searchView = search?.actionView as? SearchView
//
//        searchView?.setOnCloseListener {
//            changeSearchState()
//            getPokemons()
//            false
//        }
//        searchView?.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
//            override fun onQueryTextSubmit(query: String?): Boolean {
//                if (query != null && query != "") {
//                    changeSearchState()
//                    getPokemonByName(query)
//                    pokemonInfo.clear()
//                    pokemonInfo.addAll(searchArray)
//                    adapter.notifyDataSetChanged()
//                }
//                return true
//            }
//
//            override fun onQueryTextChange(newText: String?): Boolean { return false }
//
//        })
//        return true
//    }

    private fun getPokemonByName(pokemonName: String) {
        val callback = retrofit.pokeApiService.getPokemonByIdOrName(pokemonName)

        callback.enqueue(object : Callback<Pokemon> {
            override fun onResponse(
                call: Call<Pokemon>,
                response: Response<Pokemon>
            ) {
                searchArray.clear()
                val responseBody = response.body()
                if (responseBody != null) {
                    searchArray.clear()
                    searchArray.add(responseBody)
                }
            }

            override fun onFailure(call: Call<Pokemon>, t: Throwable) {
                Log.d("Falha", "Erro ao pesquisar por ID")
            }
        })

    }

    private fun getPokemons() {
        val callback = retrofit.pokeAPIservice().getPokemon(offset, limit)

        callback.enqueue(object : Callback<PokemonModel> {
            override fun onResponse(
                call: retrofit2.Call<PokemonModel>,
                response: Response<PokemonModel>
            ) {
                val callbackResponse = response.body()!!

                for (result in callbackResponse.results) {
                    getPokemonInfo(result.name)
                }
            }

            override fun onFailure(call: retrofit2.Call<PokemonModel>, t: Throwable) {
                Log.d(TAG, "${t.message}")
            }

        })

    }

    fun getPokemonInfo(pokemon_name: String) {
        val searchForPokemon = retrofit.pokeAPIservice().getPokemonByIdOrName(pokemon_name)

        searchForPokemon.enqueue(object : Callback<Pokemon> {
            override fun onResponse(
                call: Call<Pokemon>,
                response: Response<Pokemon>
            ) {
                fetchNameData(response.body()!!)
            }

            override fun onFailure(call: Call<Pokemon>, t: Throwable) {
                Log.d("Falha", "Erro ao pesquisar por ID")
            }
        })
    }


    private fun fetchNameData(pokemon: Pokemon) {
        pokemonInfo.add(pokemon)
        pokemonInfo.sortBy {
            it.id
        }
        adapter.notifyItemInserted(pokemonInfo.indexOf(pokemon))
    }

}

