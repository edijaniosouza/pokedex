package br.com.edijanio.pokedex.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import br.com.edijanio.pokedex.database.entity.PokemonEntity

@Dao
interface PokemonDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPokemons(pokemons: List<PokemonEntity> )

    @Query("SELECT * FROM pokemonentity ORDER BY id")
    fun findAll() : LiveData<List<PokemonEntity>?>

    @Query("SELECT * FROM pokemonentity WHERE id == :pokemonId")
    fun findById(pokemonId: Int) : LiveData<PokemonEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(pokemon: PokemonEntity)

    @Update
    suspend fun updatePokemon(pokemon: PokemonEntity)

    @Query("SELECT * FROM pokemonentity WHERE isFavorite == :valor")
    fun findOnlyFavorites(valor: Boolean): LiveData<List<PokemonEntity>?>

    @Query("SELECT * FROM pokemonentity WHERE name LIKE :name")
    fun findByName(name: String) : LiveData<List<PokemonEntity>?>
}