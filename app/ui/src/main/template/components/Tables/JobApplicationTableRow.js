import {
  Badge,
  Button,
  Flex,
  Td,
  Text,
  Tr,
  useColorModeValue
} from "@chakra-ui/react";
import React from "react";
import { formatDistanceToNow } from 'date-fns';

function JobApplicationTableRow(props) {
  const { company, role, location, status, applied, isLast, moreAction } = props;
  
  const textColor = useColorModeValue("gray.500", "white");
  const titleColor = useColorModeValue("gray.700", "white");
  const borderColor = useColorModeValue("gray.200", "gray.600");

  const formatNumber = (num) => {
    return num < 10 ? `0${num}` : num;
  };

  const getDate = () => {
    if(applied === undefined || applied?.length < 0 || applied === 'Invalid Date Time'){
      return null
    }

    var date = new Date(applied[0], applied[1] - 1, applied[2], applied[3], applied[4], 0)
    return new Date(date).toLocaleString() + ' UTC'
  }
  
  return (
    <Tr>
      <Td
        pl="0px"
        borderColor={borderColor}
        borderBottom={isLast ? "none" : null}>
        <Text fontSize="md" color={textColor} fontWeight="bold">
            {company}
          </Text>
      </Td>
      <Td borderColor={borderColor} borderBottom={isLast ? "none" : null}>
        <Flex direction="column">
          <Text fontSize="md" color={textColor} fontWeight="bold">
            {role}
          </Text>
        </Flex>
      </Td>
      <Td borderColor={borderColor} borderBottom={isLast ? "none" : null}>
      <Text fontSize="md" color={textColor} fontWeight="bold" pb=".5rem">
          {location}
        </Text>
      </Td>
      <Td borderColor={borderColor} borderBottom={isLast ? "none" : null}>
      <Flex align="center" py=".8rem" minWidth="100%" flexWrap="nowrap">
          <Flex direction="column">
            <Text
              fontSize="md"
              color={titleColor}
              fontWeight="bold"
              minWidth="100%"
            >
              {(applied !== undefined && applied !== 'Invalid Date Time' && applied[0] > 0) && applied[0] + '-' + formatNumber(applied[1]) + '-' + formatNumber(applied[2])}
              {(applied === undefined || applied === 'Invalid Date Time' || applied[0] < 0) && 'Not yet Applied'}
            </Text>
            <Text fontSize="sm" color="gray.400" fontWeight="normal">
              {(applied !== undefined && applied !== 'Invalid Date Time' && applied[0] > 0) && 'Applied ' + formatDistanceToNow(getDate(), { addSuffix: true, includeSeconds: true })}
            </Text>
          </Flex>
        </Flex>
      </Td>
      <Td borderColor={borderColor} borderBottom={isLast ? "none" : null}>
        <Badge
          bg={status === undefined ? 'white' : status.color}
          color="black"
          fontSize="16px"
          p="3px 10px"
          borderRadius="8px"
        >
          {status !== undefined && status.name}
          {status === undefined && 'No Status'}
        </Badge>
      </Td>
      <Td borderColor={borderColor} borderBottom={isLast ? "none" : null}>
        <Button p="0px" bg="transparent" variant="no-effects" onClick={moreAction}>
          <Text
            fontSize="md"
            color="gray.400"
            fontWeight="bold"
            cursor="pointer">
            More
          </Text>
        </Button>
      </Td>
    </Tr>
  );
}

export default JobApplicationTableRow;
