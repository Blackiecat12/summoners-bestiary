package com.example.summonapp.data

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.summonapp.R
import com.example.summonapp.data.converters.AbilityScoreConverter
import com.example.summonapp.data.converters.AlignmentConverter
import com.example.summonapp.data.converters.ArmourClassConverter
import com.example.summonapp.data.converters.AttackBonusConverter
import com.example.summonapp.data.converters.DamageResistConvertor
import com.example.summonapp.data.converters.HealthConverter
import com.example.summonapp.data.converters.ListStringConverter
import com.example.summonapp.data.converters.SavingThrowsConvertor
import com.example.summonapp.data.converters.SizeConverter
import com.example.summonapp.data.converters.SpecialAbilitiesConverter
import com.example.summonapp.data.converters.SpeedConverter
import com.example.summonapp.models.enums.Alignment
import com.example.summonapp.models.enums.Size
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.reflect.Type

@Database(entities = [Monster::class, FavouriteMonster::class], version = 3, exportSchema = false)
@TypeConverters(
    ListStringConverter::class,
    AlignmentConverter::class,
    SizeConverter::class,
    SpeedConverter::class,
    HealthConverter::class,
    ArmourClassConverter::class,
    SavingThrowsConvertor::class,
    DamageResistConvertor::class,
    AbilityScoreConverter::class,
    AttackBonusConverter::class,
    SpecialAbilitiesConverter::class
)
abstract class MonsterDatabase : RoomDatabase() {

    abstract fun monsterDao(): MonsterDao
    abstract fun favouriteMonsterDao(): FavouriteMonsterDao

    companion object {
        @Volatile
        private var Instance: MonsterDatabase? = null

        fun getDatabase(context: Context): MonsterDatabase {
            // if the Instance is not null, return it, otherwise create a new database instance.
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, MonsterDatabase::class.java, "item_database")
                    /**
                     * Setting this option in your app's database builder means that Room
                     * permanently deletes all data from the tables in your database when it
                     * attempts to perform a migration with no defined migration path.
                     */
                    .fallbackToDestructiveMigration()
                    .addCallback(object : Callback() {
                        override fun onOpen(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            // Insert default monsters when DB is created
                            Thread {
                                val database = getDatabase(context)
                                val monsterDao = database.monsterDao()
                                val monsters = loadMonstersFromJson(context)
                                Log.d("DATABASE", "Loaded Monsters")
                                monsterDao.insertAll(monsters)
                            }.start()
                        }
                    })
                    .build()
                    .also { Instance = it }
            }
        }
    }
}

fun loadMonstersFromJson(context: Context): List<Monster> {
    val inputStream = context.resources.openRawResource(R.raw.bestiary)
    val reader = BufferedReader(InputStreamReader(inputStream))
    val type = object : TypeToken<List<Monster>>() {}.type
    val gson = GsonBuilder()
        .registerTypeAdapter(Size::class.java, SizeDeserializer())
        .registerTypeAdapter(Alignment::class.java, AlignmentDeserializer())
        .create()
    return gson.fromJson(reader, type)
}

class SizeDeserializer : JsonDeserializer<Size> {
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Size {
        return Size.valueOf(json.asString.uppercase())
    }
}

class AlignmentDeserializer : JsonDeserializer<Alignment> {
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Alignment {
        return Alignment.valueOf(json.asString.uppercase().replace(" ", "_"))
    }
}