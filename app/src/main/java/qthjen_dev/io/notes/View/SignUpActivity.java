package qthjen_dev.io.notes.View;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import qthjen_dev.io.notes.Model.UserName;
import qthjen_dev.io.notes.Presenter.FetchAllUserPresenter;
import qthjen_dev.io.notes.Presenter.SignUpPresenter;
import qthjen_dev.io.notes.R;
import qthjen_dev.io.notes.Utils.NetworkUtils.ApiClient;
import qthjen_dev.io.notes.Utils.NetworkUtils.ApiService;
import qthjen_dev.io.notes.Utils.WidgetUtils;
import qthjen_dev.io.notes.View.ViewInterface.FetchAllUserViewInterface;
import qthjen_dev.io.notes.View.ViewInterface.SignUpViewInterface;

public class SignUpActivity extends AppCompatActivity implements SignUpViewInterface, FetchAllUserViewInterface {

    @BindView(R.id.create)
    TextView mSignUp;
    @BindView(R.id.usernameCreate)
    EditText mUser;
    @BindView(R.id.passCreate)
    EditText mPass;
    @BindView(R.id.cancelSignUp)
    ImageView mFabCancel;
    @BindView(R.id.signUpActivity)
    RelativeLayout mRootView;
    @BindView(R.id.signUpProgress)
    ProgressBar mSignUpProgress;

    private Snackbar mSnackbar;

    private SignUpPresenter signUpPresenter;
    private FetchAllUserPresenter fetchAllUserPresenter;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);

        apiService = ApiClient.getClient(this).create(ApiService.class);
        signUpPresenter = new SignUpPresenter(this, apiService);
        fetchAllUserPresenter = new FetchAllUserPresenter(this, apiService);

        mSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSignUpProgress.setVisibility(View.VISIBLE);
                fetchAllUserPresenter.fetchAllUser();
            }
        });
        mFabCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.stay, R.anim.slide_left_to_right);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSignUpProgress.setVisibility(View.INVISIBLE);
    }

    @Override
    public void success(String s) {
        mSignUpProgress.setVisibility(View.VISIBLE);
        // if success => return to signIn activity
        finish();
        overridePendingTransition(R.anim.stay, R.anim.slide_left_to_right);
    }

    @Override
    public void error(String e) {
        mSignUpProgress.setVisibility(View.INVISIBLE);
        WidgetUtils.setupSnackBar(this, mRootView, R.string.signuperror, mSnackbar, R.color.snackbartext);
    }

    @Override
    public void checkUser(ArrayList<UserName> listUser) {
        /** check user-name already exists **/
        boolean checkUser = true; // user-name not exists
        for (int i = 0; i < listUser.size(); i++) {
            if (mUser.getText().toString().trim().equals(listUser.get(i).getUser())) {
                checkUser = false; // user-name already exists
            }
        }

        if (checkUser) {
            if (!mUser.getText().toString().trim().equals("") && !mPass.getText().toString().trim().equals("")) {
                if (mPass.getText().toString().trim().length() > 7 && mUser.getText().toString().trim().length() > 1) {
                    signUpPresenter.signUp(mUser.getText().toString().trim()
                            , mPass.getText().toString().trim());
                } else {
                    mSignUpProgress.setVisibility(View.INVISIBLE);
                    WidgetUtils.setupSnackBar(this, mRootView, R.string.pass8, mSnackbar, R.color.snackbartext);
                }
            } else {
                mSignUpProgress.setVisibility(View.INVISIBLE);
                WidgetUtils.setupSnackBar(this, mRootView, R.string.enteruser, mSnackbar, R.color.snackbartext);
            }
        } else {
            mSignUpProgress.setVisibility(View.INVISIBLE);
            WidgetUtils.setupSnackBar(this, mRootView, R.string.existsuser, mSnackbar, R.color.snackbartext);
        }
    }
}
