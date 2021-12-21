package com.suho.demo.githubsearchsample.view

import android.os.Bundle
import android.util.Log
import android.view.View
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
LOADING -> 프로그래스 바 표시
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

        binding.btnSearch.setOnClickListener {
            viewModel.searchRepositories(binding.etQuery.text.toString())
        }

        binding.rcvRepos.apply {
            mainAdapter = MainAdapter()
            layoutManager = LinearLayoutManager(this@MainActivity)
            setHasFixedSize(true)
            adapter = mainAdapter
        }

        viewModel.searchResult.observe(this) {
            when (it.status) {
                Status.LOADING -> {
                    binding.progress.visibility = View.VISIBLE
                }
                Status.SUCCESS -> {
                    binding.progress.visibility = View.GONE
                    Log.d(TAG, it.data.toString())
                }
                Status.ERROR -> {
                    binding.progress.visibility = View.GONE
                    Snackbar.make(binding.root, "에러 발생", LENGTH_SHORT).show()
                }
            }
        }
    }

    companion object {
        private const val TAG = "Main Activity"
    }
}