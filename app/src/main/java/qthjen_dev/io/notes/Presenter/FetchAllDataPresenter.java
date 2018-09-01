package qthjen_dev.io.notes.Presenter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import qthjen_dev.io.notes.Model.Note;
import qthjen_dev.io.notes.Presenter.PresenterInterface.FetchAllDataInterface;
import qthjen_dev.io.notes.Utils.NetworkUtils.ApiService;
import qthjen_dev.io.notes.View.ViewInterface.FetchAllDataViewInterface;

public class FetchAllDataPresenter implements FetchAllDataInterface {

    private FetchAllDataViewInterface fetchAllDataViewInterface;
    private ApiService apiService;
    private CompositeDisposable disposable = new CompositeDisposable();

    public FetchAllDataPresenter(FetchAllDataViewInterface fetchAllDataViewInterface, ApiService apiService) {
        this.fetchAllDataViewInterface = fetchAllDataViewInterface;
        this.apiService = apiService;
    }

    @Override
    public void fetchAllData(String user) {
        disposable.add(apiService.FetchAllData(user)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map(new Function<ArrayList<Note>, ArrayList<Note>>() {
                @Override
                public ArrayList<Note> apply(ArrayList<Note> list) throws Exception {
                    Collections.sort(list, new Comparator<Note>() {
                        @Override
                        public int compare(Note o1, Note o2) {
                            return Integer.parseInt(o2.getNid()) - Integer.parseInt(o1.getNid());
                        }
                    });
                    return list;
                }
            })
            .subscribeWith(new DisposableSingleObserver<ArrayList<Note>>() {
                @Override
                public void onSuccess(ArrayList<Note> list) {
                    fetchAllDataViewInterface.fetchSuccess(list);
                }

                @Override
                public void onError(Throwable e) {
                    fetchAllDataViewInterface.fetchError(e.toString());
                }
            }));
    }
}
