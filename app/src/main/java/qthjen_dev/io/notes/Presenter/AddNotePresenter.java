package qthjen_dev.io.notes.Presenter;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import qthjen_dev.io.notes.Presenter.PresenterInterface.AddNoteInterface;
import qthjen_dev.io.notes.Utils.NetworkUtils.ApiService;
import qthjen_dev.io.notes.View.ViewInterface.AddNoteViewInterface;

public class AddNotePresenter implements AddNoteInterface {

    private AddNoteViewInterface addNoteViewInterface;
    private ApiService apiService;
    private CompositeDisposable disposable = new CompositeDisposable();

    public AddNotePresenter(AddNoteViewInterface addNoteViewInterface, ApiService apiService) {
        this.addNoteViewInterface = addNoteViewInterface;
        this.apiService = apiService;
    }

    @Override
    public void addNote(String description, String timestamp, String username) {
        disposable.add(apiService.InserNote(description, timestamp, username)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(new DisposableSingleObserver<String>() {
                @Override
                public void onSuccess(String s) {
                    addNoteViewInterface.insertSuccess(s);
                }

                @Override
                public void onError(Throwable e) {
                    addNoteViewInterface.insertError(e.toString());
                }
            }));
    }
}
