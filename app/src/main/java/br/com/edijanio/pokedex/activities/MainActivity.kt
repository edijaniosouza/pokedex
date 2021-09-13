package br.com.edijanio.pokedex.activities

import android.app.SearchManager
import android.content.ContentValues.TAG
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.SearchView
import androidx.core.widget.NestedScrollView
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
    private lateinit var layoutManager : RecyclerView.LayoutManager
    private lateinit var adapter :RecyclerView.Adapter<RecyclerAdapterMain.ViewHolder>
    private val retrofit = RetroftInstance()
    private val rv: RecyclerView by lazy{
        findViewById(R.id.rvPokemonList)
    }
    private var pokemonInfo = mutableListOf<PokemonInfoModel>()
    private var offset = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getPokemons()
    }

    fun getPokemons() {
        val callback = retrofit.api().getPokemon(offset)

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
        rv.apply{
            adapter = RecyclerAdapterMain(pokemonInfo)
            layoutManager = LinearLayoutManager(this@MainActivity)
        }
    }

}

