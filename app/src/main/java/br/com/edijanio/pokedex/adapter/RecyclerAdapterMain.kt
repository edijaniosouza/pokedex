package br.com.edijanio.pokedex.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView
import br.com.edijanio.pokedex.R
import br.com.edijanio.pokedex.activities.PokemonDetailsActivity
import br.com.edijanio.pokedex.database.entity.PokemonEntity
import br.com.edijanio.pokedex.model.pokemonInformation.Pokemon
import br.com.edijanio.pokedex.util.POKEMON_CHAVE
import br.com.edijanio.pokedex.util.changeTypeColor
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.card_pokemon.view.*

class RecyclerAdapterMain(
    private val context: Context,
    private val pokemonsList: MutableList<PokemonEntity> = mutableListOf<PokemonEntity>(),
    var onItemClicked: (pokemon: PokemonEntity) -> Unit = {}
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

    fun update(data: List<PokemonEntity>) {
        notifyItemRangeRemoved(0,pokemonsList.size)
        this.pokemonsList.clear()
        this.pokemonsList.addAll(data)
        notifyItemRangeInserted(0,pokemonsList.size)
    }

    fun add(pokemon: PokemonEntity) {
        val position = itemCount

        if(!pokemonsList.contains(pokemon)){
            pokemonsList.add(position, pokemon)
            notifyItemInserted(position)
        }
    }


    inner class ViewHolder(itemView : View) :
        RecyclerView.ViewHolder(itemView){
        private lateinit var pokemon : PokemonEntity

        init{
            itemView.setOnClickListener{
                if (::pokemon.isInitialized) {
                    onItemClicked(pokemon)
                }
            }
        }

        fun linkPokemonDetails(
            pokemon: PokemonEntity
        ) {
            this.pokemon =pokemon
            when {
                pokemon.id < 10 -> {
                    "#00${pokemon.id}".also {
                        itemView.tv_pokemonID.text = it }
                }
                pokemon.id < 100 -> {
                    "#0${pokemon.id}".also { itemView.tv_pokemonID.text = it }
                }
                else -> {
                    "#${pokemon.id}".also { itemView.tv_pokemonID.text = it }
                }
            }
            itemView.pokemon_name.text = pokemon.name.uppercase()
            Picasso.get()
                .load(pokemon.image)
                .into(itemView.pokemon_image)

            val typeName = pokemon.type1
            itemView.pokemon_type1.text = typeName.uppercase()
            val unwrappedDrawable = itemView.pokemon_type1.background
            val wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable)
            changeTypeColor(typeName, wrappedDrawable)

            if (pokemon.type2 != null) {
                itemView.pokemon_type2.visibility = VISIBLE
                val typeName2 = pokemon.type2
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