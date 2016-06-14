-- phpMyAdmin SQL Dump
-- version 4.6.2
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: Jun 13, 2016 at 11:31 AM
-- Server version: 5.5.49-MariaDB
-- PHP Version: 5.6.21

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `internetradio`
--
CREATE DATABASE IF NOT EXISTS `internetradio` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
USE `internetradio`;

-- --------------------------------------------------------

--
-- Table structure for table `stations`
--

CREATE TABLE `stations` (
  `ID` int(11) NOT NULL,
  `name` varchar(100) DEFAULT NULL,
  `url` varchar(200) DEFAULT NULL,
  `genre` varchar(50) DEFAULT NULL,
  `country` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `stations`
--

INSERT INTO `stations` (`ID`, `name`, `url`, `genre`, `country`) VALUES
(3, 'NPO Radio 1', 'http://icecast.omroep.nl:80/radio1-bb-mp3', 'News/Feature programmes', 'Netherlands'),
(5, 'NPO Radio 2', 'http://icecast.omroep.nl:80/radio2-bb-mp3', 'Adult Contemporary', 'Netherlands'),
(9, 'NPO 3FM', 'http://icecast.omroep.nl:80/3fm-bb-mp3', 'Top 40', 'Netherlands'),
(13, 'NPO Radio 4', 'http://icecast.omroep.nl:80/radio4-bb-mp3', 'Classical music', 'Netherlands'),
(17, 'NPO Radio 5', 'http://icecast.omroep.nl:80/radio5-bb-mp3', 'Light music', 'Netherlands'),
(19, 'NPO Radio 6', 'http://icecast.omroep.nl:80/radio6-bb-mp3', 'World/Jazz/Culture', 'Netherlands'),
(169, 'Q-music', 'http://icecast-qmusic.cdp.triple-it.nl:80/Qmusic_nl_live_96.mp3', 'Hot Adult Contemporary', 'Netherlands'),
(190, 'Radio 538', 'http://vip-icecast.538.lw.triple-it.nl:80/RADIO538_MP3', 'Top 40', 'Netherlands'),
(286, 'Efteling radio', 'http://vip-icecast.538.lw.triple-it.nl/WEB07_MP3', 'Sprookjesachtig', 'Laaf');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `stations`
--
ALTER TABLE `stations`
  ADD PRIMARY KEY (`ID`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `stations`
--
ALTER TABLE `stations`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=287;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
