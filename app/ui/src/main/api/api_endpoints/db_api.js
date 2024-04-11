import React from 'react';

import axios from "axios";
import { BASE_URI } from "../api_configs";

const baseUrl = BASE_URI + "db/";
const headers = {
  headers: {
    'Content-Type': 'text/plain'
  }
}

export const checkMigration = async function() {
  return await axios.get(baseUrl + "check-migration", headers).then((response) => {
    return response.data;
  });
};