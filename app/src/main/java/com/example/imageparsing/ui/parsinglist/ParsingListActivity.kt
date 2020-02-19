package com.example.imageparsing.ui.parsinglist

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.imageparsing.R
import com.example.imageparsing.model.Resource
import kotlinx.android.synthetic.main.activity_parsing_list.*

class ParsingListActivity : AppCompatActivity(),
    ParsingListAdapter.OnHeaderClickListener, ParsingListAdapter.OnItemClickListener {

    private val viewModel by lazy { ViewModelProvider(this).get(ParsingListViewModel::class.java) }
    private lateinit var parsingListAdapter: ParsingListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parsing_list)
        initAdapter()
        initLiveData()
    }

    private fun initAdapter() {
        parsingListAdapter = ParsingListAdapter(viewModel.parsingUrl)
        rvParsingList.layoutManager = LinearLayoutManager(this)
        rvParsingList.itemAnimator = DefaultItemAnimator()
        rvParsingList.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        rvParsingList.adapter = parsingListAdapter
        parsingListAdapter.setOnHeaderClickListener(this)
        parsingListAdapter.setOnItemClickListener(this)
    }

    private fun initLiveData() {
        viewModel.parsingListLiveData.observe(this, Observer {
            when (it.status) {
                Resource.Status.LOADING -> startLoading()
                Resource.Status.SUCCESS -> {
                    parsingListAdapter.setData(it.data!!)
                    completeLoading(it.data.isEmpty())
                }
                Resource.Status.ERROR -> errorLoading()
            }
        })
    }

    private fun startLoading() {
        pbParsingList.visibility = View.VISIBLE
        rvParsingList.visibility = View.GONE
        tvError.visibility = View.GONE
    }

    private fun completeLoading(isEmptyData: Boolean) {
        pbParsingList.visibility = View.GONE
        rvParsingList.visibility = View.VISIBLE
        if (isEmptyData) {
            tvError.setText(R.string.nothing_found)
            tvError.visibility = View.VISIBLE
        } else {
            tvError.visibility = View.GONE
        }
    }

    private fun errorLoading() {
        pbParsingList.visibility = View.GONE
        rvParsingList.visibility = View.VISIBLE
        tvError.setText(R.string.something_went_wrong)
        tvError.visibility = View.VISIBLE
    }

    override fun onParseButtonClicked() {
        viewModel.getParsingList()
    }

    override fun onParseTextChanged(text: String) {
        viewModel.parsingUrl = text
    }

    override fun onItemClicked(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }

}
