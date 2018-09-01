package qthjen_dev.io.notes.Presenter;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import qthjen_dev.io.notes.Model.User;
import qthjen_dev.io.notes.Presenter.PresenterInterface.SignInInterface;
import qthjen_dev.io.notes.Utils.NetworkUtils.ApiService;
import qthjen_dev.io.notes.View.ViewInterface.SignInViewInterface;

public class SignInPresenter implements SignInInterface {

    private SignInViewInterface signInViewInterface;
    private ApiService apiService;
    private CompositeDisposable disposable = new CompositeDisposable();

    public SignInPresenter(SignInViewInterface signInViewInterface, ApiService apiService) {
        this.signInViewInterface = signInViewInterface;
        this.apiService = apiService;
    }

    @Override
    public void signIn(String user, String pass) {
        disposable.add(apiService.Login(user, pass)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(new DisposableSingleObserver<ArrayList<User>>() {
                @Override
                public void onSuccess(ArrayList<User> users) {
                    if (users.size() > 0)
                        signInViewInterface.success(users);
                }

                @Override
                public void onError(Throwable e) {
                    signInViewInterface.error(e.toString());
                }
            }));
    }
}
