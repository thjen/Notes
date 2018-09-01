package qthjen_dev.io.notes.Presenter;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.schedulers.Schedulers;
import qthjen_dev.io.notes.Presenter.PresenterInterface.ChangePassInterface;
import qthjen_dev.io.notes.Utils.NetworkUtils.ApiService;
import qthjen_dev.io.notes.View.ViewInterface.ChangePassViewInterface;

public class ChangePassPresenter implements ChangePassInterface {

    private ChangePassViewInterface changePassViewInterface;
    private ApiService apiService;
    private CompositeDisposable disposable = new CompositeDisposable();

    public ChangePassPresenter(ChangePassViewInterface changePassViewInterface, ApiService apiService) {
        this.changePassViewInterface = changePassViewInterface;
        this.apiService = apiService;
    }

    @Override
    public void changePass(String user, String pass) {
        disposable.add(apiService.ChangePassword(user, pass)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableCompletableObserver() {
                    @Override
                    public void onComplete() {
                        changePassViewInterface.success();
                    }

                    @Override
                    public void onError(Throwable e) {
                        changePassViewInterface.error();
                    }
                }));
    }
}
