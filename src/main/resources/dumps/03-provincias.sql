-- Crear Provincias

-- Provincias de Argentina
INSERT INTO provinces (name, country_id) VALUES
('Buenos Aires', (SELECT country_id FROM countries WHERE name = 'Argentina')),
('Córdoba', (SELECT country_id FROM countries WHERE name = 'Argentina')),
('Santa Fe', (SELECT country_id FROM countries WHERE name = 'Argentina')),
('Mendoza', (SELECT country_id FROM countries WHERE name = 'Argentina'));

-- Provincias de Uruguay
INSERT INTO provinces (name, country_id) VALUES
('Montevideo', (SELECT country_id FROM countries WHERE name = 'Uruguay')),
('Artigas', (SELECT country_id FROM countries WHERE name = 'Uruguay')),
('Maldonado', (SELECT country_id FROM countries WHERE name = 'Uruguay')),
('Florida', (SELECT country_id FROM countries WHERE name = 'Uruguay'));

-- Provincias de Paraguay
INSERT INTO provinces (name, country_id) VALUES
('Asunción', (SELECT country_id FROM countries WHERE name = 'Paraguay')),
('Concepción', (SELECT country_id FROM countries WHERE name = 'Paraguay')),
('Alto Paraná', (SELECT country_id FROM countries WHERE name = 'Paraguay')),
('San Pedro', (SELECT country_id FROM countries WHERE name = 'Paraguay'));

-- Provincias de Chile
INSERT INTO provinces (name, country_id) VALUES
('Santiago', (SELECT country_id FROM countries WHERE name = 'Chile')),
('Valparaíso', (SELECT country_id FROM countries WHERE name = 'Chile')),
('Valdivia', (SELECT country_id FROM countries WHERE name = 'Chile')),
('Coquimbo', (SELECT country_id FROM countries WHERE name = 'Chile'));

