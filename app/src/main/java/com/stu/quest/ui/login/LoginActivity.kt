package com.stu.quest.ui.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.stu.quest.databinding.ActivityLoginBinding
import com.stu.quest.databinding.FragmentRegisterBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)

    }
}