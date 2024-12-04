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
    version = 7,
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
                    .fallbackToDestructiveMigration() //Esta línea elimina la base de datos si se actualiza la versión.
                    .addCallback(object : RoomDatabase.Callback() { //Esta línea crea un callback que se ejecuta al crear la base de datos.
                        override fun onOpen(db: SupportSQLiteDatabase) { //Esta línea se ejecuta al abrir la base de datos.
                            super.onOpen(db)
                            db.execSQL("""  
                                CREATE TABLE IF NOT EXISTS recomendacion (
                                    id INTEGER PRIMARY KEY,
                                    consejos Varchar(255)
                                );
                            """)
                            db.execSQL("""
                                INSERT OR REPLACE INTO recomendacion (id, consejos) VALUES
                                (1, "Si el cielo está muy nuboso, es buen momento para realizar actividades bajo techo."),
                                (2, "En un día muy nuboso, lleva una chaqueta ligera para mantenerte cómodo."),
                                (3, "Cuando el día está muy nuboso, es recomendable llevar un paraguas por si acaso."),
                                (4, "Si el cielo está cubierto, es buena idea llevar una bufanda o sombrero para mayor comodidad."),
                                (5, "En un día cubierto, puedes disfrutar de actividades al aire libre sin preocuparte por el sol."),
                                (6, "Cuando está cubierto, el clima es ideal para hacer ejercicio sin los efectos directos del sol."),
                                (7, "Si el día está despejado, recuerda usar protector solar para proteger tu piel de la radiación UV."),
                                (8, "En un día despejado, las gafas de sol son esenciales para proteger tus ojos del sol."),
                                (9, "Si el cielo está despejado, es un buen momento para hacer actividades al aire libre."),
                                (10, "Con algunas nubes en el cielo, disfruta de actividades fuera de casa sin el calor intenso del sol."),
                                (11, "En un día con nubes, puedes caminar sin la preocupación de quemaduras solares."),
                                (12, "Si hay algunas nubes, es el momento perfecto para un paseo tranquilo sin el calor abrumador."),
                                (13, "Si está lloviendo, lleva un paraguas para evitar mojarte."),
                                (14, "Si llueve mucho, usa un impermeable para mantenerte seco."),
                                (15, "En un día lluvioso, evita caminar por lugares con encharcamientos."),
                                (16, "Cuando hace sol, recuerda beber agua con frecuencia para mantenerte hidratado."),
                                (17, "En un día soleado, asegúrate de aplicar protector solar varias veces durante el día."),
                                (18, "Si está muy soleado, usa sombrero o gafas de sol para proteger tu rostro y ojos."),
                                (19, "Si hace mucho calor, hidrátate constantemente para evitar golpes de calor."),
                                (20, "Recuerda llevar una botella de agua cuando hace calor para mantenerte fresco."),
                                (21, "En un día caluroso, busca sombra siempre que puedas para evitar el golpe de calor."),
                                (22, "Mantén una actitud positiva ante cualquier cambio en el clima y disfruta de lo que te rodea."),
                                (23, "Recuerda hacer pausas cuando realices actividades al aire libre, sobre todo si hace calor o frío."),
                                (24, "No olvides llevar tu teléfono móvil cargado, siempre es bueno estar preparado."),
                                (25, "Lleva siempre una chaqueta ligera o suéter, ya que el clima puede cambiar rápidamente."),
                                (26, "Ten a mano un pequeño botiquín con lo esencial, nunca sabes cuándo lo podrías necesitar."),
                                (27, "No olvides revisar el pronóstico del clima antes de salir, te ayudará a estar preparado."),
                                (28, "Si vas a realizar actividades al aire libre, asegúrate de llevar protector solar y agua."),
                                (29, "Recuerda siempre estar consciente de tu entorno y de las condiciones del clima para tu seguridad.");
                                """)
                        }
                    }).build()
                INSTANCE = instance //Esta línea almacena la instancia de la base de datos en la variable INSTANCE.
                instance //Esta línea devuelve la instancia de la base de datos.
            }
        }
    }
}