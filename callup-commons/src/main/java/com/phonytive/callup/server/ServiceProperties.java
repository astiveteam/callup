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
import java.util.List;

/**
 * Contains all parameters needed by a final implementation of
 * {@link Service}.
 *
 * @see Service
 * @since 1.0.0
 */
public interface ServiceProperties {
  /**
   * Return the maximum queue length for incoming connection indications.
   *
   * @return maximum number of pending connections on the socket.
   */
  int getBacklog();

  /**
   * Return the address to where the <code>service</code> is bound, if any.
   *
   * @return the local address the <code>service</code> is bound to, or <code>null</code> for the
   * <code>anyLocal</code> address.
   */
  InetAddress getBindAddr();

  /**
   * DOCUMENT ME!
   *
   * @return DOCUMENT ME!
   */
  /**
   * DOCUMENT ME!
   *
   * @return DOCUMENT ME!
   */

  /**
   * DOCUMENT ME!
   *
   * @return DOCUMENT ME!
   */

  /**
   * DOCUMENT ME!
   *
   * @return DOCUMENT ME!
   */

  /**
   * DOCUMENT ME!
   *
   * @return DOCUMENT ME!
   */

  /**
   * DOCUMENT ME!
   *
   * @return DOCUMENT ME!
   */

  /**
   * Return the address list from where connections are accept.
   *
   * @return by default <code>service</code> accept connections only from the <code>local</code>
   * machine.
   */
  List<InetAddress> getOnlyFrom();

  /**
   * Return the <code>service</code> port.
   *
   * @return port to where <code>service</code> is bound.
   */
  int getPort();

  /**
   * Return fully qualified class name for the final implementation of
   * {@link Service}.
   *
   * @return the final implementation of a particular <code>service</code>.
   */
  String getServer();

  /**
   * Return the name of <code>service</code>.
   *
   * @return a descriptive name for a <code>service</code> (ex.: Telnet, Admin)...
   */
  String getServiceName();

  /**
   * Indicates whether or not the <code>service</code> is disabled.
   *
   * @return true if <code>service</code> is disabled or true otherwise.
   */
  boolean isDisabled();

  /**
   * Indicates whether or not the <code>service</code> was bound to a IP/Port.
   *
   * @return true if <code>service</code> can't be bound to a port(ex.: port occupied by another
   * process).
   */
  boolean isUnableToOpen();

  /**
   * Define the maximum queue length for incoming connection indications.
   *
   * @param backlog provided should be greater than 0.
   * @see ServerSocket
   */
  void setBacklog(int backlog);

  /**
   * Address to where the <code>service</code> should be bound.
   *
   * @param bindAddr should be a valid address where the code>service</code> is running.
   */
  void setBindAddr(InetAddress bindAddr);

  /**
   * Set this parameter to <code>true</code> to indicate
   *
   * @param disabled useful for services no mandatory(ex.:Telnet)
   */
  void setDisabled(boolean disabled);

  /**
   * Used to indicate the list of address from where the service can be accessed.
   *
   * @param onlyFrom by default a service can only be accessed from the
   * local machine.
   */
  void setOnlyFrom(List<InetAddress> onlyFrom);

  /**
   * A valid port to where the service is bound.
   *
   * @param port is valid if is in the range in between 0 and 65535, inclusive.
   */
  void setPort(int port);

  /**
   * Use to define the final implementation of the service.
   *
   * @param server has to be a fully qualified name class.
   */
  void setServer(String server);

  /**
   * Define the name for a particular service, for display purposes.
   *
   * @param serviceName useful to describe the function of the service.
   */
  void setServiceName(String serviceName);

  /**
   * Set to true if can't bound <code>service</code> to a particular IP/Port.
   *
   * @param unableToOpen is set to false if bound process ends normally.
   */
  void setUnableToOpen(boolean unableToOpen);
}
