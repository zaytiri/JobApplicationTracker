import React from "react";

import { List } from "./list";
import { Element } from "./element";
import Container from 'react-bootstrap/Container';
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';
import Drag from '../components/draggable'


export const DraggableView = () => {
  //   // the dummy Trello-style content
  const dummyData = [
    {
      id: 1, name: "List 1", cards: [
        { id: 1, title: "Card 1" },
        { id: 2, title: "Card 2" },
        { id: 3, title: "Card 3" },
        { id: 4, title: "Card 4" },
        { id: 5, title: "Card 5" },
      ]
    },
    {
      id: 2, name: "List 2", cards: [
        { id: 6, title: "Card 6" },
        { id: 7, title: "Card 7" },
        { id: 8, title: "Card 8" },
      ]
    },
    {
      id: 3, name: "List 3", cards: [
      ]
    },
  ];

  const [data, setData] = React.useState(dummyData);

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
    <div className="p-4 d-flex flex-column vh-100">
      <Container>
        <Drag handleDrop={handleDrop}>
          {({ activeItem, activeType, isDragging }) => (
            <Drag.DropZone className="d-flex mx-n2 overflow-auto h-100">
              <Row>
                {data.map((list, listPosition) => {
                  return (
                    <Col key={list.id} >
                      <React.Fragment key={list.id}>
                        <Drag.DropZone dropId={listPosition} dropType="list" remember={true} >
                          <Drag.DropGuide dropId={listPosition} dropType="list" className="rounded-xl bg-secondary h-96 mx-2 my-5 w-80 flex-shrink-0 flex-grow-0"/>
                        </Drag.DropZone>
                        <Drag.DropZones key={list.id} className="d-flex flex-column vh-100" prevId={listPosition} nextId={listPosition + 1} dropType="list" split="x" remember={true}>
                          <Drag.DragItem
                            key={list.id}
                            dragId={list.id}
                            className={`${activeItem === list.id && activeType === "list" && isDragging ? "d-none" : "translate-x-0"}`}
                            dragType="list">
                            <List key={list.id} name={list.name}>
                              {data[listPosition].cards.map((card, cardPosition) => {
                                return (
                                  <Row key={card.id}>
                                    <Drag.DropZones key={card.id} prevId={`${listPosition}-${cardPosition}`} nextId={`${listPosition}-${cardPosition + 1}`} dropType="element" remember={true}>
                                      <Drag.DropGuide dropId={`${listPosition}-${cardPosition}`} className="rounded-xl bg-secondary h-96 mx-2 my-5 w-80 flex-shrink-0 flex-grow-0" dropType="element" />
                                      <Drag.DragItem
                                        key={card.id}
                                        dragId={card.id}
                                        className={`${activeItem === card.id && activeType === "element" && isDragging ? "d-none" : "translate-x-0"}`}
                                        dragType="element">
                                        <Element key={card.id} title={card.title} />
                                      </Drag.DragItem>
                                    </Drag.DropZones>
                                  </Row>
                                );
                              })}
                              <Drag.DropZone dropId={`${listPosition}-${data[listPosition].cards.length}`} dropType="element" remember={true}>
                                <Drag.DropGuide dropId={`${listPosition}-${data[listPosition].cards.length}`} className="rounded-lg bg-secondary h-24 m-2" dropType="element" />
                              </Drag.DropZone>
                            </List>
                          </Drag.DragItem>
                          <Drag.DropZone dropId={`${listPosition}-${data[listPosition].cards.length}`} className="d-flex flex-grow-1" dropType="element" remember={true} />
                        </Drag.DropZones>
                      </React.Fragment>
                    </Col>
                  );
                })}
              </Row>
              <Drag.DropZone dropId={data.length} dropType="list" remember={true}>
                <Drag.DropGuide dropId={data.length} dropType="list" className="rounded-xl bg-secondary h-96 mx-2 my-5 w-80 d-flex flex-shrink-0 flex-grow-0" />
              </Drag.DropZone>
            </Drag.DropZone>
          )}
        </Drag>
      </Container>
    </div>
  );
};
