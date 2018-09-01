package qthjen_dev.io.notes.Presenter;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.schedulers.Schedulers;
import qthjen_dev.io.notes.Presenter.PresenterInterface.EditNoteInterface;
import qthjen_dev.io.notes.Utils.NetworkUtils.ApiService;
import qthjen_dev.io.notes.View.ViewInterface.EditViewInterface;

public class EditNotePresenter implements EditNoteInterface {

    private EditViewInterface editViewInterface;
    private ApiService apiService;
    private CompositeDisposable disposable = new CompositeDisposable();

    public EditNotePresenter(EditViewInterface editViewInterface, ApiService apiService) {
        this.editViewInterface = editViewInterface;
        this.apiService = apiService;
    }

    @Override
    public void editHandleLogic(int id, String note, String timestamp, String user, final int position) {
        disposable.add(apiService.UpdateNote(id, note, timestamp, user)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(new DisposableCompletableObserver() {
                @Override
                public void onComplete() {
                    editViewInterface.editSuccess(position);
                }

                @Override
                public void onError(Throwable e) {
                    editViewInterface.editError(e.toString());
                }
            }));
    }
}
