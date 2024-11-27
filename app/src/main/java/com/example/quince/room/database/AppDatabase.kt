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
    version = 1,
    exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun daoProvincia(): DaoProvincia
    abstract fun daoRecomendacion(): DaoRecomendacion
    abstract fun daoTiempo(): DaoTiempo

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    "app_database"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(object : RoomDatabase.Callback() {
                        override fun onOpen(db: SupportSQLiteDatabase) {
                            super.onOpen(db)
                            db.execSQL("""  
                                CREATE TABLE IF NOT EXISTS recomendacion (
                                    id INTEGER PRIMARY KEY,
                                    consejos Varchar(255)
                                );
                            """)
                            db.execSQL("""
                                INSERT OR REPLACE INTO recomendacion (id, consejos) VALUES
                                (1, "Si está lloviendo, lleva un paraguas para evitar mojarte."),
                                (2, "Si llueve mucho, usa un impermeable para mantenerte seco."),
                                (3, "En un día lluvioso, evita caminar por lugares con encharcamientos."),
                                (4, "Si hace sol, usa protector solar para proteger tu piel."),
                                (5, "Recuerda llevar gafas de sol para proteger tus ojos de la radiación UV."),
                                (6, "Si hace mucho calor, hidrátate constantemente para evitar golpes de calor."),
                                (7, "Si el cielo está nublado, es buen momento para hacer actividades al aire libre."),
                                (8, "En un día nublado, puede ser buena idea llevar una chaqueta ligera."),
                                (9, "Si está nublado y fresco, puedes aprovechar para hacer ejercicio sin el calor del sol."),
                                (10, "Si hace viento, ten cuidado con los objetos que puedan volar y causar accidentes."),
                                (11, "En un día ventoso, es recomendable abrigarse un poco más para mantener el calor."),
                                (12, "Si hay viento fuerte, evita caminar cerca de árboles o estructuras inestables."),
                                (13, "Si hay nieve, asegúrate de usar ropa adecuada para el frío y evitar resbalones."),
                                (14, "En un día con nieve, mantén los pies secos para evitar problemas de congelación."),
                                (15, "Si está nevando, revisa las condiciones de tránsito antes de salir para evitar accidentes."),
                                (16, "Cuando haga calor, busca sombra siempre que puedas."),
                                (17, "En un día caluroso, usa ropa ligera y fresca para mantenerte cómodo."),
                                (18, "Recuerda beber mucha agua si el día está caluroso para mantener tu cuerpo hidratado."),
                                (19, "Si el cielo está despejado y hace calor, asegúrate de descansar y evitar la sobreexposición."),
                                (20, "Cuando el clima es lluvioso, elige actividades bajo techo como leer o ver películas."),
                                (21, "Si hay tormenta eléctrica, es mejor quedarse dentro de casa para evitar peligros."),
                                (22, "Durante una tormenta eléctrica, evita usar dispositivos electrónicos conectados a la corriente.");
                                """)
                        }
                    }).build()
                INSTANCE = instance
                instance
            }
        }
    }



}