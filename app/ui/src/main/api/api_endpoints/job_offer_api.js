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
  }).catch((error) =>{
    return error
  });
};

export const get = async function(filters) {
  return await axios.post(baseUrl + "get", filters, headers).then((response) => {
    return response.data;
  }).catch((error) =>{
    return error
  });
};

export const getById = async function(id) {
  var filters = {
    "id": {"=": id}
  }
  return await axios.post(baseUrl + "get", filters, headers).then((response) => {
    return response.data;
  }).catch((error) =>{
    return error
  });
};

export const update = async function(id, jobOffer) {
  return await axios.put(baseUrl + "update/" + id, jobOffer, headers).then((response) => {
    return response.data;
  }).catch((error) =>{
    return error
  });
};

export const remove = async function(id) {
  return await axios.delete(baseUrl + "remove/" + id, headers).then((response) => {
    return response.data;
  }).catch((error) =>{
    return error
  });
};

export const scrape = async function(url) {
  return await axios.post(baseUrl + "scrape", url, headers).then((response) => {
    return response.data;
  }).catch((error) =>{
    return error
  });
};

export const updateJobStatus = async function(id) {
  return await axios.patch(baseUrl + "update-job-status", id, headers).then((response) => {
    return response.data;
  }).catch((error) =>{
    return error
  });
};

export const updateAllJobsStatus = async function(ids) {
  let results = [];
  for (let i = 0; i < ids.length; i++) {
  results.push(await axios.patch(baseUrl + "update-job-status", ids[i], headers).then((response) => {
      return response.data;
    }).catch((error) =>{
      return error
    }))
  }
  return results
};

export const getJobOffersByStatus = async function() {
  return await axios.get(baseUrl + "job-offer-by-status", headers).then((response) => {
    return response.data;
  }).catch((error) =>{
    return error
  });
};

export const find = async function(text) {
  return await axios.get(baseUrl + "find/" + text, headers).then((response) => {
    return response.data;
  }).catch((error) =>{
    return error
  });
};

export const getStatistics = async function() {
  return await axios.get(baseUrl + "statistics", headers).then((response) => {
    return response.data;
  }).catch((error) =>{
    return error
  });
};