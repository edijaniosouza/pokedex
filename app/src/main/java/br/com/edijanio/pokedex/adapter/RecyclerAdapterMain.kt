package br.com.edijanio.pokedex.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView
import br.com.edijanio.pokedex.R
import br.com.edijanio.pokedex.activities.PokemonDetails
import br.com.edijanio.pokedex.model.pokemonInformation.PokemonInfoModel
import br.com.edijanio.pokedex.util.changeTypeColor
import com.squareup.picasso.Picasso
import java.text.DecimalFormat

class RecyclerAdapterMain(private val pokemonsList: MutableList<PokemonInfoModel>) : RecyclerView.Adapter<RecyclerAdapterMain.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.card_pokemon, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.pokemonId.text = "#${pokemonsList[position].id}"
        holder.pokemonName.text = pokemonsList[position].name.uppercase()
        Picasso.get().load(pokemonsList[position].sprites.other.officialArtwork.front_default).into(holder.pokemonImage)

        val typeName = pokemonsList[position].types[0].type.name
        holder.pokemonType.text = typeName.uppercase()

        val unwrappedDrawable = holder.pokemonType.background
        val wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable)
        changeTypeColor(typeName, wrappedDrawable)

        if(pokemonsList[position].types.size > 1){
            holder.pokemonType2.visibility = VISIBLE
            val typeName2 = pokemonsList[position].types[1].type.name
            holder.pokemonType2.text = typeName2.uppercase()

            val unwrappedDrawable2 = holder.pokemonType2.background
            val wrappedDrawable2 = DrawableCompat.wrap(unwrappedDrawable2)
            changeTypeColor(typeName2, wrappedDrawable2)

        } else{
            holder.pokemonType2.visibility = GONE
        }

    }

    override fun getItemCount(): Int {
        return pokemonsList.size
    }

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        var pokemonId : TextView
        var pokemonName : TextView
        var pokemonImage : ImageView
        var pokemonType :TextView
        var pokemonType2 : TextView

        init{
            pokemonId = itemView.findViewById(R.id.tv_pokemonID)
            pokemonName = itemView.findViewById(R.id.pokemon_name)
            pokemonImage = itemView.findViewById(R.id.pokemon_image)
            pokemonType = itemView.findViewById(R.id.pokemon_type1)
            pokemonType2 = itemView.findViewById(R.id.pokemon_type2)

            itemView.setOnClickListener{
                val position = adapterPosition

                val pokemonDetailsIntent = Intent(itemView.context, PokemonDetails::class.java)
                pokemonDetailsIntent.putExtra("POKEMON_DETAILS", pokemonsList[position])
                itemView.context.startActivity(pokemonDetailsIntent)
            }
        }
    }
}