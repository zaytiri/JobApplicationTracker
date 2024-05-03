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
  Tooltip,
  Center,
} from "@chakra-ui/react";

import {
  RepeatIcon,
  DeleteIcon,
  CopyIcon,
  ExternalLinkIcon,
  QuestionOutlineIcon,
  ViewOffIcon,
  ViewIcon,
} from '@chakra-ui/icons'

import copy from 'clipboard-copy';

import { toast, Zoom } from 'react-toastify';

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

  const [showGraph, setShowGraph] = useState(false)
  const handleToggleToShowGraph = () => setShowGraph(!showGraph)

  const removeJobOffer = async (id) => {
    const response = await remove(id);

    if (response.success === false) return null;

    onToggle();
    setJobOffers(jobOffers.filter((jobOffer) => jobOffer.id !== id));

    toast.success('Job application was removed.', {
      position: "top-center",
      autoClose: 5000,
      hideProgressBar: false,
      closeOnClick: true,
      pauseOnHover: true,
      draggable: true,
      progress: undefined,
      theme: "colored",
      transition: Zoom,
      });
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

  const copyToClipboard = () => {
    copy(currentJobOffer.link)
      .then(() => {
        toast.info('URL copied to Clipboard.', {
          position: "top-center",
          autoClose: 5000,
          hideProgressBar: false,
          closeOnClick: true,
          pauseOnHover: true,
          draggable: true,
          progress: undefined,
          theme: "colored",
          transition: Zoom,
          });
      })
      .catch((error) => {
        toast.error('Some error occurred when copying URL to Clipboard.', {
          position: "top-center",
          autoClose: 5000,
          hideProgressBar: false,
          closeOnClick: true,
          pauseOnHover: true,
          draggable: true,
          progress: undefined,
          theme: "colored",
          transition: Zoom,
          });
      });
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
            <Tooltip label='Click here to check updates from the current job application.'>
              <RepeatIcon boxSize={5} color="black.500" />
            </Tooltip>
            <Spacer />
            <EditJobOffer
              currentJobOffer={currentJobOffer}
              setFetchDataAgain={setFetchDataAgain}
              closeModal={onToggle} />
            <Spacer />
            <Tooltip label="Click here to copy the job application URL to the clipboard.">
              <CopyIcon boxSize={5} color="black.500" onClick={copyToClipboard} />
            </Tooltip>
            <Spacer />
            <Tooltip label="Click here to open the job application URL. When opening any link, if it shows an homepage or similar instead of opening the job application URL, exit the sub-window and try again.">
              <Link href={currentJobOffer.link} isExternal>
                <ExternalLinkIcon boxSize={5} color="black.500" />
              </Link>
            </Tooltip>
            <Spacer />
            <Tooltip label="Click here to remove this job application.">
              <DeleteIcon boxSize={5} color="red.500" onClick={() => removeJobOffer(currentJobOffer.id)} />
            </Tooltip>
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

        <Center p="10px">
          <Tooltip label='Show/Hide job application activities.'>
            {showGraph ? <ViewOffIcon boxSize={8} onClick={handleToggleToShowGraph} /> : <ViewIcon boxSize={8} onClick={handleToggleToShowGraph} />}
          </Tooltip>
          <Tooltip label='This section aims to show all activities from a job application. To remove a status or edit a label, said component needs to be selected first and if right-clicked, a context menu will appear with more options.'>
            <QuestionOutlineIcon />
          </Tooltip>
        </Center >

        {showGraph && isOpen &&
          <StatusGraph
            jobOfferId={currentJobOffer.id}
            setFetchDataAgain={setFetchDataAgain}
            closeModal={onToggle} />}

        <Flex direction='column' height="100%">
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
        </Flex>
      </CardBody>
    </Card>
  )
}