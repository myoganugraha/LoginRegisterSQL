package itk.myoganugraha.loginregistersql

import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import itk.myoganugraha.loginregistersql.Adapter.MovieAdapter
import itk.myoganugraha.loginregistersql.Model.Movie
import itk.myoganugraha.loginregistersql.SQLite.DBHandler

class MovieListActivity : AppCompatActivity() {

    private val context = this@MovieListActivity

    lateinit var movieList: ArrayList<Movie>
    lateinit var movieAdapter: MovieAdapter

    lateinit var rvMovieList : RecyclerView

    private lateinit var dbHandler: DBHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_list)

        movieList = ArrayList()
        movieAdapter = MovieAdapter(movieList, context)

        initComponents()
    }

    private fun initComponents() {
        dbHandler = DBHandler(context)

        rvMovieList = findViewById(R.id.rv_listMovie) as RecyclerView

        val mLayoutManager = LinearLayoutManager(context)
        rvMovieList.layoutManager = mLayoutManager
        rvMovieList.setHasFixedSize(true)

        rvMovieList.adapter = movieAdapter

        getData()
    }

    private fun getData() {
        var getDataFromSQLite = GetDataFromSQLite()
        getDataFromSQLite.execute()
    }

    inner class GetDataFromSQLite : AsyncTask<Void, Void, List<Movie>>() {

        override fun doInBackground(vararg p0: Void?): List<Movie> {
            return dbHandler.getAllMovies()
        }

        override fun onPostExecute(result: List<Movie>?) {
            super.onPostExecute(result)
            movieList.clear()
            movieList.addAll(result!!)
        }

    }
}
