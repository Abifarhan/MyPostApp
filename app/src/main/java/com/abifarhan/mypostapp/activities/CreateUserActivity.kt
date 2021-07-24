package com.abifarhan.mypostapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.abifarhan.mypostapp.utils.Constanst.DATE_CREATE
import com.abifarhan.mypostapp.utils.Constanst.USERNAME
import com.abifarhan.mypostapp.utils.Constanst.USER_REF
import com.abifarhan.mypostapp.databinding.ActivityCreateUserBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class CreateUserActivity : AppCompatActivity() {
    private var _binding: ActivityCreateUserBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityCreateUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnCancel.setOnClickListener {
        }

        binding.btnCreateAccountRegister.setOnClickListener {
            val name =  binding.edtUsernameRegister.text.toString().trim()
            val email = binding.edtEmailRegister.text.toString()
            val pw = binding.edtPwRegister.text.toString()

            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email.toString(), pw.toString())
                .addOnSuccessListener {

                    val changeRequest = UserProfileChangeRequest.Builder()
                        .setDisplayName(name.toString())
                        .build()
                    it.user!!.updateProfile(changeRequest)
                        .addOnFailureListener {
                            Log.e("Exception","Can not create new user")
                        }

                    val data = HashMap<String, Any>()
                    data[USERNAME] = name
                    data[DATE_CREATE] =  FieldValue.serverTimestamp()
                    FirebaseFirestore.getInstance().collection(USER_REF)
                        .document(it.user!!.uid)
                        .set(data)
                        .addOnSuccessListener {
                            finish()
                        }
                    Toast.makeText(this, "Anda berhasil melakukan pendaftara", Toast.LENGTH_SHORT)
                        .show()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Anda gagal melakukan pendaftaran", Toast.LENGTH_SHORT).show()
                }
        }
    }
}