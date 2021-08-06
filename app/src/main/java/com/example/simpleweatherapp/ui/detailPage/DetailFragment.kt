package com.example.simpleweatherapp.ui.detailPage

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.simpleweatherapp.R
import com.example.simpleweatherapp.databinding.DetailFragmentBinding

class DetailFragment : Fragment() {

    private var _binding: DetailFragmentBinding? = null
    private val binding get() = _binding!!

    private val args: DetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DetailFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        bindView()
    }

    private fun bindView() {
        val weather = args.weather
        binding.cityNameText.text = weather.name
        binding.detailWeatherIcon.setImageDrawable(getIconByCode(weather.weather.first().id))
        binding.windDirection.text = degreesToDirection(weather.wind.deg)
        binding.weatherText.text = weather.main.temp.toInt().toString() + "°C"
    }

    private fun getIconByCode(code: Int): Drawable? {
        when (code) {
            in 200..299 -> return AppCompatResources.getDrawable(requireContext(), R.drawable.ic_thunder)
            in 300..399 -> return AppCompatResources.getDrawable(requireContext(), R.drawable.ic_dizzle)
            in 500..599 -> return AppCompatResources.getDrawable(requireContext(), R.drawable.ic_rain)
            in 600..699 -> return AppCompatResources.getDrawable(requireContext(), R.drawable.ic_snow)
            in 700..799 -> return AppCompatResources.getDrawable(requireContext(), R.drawable.ic_atmosphere)
            800 -> return AppCompatResources.getDrawable(requireContext(), R.drawable.ic_clear)
            in 801..805 -> return AppCompatResources.getDrawable(requireContext(), R.drawable.ic_cloud)
        }
        return AppCompatResources.getDrawable(requireContext(), R.drawable.ic_clear)
    }

    private fun degreesToDirection(degrees: Double): String {
        when (degrees.toInt()) {
            in 0..90 -> return "N"
            in 91..180 -> return "E"
            in 181..270 -> return "S"
            in 271..360 -> return "W"
        }
        return "N/A"
    }
}
