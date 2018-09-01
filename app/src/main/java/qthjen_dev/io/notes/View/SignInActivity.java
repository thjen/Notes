package qthjen_dev.io.notes.View;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import qthjen_dev.io.notes.Model.User;
import qthjen_dev.io.notes.Presenter.SignInPresenter;
import qthjen_dev.io.notes.R;
import qthjen_dev.io.notes.Utils.FinalString;
import qthjen_dev.io.notes.Utils.NetworkUtils.ApiClient;
import qthjen_dev.io.notes.Utils.NetworkUtils.ApiService;
import qthjen_dev.io.notes.Utils.NetworkUtils.NetworkStateChange;
import qthjen_dev.io.notes.Utils.WidgetUtils;
import qthjen_dev.io.notes.View.ViewInterface.SignInViewInterface;

public class SignInActivity extends AppCompatActivity implements SignInViewInterface {

    @BindView(R.id.signUp)
    TextView mSignUp;
    @BindView(R.id.signIn)
    TextView mSignIn;
    @BindView(R.id.username)
    EditText mUser;
    @BindView(R.id.pass)
    EditText mPass;
    @BindView(R.id.signInActivity)
    RelativeLayout mRootView;
    @BindView(R.id.cardLogin)
    CardView mCardLogin;
    @BindView(R.id.tvTemp1)
    TextView tvTemp1;
    @BindView(R.id.tvTemp2)
    TextView tvTemp2;
    @BindView(R.id.reload)
    CardView mReload;
    @BindView(R.id.loginProgress)
    ProgressBar mLoginProgress;

    private Snackbar mSnackbar;

    private SignInPresenter signInPresenter;
    private ApiService apiService;

    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        ButterKnife.bind(this);

        mSharedPreferences = getSharedPreferences(FinalString.USER_SAVED, MODE_PRIVATE);

        /** TODO: checknetwork **/
        if (NetworkStateChange.CheckNetwork(this)) {
            mCardLogin.setVisibility(View.VISIBLE);
            tvTemp1.setText(getResources().getString(R.string.hithere));
            tvTemp2.setText(getResources().getString(R.string.wellcome));
            mSignUp.setVisibility(View.VISIBLE);
            mReload.setVisibility(View.INVISIBLE);
        } else {
            mCardLogin.setVisibility(View.INVISIBLE);
            tvTemp1.setText(getResources().getString(R.string.networkerror));
            tvTemp2.setText(getResources().getString(R.string.turnonnetwork));
            mSignUp.setVisibility(View.INVISIBLE);
            mReload.setVisibility(View.VISIBLE);
        }

        IntentFilter intentFilter = new IntentFilter(NetworkStateChange.NETWORK_AVAILABLE_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                boolean isConnectNetwork = intent.getBooleanExtra(NetworkStateChange.IS_NETWORK_AVAILABLE, false);
                if (!isConnectNetwork) {
                    mCardLogin.setVisibility(View.INVISIBLE);
                    tvTemp1.setText(getResources().getString(R.string.networkerror));
                    tvTemp2.setText(getResources().getString(R.string.turnonnetwork));
                    mSignUp.setVisibility(View.INVISIBLE);
                    mReload.setVisibility(View.VISIBLE);
                } else {
                    mCardLogin.setVisibility(View.VISIBLE);
                    tvTemp1.setText(getResources().getString(R.string.hithere));
                    tvTemp2.setText(getResources().getString(R.string.wellcome));
                    mSignUp.setVisibility(View.VISIBLE);
                    mReload.setVisibility(View.INVISIBLE);
                }
            }
        }, intentFilter);

        mReload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(0,0);
                startActivity(getIntent());
                overridePendingTransition(0,0);
            }
        });

        apiService = ApiClient.getClient(this).create(ApiService.class);
        signInPresenter = new SignInPresenter(this, apiService);

        mSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
                overridePendingTransition(R.anim.slide_right_to_left, R.anim.stay);
            }
        });

        mEditor = mSharedPreferences.edit();
        mSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLoginProgress.setVisibility(View.VISIBLE);
                signInPresenter.signIn(mUser.getText().toString().trim()
                , mPass.getText().toString().trim());
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mLoginProgress.setVisibility(View.INVISIBLE);

        String user = mSharedPreferences.getString("usersaved", "");
        String pass = mSharedPreferences.getString("passsaved", "");
        mUser.setText(user.trim());
        mPass.setText(pass.trim());
    }

    @Override
    public void success(ArrayList<User> listUser) {
        mLoginProgress.setVisibility(View.INVISIBLE);
        // save user
        mEditor.putString("usersaved", listUser.get(0).getEmail());
        mEditor.putString("idsaved", listUser.get(0).getId());
        mEditor.putString("passsaved", listUser.get(0).getPass());
        mEditor.commit();

        mLoginProgress.setVisibility(View.VISIBLE);
        // if success then start home activity and send 1 info user
        Intent intent = new Intent(SignInActivity.this, MainActivity.class);
        intent.putExtra("user", listUser.get(0).getEmail());
        startActivity(intent);
        overridePendingTransition(R.anim.slide_right_to_left, R.anim.stay);
    }

    @Override
    public void error(String e) {
        mLoginProgress.setVisibility(View.INVISIBLE);
        WidgetUtils.setupSnackBar(this, mRootView, R.string.signinerror, mSnackbar, R.color.snackbartext);
    }
}
