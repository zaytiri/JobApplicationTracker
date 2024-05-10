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
    Select,
    Textarea,
    Switch,
    Divider,
    Flex,
    Tooltip,
} from "@chakra-ui/react";

import {
    EditIcon
} from "@chakra-ui/icons"

import { update } from "../api/api_endpoints/job_offer_api.js"
import { get } from "../api/api_endpoints/status_api.js"
import { Zoom, toast } from "react-toastify";

export const EditJobOffer = ({ currentJobOffer, setFetchDataAgain, closeModal }) => {
    const { isOpen, onOpen, onClose } = useDisclosure()

    const initialRef = React.useRef(null);
    const finalRef = React.useRef(null);

    const [status, setStatus] = useState([])

    const [company, setCompany] = useState('')
    const [role, setRole] = useState('')
    const [link, setLink] = useState('')
    const [description, setDescription] = useState('')
    const [location, setLocation] = useState('')
    const [companyWebsite, setCompanyWebsite] = useState('')
    const [applied, setApplied] = useState(false)
    const [statusId, setStatusId] = useState(0)
    const [appliedAt, setAppliedAt] = useState(new Date())
    const [notes, setNotes] = useState('')

    const editJobOffer = async () => {
        const obj =
        {
            company: company,
            role: role,
            companyWebsite: companyWebsite,
            location: location,
            link: link,
            description: description,
            appliedAt: appliedAt === '' ? new Date(appliedAt) : new Date(appliedAt).toISOString(),
            statusId: statusId.toString(),
            interviewNotes: notes,
        }

        const response = await update(currentJobOffer.id, obj);

        if (response.success === false) return null;

        setFetchDataAgain(true)
        onClose();
        closeModal();

        toast.success('Job application was edited.', {
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

    useEffect(() => {
        const setData = async () => {
            setModalInformation();
        };

        setData();
    }, []);

    useEffect(() => {
        const fetchData = async () => {
            try {
                const response = await get({});
                setStatus(response);
            } catch (error) {
            }
        };

        if (isOpen) {
            fetchData();
        }
    }, [isOpen]);

    const setModalInformation = () => {
        setCompany(currentJobOffer.company);
        setRole(currentJobOffer.role);
        setLocation(currentJobOffer.location);
        setDescription(currentJobOffer.description);
        setLink(currentJobOffer.link);
        setCompanyWebsite(currentJobOffer.companyWebsite);
        setApplied(currentJobOffer.appliedAt === undefined || currentJobOffer.appliedAt[0] < 0 ? false : true);
        setAppliedAt(getFormattedDate(currentJobOffer.appliedAt));
        setStatusId(currentJobOffer.statusId);
        setNotes(currentJobOffer.interviewNotes);
    }

    const setStatusApplied = () => {
        setApplied(!applied)

        if (applied) {
            setAppliedAt(getFormattedDate(''))
        } else {
            setAppliedAt(getFormattedDate(new Date(Date.now())))
        }
    }

    const getFormattedDate = (dateToFormat) => {
        if (dateToFormat?.length < 0 || dateToFormat === '' || dateToFormat === undefined || applied === 'Invalid Date Time') {
            return '';
        }

        let date = dateToFormat
        if (dateToFormat?.length > 0) {
            date = new Date(dateToFormat[0], dateToFormat[1] - 1, dateToFormat[2], dateToFormat[3], dateToFormat[4], 0);
            date = new Date(date).toLocaleString()
            date = new Date(new Date(date).toLocaleString() + ' UTC')
        }

        const day = date.getDate().toString().padStart(2, '0');
        const month = (date.getMonth() + 1).toString().padStart(2, '0');
        const year = date.getFullYear();
        const hour = date.getHours().toString().padStart(2, '0');
        const minute = date.getMinutes().toString().padStart(2, '0');

        const formattedDate = `${year}-${month}-${day}T${hour}:${minute}`;
        return formattedDate
    }

    return (
        <>
            <Tooltip label="Click here to edit job applications details.">
                <EditIcon boxSize={5} color="black.500" onClick={onOpen} />
            </Tooltip>
            <Modal
                initialFocusRef={initialRef}
                finalFocusRef={finalRef}
                isOpen={isOpen}
                onClose={onClose}
                size="xl"
                onOverlayClick={setModalInformation}
            >
                <ModalOverlay />
                <ModalContent>
                    <ModalHeader>Edit {currentJobOffer.company} Job Offer</ModalHeader>
                    <ModalCloseButton onClick={setModalInformation} />
                    <ModalBody pb={6}>
                        <FormControl>
                            <FormLabel>Job Offer URL</FormLabel>
                            <Input value={link} placeholder='www.job-offer-for-google-from-linkedin.com' />
                        </FormControl>
                        <Divider mt='10px' mb='10px' />
                        <Grid
                            templateColumns={{ sm: "1fr", lg: "2fr 1fr" }}
                            templateRows={{ lg: "repeat(4, auto)" }}
                            gap='20px'>
                            <FormControl>
                                <FormLabel>Company</FormLabel>
                                <Input value={company} onChange={(event) => setCompany(event.target.value)} ref={initialRef} placeholder='Google' />
                            </FormControl>
                            <FormControl>
                                <FormLabel>Role</FormLabel>
                                <Input value={role} onChange={(event) => setRole(event.target.value)} placeholder='Developer' />
                            </FormControl>
                            <FormControl>
                                <FormLabel>Location</FormLabel>
                                <Input value={location} onChange={(event) => setLocation(event.target.value)} placeholder='Switzerland' />
                            </FormControl>
                            <FormControl>
                                <FormLabel>Status</FormLabel>
                                <Select onChange={(event) => setStatusId(event.target.value)} placeholder='Select option' value={statusId}>
                                    {status?.length > 0 && status.map((row) => {
                                        return (
                                            <option key={row.id} value={row.id}>{row.name}</option>
                                        )
                                    })}
                                </Select>
                            </FormControl>
                            <FormControl>
                                <FormLabel>Company Website</FormLabel>
                                <Input value={companyWebsite} onChange={(event) => setCompanyWebsite(event.target.value)} placeholder='www.companywebsite.com' />
                            </FormControl>
                        </Grid>
                        <FormControl>
                            <FormLabel>Description</FormLabel>
                            <Textarea value={description} onChange={(event) => setDescription(event.target.value)} placeholder='This job requires C#, Java and React.' />
                        </FormControl>
                        <Flex>
                            <FormControl display='flex' alignItems='center' mt="5px">
                                <FormLabel mb='0'>
                                    Applied?
                                </FormLabel>
                                <Switch onChange={() => setStatusApplied()} isChecked={applied} />
                            </FormControl>
                            <FormControl display='flex' alignItems='center' mt="5px">
                                <FormLabel mb='0'>
                                    Applied At
                                </FormLabel>
                                <Input type="datetime-local" onChange={(event) => setAppliedAt(event.target.value)} value={appliedAt} />
                            </FormControl>
                        </Flex>
                        <FormControl>
                            <FormLabel>Notes</FormLabel>
                            <Textarea value={notes} onChange={(event) => setNotes(event.target.value)} placeholder='Some notes about the interview or the job.' />
                        </FormControl>
                    </ModalBody>

                    <ModalFooter>
                        <Button colorScheme='blue' mr={3} onClick={editJobOffer}>
                            Edit
                        </Button>
                    </ModalFooter>
                </ModalContent>
            </Modal>
        </>
    )
}