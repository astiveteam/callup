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
package com.phonytive.callup.api;

import com.phonytive.callup.CallupException;
import com.phonytive.callup.entities.AudioFile;
import com.phonytive.callup.entities.Campaign;
import com.phonytive.callup.entities.User;
import java.io.File;
import java.net.URL;
import java.util.List;

public interface AudioFilesAPI {

    AudioFile getAudioFile(AudioFile audioFile);
    
    List<AudioFile> listAudioFiles(User user);
    
    List<AudioFile> listAudioFiles(Campaign campaign);
    
    List<AudioFile> listAllAudioFiles();
    
    void removeAudioFile(AudioFile audioFile) throws CallupException;
    
    boolean campaignHasAudioFiles(Campaign campaign);
    
    void uploadAudioFile(User user, File file) throws CallupException;
    
    void uploadAudioFile(User user, URL file) throws CallupException;
}
