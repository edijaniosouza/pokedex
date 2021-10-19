package br.com.edijanio.pokedex.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView
import br.com.edijanio.pokedex.R
import br.com.edijanio.pokedex.model.pokemonInformation.Pokemon
import br.com.edijanio.pokedex.util.changeTypeColor
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.card_pokemon.view.*

class RecyclerAdapterMain(
    private val context: Context,
    private val pokemonsList: MutableList<Pokemon> = mutableListOf<Pokemon>(),
    var onItemClicked: (Pokemon)-> Unit = {}
) : RecyclerView.Adapter<RecyclerAdapterMain.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val createdView = LayoutInflater.from(context)
            .inflate(R.layout.card_pokemon, parent, false)
        return ViewHolder(createdView)
    }

    override fun getItemCount(): Int = pokemonsList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pokemon = pokemonsList[position]
        holder.linkPokemonDetails(pokemon)
    }


    inner class ViewHolder(itemView : View) :
        RecyclerView.ViewHolder(itemView){
        lateinit var pokemon : Pokemon

        init{
            itemView.setOnClickListener{

                if(::pokemon.isInitialized){
                    onItemClicked(pokemon)
                }
            }
        }

        fun linkPokemonDetails(
            pokemon: Pokemon
        ) {
            itemView.tv_pokemonID.text = pokemon.id.toString()
            itemView.pokemon_name.text = pokemon.name.uppercase()

            Picasso.get().load(pokemon.sprites.other.officialArtwork.front_default)
                .into(itemView.pokemon_image)

            val typeName = pokemon.types[0].type.name
            itemView.pokemon_type1.text = typeName.uppercase()
            val unwrappedDrawable = itemView.pokemon_type1.background
            val wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable)
            changeTypeColor(typeName, wrappedDrawable)

            if (pokemon.types.size > 1) {
                itemView.pokemon_type2.visibility = VISIBLE
                val typeName2 = pokemonsList[position].types[1].type.name
                itemView.pokemon_type2.text = typeName2.uppercase()
                val unwrappedDrawable2 = itemView.pokemon_type2.background
                val wrappedDrawable2 = DrawableCompat.wrap(unwrappedDrawable2)
                changeTypeColor(typeName2, wrappedDrawable2)

            } else {
                itemView.pokemon_type2.visibility = GONE
            }
        }
    }
}