package com.gigfa.ariya.adapters

import android.R
import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.Context
import android.content.Context.DOWNLOAD_SERVICE
import android.graphics.Color
import android.net.Uri
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gigfa.ariya.Model.ModelTrendingResponse
import com.gigfa.ariya.R
import java.io.File


class TrendingAdapter(private val context: Context) :
    RecyclerView.Adapter<TrendingAdapter.ViewHolder>() {

    private var trendinglist = arrayListOf<ModelTrendingResponse.Data>()

    @SuppressLint("NotifyDataSetChanged")
    fun setData(items: ArrayList<ModelTrendingResponse.Data>) {
        trendinglist = items
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_trending, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val trendings = trendinglist[position]


//        val resourceId =
//            context.resources.getIdentifier(services.image, "drawable", context.packageName)
//        holder.serviceImage.setImageResource(resourceId)

        Glide.with(context)
            .load(trendings.images.downsizedMedium.url)
            .into(holder.trendingImage)

        holder.trendingTitle.text = trendings.title

        holder.itemView.setOnClickListener {

            fun randomNumber(): Int {
                return (0..10000).shuffled().last()
            }

            val thatNumber = randomNumber()

            val request = DownloadManager.Request(Uri.parse(trendings.images.original.url))
                .setTitle("Gif")
                .setDescription("Downloading ...")
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(false)
                .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,
                    File.separator + "Ariya Gif " + "$thatNumber" + ".gif")

            val dm = context.getSystemService(DOWNLOAD_SERVICE) as DownloadManager
            dm.enqueue(request)
            Toast.makeText(context, "Download Started in download folder!", Toast.LENGTH_LONG)
                .show()




        }

    }

    override fun getItemCount(): Int {
        return trendinglist.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val trendingTitle: TextView = itemView.findViewById(R.id.trending_item_gif_name)
        val trendingImage: ImageView = itemView.findViewById(R.id.trending_item_gif_picture)


    }

}