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
package com.phonytive.callup.db;

import com.mongodb.Mongo;
import com.mongodb.MongoException;
import java.net.UnknownHostException;
import org.jongo.Jongo;
import org.jongo.MongoCollection;

/**
 * Singleton that provide a single connection to Callup DB.
 * 
 * @since 1.0.0 
 */
public class DBConnection {

    private static final DBConnection INSTANCE = new DBConnection();
    private Mongo mongo;
    private Jongo jongo;
    
    /**
     * Creates a new DBConnection object.
     */    
    private DBConnection()  {
        try {
            mongo = new Mongo("127.0.0.1" , 27017);
            jongo = new Jongo(mongo.getDB("callup"));
        } catch (UnknownHostException ex) {            
            // TODO: Use log4j
        } catch (MongoException ex) {
            // TODO: Use log4j
        }
    }
    
    public MongoCollection getCollection(String name) {
        return jongo.getCollection(name);
    }

    public static DBConnection getInstance() {
        return INSTANCE;
    }    
}
