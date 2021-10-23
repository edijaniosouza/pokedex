package br.com.edijanio.pokedex.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.com.edijanio.pokedex.database.entity.PokemonEntity

@Dao
interface PokemonDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPokemons(pokemons: List<PokemonEntity> )

    @Query("SELECT * FROM pokemonentity ORDER BY id")
    fun findAll() : LiveData<List<PokemonEntity>?>

    @Query("SELECT * FROM pokemonentity WHERE id == :pokemonId")
    fun findById(pokemonId: Int) : LiveData<PokemonEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(pokemon: PokemonEntity)
}