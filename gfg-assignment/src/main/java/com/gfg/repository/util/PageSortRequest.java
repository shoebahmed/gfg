package com.gfg.repository.util;

import java.io.Serializable;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import com.gfg.constant.Constant;

public class PageSortRequest implements Serializable {

  private static final long serialVersionUID = 1962783744653886223L;

  public static PageRequest of(int page, int size, String sortBy) {
    return PageRequest.of(page, size, Sort.by(getDirection(sortBy), getSortField(sortBy)));
  }

  private static String getSortField(String sortBy) {
    if (sortBy.toUpperCase().endsWith(Direction.DESC.toString())) {
      return sortBy.substring(0, sortBy.length() - 5);
    } else if (sortBy.toUpperCase().endsWith(Direction.ASC.toString())) {
      return sortBy.substring(0, sortBy.length() - 4);
    }
    return Constant.BLANK;
  }

  private static Direction getDirection(String sortBy) {
    if (sortBy.toUpperCase().endsWith(Direction.DESC.toString())) {
      return Direction.DESC;
    }
    return Direction.ASC;
  }
}
