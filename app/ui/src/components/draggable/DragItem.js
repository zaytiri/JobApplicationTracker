import React from 'react';

// context
import { DragContext } from './Drag';

// a draggable item
function DragItem({ as, dragId, dragType, ...props }) {

  console.log(DragContext);
  console.log(React.useContext(DragContext));

  const { draggable, dragStart, drag, dragEnd } = React.useContext(DragContext);

  let Component = as || "div";
  return <Component onDragStart={(e) => dragStart(e, dragId, dragType)} onDrag={drag} draggable={draggable} onDragEnd={dragEnd} {...props} />;
};

export default DragItem;