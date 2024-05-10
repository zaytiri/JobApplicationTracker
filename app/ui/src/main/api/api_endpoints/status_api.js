import React from 'react';

import axios from "axios";
import { BASE_URI } from "../api_configs";

const baseUrl = BASE_URI + "status/";
const headers = {
  headers: {
    'Content-Type': 'text/plain'
  }
}

export const create = async function(status) {
  return await axios.post(baseUrl + "create", status, headers).then((response) => {
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

export const update = async function(id, status) {
  return await axios.put(baseUrl + "update/" + id, status, headers).then((response) => {
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

export const getStatusByJobOfferId = async function(id) {
  return await axios.get(baseUrl + "status-by-job-offer/" + id, headers).then((response) => {
    return response.data;
  }).catch((error) =>{
    return error
  });
};

export const removeStatusFromJobOffer = async function(id) {
  return await axios.delete(baseUrl + "remove-from-job-offer/" + id, headers).then((response) => {
    return response.data;
  }).catch((error) =>{
    return error
  });
};

export const editStatusFromJobOffer = async function(id, status) {
  return await axios.patch(baseUrl + "update-from-job-offer/" + id, status, headers).then((response) => {
    return response.data;
  }).catch((error) =>{
    return error
  });
};