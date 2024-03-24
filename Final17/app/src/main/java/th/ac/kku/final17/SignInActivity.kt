package th.ac.kku.final17

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import th.ac.kku.final17.databinding.ActivitySignInBinding

class SignInActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignInBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        // กำหนดฟังก์ชันสำหรับการคลิกที่ TextView "Sign Up"
        binding.textView.setOnClickListener {
            // เมื่อคลิก TextView "Sign Up" จะเปิด SignUpActivity
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        // กำหนดฟังก์ชันสำหรับการคลิกที่ปุ่ม Sign In
        binding.button.setOnClickListener {
            // ดึงอีเมลและรหัสผ่านจากช่องกรอกข้อมูล
            val email = binding.emailEt.text.toString()
            val pass = binding.passET.text.toString()

            // ตรวจสอบว่าอีเมลและรหัสผ่านไม่ว่างเปล่า
            if (email.isNotEmpty() && pass.isNotEmpty()) {
                // เข้าสู่ระบบด้วยอีเมลและรหัสผ่าน
                firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // หากเข้าสู่ระบบสำเร็จ เปิด MainActivity
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish() // ปิด SignInActivity เพื่อป้องกันผู้ใช้ไม่สามารถย้อนกลับได้
                    } else {
                        // หากเข้าสู่ระบบล้มเหลว แสดงข้อความแสดงข้อผิดพลาด
                        Toast.makeText(this, task.exception.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                // หากอีเมลหรือรหัสผ่านว่างเปล่า แสดงข้อความแสดงข้อผิดพลาด
                Toast.makeText(this, "กรุณากรอกข้อมูลให้ครบทุกช่อง!!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
