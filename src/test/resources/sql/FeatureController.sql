INSERT INTO T_COUNTRY(id, iso, iso3, name, population, area, tld, languages, currency_code) VALUES(2233387, 'CM', 'CMR', 'Cameroon', 19294149, 475440, '.cm', 'fr-CM, en-CM', 'XAF');

INSERT INTO T_CITY(id, country_fk, name, ascii_name, timezone, population) VALUES(223338700, 2233387, 'Yaounde', 'Yaounde', 'Africa/Douala', 1239475);

INSERT INTO T_ADDRESS(id, city_fk, street, postal_code) VALUES(100, 223338700, '3030 Linton', '123');
INSERT INTO T_ADDRESS(id, city_fk, street, postal_code) VALUES(101, 223338700, '2095 Saint Laurent', '456');

INSERT INTO T_GEO_POINT(id, latitude, longitude) VALUES(100, 1, 2);
INSERT INTO T_GEO_POINT(id, latitude, longitude) VALUES(101, 3, 4);

INSERT INTO T_FEATURE_TYPE(id, name) VALUES(100, 'bus_station');
INSERT INTO T_FEATURE_TYPE(id, name) VALUES(200, 'airport');

INSERT INTO T_FEATURE(id, feature_type_fk, city_fk, address_fk, geo_point_fk, name, description, phone, fax, email, website)
    VALUES(100, 100, 223338700, 100, 100, 'gare1', 'description1', '1111111', '1111112', 'gare1@gmail.com', 'http://www.gare1.com');

INSERT INTO T_FEATURE(id, feature_type_fk, city_fk, address_fk, geo_point_fk, name, description, phone, fax, email, website)
  VALUES(101, 100, 223338700, 101, 101, 'gare2', 'description2', '2111111', '2111112', 'gare2@gmail.com', 'http://www.gare2.com');

INSERT INTO T_FEATURE(id, feature_type_fk, city_fk, name)
  VALUES(200, 200, 223338700,'Nsimalen');

INSERT INTO T_FEATURE(id, feature_type_fk, city_fk, name)
  VALUES(201, 200, 223338700,'Douala');
