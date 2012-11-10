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
import com.phonytive.callup.controllers.AudioFileJpaController;
import com.phonytive.callup.controllers.CampaignJpaController;
import com.phonytive.callup.controllers.UserJpaController;
import com.phonytive.callup.entities.AudioFile;
import com.phonytive.callup.entities.Campaign;
import com.phonytive.callup.entities.User;
import java.io.File;
import java.net.URL;
import java.util.Collection;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class AudioFilesAPI {

    private EntityManagerFactory emf = null;
    AudioFileJpaController ajc;
    UserJpaController ujc;
    CampaignJpaController cjc;
            
    public AudioFilesAPI() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("CallupPU");
        ajc = new AudioFileJpaController(emf);
        cjc = new CampaignJpaController(emf);
        ujc = new UserJpaController(emf);
    }
    
    public AudioFile getAudioFile(Integer audioFileId) {
        return ajc.findAudioFile(audioFileId);
    }
    
    public Collection<AudioFile> listUserAudioFiles(Integer userId) {
        User user = ujc.findUser(userId);
        return user.getAudioFileCollection();
    }
    
    public Collection<AudioFile> listCampaignAudioFiles(Integer campaignId) {
        Campaign campaign = cjc.findCampaign(campaignId);
        return campaign.getAudioFileCollection();
    }
    
    public Collection<AudioFile> listAllAudioFiles() {    
        return ajc.findAudioFileEntities();
    }
    
    public void removeAudioFile(Integer audioFileId) throws CallupException{
        try {
            ajc.destroy(audioFileId);
        } catch (Exception ex) {
            throw new CallupException(ex);
        }
    }
    
    boolean campaignHasAudioFile(Integer campaignId, Integer audioFileId) {
        Campaign campaign = cjc.findCampaign(campaignId);
        AudioFile audioFile = ajc.findAudioFile(campaignId);
        
        Collection <AudioFile> audioFiles = campaign.getAudioFileCollection();
        for(AudioFile current :audioFiles ) {
            if(current.getAudioFileId().equals(audioFile.getAudioFileId())) {
                return true;
            }
        }
        return false;
    }

    boolean campaignHasAudioFiles(Integer campaignId) {
        Campaign campaign = cjc.findCampaign(campaignId);
        if(campaign.getAudioFileCollection().size() > 0) {
            return true;
        }
        return false;
    }    
    
    void uploadAudioFile(User user, File file) throws CallupException {
        //WARNING: Not yet implemented
    }
    
    void uploadAudioFile(User user, URL file) throws CallupException {
        //WARNING: Not yet implemented
    }
}
