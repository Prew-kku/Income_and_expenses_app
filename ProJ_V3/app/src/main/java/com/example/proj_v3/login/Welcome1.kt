package com.example.proj_v3.login


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.proj_v3.R

class Welcome1 : AppCompatActivity() {
    var btnnext: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_welcome1)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.txt)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        init()
        btnnext!!.setOnClickListener{
            var intent = Intent(this, Welcome2::class.java)
            startActivity(intent)

        }

    }

    private fun init() {
        btnnext = findViewById(R.id.btn)
    }

}