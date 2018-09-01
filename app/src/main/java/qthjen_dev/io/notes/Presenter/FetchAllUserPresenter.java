package qthjen_dev.io.notes.Presenter;

import java.util.ArrayList;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import qthjen_dev.io.notes.Model.UserName;
import qthjen_dev.io.notes.Presenter.PresenterInterface.FetchAllUserInterface;
import qthjen_dev.io.notes.Utils.NetworkUtils.ApiService;
import qthjen_dev.io.notes.View.ViewInterface.FetchAllUserViewInterface;

public class FetchAllUserPresenter implements FetchAllUserInterface {

    private ApiService apiService;
    private FetchAllUserViewInterface fetchAllUserView;
    private CompositeDisposable disposable = new CompositeDisposable();

    public FetchAllUserPresenter(FetchAllUserViewInterface fetchAllUserViewInterface, ApiService apiService){
        this.fetchAllUserView = fetchAllUserViewInterface;
        this.apiService = apiService;
    }

    @Override
    public void fetchAllUser() {
        disposable.add(apiService.FetchAllUser()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<ArrayList<UserName>>() {
                    @Override
                    public void onSuccess(ArrayList<UserName> list) {
                        fetchAllUserView.checkUser(list);
                    }

                    @Override
                    public void onError(Throwable e) {
                    }
                }));
    }
}
