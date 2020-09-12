-- phpMyAdmin SQL Dump
-- version 4.9.2
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1:3306
-- Generation Time: Sep 12, 2020 at 10:14 PM
-- Server version: 10.4.10-MariaDB
-- PHP Version: 7.4.0

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `emploitemps`
--

-- --------------------------------------------------------

--
-- Table structure for table `annee`
--

DROP TABLE IF EXISTS `annee`;
CREATE TABLE IF NOT EXISTS `annee` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `active` bit(1) DEFAULT NULL,
  `adresse` varchar(255) DEFAULT NULL,
  `debut` int(11) DEFAULT NULL,
  `departement` varchar(255) DEFAULT NULL,
  `fin` int(11) DEFAULT NULL,
  `name_etablissement` varchar(255) DEFAULT NULL,
  `nom_etablissement` varchar(255) DEFAULT NULL,
  `pays` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `region` varchar(255) DEFAULT NULL,
  `sender_id` varchar(255) DEFAULT NULL,
  `slogan` varchar(255) DEFAULT NULL,
  `ville` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `annee`
--

INSERT INTO `annee` (`id`, `active`, `adresse`, `debut`, `departement`, `fin`, `name_etablissement`, `nom_etablissement`, `pays`, `phone`, `region`, `sender_id`, `slogan`, `ville`) VALUES
(1, b'1', 'rue ngwele', 2020, 'wouri', 2021, 'yvanschool', 'yvanschool', 'cameroun', '690735187', 'littoral', '9091', 'paix-unite-reussite', 'douala');

-- --------------------------------------------------------

--
-- Table structure for table `cours`
--

DROP TABLE IF EXISTS `cours`;
CREATE TABLE IF NOT EXISTS `cours` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `jour` int(11) NOT NULL,
  `id_creneau` bigint(20) DEFAULT NULL,
  `matiereteacher_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKmwlkdx0i2qx31a6597m3as65j` (`id_creneau`),
  KEY `FKkci1xlnavmgo7x3dcexgoiudy` (`matiereteacher_id`)
) ENGINE=MyISAM AUTO_INCREMENT=24 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `cours`
--

INSERT INTO `cours` (`id`, `jour`, `id_creneau`, `matiereteacher_id`) VALUES
(1, 1, 1, 7),
(2, 1, 2, 7),
(3, 4, 5, 7),
(4, 4, 6, 7),
(5, 3, 2, 7),
(6, 3, 3, 7),
(8, 1, 3, 8),
(9, 1, 4, 8),
(10, 2, 1, 8),
(11, 2, 2, 8),
(12, 2, 3, 9),
(13, 2, 4, 9),
(14, 2, 5, 9),
(15, 2, 6, 9),
(16, 4, 1, 9),
(17, 4, 2, 9),
(18, 4, 3, 9),
(19, 4, 4, 9),
(20, 4, 4, 10),
(21, 4, 3, 10),
(22, 2, 2, 11),
(23, 2, 1, 11);

-- --------------------------------------------------------

--
-- Table structure for table `creneau`
--

DROP TABLE IF EXISTS `creneau`;
CREATE TABLE IF NOT EXISTS `creneau` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `heure_debut` time DEFAULT NULL,
  `heure_fin` time DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=9 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `creneau`
--

INSERT INTO `creneau` (`id`, `heure_debut`, `heure_fin`) VALUES
(1, '07:30:00', '08:30:00'),
(2, '08:30:00', '09:30:00'),
(3, '09:30:00', '10:30:00'),
(4, '10:30:00', '11:30:00'),
(5, '11:30:00', '12:30:00'),
(6, '12:30:00', '13:30:00'),
(7, '13:30:00', '14:30:00'),
(8, '14:30:00', '15:30:00');

-- --------------------------------------------------------

--
-- Table structure for table `matiere`
--

DROP TABLE IF EXISTS `matiere`;
CREATE TABLE IF NOT EXISTS `matiere` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `coef` double DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `note_sur` int(11) DEFAULT NULL,
  `taux_horaire` int(11) DEFAULT NULL,
  `promo_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKc1b6l2vxw8x8q9yoocj3rgu26` (`promo_id`)
) ENGINE=MyISAM AUTO_INCREMENT=8 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `matiere`
--

INSERT INTO `matiere` (`id`, `coef`, `name`, `note_sur`, `taux_horaire`, `promo_id`) VALUES
(1, 3, 'Francais', 20, 1500, 1),
(2, 4, 'Francais', 20, 2500, 2),
(3, 3, 'Anglais', 20, 1500, 1),
(4, 3, 'Anglais', 20, 2000, 2),
(5, 4, 'Maths generales', 20, 2000, 1),
(6, 5, 'Litterature', 20, 3500, 5),
(7, 5, 'Litterature', 20, 4000, 6);

-- --------------------------------------------------------

--
-- Table structure for table `matiere_teacher`
--

