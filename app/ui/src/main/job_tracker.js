import React from "react";
import { HashRouter, Route, Switch, Redirect } from "react-router-dom";

import AdminLayout from "./template/layouts/Admin.js";
import { ChakraProvider } from "@chakra-ui/react";

import theme from "./template/theme/theme.js";

export const JobTracker = () => {
  return (
    <ChakraProvider theme={theme} resetCss={false} position="relative">
    <HashRouter>
      <Switch>
        <Route path={`/home`} component={AdminLayout} />
        <Redirect from={`/`} to="/home/home" />
      </Switch>
    </HashRouter>
  </ChakraProvider>
  );
};