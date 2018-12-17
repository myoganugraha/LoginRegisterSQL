package itk.myoganugraha.loginregistersql.SQLite

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import itk.myoganugraha.loginregistersql.Model.Movie
import itk.myoganugraha.loginregistersql.Model.User

class DBHandler(context: Context) : SQLiteOpenHelper(context, DB_Name, null, DB_Version){

    companion object {
        private val DB_Name = "User.db"
        private val DB_Version = 1
        private val TB_USER = "user"
        private val TB_MOVIE = "movie"

        private val COLUMN_USER_ID = "user_id"
        private val COLUMN_USER_NAME = "user_name"
        private val COLUMN_USER_EMAIL = "user_email"
        private val COLUMN_USER_PASSWORD = "user_password"

        private val COLUMN_MOVIE_ID = "movie_id"
        private val COLUMN_MOVIE_NAME = "movie_name"
        private val COLUMN_MOVIE_DESC = "movie_description"
        private val COLUMN_MOVIE_IMAGE = "movie_image"
    }

    private val CREATE_USER_TABLE = ("CREATE TABLE " + TB_USER + "("
            + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_USER_NAME + " TEXT,"
            + COLUMN_USER_EMAIL + " TEXT," + COLUMN_USER_PASSWORD + " TEXT" + ")")

    private val CREATE_MOVIE_TABLE = ("CREATE TABLE " + TB_MOVIE + "("
            + COLUMN_MOVIE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_MOVIE_NAME + " TEXT,"
            + COLUMN_MOVIE_DESC + " TEXT," + COLUMN_MOVIE_IMAGE + " TEXT" + ")")

    private val DROP_USER_TABLE = "DROP TABLE IF EXISTS $TB_USER"
    private val DROP_MOVIE_TABLE = "DROP TABLE IF EXISTS $TB_MOVIE"

    override fun onCreate(db: SQLiteDatabase?) {

        db!!.execSQL(CREATE_MOVIE_TABLE)
        db!!.execSQL(CREATE_USER_TABLE)


    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL(DROP_USER_TABLE)
        db.execSQL(DROP_MOVIE_TABLE)
        onCreate(db)
    }

    fun addMovie(movie: Movie){
        val db = this.writableDatabase

        val values = ContentValues()
        values.put(COLUMN_MOVIE_NAME, movie.movieName)
        values.put(COLUMN_MOVIE_DESC, movie.movieDesc)
        values.put(COLUMN_MOVIE_IMAGE, movie.movieImage)

        db.insert(TB_MOVIE, null, values)
        db.close()
    }

    fun getAllMovies(): List<Movie>{
        val columns = arrayOf(COLUMN_MOVIE_ID, COLUMN_MOVIE_NAME, COLUMN_MOVIE_DESC, COLUMN_MOVIE_IMAGE)

        val sortOrder = "$COLUMN_MOVIE_NAME ASC"
        val movieList = ArrayList<Movie>()

        val db = this.readableDatabase

        val cursor = db.query(TB_MOVIE,
            columns,
            null,
            null,
            null,
            null,
            sortOrder)

        if(cursor.moveToFirst()){
            do {
                val movie = Movie(
                    idMovie = cursor.getInt(cursor.getColumnIndex(COLUMN_MOVIE_ID)),
                    movieName = cursor.getString(cursor.getColumnIndex(COLUMN_MOVIE_NAME)),
                    movieDesc =  cursor.getString(cursor.getColumnIndex(COLUMN_MOVIE_DESC)),
                    movieImage = cursor.getString(cursor.getColumnIndex(COLUMN_MOVIE_IMAGE))
                )
                movieList.add(movie)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return movieList
    }

    //Regist User
    fun addUser(user: User){
        val db = this.writableDatabase

        val values = ContentValues()
        values.put(COLUMN_USER_NAME, user.name) //nama user
        values.put(COLUMN_USER_EMAIL, user.email) //email user
        values.put(COLUMN_USER_PASSWORD, user.password) //password user

        db.insert(TB_USER, null, values) //insert data ke table user
        db.close()
    }

    fun updateUser(user: User){
        val db = this.writableDatabase

        val values = ContentValues()
        values.put(COLUMN_USER_NAME, user.name) //nama user
        values.put(COLUMN_USER_EMAIL, user.email) //email user
        values.put(COLUMN_USER_PASSWORD, user.password) //password user

        //update db
        db.update(TB_USER, values, "$COLUMN_USER_EMAIL = ?", arrayOf(user.email.toString())) //cek berdasarkan email
        db.close()
    }

    fun checkIsEmailRegistered(email: String): Boolean{
        val columns = arrayOf(COLUMN_USER_ID)
        val db = this.readableDatabase

        val selection =  "$COLUMN_USER_EMAIL = ?" //yang di cek
        val selectionArgs = arrayOf(email)

        val cursor = db.query(TB_USER,
            columns,
            selection,
            selectionArgs,
            null,
            null,
            null)

        val cursorCount = cursor.count
        cursor.close()
        db.close()

        if(cursorCount >0){
            return true
        }

        return false
    }

    fun checkUser(email: String, password: String): Boolean {

        // array of columns to fetch
        val columns = arrayOf(COLUMN_USER_ID)

        val db = this.readableDatabase

        // selection criteria
        val selection = "$COLUMN_USER_EMAIL = ? AND $COLUMN_USER_PASSWORD = ?"

        // selection arguments
        val selectionArgs = arrayOf(email, password)

        // query user table with conditions
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_email = 'jack@androidtutorialshub.com' AND user_password = 'qwerty';
         */
        val cursor = db.query(
            TB_USER, //Table to query
            columns, //columns to return
            selection, //columns for the WHERE clause
            selectionArgs, //The values for the WHERE clause
            null,  //group the rows
            null, //filter by row groups
            null) //The sort order

        val cursorCount = cursor.count
        cursor.close()
        db.close()

        if (cursorCount > 0)
            return true

        return false

    }

    fun getUserData(email: String) : User{

        val columns = arrayOf(COLUMN_USER_ID, COLUMN_USER_NAME, COLUMN_USER_EMAIL, COLUMN_USER_PASSWORD)
        val db = this.readableDatabase
        lateinit var user: User

        val selection =  "$COLUMN_USER_EMAIL = ?" //yang di cek
        val selectionArgs = arrayOf(email.toString())

        val cursor = db.query(TB_USER,
            columns,
            selection,
            selectionArgs,
            null,
            null,
            null)

        if (cursor != null)
            cursor.moveToFirst()

        user  = User(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3))


        return user

    }


}