import React from 'react';

import axios from "axios";
import { BASE_URI } from "../api_configs";

const baseUrl = BASE_URI + "job-offer/";
const headers = {
  headers: {
    'Content-Type': 'text/plain'
  }
}

export const create = async function(jobOffer) {
  return await axios.post(baseUrl + "create", jobOffer, headers).then((response) => {
    return response.data;
  });
};

export const get = async function(filters) {
  return await axios.post(baseUrl + "get", filters, headers).then((response) => {
    return response.data;
  });
};

export const getById = async function(id) {
  var filters = {
    "id": {"=": id}
  }
  return await axios.post(baseUrl + "get", filters, headers).then((response) => {
    return response.data;
  });
};

export const update = async function(id, jobOffer) {
  return await axios.put(baseUrl + "update/" + id, jobOffer, headers).then((response) => {
    return response.data;
  });
};

export const remove = async function(id) {
  return await axios.delete(baseUrl + "remove/" + id, headers).then((response) => {
    return response.data;
  });
};

export const scrape = async function(url) {
  return await axios.post(baseUrl + "scrape", url, headers).then((response) => {
    return response.data;
  });
};

export const getJobOffersByStatus = async function() {
  return await axios.get(baseUrl + "job-offer-by-status", headers).then((response) => {
    return response.data;
  });
};

export const find = async function(text) {
  return await axios.get(baseUrl + "find/" + text, headers).then((response) => {
    return response.data;
  });
};