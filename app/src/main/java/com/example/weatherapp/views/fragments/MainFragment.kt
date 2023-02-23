package com.example.weatherapp.views.fragments

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.core.view.setPadding
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.R
import com.example.weatherapp.api.RetrofitHelper
import com.example.weatherapp.api.WeatherService
import com.example.weatherapp.databinding.FragmentMainBinding
import com.example.weatherapp.repository.WeatherRepository
import com.example.weatherapp.utils.CommonUtils
import com.example.weatherapp.viewmodels.MainViewModel
import com.example.weatherapp.viewmodels.MainViewModelFactory
import com.squareup.picasso.Picasso

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel
    private lateinit var dataBinding: FragmentMainBinding

    lateinit var lastSavedLocation: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)
        return dataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val sharedPreference =
            activity?.getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
        lastSavedLocation = sharedPreference?.getString("location", "New York").toString()
        dataBinding.tvLocation.text = lastSavedLocation
        val weatherService = RetrofitHelper.getInstance().create(WeatherService::class.java)
        val repository = WeatherRepository(weatherService)
        viewModel =
            ViewModelProvider(
                this,
                MainViewModelFactory(repository, lastSavedLocation)
            )[MainViewModel::class.java]
        viewModel.weather.observe(viewLifecycleOwner, Observer {
            setWeatherIcon(dataBinding.ivWeatherIcon, it.weather[0].icon)

            dataBinding.tvTemp.text = it.main?.temp.toString() + "°"
            dataBinding.tvWeatherTitle.text = it.weather[0].main
            dataBinding.tvMinTemp.text = "min\n" + it.main?.tempMin.toString() + "°"
            dataBinding.tvMaxTemp.text = "max\n" + it.main?.tempMax.toString() + "°"
            dataBinding.tvDate.text = CommonUtils.getDate(it.dt.toString(), it.timezone!!.toInt())
        })

        dataBinding.llLocation.setOnClickListener(View.OnClickListener {
            addLocation()

        })
    }

    /**
     * Alert dialog to get the user entered location.
     * Design can be improved with custom UI
     */
    private fun addLocation() {
        val builder = AlertDialog.Builder(activity)
        with(builder)
        {
            val input = EditText(activity)
            input.setHint("Enter city name")
            input.setPadding(10)
//            val layoutParams : LinearLayout.LayoutParams = input.layoutParams as LinearLayout.LayoutParams
//            layoutParams.setMargins(10)
//            input.layoutParams = layoutParams
            input.inputType = InputType.TYPE_CLASS_TEXT
            builder.setView(input)
            setTitle("Enter Location")
            setMessage("Enter any city, state, country name")
            setPositiveButton("OK") { _, _ ->
                saveLocation(input.text.toString())
            }
            show()
        }


    }

    private fun saveLocation(text: String) {
        if(text.isNotEmpty()) {
            val sharedPreference =
                activity?.getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
            val editor = sharedPreference?.edit()
            if (editor != null) {
                editor.putString("location", text)
                editor.apply()
            }
            dataBinding.tvLocation.text = text
            viewModel.getWeather(text)
        }
    }

    private fun setWeatherIcon(view: ImageView, str: String?) {
        Picasso.get().load("https://openweathermap.org/img/wn/$str@4x.png").into(view)
    }

}