import React, { useEffect, useRef, useState } from 'react';
import { Network } from 'vis-network';
import 'vis-network/styles/vis-network.css';

import { getStatusByJobOfferId, get } from '../../api/api_endpoints/status_api';

export const StatusGraph = ({ jobOfferId }) => {
    const containerRef = useRef(null);
    let network = null;
    
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
          
    }, []);

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

            if(nextElement){
                const date = new Date(nextElement.changedAt[0], nextElement.changedAt[1] - 1, nextElement.changedAt[2], nextElement.changedAt[3], nextElement.changedAt[4], 0).toISOString().substring(0, 10)
                edges = [...edges, { from: currentStatus.id, to: nextElement.id, arrows: 'to', label: date }]
            }
        }
        
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
    }

    return (
        <div ref={containerRef} key={jobOfferId} style={{ width: '450px', height: '400px' }} />
    )
}