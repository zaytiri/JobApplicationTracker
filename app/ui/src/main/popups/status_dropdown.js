import { Badge, Menu, MenuButton, MenuItem, MenuList } from "@chakra-ui/react";
import React, { useState } from "react";

export const StatusDropdown = ({ statuses, currentStatus, setCurrentStatus, doActionOnItemChosen }) => {
    const [isOpen, setIsOpen] = useState(false);

    const handleBadgeClick = () => {
        setIsOpen(!isOpen);
    };

    const handleMenuItemClick = (status) => {
        setCurrentStatus(status);
        setIsOpen(false);
        doActionOnItemChosen(status)
    };

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