package com.gigfa.ariya.adapters

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.Context
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
import com.gigfa.ariya.Model.ModelSearchResponse
import com.gigfa.ariya.R
import java.io.File

class SearchAdapter(private val context: Context) :
    RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    private var searchlist = arrayListOf<ModelSearchResponse.Data>()


    @SuppressLint("NotifyDataSetChanged")
    fun setData(items: ArrayList<ModelSearchResponse.Data>) {
        searchlist = items
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_trending, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val searchs = searchlist[position]

        Glide.with(context)
            .load(searchs.images.downsizedMedium.url)
            .into(holder.trendingImage)

        holder.trendingTitle.text = searchs.title
        holder.itemView.setOnClickListener {

            fun randomNumber(): Int {
                return (0..10000).shuffled().last()
            }

            val thatNumber = randomNumber()

            val request = DownloadManager.Request(Uri.parse(searchs.images.original.mp4))
                .setTitle("Gif")
                .setDescription("Downloading ...")
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(false)
                .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,
                    File.separator + "Ariya Gif " + "$$thatNumber" + ".mp4")

            val dm = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            dm.enqueue(request)
            Toast.makeText(context, "دانلود آغاز شد ...", Toast.LENGTH_LONG).show()

        }

    }

    override fun getItemCount(): Int {
        return searchlist.size
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val trendingTitle: TextView = itemView.findViewById(R.id.trending_item_gif_name)
        val trendingImage: ImageView = itemView.findViewById(R.id.trending_item_gif_picture)


    }



}