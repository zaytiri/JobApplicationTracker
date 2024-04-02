import React from 'react';
import {
  Text,
  useColorModeValue,
} from "@chakra-ui/react";

import Card from "../../template/components/Card/Card";
import CardBody from "../../template/components/Card/CardBody";
import CardHeader from "../../template/components/Card/CardHeader";

export const List = ({ name, children }) => {
  const textColor = useColorModeValue("gray.700", "white");

  return (
    <Card p='16px'>
      <CardHeader p='12px 5px' mb='12px'>
        <Text fontSize='lg' color={textColor} fontWeight='bold'>
          {name}
        </Text>
      </CardHeader>
      <CardBody px='5px'>
          {children}
      </CardBody>
    </Card>
  );
};