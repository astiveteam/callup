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
package com.phonytive.callup;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Helps to set the correct enviroment(dev, test or production) for the EntityManager
 *
 * @since 1.0.0
 */
public class PUSelector {

    private static String namePU;

    private PUSelector() {
    }    
    
    public static void selectPU(String namePU) {
        setNamePU(namePU);
    }

    public static EntityManagerFactory getEntityManager() {
        return Persistence.createEntityManagerFactory(getNamePU());
    }
    
    public static String getNamePU() {
        return namePU;
    }
    
    public static void setNamePU(String aNamePU) {
        namePU = aNamePU;
    }
}
