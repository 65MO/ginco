INSERT INTO  alignment_type VALUES (1, 'Equivalence exacte', '=EQ', false, true);
INSERT INTO  alignment_type VALUES (2, 'Equivalence inexacte', '~EQ', false, true);
INSERT INTO  alignment_type VALUES (3, 'Alignement générique', 'BM', false, false);
INSERT INTO  alignment_type VALUES (4, 'Alignement spécifique', 'NM', false, false);
INSERT INTO  alignment_type VALUES (5, 'Alignement générique générique', 'BMG', false, false);
INSERT INTO  alignment_type VALUES (6, 'Alignement spécifique générique', 'NMG', false, false);
INSERT INTO  alignment_type VALUES (7, 'Alignement générique partitif', 'BMP', false, false);
INSERT INTO  alignment_type VALUES (8, 'Alignement spécifique partitif', 'NMP', false, false);
INSERT INTO  alignment_type VALUES (9, 'Alignement générique instance', 'BMI', false, false);
INSERT INTO  alignment_type VALUES (10, 'Alignement spécifique instance', 'NMI', false, false);
INSERT INTO  alignment_type VALUES (11, 'Alignement associatif', 'RM', false, false);

INSERT INTO  external_thesaurus_type VALUES (1, 'Autorités');
INSERT INTO  external_thesaurus_type VALUES (2, 'Classification');
INSERT INTO  external_thesaurus_type VALUES (3, 'Ontologie');
INSERT INTO  external_thesaurus_type VALUES (4, 'Taxonomie');
INSERT INTO  external_thesaurus_type VALUES (5, 'Terminologie');
INSERT INTO  external_thesaurus_type VALUES (6, 'Thésaurus');
INSERT INTO  external_thesaurus_type VALUES (7, 'Vedettes-matière');
INSERT INTO  external_thesaurus_type VALUES (8, 'Autre');