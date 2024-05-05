import { Button, Flex, Select, SimpleGrid, Spacer, Text, Tooltip, useColorModeValue, CardFooter, Divider } from "@chakra-ui/react";
import React, { useEffect, useState } from "react";
import Card from "../../template/components/Card/Card";
import CardHeader from "../../template/components/Card/CardHeader";
import { get as getStatus } from "../../api/api_endpoints/status_api";
import { get as getSettings, update } from "../../api/api_endpoints/settings_api";
import CardBody from "../../template/components/Card/CardBody";
import { Zoom, toast } from "react-toastify";

export const Settings = () => {
    const textColor = useColorModeValue("gray.500", "white");

    const [status, setStatus] = useState([])

    const [appliedStatusId, setAppliedStatusId] = useState(0)
    const [closedStatusId, setClosedStatusId] = useState(0)

    useEffect(() => {
        const fetchData = async () => {
            try {
                const response = await getStatus({});
                setStatus(response);

                const responseSettings = await getSettings();
                setAppliedStatusId(responseSettings.appliedStatus)
                setClosedStatusId(responseSettings.closedStatus)

            } catch (error) {
                console.error("Error fetching status:", error);
            }
        };

        fetchData();
    }, []);

    const saveSettings = async () => {
        const response = await update(
            {
                appliedStatus: appliedStatusId,
                closedStatus: closedStatusId,
            }
        );

        if (response.success === false) return null;

        toast.success('Settings were saved.', {
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
        <Flex flexDirection='column' pt={{ base: "120px", md: "75px" }}>
            <SimpleGrid columns={{ sm: 1, md: 2, xl: 2 }} spacing='24px' mb='20px'>
                <Card minH='125px'>
                    <CardHeader>
                        <Text fontSize="xl" color={textColor} fontWeight="bold" mb='20px'>
                            Status Mappings
                        </Text>
                    </CardHeader>
                    <CardBody>
                        <Text fontSize="md" color={textColor} mb='10px'>
                            Which custom status should the system apply in certain situations?
                        </Text>
                        <Flex direction='column' gap="10px">
                            <Flex>
                                <Tooltip label='This status will be automatically applied to a job application when a new one is created with the "Applied" toggle switched on.'>
                                    <Text fontSize="sm" color={textColor} fontWeight="bold" pb=".5rem" >
                                        When applied to a job application:
                                    </Text>
                                </Tooltip>

                                <Select value={appliedStatusId} onChange={(event) => setAppliedStatusId(event.target.value)} placeholder='Select option'>
                                    {status?.length > 0 && status.map((row) => {
                                        return (
                                            <option value={row.id}>{row.name}</option>
                                        )
                                    })}
                                </Select>
                            </Flex>
                            <Spacer />
                            <Flex>
                                <Tooltip label='This status will be automatically applied to a job application when the user clicks on "Get Updates" and the system checks if the job application is closed.'>
                                    <Text fontSize="sm" color={textColor} fontWeight="bold" pb=".5rem">
                                        When a job application closes:
                                    </Text>
                                </Tooltip>
                                <Select value={closedStatusId} onChange={(event) => setClosedStatusId(event.target.value)} placeholder='Select option'>
                                    {status?.length > 0 && status.map((row) => {
                                        return (
                                            <option value={row.id}>{row.name}</option>
                                        )
                                    })}
                                </Select>
                            </Flex>
                        </Flex>
                        <Divider mt='15px' mb='15px'/>
                        <Flex justifyContent="right">
                            <Button colorScheme='blue' mr={3} onClick={saveSettings}>
                                Save
                            </Button>
                        </Flex>
                    </CardBody>
                </Card>
            </SimpleGrid>
        </Flex>
    )
}