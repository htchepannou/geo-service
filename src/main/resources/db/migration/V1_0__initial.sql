--- schemas

CREATE TABLE T_COUNTRY (
  id          INT          NOT NULL AUTO_INCREMENT,

  iso         CHAR(2)      NOT NULL,
  iso3        CHAR(3)      NOT NULL,
  name        VARCHAR(64)  NOT NULL UNIQUE,
  area        INT,
  population  INT,
  currency_code CHAR(3),
  languages   TEXT,
  tld         VARCHAR(10),

  insert_timestamp  DATETIME   DEFAULT CURRENT_TIMESTAMP,
  update_timestamp  DATETIME   DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

  PRIMARY KEY (id)
) ENGINE = InnoDB;

CREATE TABLE T_CITY (
  id                INT           NOT NULL AUTO_INCREMENT,
  country_fk        INT           NOT NULL REFERENCES T_COUNTRY(id),

  name              VARCHAR(200)  NOT NULL,
  ascii_name        VARCHAR(200)  NOT NULL,
  timezone          VARCHAR(40),
  population        INT,
  modification_date DATE,

  insert_timestamp  DATETIME   DEFAULT CURRENT_TIMESTAMP,
  update_timestamp  DATETIME   DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

  PRIMARY KEY (id),
  UNIQUE(country_fk, name)
) ENGINE = InnoDB;

CREATE TABLE T_ADDRESS (
  id              INT           NOT NULL AUTO_INCREMENT,
  city_fk         INT           NOT NULL REFERENCES T_CITY(id),

  street          VARCHAR(200),
  postal_code     VARCHAR(20),

  insert_timestamp  DATETIME   DEFAULT CURRENT_TIMESTAMP,
  update_timestamp  DATETIME   DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

  PRIMARY KEY (id)
) ENGINE = InnoDB;

CREATE TABLE T_GEO_POINT (
  id              INT           NOT NULL AUTO_INCREMENT,

  longitude       DOUBLE,
  latitude        DOUBLE,

  insert_timestamp  DATETIME   DEFAULT CURRENT_TIMESTAMP,
  update_timestamp  DATETIME   DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

  PRIMARY KEY (id)
) ENGINE = InnoDB;

CREATE TABLE T_FEATURE_TYPE (
  id              INT       NOT NULL AUTO_INCREMENT,
  name            VARCHAR(200)  NOT NULL,

  PRIMARY KEY (id)
) ENGINE = InnoDB;

CREATE TABLE T_FEATURE (
  id              INT           NOT NULL AUTO_INCREMENT,
  feature_type_fk INT           NOT NULL REFERENCES T_FEATURE_TYPE(id),
  city_fk         INT           NOT NULL REFERENCES T_CITY(id),
  address_fk      INT           REFERENCES T_ADDRESS(id),
  geo_point_fk    INT           REFERENCES T_GEO_POINT(id),

  name            VARCHAR(200)  NOT NULL,
  description     TEXT,
  phone           VARCHAR(64),
  fax             VARCHAR(64),
  website         TEXT,
  email           TEXT,
  longitude       DOUBLE,
  latitude        DOUBLE,

  insert_timestamp  DATETIME   DEFAULT CURRENT_TIMESTAMP,
  update_timestamp  DATETIME   DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

  PRIMARY KEY (id)
) ENGINE = InnoDB;

CREATE TABLE T_IMPORT_LOG(
  id                INT           NOT NULL AUTO_INCREMENT,
  url               VARCHAR(200),
  success           BOOL,
  rows              INT,
  error             TEXT,
  stack_trace       TEXT,

  insert_timestamp  DATETIME     DEFAULT CURRENT_TIMESTAMP,

  PRIMARY KEY (id)
) ENGINE = InnoDB;

CREATE TABLE T_LOCK(
  id                INT          NOT NULL AUTO_INCREMENT,
  name              VARCHAR(200) NOT NULL UNIQUE,
  owner             VARCHAR(200),

  insert_timestamp  DATETIME     DEFAULT CURRENT_TIMESTAMP,

  PRIMARY KEY (id)
) ENGINE = InnoDB;


--- Data
INSERT INTO T_FEATURE_TYPE VALUES
  (1, 'airport'),
  (2, 'train_station'),
  (3, 'bus_station')
;
