package com.gfg.model.converter;

import org.bson.types.ObjectId;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

public class CustomObjectIdToStringConverter implements Converter<ObjectId, String> {
  
  /**
   * This convert is used to convert Binary notation ObjectId into human readable value.
   */
  @Override
  public String convert(MappingContext<ObjectId, String> context) {
    return context.getSource() != null ? (context.getSource()).toString() : null;
  }
}
