if (!:Defined('assert')) {
  assert = ES:Assert;
}

function create(name, type) {
  if (type == null) {
    return :Create(name);
  } else {
    return :Create(name, type);
  }
}

function insert(series, date, value) {
  :Insert(series, date, value);
}

function getSize(series) {
  return :GetSize(series);
}

function getOffset(series) {
  return :GetOffset(series);
}

function get(series, index) {
  return :Get(series, index);
}

function average(series, n) {
  return :Average(series, n);
}

function sum(series, n) {
  return :Sum(series, n);
}

function date(series) {
  return :Date(series);
}

function stdev(series, n) {
  return :Stdev(series, n);
}

function defined(symbol) {
  return :Defined(symbol);
}

function gGet(symbol) {
  return :GGet(symbol);
}

function gPut(symbol, value) {
  :GPut(symbol, value);
}

function collapse(series1, series2) {
  if (series2 == null) {
    return :Collapse(series1);
  } else {
    return :Collapse(series1, series2);
  }
}

function normalize(seriesCollapsed, series) {
  return :Normalize(seriesCollapsed, series);
}

function setId(series, id) {
  :SetId(series, id);
}

function setTitle(series, title) {
  :SetTitle(series, title);
}

