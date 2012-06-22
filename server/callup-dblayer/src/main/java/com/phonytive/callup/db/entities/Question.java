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
package com.phonytive.callup.db.entities;

import com.phonytive.callup.db.annotation.Embedded;
import com.phonytive.callup.db.annotation.NotNull;

/**
 *
 * @since 1.0.0
 */
@Embedded
public class Question {

    @NotNull
    private String audio;
    private String altTxt;
    @NotNull
    // Todo: Use the enum Key in astive-common
    private String dtmf;

    public String getAltTxt() {
        return altTxt;
    }

    public void setAltTxt(String altTxt) {
        this.altTxt = altTxt;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    public String getDtmf() {
        return dtmf;
    }

    public void setDtmf(String dtmf) {
        this.dtmf = dtmf;
    }
}