import React, { useEffect, useRef } from 'react';
import { Network } from 'vis-network';
import 'vis-network/styles/vis-network.css';

export const StatusGraph = () => {
    const containerRef = useRef(null);
    let network = null;

    useEffect(() => {
        const nodes = [
            { id: 1, label: 'Node 1' },
            { id: 2, label: 'Node 2' },
            { id: 3, label: 'Node 3' },
            { id: 4, label: 'Node 4' }
        ];

        const edges = [
            { from: 1, to: 2, arrows: 'to', label: '29/3/1997' },
            { from: 2, to: 3, arrows: 'to' },
            { from: 3, to: 4, arrows: 'to' },
        ];

        const data = { nodes, edges };
        const options = {
            interaction: {
                zoomView: false
            }
        };

        network = new Network(containerRef.current, data, options);

        return () => {
            if (network !== null) {
                network.destroy();
                network = null;
            }
        };
    }, []);

    return (
        <div ref={containerRef} style={{ width: '450px', height: '400px' }} />
    )
}