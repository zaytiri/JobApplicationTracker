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
  useDisclosure,
  Spacer,
  Tooltip,
  Spinner,
  Center,
  Button,
  Select,
  border,
} from "@chakra-ui/react";

import { ChevronUpIcon, ChevronDownIcon } from "@chakra-ui/icons";
import { useTable, useSortBy } from "react-table";

import Card from "../../template/components/Card/Card.js";
import CardBody from "../../template/components/Card/CardBody.js";
import CardHeader from "../../template/components/Card/CardHeader.js";
import JobApplicationTableRow from "../../template/components/Tables/JobApplicationTableRow.js";
import { motion } from "framer-motion";

import { get as getJobOffers, find, updateAllJobsStatus } from "../../api/api_endpoints/job_offer_api.js"
import { get as getStatus } from "../../api/api_endpoints/status_api.js"
import { AddJobOffer } from "../../popups/add_job_offer.js"
import { MoreInfo } from "./more_info.js";
import { ManageJobStatus } from "../../popups/manage_job_status.js";
import { SearchBar } from "../../template/components/Navbars/SearchBar/SearchBar.js";
import { RepeatIcon } from "@chakra-ui/icons";
import { StatusDropdown } from "../../popups/status_dropdown.js";

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

  const [loadingAllJobsUpdate, setLoadingAllJobsUpdate] = useState(false)
  const [loadingAllJobs, setLoadingAllJobs] = useState(true)

  const columns = React.useMemo(
    () => [
      {
        Header: "Company",
        accessor: "company",
      },
      {
        Header: "Role",
        accessor: "role",
      },
      {
        Header: "Location",
        accessor: "location",
      },
      {
        Header: "Applied At",
        accessor: "appliedAt",
        sortType: (rowA, rowB, columnId) => {
          // Custom sorting function for the appliedAt column
          const valueA = new Date(rowA.original.appliedAt[0], rowA.original.appliedAt[1] - 1, rowA.original.appliedAt[2], rowA.original.appliedAt[3], rowA.original.appliedAt[4], 0);
          const valueB = new Date(rowB.original.appliedAt[0], rowB.original.appliedAt[1] - 1, rowB.original.appliedAt[2], rowB.original.appliedAt[3], rowB.original.appliedAt[4], 0);

          return valueA > valueB ? 1 : valueA < valueB ? -1 : 0;
        }
      },
      {
        Header: "Status",
        accessor: "status",
      },
      {
        Header: "",
        accessor: "action",
      },
    ],
    []
  );

  const {
    getTableProps,
    getTableBodyProps,
    headerGroups,
    rows,
    prepareRow,
  } = useTable(
    {
      columns,
      data: jobOffers, // Pass your data here
    },
    useSortBy
  );

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
      }
    };

    if (fetchDataAgain) {
      fetchData();
      setFetchDataAgain(false);
    }

    setLoadingAllJobs(false)

  }, [fetchDataAgain]);

  const setGlobalJobOffers = (response) => {
    setJobOffers(response);
    setOriginalJobOffers(response);
  }

  const findText = async (text) => {
    if (text === '') {
      setJobOffers(originalJobOffers);
      return;
    }

    const response = await find(text);
    setJobOffers(response);
  }

  const checkAllJobs = async () => {
    setLoadingAllJobsUpdate(true)
    await updateAllJobsStatus(jobOffers.map(jo => jo.id))
      .then((res) => {
        setFetchDataAgain(true)
      });
    setLoadingAllJobsUpdate(false)
  }

  const [currentStatusId, setCurrentStatusId] = useState(0)

  const showOnlyStatus = (id) => {
    if(id === 0){
      clearFilters()
      return
    }
    setCurrentStatusId(id)
    setJobOffers(originalJobOffers.filter(jo => jo.statusId === Number(id)))
  }

  const clearFilters = () => {
    setCurrentStatusId(0)
    setJobOffers(originalJobOffers)
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
        gap='20px'
        alignItems='center'>

        {loadingAllJobsUpdate ?
          <Spinner
            display={loadingAllJobsUpdate ? 'block' : 'none'}
            align="center"
            thickness='4px'
            speed='0.65s'
            emptyColor='gray.200'
            color='blue.500'
            size='md'
            p="12px"
          />
          :
          <Tooltip label='Click here to check updates from all job applications.'>
            <RepeatIcon boxSize={8} color="black.500" onClick={checkAllJobs} />
          </Tooltip>}
        {/* <SankeymaticFeature/>  */}
        <Spacer />
        <AddJobOffer setFetchDataAgain={setFetchDataAgain} />
        <ManageJobStatus />
      </Flex>

      <Flex
        direction={{ sm: "column", md: "row" }}
        mb='24px'
        maxH='330px'
        backdropFilter='blur(21px)'
        boxShadow='0px 2px 5.5px rgba(0, 0, 0, 0.02)'
        border='1.5px solid'
        borderColor={borderProfileColor}
        bg={bgProfile}
        p='24px'
        borderRadius='20px'
        gap='20px'
        alignItems='center'>
        <SearchBar me='18px' findText={findText} />
        
        <Select fontSize='xs' bgColor='white' width='20%' onChange={(event) => showOnlyStatus(event.target.value)} placeholder='Select Status to filter by' value={currentStatusId}>
          {status?.length > 0 && status.map((row) => {
            return (
              <option value={row.id}>{row.name}</option>
            )
          })}
        </Select>

        <Spacer />
        <Flex justifyContent='right'>
          <Button variant='primary' onClick={clearFilters}>
            Clear Filters
          </Button>
        </Flex>
      </Flex>
      <Grid
        templateColumns={{ sm: "1fr", lg: `${hidden ? "1fr" : "2fr 1fr"}` }}
        templateRows={{ lg: "repeat(1, auto)" }}
        gap='20px'>
        <Card overflowX={{ sm: jobOffers?.length === 0 ? "hidden" : "scroll", xl: "hidden" }} pb="0px">
          <CardHeader p="6px 0px 22px 0px">
            <Flex flexDirection='row' gap='15px'>
              <Text fontSize="xl" color={textColor} fontWeight="bold">
                Job Applications
              </Text>
              <Spinner
                display={loadingAllJobs ? 'block' : 'none'}
                align="center"
                thickness='4px'
                speed='0.65s'
                emptyColor='gray.200'
                color='blue.500'
                size='md'
                p="10px"
              />
            </Flex>
          </CardHeader>
          <CardBody>

            <Table variant="simple" color={textColor} {...getTableProps()}>
              <Thead>
                {headerGroups.map((headerGroup) => (
                  <Tr {...headerGroup.getHeaderGroupProps()}>
                    {headerGroup.headers.map((column) => (
                      <Th
                        borderColor={borderColor}
                        {...column.getHeaderProps(column.getSortByToggleProps())}
                      >
                        <Flex alignItems="center">
                          <Text>{column.render("Header")}</Text>
                          {/* Add sorting indicators */}
                          {column.isSorted ? (
                            column.isSortedDesc ? (
                              <ChevronDownIcon ml={1} w={4} h={4} />
                            ) : (
                              <ChevronUpIcon ml={1} w={4} h={4} />
                            )
                          ) : (
                            ""
                          )}
                        </Flex>
                      </Th>
                    ))}
                  </Tr>
                ))}
              </Thead>
              <Tbody {...getTableBodyProps()}>
                {rows?.length > 0 && rows.map((row, index) => {
                  prepareRow(row);
                  return (
                    <JobApplicationTableRow
                      // Pass props to your JobApplicationTableRow component
                      company={row.original.company}
                      role={row.original.role}
                      location={row.original.location}
                      status={getStatusById(row.original.statusId)}
                      applied={row.original.appliedAt}
                      moreAction={() => moreAction(row.original.id)}
                      isLast={index === jobOffers.length - 1 ? true : false}
                      key={index}
                    />
                  );
                })}
                {rows?.length === 0 &&
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
            isOpen={isOpen}
            setFetchDataAgain={setFetchDataAgain}
            statuses={status} />
        </motion.div>
      </Grid>

    </Flex>
  );
}

export default Home;
