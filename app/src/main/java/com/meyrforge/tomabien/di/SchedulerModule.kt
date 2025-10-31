package com.meyrforge.tomabien.di

import com.meyrforge.tomabien.common.alarm.AndroidAlarmScheduler
import com.meyrforge.tomabien.my_medications.domain.AlarmScheduler
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SchedulerModule {

    @Binds
    @Singleton
    abstract fun bindAlarmScheduler(
        androidAlarmScheduler: AndroidAlarmScheduler
    ): AlarmScheduler
}