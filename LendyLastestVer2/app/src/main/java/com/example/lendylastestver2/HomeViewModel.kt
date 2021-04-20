package com.example.lendylastestver2

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*

class HomeViewModel : ViewModel() {
    var books = MutableLiveData<List<Book>>()

    private val viewModelJob = SupervisorJob()
    private val mainScope  = CoroutineScope(Dispatchers.Main + viewModelJob)

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun retrieveBooks() {
         mainScope.launch(Dispatchers.IO) {
             delay(1000)
             val list = listOf(
                     Book("THE GUILD", "R.K NARAYAN"),
                     Book("MALGUDI DAYS", "R.K NARAYAN"),
                     Book("THE PRIVATE LIFE OF AN INDIAN PRINCE", "MULK RAJ ANAND"),
                     Book("TRAIN TO PAKISTAN", "KHUSHWANT SINGH"),
                     Book("GODAN BY MUNSHI PREMCHAND, TRANSLATED", "JAI RATAN")
             )

             withContext(Dispatchers.Main) {
                 books.value = list
             }
         }
    }
}