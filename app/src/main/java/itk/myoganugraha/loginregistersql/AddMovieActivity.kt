package itk.myoganugraha.loginregistersql

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.opengl.Visibility
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import itk.myoganugraha.loginregistersql.Model.Movie
import itk.myoganugraha.loginregistersql.SQLite.DBHandler
import itk.myoganugraha.loginregistersql.Utility.ImageUtility
import kotlinx.android.synthetic.main.activity_add_movie.*
import pl.aprilapps.easyphotopicker.DefaultCallback
import pl.aprilapps.easyphotopicker.EasyImage
import java.io.File
import java.io.IOException

class AddMovieActivity : AppCompatActivity() {

    private val context = this@AddMovieActivity

    private lateinit var edtMovieName : EditText
    private lateinit var  edtMovieDesc : EditText
    private lateinit var ivMovieImage : ImageView

    private lateinit var imageSelected : Bitmap
    private lateinit var dbHandler: DBHandler

    private val REQUEST_CHOOSE_IMAGE = 3

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_movie)

        dbHandler = DBHandler(context)

        initComponents()
    }

    private fun initComponents() {

        edtMovieName = findViewById(R.id.edt_movie_name) as EditText
        edtMovieDesc = findViewById(R.id.edt_movie_desc) as EditText
        ivMovieImage = findViewById(R.id.iv_selectedImage) as ImageView

        btn_addMovie.setOnClickListener {
            if(edtMovieName.length() < 2 || edtMovieDesc.length() < 5 || ivMovieImage.visibility == View.GONE){
                Toast.makeText(context, "Credential Error", Toast.LENGTH_SHORT).show()
            }

            else{
                var movie = Movie(
                    movieName = edtMovieName.text.toString().trim(),
                    movieDesc =  edtMovieDesc.text.toString().trim(),
                    movieImage = ImageUtility.encodeBitmap(imageSelected)  )
                dbHandler.addMovie(movie)

                Toast.makeText(context, "Thank You", Toast.LENGTH_SHORT).show()
                finish()
            }
        }

        btn_addImage_movie.setOnClickListener {
            selectImage()
        }
    }

    private fun selectImage() {
        EasyImage.openChooserWithDocuments(context, "Choose Picture", REQUEST_CHOOSE_IMAGE)
    }

    protected override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?){
        super.onActivityResult(requestCode, resultCode, data)

        EasyImage.handleActivityResult(requestCode, resultCode, data, this, object : DefaultCallback() {
            override fun onImagePicked(imageFile: File, source: EasyImage.ImageSource, type: Int) {

                CropImage.activity(Uri.fromFile(imageFile))
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(4, 3)
                    .setFixAspectRatio(true)
                    .start(context)
            }

            override fun onImagePickerError(e: Exception?, source: EasyImage.ImageSource?, type: Int) {
                super.onImagePickerError(e, source, type)
                Toast.makeText(context, e!!.message, Toast.LENGTH_SHORT).show()
            }

            override fun onCanceled(source: EasyImage.ImageSource?, type: Int) {
                if (source == EasyImage.ImageSource.CAMERA) {
                    val photoFile = EasyImage.lastlyTakenButCanceledPhoto(context)
                    photoFile?.delete()
                }
            }
        })

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {
                val resultUri = result.uri
                try {
                    imageSelected = MediaStore.Images.Media.getBitmap(this.contentResolver, resultUri)
                } catch (e: IOException) {
                    e.printStackTrace()
                }

                Glide.with(this)
                    .load(File(resultUri.path!!))
                    .into(ivMovieImage)

                ivMovieImage.setVisibility(View.VISIBLE)


            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error

                Toast.makeText(this, error.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }
}
