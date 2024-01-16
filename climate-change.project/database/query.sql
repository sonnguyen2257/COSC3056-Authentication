DROP TABLE IF EXISTS USER;

CREATE TABLE IF NOT EXISTS USER(
    USER_NAME VARCHAR(30),
    PASSWD VARCHAR(100), 
    DOWNLOAD_PERMIT BOOL DEFAULT 'FALSE' NOT NULL, 
    NIGHT_MODE BOOL DEFAULT'FALSE' NOT NULL, 
    PRIMARY KEY (USER_NAME));
    
DROP TABLE IF EXISTS `Population`;

CREATE TABLE IF NOT EXISTS `Population` (
  `Country_Name` varchar(40),
  `Country_Code` varchar(3),
  `Year` int,
  `Population` integer,
  PRIMARY KEY (`Country_Name`, `Country_Code`,`Year`)
);

DROP TABLE IF EXISTS `GlobalYearlyLandTempByCountry`;

CREATE TABLE IF NOT EXISTS `GlobalYearlyLandTempByCountry` (
  `Year` int,
  `AverageTemperature` double,
  `MinimumTemperature` double,
  `MaximumTemperature` double,
  `DataInterpolation` bool,
  `Country` varchar(40),
  PRIMARY KEY (`Year`, `Country`)
);

CREATE TABLE IF NOT EXISTS `GlobalYearlyTemp`;

CREATE TABLE `GlobalYearlyTemp` (
  `Year` int,
  `AverageTemperature` double,
  `MinimumTemperature` double,
  `MaximumTemperature` double,
  `LandOceanAverageTemperature` double,
  `LandOceanMinimumTemperature` double,
  `LandOceanMaximumTemperature` double,
  `DataInterpolation` bool,
  PRIMARY KEY (`Year`)
);

CREATE TABLE IF NOT EXISTS `GlobalYearlyLandTempByState`;

CREATE TABLE `GlobalYearlyLandTempByState` (
  `Year` int,
  `AverageTemperature` double,
  `MinimumTemperature` double,
  `MaximumTemperature` double,
  `State` varchar(40),
  `Country` varchar(40),
  `DataInterpolation` bool,
  PRIMARY KEY (`Year`, `State`, `Country`)
);

DROP TABLE IF EXISTS `GlobalYearlyLandTempByCity`;

CREATE TABLE IF NOT EXISTS `GlobalYearlyLandTempByCity` (
  `Year` int,
  `AverageTemperature` double,
  `MinimumTemperature` double,
  `MaximumTemperature` double,
  `City` varchar(40),
  `Country` varchar(40),
  `DataInterpolation` bool,
  `Latitude` varchar(10),
  `Longitude` varchar(10),
  PRIMARY KEY (`Year`, `City`, `Latitude`, `Longitude`)
);

