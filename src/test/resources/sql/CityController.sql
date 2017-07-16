INSERT INTO T_COUNTRY(id, iso, iso3, name, population, area, tld, languages, currency_code) VALUES(2233387, 'CM', 'CMR', 'Cameroon', 19294149, 475440, '.cm', 'fr-CM, en-CM', 'XAF');
INSERT INTO T_COUNTRY(id, iso, iso3, name, population, area, tld, languages, currency_code) VALUES(2233388, 'FR', 'FRA', 'France', 19294149, 475440, '.fr', 'fr-FR', 'EUR');

INSERT INTO T_CITY(id, country_fk, name, ascii_name, timezone, population) VALUES(223338700, 2233387, 'Yaounde', 'Yaounde', 'Africa/Douala', 1239475);
INSERT INTO T_CITY(id, country_fk, name, ascii_name, timezone, population) VALUES(223338701, 2233387, 'Douala', 'Douala', 'Africa/Douala', 2239475);
INSERT INTO T_CITY(id, country_fk, name, ascii_name, timezone, population) VALUES(223338702, 2233387, 'Bafoussam', 'Bafoussam', 'Africa/Douala', 0);
INSERT INTO T_CITY(id, country_fk, name, ascii_name, timezone, population) VALUES(223338703, 2233387, 'Bafia', 'Bafia', 'Africa/Douala', 0);
INSERT INTO T_CITY(id, country_fk, name, ascii_name, timezone, population) VALUES(223338704, 2233387, 'Bangoua', 'Bangoua', 'Africa/Douala', 0);


INSERT INTO T_CITY(id, country_fk, name, ascii_name, timezone, population) VALUES(223338800, 2233388, 'Baham', 'Baham', 'Africa/Douala', 0);
