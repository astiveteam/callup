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

import com.phonytive.callup.db.annotation.Id;
import com.phonytive.callup.db.utils.Util;
import java.lang.reflect.Field;
import java.util.List;
import org.bson.types.ObjectId;
import org.jongo.MongoCollection;

/**
 *
 * @since 1.0.0
 */
public class EntityManager {

    /**
     * Create a new EntityManager object using the default @{link DBConnection)
     */
    public EntityManager() {
    }

    public void persist(Object obj) {
        getCollection(obj.getClass()).save(obj);
    }

    public void merge(Object obj) {
        persist(obj);
    }

    public void remove(Object obj) {        
        Field[] fields = obj.getClass().getDeclaredFields();
        ObjectId id = null;
        for (Field f : fields) {
            f.setAccessible(true);
            if (f.getAnnotation(Id.class) != null) {
                try {
                    id = ((ObjectId) f.get(obj));
                } catch (IllegalArgumentException ex) {
                    // TODO:
                } catch (IllegalAccessException ex) {
                    // TODO:
                }
            }
        }
        getCollection(obj.getClass()).remove(id);
    }
    
    public Object findOne(Class clazz, ObjectId id) {        
        return getCollection(clazz).findOne(id).as(clazz);
    }
    
    public Object findOne(Class clazz, String query) {
        return getCollection(clazz).findOne(query).as(clazz);
    }
    
    public List find(Class clazz) {
        MongoCollection mc = DBConnection.getInstance().getCollection(clazz.getSimpleName());
        return Util.toList(mc.find("{}").as(clazz));
    }

    public List find(Class clazz, String query) {
        MongoCollection mc = DBConnection.getInstance().getCollection(clazz.getSimpleName());
        return Util.toList(mc.find(query).as(clazz));
    }    

    private MongoCollection getCollection(Class clazz) {
        return DBConnection.getInstance().getCollection(clazz.getSimpleName());
    }
}
