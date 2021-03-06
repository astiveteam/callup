/* 
 * Copyright (C) 2012 PhonyTive LLC
 * http://callup.phonytive.com
 *
 * This file is part of Callup
 *
 * Callup is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Callup is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Callup.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.phonytive.callup.server;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import com.phonytive.callup.util.AppLocale;

/**
 * Final implementation of {@link ServiceProperties} use by {@link Service}.
 *
 * @see ServiceProperties, Service
 * @since 1.0.0
 */
public final class ServicePropertiesImpl implements ServiceProperties {
  private InetAddress bindAddr;
  private List<InetAddress> onlyFrom;
  private Properties properties;
  private String server;
  private String serviceName;
  private boolean disabled;
  private boolean unableToOpen;
  private int backlog;
  private int port;

  /**
   * Creates a new ServicePropertiesImpl object.
   */
  public ServicePropertiesImpl() {
  }

  /**
   * Creates a new ServicePropertiesImpl object with the parameters indicated
   * in properties and the name of service.
   *
   * @param properties represent the properties file for a particular service.
   * @param serviceName the name to be displayed by the server.
   *
   * @throws SystemException if the service cannot be binded to the bind address.
   */
  public ServicePropertiesImpl(Properties properties, String serviceName)
                        throws SystemException {
    this.properties = properties;

    setDisabled(Boolean.parseBoolean(properties.get("disabled").toString().trim()));
    setPort(Integer.valueOf(properties.get("port").toString().trim()).intValue());
    setBacklog(Integer.valueOf(properties.get("threads").toString().trim()).intValue());

    try {
      setBindAddr(InetAddress.getByName(properties.get("bind").toString().trim()));
    } catch (UnknownHostException ex) {
      throw new SystemException(AppLocale.getI18n("unknownHost",
                                                  new Object[] {
                                                    properties.get("bind").toString().trim()
                                                  }));
    }

    try {
      List<InetAddress> onlyFromList = new ArrayList<InetAddress>();
      String[] l = properties.get("onlyFrom").toString().split(",");

      for (int i = 0x0; l.length > i; i++) {
        InetAddress id = InetAddress.getByName(l[i].trim());
        onlyFromList.add(id);
      }

      setOnlyFrom(onlyFromList);
    } catch (UnknownHostException ex) {
      throw new SystemException(AppLocale.getI18n("unknownHost",
                                                  new Object[] {
                                                    properties.get("onlyFrom").toString().trim()
                                                  }));
    } catch (NullPointerException ex) {
    }

    setServer((String) properties.get("server"));
    setServiceName(serviceName);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getBacklog() {
    return backlog;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public InetAddress getBindAddr() {
    return bindAddr;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<InetAddress> getOnlyFrom() {
    return Collections.unmodifiableList(onlyFrom);
  }

  /**
   * Use to return arbitrary parameters.
   *
   * @return a parameter present in the properties file, or null if none.
   */
  public String getPameter(String parameter) {
    try {
      return properties.get(parameter).toString().trim();
    } catch (NullPointerException ex) {
      return null;
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getPort() {
    return port;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getServer() {
    return server;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getServiceName() {
    return serviceName;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isDisabled() {
    return disabled;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isUnableToOpen() {
    return unableToOpen;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setBacklog(int backlog) {
    this.backlog = backlog;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setBindAddr(InetAddress bindAddr) {
    this.bindAddr = bindAddr;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setDisabled(boolean disabled) {
    this.disabled = disabled;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setOnlyFrom(List<InetAddress> onlyFrom) {
    this.onlyFrom = onlyFrom;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setPort(int port) {
    this.port = port;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setServer(String server) {
    this.server = server;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setServiceName(String serviceName) {
    this.serviceName = serviceName;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setUnableToOpen(boolean unableToOpen) {
    this.unableToOpen = unableToOpen;
  }
}
