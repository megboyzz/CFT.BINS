package ru.megboyzz.bin

import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.megboyzz.bin.entity.BINInfoNumber
import java.lang.IllegalArgumentException

class MainViewModel(application: App): AndroidViewModel(application) {

    init{

        viewModelScope.launch(Dispatchers.IO) {
            while (true) {

            }
        }
    }

    val binInfoNumberList: MutableLiveData<MutableList<BINInfoNumber>> by lazy {
        MutableLiveData<MutableList<BINInfoNumber>>()
    }



}

class MainViewModelFactory(private val application: App) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java))
            return MainViewModel(application) as T
        throw  IllegalArgumentException("Unknown ViewModel Class")
    }

}