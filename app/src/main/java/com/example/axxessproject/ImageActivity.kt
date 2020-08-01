package com.example.axxessproject

import android.app.Activity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.example.axxessproject.models.ImageItem
import com.example.axxessproject.viewmodel.ImageViewModel

/**
 * This is the single image viewing activity which displays image and comment associated with it if
 * it exists.
 */
class ImageActivity : AppCompatActivity() {
    lateinit private var mImageView: ImageView
    lateinit private var mEditText: EditText
    lateinit private var mSubmitButton: Button
    lateinit private var mComment: TextView
    private var image: ImageItem? = null
    lateinit private var mImageViewModel: ImageViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)
        mImageView = findViewById(R.id.image)
        mEditText = findViewById(R.id.edit_text)
        mSubmitButton = findViewById(R.id.submitButton)
        mComment = findViewById(R.id.comment)
        setSupportActionBar(findViewById<View>(R.id.toolbar) as Toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        mImageViewModel = ViewModelProviders.of(this).get(ImageViewModel::class.java)
        incomingIntent
        if (image != null && image!!.imageUrl != null) {
            Glide.with(this)
                    .load(image!!.imageUrl)
                    .into(mImageView)
        }
        mSubmitButton.setOnClickListener {
            val inputComment = mEditText.getText().toString()
            if (!inputComment.isEmpty()) {
                mComment.setText(inputComment)
                image!!.comment = inputComment
                mImageViewModel.saveImageItem(image)
                mEditText.getText()?.clear()
                hideKeyboard(this@ImageActivity)
            }
        }
    }

    private val incomingIntent: Unit
        get() {
            if (intent.hasExtra("image")) {
                image = intent.getParcelableExtra("image")
                title = image!!.title
                subscribeObservers(image!!.id)
            }
        }

    private fun subscribeObservers(imageId: String) {
        mImageViewModel.getImageFromDb(imageId).observe(this, Observer { imageItem ->
            if (imageItem != null) {
                image!!.comment = imageItem.comment
                mComment.text = image!!.comment
            } else {
                mImageViewModel.saveImageItem(image)
            }
        })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                // API 5+ solution
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    companion object {
        private const val TAG = "ImageActivity"
        fun hideKeyboard(activity: Activity) {
            val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            var view = activity.currentFocus
            if (view == null) {
                view = View(activity)
            }
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}