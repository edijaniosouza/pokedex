package br.com.edijanio.pokedex.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import br.com.edijanio.pokedex.database.dao.PokemonDAO
import br.com.edijanio.pokedex.model.pokemonInformation.Pokemon

const val DATABASE_NAME = "pokedex.db"

@Database(entities = [Pokemon::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract val pokemonDAO: PokemonDAO

    companion object {
        private lateinit var db: AppDatabase

        fun getInstance(context: Context) : AppDatabase{
            if(::db.isInitialized) return db

            db = Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                DATABASE_NAME
            ).build()

            return db
        }
    }

}