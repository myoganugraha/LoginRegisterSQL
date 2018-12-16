package itk.myoganugraha.loginregistersql

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import itk.myoganugraha.loginregistersql.Model.User
import itk.myoganugraha.loginregistersql.SQLite.DBHandler
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    private val context = this@RegisterActivity

    private lateinit var edtEmail : EditText
    private lateinit var  edtPassword : EditText
    private lateinit var edtName : EditText

    private lateinit var dbHandler: DBHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        initComponents()
        initDb()
    }

    private fun initDb() {
        dbHandler = DBHandler(context)
    }

    private fun initComponents() {
        edtName = findViewById(R.id.edt_name_register) as EditText
        edtEmail = findViewById(R.id.edt_email_register) as EditText
        edtPassword = findViewById(R.id.edt_password_register) as EditText

        btn_tologinactivity.setOnClickListener {
            startActivity(Intent(context, LoginActivity::class.java))
        }

        btn_register.setOnClickListener {
            if(edtEmail.length() < 4){

                Toast.makeText(context, "Email too short", Toast.LENGTH_SHORT).show()
            }
            else if(edtPassword.length() < 4) {

                Toast.makeText(context, "Password too short", Toast.LENGTH_SHORT).show()
            }
            else if(edtName.length() < 4){

                Toast.makeText(context, "Name too short", Toast.LENGTH_SHORT).show()
            }
            else if (!dbHandler!!.checkIsEmailRegistered(edtEmail!!.text.toString().trim())) {

                var user = User(
                    name = edtName.text.toString().trim(),
                    email = edtEmail.text.toString().trim(),
                    password = edtPassword.text.toString().trim())

                dbHandler.addUser(user)

                Toast.makeText(context, "Register Successfully, Login Now", Toast.LENGTH_SHORT).show()

                startActivity(Intent(context, LoginActivity::class.java))
            }
            else{
                Toast.makeText(context, "Email Already Registered", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
