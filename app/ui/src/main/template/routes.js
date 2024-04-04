// import
import React from 'react';
import Dashboard from "./views/Dashboard/Dashboard.js";
import Tables from "./views/Dashboard/Tables.js";
import Billing from "./views/Dashboard/Billing.js";
import Profile from "./views/Dashboard/Profile.js";
import { Home } from '../views/home/home.js';
import { DraggableView } from '../views/draggable_view/draggable_view.js';
import { Statistics } from '../views/statistics/statistics.js';

import {
  HomeIcon,
  StatsIcon,
  CreditIcon,
  PersonIcon,
} from "./components/Icons/Icons";

var dashRoutes = [
  {
    path: "/home",
    name: "Home",
    icon: <CreditIcon color='inherit' />,
    component: Home,
    layout: "/home",
  },
  {
    path: "/draggableview",
    name: "Draggable View",
    icon: <CreditIcon color='inherit' />,
    component: DraggableView,
    layout: "/home",
  },
  {
    path: "/statistics",
    name: "Statistics",
    icon: <CreditIcon color='inherit' />,
    component: Statistics,
    layout: "/home",
  },
  {
    path: "/settings",
    name: "Settings",
    icon: <CreditIcon color='inherit' />,
    component: DraggableView,
    layout: "/home",
  },
  // {
  //   name: "TEMPORARY TEMPLATES",
  //   category: "account",
  //   state: "pageCollapse",
  //   views: [
  //     {
  //       path: "/dashboard",
  //       name: "Dashboard",
  //       icon: <HomeIcon color='inherit' />,
  //       secondaryNavbar: true,
  //       component: Dashboard,
  //       layout: "/home",
  //     },
  //     {
  //       path: "/tables",
  //       name: "Tables",
  //       icon: <StatsIcon color='inherit' />,
  //       secondaryNavbar: true,
  //       component: Tables,
  //       layout: "/home",
  //     },
  //     {
  //       path: "/billing",
  //       name: "Billing",
  //       icon: <CreditIcon color='inherit' />,
  //       secondaryNavbar: true,
  //       component: Billing,
  //       layout: "/home",
  //     },
  //     {
  //       path: "/profile",
  //       name: "Profile",
  //       icon: <PersonIcon color='inherit' />,
  //       secondaryNavbar: true,
  //       component: Profile,
  //       layout: "/home",
  //     },
  //   ],
  // },
];
export default dashRoutes;
