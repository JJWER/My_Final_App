package th.ac.kku.final17

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import th.ac.kku.final17.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // กำหนด layout และผูกตัวแปรของ View Binding
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // สร้าง instance ของ FirebaseAuth
        firebaseAuth = FirebaseAuth.getInstance()

        // กำหนด event listener ให้กับ TextView เพื่อเปลี่ยนไปยังหน้า Sign In
        binding.textView.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }

        // กำหนด event listener ให้กับปุ่ม Sign Up
        binding.button.setOnClickListener {
            val email = binding.emailEt.text.toString()
            val pass = binding.passET.text.toString()
            val confirmPass = binding.confirmPassEt.text.toString()

            // ตรวจสอบว่าไม่มีช่องว่างในอีเมลและรหัสผ่าน
            if (email.isNotEmpty() && pass.isNotEmpty() && confirmPass.isNotEmpty()) {
                // ตรวจสอบว่ารหัสผ่านและการยืนยันรหัสผ่านตรงกันหรือไม่
                if (pass == confirmPass) {
                    // ใช้ Firebase Authentication เพื่อสร้างบัญชีผู้ใช้ใหม่
                    firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener {
                        if (it.isSuccessful) {
                            // เมื่อสร้างบัญชีผู้ใช้สำเร็จ ให้เปิด SignInActivity เพื่อเข้าสู่ระบบ
                            val intent = Intent(this, SignInActivity::class.java)
                            startActivity(intent)
                        } else {
                            // แสดงข้อความข้อผิดพลาดหากการสร้างบัญชีผู้ใช้ล้มเหลว
                            Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    // แสดงข้อความเตือนเมื่อรหัสผ่านและการยืนยันรหัสผ่านไม่ตรงกัน
                    Toast.makeText(this, "รหัสผ่านไม่ตรงกัน", Toast.LENGTH_SHORT).show()
                }
            } else {
                // แสดงข้อความเตือนเมื่อมีช่องว่างในการป้อนข้อมูล
                Toast.makeText(this, "กรุณากรอกข้อมูลให้ครบทุกช่อง", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
