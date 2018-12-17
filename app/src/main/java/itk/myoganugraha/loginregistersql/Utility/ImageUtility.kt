package itk.myoganugraha.loginregistersql.Utility

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import java.io.ByteArrayOutputStream
import java.util.*

class ImageUtility{

    companion object {
        @SuppressLint("NewApi")
        fun encodeBitmap (bitmap: Bitmap) : String{
            val outputStream : ByteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)

            return android.util.Base64.encodeToString(outputStream.toByteArray(), android.util.Base64.DEFAULT)
        }

        @Throws(IllegalArgumentException::class)
        fun decodeBase64(base64Str: String): Bitmap {
            val decodedBytes = Base64.decode(
                base64Str.substring(base64Str.indexOf(",") + 1),
                Base64.DEFAULT
            )

            return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
        }
    }

}