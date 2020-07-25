package com.pedolu.smkcodingchallenge3team.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.pedolu.smkcodingchallenge3team.R
import com.pedolu.smkcodingchallenge3team.adapter.IndonesiaAdapter
import com.pedolu.smkcodingchallenge3team.data.httpClient
import com.pedolu.smkcodingchallenge3team.data.kawalCoronaApiRequest
import com.pedolu.smkcodingchallenge3team.data.model.indonesia.ProvinsiItem
import com.pedolu.smkcodingchallenge3team.data.model.room.IndonesiaSummaryModel
import com.pedolu.smkcodingchallenge3team.data.service.CovidKawalCoronaService
import com.pedolu.smkcodingchallenge3team.util.dismissLoading
import com.pedolu.smkcodingchallenge3team.util.showLoading
import com.pedolu.smkcodingchallenge3team.util.tampilToast
import com.pedolu.smkcodingchallenge3team.viewmodel.IndonesiaSummaryViewModel
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.fragment_indonesia.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class IndonesiaFragment : Fragment() {
    private val indonesiaSummaryViewModel by viewModels<IndonesiaSummaryViewModel>()
    private var provinsiList: MutableList<IndonesiaSummaryModel> = ArrayList()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_indonesia, container, false)
    }

    override fun onViewCreated(view: View, @Nullable savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        retriveRoomIndonesiaSummary()
        swipeRefreshLayout.post { callIndonesiaProvinsi() }
        swipeRefreshLayout.setOnRefreshListener {
            callIndonesiaProvinsi()
        }
    }

    private fun retriveRoomIndonesiaSummary() {
        indonesiaSummaryViewModel.init(requireContext())
        indonesiaSummaryViewModel.indonesiaSummary.observe(
            viewLifecycleOwner,
            Observer { indonesiaSummary ->
                if (indonesiaSummary.isEmpty()) {
                    callIndonesiaProvinsi()
                } else {
                    showIndonesiaSummary(indonesiaSummary)
                }
            })
    }


    private fun callIndonesiaProvinsi() {
        setInvisible()
        showLoading(requireContext(), swipeRefreshLayout)
        val httpClient = httpClient()
        val apiRequest = kawalCoronaApiRequest<CovidKawalCoronaService>(httpClient)
        val call = apiRequest.getProvinsi()
        call.enqueue(object : Callback<List<ProvinsiItem>> {
            override fun onFailure(call: Call<List<ProvinsiItem>>, t: Throwable) {
                dismissLoading(swipeRefreshLayout)
                setVisible()
            }

            override fun onResponse(
                call: Call<List<ProvinsiItem>>, response:
                Response<List<ProvinsiItem>>
            ) {
                setVisible()
                dismissLoading(swipeRefreshLayout)
                when {
                    response.isSuccessful -> {
                        when {
                            response.body()?.size != 0 -> {
                                indonesiaSummaryViewModel.init(requireContext())

                                for (provinsi in response.body()!!) {
                                    provinsiList.add(
                                        IndonesiaSummaryModel(
                                            provinsi.attributes.kasusPosi.toString(),
                                            provinsi.attributes.kasusSemb.toString(),
                                            provinsi.attributes.kasusMeni.toString(),
                                            provinsi.attributes.provinsi
                                        )
                                    )
                                }
                                indonesiaSummaryViewModel.addAllData(provinsiList)
                                showIndonesiaSummary(provinsiList)
                            }
                            else -> {
                                tampilToast(requireContext(), "Berhasil")
                            }
                        }
                    }
                }
            }
        })
    }

    private fun showIndonesiaSummary(provinsiList: List<IndonesiaSummaryModel>) {
        listProvinsiIndonesia.layoutManager = LinearLayoutManager(context)
        listProvinsiIndonesia.adapter =
            IndonesiaAdapter(
                requireContext(),
                provinsiList
            ) {
                val provinsi = it
                tampilToast(requireContext(), provinsi.nama_provinsi)
            }
    }

    private fun setVisible() {
        txtIndonesia.visibility = View.VISIBLE
        listProvinsiIndonesia.visibility = View.VISIBLE
        progressBar.visibility = View.GONE
    }

    private fun setInvisible() {
        txtIndonesia.visibility = View.GONE
        listProvinsiIndonesia.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
    }

    override fun onDestroy() {
        super.onDestroy()
        this.clearFindViewByIdCache()
    }
}
