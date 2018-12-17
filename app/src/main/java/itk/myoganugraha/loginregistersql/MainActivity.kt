package itk.myoganugraha.loginregistersql

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import itk.myoganugraha.loginregistersql.SQLite.DBHandler
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val context = this@MainActivity
    private lateinit var email: String

    private lateinit var dbHandler: DBHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

         email = intent.getStringExtra("Email") //menerima email by intent dari login activity

        initComponents()
    }

    private fun initComponents() {
        dbHandler = DBHandler(context)

        tv_userid_mainactivity.setText("Your UserID: " + dbHandler.getUserData(email).id)
        tv_email_mainactivity.setText("Your Email: " + dbHandler.getUserData(email).email) //ambil email dari sqlite berdasarkan email saat login
        tv_username_mainactivity.setText("Your Name: " + dbHandler.getUserData(email).name) //ambil data nama dari sqlite berdasarkan email saat login

        btn_addMovieMain.setOnClickListener {
            startActivity(Intent(context, AddMovieActivity::class.java))
        }

        btn_goToMovieList.setOnClickListener {
            startActivity(Intent(context, MovieListActivity::class.java))
        }
    }
}
