import React, { useState, useEffect } from "react";
// Chakra imports
import {
  Flex,
  Table,
  Tbody,
  Text,
  Th,
  Thead,
  Tr,
  useColorModeValue,
  Grid,
  Spacer,
  useDisclosure,
} from "@chakra-ui/react";
// Custom components
import Card from "../../template/components/Card/Card.js";
import CardBody from "../../template/components/Card/CardBody.js";
import CardHeader from "../../template/components/Card/CardHeader.js";
import JobApplicationTableRow from "../../template/components/Tables/JobApplicationTableRow.js";
import { motion } from "framer-motion";

import { get as getJobOffers, find } from "../../api/api_endpoints/job_offer_api.js"
import { get as getStatus } from "../../api/api_endpoints/status_api.js"
import { AddJobOffer } from "../../popups/add_job_offer.js"
import { MoreInfo } from "./more_info.js";
import { ManageJobStatus } from "../../popups/manage_job_status.js";
import { SearchBar } from "../../template/components/Navbars/SearchBar/SearchBar.js";


export const Home = () => {
  const textColor = useColorModeValue("gray.700", "white");
  const borderColor = useColorModeValue("gray.200", "gray.600");
  const bgProfile = useColorModeValue("hsla(0,0%,100%,.8)", "navy.800");
  const borderProfileColor = useColorModeValue("white", "transparent");

  const [status, setStatus] = useState([])
  const [jobOffers, setJobOffers] = useState([]);
  const [originalJobOffers, setOriginalJobOffers] = useState([]);
  const [currentJobOffer, setCurrentJobOffer] = useState({});

  const { isOpen, onToggle } = useDisclosure();
  const [hidden, setHidden] = useState(!isOpen)

  const moreAction = (id) => {
    setCurrentJobOffer(jobOffers.find((jobOffer) => { return jobOffer.id === id }));

    if (!isOpen) {
      onToggle();
    } else {
      if (id === currentJobOffer.id) {
        onToggle();
      }
    }
  }

  const getStatusById = (id) => {
    return status.find((status) => { return status.id === id });
  }

  const [fetchDataAgain, setFetchDataAgain] = useState(true);
  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await getJobOffers({});
        setGlobalJobOffers(response);

        const statusResponse = await getStatus({});
        setStatus(statusResponse);
      } catch (error) {
        console.error("Error fetching job offers:", error);
      }
    };

    if (fetchDataAgain) {
      fetchData();
      setFetchDataAgain(false);
    }
  }, [fetchDataAgain]);

  const setGlobalJobOffers = (response) => {
    setJobOffers(response);
    setOriginalJobOffers(response);
  }

  const findText = async (text) => {
    console.log(text);

    if (text === '') {
      setJobOffers(originalJobOffers);
      return;
    }

    const response = await find(text);
    setJobOffers(response);
  }

  return (
    <Flex direction="column" pt={{ base: "120px", md: "75px" }} >
      <Flex
        direction={{ sm: "column", md: "row" }}
        mb='24px'
        maxH='330px'
        justifyContent='right'
        // align='center'
        backdropFilter='blur(21px)'
        boxShadow='0px 2px 5.5px rgba(0, 0, 0, 0.02)'
        border='1.5px solid'
        borderColor={borderProfileColor}
        bg={bgProfile}
        p='24px'
        borderRadius='20px'
        gap='20px'>

        <SearchBar me='18px' findText={findText} />

        <AddJobOffer setFetchDataAgain={setFetchDataAgain} />

        <ManageJobStatus />

      </Flex>
      <Grid
        templateColumns={{ sm: "1fr", lg: `${hidden ? "1fr" : "2fr 1fr"}` }}
        templateRows={{ lg: "repeat(1, auto)" }}
        gap='20px'>
        <Card overflowX={{ sm: jobOffers?.length === 0 ? "hidden" : "scroll", xl: "hidden" }} pb="0px">
          <CardHeader p="6px 0px 22px 0px">
            <Flex flexDirection='row'>
              <Text fontSize="xl" color={textColor} fontWeight="bold">
                Job Applications
              </Text>

            </Flex>
          </CardHeader>
          <CardBody>
            <Table variant="simple" color={textColor}>
              <Thead>
                <Tr my=".8rem" pl="0px" color="gray.400" >
                  <Th pl="0px" borderColor={borderColor} color="gray.400" >
                    Company
                  </Th>
                  <Th borderColor={borderColor} color="gray.400" >Role</Th>
                  <Th borderColor={borderColor} color="gray.400" >Location</Th>
                  <Th borderColor={borderColor} color="gray.400" >Applied At</Th>
                  <Th borderColor={borderColor} color="gray.400" >Status</Th>
                  <Th borderColor={borderColor}></Th>
                </Tr>
              </Thead>
              <Tbody>
                {jobOffers?.length > 0 && status?.length > 0 && jobOffers.map((row, index) => {
                  return (
                    <JobApplicationTableRow
                      company={row.company}
                      role={row.role}
                      location={row.location}
                      status={getStatusById(row.statusId)}
                      applied={row.appliedAt}
                      moreAction={() => moreAction(row.id)}
                      isLast={index === jobOffers.length - 1 ? true : false}
                      key={index}
                    />
                  );
                })}
                {jobOffers?.length === 0 &&
                  <Tr style={{ textAlign: 'center' }}>
                    <Text fontSize='md' color='gray.400' fontWeight='400' p="15px">
                      No Job Applications added yet.
                    </Text>
                  </Tr>
                }
              </Tbody>
            </Table>
          </CardBody>
        </Card>

        <motion.div
          hidden={hidden}
          initial={false}
          onAnimationStart={() => setHidden(false)}
          onAnimationComplete={() => setHidden(!isOpen)}
          animate={{ width: isOpen ? 500 : 0 }}
          style={{
            overflow: 'hidden',
            whiteSpace: 'nowrap',
            position: 'relative',
          }}
        >
          <MoreInfo
            key={currentJobOffer.id}
            currentJobOffer={currentJobOffer}
            currentStatus={getStatusById(currentJobOffer.statusId)}
            jobOffers={jobOffers}
            setJobOffers={setGlobalJobOffers}
            onToggle={onToggle}
            isOpen={isOpen} />
        </motion.div>
      </Grid>

    </Flex>
  );
}

export default Home;
