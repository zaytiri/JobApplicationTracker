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
  Box,
  Link,
} from "@chakra-ui/react";

import Card from "../../template/components/Card/Card.js";
import CardBody from "../../template/components/Card/CardBody.js";
import CardHeader from "../../template/components/Card/CardHeader.js";

import { remove } from "../../api/api_endpoints/job_offer_api.js"
import { StatusGraph } from "./graph_view.js";
import { EditJobOffer } from "../../popups/edit_job_offer.js";

export const MoreInfo = ({ currentJobOffer, currentStatus, jobOffers, setJobOffers, onToggle, isOpen, setFetchDataAgain }) => {
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
    setJobOffers(jobOffers.filter((jobOffer) => jobOffer.id !== id));
  }

  const hexToRgb = (hex) => {
    hex = hex.replace(/^#/, '');

    const bigint = parseInt(hex, 16);
    const r = (bigint >> 16) & 255;
    const g = (bigint >> 8) & 255;
    const b = bigint & 255;

    return { r, g, b };
  }

  const hexToRgba = (hex, opacity) => {
    const { r, g, b } = hexToRgb(hex);
    return `rgba(${r}, ${g}, ${b}, ${opacity})`;
  }

  return (
    <Card p='15px' maxW={{ sm: "320px", md: "100%" }} h="100%" w='100%'>
      <CardHeader mb={{ base: "0px", lg: "20px" }} align='center'>
        <Box
          bgColor={hexToRgba(currentStatus === undefined ? '#FFFFFF' : currentStatus.color, 0.2)}
          borderRadius='16px'
          h='160px'
          w='100%'
          boxShadow='0px 7px 18px 3px rgba(0, 0, 0, 0.2)'
        >
          <Flex p='10px' alignItems='center'>
            <Button variant="primary" >
              Get updates
            </Button>
            <Spacer />
            <EditJobOffer
            currentJobOffer={currentJobOffer}
            setFetchDataAgain={setFetchDataAgain}
            closeModal={onToggle}
            />
          </Flex>
        </Box>

        <Flex mx='auto'
          h='100%'
          w='100%'
          mt='-66px'
          align='center'
          direction='column'>

          <Text fontSize='2xl' color={textColor} fontWeight='bold'>
            {currentJobOffer.company}
          </Text>
          <Text fontSize='lg' color={textColor} >
            {currentJobOffer.role}
          </Text>
          <Badge
            bg={currentStatus === undefined ? 'white' : currentStatus.color}
            color="black"
            fontSize="16px"
            borderRadius="8px"
          >
            {currentStatus !== undefined && currentStatus.name}
            {currentStatus === undefined && 'No Status'}
          </Badge>
        </Flex>
      </CardHeader>

      <CardBody px='5px' height="100%">

        {isOpen && <StatusGraph />}

        <Flex direction='column' height="100%">
          <Button variant="primary" mt='15px' mb='15px'>
            <Link href={currentJobOffer.link} isExternal>
              Go to Job Offer
            </Link>
          </Button>
          <Flex mb='10px' direction="column">
            <Text
              fontSize='md'
              color={textColor}
              fontWeight='bold'
              me='10px'>
              Location:{" "}
            </Text>
            <Text fontSize='md' color='gray.400' fontWeight='400' mb='15px' style={{ whiteSpace: 'wrap' }}>
              {currentJobOffer.location}
            </Text>
          </Flex>
          <Flex mb='10px' direction="column">
            <Text
              fontSize='md'
              color={textColor}
              fontWeight='bold'
              me='10px'>
              Company Website:{" "}
            </Text>
            <Text fontSize='md' color='gray.400' fontWeight='400' mb='15px' style={{ whiteSpace: 'wrap' }}>
              {currentJobOffer.companyWebsite}
            </Text>
          </Flex>
          <Flex mb='10px' direction="column">
            <Text
              fontSize='md'
              color={textColor}
              fontWeight='bold'
              me='10px'>
              Interview Notes:{" "}
            </Text>
            <Text fontSize='md' color='gray.400' fontWeight='400' mb='15px' style={{ whiteSpace: 'wrap' }}>
              {currentJobOffer.interviewNotes}
            </Text>
          </Flex>
          <Text fontSize='md' color='gray.400' fontWeight='400' mb='10px' style={{ whiteSpace: 'wrap' }}>
            <Text
              fontSize='md'
              color={textColor}
              fontWeight='bold'
              me='10px'>
              Description:{" "}
            </Text>
            <Collapse startingHeight={50} in={show} key={currentJobOffer.id}>
              {formatDescription(currentJobOffer.description)}
            </Collapse>
            <Button size='sm' onClick={handleToggleToShowDescription} mt='1rem'>
              Show {show ? 'Less' : 'More'}
            </Button>
          </Text>



          <Flex justifyContent='right'>
            <Button variant="danger" onChange={() => removeJobOffer(currentJobOffer.id)}>
              DELETE
            </Button>
          </Flex>
        </Flex>
      </CardBody>
    </Card>
  )
}