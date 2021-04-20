package com.example.lendylastestver2.ui.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.lendylastestver2.HomeActivity
import com.example.lendylastestver2.R
import com.example.lendylastestver2.SignUpActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_login.*


const val RC_SIGN_IN = 123

class LoginActivity : AppCompatActivity() {

    private lateinit var mView: View
    private lateinit var viewModel: LoginViewModel
    private lateinit var googleSignInClient : GoogleSignInClient
    private lateinit var loginViewModel: LoginViewModel

    companion object {
        fun newInstance() = LoginActivity()
        const val RC_SIGN_IN =202
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        viewModel.user.observe(this, {
            it?.let { firebaseUser ->
                Log.d("Test login result:", firebaseUser.email ?: "There are no email")
            }
        })
        //viewModel.login(requireActivity(), "test@mail.com", "12345678")
        //viewModel.register(requireActivity(), "test2@mail.com", "12345678")

        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        mView.findViewById<SignInButton>(R.id.signin_button).setOnClickListener{
            signIn()
        }
        Firebase.auth.addAuthStateListener{
            it.currentUser?.let{firebaseUser ->
                Log.d("Login State", firebaseUser.email ?:"There are no email but login")
            } ?: run {
                Log.d("Login state", "There is no user!")
            }
        }

        setContentView(R.layout.activity_login)

        val email = findViewById<EditText>(R.id.email)
        val password = findViewById<EditText>(R.id.password)
        val login = findViewById<Button>(R.id.login)
        val loading = findViewById<ProgressBar>(R.id.loading)
        val signup = findViewById<TextView>(R.id.sign_up)

        loginViewModel = ViewModelProvider(this, LoginViewModelFactory())
                .get(LoginViewModel::class.java)

        loginViewModel.loginFormState.observe(this@LoginActivity, Observer {
            val loginState = it ?: return@Observer

            // disable login button unless both username / password is valid
            login.isEnabled = loginState.isDataValid

            if (loginState.emailError != null) {
                email.error = getString(loginState.emailError)
            }
            if (loginState.passwordError != null) {
                password.error = getString(loginState.passwordError)
            }
        })

        loginViewModel.loginResult.observe(this@LoginActivity, Observer {
            val loginResult = it ?: return@Observer

            loading.visibility = View.GONE
            if (loginResult.error != null) {
                showLoginFailed(loginResult.error)
            }

            if (loginResult.success != null) {

                updateUiWithUser(loginResult.success)

            }
            setResult(Activity.RESULT_OK)

            //Complete and destroy login activity once successful
//            finish()
        })

        email.afterTextChanged {
            loginViewModel.loginDataChanged(
                email.text.toString(),
                password.text.toString()
            )
        }

        password.apply {
            afterTextChanged {
                loginViewModel.loginDataChanged(
                    email.text.toString(),
                    password.text.toString()
                )
            }

            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        loginViewModel.login(
                            email.text.toString(),
                            password.text.toString()
                        )
                }
                false
            }

            login.setOnClickListener {
                loading.visibility = View.VISIBLE
                loginViewModel.login(email.text.toString(), password.text.toString())
                val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                startActivity(intent)
            }

        }

        signup.setOnClickListener {
            val intent = Intent(this@LoginActivity, SignUpActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d("Test google auth", "firebaseAuthWithGoogle" + account.id)
                account.idToken?.let {
                    viewModel.firebaseAuthWithGoogle(this, it)
                }
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w("Test Google auth", "Google sign in failed", e)
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

    private fun showLoginFailed(@StringRes errorString: Int) {
        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
    }


    override fun onStart() {
        super.onStart()

        val account = GoogleSignIn.getLastSignedInAccount(this)
    }

    private fun signIn() {
        val signInIntent: Intent = googleSignInClient.getSignInIntent()
        startActivityForResult(signInIntent, RC_SIGN_IN)
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
