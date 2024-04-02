import React, { useState, useEffect } from "react";
// Chakra imports
import {
    Button,
    useDisclosure,
    Modal,
    ModalOverlay,
    ModalContent,
    ModalHeader,
    ModalCloseButton,
    FormControl,
    FormLabel,
    Input,
    ModalBody,
    ModalFooter,
    Grid,
    Tr,
    Td,
    Divider,
    Text,
    Table,
    Thead,
    Th,
    Tbody,
    useColorModeValue,
    Box,

} from "@chakra-ui/react";

import { create, get, remove } from '../api/api_endpoints/status_api'

export const ManageJobStatus = () => {
    const textColor = useColorModeValue("gray.700", "white");
    const borderColor = useColorModeValue("gray.200", "gray.600");

    const { isOpen, onOpen, onClose } = useDisclosure()

    const initialRef = React.useRef(null)
    const finalRef = React.useRef(null)

    const [status, setStatus] = useState([]);
    const [name, setName] = useState('');
    const [color, setColor] = useState('#000000');

    const [fetchDataAgain, setFetchDataAgain] = useState(true);
    useEffect(() => {
        const fetchData = async () => {
            try {
                const response = await get({});
                setStatus(response);
            } catch (error) {
                console.error("Error fetching job offers:", error);
            }
        };

        if (fetchDataAgain) {
            fetchData();
            setFetchDataAgain(false);
        }
    }, [fetchDataAgain]);

    const addStatus = async () => {
        if(name === '') return null;
        
        const response = await create({ name: name, color: color });

        if (response.success === false) return null;
        setFetchDataAgain(true);
        resetModal();
    }
    const removeStatus = async (id) => {
        const response = await remove(id);

        if (response.success === false) return null;
        setStatus(status.filter((status) => status.id !== id));
    }

    const resetModal = () => {
        setName('');
        setColor('#000000');
    }

    return (
        <>
            <Button variant="primary" onClick={onOpen}>
                MANAGE JOB STATUS
            </Button>

            <Modal
                initialFocusRef={initialRef}
                finalFocusRef={finalRef}
                isOpen={isOpen}
                onClose={onClose}
                size="xl"
            >
                <ModalOverlay />
                <ModalContent>
                    <ModalHeader>Manage Job Status</ModalHeader>
                    <ModalCloseButton />
                    <ModalBody pb={6}>
                        <Text fontSize="xl" color={textColor} fontWeight="bold">
                            Add new Status
                        </Text>
                        <Divider mt='10px' mb='10px' />
                        <Grid
                            templateColumns={{ sm: "1fr", lg: "3fr 1fr" }}
                            templateRows={{ lg: "repeat(1, auto)" }}
                            gap='20px'>
                            <FormControl>
                                <FormLabel>Name</FormLabel>
                                <Input value={name} onChange={(event) => setName(event.target.value)} ref={initialRef} placeholder='Ghosted' />
                            </FormControl>
                            <FormControl>
                                <FormLabel>Color</FormLabel>
                                <Input
                                    value={color}
                                    onChange={(event) => setColor(event.target.value)}
                                    color='teal'
                                    placeholder="Select Status Color"
                                    size="md"
                                    type="color"
                                />
                            </FormControl>
                            <Button colorScheme='blue' mr={3} onClick={addStatus}>
                                Add
                            </Button>
                        </Grid>
                        <Divider mt='20px' mb='20px' />
                        <Text fontSize="xl" color={textColor} fontWeight="bold">
                            View available Status
                        </Text>
                        <Divider mt='10px' mb='10px' />
                        <Table variant="simple" width="100%">
                                <Thead>
                                    <Tr my=".8rem" pl="0px" color="gray.400" >
                                        <Th pl="0px" borderColor={borderColor} color="gray.400" >
                                            Name
                                        </Th>
                                        <Th borderColor={borderColor} color="gray.400" >Color</Th>
                                        <Th borderColor={borderColor}></Th>
                                    </Tr>
                                </Thead>
                                <Tbody>
                                    {status?.length > 0 && status.map((row) => {
                                        return (
                                            <Tr>
                                                <Td
                                                    pl="0px"
                                                    borderColor={borderColor}>
                                                    <Text fontSize="md" color={textColor} overflowWrap='anywhere' fontWeight="bold">
                                                        {row.name}
                                                    </Text>
                                                </Td>
                                                <Td
                                                    pl="0px"
                                                    borderColor={borderColor}>
                                                    <Box w='70px' h='10' bg={row.color} textAlign='center'>
                                                        <Text fontSize="md" color={textColor} textAlign='center'>
                                                            {row.color}
                                                        </Text>
                                                    </Box>
                                                </Td>
                                                <Td>
                                                    <Button variant="danger" onClick={() => removeStatus(row.id)}>
                                                        DELETE
                                                    </Button>
                                                </Td>
                                            </Tr>
                                        );
                                    })}
                                    {status?.length === 0 &&
                                        <Tr style={{ textAlign: 'center' }}>
                                            <Text fontSize='md' color={textColor} fontWeight='400' p="15px">
                                                No Status added yet.
                                            </Text>
                                        </Tr>
                                    }
                                </Tbody>
                            </Table>
                    </ModalBody>

                    <ModalFooter>
                    </ModalFooter>
                </ModalContent>
            </Modal>
        </>
    )
}