import React from 'react';

import axios from "axios";
import { BASE_URI } from "../api_configs";

const baseUrl = BASE_URI + "settings/";
const headers = {
  headers: {
    'Content-Type': 'text/plain'
  }
}

export const get = async function() {
  return await axios.post(baseUrl + "get", headers).then((response) => {
    return response.data[0];
  });
};

export const update = async function(settings) {
  return await axios.put(baseUrl + "update/1", settings, headers).then((response) => {
    return response.data;
  });
};
