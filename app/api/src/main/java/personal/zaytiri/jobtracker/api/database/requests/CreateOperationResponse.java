package personal.zaytiri.jobtracker.api.database.requests;

public class CreateOperationResponse {
    private boolean isCreated;
    private int idCreated;
    public boolean isCreated() {
        return isCreated;
    }

    public void setCreated(boolean created) {
        isCreated = created;
    }
    public int getIdCreated() {
        return idCreated;
    }

    public void setIdCreated(int idCreated) {
        this.idCreated = idCreated;
    }
}
