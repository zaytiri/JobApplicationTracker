import React, { useEffect, useRef, useState } from 'react';
import { Network } from 'vis-network';
import 'vis-network/styles/vis-network.css';

import { getStatusByJobOfferId, get } from '../../api/api_endpoints/status_api';
import { Menu, Item, useContextMenu } from 'react-contexify';
import 'react-contexify/ReactContexify.css';


export const StatusGraph = ({ jobOfferId }) => {
    const [nodes, setNodes] = useState([])
    const [edges, setEdges] = useState([])
    const [isRemoved, setIsRemoved] = useState(false)
    const containerRef = useRef(null);
    let network = null;
    
    const MENU_ID = 'NODE_MENU';

    const { show } = useContextMenu({
        id: MENU_ID,
    });

    function handleContextMenu(event, id) {
        show({
            event,
            props: {
                key: id
            }
        })
    }

    const removeNode = ({ props }) => {
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
        setIsRemoved(false)
    }, [isRemoved]);

    const convertResponseToProperNodeNetwork = (data, statusNamesData) => {
        let nodes = []
        let edges = []

        const sortedByDay = data.sort((a, b) => new Date(a.changedAt) - new Date(b.changedAt));

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
                const date = new Date(nextElement.changedAt[0], nextElement.changedAt[1] - 1, nextElement.changedAt[2], nextElement.changedAt[3], nextElement.changedAt[4], 0).toISOString().substring(0, 10)
                edges = [...edges, { from: currentStatus.id, to: nextElement.id, arrows: 'to', label: date }]
            }
        }

        setNodes(nodes)
        setEdges(edges)
        return constructNetwork(nodes, edges)
    }

    const getStatusById = (id, status) => {
        return status.find((status) => { return status.id === id });
    }

    const constructNetwork = (nodes, edges) => {
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
                handleContextMenu(params.event, nodeId)
            }
        });
    }

    return (
        <div>
            <Menu id={MENU_ID}>
                <Item onClick={removeNode}>Remove</Item>
            </Menu>
            <div ref={containerRef} key={jobOfferId} style={{ width: '450px', height: '400px' }} />
        </div>
    )
}