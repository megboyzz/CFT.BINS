package ru.megboyzz.bin

import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.*
import androidx.room.OnConflictStrategy
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import ru.megboyzz.bin.entity.BINInfo
import ru.megboyzz.bin.entity.BINInfoNumber
import ru.megboyzz.bin.services.APIService
import java.lang.IllegalArgumentException
import kotlin.concurrent.thread

class MainViewModel(application: App): AndroidViewModel(application) {

    val binInfoNumberList: MutableLiveData<MutableList<BINInfoNumber>> by lazy {
        MutableLiveData<MutableList<BINInfoNumber>>(mutableListOf())
    }

    val isLoadingInfo: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>(false) }

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://lookup.binlist.net/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val binService = retrofit.create(APIService::class.java)

    private val dao = App.instance?.database?.binInfoDao()

    init{
        viewModelScope.launch(Dispatchers.IO) { updateAll() }
    }

    private fun updateAll(){
        val all = dao?.getAll()
        if(all == null || all.isEmpty()){
            binInfoNumberList.postValue(mutableListOf())
            return
        }
        binInfoNumberList.postValue(all.toMutableList())
    }

    fun addBinInfo(number: Int){
        isLoadingInfo.postValue(true)
        viewModelScope.launch(Dispatchers.IO) {
            val response = binService.getData(number).execute()
            if(response.isSuccessful) {
                val newInfo = response.body()
                val infoNumber = BINInfoNumber(number = number, info = newInfo!!)
                dao?.addBinInfo(infoNumber)
                updateAll()
            }else{
                val application = getApplication<App>()
                val string = application.applicationContext.getString(R.string.title_no_info)
                withContext(Dispatchers.Main) {
                    Toast.makeText(getApplication(), string + number, Toast.LENGTH_LONG).show()
                }
            }
            isLoadingInfo.postValue(false)
        }
    }

    fun deleteInfo(number: BINInfoNumber){
        viewModelScope.launch(Dispatchers.IO) {
            dao?.removeBinInfo(number)
            updateAll()
        }
    }



}

class MainViewModelFactory(private val application: App) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java))
            return MainViewModel(application) as T
        throw  IllegalArgumentException("Unknown ViewModel Class")
    }

}