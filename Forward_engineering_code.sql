-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema studio_medico
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema studio_medico
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `studio_medico` DEFAULT CHARACTER SET utf8 ;
USE `studio_medico` ;

-- -----------------------------------------------------
-- Table `studio_medico`.`pazienti`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `studio_medico`.`pazienti` (
  `Nome` VARCHAR(45) NOT NULL,
  `cognome` VARCHAR(45) NOT NULL,
  `dataNascita` VARCHAR(10) NOT NULL,
  `codiceFiscale` VARCHAR(45) NOT NULL,
  `indirizzo` VARCHAR(100) NOT NULL,
  `telefono` VARCHAR(20) NOT NULL,
  `email` VARCHAR(45) NOT NULL,
  `noteCliniche` VARCHAR(200) NULL,
  PRIMARY KEY (`codiceFiscale`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `studio_medico`.`segretari`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `studio_medico`.`segretari` (
  `nome` VARCHAR(45) NOT NULL,
  `cognome` VARCHAR(45) NOT NULL,
  `email` VARCHAR(45) NOT NULL,
  `password` VARCHAR(20) NOT NULL,
  PRIMARY KEY (`email`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `studio_medico`.`medici`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `studio_medico`.`medici` (
  `nome` VARCHAR(45) NOT NULL,
  `cognome` VARCHAR(45) NOT NULL,
  `specializzazione` VARCHAR(45) NOT NULL,
  `telefono` VARCHAR(20) NOT NULL,
  `email` VARCHAR(45) NOT NULL,
  `password` VARCHAR(20) NOT NULL,
  UNIQUE INDEX `Telefono_UNIQUE` (`telefono` ASC) VISIBLE,
  PRIMARY KEY (`email`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `studio_medico`.`visite`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `studio_medico`.`visite` (
  `idVisita` INT NOT NULL AUTO_INCREMENT,
  `dataOra` VARCHAR(20) NOT NULL,
  `prescrizione` VARCHAR(200) NULL,
  `pazienteCodiceFiscale` VARCHAR(45) NOT NULL,
  `segretarioEmail` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`idVisita`),
  INDEX `fk_Visite_Pazienti1_idx` (`pazienteCodiceFiscale` ASC) VISIBLE,
  INDEX `fk_Visite_Segretari1_idx` (`segretarioEmail` ASC) VISIBLE,
  CONSTRAINT `fk_Visite_Pazienti1`
    FOREIGN KEY (`pazienteCodiceFiscale`)
    REFERENCES `studio_medico`.`pazienti` (`codiceFiscale`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Visite_Segretari1`
    FOREIGN KEY (`segretarioEmail`)
    REFERENCES `studio_medico`.`segretari` (`email`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `studio_medico`.`pagamenti`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `studio_medico`.`pagamenti` (
  `idPagamento` INT NOT NULL AUTO_INCREMENT,
  `stato` VARCHAR(20) NOT NULL,
  `importo` DECIMAL(5,2) NOT NULL,
  `segretarioEmail` VARCHAR(45) NOT NULL,
  `visitaIdVisita` INT NOT NULL,
  PRIMARY KEY (`idPagamento`),
  INDEX `fk_Pagamenti_Segretari1_idx` (`segretarioEmail` ASC) VISIBLE,
  INDEX `fk_Pagamenti_Visite1_idx` (`visitaIdVisita` ASC) VISIBLE,
  CONSTRAINT `fk_Pagamenti_Segretari1`
    FOREIGN KEY (`segretarioEmail`)
    REFERENCES `studio_medico`.`segretari` (`email`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Pagamenti_Visite1`
    FOREIGN KEY (`visitaIdVisita`)
    REFERENCES `studio_medico`.`visite` (`idVisita`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `studio_medico`.`fasceOrarie`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `studio_medico`.`fasceOrarie` (
  `idFasciaOraria` INT NOT NULL AUTO_INCREMENT,
  `data` VARCHAR(10) NOT NULL,
  `oraInizio` VARCHAR(5) NOT NULL,
  `oraFine` VARCHAR(5) NOT NULL,
  PRIMARY KEY (`idFasciaOraria`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `studio_medico`.`fasceOrarie_has_medici`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `studio_medico`.`fasceOrarie_has_medici` (
  `fasciaOraria_id` INT NOT NULL,
  `medicoEmail` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`fasciaOraria_id`, `medicoEmail`),
  INDEX `fk_fasceOrarie_has_medici_medici1_idx` (`medicoEmail` ASC) VISIBLE,
  INDEX `fk_fasceOrarie_has_medici_fasceOrarie1_idx` (`fasciaOraria_id` ASC) VISIBLE,
  CONSTRAINT `fk_fasceOrarie_has_medici_fasceOrarie1`
    FOREIGN KEY (`fasciaOraria_id`)
    REFERENCES `studio_medico`.`fasceOrarie` (`idFasciaOraria`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_fasceOrarie_has_medici_medici1`
    FOREIGN KEY (`medicoEmail`)
    REFERENCES `studio_medico`.`medici` (`email`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `studio_medico`.`prenotazioni`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `studio_medico`.`prenotazioni` (
  `idPrenotazioni` INT NOT NULL AUTO_INCREMENT,
  `paziente_codiceFiscale` VARCHAR(45) NOT NULL,
  `medico_email` VARCHAR(45) NOT NULL,
  `fasciaOrariaId` INT NOT NULL,
  PRIMARY KEY (`idPrenotazioni`, `fasciaOrariaId`),
  INDEX `fk_prenotazioni_pazienti1_idx` (`paziente_codiceFiscale` ASC) VISIBLE,
  INDEX `fk_prenotazioni_medici1_idx` (`medico_email` ASC) VISIBLE,
  INDEX `fk_prenotazioni_fasceOrarie1_idx` (`fasciaOrariaId` ASC) VISIBLE,
  CONSTRAINT `fk_prenotazioni_pazienti1`
    FOREIGN KEY (`paziente_codiceFiscale`)
    REFERENCES `studio_medico`.`pazienti` (`codiceFiscale`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_prenotazioni_medici1`
    FOREIGN KEY (`medico_email`)
    REFERENCES `studio_medico`.`medici` (`email`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_prenotazioni_fasceOrarie1`
    FOREIGN KEY (`fasciaOrariaId`)
    REFERENCES `studio_medico`.`fasceOrarie` (`idFasciaOraria`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
