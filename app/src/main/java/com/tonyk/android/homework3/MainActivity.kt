package com.tonyk.android.homework3


import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

import com.tonyk.android.homework3.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.editTextUrl.setOnEditorActionListener { _, _, _ ->
        loadImage()
            true
        }
    }

    private fun loadImage() {
        binding.progressBar.visibility = View.VISIBLE
        val imageUrl = binding.editTextUrl.text.toString()
        if (imageUrl.isNotEmpty()) {
            Picasso.get()
                .load(imageUrl)
                .error(R.drawable.error)
                .into(binding.imageView, object : Callback {
                    override fun onSuccess() {
                        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.hideSoftInputFromWindow(binding.editTextUrl.windowToken, 0)
                        binding.progressBar.visibility = View.GONE
                    }
                    override fun onError(e: Exception?) {
                        Toast.makeText(this@MainActivity, "Failed to load image. ${e?.message}", Toast.LENGTH_SHORT).show()
                        binding.progressBar.visibility = View.GONE
                    }
                })
        } else {
            Toast.makeText(this@MainActivity, "Please enter a valid image URL", Toast.LENGTH_SHORT).show()
            binding.progressBar.visibility = View.GONE
        }
    }
}