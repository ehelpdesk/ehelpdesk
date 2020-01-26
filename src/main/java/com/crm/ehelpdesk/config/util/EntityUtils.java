package com.crm.ehelpdesk.config.util;

import javax.persistence.Column;

public class EntityUtils {

  public static String getNativeColumnName(String columnName, Class entityClass) {
    try {
      return entityClass.getDeclaredField(columnName).getAnnotation(Column.class).name();
    } catch (NoSuchFieldException e) {
      e.printStackTrace();
    }
    return null;
  }
}
