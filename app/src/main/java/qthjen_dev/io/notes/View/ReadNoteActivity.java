package qthjen_dev.io.notes.View;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import qthjen_dev.io.notes.Model.Note;
import qthjen_dev.io.notes.Presenter.EditNotePresenter;
import qthjen_dev.io.notes.R;
import qthjen_dev.io.notes.Utils.NetworkUtils.ApiClient;
import qthjen_dev.io.notes.Utils.NetworkUtils.ApiService;
import qthjen_dev.io.notes.Utils.WidgetUtils;
import qthjen_dev.io.notes.View.ViewInterface.EditViewInterface;

public class ReadNoteActivity extends AppCompatActivity implements EditViewInterface {

    @BindView(R.id.note)
    EditText mNote;
    @BindView(R.id.readDone)
    TextView mDone;
    @BindView(R.id.returnHome)
    TextView mBack;
    @BindView(R.id.readRootView)
    RelativeLayout mRootView;

    private Snackbar mSnackbar;

    private EditNotePresenter editNotePresenter;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_note);
        ButterKnife.bind(this);

        mBack.setText(Html.fromHtml("&#9664") + "  " + getResources().getString(R.string.home));
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition( R.anim.stay, R.anim.slide_left_to_right);
            }
        });

        /** TODO: getdata from main **/
        Bundle bundle = getIntent().getBundleExtra("mybundle");
        final Note note = bundle.getParcelable("readnote");
        mNote.setText(note.getNote());
        final String username = getIntent().getExtras().getString("user-name");
        final int position = getIntent().getExtras().getInt("positionitem");

        /** edit note item **/
        apiService = ApiClient.getClient(this).create(ApiService.class);
        editNotePresenter = new EditNotePresenter(this, apiService);
        mDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM d");

                if (!mNote.getText().toString().trim().equals("")) {
                    editNotePresenter.editHandleLogic(Integer.parseInt(note.getNid()), mNote.getText().toString().trim(),
                            simpleDateFormat.format(calendar.getTime()), username, position);
                } else {
                    WidgetUtils.setupSnackBar(ReadNoteActivity.this, mRootView, R.string.emptynote, mSnackbar, R.color.snackbartext);
                }
            }
        });
    }

    @Override
    public void editSuccess(int position) {
        finish();
        overridePendingTransition( R.anim.stay, R.anim.slide_left_to_right);
    }

    @Override
    public void editError(String e) {
        WidgetUtils.setupSnackBar(this, mRootView, R.string.editerror, mSnackbar, R.color.snackbartext);
    }
}
