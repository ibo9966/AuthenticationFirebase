package com.example.authenticationfirebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.authenticationfirebase.databinding.ActivityProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ProfileActivity : AppCompatActivity() {
    lateinit var binding: ActivityProfileBinding

    private lateinit var auth: FirebaseAuth
    var databaseReference:DatabaseReference?=null
    var database: FirebaseDatabase?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth=FirebaseAuth.getInstance()
        database=FirebaseDatabase.getInstance()
        databaseReference=database?.reference!!.child("profile")

        var currentUser = auth.currentUser
        binding.profilemail.text = "Email: ${currentUser?.email}"

        //realtime - database'deki id'ye ulaşıp altındaki child'ların içindeki veriyi sayfaya aktarıyorum.
        var userReference = databaseReference?.child(currentUser?.uid!!)
        userReference?.addValueEventListener(object  : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                binding.profiladsoyad.text = "Tam adınız:" +snapshot.child("adisoyadi").value.toString()
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

    // Çıkış Yap butonu
        binding.profilecikisyapbutton.setOnClickListener {
            auth.signOut()
            val intent=Intent(this@ProfileActivity,GirisActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Hesabımı sil butonu
        binding.profilhesabimisilbutton.setOnClickListener {
           currentUser?.delete()?.addOnCompleteListener {
               if (it.isSuccessful){
                   Toast.makeText(this,"Hesabınız Silindi.",Toast.LENGTH_SHORT).show()
                   auth.signOut()
                   val intent=Intent(this@ProfileActivity,GirisActivity::class.java)
                   startActivity(intent)
                   finish()

                   var currentUserDb = currentUser?.let { it1 -> databaseReference?.child(it1.uid) }
                   currentUserDb?.removeValue()
                   Toast.makeText(this,"Adı soyadı silindi.",Toast.LENGTH_SHORT).show()
               }
           }
        }

        //  Güncelle  sayfasına geçiş ediliyor
        binding.profilbilgilerimiguncellebutton.setOnClickListener {
            val intent=Intent(this@ProfileActivity,GuncelleActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}