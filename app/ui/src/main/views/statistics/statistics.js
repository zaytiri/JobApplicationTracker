// Chakra imports
import {
    Box,
    Button,
    Flex,
    Grid,
    Progress,
    SimpleGrid,
    Stat,
    StatLabel,
    StatNumber,
    Table,
    Tbody,
    Td,
    Text,
    Th,
    Thead,
    Tr,
    useColorMode,
    useColorModeValue,
} from "@chakra-ui/react";
// Custom components
import Card from "../../template/components/Card/Card.js";
import BarChart from "../../template/components/Charts/BarChart";
import LineChart from "../../template/components/Charts/LineChart";
import IconBox from "../../template/components/Icons/IconBox";
// Custom icons
import {
    CartIcon,
    DocumentIcon,
    GlobeIcon,
    WalletIcon,
} from "../../template/components/Icons/Icons.js";
import React, { useState, useEffect } from "react";
// Variables
import {
    barChartData,
    barChartOptions,
    lineChartData,
    lineChartOptions,
} from "../../template/variables/charts";

import { getStatistics } from "../../api/api_endpoints/job_offer_api.js";

export const Statistics = () => {
    // Chakra Color Mode
    const iconBlue = useColorModeValue("blue.500", "blue.500");
    const iconBoxInside = useColorModeValue("white", "white");
    const textColor = useColorModeValue("gray.700", "white");

    const { colorMode } = useColorMode();

    const monthNames = [
        'January', 'February', 'March', 'April', 'May', 'June', 'July',
        'August', 'September', 'October', 'November', 'December'
    ];

    const [statistics, setStatistics] = useState({});
    const [fetchDataAgain, setFetchDataAgain] = useState(true);
    useEffect(() => {
        const fetchData = async () => {
            try {
                const response = await getStatistics();
                setStatistics(response);
            } catch (error) {
                console.error("Error fetching job offers:", error);
            }
        };

        if (fetchDataAgain) {
            fetchData();
            setFetchDataAgain(false);
        }
    }, [fetchDataAgain]);

    const calculateDifferenceBetweenLastAndCurrentMonth = (stat) => {
        if (statistics[stat] === undefined) {
            return null;
        }

        let result = statistics[stat][new Date().getMonth()].numberOfJobs
            - statistics[stat][new Date().getMonth() - 1].numberOfJobs

        if (result > 0) {
            result = '+' + result;
        } else if (result < 0) {
            result = '-' + result;
        }

        return result
    }

    const doJobsExist = (statistics['TotalAppliedJobsByDay'] !== undefined || statistics['TotalJobsByStatus'] !== undefined)

    return (
        <Flex flexDirection='column' pt={{ base: "120px", md: "75px" }}>
            <SimpleGrid columns={{ sm: 1, md: 2, xl: 3 }} spacing='24px' mb='20px'>
                <Card minH='125px'>
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
                                    Total Job Applications
                                </StatLabel>
                                <Flex>
                                    <StatNumber fontSize='lg' color={textColor} fontWeight='bold'>
                                        {statistics['TotalJobs'] !== undefined ? statistics['TotalJobs'][0].numberOfJobs : 0}
                                    </StatNumber>
                                </Flex>
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
                        {/* <Text color='gray.400' fontSize='sm'>
                            <Text as='span' color='green.400' fontWeight='bold'>
                                +3.48%{" "}
                            </Text>
                            Since last month
                        </Text> */}
                    </Flex>
                </Card>
                <Card minH='125px'>
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
                                    Total Applied Job Applications
                                </StatLabel>
                                <Flex>
                                    <StatNumber fontSize='lg' color={textColor} fontWeight='bold'>
                                        {statistics['TotalAppliedJobs'] !== undefined ? statistics['TotalAppliedJobs'][0].numberOfAppliedJobs : 0}
                                    </StatNumber>
                                </Flex>
                            </Stat>
                            <IconBox
                                borderRadius='50%'
                                as='box'
                                h={"45px"}
                                w={"45px"}
                                bg={iconBlue}>
                                <GlobeIcon h={"24px"} w={"24px"} color={iconBoxInside} />
                            </IconBox>
                        </Flex>
                        {/* <Text color='gray.400' fontSize='sm'>
                            <Text as='span' color='green.400' fontWeight='bold'>
                                +5.2%{" "}
                            </Text>
                            Since last month
                        </Text> */}
                    </Flex>
                </Card>
                <Card minH='125px'>
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
                                    Total Applied Jobs this Month ({monthNames[new Date().getMonth()]})
                                </StatLabel>
                                <Flex>
                                    <StatNumber fontSize='lg' color={textColor} fontWeight='bold'>
                                        {statistics['TotalAppliedJobsByMonth'] !== undefined ? statistics['TotalAppliedJobsByMonth'][new Date().getMonth()].numberOfJobs : 0}
                                    </StatNumber>
                                </Flex>
                            </Stat>
                            <IconBox
                                borderRadius='50%'
                                as='box'
                                h={"45px"}
                                w={"45px"}
                                bg={iconBlue}>
                                <CartIcon h={"24px"} w={"24px"} color={iconBoxInside} />
                            </IconBox>
                        </Flex>
                        <Text color='gray.400' fontSize='sm'>
                            <Text as='span' color={
                                calculateDifferenceBetweenLastAndCurrentMonth('TotalAppliedJobsByMonth') < 0
                                    ? 'red.500'
                                    : calculateDifferenceBetweenLastAndCurrentMonth('TotalAppliedJobsByMonth') > 0
                                        ? 'green.400'
                                        : 'gray.400'} fontWeight='bold'>
                                {calculateDifferenceBetweenLastAndCurrentMonth('TotalAppliedJobsByMonth')}{" "}
                            </Text>
                            Since last month ({monthNames[new Date().getMonth() - 1]})
                        </Text>
                    </Flex>
                </Card>
            </SimpleGrid>
            <Grid
                templateColumns={{ sm: "1fr", lg: `${doJobsExist ? "2fr 1fr" : "1"}` }}
                templateRows={{ lg: "repeat(1, auto)" }}
                gap='20px'>
                {statistics['TotalAppliedJobsByDay'] !== undefined && <Card
                    bg={
                        colorMode === "dark"
                            ? "navy.800"
                            : "linear-gradient(81.62deg, #313860 2.25%, #151928 79.87%)"
                    }
                    p='0px'
                    maxW={{ sm: "320px", md: "100%" }}>
                    <Flex direction='column' mb='40px' p='28px 0px 0px 22px'>
                        <Text color='#fff' fontSize='lg' fontWeight='bold' mb='6px'>
                            Total Job Applications by Day
                        </Text>
                        {/* <Text color='#fff' fontSize='sm'>
                            <Text as='span' color='green.400' fontWeight='bold'>
                                (+5) more{" "}
                            </Text>
                            in 2022
                        </Text> */}
                    </Flex>
                    <Box minH='300px'>
                        <LineChart
                            chartData={lineChartData(statistics['TotalAppliedJobsByDay'])}
                            chartOptions={lineChartOptions(statistics['TotalAppliedJobsByDay'])}
                        />
                    </Box>
                </Card>
                }
                {statistics['TotalJobsByStatus'] !== undefined && <Card p='0px' maxW={{ sm: "320px", md: "100%" }}>
                    <Flex direction='column' mb='40px' p='28px 0px 0px 22px'>
                        <Text color='gray.400' fontSize='sm' fontWeight='bold' mb='6px'>
                            Total Job Applications
                        </Text>
                        <Text color={textColor} fontSize='lg' fontWeight='bold'>
                            by Status
                        </Text>
                    </Flex>
                    <Box minH='300px'>
                        <BarChart chartData={barChartData(statistics['TotalJobsByStatus'])} chartOptions={barChartOptions(statistics['TotalJobsByStatus'])} />
                    </Box>
                </Card>
                }
                {(!doJobsExist) && <Card p='0px' maxW={{ sm: "320px", md: "100%" }}>
                <Text fontSize='md' color='gray.400' fontWeight='400' p="15px">
                      No Job Applications added yet.
                    </Text>
                </Card>}
            </Grid>
        </Flex>
    );
}
