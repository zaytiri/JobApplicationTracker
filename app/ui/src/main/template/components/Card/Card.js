import { Box, useStyleConfig } from "@chakra-ui/react";
import React, { Component }  from 'react';
function Card(props) {
  const { variant, children, ...rest } = props;
  const styles = useStyleConfig("Card", { variant });
  // Pass the computed styles into the `__css` prop
  return (
    <Box __css={styles} {...rest} backgroundColor="white">
      {children}
    </Box>
  );
}

export default Card;
