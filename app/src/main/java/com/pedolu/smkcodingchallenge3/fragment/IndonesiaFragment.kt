package com.pedolu.smkcodingchallenge3.fragment


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
import com.pedolu.smkcodingchallenge3.adapter.IndonesiaAdapter
import com.pedolu.smkcodingchallenge3.adapter.IndonesiaRoomAdapter
import com.pedolu.smkcodingchallenge3.data.httpClient
import com.pedolu.smkcodingchallenge3.data.kawalCoronaApiRequest
import com.pedolu.smkcodingchallenge3.data.model.indonesia.ProvinsiItem
import com.pedolu.smkcodingchallenge3.data.model.room.IndonesiaSummaryModel
import com.pedolu.smkcodingchallenge3.data.service.CovidKawalCoronaService
import com.pedolu.smkcodingchallenge3.util.dismissLoading
import com.pedolu.smkcodingchallenge3.util.showLoading
import com.pedolu.smkcodingchallenge3.util.tampilToast
import com.pedolu.smkcodingchallenge3.viewmodel.IndonesiaSummaryViewModel
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.fragment_indonesia.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class IndonesiaFragment : Fragment() {
    private val indonesiaSummaryViewModel by viewModels<IndonesiaSummaryViewModel>()

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
                if (indonesiaSummary != null) {
                    listProvinsiIndonesia.layoutManager = LinearLayoutManager(context)
                    listProvinsiIndonesia.adapter =
                        IndonesiaRoomAdapter(
                            requireContext(),
                            indonesiaSummary
                        ) {
                            val provinsi = it
                            tampilToast(requireContext(), provinsi.nama_provinsi)
                        }
                } else {
                    callIndonesiaProvinsi()
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
                                val provinsiList: MutableList<IndonesiaSummaryModel> = ArrayList()
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
                                showIndonesiaSummary(response.body()!!)
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

    private fun showIndonesiaSummary(provinsi: List<ProvinsiItem>) {
        listProvinsiIndonesia.layoutManager = LinearLayoutManager(context)
        listProvinsiIndonesia.adapter =
            IndonesiaAdapter(
                requireContext(),
                provinsi
            ) {
                val provinsi = it
                tampilToast(requireContext(), provinsi.attributes.provinsi)
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
