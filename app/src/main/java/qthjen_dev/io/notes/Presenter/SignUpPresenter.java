package qthjen_dev.io.notes.Presenter;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import qthjen_dev.io.notes.Presenter.PresenterInterface.SignUpInterface;
import qthjen_dev.io.notes.Utils.NetworkUtils.ApiService;
import qthjen_dev.io.notes.View.ViewInterface.SignUpViewInterface;

public class SignUpPresenter implements SignUpInterface {

    private SignUpViewInterface signUpViewInterface;
    private CompositeDisposable disposable = new CompositeDisposable();
    private ApiService apiService;

    public SignUpPresenter(SignUpViewInterface signUpViewInterface, ApiService apiService) {
        this.signUpViewInterface = signUpViewInterface;
        this.apiService = apiService;
    }

    @Override
    public void signUp(final String user, final String pass) {
        disposable.add(apiService.InsertUser(user, pass)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<String>() {
                    @Override
                    public void onSuccess(String s) {
                        signUpViewInterface.success(s);
                    }

                    @Override
                    public void onError(Throwable e) {
                        signUpViewInterface.error(e.toString());
                    }
                }));
    }
}
