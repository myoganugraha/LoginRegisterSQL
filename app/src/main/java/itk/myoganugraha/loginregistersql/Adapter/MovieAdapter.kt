package itk.myoganugraha.loginregistersql.Adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import itk.myoganugraha.loginregistersql.Model.Movie
import itk.myoganugraha.loginregistersql.R
import itk.myoganugraha.loginregistersql.Utility.ImageUtility
import kotlinx.android.synthetic.main.cardview_item.view.*

class MovieAdapter(var movieList: ArrayList<Movie>?, val mContext : Context) :
    RecyclerView.Adapter<MovieAdapter.CustomviewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): CustomviewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.cardview_item, viewGroup, false)
        return CustomviewHolder(view)
    }

    override fun getItemCount(): Int {
       return movieList!!.size
    }

    override fun onBindViewHolder(customviewHolder: CustomviewHolder, position: Int) {
       val movie = movieList!!.get(position)

        customviewHolder.tvMovieTitle.setText(movie.movieName)
        customviewHolder.tvMovieDesc.setText(movie.movieDesc)

        Glide.with(mContext)
            .load(ImageUtility.decodeBase64(movie.movieImage))
            .apply(RequestOptions().placeholder(R.color.colorPrimary))
            .into(customviewHolder.ivMovie)
    }

    class CustomviewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val ivMovie = itemView.civ_cardview_movie
        val tvMovieTitle = itemView.movie_title
        val tvMovieDesc = itemView.movie_desc
    }
}