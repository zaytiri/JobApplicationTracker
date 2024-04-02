import React from 'react';

import {
  Flex,
  Text,
  useColorModeValue,
  Stat,
  StatLabel,
} from "@chakra-ui/react";

import Card from "../../template/components/Card/Card";
import IconBox from "../../template/components/Icons/IconBox";
import {
  WalletIcon,
} from "../../template/components/Icons/Icons.js";
export const Element = ({ card }) => {

  const iconBoxInside = useColorModeValue("white", "white");
  const iconBlue = useColorModeValue("blue.500", "blue.500");
  return (
    <Card mb="10px" boxShadow='2xl' rounded='md' bg='white'>
      <Flex direction='column'>
        <Flex
          flexDirection='row'
          align='center'
          justify='center'
          w='100%'
          mb='25px'>
          <Stat me='auto'>
            <StatLabel
              fontSize='xs'
              color='gray.400'
              fontWeight='bold'
              textTransform='uppercase'>
              {card.role}
            </StatLabel>
          </Stat>
          <IconBox
            borderRadius='50%'
            as='box'
            h={"45px"}
            w={"45px"}
            bg={iconBlue}>
            <WalletIcon h={"24px"} w={"24px"} color={iconBoxInside} />
          </IconBox>
        </Flex>
        <Text color='gray.400' fontSize='sm'>
          {card.company}
        </Text>
      </Flex>
    </Card>
  );
};