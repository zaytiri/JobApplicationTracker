/*eslint-disable*/
import { Flex, Link, List, ListItem, Text } from "@chakra-ui/react";
import React from "react";

export default function Footer(props) {
  return (
    <Flex
      flexDirection={{
        base: "column",
        xl: "row",
      }}
      alignItems={{
        base: "center",
        xl: "start",
      }}
      justifyContent='space-between'
      px='30px'
      pb='20px'>
      <Text
        color='gray.400'
        textAlign={{
          base: "center",
          xl: "start",
        }}
        mb={{ base: "20px", xl: "0px" }}>
        &copy; {1900 + new Date().getYear()},{" "}
        <Text as='span'><Text as='span' fontWeight="bold">Job Application Tracker</Text> made by zaytiri.</Text>
        <br/>
        <Text as='span'>
        Base template made by
        </Text>
        <Link
          color='blue.400'
          href='https://www.creative-tim.com'
          target='_blank'>
          {" Creative Tim "}
        </Link>
         & 
        <Link color='blue.400' href='https://www.simmmple.com' target='_blank'>
        {" Simmmple."}
        </Link>
      </Text>
      <List display='flex'>
        <ListItem>
          <Link
            color='gray.400'
            href='https://github.com/zaytiri/JobApplicationTracker'>
            Github
          </Link>
        </ListItem>
      </List>
    </Flex>
  );
}
