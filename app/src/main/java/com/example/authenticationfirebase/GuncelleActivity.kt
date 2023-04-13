package com.example.authenticationfirebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.authenticationfirebase.databinding.ActivityGuncelleBinding
import com.example.authenticationfirebase.databinding.ActivityUyeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class GuncelleActivity : AppCompatActivity() {

    lateinit var binding: ActivityGuncelleBinding

    private lateinit var auth: FirebaseAuth
    var databaseReference: DatabaseReference?=null
    var database: FirebaseDatabase?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGuncelleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        databaseReference = database?.reference!!.child("profile")

        var currentUser = auth.currentUser
        binding.guncelleemail.setText(currentUser?.email)

        //realtime-database de bulunan kullanının id'sine erişip adını soyadını alalım.
        var userReference = databaseReference?.child(currentUser?.uid!!)
        userReference?.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                binding.guncelleadsoyad.setText(snapshot.child("adisoyadi").value.toString())
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

        //Bilgilerimi Güncelle button aktif ediliyor
        binding.guncellebilgilerimibutton.setOnClickListener {
            var guncelleemail = binding.guncelleemail.text.toString().trim()
            currentUser!!.updateEmail(guncelleemail)
                .addOnCompleteListener { task->
                    if (task.isSuccessful){
                        Toast.makeText(this,"Email güncellendi.",Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(this,"Email güncellemesi başarısız.",Toast.LENGTH_SHORT).show()

                    }

                }
            //Parola güncelleme
            var guncelleparola = binding.guncelleparola.text.toString().trim()

            currentUser?.updatePassword(guncelleparola ?: "")
                ?.addOnCompleteListener { task ->
                    if (task.isSuccessful){
                        Toast.makeText(this,"Parola güncellendi.",Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(this,"Parola güncellemesi başarısız.",Toast.LENGTH_SHORT).show()
                    }

                }

            //Ad soyad güncelleme
            var currentUserDb = currentUser?.let { it1 -> databaseReference?.child(it1.uid) }
            currentUserDb?.removeValue()
            currentUserDb?.child("adisoyadi")?.setValue(binding.guncelleadsoyad.text.toString())
            Toast.makeText(this,"Ad- Soyad güncellendi",Toast.LENGTH_SHORT).show()
        }

        //Giriş sayfasına gitmek için
        binding.guncellegirisyapbutton.setOnClickListener {
            intent= Intent(this@GuncelleActivity,GirisActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
}