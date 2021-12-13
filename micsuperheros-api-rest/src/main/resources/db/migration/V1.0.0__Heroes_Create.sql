DROP TABLE IF EXISTS superhero_powers;
DROP TABLE IF EXISTS superhero;

CREATE TABLE superhero (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(150) NOT NULL UNIQUE,
  universe VARCHAR(50) NOT NULL
);
CREATE INDEX idx_superhero_name ON superhero(name);

CREATE TABLE superhero_powers (
  superhero_id INT NOT NULL,
  power VARCHAR(100) NOT NULL,
  FOREIGN KEY(superhero_id) REFERENCES superhero(id)
);