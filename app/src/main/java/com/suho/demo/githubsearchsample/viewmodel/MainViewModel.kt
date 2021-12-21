package com.suho.demo.githubsearchsample.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.suho.demo.githubsearchsample.data.common.Resource
import com.suho.demo.githubsearchsample.data.common.Status
import com.suho.demo.githubsearchsample.data.model.GithubRepo
import com.suho.demo.githubsearchsample.data.repository.SearchRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/*
data layer에서는 flow로 데이터를 담고 수집 했다면
viewModel에서는 lifecycle aware한 livedata로 리소스를 view에서 observe 가능하게 함.
 */
class MainViewModel : ViewModel() {

    private val _searchResult: MutableLiveData<Resource<List<GithubRepo>>> by lazy {
        MutableLiveData<Resource<List<GithubRepo>>>()
    }
    val searchResult: LiveData<Resource<List<GithubRepo>>>
        get() = _searchResult

    private val _reposData: MutableLiveData<List<GithubRepo>> by lazy {
        MutableLiveData<List<GithubRepo>>()
    }
    val reposData: LiveData<List<GithubRepo>>
        get() = _reposData

    fun searchRepositories(query: String) {
        viewModelScope.launch {
            SearchRepository.getSearchResult(query).collect {
                _searchResult.postValue(it)
                if (it.status == Status.SUCCESS) {
                    _reposData.postValue(it.data)
                }
            }
        }
    }
}