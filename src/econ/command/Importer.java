package econ.command;

import java.util.List;

import econ.TimeSeries;

public interface Importer {
  public TimeSeries run(List<Object> params);
}
