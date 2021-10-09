package br.com.edijanio.pokedex.activities

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.edijanio.pokedex.R
import br.com.edijanio.pokedex.adapter.RecyclerAdapterMain
import br.com.edijanio.pokedex.api.RetroftInstance
import br.com.edijanio.pokedex.databinding.ActivityMainBinding
import br.com.edijanio.pokedex.model.PokemonModel
import br.com.edijanio.pokedex.model.pokemonInformation.PokemonInfoModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private val retrofit = RetroftInstance()
    private var pokemonInfo = mutableListOf<PokemonInfoModel>()
    private var offset = 0
    private var limit = 20
    private lateinit var rv : RecyclerView
    private lateinit var layoutManager : RecyclerView.LayoutManager
    private lateinit var adapter :RecyclerView.Adapter<RecyclerAdapterMain.ViewHolder>
    private var isSearch = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        config()
    }

    private fun config(){
        setSupportActionBar(binding.materialAppBar)
        rv = binding.rvPokemonList!!
        adapter = RecyclerAdapterMain(pokemonInfo)
        layoutManager = LinearLayoutManager(this)
        rv.adapter = adapter
        rv.layoutManager = layoutManager
        endLessScroll(rv)
        getPokemons()
    }

    private fun changeSearchState(){
        if (isSearch) {
            offset = 0
            isSearch = false
        }
        else isSearch = true

    }

    private fun endLessScroll(rv : RecyclerView){
        rv.addOnScrollListener(object: RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val currentLastVisible = (layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                if(currentLastVisible > (pokemonInfo.size - 2) && !isSearch){
                    offset += 20
                    getPokemons()
                }
            }
        })
    }

    val searchArray = mutableListOf<PokemonInfoModel>()
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.main, menu)

        val search = menu?.findItem(R.id.search_menu)
        val searchView = search?.actionView as? SearchView

        searchView?.setOnCloseListener {
            changeSearchState()
            getPokemons()
            false
        }
        searchView?.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null && query != "") {
                    changeSearchState()
                    getPokemonByName(query)
                    pokemonInfo.clear()
                    pokemonInfo.addAll(searchArray)
                    adapter.notifyDataSetChanged()
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean { return false }

        })
        return true
    }

    private fun getPokemonByName(pokemonName : String){
        val callback = retrofit.api().getPokemonByIdOrName(pokemonName)

        callback.enqueue(object: Callback<PokemonInfoModel>{
            override fun onResponse(
                call: Call<PokemonInfoModel>,
                response: Response<PokemonInfoModel>
            ) {
                searchArray.clear()
                val responseBody = response.body()
                if(responseBody != null){
                    searchArray.clear()
                    searchArray.add(responseBody)
                }
            }

            override fun onFailure(call: Call<PokemonInfoModel>, t: Throwable) {
                Log.d("Falha", "Erro ao pesquisar por ID")
            }
        })

    }

    private fun getPokemons() {
        val callback = retrofit.api().getPokemon(offset, limit)

        callback.enqueue(object: Callback<PokemonModel> {
            override fun onResponse(
                call: retrofit2.Call<PokemonModel>,
                response: Response<PokemonModel>
            ) {
                val callbackResponse = response.body()!!

                for(result in callbackResponse.results){
                    getPokemonInfo(result.name)
                }
            }

            override fun onFailure(call: retrofit2.Call<PokemonModel>, t: Throwable) {
                Log.d(TAG, "${t.message}")
            }

        })

    }

    fun getPokemonInfo(pokemon_name: String) {
        val searchForPokemon = retrofit.api().getPokemonByIdOrName(pokemon_name)

        searchForPokemon.enqueue(object: Callback<PokemonInfoModel>{
            override fun onResponse(
                call: Call<PokemonInfoModel>,
                response: Response<PokemonInfoModel>
            ) {
                fetchNameData(response.body()!!)
            }

            override fun onFailure(call: Call<PokemonInfoModel>, t: Throwable) {
                Log.d("Falha", "Erro ao pesquisar por ID")
            }
        })
    }


    private fun fetchNameData(pokemon: PokemonInfoModel) {
        pokemonInfo.add(pokemon)
        pokemonInfo.sortBy {
            it.id
        }
        adapter.notifyItemInserted(pokemonInfo.indexOf(pokemon))
    }

}

