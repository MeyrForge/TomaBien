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
    version = 13
)
abstract class TomaBienDatabase : RoomDatabase() {
    abstract fun medicationDao(): MedicationDao
    abstract fun medicationTrackerDao(): MedicationTrackerDao
}

val MIGRATION_7_8 = object : Migration(7, 8) {
    override fun migrate(db: SupportSQLiteDatabase) {
        // Renombrar la tabla original
        db.execSQL("ALTER TABLE medication_table RENAME TO medications_old")

        // Crear la nueva tabla con la estructura que debería tener en la versión 8
        // (incluyendo el cambio de tipo de dosage y el grammage)
        db.execSQL(
            """
            CREATE TABLE medication_table (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                medication_name TEXT NOT NULL,
                medication_dosage REAL NOT NULL, 
                medication_grammage TEXT NOT NULL,
                optional INTEGER NOT NULL,
                deleted INTEGER NOT NULL
            )
        """
        )

        // Copiar los datos, moviendo 'dosage' a 'grammage' y poniendo un valor por defecto en el nuevo 'dosage'
        db.execSQL(
            """
            INSERT INTO medication_table (id, medication_name, medication_dosage, medication_grammage, optional, deleted)
            SELECT id, medication_name, 1.0, medication_dosage, optional, deleted
            FROM medications_old
        """
        )

        // Borrar la tabla antigua
        db.execSQL("DROP TABLE medications_old")
    }
}

// --- MIGRACIÓN 8 -> 9 CORREGIDA ---
// Esta migración añade la columna number_of_pills con el valor por defecto 0.0
val MIGRATION_8_9 = object : Migration(8, 9) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE medication_table ADD COLUMN number_of_pills REAL NOT NULL DEFAULT 0.0")
    }
}

// --- MIGRACIÓN 9 -> 10 CORREGIDA ---
// Esta migración solo actualiza los valores existentes de 0.0 a -1.0
// La definición de la columna ya existe, solo cambiamos los datos.
val MIGRATION_9_10 = object : Migration(9, 10) {
    override fun migrate(db: SupportSQLiteDatabase) {
        // Actualiza las filas existentes que tienen el antiguo valor por defecto (0.0)
        // al nuevo valor por defecto (-1.0).
        db.execSQL("UPDATE medication_table SET number_of_pills = -1.0 WHERE number_of_pills = 0.0")
    }
}

val MIGRATION_10_11 = object : Migration(10, 11) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE medication_table ADD COLUMN count_activated INTEGER NOT NULL DEFAULT 0")
    }
}

val MIGRATION_11_12 = object : Migration(11, 12) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE alarm_table ADD COLUMN medication_dosage REAL NOT NULL DEFAULT 1.0")
        db.execSQL(
            """
            CREATE TABLE medication_table_new (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                medication_name TEXT NOT NULL,
                medication_grammage TEXT NOT NULL,
                optional INTEGER NOT NULL,
                deleted INTEGER NOT NULL,
                number_of_pills REAL NOT NULL DEFAULT -1.0,
                count_activated INTEGER NOT NULL
            )
        """
        )

        db.execSQL(
            """
            INSERT INTO medication_table_new (id, medication_name, medication_grammage, optional, deleted, number_of_pills, count_activated)
            SELECT id, medication_name, medication_grammage, optional, deleted, number_of_pills, count_activated FROM medication_table
        """
        )

        db.execSQL("DROP TABLE medication_table")

        db.execSQL("ALTER TABLE medication_table_new RENAME TO medication_table")

    }
}

val MIGRATION_12_13 = object : Migration(12, 13) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE medication_tracker_table ADD COLUMN number_of_pills REAL NOT NULL DEFAULT 0.0")
        db.execSQL("ALTER TABLE medication_tracker_table ADD COLUMN last_time_was_extraction INTEGER NOT NULL DEFAULT 0")
    }
}