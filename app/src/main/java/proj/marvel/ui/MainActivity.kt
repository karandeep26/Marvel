package proj.marvel.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import proj.marvel.R
import proj.marvel.service.model.MarvelResponse
import proj.marvel.service.model.ResponseWrapper
import proj.marvel.utils.gone
import proj.marvel.utils.toast
import proj.marvel.utils.visible
import proj.marvel.viewmodel.MarvelViewModel

class MainActivity : AppCompatActivity() {

    private val items = mutableListOf<MarvelResponse.Data.Result>()
    private val mAdapter = ListAdapter(items)
    private lateinit var viewModel: MarvelViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)

        setRecyclerView()

        viewModel = ViewModelProviders.of(this).get(MarvelViewModel::class.java)

        viewModel.listObservable.observe(this, getObserver())


        list.setOnMoreListener { _, _, _ ->
            viewModel.fetchData()
        }

        empty_layout.setOnClickListener {
            onTryAgain()
        }

    }

    private fun setRecyclerView() {

        mAdapter.setHasStableIds(true)
        list.run {
            setLayoutManager(LinearLayoutManager(this@MainActivity))
            this.adapter = mAdapter
        }


        list.showProgress()

    }

    private fun getObserver(): Observer<ResponseWrapper> {
        return Observer {
            list.hideProgress()
            list.showRecycler()
            if (it.exception == null) {
                if (it.data != null) {
                    onSuccess(it.data, it.allContentLoaded)
                }
            } else {
                onException(it.exception)
            }
        }
    }

    private fun onSuccess(
        dataList: MutableList<MarvelResponse.Data.Result>, allContentLoaded: Boolean
    ) {
        mAdapter.items.addAll(dataList)
        mAdapter.notifyItemRangeInserted(mAdapter.items.size - dataList.size, dataList.size)
        if (allContentLoaded) {
            list.removeMoreListener()
        }
    }

    private fun onException(exception: Exception) {
        if (mAdapter.items.isEmpty()) {
            empty_layout.visible()
        } else {
            list.isLoadingMore = false
            toast(exception.localizedMessage)
        }
    }

    private fun onTryAgain() {
        empty_layout.gone()
        list.showProgress()
        viewModel.fetchData()
    }
}
