
export const createJobOfferObject = function (props) {

    const {
        company,
        role,
        companyWebsite,
        location,
        link,
        description,
        applied,
        appliedAt,
        interviewNotes,
        statusId } = props;

    if (appliedAt === undefined && applied) {
        appliedAt = new Date();
    }

    return {
        company: company,
        role: role,
        companyWebsite: companyWebsite,
        location: location,
        link: link,
        description: description,
        appliedAt: appliedAt,
        statusId: statusId.toString(),
        interviewNotes: interviewNotes,
    }
}