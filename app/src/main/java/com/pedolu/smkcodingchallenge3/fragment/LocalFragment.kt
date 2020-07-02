package com.pedolu.smkcodingchallenge3.fragment

import android.annotation.SuppressLint
import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.pedolu.smkcodingchallenge3.R
import com.pedolu.smkcodingchallenge3.data.httpClient
import com.pedolu.smkcodingchallenge3.data.mathdroidApiRequest
import com.pedolu.smkcodingchallenge3.data.model.local.Countries
import com.pedolu.smkcodingchallenge3.data.model.local.CountrySummary
import com.pedolu.smkcodingchallenge3.data.model.room.CountriesModel
import com.pedolu.smkcodingchallenge3.data.model.room.LocalSummaryModel
import com.pedolu.smkcodingchallenge3.data.service.CovidMathdroidService
import com.pedolu.smkcodingchallenge3.util.dismissLoading
import com.pedolu.smkcodingchallenge3.util.showLoading
import com.pedolu.smkcodingchallenge3.util.tampilToast
import com.pedolu.smkcodingchallenge3.viewmodel.CountriesViewModel
import com.pedolu.smkcodingchallenge3.viewmodel.LocalSummaryViewModel
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.fragment_local.*
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.NumberFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class LocalFragment : Fragment() {
    private lateinit var confirmed: String
    private lateinit var recovered: String
    private lateinit var deaths: String
    private var countryList: MutableList<CountriesModel> = ArrayList()
    private var countriesItem: ArrayList<String> = ArrayList()
    private val localSummaryViewModel by viewModels<LocalSummaryViewModel>()
    private val countriesViewModel by viewModels<CountriesViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_local, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        retrieveRoomCountries()
        swipeRefreshLayout.setOnRefreshListener {
            callCountrySummary(countrySpinner.selectedItem.toString())
            showLocalSummary()
        }
    }

    private fun retrieveRoomCountries() {
        countriesViewModel.init(requireContext())
        countriesViewModel.countries.observe(viewLifecycleOwner, Observer { countries ->
            if (countries != null) {
                for (country in countries) {
                    countriesItem.add(country.name)
                }
                if(countriesItem.isEmpty()){
                    callCountries()
                }
                setCountrySpinner()
            } else {
                Log.i("countries","onok")
                callCountries()
            }
        })

    }

    private fun retrieveRoomCountrySummary(country: String) {
        localSummaryViewModel.init(requireContext(), country)
        localSummaryViewModel.localSummary.observe(viewLifecycleOwner, Observer { localSummary ->
            if (localSummary != null) {
                confirmed = localSummary.confirmed
                recovered = localSummary.recovered
                deaths = localSummary.deaths
                setLastUpdate(localSummary.last_update)
                showLocalSummary()
            } else {
                callCountrySummary(country)
            }
        })
    }


    private fun callCountries() {
        setInvisible()
        val httpClient = httpClient()
        val mathdroidApiRequest = mathdroidApiRequest<CovidMathdroidService>(httpClient)
        val call = mathdroidApiRequest.getCountries()
        call.enqueue(object : Callback<Countries> {
            override fun onFailure(call: Call<Countries>, t: Throwable) {
                setVisible()
            }

            override fun onResponse(
                call: Call<Countries>, response:
                Response<Countries>
            ) {
                setVisible()
                when {
                    response.isSuccessful ->
                        when {
                            response.body() != null -> {
                                countriesViewModel.init(requireContext())
                                for (country in response.body()!!.countries) {
                                    addCountrySummaryAsync(country.name)
                                }
                                countriesViewModel.addAllData(countryList)
                                setCountrySpinner()
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

    private fun callCountrySummary(country: String) {
        showLoading(requireContext(), swipeRefreshLayout)
        val httpClient = httpClient()
        val mathdroidApiRequest = mathdroidApiRequest<CovidMathdroidService>(httpClient)
        val call = mathdroidApiRequest.getCountry(country)
        call.enqueue(object : Callback<CountrySummary> {
            override fun onFailure(call: Call<CountrySummary>, t: Throwable) {
                dismissLoading(swipeRefreshLayout)
            }

            override fun onResponse(
                call: Call<CountrySummary>, response:
                Response<CountrySummary>
            ) {
                dismissLoading(swipeRefreshLayout)
                when {
                    response.isSuccessful ->
                        when {
                            response.body() != null -> {
                                val item = response.body()!!
                                confirmed = item.confirmed.value.toString()
                                recovered = item.recovered.value.toString()
                                deaths = item.deaths.value.toString()
                                val localSummary = LocalSummaryModel(
                                    confirmed,
                                    recovered,
                                    deaths,
                                    item.lastUpdate,
                                    country
                                )
                                localSummaryViewModel.addData(localSummary)

                            }
                            else ->
                                tampilToast(context!!, "Berhasil")
                        }
                }
            }
        })
    }

    private fun addCountrySummaryAsync(country: String): Deferred<Int> = GlobalScope.async {
        countriesItem.add(country)
        countryList.add(CountriesModel(country))
        val httpClient = httpClient()
        val mathdroidApiRequest = mathdroidApiRequest<CovidMathdroidService>(httpClient)
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
                                val localSummary = LocalSummaryModel(
                                    item.confirmed.value.toString(),
                                    item.recovered.value.toString(),
                                    item.deaths.value.toString(),
                                    item.lastUpdate,
                                    country
                                )
                                localSummaryViewModel.addData(localSummary)
                            }
                        }
                }
            }
        })
        return@async 42
    }

    private fun showLocalSummary() {
        createLocalSummaryChart()
        txtConfirmed.text = NumberFormat.getNumberInstance(Locale.US).format(confirmed.toInt())
        txtRecovered.text = NumberFormat.getNumberInstance(Locale.US).format(recovered.toInt())
        txtDeath.text = NumberFormat.getNumberInstance(Locale.US).format(deaths.toInt())
    }

    private fun createLocalSummaryChart() {
        val pieChart: PieChart = localSummaryChart
        val listPie = ArrayList<PieEntry>()
        val listColors = ArrayList<Int>()
        listPie.add(PieEntry(recovered.toFloat()))
        listColors.add(ContextCompat.getColor(requireContext(), R.color.colorGreen))
        listPie.add(PieEntry(confirmed.toFloat()))
        listColors.add(ContextCompat.getColor(requireContext(), R.color.colorYellow))
        listPie.add(PieEntry(deaths.toFloat()))
        listColors.add(ContextCompat.getColor(requireContext(), R.color.colorRed))

        val pieDataSet = PieDataSet(listPie, "")
        pieDataSet.colors = listColors
        pieDataSet.setDrawValues(false)
        val pieData = PieData(pieDataSet)
        pieChart.data = pieData
        pieChart.apply {
            description.isEnabled = false
            legend.isEnabled = false
            setDrawEntryLabels(false)
            isDrawHoleEnabled = true
            holeRadius = 60f
            description.isEnabled = false
            transparentCircleRadius = 0f
            animateY(1400, Easing.EaseInOutQuad)
            setHoleColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.colorWhite
                )
            )
            invalidate()
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun setLastUpdate(lastUpdate: String) {
        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        try {
            val date: Date = format.parse(lastUpdate)
            val formatter =
                SimpleDateFormat("yyyy-MM-dd | HH:mm:ss")
            val output =
                formatter.format(date)
            txtLastUpdate.text = output.toString()
        } catch (e: ParseException) {
            e.printStackTrace()
            Log.e("error", e.message.toString())
        }
    }

    private fun setCountrySpinner() {
        val adapter = ArrayAdapter(
            this.requireActivity(),
            android.R.layout.simple_spinner_item,
            countriesItem
        )
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        countrySpinner.background.setColorFilter(
            ContextCompat.getColor(requireContext(), R.color.colorPrimary),
            PorterDuff.Mode.SRC_ATOP
        )
        countrySpinner.adapter = adapter
        countrySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                (parent!!.getChildAt(0) as TextView).textSize = 32f
                (parent.getChildAt(0) as TextView).gravity = Gravity.CENTER
                retrieveRoomCountrySummary(countrySpinner.selectedItem.toString())
                callCountrySummary(countrySpinner.selectedItem.toString())
            }
        }
    }

    private fun setVisible() {
        graphCard.visibility = View.VISIBLE
        confirmedCard.visibility = View.VISIBLE
        recoveredCard.visibility = View.VISIBLE
        deathCard.visibility = View.VISIBLE
        progressBar.visibility = View.GONE
    }

    private fun setInvisible() {
        graphCard.visibility = View.GONE
        confirmedCard.visibility = View.GONE
        recoveredCard.visibility = View.GONE
        deathCard.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
    }

    override fun onDestroy() {
        super.onDestroy()
        this.clearFindViewByIdCache()
    }
}