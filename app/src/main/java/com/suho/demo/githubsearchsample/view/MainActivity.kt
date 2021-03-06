package com.suho.demo.githubsearchsample.view

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_SHORT
import com.google.android.material.snackbar.Snackbar
import com.suho.demo.githubsearchsample.data.common.Status
import com.suho.demo.githubsearchsample.databinding.ActivityMainBinding
import com.suho.demo.githubsearchsample.viewmodel.MainViewModel

/*
리소스의 상태에 따라
LOADING -> 프로그래스 바 표시 (바인딩어댑터에서 처리)
SUCCESS -> UI 갱신 (리사이클러뷰 데이터 바인딩으로 처리, CustomBindingAdapter 참고)
ERROR -> 스낵바로 에러 표시
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    private lateinit var mainAdapter: MainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        setContentView(binding.root)

        initRecyclerView()
        setObserver()
    }

    private fun initRecyclerView() {
        binding.rcvRepos.apply {
            mainAdapter = MainAdapter()
            layoutManager = LinearLayoutManager(this@MainActivity)
            setHasFixedSize(true)
            adapter = mainAdapter
        }
    }

    private fun setObserver() {
        viewModel.searchResult.observe(this) {
            if (it.status == Status.ERROR) {
                Snackbar.make(binding.root, "에러 발생", LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        private const val TAG = "Main Activity"
    }
}