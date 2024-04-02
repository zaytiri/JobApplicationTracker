import React, { useState } from "react";
// Chakra imports
import {
  Flex,
  Text,
  useColorModeValue,
  Badge,
  Button,
  Spacer,
  Collapse,
} from "@chakra-ui/react";
// Custom components
import Card from "../../template/components/Card/Card.js";
import CardBody from "../../template/components/Card/CardBody.js";
import CardHeader from "../../template/components/Card/CardHeader.js";

import { remove } from "../../api/api_endpoints/job_offer_api.js"

export const MoreInfo = ({currentJobOffer, jobOffers, setJobOffers, onToggle}) => {
  const textColor = useColorModeValue("gray.700", "white");

  const formatDescription = (text) => {
    if (typeof text === 'undefined') {
      return null;
    }

    const stringArray = text.split('\n');

    const elements = [];

    for (let i = 0; i < stringArray.length; i++) {
      elements.push(<span>{stringArray[i]}<br /></span>);
    }

    return elements;
  }
  const [show, setShow] = useState(false)
  const handleToggleToShowDescription = () => setShow(!show)

  const removeJobOffer = async (id) => {
    const response = await remove(id);
    
    if (response.success === false) return null;

    onToggle();
    setJobOffers(jobOffers.filter((jobOffer) => jobOffer.id !== id ));
  }

    return (
        <Card p='15px' maxW={{ sm: "320px", md: "100%" }} h="100%">
            <CardHeader p='12px 5px' mb='12px'>
              <Flex >
                <Text fontSize='lg' color={textColor} fontWeight='bold'>
                  {currentJobOffer.company}
                </Text>
                <Spacer />
                <Badge
                  bg="green"
                  color="black"
                  fontSize="16px"
                  p="3px 10px"
                  borderRadius="8px">
                  {currentJobOffer.status}
                </Badge>
              </Flex>
            </CardHeader>
            <CardBody px='5px' height="100%">
              <Flex direction='column' height="100%">
                <Text fontSize='md' color='gray.400' fontWeight='400' mb='30px' style={{ whiteSpace: 'wrap' }}>
                  <Collapse startingHeight={50} in={show} key={currentJobOffer.id}>
                    {formatDescription(currentJobOffer.description)}
                  </Collapse>
                  <Button size='sm' onClick={handleToggleToShowDescription} mt='1rem'>
                    Show {show ? 'Less' : 'More'}
                  </Button>
                </Text>
                <Flex mb='18px' direction="column">
                  <Text
                    fontSize='md'
                    color={textColor}
                    fontWeight='bold'
                    me='10px'>
                    Role:{" "}
                  </Text>
                  <Text fontSize='md' color='gray.400' fontWeight='400' mb='30px' style={{ whiteSpace: 'wrap' }}>
                    {currentJobOffer.role}
                  </Text>
                </Flex>
                <Flex mb='18px' direction="column">
                  <Text
                    fontSize='md'
                    color={textColor}
                    fontWeight='bold'
                    me='10px'>
                    Location:{" "}
                  </Text>
                  <Text fontSize='md' color='gray.400' fontWeight='400' mb='30px' style={{ whiteSpace: 'wrap' }}>
                    {currentJobOffer.location}
                  </Text>
                </Flex>
                <Flex mb='18px' direction="column">
                  <Text
                    fontSize='md'
                    color={textColor}
                    fontWeight='bold'
                    me='10px'>
                    Company Website:{" "}
                  </Text>
                  <Text fontSize='md' color='gray.400' fontWeight='400' mb='30px' style={{ whiteSpace: 'wrap' }}>
                    {currentJobOffer.companyWebsite}
                  </Text>
                </Flex>
                <Flex mb='18px' direction="column">
                  <Text
                    fontSize='md'
                    color={textColor}
                    fontWeight='bold'
                    me='10px'>
                    Job Offer Link:{" "}
                  </Text>
                  <Text fontSize='md' color='gray.400' fontWeight='400' mb='30px' style={{ whiteSpace: 'wrap' }}>
                    {currentJobOffer.link}
                  </Text>
                </Flex>
                <Flex mb='18px' direction="column">
                  <Text
                    fontSize='md'
                    color={textColor}
                    fontWeight='bold'
                    me='10px'>
                    Interview Notes:{" "}
                  </Text>
                  <Text fontSize='md' color='gray.400' fontWeight='400' mb='30px' style={{ whiteSpace: 'wrap' }}>
                    {currentJobOffer.interviewNotes}
                  </Text>
                </Flex>
                <Spacer />
                <Flex>
                  <Button variant="primary">
                    EDIT
                  </Button>
                  <Spacer />
                  <Button variant="danger" onClick={() => removeJobOffer(currentJobOffer.id)}>
                    DELETE
                  </Button>
                </Flex>
              </Flex>
            </CardBody>
          </Card>
    )
}