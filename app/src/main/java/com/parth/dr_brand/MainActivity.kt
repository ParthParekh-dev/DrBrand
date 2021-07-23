package com.parth.dr_brand

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.DisplayMetrics
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.parth.dr_brand.databinding.ActivityMainBinding
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private lateinit var viewModel: MainActivityViewModel
    private val pickProfile = 100
    private val pickLogo = 101
    private var profileUri: Uri? = null
    private var logoUri: Uri? = null
    private var xCoOrdinate = 0.0f
    private var yCoOrdinate = 0.0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)
        viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)

        binding.myViewModel = viewModel
        binding.lifecycleOwner = this
        


        binding.profile.setOnTouchListener(OnTouchListener { view, event ->
            when (event.actionMasked) {
                MotionEvent.ACTION_DOWN -> {
                    xCoOrdinate = view.x - event.rawX
                    yCoOrdinate = view.y - event.rawY
                }
                MotionEvent.ACTION_MOVE -> view.animate().x(event.rawX + xCoOrdinate)
                    .y(event.rawY + yCoOrdinate).setDuration(0).start()
                else -> return@OnTouchListener false
            }
            true
        })

        binding.logo.setOnTouchListener(OnTouchListener { view, event ->
            when (event.actionMasked) {
                MotionEvent.ACTION_DOWN -> {
                    xCoOrdinate = view.x - event.rawX
                    yCoOrdinate = view.y - event.rawY
                }
                MotionEvent.ACTION_MOVE -> view.animate().x(event.rawX + xCoOrdinate)
                    .y(event.rawY + yCoOrdinate).setDuration(0).start()
                else -> return@OnTouchListener false
            }
            true
        })

        binding.profileName.setOnTouchListener(OnTouchListener { view, event ->
            when (event.actionMasked) {
                MotionEvent.ACTION_DOWN -> {
                    xCoOrdinate = view.x - event.rawX
                    yCoOrdinate = view.y - event.rawY
                }
                MotionEvent.ACTION_MOVE -> view.animate().x(event.rawX + xCoOrdinate)
                    .y(event.rawY + yCoOrdinate).setDuration(0).start()
                else -> return@OnTouchListener false
            }
            true
        })



        binding.changeProfile.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, pickProfile)
        }

        binding.changeLogo.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, pickLogo)
        }

        binding.share.setOnClickListener {
            share()
        }


    }






    private fun share() {
        val bitmap: Bitmap? = getBitmapFromView(binding.template)
        val context: Context = this

        try {
            val cachePath = File(context.cacheDir, "images")
            cachePath.mkdirs() // don't forget to make the directory
            val stream =
                FileOutputStream("$cachePath/image.png") // overwrites this image every time
            bitmap?.compress(Bitmap.CompressFormat.PNG, 100, stream)
            stream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }


        val imagePath = File(context.cacheDir, "images")
        val newFile = File(imagePath, "image.png")
        val contentUri =
            FileProvider.getUriForFile(context, "com.parth.dr_brand.fileprovider", newFile)

        if (contentUri != null) {
            val shareIntent = Intent()
            shareIntent.action = Intent.ACTION_SEND
            shareIntent.type = "image/*"
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION) // temp permission for receiving app to read this file
            shareIntent.setDataAndType(contentUri, contentResolver.getType(contentUri))
            shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri)
            startActivity(Intent.createChooser(shareIntent, "Choose an app"))
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == pickProfile) {
            profileUri = data?.data
            binding.profile.setImageURI(profileUri)
        }
        else if (resultCode == RESULT_OK && requestCode == pickLogo) {
            logoUri = data?.data
            binding.logo.setImageURI(logoUri)
        }
    }

    @SuppressLint("ResourceAsColor")
    private fun getBitmapFromView(view: View): Bitmap? {
        val returnedBitmap =
            Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        //Bind a canvas to it
        val canvas = Canvas(returnedBitmap)

        canvas.drawColor(Color.WHITE)

        view.draw(canvas)
        return returnedBitmap
    }



}


