import React, { useEffect, useRef, useState } from 'react';
import { Network } from 'vis-network';
import 'vis-network/styles/vis-network.css';

import {
    useDisclosure,
    Modal,
    ModalOverlay,
    ModalContent,
    ModalHeader,
    ModalFooter,
    ModalBody,
    ModalCloseButton,
    Button,
    Input
} from '@chakra-ui/react'

import { Menu, Item, useContextMenu } from 'react-contexify';
import 'react-contexify/ReactContexify.css';

import { getStatusByJobOfferId, get, removeStatusFromJobOffer, editStatusFromJobOffer } from '../../api/api_endpoints/status_api';
import { Zoom, toast } from 'react-toastify';

export const StatusGraph = ({ jobOfferId, setFetchDataAgain, closeModal }) => {
    const [nodes, setNodes] = useState([])
    const [edges, setEdges] = useState([])
    const [nodeIdToEdit, setNodeIdToEdit] = useState(0)
    const [changedEdgeLabel, setChangedEdgeLabel] = useState('')

    const [isNetworkChanged, setIsNetworkChanged] = useState(false)
    const containerRef = useRef(null);
    let network = null;

    const { isOpen, onOpen, onClose } = useDisclosure()

    const NODE_MENU_ID = 'NODE_MENU';

    const { show } = useContextMenu();

    const handleContextMenu = (event, menuId, props) => {
        show({
            id: menuId,
            event: event,
            props: props,
          })
    }

    const removeNode = ({ props }) => {
        removeStatusFromJobOffer(props.key)
        setIsNetworkChanged(true)
        setFetchDataAgain(true)
        closeModal()

        toast.success('Status was removed from Job Application.', {
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

    const openModalToEditEdge = ({ props }) => {
        setNodeIdToEdit(props.key)
        setChangedEdgeLabel(new Date(props.date).toISOString().split('.')[0])
        onOpen()
    }

    const editEdge = async () => {        
        const response = await editStatusFromJobOffer(nodeIdToEdit, {date: changedEdgeLabel})

        if (response.success === false) return null;

        setIsNetworkChanged(true)
        onClose()

        toast.success('Date was updated.', {
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
                const response = await getStatusByJobOfferId(jobOfferId);
                const statusResponse = await get({});

                convertResponseToProperNodeNetwork(response, statusResponse);

            } catch (error) {
                console.log(error)
            }
        };

        fetchData();
        setIsNetworkChanged(false)
    }, [isNetworkChanged]);

    const convertResponseToProperNodeNetwork = (data, statusNamesData) => {
        let nodes = []
        let edges = []

        const sortedByDay = data.sort((a, b) => new Date(a.changedAt[0], a.changedAt[1] - 1, a.changedAt[2], a.changedAt[3], a.changedAt[4], 0) - new Date(b.changedAt[0], b.changedAt[1] - 1, b.changedAt[2], b.changedAt[3], b.changedAt[4], 0));
        for (let index = 0; index < sortedByDay.length; index++) {
            const currentStatus = sortedByDay[index];
            const nextElement = sortedByDay[index + 1];

            const currentStatusObject = getStatusById(currentStatus.statusId, statusNamesData);

            nodes = [...nodes, {
                id: currentStatus.id,
                label: currentStatusObject.name,
                color: {
                    background: currentStatusObject.color
                },
                shape: 'box'
            }]

            if (nextElement) {
                const date = nextElement.changedAt[0] + "-" + nextElement.changedAt[1].toString().padStart(2, '0') + "-" + nextElement.changedAt[2].toString().padStart(2, '0');
                edges = [...edges, { from: currentStatus.id, to: nextElement.id, arrows: 'to', label: date }]
            }
        }

        setNodes(nodes)
        setEdges(edges)
        return constructNetwork(nodes, edges, data)
    }

    const getStatusById = (id, status) => {
        return status.find((status) => { return status.id === id });
    }

    const constructNetwork = (nodes, edges, statuses) => {
        const data = { nodes, edges };
        const options = {
            interaction: {
                zoomView: false
            }
        };
        network = new Network(containerRef.current, data, options);

        network.on("oncontext", function (params) {
            if (params.nodes.length > 0) {
                const nodeId = params.nodes[0];
                const node = getStatusById(nodeId, statuses);
                const date = node.changedAt[0] + "-" + node.changedAt[1].toString().padStart(2, '0') + "-" + node.changedAt[2].toString().padStart(2, '0');

                handleContextMenu(params.event, NODE_MENU_ID, {key: nodeId, date: date})
            }
        });
    }

    return (
        <div>
            <>
                <Modal isOpen={isOpen} onClose={onClose}>
                    <ModalOverlay />
                    <ModalContent>
                        <ModalHeader>Edit Edge</ModalHeader>
                        <ModalCloseButton />
                        <ModalBody>
                            <Input type="datetime-local" onChange={(event) => setChangedEdgeLabel(event.target.value)} value={changedEdgeLabel} />
                        </ModalBody>

                        <ModalFooter>
                            <Button colorScheme='blue' mr={3} onClick={editEdge}>
                                Edit
                            </Button>
                        </ModalFooter>
                    </ModalContent>
                </Modal>
            </>

            <Menu id={NODE_MENU_ID}>
                <Item onClick={removeNode}>Remove</Item>
                <Item onClick={openModalToEditEdge}>Change Date</Item>
            </Menu>
            <div ref={containerRef} key={jobOfferId} style={{ width: '450px', height: '400px' }} />
        </div>
    )
}