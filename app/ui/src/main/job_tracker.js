import React, { useEffect } from "react";
import { HashRouter, Route, Routes, Navigate } from "react-router-dom";

import AdminLayout from "./template/layouts/Admin.js";
import { ChakraProvider } from "@chakra-ui/react";
import { checkMigration } from "./api/api_endpoints/db_api.js"

import theme from "./template/theme/theme.js";

export const JobTracker = () => {

  useEffect(() => { 
    checkMigration()
  }, []);

  return (
    <ChakraProvider theme={theme} resetCss={false} position="relative">
    <HashRouter>
      <Routes>
        <Route path={`/home/*`} element={<AdminLayout/>} ></Route>
        <Route exact path={`/`} element={<Navigate to="/home/home" />} ></Route>
      </Routes>
    </HashRouter>
  </ChakraProvider>
  );
};