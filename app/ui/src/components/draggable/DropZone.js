import React from 'react';

// context
import { DragContext } from './Drag';

// listens for drags over drop zones
function DropZone({ as, dropId, dropType, remember, style, children, ...props }) {
  const { dragItem, dragType, setDrop, drop, onDrop } = React.useContext(DragContext);
  
  function handleDragOver(e) {
    if (e.preventDefault) {
      e.preventDefault();
    }
    return false;
  };

  function handleLeave() {
    if (!remember) {
      setDrop(null); 
    }
  };

  let Component = as || "div";
  return ( 
    <Component onDragEnter={(e) => dragItem && dropType === dragType && setDrop(dropId)} onDragOver={handleDragOver} onDrop={onDrop} style={{position: "relative", ...style}} {...props}>
      { children }
      { drop === dropId && <div style={{position: "absolute", inset: "0px"}} onDragLeave={handleLeave}></div> }
    </Component>
  );
};

export default DropZone;