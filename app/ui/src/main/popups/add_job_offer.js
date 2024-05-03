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
    Tooltip,
    Grid,
    Select,
    Textarea,
    Switch,
    Divider,
    Spinner,

} from "@chakra-ui/react";

import { create, scrape } from "../api/api_endpoints/job_offer_api.js"
import { get } from "../api/api_endpoints/status_api.js"
import { toast } from "react-toastify";

export const AddJobOffer = ({ setFetchDataAgain }) => {
    const { isOpen, onOpen, onClose } = useDisclosure()

    const initialRef = React.useRef(null);
    const finalRef = React.useRef(null);

    const [loadingScrape, setLoadingScrape] = useState(false);

    const [status, setStatus] = useState([])

    const [company, setCompany] = useState('')
    const [role, setRole] = useState('')
    const [link, setLink] = useState('')
    const [description, setDescription] = useState('')
    const [location, setLocation] = useState('')
    const [companyWebsite, setCompanyWebsite] = useState('')
    const [applied, setApplied] = useState(false)
    const [statusId, setStatusId] = useState(0)

    const linkHandleChange = async (event) => {
        const url = event.target.value;
        setLink(url);

        if (url === '') return;

        setLoadingScrape(true);
        let response = {}
        try {
            response = await scrape(url);

            setCompany(response.company);
            setRole(response.role);
            setLocation(response.location);
            setDescription(response.description);

        } catch (error) {
            toast.error('Something went wrong while scraping website. Try again later if failing more than two times.', {
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

        setLoadingScrape(false);
    }

    const addJobOffer = async () => {
        const obj =
        {
            company: company,
            role: role,
            companyWebsite: companyWebsite,
            location: location,
            link: link,
            description: description,
            appliedAt: !applied ? null : new Date(),
            statusId: statusId.toString(),
        }
        const response = await create(obj);

        if (response.success === false) return null;

        setFetchDataAgain(true);

        resetModal();

        onClose();

        toast.success('New Job Application was created.', {
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
        const fetchData = async () => {
            try {
                const response = await get({});
                setStatus(response);
            } catch (error) {
                console.error("Error fetching job offers:", error);
            }
        };

        if (isOpen) {
            fetchData();
        }
    }, [isOpen]);

    const resetModal = () => {
        setCompany('');
        setRole('');
        setLocation('');
        setDescription('');
        setLink('');
        setCompanyWebsite('');
        setApplied(false);
        setStatusId(0);
    }

    return (
        <>
            <Button variant="primary" onClick={onOpen}>
                ADD JOB OFFER
            </Button>

            <Modal
                initialFocusRef={initialRef}
                finalFocusRef={finalRef}
                isOpen={isOpen}
                onClose={onClose}
                size="xl"
                onOverlayClick={resetModal}
            >
                <ModalOverlay />
                <ModalContent>
                    <ModalHeader>Add new Job Offer</ModalHeader>
                    <ModalCloseButton onClick={resetModal} />
                    <ModalBody pb={6}>
                        <FormControl>
                            <Tooltip label='By providing the link offer, the information can be filled in automatically to the relevant fields. Currently, only works for LinkedIn.'>
                                <FormLabel>Job Offer URL</FormLabel>
                            </Tooltip>
                            <Input value={link} onChange={linkHandleChange} placeholder='www.job-offer-for-google-from-linkedin.com' />
                            <Spinner
                                display={loadingScrape ? 'block' : 'none'}
                                align="center"
                                thickness='4px'
                                speed='0.65s'
                                emptyColor='gray.200'
                                color='blue.500'
                                size='xl'
                            />
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
                                <Select onChange={(event) => setStatusId(event.target.value)} placeholder='Select option'>
                                    {status?.length > 0 && status.map((row) => {
                                        return (
                                            <option value={row.id}>{row.name}</option>
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
                        <FormControl display='flex' alignItems='center' mt="5px">
                            <FormLabel htmlFor='applied' mb='0'>
                                Applied?
                            </FormLabel>
                            <Switch onChange={() => setApplied(!applied)} id='applied' />
                        </FormControl>
                    </ModalBody>

                    <ModalFooter>
                        <Button colorScheme='blue' mr={3} onClick={addJobOffer}>
                            Add
                        </Button>
                    </ModalFooter>
                </ModalContent>
            </Modal>
        </>
    )
}