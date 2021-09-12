package br.com.edijanio.pokedex

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.edijanio.pokedex.adapter.RecyclerAdapterMain
import br.com.edijanio.pokedex.api.RetroftInstance
import br.com.edijanio.pokedex.databinding.ActivityMainBinding
import br.com.edijanio.pokedex.model.PokemonModel
import br.com.edijanio.pokedex.model.Result
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private lateinit var layoutManager : RecyclerView.LayoutManager
    private lateinit var adapter :RecyclerView.Adapter<RecyclerAdapterMain.ViewHolder>

    private val rv: RecyclerView by lazy{
        findViewById(R.id.rvPokemonList)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        layoutManager = LinearLayoutManager(this)
        rv.layoutManager = layoutManager
        val pokemonList = getPokemons()
        adapter = RecyclerAdapterMain()
        rv.adapter = adapter

        clickListeners()

    }

    private fun clickListeners(){

    }


    fun getPokemons(): MutableList<String> {
        val retrofit = RetroftInstance()
        val callback = retrofit.api().getPokemon()
        var list = mutableListOf<String>()

        callback.enqueue(object: Callback<PokemonModel> {
            override fun onFailure(call: retrofit2.Call<PokemonModel>, t: Throwable) {
                Log.d(ContentValues.TAG, "${t.message}")
            }

            override fun onResponse(
                call: retrofit2.Call<PokemonModel>,
                response: Response<PokemonModel>
            ) {
                Log.d(ContentValues.TAG, "${response.body()?.results?.get(0)}")
                for (info in response.body()?.results!!){
                    list.add(info.name)
                }

            }

        })

        return list
    }

}