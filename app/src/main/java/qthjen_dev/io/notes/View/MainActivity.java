package qthjen_dev.io.notes.View;

import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.support.v7.widget.SearchView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import qthjen_dev.io.notes.Adapter.NoteAdapter;
import qthjen_dev.io.notes.Utils.OnPressItem;
import qthjen_dev.io.notes.Utils.WidgetUtils;
import qthjen_dev.io.notes.View.Fragment.SettingFragment;
import qthjen_dev.io.notes.Model.Note;
import qthjen_dev.io.notes.Presenter.AddNotePresenter;
import qthjen_dev.io.notes.Presenter.DeleteNotePresenter;
import qthjen_dev.io.notes.Presenter.EditNotePresenter;
import qthjen_dev.io.notes.Presenter.FetchAllDataPresenter;
import qthjen_dev.io.notes.R;
import qthjen_dev.io.notes.Utils.OnPressDelete;
import qthjen_dev.io.notes.Utils.OnPressEdit;
import qthjen_dev.io.notes.Utils.NetworkUtils.ApiClient;
import qthjen_dev.io.notes.Utils.NetworkUtils.ApiService;
import qthjen_dev.io.notes.View.ViewInterface.AddNoteViewInterface;
import qthjen_dev.io.notes.View.ViewInterface.DeleteViewInterface;
import qthjen_dev.io.notes.View.ViewInterface.EditViewInterface;
import qthjen_dev.io.notes.View.ViewInterface.FetchAllDataViewInterface;

