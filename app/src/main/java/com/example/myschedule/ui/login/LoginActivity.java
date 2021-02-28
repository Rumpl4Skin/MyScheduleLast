package com.example.myschedule.ui.login;

import android.app.Activity;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myschedule.DbHelper;
import com.example.myschedule.MainActivity;
import com.example.myschedule.R;
import com.example.myschedule.data.model.LoggedInUser;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static java.security.AccessController.getContext;

public class LoginActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;
    //Переменная для работы с БД
    private DbHelper mDBHelper;
    private SQLiteDatabase mDb;
    public EditText usernameEditText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);

        mDBHelper = new DbHelper(this);

        try {
            mDBHelper.updateDataBase();
        } catch (IOException mIOException) {
            throw new Error("UnableToUpdateDatabase");
        }

        try {
            mDb = mDBHelper.getWritableDatabase();
        } catch (SQLException mSQLException) {
            throw mSQLException;
        }

        usernameEditText = findViewById(R.id.mail);
        final EditText passwordEditText = findViewById(R.id.password);
        final AutoCompleteTextView groupEditText = findViewById(R.id.group);
        final EditText fioEditText = findViewById(R.id.user_fio);
        final Button loginButton = findViewById(R.id.login);
        final Button registrButton = findViewById(R.id.registr);
        final ProgressBar loadingProgressBar = findViewById(R.id.loading);
        final CheckBox checkBox = findViewById(R.id.checkPass);

        List<String> namesList = Arrays.asList(mDBHelper.getAllGroupName());
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_dropdown_item_1line, namesList);
        groupEditText.setAdapter(adapter);
        groupEditText.setThreshold(1);

        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                loginButton.setEnabled(loginFormState.isDataValid());
                if (loginFormState.getUsernameError() != null) {
                    usernameEditText.setError(getString(loginFormState.getUsernameError()));
                }
                if (loginFormState.getPasswordError() != null) {
                    passwordEditText.setError(getString(loginFormState.getPasswordError()));
                }
            }
        });

        loginViewModel.getLoginResult().observe(this, new Observer<LoginResult>() {
            @Override
            public void onChanged(@Nullable LoginResult loginResult) {
                if (loginResult == null) {
                    return;
                }
                loadingProgressBar.setVisibility(View.GONE);
                if (loginResult.getError() != null) {
                    showLoginFailed(loginResult.getError());
                }
                if (loginResult.getSuccess() != null) {
                    updateUiWithUser(loginResult.getSuccess());
                }
                setResult(Activity.RESULT_OK);

                //Complete and destroy login activity once successful
                //finish();
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    loginViewModel.login(usernameEditText.getText().toString(),
                            passwordEditText.getText().toString());
                }
                return false;
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoggedInUser user=new LoggedInUser(usernameEditText.getText().toString(),passwordEditText.getText().toString());
                if(mDBHelper.userIsExist(user)&&mDBHelper.passIsRight(user)){
                Toast.makeText(getApplicationContext(), "Успешная авторизация!", Toast.LENGTH_SHORT).show();
                loadingProgressBar.setVisibility(View.VISIBLE);
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
// передача объекта с ключом "hello" и значением "Hello World"
                    intent.putExtra("user", usernameEditText.getText().toString());
                loginViewModel.login(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());

                }
                else  Toast.makeText(getApplicationContext(), "Неверный логин и(или пароль)!", Toast.LENGTH_SHORT).show();
            }
        });

        registrButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoggedInUser user=new LoggedInUser(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
                if(!mDBHelper.userIsExist(user)){//пользователь не существует
                    if(groupEditText.getVisibility()==View.GONE){//ввод группы
                        groupEditText.setVisibility(View.VISIBLE);
                        fioEditText.setVisibility(View.VISIBLE);
                        Toast.makeText(getApplicationContext(), "Введите название группы и фио", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        if(groupEditText.getText().toString()==""&&passwordEditText.getText().toString()==""&&fioEditText.getText().toString()=="")
                            Toast.makeText(getApplicationContext(), "Не все поля регистрации заполнены", Toast.LENGTH_LONG).show();
                        else{
                            mDBHelper.addUser(new LoggedInUser(fioEditText.getText().toString(),
                                    usernameEditText.getText().toString(),
                                passwordEditText.getText().toString(),
                                groupEditText.getText().toString()));
                        Toast.makeText(getApplicationContext(), "Регистрация успешна! Повторно введите пароль", Toast.LENGTH_LONG).show();
                        registrButton.setVisibility(View.INVISIBLE);
                        passwordEditText.setText("");}
                    }

                }
                    else Toast.makeText(getApplicationContext(), "Указанный пользователь существует", Toast.LENGTH_SHORT).show();
            }
        });

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                   passwordEditText.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                } else {
                    passwordEditText.setInputType(129);
                }
            }
        });
    }

    private void updateUiWithUser(LoggedInUserView model) {
        String welcome = getString(R.string.welcome) + model.getDisplayName();
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.putExtra("user", usernameEditText.getText().toString());
        mDBHelper.close();
        startActivity(intent);

    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }
}