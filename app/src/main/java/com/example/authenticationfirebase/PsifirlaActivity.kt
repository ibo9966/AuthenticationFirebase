package com.example.authenticationfirebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.example.authenticationfirebase.databinding.ActivityProfileBinding
import com.example.authenticationfirebase.databinding.ActivityPsifirlaBinding
import com.google.firebase.auth.FirebaseAuth

class PsifirlaActivity : AppCompatActivity() {

    lateinit var binding: ActivityPsifirlaBinding
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityPsifirlaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.psifirlamabutton.setOnClickListener {
            var psifirlaemail = binding.psifirlaemail.text.toString().trim()
            if (TextUtils.isEmpty(psifirlaemail)) {
                binding.psifirlaemail.error = "Lütfen e-mail adresinizi yazınız."
            } else {
                auth.sendPasswordResetEmail(psifirlaemail)
                    .addOnCompleteListener(this) { sifirlama ->
                        if (sifirlama.isSuccessful) {
                            binding.psifirlamesaj.text =
                                "E-mail adresinize sıfırlama bağlantısı gönderildi, lütfen kontrol ediniz."
                        } else {
                            binding.psifirlamesaj.text =
                                "Sıfırlama işlemi başarısız.Tekrar Deneyiniz."
                        }
                    }
            }
        }

        // Giriş sayfasına gitmek için
        binding.psifirlamagirisyapbutton.setOnClickListener {
            intent = Intent(this@PsifirlaActivity, GirisActivity::class.java)
            startActivity(intent)
            finish()


        }
    }
}