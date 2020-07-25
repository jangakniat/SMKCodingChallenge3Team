package com.pedolu.smkcodingchallenge3team.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.pedolu.smkcodingchallenge3team.R
import com.pedolu.smkcodingchallenge3team.adapter.CountrySummaryAdapter
import com.pedolu.smkcodingchallenge3team.data.httpClient
import com.pedolu.smkcodingchallenge3team.data.mathdroidApiRequest
import com.pedolu.smkcodingchallenge3team.data.model.local.Countries
import com.pedolu.smkcodingchallenge3team.data.model.local.CountrySummary
import com.pedolu.smkcodingchallenge3team.data.model.room.CountriesModel
import com.pedolu.smkcodingchallenge3team.data.model.room.CountrySummaryModel
import com.pedolu.smkcodingchallenge3team.data.service.CovidMathdroidService
import com.pedolu.smkcodingchallenge3team.util.dismissLoading
import com.pedolu.smkcodingchallenge3team.util.showLoading
import com.pedolu.smkcodingchallenge3team.util.tampilToast
import com.pedolu.smkcodingchallenge3team.viewmodel.CountriesViewModel
import com.pedolu.smkcodingchallenge3team.viewmodel.CountrySummaryViewModel
import kotlinx.android.synthetic.main.fragment_country_summary.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CountrySummaryFragment : Fragment() {
    lateinit var adapter: CountrySummaryAdapter
    private var countryList: MutableList<CountrySummaryModel> = ArrayList()
    private var countriesList: MutableList<CountriesModel> = ArrayList()
    private val countrySummaryViewModel by viewModels<CountrySummaryViewModel>()
    private val countriesViewModel by viewModels<CountriesViewModel>()
    private val httpClient = httpClient()
    private lateinit var mathdroidApiRequest: CovidMathdroidService
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_country_summary, container, false)
    }

    @SuppressLint("ResourceAsColor")
    override fun onViewCreated(view: View, @Nullable savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        countrySearch.findViewById<ImageView>(R.id.search_mag_icon)
            .setColorFilter(R.color.colorBlack)
        countrySearch.findViewById<ImageView>(R.id.search_close_btn)
            .setColorFilter(R.color.colorBlack)
        countrySearch.findViewById<TextView>(R.id.search_src_text).setTextColor(
            R.color.colorBlack
        )
        countrySearch.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                countryRecyclerView.visibility = View.GONE
                adapter.filter.filter(newText)
                countryRecyclerView.visibility = View.VISIBLE
                return false
            }
        })

        countriesViewModel.init(requireContext())
        countrySummaryViewModel.init(requireContext())
        countryRecyclerView.layoutManager = LinearLayoutManager(countryRecyclerView.context)
        countryRecyclerView.isNestedScrollingEnabled = false
        retrieveRoomCountries()
        swipeRefreshLayout.setOnRefreshListener {
            progressBar.visibility = View.VISIBLE
            countryRecyclerView.visibility = View.GONE
            callCountries()
        }
    }

    private fun retrieveRoomCountries() {
        progressBar.visibility = View.VISIBLE
        countryRecyclerView.visibility = View.GONE
        countriesViewModel.countries.observe(viewLifecycleOwner, Observer { countries ->
            if (countries.isEmpty()) {
                callCountries()
            } else {
                retrieveRoomCountrySummary(countries)
            }
        })

    }

    private fun retrieveRoomCountrySummary(countries: List<CountriesModel>) {
        countrySummaryViewModel.countrySummary.observe(
            viewLifecycleOwner,
            Observer { countrySummary ->
                if (countrySummary.isEmpty()) {
                    for (country in countries) {
                        addCountrySummaryAsync(country.name)
                    }
                    countrySummaryViewModel.addAllData(countryList)
                } else {
                    countryList = countrySummary as MutableList<CountrySummaryModel>

                }
                showCountrySummary()
            })
    }

    private fun callCountries() {
        showLoading(requireContext(), swipeRefreshLayout)
        mathdroidApiRequest = mathdroidApiRequest(httpClient)
        val call = mathdroidApiRequest.getCountries()
        call.enqueue(object : Callback<Countries> {
            override fun onFailure(call: Call<Countries>, t: Throwable) {

            }

            override fun onResponse(
                call: Call<Countries>, response:
                Response<Countries>
            ) {
                dismissLoading(swipeRefreshLayout)
                when {
                    response.isSuccessful ->
                        when {
                            response.body() != null -> {
                                countriesList = ArrayList()
                                countryList = ArrayList()
                                for (country in response.body()!!.countries) {
                                    countriesList.add(CountriesModel(country.name))
                                    addCountrySummaryAsync(country.name)
                                }
                                countrySummaryViewModel.addAllData(countryList)
                                countriesViewModel.addAllData(countriesList)
                                showCountrySummary()
                            }
                            else -> {
                                tampilToast(context!!, "Berhasil")
                            }
                        }
                    else -> {
                        tampilToast(requireContext(), "Coba Lagi!")
                    }
                }
            }
        })
    }

    private fun addCountrySummaryAsync(country: String) {
        mathdroidApiRequest = mathdroidApiRequest(httpClient)
        val call = mathdroidApiRequest.getCountry(country)
        call.enqueue(object : Callback<CountrySummary> {
            override fun onFailure(call: Call<CountrySummary>, t: Throwable) {
            }

            override fun onResponse(
                call: Call<CountrySummary>, response:
                Response<CountrySummary>
            ) {
                when {
                    response.isSuccessful ->
                        when {
                            response.body() != null -> {
                                val item = response.body()!!
                                countryList.add(
                                    CountrySummaryModel(
                                        item.confirmed.value.toString(),
                                        item.recovered.value.toString(),
                                        item.deaths.value.toString(),
                                        item.lastUpdate,
                                        country
                                    )
                                )
                            }
                        }
                }
            }
        })
    }

    private fun showCountrySummary() {

        adapter = CountrySummaryAdapter(requireContext(), countryList)
        countryRecyclerView.adapter = adapter
        countryRecyclerView.visibility = View.VISIBLE
        progressBar.visibility = View.GONE
    }


}