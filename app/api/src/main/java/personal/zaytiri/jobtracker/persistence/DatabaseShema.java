package personal.zaytiri.jobtracker.persistence;


public class DatabaseShema {
    private static DatabaseShema INSTANCE;

    // job offer table
    public String jobOfferTableName = "job_offer";
    public String companyColumnName = "company";
    public String roleColumnName = "role";
    public String companyWebsiteColumnName = "company_website";
    public String locationColumnName = "location";
    public String linkColumnName = "link";
    public String descriptionColumnName = "description";
    public String appliedAtColumnName = "applied_at";
    public String interviewNotesColumnName = "interview_notes";

    // job offer - status - intermediate table
    public String jobOfferStatusTableName = "job_offer_status";
    public String changedAtColumnName = "changed_at";

    // status table
    public String statusTableName = "status";
    public String colorColumnName = "color";

    // job offer - document - intermediary table
    public String jobOfferDocumentTableName = "job_offer_document";
    public String documentIdColumnName = "document_id";

    // document table
    public String documentTableName = "document";
    public String filePathColumnName = "file_path";

    // job offer - tag intermediary table
    public String jobOfferTagTableName = "job_offer_tag";
    public String tagIdColumnName = "tag_id";

    // tag table
    public String tagTableName = "tag";

    // settings table
    public String settingsTableName = "settings";
    public String appliedStatusColumnName = "applied_status";
    public String closedStatusColumnName = "closed_status";

    // version table
    public String versionTableName = "version";

    // common
    public String idColumnName = "id";
    public String nameColumnName = "name";
    public String jobOfferIdColumnName = "job_offer_id";
    public String statusIdColumnName = "status_id";
    public String updatedAtColumnName = "updated_at";
    public String createdAtColumnName = "created_at";

    private DatabaseShema(){
    }

    public static DatabaseShema getINSTANCE() {
        if(INSTANCE == null){
            INSTANCE = new DatabaseShema();
        }
        return INSTANCE;
    }
}
