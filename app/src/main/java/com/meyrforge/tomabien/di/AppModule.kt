package com.meyrforge.tomabien.di

import android.content.Context
import androidx.room.Room
import com.meyrforge.tomabien.common.data.TomaBienDatabase
import com.meyrforge.tomabien.medication_tracker.data.MedicationTrackerDao
import com.meyrforge.tomabien.medication_tracker.data.MedicationTrackerRepositoryImpl
import com.meyrforge.tomabien.medication_tracker.domain.MedicationTrackerRepository
import com.meyrforge.tomabien.my_medications.data.MedicationDao
import com.meyrforge.tomabien.my_medications.data.MedicationRepositoryImpl
import com.meyrforge.tomabien.my_medications.domain.MedicationRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private const val DATABASE_NAME = "tomabien_database"

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, TomaBienDatabase::class.java, DATABASE_NAME)
            .fallbackToDestructiveMigration(false)
            .build()

    @Provides
    @Singleton
    fun provideMedicationDao(database: TomaBienDatabase) = database.medicationDao()

    @Provides
    fun provideMedicationRepository(medicationDao: MedicationDao): MedicationRepository {
        return MedicationRepositoryImpl(medicationDao)
    }
    @Provides
    @Singleton
    fun provideMedicationTrackerDao(database: TomaBienDatabase) = database.medicationTrackerDao()

    @Provides
    fun provideMedicationTrackerRepository(medicationTrackerDao: MedicationTrackerDao): MedicationTrackerRepository {
        return MedicationTrackerRepositoryImpl(medicationTrackerDao)
    }


}