package com.example.simpleweatherapp.adapters

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.example.simpleweatherapp.R
import com.example.simpleweatherapp.data.model.Weather
import com.example.simpleweatherapp.databinding.ItemCityCardBinding

class WeatherListAdapter(
    private val listener: WeatherItemClickListener
) : RecyclerView.Adapter<WeatherListAdapter.WeatherViewHolder>() {

    val items = ArrayList<Weather>()

    fun addItem(item: Weather) {
        if (!items.contains(item)) {
            this.items.add(item)
        }
        notifyDataSetChanged()
    }

    fun deleteItem(position: Int) {
        items.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, itemCount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val binding: ItemCityCardBinding =
            ItemCityCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WeatherViewHolder(binding, listener)
    }

    class WeatherViewHolder(
        private val itemBinding: ItemCityCardBinding,
        private val listener: WeatherItemClickListener
    ) : RecyclerView.ViewHolder(itemBinding.root), View.OnClickListener {

        private lateinit var weather: Weather

        init {
            itemBinding.root.setOnClickListener(this)
        }

        fun bind(item: Weather) {
            this.weather = item

            itemBinding.cityName.text = item.name
            itemBinding.weatherType.text = item.weather.first().main
            itemBinding.weather.text = item.main.temp.toInt().toString() + "°C"
            itemBinding.imageView.setImageDrawable(getIconByCode(item.weather.first().id))
        }

        override fun onClick(p0: View?) {
            listener.onClickedWeather(weather)
        }

        private fun getIconByCode(code: Int): Drawable? {
            when (code) {
                in 200..299 -> return AppCompatResources.getDrawable(this.itemView.context, R.drawable.ic_thunder)
                in 300..399 -> return AppCompatResources.getDrawable(this.itemView.context, R.drawable.ic_dizzle)
                in 500..599 -> return AppCompatResources.getDrawable(this.itemView.context, R.drawable.ic_rain)
                in 600..699 -> return AppCompatResources.getDrawable(this.itemView.context, R.drawable.ic_snow)
                in 700..799 -> return AppCompatResources.getDrawable(this.itemView.context, R.drawable.ic_atmosphere)
                800 -> return AppCompatResources.getDrawable(this.itemView.context, R.drawable.ic_clear)
                in 801..805 -> return AppCompatResources.getDrawable(this.itemView.context, R.drawable.ic_cloud)
            }
            return AppCompatResources.getDrawable(this.itemView.context, R.drawable.ic_clear)
        }
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) =
        holder.bind(items[position])

    fun getItem(position: Int): Weather {
        return items[position]
    }

    override fun getItemCount(): Int = items.size
}
