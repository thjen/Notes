package qthjen_dev.io.notes.Presenter;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.schedulers.Schedulers;
import qthjen_dev.io.notes.Presenter.PresenterInterface.DeleteNoteInterface;
import qthjen_dev.io.notes.Utils.NetworkUtils.ApiService;
import qthjen_dev.io.notes.View.ViewInterface.DeleteViewInterface;

public class DeleteNotePresenter implements DeleteNoteInterface {

    private DeleteViewInterface deleteViewInterface;
    private ApiService apiService;
    private CompositeDisposable disposable = new CompositeDisposable();

    public DeleteNotePresenter(DeleteViewInterface deleteViewInterface, ApiService apiService) {
        this.deleteViewInterface = deleteViewInterface;
        this.apiService = apiService;
    }

    @Override
    public void deleteHandleLogic(int id, final int position) {
        disposable.add(apiService.DeleteNote(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(new DisposableCompletableObserver() {
                @Override
                public void onComplete() {
                    deleteViewInterface.deleteSuccess(position);
                }

                @Override
                public void onError(Throwable e) {
                    deleteViewInterface.deleteError(e.toString());
                }
            }));
    }
}
