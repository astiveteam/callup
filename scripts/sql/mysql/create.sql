SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

CREATE SCHEMA IF NOT EXISTS `callup` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci ;
USE `callup` ;

-- -----------------------------------------------------
-- Table `callup`.`User`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `callup`.`User` (
  `userId` INT NOT NULL ,
  `name` VARCHAR(45) NULL ,
  `username` VARCHAR(45) NULL ,
  `password` VARCHAR(45) NULL ,
  `created` DATETIME NULL ,
  `updated` DATETIME NULL ,
  `email` VARCHAR(45) NULL ,
  `type` SET('ADMIN', 'NORMAL') NULL ,
  PRIMARY KEY (`userId`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `callup`.`Catalog`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `callup`.`Catalog` (
  `listId` INT NOT NULL ,
  `name` VARCHAR(45) NULL ,
  `created` DATETIME NULL ,
  `updated` DATETIME NULL ,
  INDEX `fk_List_User1_idx` (`listId` ASC) ,
  PRIMARY KEY (`listId`) ,
  CONSTRAINT `fk_List_User1`
    FOREIGN KEY (`listId` )
    REFERENCES `callup`.`User` (`userId` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `callup`.`Campaign`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `callup`.`Campaign` (
  `campaignId` INT NOT NULL ,
  `user` INT NOT NULL ,
  `catalog` INT NOT NULL ,
  `callerId` VARCHAR(45) NULL ,
  `name` VARCHAR(45) NULL ,
  `maxRetriesPerDay` INT NULL ,
  `maxRetriesPerCampaign` INT NULL ,
  `fromDate` DATE NULL ,
  `toDate` DATE NULL ,
  `fromTime` DATETIME NULL ,
  `toTime` DATETIME NULL ,
  `status` SET('ACTIVE', 'INACTIVE','DELETED') NULL ,
  `created` DATETIME NULL ,
  `updated` DATETIME NULL ,
  `includeDays` VARCHAR(45) NULL ,
  `type` SET('COMPOSITE_VOICE', 'SURVEY') NULL ,
  `dataXML` LONGTEXT NULL ,
  PRIMARY KEY (`campaignId`) ,
  INDEX `fk_Campaign_User1_idx` (`user` ASC) ,
  INDEX `fk_Campaign_List1_idx` (`catalog` ASC) ,
  CONSTRAINT `fk_Campaign_User1`
    FOREIGN KEY (`user` )
    REFERENCES `callup`.`User` (`userId` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Campaign_List1`
    FOREIGN KEY (`catalog` )
    REFERENCES `callup`.`Catalog` (`listId` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `callup`.`Subscriber`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `callup`.`Subscriber` (
  `subscriberId` INT NOT NULL ,
  `externalId` VARCHAR(45) NOT NULL COMMENT 'Use this field to link\\nsubscriber with\\nthird party apps.' ,
  `number` VARCHAR(45) NULL ,
  `addedOn` DATETIME NULL ,
  `firstName` VARCHAR(45) NULL ,
  `midName` VARCHAR(45) NULL ,
  `lastName` VARCHAR(45) NULL ,
  `description` VARCHAR(45) NULL ,
  PRIMARY KEY (`subscriberId`) ,
  UNIQUE INDEX `externalId_UNIQUE` (`externalId` ASC) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `callup`.`SubscriberCatalog`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `callup`.`SubscriberCatalog` (
  `subscriber` INT NOT NULL ,
  `list` INT NOT NULL ,
  PRIMARY KEY (`subscriber`, `list`) ,
  INDEX `fk_Subscriber_has_List_List1_idx` (`list` ASC) ,
  INDEX `fk_Subscriber_has_List_Subscriber1_idx` (`subscriber` ASC) ,
  CONSTRAINT `fk_Subscriber_has_List_Subscriber1`
    FOREIGN KEY (`subscriber` )
    REFERENCES `callup`.`Subscriber` (`subscriberId` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Subscriber_has_List_List1`
    FOREIGN KEY (`list` )
    REFERENCES `callup`.`Catalog` (`listId` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `callup`.`AudioFile`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `callup`.`AudioFile` (
  `audioFileId` INT NOT NULL ,
  `name` VARCHAR(45) NULL ,
  `time` INT NULL ,
  `supportedFormat` SET('MP3','WAV','GSM') NULL ,
  `description` VARCHAR(45) NULL ,
  `created` DATETIME NULL ,
  `gender` SET('MALE', 'FEMALE') NULL ,
  `hide` TINYINT(1) NULL ,
  PRIMARY KEY (`audioFileId`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `callup`.`ActivityType`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `callup`.`ActivityType` (
  `activityTypeId` INT NOT NULL ,
  `name` VARCHAR(45) NULL ,
  `description` VARCHAR(45) NULL ,
  PRIMARY KEY (`activityTypeId`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `callup`.`Activity`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `callup`.`Activity` (
  `activityId` INT NOT NULL ,
  `description` VARCHAR(45) NULL ,
  `User_userId` INT NOT NULL ,
  `ActivityType` INT NOT NULL ,
  PRIMARY KEY (`activityId`, `User_userId`) ,
  INDEX `fk_History_User1_idx` (`User_userId` ASC) ,
  INDEX `fk_History_HistoryType1_idx` (`ActivityType` ASC) ,
  CONSTRAINT `fk_History_User1`
    FOREIGN KEY (`User_userId` )
    REFERENCES `callup`.`User` (`userId` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_History_HistoryType1`
    FOREIGN KEY (`ActivityType` )
    REFERENCES `callup`.`ActivityType` (`activityTypeId` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `callup`.`CampaignAudioFiles`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `callup`.`CampaignAudioFiles` (
  `Campaign` INT NOT NULL ,
  `AudioFile` INT NOT NULL ,
  PRIMARY KEY (`Campaign`, `AudioFile`) ,
  INDEX `fk_Campaign_has_AudioFile_AudioFile1_idx` (`AudioFile` ASC) ,
  INDEX `fk_Campaign_has_AudioFile_Campaign1_idx` (`Campaign` ASC) ,
  CONSTRAINT `fk_Campaign_has_AudioFile_Campaign1`
    FOREIGN KEY (`Campaign` )
    REFERENCES `callup`.`Campaign` (`campaignId` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Campaign_has_AudioFile_AudioFile1`
    FOREIGN KEY (`AudioFile` )
    REFERENCES `callup`.`AudioFile` (`audioFileId` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `callup`.`CallDetailRecord`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `callup`.`CallDetailRecord` (
  `callDetailRecordId` INT NOT NULL ,
  `subscriber` INT NOT NULL ,
  `campaign` INT NOT NULL ,
  `time` MEDIUMTEXT NULL ,
  `rate` DOUBLE NULL ,
  `resultCode` SET('REJECTED', 'SUCCESS', 'BUSY') NULL ,
  `gatewayCdrId` MEDIUMTEXT NULL ,
  PRIMARY KEY (`callDetailRecordId`, `subscriber`, `campaign`) ,
  INDEX `fk_CallDetailRecord_Subscriber1_idx` (`subscriber` ASC) ,
  INDEX `fk_CallDetailRecord_Campaign1_idx` (`campaign` ASC) ,
  CONSTRAINT `fk_CallDetailRecord_Subscriber1`
    FOREIGN KEY (`subscriber` )
    REFERENCES `callup`.`Subscriber` (`subscriberId` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_CallDetailRecord_Campaign1`
    FOREIGN KEY (`campaign` )
    REFERENCES `callup`.`Campaign` (`campaignId` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `callup`.`UserAudioFile`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `callup`.`UserAudioFile` (
  `User` INT NOT NULL ,
  `AudioFile` INT NOT NULL ,
  PRIMARY KEY (`User`, `AudioFile`) ,
  INDEX `fk_User_has_AudioFile_AudioFile1_idx` (`AudioFile` ASC) ,
  INDEX `fk_User_has_AudioFile_User1_idx` (`User` ASC) ,
  CONSTRAINT `fk_User_has_AudioFile_User1`
    FOREIGN KEY (`User` )
    REFERENCES `callup`.`User` (`userId` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_User_has_AudioFile_AudioFile1`
    FOREIGN KEY (`AudioFile` )
    REFERENCES `callup`.`AudioFile` (`audioFileId` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;



SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
