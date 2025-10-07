package com.meyrforge.tomabien.common.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.meyrforge.tomabien.medication_tracker.data.MedicationTrackerDao
import com.meyrforge.tomabien.medication_tracker.data.entities.MedicationTrackerEntity
import com.meyrforge.tomabien.my_medications.data.MedicationDao
import com.meyrforge.tomabien.my_medications.data.entities.AlarmEntity
import com.meyrforge.tomabien.my_medications.data.entities.MedicationEntity

@Database(
    entities = [MedicationEntity::class, AlarmEntity::class, MedicationTrackerEntity::class],
    version = 9
)
abstract class TomaBienDatabase : RoomDatabase(){
    abstract fun medicationDao(): MedicationDao
    abstract fun medicationTrackerDao(): MedicationTrackerDao
}

val MIGRATION_7_8 = object : Migration(7, 8) {
    override fun migrate(db: SupportSQLiteDatabase) {
        // Paso 1: Añadir la nueva columna 'grammage' como TEXT (para Strings)
        db.execSQL("ALTER TABLE medication_table ADD COLUMN medication_grammage TEXT NOT NULL DEFAULT ''")

        // Paso 2: Copiar los valores de la antigua columna 'dosage' a 'grammage'
        db.execSQL("UPDATE medication_table SET medication_grammage = medication_dosage")

        // Paso 3: Alterar la tabla para recrear la columna 'dosage' como REAL (Float)
        // SQLite no tiene un comando 'ALTER COLUMN' directo para cambiar el tipo,
        // por lo que la forma más segura es recrear la tabla.

        // 3.1. Renombrar la tabla original
        db.execSQL("ALTER TABLE medication_table RENAME TO medications_old")

        // 3.2. Crear la nueva tabla con la estructura correcta (copia la definición de tu Entity)
        db.execSQL("""
            CREATE TABLE medication_table (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                medication_name TEXT NOT NULL,
                medication_dosage REAL NOT NULL, 
                medication_grammage TEXT NOT NULL,
                optional INTEGER NOT NULL,
                deleted INTEGER NOT NULL
            )
        """)

        // 3.3. Copiar los datos de la tabla antigua a la nueva, asignando un valor por defecto a 'dosage'
        // Aquí decidimos que 'dosage' será 0.0f por defecto. Puedes ajustar esto.
        db.execSQL("""
            INSERT INTO medication_table (id, medication_name, medication_dosage, medication_grammage, optional, deleted)
            SELECT id, medication_name, 1.0, medication_grammage, optional, deleted
            FROM medications_old
        """)

        // 3.4. Borrar la tabla antigua
        db.execSQL("DROP TABLE medications_old")
    }
}

val MIGRATION_8_9 = object : Migration(8, 9) {
    override fun migrate(db: SupportSQLiteDatabase) {
        // Añade la nueva columna 'number_of_pills' de tipo REAL (Float en SQL)
        // y establece un valor por defecto de 0.0 para las filas existentes.
        db.execSQL("ALTER TABLE medication_table ADD COLUMN number_of_pills REAL NOT NULL DEFAULT 0.0")
    }
}