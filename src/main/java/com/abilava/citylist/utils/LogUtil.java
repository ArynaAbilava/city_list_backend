package com.abilava.citylist.utils;

import java.util.List;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UtilityClass
public class LogUtil {

  public void log(String methodName, List<Object> params) {
    log.debug(String.format("Run - %s - method with params: %s", methodName, params));
  }
}
