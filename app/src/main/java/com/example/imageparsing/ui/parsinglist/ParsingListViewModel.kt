package com.example.imageparsing.ui.parsinglist

import androidx.lifecycle.ViewModel
import com.example.imageparsing.api.Repository

class ParsingListViewModel : ViewModel() {

    val repository = Repository.instance
    val parsingListLiveData = repository.parsingListLiveData
    var parsingUrl: String = ""

    fun getParsingList() {
        if (!parsingUrl.startsWith("http://") && !parsingUrl.startsWith("https://"))
            repository.getParsingList("http://$parsingUrl")
        else
            repository.getParsingList(parsingUrl)
    }
}