DROP TABLE IF EXISTS `matiere_teacher`;
CREATE TABLE IF NOT EXISTS `matiere_teacher` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `matiere_id` bigint(20) DEFAULT NULL,
  `teacher_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK8u7gao0bvybq144xgtv8ybl0w` (`matiere_id`),
  KEY `FKte3n3qnx1wucsiofq0bggp78q` (`teacher_id`)
) ENGINE=MyISAM AUTO_INCREMENT=12 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `matiere_teacher`
--

INSERT INTO `matiere_teacher` (`id`, `matiere_id`, `teacher_id`) VALUES
(1, 1, 1),
(2, 2, 1),
(3, 3, 2),
(4, 4, 3),
(5, 5, 2),
(6, 6, 4),
(7, 1, 2),
(8, 3, 1),
(9, 5, 3),
(10, 2, 4),
(11, 4, 2);

-- --------------------------------------------------------

--
-- Table structure for table `promo`
--

DROP TABLE IF EXISTS `promo`;
CREATE TABLE IF NOT EXISTS `promo` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `capacite` int(11) DEFAULT NULL,
  `classe` varchar(255) NOT NULL,
  `montant_scolarite` double DEFAULT NULL,
  `annee_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK95onvy1rmurys7sa3ojdd2j9v` (`annee_id`)
) ENGINE=MyISAM AUTO_INCREMENT=8 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `promo`
--

INSERT INTO `promo` (`id`, `capacite`, `classe`, `montant_scolarite`, `annee_id`) VALUES
(1, 45, '6eme', 35000, 1),
(2, 42, '5eme', 20000, 1),
(3, 40, '4eme', 22000, 1),
(4, 41, '3eme', 21500, 1),
(5, 50, '2eme', 27500, 1),
(6, 49, '1ere', 23500, 1),
(7, 53, 'Tle', 26000, 1);

-- --------------------------------------------------------

--
-- Table structure for table `seance`
--

DROP TABLE IF EXISTS `seance`;
CREATE TABLE IF NOT EXISTS `seance` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `enabled` bit(1) NOT NULL,
  `jour` date DEFAULT NULL,
  `id_cours` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK2m8aw7gjwv3l098ji1bmvg8bo` (`id_cours`)
) ENGINE=MyISAM AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `seance`
--

INSERT INTO `seance` (`id`, `enabled`, `jour`, `id_cours`) VALUES
(1, b'1', '2020-09-08', 10),
(2, b'1', '2020-09-08', 11);

-- --------------------------------------------------------

--
-- Table structure for table `teacher`
--

DROP TABLE IF EXISTS `teacher`;
CREATE TABLE IF NOT EXISTS `teacher` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `domicile` varchar(255) DEFAULT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `nationalite` varchar(255) DEFAULT NULL,
  `sexe` varchar(20) DEFAULT NULL,
  `telephone` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `teacher`
--

INSERT INTO `teacher` (`id`, `domicile`, `first_name`, `last_name`, `nationalite`, `sexe`, `telephone`) VALUES
(1, 'Douala', 'Patrick', 'edgard', 'camerounaise', 'Homme', '693215487'),
(2, 'Douala', 'Marin', 'Boris', 'camerounaise', 'Homme', '693827145'),
(3, 'Yaoundé', 'samuel', 'soro', 'camerounaise', 'Homme', '653214897'),
(4, 'Douala', 'Marceline', 'Ambo', 'camerounaise', 'Femme', '678935421');

-- --------------------------------------------------------

--
-- Table structure for table `teacher_principal`
--

DROP TABLE IF EXISTS `teacher_principal`;
CREATE TABLE IF NOT EXISTS `teacher_principal` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `annee` int(11) DEFAULT NULL,
  `promo_id` bigint(20) NOT NULL,
  `teacher_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_l6g3h7kq4jao0vqvf92qt00lj` (`promo_id`),
  UNIQUE KEY `UK_gb5p1icn8cm07fy4dgs2xy7ar` (`teacher_id`)
) ENGINE=MyISAM AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `teacher_principal`
--

INSERT INTO `teacher_principal` (`id`, `annee`, `promo_id`, `teacher_id`) VALUES
(1, 2020, 1, 1);

-- --------------------------------------------------------

--
-- Table structure for table `teacher_promos`
--

DROP TABLE IF EXISTS `teacher_promos`;
CREATE TABLE IF NOT EXISTS `teacher_promos` (
  `teachers_id` bigint(20) NOT NULL,
  `promos_id` bigint(20) NOT NULL,
  KEY `FKl4y8ndfmwduxjqkaj05jfhtlx` (`promos_id`),
  KEY `FKsww54e75vghhdeuiv0a8ldm0t` (`teachers_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `teacher_promos`
--

INSERT INTO `teacher_promos` (`teachers_id`, `promos_id`) VALUES
(1, 1),
(1, 2),
(3, 2),
(3, 1),
(3, 4),
(2, 2),
(2, 5);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
