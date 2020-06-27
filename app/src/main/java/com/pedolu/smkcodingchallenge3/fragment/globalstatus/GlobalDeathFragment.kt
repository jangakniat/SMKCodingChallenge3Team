package com.pedolu.smkcodingchallenge3.fragment.globalstatus

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.pedolu.smkcodingchallenge3.R
import com.pedolu.smkcodingchallenge3.adapter.GlobalStatusAdapter
import com.pedolu.smkcodingchallenge3.adapter.StatusRoomAdapter
import com.pedolu.smkcodingchallenge3.data.httpClient
import com.pedolu.smkcodingchallenge3.data.mathdroidApiRequest
import com.pedolu.smkcodingchallenge3.data.model.global.GlobalStatusSummaryItem
import com.pedolu.smkcodingchallenge3.data.model.room.StatusSummaryModel
import com.pedolu.smkcodingchallenge3.data.service.CovidMathdroidService
import com.pedolu.smkcodingchallenge3.util.dismissLoading
import com.pedolu.smkcodingchallenge3.util.showLoading
import com.pedolu.smkcodingchallenge3.util.tampilToast
import com.pedolu.smkcodingchallenge3.viewmodel.StatusSummaryViewModel
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.fragment_global_status_summary.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class GlobalDeathFragment : Fragment() {
    private val statusSummaryViewModel by viewModels<StatusSummaryViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_global_status_summary, container, false)
    }

    override fun onViewCreated(view: View, @Nullable savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        retriveRoomStatusSummary()

        swipeRefreshLayout.setOnRefreshListener {
            callGlobalDeathSummary()
        }

    }

    private fun retriveRoomStatusSummary() {
        statusSummaryViewModel.init(requireContext(), "death")
        statusSummaryViewModel.statusSummary.observe(
            viewLifecycleOwner,
            Observer { statusSummary ->
                if (statusSummary != null) {
                    listGlobalStatus.layoutManager = LinearLayoutManager(context)
                    listGlobalStatus.adapter =
                        StatusRoomAdapter(
                            requireContext(),
                            statusSummary,
                            "death"
                        ) {
                            val country = it
                            tampilToast(requireContext(), country.combined_key)
                        }
                } else {
                    callGlobalDeathSummary()
                }
            })
    }

    private fun callGlobalDeathSummary() {
        showLoading(requireContext(), swipeRefreshLayout)
        setInvisible()
        val httpClient = httpClient()
        val apiRequest = mathdroidApiRequest<CovidMathdroidService>(httpClient)
        val call = apiRequest.getGlobalDeaths()
        call.enqueue(object : Callback<List<GlobalStatusSummaryItem>> {
            override fun onFailure(call: Call<List<GlobalStatusSummaryItem>>, t: Throwable) {
                tampilToast(requireContext(), "Gagal")
                dismissLoading(swipeRefreshLayout)
                setVisible()
            }

            override fun onResponse(
                call: Call<List<GlobalStatusSummaryItem>>, response:
                Response<List<GlobalStatusSummaryItem>>
            ) {
                setVisible()
                dismissLoading(swipeRefreshLayout)
                when {
                    response.isSuccessful -> {
                        when {
                            response.body()?.size != 0 -> {
                                statusSummaryViewModel.init(requireContext(), "death")
                                val statusList: MutableList<StatusSummaryModel> = ArrayList()
                                for (status in response.body()!!) {
                                    statusList.add(
                                        StatusSummaryModel(
                                            status.deaths, "death", status.combinedKey
                                        )
                                    )
                                }
                                statusSummaryViewModel.addAllData(statusList)
                                showGlobalDeathSummary(response.body()!!)
                            }
                            else -> {
                                tampilToast(requireContext(), "Berhasil")
                            }
                        }
                    }
                    else -> {
                        tampilToast(requireContext(), "Coba Lagi")
                    }
                }
            }
        })
    }

    private fun showGlobalDeathSummary(globalStatus: List<GlobalStatusSummaryItem>) {
        listGlobalStatus.layoutManager = LinearLayoutManager(context)
        listGlobalStatus.adapter =
            GlobalStatusAdapter(
                requireContext(),
                globalStatus,
                "death"
            ) {
                val country = it
                tampilToast(requireContext(), country.combinedKey)
            }
    }

    private fun setVisible() {
        listGlobalStatus.visibility = View.VISIBLE
        progressBar.visibility = View.GONE
    }

    private fun setInvisible() {
        listGlobalStatus.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
    }

    override fun onDestroy() {
        super.onDestroy()
        this.clearFindViewByIdCache()
    }
}
