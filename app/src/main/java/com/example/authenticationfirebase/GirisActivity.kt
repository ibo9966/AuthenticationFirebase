package com.example.authenticationfirebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.example.authenticationfirebase.databinding.ActivityGirisBinding
import com.google.firebase.auth.FirebaseAuth

class GirisActivity : AppCompatActivity() {
    lateinit var binding:ActivityGirisBinding

    lateinit var auth:FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityGirisBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        //Kullanıcının oturum açıp açmadığını kontrol edelim.
        var  currentUser = auth.currentUser
        if (currentUser !=null){
            intent=Intent(this@GirisActivity,ProfileActivity::class.java)
            startActivity(intent)
            finish()
        }

        //giriş yap butonuna tıklandığında
        binding.girisyapbutton.setOnClickListener {
            var girisemail = binding.girisemail.text.toString()
            var girisparola = binding.girisparola.text.toString()
            if (TextUtils.isEmpty(girisemail)){
                binding.girisemail.error = "Lütfen email adresinizi yazınız."
                return@setOnClickListener
            }else if (TextUtils.isEmpty(girisparola)){
                binding.girisemail.error = "Lütfen parolanızı yazınız."
                return@setOnClickListener
            }

            // Giriş bilgilerimizi doğrulayıp giriş yapıyoruz.
            auth.signInWithEmailAndPassword(girisemail,girisparola)
                .addOnCompleteListener(this){
                    if (it.isSuccessful){
                        intent= Intent(this@GirisActivity,ProfileActivity::class.java)
                        startActivity(intent)
                        finish()
                    }else{
                        Toast.makeText(this,"Giriş Hatalı lütfen tekrar deneyiniz.",Toast.LENGTH_SHORT).show()
                    }
                }
        }
        // Yeni üyelik sayfasına gitmek için
        binding.girisyeniuyelik.setOnClickListener {
            intent= Intent(this@GirisActivity,UyeActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Parolamı unuttum sayfasına gitmek için
        binding.girisparolaunuttum.setOnClickListener {
            intent= Intent(this@GirisActivity,PsifirlaActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}