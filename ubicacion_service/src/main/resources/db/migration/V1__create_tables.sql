CREATE TABLE region (
  region_id INT AUTO_INCREMENT,
  descripcion VARCHAR(60) NOT NULL,
  PRIMARY KEY (region_id)
);

CREATE TABLE comuna (
  comuna_id INT AUTO_INCREMENT,
  region INT NOT NULL,
  descripcion VARCHAR(30) NOT NULL,
  PRIMARY KEY (comuna_id),
  FOREIGN KEY (region) REFERENCES region(region_id)
);