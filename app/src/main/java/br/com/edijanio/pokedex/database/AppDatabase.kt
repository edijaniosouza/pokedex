package br.com.edijanio.pokedex.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import br.com.edijanio.pokedex.database.converter.PokemonTypeConverter
import br.com.edijanio.pokedex.database.dao.PokemonDAO
import br.com.edijanio.pokedex.database.entity.PokemonEntity
import br.com.edijanio.pokedex.model.pokemonInformation.Pokemon

private const val DATABASE_NAME = "pokedex.db"

@Database(entities = [PokemonEntity::class], version = 3, exportSchema = false)
@TypeConverters(PokemonTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract val pokemonDAO: PokemonDAO

    companion object {
        private lateinit var db: AppDatabase

        fun getInstance(context: Context): AppDatabase {
            if (::db.isInitialized) return db

            db = Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                DATABASE_NAME
            )
                .fallbackToDestructiveMigration()
                .build()

            return db
        }
    }

}