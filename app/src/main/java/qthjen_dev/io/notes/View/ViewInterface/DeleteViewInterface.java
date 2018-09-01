package qthjen_dev.io.notes.View.ViewInterface;

public interface DeleteViewInterface {
    void deleteSuccess(int position); // position for notifyItemRemove
    void deleteError(String e);
}
