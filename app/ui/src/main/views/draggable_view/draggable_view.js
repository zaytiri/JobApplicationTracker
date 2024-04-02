import React, {useEffect, useState} from "react";
import {
  Grid,
  Flex,
} from "@chakra-ui/react";

import { List } from "./list";
import { Element } from "./element";
import Drag from '../../../components/draggable';
import { getJobOffersByStatus } from "../../api/api_endpoints/job_offer_api.js"

export const DraggableView = () => { 
  const [data, setData] = useState([]);

  const [fetchDataAgain, setFetchDataAgain] = useState(true);
  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await getJobOffersByStatus();
        setData(response);
        console.log(response);
      } catch (error) {
        console.error("Error fetching job offers:", error);
      }
    };

    if (fetchDataAgain) {
      fetchData();
      setFetchDataAgain(false);
    }
  }, [fetchDataAgain]);

  function handleDrop({ dragItem, dragType, drop }) {
    if (dragType === "element") {
      let [newListPosition, newCardPosition] = drop.split("-").map((string) => parseInt(string));
      let newData = structuredClone(data); // deep clone

      let oldCardPosition;
      let oldListPosition = data.findIndex((list) => {
        oldCardPosition = list.cards.findIndex((card) => card.id === dragItem);
        return oldCardPosition >= 0;
      });

      let card = data[oldListPosition].cards[oldCardPosition];
      if (newListPosition === oldListPosition && oldCardPosition < newCardPosition) {
        newCardPosition--;
      }

      newData[oldListPosition].cards.splice(oldCardPosition, 1);
      newData[newListPosition].cards.splice(newCardPosition, 0, card);

      setData(newData);

    } else if (dragType === "list") {
      let newListPosition = drop;
      let oldListPosition = data.findIndex((list) => list.id === dragItem);

      let newData = structuredClone(data); // deep clone
      let list = data[oldListPosition];
      if (oldListPosition < newListPosition) {
        newListPosition--;
      }
      newData.splice(oldListPosition, 1);
      newData.splice(newListPosition, 0, list);

      setData(newData);
    }
  };

  return (
    <Flex direction='column' pt={{ base: "120px", md: "75px", lg: "100px" }}>
      <Drag handleDrop={handleDrop}>
        {({ activeItem, activeType, isDragging }) => (
          <Drag.DropZone className="mx-n2 overflow-auto h-100">
            <Grid templateColumns={{ sm: "repeat(auto-fit, minmax(0, 1fr))", xl: "repeat(auto-fit, minmax(0, 1fr))" }} gap='22px'>
              {data.map((list, listPosition) => {
                return (
                  <Flex direction='column'>
                    <React.Fragment key={list.id}>
                      <Drag.DropZone dropId={listPosition} dropType="list" remember={true} >
                        <Drag.DropGuide dropId={listPosition} dropType="list" className="rounded-xl bg-secondary h-96 mx-2 my-5 w-80 flex-shrink-0 flex-grow-0" />
                      </Drag.DropZone>
                      <Drag.DropZones className="d-flex flex-column vh-100" key={list.id} prevId={listPosition} nextId={listPosition + 1} dropType="list" split="x" remember={true}>
                        <Drag.DragItem
                          key={list.id}
                          dragId={list.id}
                          className={`${activeItem === list.id && activeType === "list" && isDragging ? "d-none" : ""}`}
                          dragType="list">
                          <List key={list.id} name={list.name}>
                            {data[listPosition].cards.map((card, cardPosition) => {
                              return (
                                <Drag.DropZones key={card.id} prevId={`${listPosition}-${cardPosition}`} nextId={`${listPosition}-${cardPosition + 1}`} dropType="element" remember={true}>
                                  <Drag.DropGuide dropId={`${listPosition}-${cardPosition}`} dropType="element" className="rounded-xl bg-secondary h-96 mx-2 my-5 w-80 flex-shrink-0 flex-grow-0" />
                                  <Drag.DragItem
                                    key={card.id}
                                    dragId={card.id}
                                    className={`${activeItem === card.id && activeType === "element" && isDragging ? "d-none" : ""}`}
                                    dragType="element">
                                    <Element key={card.id} card={card} />
                                  </Drag.DragItem>
                                </Drag.DropZones>
                              );
                            })}
                            <Drag.DropZone dropId={`${listPosition}-${data[listPosition].cards.length}`} dropType="element" remember={true}>
                              <Drag.DropGuide dropId={`${listPosition}-${data[listPosition].cards.length}`} dropType="element" className="rounded-lg bg-secondary h-24 m-2" />
                            </Drag.DropZone>
                          </List>
                        </Drag.DragItem>
                        <Drag.DropZone dropId={`${listPosition}-${data[listPosition].cards.length}`} className="d-flex flex-grow-1" dropType="element" remember={true} />
                      </Drag.DropZones>
                    </React.Fragment>
                  </Flex>

                );
              })}
            </Grid>
          </Drag.DropZone>
        )}
      </Drag>
    </Flex>
  );
};
