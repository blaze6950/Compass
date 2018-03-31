package zna.online.compass;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class SignUpEmailActivity extends AppCompatActivity implements View.OnClickListener{

    private AutoCompleteTextView emailAutoCompleteTextView;
    private EditText passwordEditText;
    private Button signupButton;
    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_email);

        firebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);
        emailAutoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.email);
        passwordEditText = (EditText) findViewById(R.id.password);
        signupButton = (Button) findViewById(R.id.email_sign_in_button);

        signupButton.setOnClickListener(this);
    }

    private void registerUser()
    {
        String email = emailAutoCompleteTextView.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (TextUtils.isEmpty(email))
        {
            //email is empty
            Toast.makeText(this, "Please enter the correct email", Toast.LENGTH_SHORT).show();
            //stopping the function executing
            return;
        }

        if (TextUtils.isEmpty(password))
        {
            //password is empty
            Toast.makeText(this, "Please enter the correct password", Toast.LENGTH_SHORT).show();
            //stopping the function executing
            return;
        }
        //if validating ok
        //we will first show a progressDialog
        progressDialog.setMessage("Sign up...");
        progressDialog.show();

    }

    @Override
    public void onClick(View v) {
        if (v == signupButton)
        {
            registerUser();
        }
    }
}
