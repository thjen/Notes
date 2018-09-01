package qthjen_dev.io.notes.View.ViewInterface;

import java.util.ArrayList;

import qthjen_dev.io.notes.Model.Note;

public interface FetchAllDataViewInterface {
    void fetchSuccess(ArrayList<Note> list); // list for adapter
    void fetchError(String e);
}
