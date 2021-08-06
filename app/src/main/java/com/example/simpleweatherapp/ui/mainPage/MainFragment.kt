package com.example.simpleweatherapp.ui.mainPage

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.simpleweatherapp.R
import com.example.simpleweatherapp.adapters.WeatherItemClickListener
import com.example.simpleweatherapp.adapters.WeatherListAdapter
import com.example.simpleweatherapp.data.model.Weather
import com.example.simpleweatherapp.databinding.MainFragmentBinding
import com.example.simpleweatherapp.util.Resource
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment(), WeatherItemClickListener {

    private val viewModel: MainViewModel by viewModels()

    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var weatherListAdapter: WeatherListAdapter

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MainFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        setUpObserver()
        setUpRecyclerView()
        if (weatherListAdapter.items.isEmpty()) {
            requestPermission()
        }
        setUpToolbar()
        setItemDragListener()
    }

    private val requestPermissionLauncher = registerForActivityResult(
        RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            getLocation()
        } else {
            Toast.makeText(
                context,
                "Can't get access to current location",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun setUpObserver() {
        viewModel.weatherByLocationResponse.observe(
            viewLifecycleOwner, { it ->
                when (it.status) {
                    Resource.Status.SUCCESS -> {
                        weatherListAdapter.addItem((it.data!!))
                        viewModel.getCitiesFromLocal()
                        binding.cities.visibility = View.VISIBLE
                        binding.progressBar.visibility = View.GONE
                    }
                    Resource.Status.ERROR -> {
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }
                    Resource.Status.LOADING -> {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.cities.visibility = View.GONE
                    }
                }
            }
        )

        viewModel.weatherByCityResponse.observe(
            viewLifecycleOwner, {
                when (it.status) {
                    Resource.Status.SUCCESS -> {
                        viewModel.insertCity(it.data!!)
                        weatherListAdapter.addItem(it.data)
                        binding.cities.visibility = View.VISIBLE
                        binding.progressBar.visibility = View.GONE
                    }
                    Resource.Status.ERROR -> {
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }
                    Resource.Status.LOADING -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                }
            }
        )

        viewModel.localCities.observe(
            viewLifecycleOwner,
            { it ->
                it.forEach {
                    viewModel.getWeatherByCity(it.name)
                }
            }
        )
    }

    private fun setUpRecyclerView() {
        weatherListAdapter = WeatherListAdapter(this)
        binding.cities.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.cities.adapter = weatherListAdapter
        binding.cities.visibility = View.GONE
    }

    private fun requestPermission() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
        getLocation()
    }

    @SuppressLint("MissingPermission")
    fun getLocation() {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                if (location != null) {
                    viewModel.getWeatherByLocation(location.latitude, location.longitude)
                }
            }
    }

    private fun setItemDragListener() {
        val itemTouchCallback =
            object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val position = viewHolder.adapterPosition
                    viewModel.removeCity(weatherListAdapter.getItem(position))
                    weatherListAdapter.deleteItem(position)
                }
            }

        ItemTouchHelper(itemTouchCallback).apply {
            attachToRecyclerView(binding.cities)
        }
    }

    private fun setUpToolbar() {
        binding.toolbar.inflateMenu(R.menu.app_bar_menu)

        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.add_button -> {
                    val builder: AlertDialog.Builder = android.app.AlertDialog.Builder(requireContext())
                    builder.setTitle(getString(R.string.add_dialog_title))
                    val input = EditText(requireContext())
                    input.hint = getString(R.string.add_dialog_hint)
                    input.inputType = InputType.TYPE_CLASS_TEXT
                    builder.setView(input)

                    builder.setPositiveButton(getString(R.string.text_add)) { dialog, _ ->
                        val cityName = input.text.toString()
                        viewModel.getWeatherByCity(cityName)
                    }

                    builder.setNegativeButton(getString(R.string.text_cancel)) { dialog, _ ->
                        dialog.cancel()
                    }
                    builder.show()
                    true
                }
                else -> false
            }
        }
    }

    override fun onPause() {
        weatherListAdapter.items.clear()
        super.onPause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClickedWeather(weather: Weather) {
        val action = MainFragmentDirections.actionSearchFragmentToDetailFragment(weather)
        findNavController().navigate(action)
    }
}
