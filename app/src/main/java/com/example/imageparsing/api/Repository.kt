package com.example.imageparsing.api

import androidx.lifecycle.MutableLiveData
import com.example.imageparsing.model.Resource
import kotlinx.coroutines.*
import org.jsoup.HttpStatusException
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class Repository private constructor() {

    companion object {
        val instance = Repository()
    }

    private val coroutineScope = CoroutineScope(Dispatchers.IO + Job())

    val parsingListLiveData = MutableLiveData<Resource<List<String>>>()

    fun getParsingList(url: String) {
        parsingListLiveData.value = Resource()
        coroutineScope.launch {
            try {
                val doc: Document = Jsoup.connect(url).get()
                withContext(Dispatchers.Main) {
                    val resultList = ArrayList<String>()
                    val pngList = doc.select("img[src$=.png]")
                    val jpgList = doc.select("img[src$=.jpg]")
                    for (element in pngList) {
                        resultList.add(element.absUrl("src"))
                    }
                    for (element in jpgList) {
                        resultList.add(element.absUrl("src"))
                    }
                    parsingListLiveData.value = Resource(Resource.Status.SUCCESS, resultList)
                }
            } catch (e: HttpStatusException) {
                parsingListLiveData.postValue(Resource(Resource.Status.ERROR, error = e))
            } catch (e: Throwable) {
                parsingListLiveData.postValue(Resource(Resource.Status.ERROR, error = e))
            }
        }
    }

}