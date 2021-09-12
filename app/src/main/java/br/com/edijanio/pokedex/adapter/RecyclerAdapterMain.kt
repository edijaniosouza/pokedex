package br.com.edijanio.pokedex.adapter

import android.content.ContentValues
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import br.com.edijanio.pokedex.R
import br.com.edijanio.pokedex.api.RetroftInstance
import br.com.edijanio.pokedex.databinding.ActivityMainBinding
import br.com.edijanio.pokedex.model.PokemonModel
import br.com.edijanio.pokedex.model.Result
import retrofit2.Callback
import retrofit2.Response

class RecyclerAdapterMain : RecyclerView.Adapter<RecyclerAdapterMain.ViewHolder>() {

    //private var names = arrayOf("bulbasaur", "ivysaur", "charmeleon", "Charizard", "bulbasaur", "ivysaur", "charmeleon", "Charizard", "bulbasaur", "ivysaur", "charmeleon", "Charizard")
    private var type = arrayOf("Grama", "Grama", "Fogo", "Fogo", "Grama", "Grama", "Fogo", "Fogo", "Grama", "Grama", "Fogo", "Fogo")
    private var images = arrayOf(R.drawable.bulbarsaur, R.drawable.ivysaur, R.drawable.charmeleon, R.drawable.charizard, R.drawable.bulbarsaur, R.drawable.ivysaur, R.drawable.charmeleon, R.drawable.charizard, R.drawable.bulbarsaur, R.drawable.ivysaur, R.drawable.charmeleon, R.drawable.charizard)
    private var names = mutableListOf<String>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.card_pokemon, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.pokemonName.text = names[position]
        holder.pokemonImage.setImageResource(images[position])
        holder.pokemonType.text = type[position]
    }

    override fun getItemCount(): Int {
        return 20
    }

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        var pokemonName : TextView
        var pokemonImage : ImageView
        var pokemonType :TextView
        /*var pokemonWeight : TextView
        var pokemonHeight : TextView
*/
        init{
            pokemonName = itemView.findViewById(R.id.pokemon_name)
            pokemonImage = itemView.findViewById(R.id.pokemon_image)
            pokemonType = itemView.findViewById(R.id.pokemon_type1)

            itemView.setOnClickListener{
                val position = adapterPosition
                Toast.makeText(itemView.context, "clicou em ${names[position]}", Toast.LENGTH_LONG).show()
            }
        }
    }
}