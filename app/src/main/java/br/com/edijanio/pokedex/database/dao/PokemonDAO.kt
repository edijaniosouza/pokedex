package br.com.edijanio.pokedex.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.com.edijanio.pokedex.database.entity.PokemonEntity
import br.com.edijanio.pokedex.model.pokemonInformation.Pokemon

@Dao
interface PokemonDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPokemons(pokemons: List<PokemonEntity> )

    @Query("SELECT * FROM pokemonentity ORDER BY id")
    suspend fun findAll() : List<PokemonEntity>?

    @Query("SELECT * FROM pokemonentity WHERE id == :pokemonId")
    suspend fun findById(pokemonId: Int) : PokemonEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(pokemon: PokemonEntity)
}