package qthjen_dev.io.notes.View.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import qthjen_dev.io.notes.Presenter.ChangePassPresenter;
import qthjen_dev.io.notes.R;
import qthjen_dev.io.notes.Utils.FinalString;
import qthjen_dev.io.notes.Utils.NetworkUtils.ApiClient;
import qthjen_dev.io.notes.Utils.NetworkUtils.ApiService;
import qthjen_dev.io.notes.Utils.WidgetUtils;
import qthjen_dev.io.notes.View.MainActivity;
import qthjen_dev.io.notes.View.ViewInterface.ChangePassViewInterface;

public class SettingFragment extends Fragment implements ChangePassViewInterface {

    @BindView(R.id.changePassLayout)
    RelativeLayout mChangePassLayout;
    @BindView(R.id.til_changePass)
    TextInputLayout mTilPass;
    @BindView(R.id.btChangePass)
    RelativeLayout mBtChangePass;
    @BindView(R.id.btDone)
    TextView mDone;
    @BindView(R.id.arrow)
    ImageView mArrow;
    @BindView(R.id.identity)
    TextView mIdentity;
    @BindView(R.id.signOut)
    CardView mSignOut;
    @BindView(R.id.settingFragment)
    RelativeLayout mRootView;

    private Snackbar mSnackBar;
    private boolean CHANGE_PASS_LAYOUT_VISIBLE = true;

    private ChangePassPresenter changePassPresenter;
    private ApiService apiService;

    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;

    public SettingFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mSharedPreferences = getContext().getSharedPreferences(FinalString.USER_SAVED, Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();

        apiService = ApiClient.getClient(getContext()).create(ApiService.class);
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        ButterKnife.bind(this, view);

        final String username = getArguments().getString("userToChangePass");
        changePassPresenter = new ChangePassPresenter(this, apiService);

        mIdentity.setText(getResources().getString(R.string.username) + ": " + username);

        mBtChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CHANGE_PASS_LAYOUT_VISIBLE) {
                    mChangePassLayout.setVisibility(View.VISIBLE);
                    mArrow.setImageDrawable(getResources().getDrawable(R.drawable.arrow_up));
                    CHANGE_PASS_LAYOUT_VISIBLE = false;
                } else {
                    mChangePassLayout.setVisibility(View.INVISIBLE);
                    mArrow.setImageDrawable(getResources().getDrawable(R.drawable.arrow_down));
                    CHANGE_PASS_LAYOUT_VISIBLE = true;
                }
            }
        });

        mDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTilPass.getEditText().getText().toString().trim().length() > 7) {
                    changePassPresenter.changePass(username, mTilPass.getEditText().getText().toString().trim());
                    mEditor.putString("passsaved", mTilPass.getEditText().getText().toString().trim());
                    mEditor.commit();
                    WidgetUtils.hideKeyboard(getActivity());
                } else {
                    WidgetUtils.setupSnackBar(getActivity(), mRootView, R.string.pass8, mSnackBar, R.color.snackbartext);
                }
            }
        });

        mSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                //fragmentTransaction.remove(SettingFragment.this).commit();
                mEditor.clear();
                mEditor.commit();
                getActivity().finish();
            }
        });

        return view;
    }

    @Override
    public void success() {
        WidgetUtils.setupSnackBar(getActivity(), mRootView, R.string.successchange, mSnackBar, R.color.snackbartext);

        /** TODO: setIcon menu in activity **/
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.menu.getItem(1).setIcon(ContextCompat.getDrawable(mainActivity, R.drawable.accountsetting));
        mainActivity.SHOW_SETTING_FRAGMENT = true;
        mainActivity.mContainerRecyclerView.setVisibility(View.VISIBLE);

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.remove(this).commit();
    }

    @Override
    public void error() {
        //Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
    }
}
