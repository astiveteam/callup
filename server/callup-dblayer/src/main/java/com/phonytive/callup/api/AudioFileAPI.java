package com.phonytive.callup.api;

import com.phonytive.callup.CallupException;
import com.phonytive.callup.entities.AudioFile;
import com.phonytive.callup.entities.Campaign;
import com.phonytive.callup.entities.User;
import java.io.File;
import java.net.URL;
import java.util.List;

public interface AudioFileAPI {

    AudioFile getAudioFile(AudioFile audioFile);
    
    List<AudioFile> listAudioFiles(User user);
    
    List<AudioFile> listAudioFiles(Campaign campaign);
    
    List<AudioFile> listAllAudioFiles();
    
    void removeAudioFile(AudioFile audioFile) throws CallupException;
    
    boolean campaignHasAudioFiles(Campaign campaign);
    
    void uploadAudioFile(User user, File file) throws CallupException;
    
    void uploadAudioFile(User user, URL file) throws CallupException;
}
