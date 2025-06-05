package com.example.quince.room.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.quince.room.daos.DaoProvincia
import com.example.quince.room.daos.DaoRecomendacion
import com.example.quince.room.daos.DaoTiempo
import com.example.quince.room.dataclasses.Provincia
import com.example.quince.room.dataclasses.Recomendacion
import com.example.quince.room.dataclasses.Tiempo

@Database(
    entities = [Provincia::class, Recomendacion::class, Tiempo::class],
    version = 8,
    exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    // Aquí van las instancias de los DAO que permiten interactuar con las tablas de la base de datos.
    abstract fun daoProvincia(): DaoProvincia
    abstract fun daoRecomendacion(): DaoRecomendacion
    abstract fun daoTiempo(): DaoTiempo

    //Aquí se crea la base de datos, que permite que se pueda acceder a ella.
    /*
    * companion object significa que es un objeto que pertenece a la clase
    * y no a la instancia de la clase, eso significa que solo hay una instancia
    * de la base de datos en la aplicación aunque haya varias instancias de la clase.
    * */
    companion object {
        @Volatile //Esta anotación asegura que los cambios en la instancia se visualicen de forma inmediata a otros hilos.
        private var INSTANCE: AppDatabase? = null //Esta variable almacena la instancia de la base de datos.

        fun getDatabase(context: Context): AppDatabase { //Esta función devuelve la instancia de la base de datos.
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    "app_database"
                )
                    .fallbackToDestructiveMigration() //Esto elimina la base de datos si se actualiza la versión.
                    .addCallback(object : RoomDatabase.Callback() { //Esta línea crea un callback que se ejecuta al crear la base de datos.
                        override fun onOpen(db: SupportSQLiteDatabase) { //Esta línea se ejecuta al abrir la base de datos.
                            super.onOpen(db)
                            db.execSQL(crearTabla)
                            db.execSQL(recomendaciones)
                        }
                    }).build()
                INSTANCE = instance //Esta línea almacena la instancia de la base de datos en la variable INSTANCE.
                instance //Esta línea devuelve la instancia de la base de datos.
            }
        }
    }
}