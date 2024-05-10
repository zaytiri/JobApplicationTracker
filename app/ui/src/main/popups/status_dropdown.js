import { Badge, Menu, MenuButton, MenuItem, MenuList } from "@chakra-ui/react";
import React, { useState } from "react";
import { update } from "../api/api_endpoints/job_offer_api";
import { Zoom, toast } from "react-toastify";

export const StatusDropdown = ({ statuses, currentStatus, setCurrentStatus, currentJobOffer }) => {
    const [isOpen, setIsOpen] = useState(false);

    const handleBadgeClick = () => {
        setIsOpen(!isOpen);
    };

    const handleMenuItemClick = (status) => {
        setCurrentStatus(status);
        setIsOpen(false);
        editStatus(status)
    };

    const editStatus = async (status) => {
        const obj =
        {
            company: currentJobOffer.company,
            role: currentJobOffer.role,
            companyWebsite: currentJobOffer.companyWebsite,
            location: currentJobOffer.location,
            link: currentJobOffer.link,
            description: currentJobOffer.description,
            appliedAt: (currentJobOffer.appliedAt === '' || currentJobOffer.appliedAt[0] < 0) ? new Date(currentJobOffer.appliedAt) : new Date(currentJobOffer.appliedAt).toISOString(),
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
        <Menu isOpen={isOpen} onClose={() => setIsOpen(false)} placement="top">
            <MenuButton
                as={Badge}
                bg={currentStatus === undefined ? 'white' : currentStatus.color}
                color="black"
                fontSize="16px"
                borderRadius="8px"
                cursor="pointer"
                onClick={handleBadgeClick}
            >
                {currentStatus !== undefined ? currentStatus.name : 'No Status'}
            </MenuButton>
            <MenuList>
                {statuses?.length > 0 && statuses.map((row, index) => {
                    return (
                        <MenuItem width='inherit' key={row.name} onClick={() => handleMenuItemClick({ id: row.id, name: row.name, color: row.color })}>
                            <Badge
                                bg={row === undefined ? 'white' : row.color}
                                color="black"
                                fontSize="16px"
                                borderRadius="8px"
                                cursor="pointer"
                                onClick={handleBadgeClick}
                            >
                                {row.name}
                            </Badge>
                        </MenuItem>
                    );
                })}
            </MenuList>
        </Menu>
    )
}