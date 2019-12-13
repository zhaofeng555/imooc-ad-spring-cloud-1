package com.imooc.ad.mysql;

import com.github.shyiko.mysql.binlog.BinaryLogClient;
import com.imooc.ad.mysql.listener.AggregationListener;
import com.netflix.discovery.converters.Auto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class BinlogClient {
  private BinaryLogClient client;
  private final BinlogConfig config;
  private final AggregationListener listener;

  public BinlogClient(BinlogConfig config, AggregationListener listener) {
    this.config = config;
    this.listener = listener;
  }

  // 是在一个新线程启动的，不会导致主线程hang住
  public void connect() {
    new Thread(() -> {
      client = new BinaryLogClient(
        config.getHost(),
        config.getPort(),
        config.getUsername(),
        config.getPassword()
      );
      if (!StringUtils.isEmpty(config.getBinlogName()) &&
        !config.getPosition().equals(-1L)) {
        client.setBinlogFilename(config.getBinlogName());
        client.setBinlogPosition(config.getPosition());
      }
      client.registerEventListener(listener);
      try {
        log.info("connecting to mysql start");
        client.connect();
        log.info("connecting to mysql done");
      } catch (IOException ex) {
        ex.printStackTrace();
      }
    }).start();
  }

  public void close() {
    try {
      client.disconnect();
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }
}