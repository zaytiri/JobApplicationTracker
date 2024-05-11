import React from "react";
import { update } from "../api/api_endpoints/job_offer_api";
import { Zoom, toast } from "react-toastify";
import { StatusDropdown } from "./status_dropdown";

export const EditStatusDropdown = ({ statuses, currentStatus, setCurrentStatus, currentJobOffer }) => {

    const editStatus = async (status) => {
        var date = new Date(
            currentJobOffer.appliedAt[0], 
            currentJobOffer.appliedAt[1] - 1, 
            currentJobOffer.appliedAt[2], 
            currentJobOffer.appliedAt[3], 
            currentJobOffer.appliedAt[4], 
            0
        )
        const obj =
        {
            company: currentJobOffer.company,
            role: currentJobOffer.role,
            companyWebsite: currentJobOffer.companyWebsite,
            location: currentJobOffer.location,
            link: currentJobOffer.link,
            description: currentJobOffer.description,
            appliedAt: (currentJobOffer.appliedAt === '' || currentJobOffer.appliedAt[0] < 0) ? date : date.toISOString(),
            statusId: status.id.toString(),
            interviewNotes: currentJobOffer.interviewNotes,
        }

        const response = await update(currentJobOffer.id, obj);

        if (response.success === false) return null;

        toast.success('Status was updated.', {
            position: "top-center",
            autoClose: 5000,
            hideProgressBar: false,
            closeOnClick: true,
            pauseOnHover: true,
            draggable: true,
            progress: undefined,
            theme: "colored",
            transition: Zoom,
        });
    }

    return (
        <StatusDropdown
            statuses={statuses}
            currentStatus={currentStatus}
            setCurrentStatus={setCurrentStatus}
            currentJobOffer={currentJobOffer}
            doActionOnItemChosen={editStatus}
        />
    )
}