package com.stu.quest.ui.login

import android.app.Activity
import android.content.Intent
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.Toast
import androidx.core.widget.addTextChangedListener

import com.stu.quest.R
import com.stu.quest.databinding.FragmentRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var binding: FragmentRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

     binding = FragmentRegisterBinding.inflate(layoutInflater)
     setContentView(binding.root)

        val username = binding.username
        val password = binding.password
        val register = binding.register
        val loading = binding.loading

        loginViewModel = ViewModelProvider(this, LoginViewModelFactory(application))
            .get(LoginViewModel::class.java)

        loginViewModel.loginFormState.observe(this@RegisterActivity, Observer {
            val loginState = it ?: return@Observer

            // disable login button unless both username / password is valid
            register.isEnabled = loginState.isDataValid

            if (loginState.usernameError != null) {
                username.error = getString(loginState.usernameError)
            }
            if (loginState.passwordError != null) {
               password.error = getString(loginState.passwordError)
            }
        })

        loginViewModel.registerResult.observe(this@RegisterActivity, Observer {
            val loginResult = it ?: return@Observer

            loading.visibility = View.GONE
            if (loginResult.errorMessage != null) {
                showLoginFailed(loginResult.errorMessage)
                startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                finish()
            }

            if (loginResult.success != null) {
                updateUiWithUser(loginResult.success)

                setResult(Activity.RESULT_OK)
                //Complete and destroy login activity once successful
                finish()
            }

        })

        username.afterTextChanged {
            loginViewModel.loginDataChanged(
                username.text.toString(),
                password.text.toString()
            )
        }

        password.apply {
            afterTextChanged {
                loginViewModel.loginDataChanged(
                    username.text.toString(),
                    password.text.toString()
                )
            }

            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        loginViewModel.register(
                            username.text.toString(),
                            password.text.toString()
                        )
                }
                false
            }

            register.setOnClickListener {
                if(binding.password.text.toString() ==  binding.confirmPassword.text.toString()) {
                    loading.visibility = View.VISIBLE
                    loginViewModel.register(username.text.toString(), password.text.toString())
                }
            }
        }

        binding.confirmPassword.addTextChangedListener { text ->
            if(binding.password.text?.toString() != text?.toString()){
                binding.tvPasswordError.visibility = View.VISIBLE
            }else{
                binding.tvPasswordError.visibility = View.GONE
            }
        }
    }

    private fun updateUiWithUser(model: LoggedInUserView) {
        val welcome = getString(R.string.welcome)
        val displayName = model.displayName
        // TODO : initiate successful logged in experience
        Toast.makeText(
            applicationContext,
            "$welcome $displayName",
            Toast.LENGTH_LONG
        ).show()
    }

    private fun showLoginFailed(errorString: String) {
        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
    }
}

/**
 * Extension function to simplify setting an afterTextChanged action to EditText components.
 */
fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}