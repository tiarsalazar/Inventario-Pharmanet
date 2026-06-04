CREATE TABLE region (
  region_id INT AUTO_INCREMENT,
  cod_region VARCHAR(2) NOT NULL UNIQUE,
  descripcion VARCHAR(60) NOT NULL UNIQUE,
  PRIMARY KEY (region_id)
);

CREATE TABLE comuna (
  comuna_id INT AUTO_INCREMENT,
  cod_comuna INT NOT NULL UNIQUE,
  region VARCHAR(2) NOT NULL,
  descripcion VARCHAR(30) NOT NULL UNIQUE,
  PRIMARY KEY (comuna_id),
  FOREIGN KEY (region) REFERENCES region(cod_region)
);