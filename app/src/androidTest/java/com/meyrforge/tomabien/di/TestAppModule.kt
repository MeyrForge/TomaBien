package com.meyrforge.tomabien.di

import android.app.AlarmManager
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
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import org.mockito.Mockito.mock
import javax.inject.Singleton


@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [AppModule::class]
)
object TestAppModule {

    @Provides
    @Singleton
    fun provideAlarmManager(): AlarmManager {
        return mock()
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context) =
        Room.inMemoryDatabaseBuilder(context, TomaBienDatabase::class.java)
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