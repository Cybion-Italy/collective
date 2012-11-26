-- phpMyAdmin SQL Dump
-- version 3.2.5
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generato il: 21 apr, 2011 at 04:06 PM
-- Versione MySQL: 5.1.44
-- Versione PHP: 5.3.2

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";

--
-- Database: `collective-messages`
--

-- --------------------------------------------------------

--
-- Struttura della tabella `messages`
--

CREATE TABLE `messages` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `type` varchar(25) COLLATE utf8_unicode_ci NOT NULL,
  `action` varchar(25) COLLATE utf8_unicode_ci NOT NULL,
  `object` longtext COLLATE utf8_unicode_ci NOT NULL COMMENT 'json encoded object',
  `time` bigint(20) NOT NULL COMMENT 'consuming time',
  `is_analyzed` tinyint(1) NOT NULL DEFAULT '0' COMMENT 'if it was analyzed',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='received messages' AUTO_INCREMENT=78 ;
