package proj.marvel.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.adapter_item.view.*
import proj.marvel.R
import proj.marvel.service.model.MarvelResponse

class ListAdapter(var items: MutableList<MarvelResponse.Data.Result>) : RecyclerView.Adapter<ListAdapter.ViewHolder>() {
    lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.adapter_item,
                parent,
                false
            )
        )
    }

    override fun getItemCount() = items.count()

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = items[position]
        holder.name.text = model.name
        holder.description.text = model.description
        Glide.with(holder.image).load("${model.thumbnail.path}.${model.thumbnail.extension}")
            .apply(RequestOptions().placeholder(R.drawable.placeholder))
            .apply(RequestOptions.circleCropTransform()).into(holder.image)

    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    class ViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        var name: TextView = item.name
        var image: ImageView = item.image
        var description: TextView = item.description
    }
}