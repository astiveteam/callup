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
package com.phonytive.callup.util;

import java.text.Format;
import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Help to localize messages.
 *
 * @since 1.0.0
 */
public class AppLocale {
  // Define the bundle prefix.     
  private static final ResourceBundle messages = ResourceBundle.getBundle("Messages");

  // Define the message format.     
  private static MessageFormat messageForm = new MessageFormat("");

  // Formats to be use in messages.    
  private static Format[] formats = { null };

  /**
   * Constructor class. This class only has a static methods, therefore can't
   * be instantiated.
   */
  private AppLocale() {
  }

  /**
   * Get a localized message.
   *
   * @param key used to access a particular message.
   * @return localized message.
   */
  public static String getI18n(String key) {
    try {
      return messages.getString(key);
    } catch (MissingResourceException ex) {
      return key;
    }
  }

  /**
   * Get a localized message with arguments. The message must be defined like
   * so:
   *
   * <p>myMessage=This is a message with arguments <code>{0}</code>
   *
   * <p>Then
   * <code>{0}</code> will be substitute by
   * <code>args[0]</code>.
   *
   * @param key message key.
   * @param args arguments to be passed to the message.
   * @return localized message with arguments.
   */
  public static String getI18n(String key, Object[] args) {
    try {
      String msgPatter = messages.getString(key);
      messageForm.setFormats(formats);
      messageForm.applyPattern(msgPatter);

      String result = messageForm.format(args);

      return result;
    } catch (MissingResourceException ex) {
      return key;
    }
  }
}