public class MainActivity extends AppCompatActivity implements AddNoteViewInterface, FetchAllDataViewInterface
        , OnPressDelete, OnPressEdit, DeleteViewInterface, EditViewInterface, OnPressItem {

    @BindView(R.id.fab_add)
    FloatingActionButton mFabAdd;
    @BindView(R.id.recyclerNote)
    RecyclerView mRecyclerView;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.mainActivity)
    RelativeLayout mRootView;
    @BindView(R.id.mainProgress)
    ProgressBar mProgress;
    @BindView(R.id.containerRecyclerView)
    public RelativeLayout mContainerRecyclerView;

    private Snackbar mSnackbar;
    private String userName;
    private Dialog dialog;
    private AddNotePresenter addNotePresenter;
    private ApiService apiService;

    private FetchAllDataPresenter fetchAllDataPresenter;
    private DeleteNotePresenter deleteNotePresenter;
    private EditNotePresenter editNotePresenter;

    private NoteAdapter mAdapter;

    public boolean SHOW_SETTING_FRAGMENT = true;
    private FragmentManager fragmentManager = getSupportFragmentManager();

    private SearchView mSearchView;
    public Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mProgress.setVisibility(View.VISIBLE);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.home));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        apiService = ApiClient.getClient(this).create(ApiService.class);
        userName = getIntent().getExtras().getString("user");

        fetchAllDataPresenter = new FetchAllDataPresenter(this, apiService);
        addNotePresenter = new AddNotePresenter(this, apiService);
        deleteNotePresenter = new DeleteNotePresenter(this, apiService);
        editNotePresenter = new EditNotePresenter(this, apiService);

        /** TODO: insert note **/
        mFabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new Dialog(MainActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCanceledOnTouchOutside(false);
                dialog.setContentView(R.layout.dialog_add);

                TextView title = dialog.findViewById(R.id.titleDialog);
                CardView mDone = dialog.findViewById(R.id.done);
                final TextInputLayout desc = dialog.findViewById(R.id.til_description);
                CardView cancel = dialog.findViewById(R.id.cancelAdd);

                title.setText(getResources().getString(R.string.addnote));
                mDone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Calendar calendar = Calendar.getInstance();
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM d");
                        if (!desc.getEditText().getText().toString().trim().equals("")) {
                            addNotePresenter.addNote(desc.getEditText().getText().toString().trim(), simpleDateFormat.format(calendar.getTime()), userName);
                        } else {
                            WidgetUtils.setupSnackBar(MainActivity.this, mRootView, R.string.emptynote, mSnackbar, R.color.snackbartext);
                        }
                    }
                });

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });
                dialog.show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchAllDataPresenter.fetchAllData(userName);
    }

    @Override
    public void insertSuccess(String s) {
        WidgetUtils.setupSnackBar(this, mRootView, R.string.insertsuccess, mSnackbar, R.color.snackbartext);

        // re fetch data when data is changed
        fetchAllDataPresenter.fetchAllData(userName);
        dialog.cancel();
    }

    @Override
    public void insertError(String e) {
        WidgetUtils.setupSnackBar(this, mRootView, R.string.inserterror, mSnackbar, R.color.snackbartext);
    }

    @Override
    public void fetchSuccess(ArrayList<Note> list) {
        mProgress.setVisibility(View.INVISIBLE);
        mAdapter = new NoteAdapter(this, list, this, this, this);
        mAdapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void fetchError(String e) {
        WidgetUtils.setupSnackBar(this, mRootView, R.string.networkerror, mSnackbar, R.color.snackbartext);
    }

    /** TODO: edit note **/
    @Override
    public void onEdit(final int id, final int position) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.dialog_add);

        TextView title = dialog.findViewById(R.id.titleDialog);
        CardView mDone = dialog.findViewById(R.id.done);
        final TextInputLayout desc = dialog.findViewById(R.id.til_description);
        CardView cancel = dialog.findViewById(R.id.cancelAdd);

        title.setText(getResources().getString(R.string.editnote));
        mDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM d");

                if (!desc.getEditText().getText().toString().trim().equals("")) {
                    editNotePresenter.editHandleLogic(id, desc.getEditText().getText().toString().trim()
                            , simpleDateFormat.format(calendar.getTime()), userName, position);
                    dialog.cancel();
                } else {
                    WidgetUtils.setupSnackBar(MainActivity.this, mRootView, R.string.emptynote, mSnackbar, R.color.snackbartext);
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialog.show();
    }

    @Override
    public void editSuccess(int position) {
        //mAdapter.notifyItemChanged(position);
        //Toast.makeText(this, "position: " + position , Toast.LENGTH_SHORT).show();
        // re fetch data when data is changed
        fetchAllDataPresenter.fetchAllData(userName);
        WidgetUtils.setupSnackBar(this, mRootView, R.string.editsuccess, mSnackbar, R.color.snackbartext);
    }

    @Override
    public void editError(String e) {
        WidgetUtils.setupSnackBar(this, mRootView, R.string.editerror, mSnackbar, R.color.snackbartext);
    }

    /** TODO: delete note **/
    @Override
    public void onDelete(final int id, final int position) {
        final Dialog dialogDelete = new Dialog(this);
        dialogDelete.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogDelete.setCanceledOnTouchOutside(false);
        dialogDelete.setContentView(R.layout.dialog_delete);

        CardView yes = dialogDelete.findViewById(R.id.yes);
        CardView no = dialogDelete.findViewById(R.id.no);

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteNotePresenter.deleteHandleLogic(id, position);
                dialogDelete.cancel();
            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogDelete.cancel();
            }
        });

        dialogDelete.show();
    }

    @Override
    public void deleteSuccess(int position) {
        mAdapter.notifyItemRemoved(position);
        fetchAllDataPresenter.fetchAllData(userName);
        WidgetUtils.setupSnackBar(this, mRootView, R.string.deletesuccess, mSnackbar, R.color.snackbartext);
    }

    @Override
    public void deleteError(String e) {
        WidgetUtils.setupSnackBar(this, mRootView, R.string.errordelete, mSnackbar, R.color.snackbartext);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    /** TODO: option menu **/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_setting, menu);
        this.menu = menu;

        /** TODO: SEARCH **/
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        mSearchView = (SearchView) menu.findItem(R.id.search).getActionView();
        EditText textSearch = mSearchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        textSearch.setHintTextColor(getResources().getColor(R.color.note_list_text));
        textSearch.setTextColor(getResources().getColor(R.color.note_list_text));

        mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        mSearchView.setMaxWidth(Integer.MAX_VALUE);

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mAdapter.getFilter().filter(newText);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.settingMenu) {
            if (SHOW_SETTING_FRAGMENT) {

                /** TODO: send user by bundle **/
                Bundle bundle = new Bundle();
                bundle.putString("userToChangePass", userName);
                SettingFragment settingFragment = new SettingFragment();
                settingFragment.setArguments(bundle);

                /** TODO: add fragment **/
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                //fragmentTransaction.addToBackStack("settingKey");
                fragmentTransaction.setCustomAnimations(R.anim.slide_right_to_left, R.anim.stay);
                fragmentTransaction.add(R.id.fragment_content, settingFragment, "TagSetting");
                fragmentTransaction.commit();
                mContainerRecyclerView.setVisibility(View.INVISIBLE);
                menu.getItem(1).setIcon(ContextCompat.getDrawable(this, R.drawable.usercicle));
                SHOW_SETTING_FRAGMENT = false;
            } else {

                /** TODO: remove fragment **/
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                SettingFragment settingFragment = (SettingFragment) getSupportFragmentManager().findFragmentByTag("TagSetting");
                if (settingFragment != null) {
                    fragmentTransaction.setCustomAnimations(R.anim.stay, R.anim.slide_left_to_right);
                    fragmentTransaction.remove(settingFragment);
                    fragmentTransaction.commit();
                }
                mContainerRecyclerView.setVisibility(View.VISIBLE);
                menu.getItem(1).setIcon(ContextCompat.getDrawable(this, R.drawable.accountsetting));
                SHOW_SETTING_FRAGMENT = true;
            }
        } else if (item.getItemId() == R.id.search) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPress(Note note, int position) {
        Intent intent = new Intent(MainActivity.this, ReadNoteActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("readnote", note);
        intent.putExtra("mybundle", bundle);
        intent.putExtra("positionitem", position);
        intent.putExtra("user-name", userName);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_right_to_left, R.anim.stay);
    }
}
