package com.example.backgroundremoverlibrary

import android.animation.Animator
import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toBitmap
import com.airbnb.lottie.LottieAnimationView
import com.example.backgroundremoverlibrary.databinding.ActivityMainPreviousBinding
import com.slowmac.autobackgroundremover.BackgroundRemover
import com.slowmac.autobackgroundremover.OnBackgroundChangeListener
import java.io.OutputStream

class MainActivityPrevious : AppCompatActivity() {
    private lateinit var lottieAnimationView: LottieAnimationView
    private lateinit var binding: ActivityMainPreviousBinding
    private var processedBitmap: Bitmap? = null

    private val imageResult =
        registerForActivityResult(
            ActivityResultContracts.GetContent()
        ) { uri: Uri? ->
            uri?.let { uri ->
                binding.img.setImageURI(uri)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainPreviousBinding.inflate(layoutInflater)
        setContentView(binding.root)
        lottieAnimationView = findViewById(R.id.lottieAnimationView)
        // Set animation listener to detect when animation ends
        lottieAnimationView.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
                // Animation started
            }

            override fun onAnimationEnd(animation: Animator) {
                // Animation ended, set visibility of the button to visible
                binding.save.visibility = View.VISIBLE
                lottieAnimationView.visibility=View.INVISIBLE
            }

            override fun onAnimationCancel(animation: Animator) {
                // Animation canceled
            }

            override fun onAnimationRepeat(animation: Animator) {
                // Animation repeated
            }
        })


        imageResult.launch("image/*")
        binding.removeBgBtn.setOnClickListener {
            removeBg()
            binding.removeBgBtn.visibility = View.INVISIBLE

            // Start the Lottie animation
            lottieAnimationView.visibility = View.VISIBLE
            lottieAnimationView.playAnimation()
        }
        binding.save.setOnClickListener {
            processedBitmap?.let { saveImageToGallery(it) }
        }

    }

    private fun removeBg() {
        binding.img.invalidate()
        BackgroundRemover.bitmapForProcessing(
            binding.img.drawable.toBitmap(),
            true,
            object : OnBackgroundChangeListener {
                override fun onSuccess(bitmap: Bitmap) {
                    binding.img.setImageBitmap(bitmap)
                    processedBitmap = bitmap
                    binding.save.visibility = View.VISIBLE
                }

                override fun onFailed(exception: Exception) {
                    Toast.makeText(
                        this@MainActivityPrevious,
                        "Error Occurred",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    private fun saveImageToGallery(bitmap: Bitmap) {
        val contentResolver = contentResolver
        val imageUri = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)

        val imageDetails = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "processed_image.png")
            put(MediaStore.Images.Media.MIME_TYPE, "image/png")
        }

        val imageUriResult = contentResolver.insert(imageUri, imageDetails)
        imageUriResult?.let { uri ->
            val outputStream: OutputStream? = contentResolver.openOutputStream(uri)

            val fullBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(fullBitmap)

            // Draw the processed bitmap onto the full-sized bitmap
            val paint = Paint(Paint.ANTI_ALIAS_FLAG)
            canvas.drawBitmap(bitmap, 0f, 0f, paint)

            // Compress and save the full-sized bitmap
            outputStream?.use { stream ->
                fullBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            }

            Toast.makeText(this@MainActivityPrevious, "Image saved to gallery", Toast.LENGTH_SHORT).show()
        } ?: run {
            Toast.makeText(this@MainActivityPrevious, "Failed to save image", Toast.LENGTH_SHORT).show()
        }
    }




}
