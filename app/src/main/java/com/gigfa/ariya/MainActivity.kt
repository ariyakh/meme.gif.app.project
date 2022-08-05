package com.gigfa.ariya

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.*
import android.widget.TextView.OnEditorActionListener
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.registerForActivityResult
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gigfa.ariya.DataLayer.ApiClient
import com.gigfa.ariya.DataLayer.ApiInterface
import com.gigfa.ariya.Model.ModelRandomResponse
import com.gigfa.ariya.Model.ModelSearchResponse
import com.gigfa.ariya.Model.ModelTrendingResponse
import com.gigfa.ariya.adapters.RandomAdapter
import com.gigfa.ariya.adapters.SearchAdapter
import com.gigfa.ariya.adapters.TrendingAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.popup_window.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {

    val values = arrayOf("انتخاب محدودیت تعداد نمایش", "5", "10", "20", "30", "40", "50", "60")

    private var trendAdapter: TrendingAdapter? = null
    private var searchAdapter: SearchAdapter? = null
    private var randomAdapter: RandomAdapter? = null

    private lateinit var trends: ModelTrendingResponse
    private lateinit var searchs: ModelSearchResponse
    private lateinit var randoms: ModelRandomResponse

    private var limitedNumber = 10

    private lateinit var refreshBtn: Button
    private lateinit var edittext: EditText
    private lateinit var spinner: Spinner

    private var permission = 0
    private val requestPermisionLancher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            permission = if (it) {
                1
            } else {
                0
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestPermisionLancher.launch(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)

        initialView()
        clickListeners()
        trendsLoad()
        switch1.setOnClickListener {
            if (switch1.isChecked) {
                randomLoad()
            } else {
                trendsLoad()
            }
        }
        spinnerStuff()


        edittext.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                searchLoad()
                return@OnEditorActionListener true
            }
            false
        })
    }

    private fun initialView() {
        refreshBtn = refresh_btn
        edittext = mainact_editText
        spinner = main_spinner
    }

    private fun spinnerStuff() {
        val arrayAdapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, values)
        spinner.adapter = arrayAdapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener,
            AdapterView.OnItemClickListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long,
            ) {
                if (position != 0) {
                    limitedNumber = values[position].toInt()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // TODO
            }

            override fun onItemClick(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long,
            ) {
                // TODO
            }

        }
    }

    private fun clickListeners() {
        refreshBtn.setOnClickListener {

            if (edittext.text.toString() == "") {
                if (switch1.isChecked) {
                    randomLoad()
                } else {
                    trendsLoad()
                }

            } else {
                searchLoad()
            }
        }
    }

    private fun searchLoad() {

        val searchText = edittext.text.toString()

        ApiClient.getClient().create(ApiInterface::class.java)
            .getSearch("cIHL3Oight8TdEK2ORenfWM8Zc5Mc76R", searchText, limitedNumber, 0, "g", "en")
            .enqueue(object : Callback<ModelSearchResponse> {
                override fun onResponse(
                    call: Call<ModelSearchResponse>,
                    response: Response<ModelSearchResponse>,
                ) {
                    if (response.isSuccessful) {
                        searchSetState(true, response)
                    } else {
                        searchSetState(false)
                    }
                }

                override fun onFailure(call: Call<ModelSearchResponse>, t: Throwable) {
                    searchSetState(false)
                }

            })
    }

    private fun trendsLoad() {

        ApiClient.getClient().create(ApiInterface::class.java)
            .getTrending("cIHL3Oight8TdEK2ORenfWM8Zc5Mc76R", limitedNumber, "g")
            .enqueue(object : Callback<ModelTrendingResponse> {
                override fun onResponse(
                    call: Call<ModelTrendingResponse>,
                    response: Response<ModelTrendingResponse>,
                ) {
                    if (response.isSuccessful) {
                        setState(true, response)
                    } else {
                        setState(false)
                    }
                }

                override fun onFailure(call: Call<ModelTrendingResponse>, t: Throwable) {
                    setState(false)
                }

            })

    }

    private fun randomLoad() {

        ApiClient.getClient().create(ApiInterface::class.java)
            .getRandom("cIHL3Oight8TdEK2ORenfWM8Zc5Mc76R", "", "g")
            .enqueue(object : Callback<ModelRandomResponse> {
                override fun onResponse(
                    call: Call<ModelRandomResponse>,
                    response: Response<ModelRandomResponse>,
                ) {
                    if (response.isSuccessful) {
                        randomSetState(true, response)
                    } else {
                        randomSetState(false)
                    }
                }

                override fun onFailure(call: Call<ModelRandomResponse>, t: Throwable) {
                    randomSetState(false)
                }

            })

    }

    private fun randomSetState(
        isSuccess: Boolean,
        response: Response<ModelRandomResponse>? = null,
    ) {
        if (isSuccess) {

            randoms = response?.body()!!
            randomAdapter = RandomAdapter(this)
            randomAdapter!!.setData(arrayListOf(randoms.data))
            randomsInitRecycler()


        } else {
            val toastText = "اینترنت خود را بررسی کنید." + "\n" + "(از IP خارجی استفاده نمایید)"
            showMessageBox(toastText)
            //Toast.makeText(this, toastText, Toast.LENGTH_LONG).show()
        }
    }

    private fun setState(isSuccess: Boolean, response: Response<ModelTrendingResponse>? = null) {
        if (isSuccess) {

            trends = response?.body()!!
            trendAdapter = TrendingAdapter(this)
            trendAdapter!!.setData(trends.data as ArrayList<ModelTrendingResponse.Data>)
            trendingsInitRecycler()


        } else {
            val toastText = "اینترنت خود را بررسی کنید." + "\n" + "(از IP خارجی استفاده نمایید)"
            showMessageBox(toastText)
            //Toast.makeText(this, toastText, Toast.LENGTH_LONG).show()
        }
    }

    private fun searchSetState(
        isSuccess: Boolean,
        response: Response<ModelSearchResponse>? = null,
    ) {
        if (isSuccess) {

            searchs = response?.body()!!
            searchAdapter = SearchAdapter(this)
            searchAdapter!!.setData(searchs.data as ArrayList<ModelSearchResponse.Data>)
            searchInitRecycler()

        } else {
            val toastText = "اینترنت خود را بررسی کنید." + "\n" + "(از IP خارجی استفاده نمایید)"
            showMessageBox(toastText)
            //Toast.makeText(this, toastText, Toast.LENGTH_LONG).show()
        }
    }

    private fun trendingsInitRecycler() {
        val recyclerView = findViewById<RecyclerView>(R.id.search_RecyclerView)
        if (recyclerView != null) {
            recyclerView.itemAnimator = DefaultItemAnimator()
            recyclerView.adapter = trendAdapter
        }
        if (recyclerView != null) {
            recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        }

    }

    private fun randomsInitRecycler() {
        val recyclerView = findViewById<RecyclerView>(R.id.search_RecyclerView)
        if (recyclerView != null) {
            recyclerView.itemAnimator = DefaultItemAnimator()
            recyclerView.adapter = randomAdapter
        }
        if (recyclerView != null) {
            recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        }

    }

    private fun searchInitRecycler() {
        val recyclerView = findViewById<RecyclerView>(R.id.search_RecyclerView)
        if (recyclerView != null) {
            recyclerView.itemAnimator = DefaultItemAnimator()
            recyclerView.adapter = searchAdapter
        }
        if (recyclerView != null) {
            recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        }

    }

    private fun randomInitRecycler() {
        val recyclerView = findViewById<RecyclerView>(R.id.search_RecyclerView)
        if (recyclerView != null) {
            recyclerView.itemAnimator = DefaultItemAnimator()
            recyclerView.adapter = randomAdapter
        }
        if (recyclerView != null) {
            recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        }

    }

    private fun showMessageBox(text: String) {
        val messageBoxView = LayoutInflater.from(this).inflate(R.layout.popup_window, null)
        val messageBoxBuilder = AlertDialog.Builder(this).setView(messageBoxView)
        messageBoxView.popUpMenuText.text = text
        val messageBoxInstance = messageBoxBuilder.show()
        messageBoxInstance.window?.setBackgroundDrawableResource(R.drawable.popup_menu_background)
        messageBoxView.popUpMenuButton.setOnClickListener {
            messageBoxInstance.dismiss()
        }
    }

}