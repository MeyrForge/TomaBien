package com.meyrforge.tomabien.weekly_summary.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.meyrforge.tomabien.medication_tracker.domain.models.MedicationTracker
import com.meyrforge.tomabien.medication_tracker.domain.usecases.GetAllMedicationTrackerUseCase
import com.meyrforge.tomabien.weekly_summary.domain.models.TrackerWithMedicationData
import com.meyrforge.tomabien.weekly_summary.domain.usecases.GetMedicationByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeeklySummaryViewModel @Inject constructor(
    private val getAllMedicationTrackerUseCase: GetAllMedicationTrackerUseCase,
    private val getMedicationByIdUseCase: GetMedicationByIdUseCase
) : ViewModel() {

    private val _medicationTrackerList = MutableLiveData(emptyList<MedicationTracker>())
    val medicationTrackerList: LiveData<List<MedicationTracker>?> = _medicationTrackerList

    private val _trackerWithMedicationList = MutableLiveData(emptyList<TrackerWithMedicationData>())
    val trackerWithMedicationList: LiveData<List<TrackerWithMedicationData>?> = _trackerWithMedicationList

    init {
        getAllTrackersWithMedications()
    }

    fun getAllTrackersWithMedications() {
        viewModelScope.launch {
            _medicationTrackerList.value = getAllMedicationTrackerUseCase()
            val newTrackerWithMedDataList = mutableListOf<TrackerWithMedicationData>()
            _medicationTrackerList.value?.let {
                for(tracker in it){
                    val medication = getMedicationByIdUseCase(tracker.medicationId)
                    medication?.let { med->
                        newTrackerWithMedDataList.add(TrackerWithMedicationData(tracker = tracker, medication = med))
                    }
                }
            }
            _trackerWithMedicationList.value = newTrackerWithMedDataList
        }
    }
}