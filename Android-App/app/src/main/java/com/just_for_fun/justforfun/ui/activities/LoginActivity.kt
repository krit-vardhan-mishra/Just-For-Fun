package com.just_for_fun.justforfun.ui.activities

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.just_for_fun.justforfun.databinding.ActivityLoginBinding
import com.just_for_fun.justforfun.ui.activities.viewmodel.LoginViewModel
import com.just_for_fun.justforfun.util.delegates.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : AppCompatActivity() {
    private val binding by viewBinding(ActivityLoginBinding::inflate)
    private val viewModel: LoginViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        val signUpClickable = SpannableString("If you don't have an ID, Sign Up then")

        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                startActivity(Intent(this@LoginActivity, SignUpActivity::class.java))
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.color = Color.BLUE
                ds.isUnderlineText = true
            }
        }

        signUpClickable.setSpan(clickableSpan, 24, 32, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.tvSignUp.text = signUpClickable
        binding.tvSignUp.movementMethod = LinkMovementMethod.getInstance()

        binding.btnSubmit.setOnClickListener {
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            Toast.makeText(this@LoginActivity, "Hello, ${binding.emailInputLogin.text}", Toast.LENGTH_SHORT).show()
        }
    }
}
