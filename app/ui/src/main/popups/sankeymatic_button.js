import { Button, Image, Modal, ModalBody, ModalCloseButton, ModalContent, ModalFooter, ModalHeader, ModalOverlay, Text, Textarea, Tooltip, useDisclosure, Link } from "@chakra-ui/react";
import { useColorModeValue } from "@chakra-ui/system";
import React, { useState } from "react";
import { Zoom, toast } from "react-toastify";
import sankeymaticIcon from "../template/assets/img/SKM-400.png"
import copy from 'clipboard-copy';

export const SankeymaticFeature = () => {
    const textColor = useColorModeValue("gray.500", "white");

    const { isOpen, onOpen, onClose } = useDisclosure()

    let [sankeymaticInputDiagram, setSankeymaticInputDiagram] = useState('')

    let setInput = (e) => {
        let inputValue = e.target.value
        setSankeymaticInputDiagram(inputValue)
    }

    const copyInputDiagram = () => {
        copy(sankeymaticInputDiagram)
            .then(() => {
                toast.info('Sankeymatic Input copied to Clipboard.', {
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
            })
            .catch((error) => {
                toast.error('Some error occurred when copying Sankeymatic Input to Clipboard.', {
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
            });
    }

    return (
        <>
            <Tooltip label='Click here to get a Sankeymatic input to be used for creating a diagram.'>
                <Image src={sankeymaticIcon} alt="Sankeymatic Icon" boxSize="30px" onClick={onOpen} />
            </Tooltip>

            <Modal isOpen={isOpen} onClose={onClose}>
                <ModalOverlay />
                <ModalContent>
                    <ModalHeader>Sankeymatic</ModalHeader>
                    <ModalCloseButton />
                    <ModalBody>
                        <Text fontSize="md" color={textColor} mb='10px'>
                            The following input is a auto generated Sankeymatic input regarding the all job applications, statuses and statistics displayed in this app.
                            Copy the input and paste it into the Sankeymatic website to generate a diagram.
                        </Text>
                        <Textarea
                            value={sankeymaticInputDiagram}
                            onChange={setInput}
                            placeholder='Sankeymatic Input'
                            size='md'
                        />
                    </ModalBody>
                    <ModalFooter>
                    <Button colorScheme='blue' mr={3} onClick={copyInputDiagram}>
                        Copy Diagram Input
                    </Button>
                    <Tooltip label="https://www.sankeymatic.com/build/">
                        <Link href="https://www.sankeymatic.com/build/" isExternal>
                            Go to Website
                        </Link>
                    </Tooltip>
                </ModalFooter>
                </ModalContent>
            </Modal>
        </>

    )
}