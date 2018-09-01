package qthjen_dev.io.notes.Presenter.PresenterInterface;

public interface EditNoteInterface {
    void editHandleLogic(int id, String note, String timestamp, String user, int position);
    // position for notifyItemChanged
}
