package econ.command;

import java.util.List;

import econ.TimeSeries;

public class QTemplateImporter implements Importer {
  @Override
  public TimeSeries run(List<Object> params) {
    return new TimeSeries();
  }
}
