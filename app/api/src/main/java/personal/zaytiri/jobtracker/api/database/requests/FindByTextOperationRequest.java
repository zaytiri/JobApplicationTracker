package personal.zaytiri.jobtracker.api.database.requests;

public class FindByTextOperationRequest<M> {
    private M model;
    private String textToFind;

    public M getModel() {
        return model;
    }

    public void setModel(M model) {
        this.model = model;
    }

    public String getTextToFind() {
        return textToFind;
    }

    public void setTextToFind(String textToFind) {
        this.textToFind = textToFind;
    }

    public String getColumnToFind() {
        return columnToFind;
    }

    public void setColumnToFind(String columnToFind) {
        this.columnToFind = columnToFind;
    }

    private String columnToFind;
}
