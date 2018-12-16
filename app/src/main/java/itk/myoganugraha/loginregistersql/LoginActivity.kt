package itk.myoganugraha.loginregistersql

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import itk.myoganugraha.loginregistersql.SQLite.DBHandler
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private val context = this@LoginActivity

    private lateinit var edtEmail : EditText
    private lateinit var  edtPassword : EditText

    private lateinit var dbHandler: DBHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initComponents()
        initDb()
    }

    private fun initDb() {
        dbHandler = DBHandler(context)
    }

    private fun initComponents() {
        edtEmail = findViewById(R.id.edt_email_login) as EditText
        edtPassword = findViewById(R.id.edt_password_login) as EditText

        btn_toregisteractivity.setOnClickListener {
            startActivity(Intent(context, RegisterActivity::class.java))
        }

        btn_login.setOnClickListener{
            if(edtEmail.length() < 4){

                Toast.makeText(context, "Email too short", Toast.LENGTH_SHORT).show()
            }
            else if(edtPassword.length() < 4) {

                Toast.makeText(context, "Password too short", Toast.LENGTH_SHORT).show()
            }
            else if (dbHandler!!.checkUser(edtEmail!!.text.toString().trim{it <= ' '}, edtPassword!!.text.toString().trim { it <= ' ' })) {

                val intent = Intent(context, MainActivity::class.java)
                intent.putExtra("Email", edtEmail.text.toString())
                startActivity(intent)
            }
            else{
                Toast.makeText(context, "Wrong Credentials", Toast.LENGTH_SHORT).show()
            }
        }



    }
}
