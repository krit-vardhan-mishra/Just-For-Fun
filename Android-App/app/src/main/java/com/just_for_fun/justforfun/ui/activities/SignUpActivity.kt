package com.just_for_fun.justforfun.ui.activities

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.just_for_fun.justforfun.databinding.ActivitySignUpBinding
import com.just_for_fun.justforfun.ui.activities.viewmodel.SignUpViewModel
import com.just_for_fun.justforfun.util.delegates.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class SignUpActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivitySignUpBinding::inflate)
    private val viewModel: SignUpViewModel by viewModel()

    companion object {
        private val PICK_IMAGE_REQUEST = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        val signUpClickable = SpannableString("If you have an ID, Login then")

        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                startActivity(Intent(this@SignUpActivity, LoginActivity::class.java))
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.color = Color.BLUE
                ds.isUnderlineText = true
            }
        }

        signUpClickable.setSpan(clickableSpan, 19, 24, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.tvLogin.text = signUpClickable
        binding.tvLogin.movementMethod = LinkMovementMethod.getInstance()
        binding.imageSelector.setOnClickListener {
            openGallery()
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            val selectedImageUri: Uri? = data.data
            binding.imageSelector.setImageURI(selectedImageUri)
        }
    }
}
