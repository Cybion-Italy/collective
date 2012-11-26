-- phpMyAdmin SQL Dump
-- version 3.2.5
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generato il: 03 ago, 2011 at 11:16 AM
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
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='received messages' AUTO_INCREMENT=92 ;

-- --------------------------------------------------------

--
-- Struttura della tabella `project_semantic_search`
--

CREATE TABLE `project_semantic_search` (
  `project_id` bigint(20) NOT NULL,
  `semantic_search_id` bigint(20) NOT NULL,
  KEY `user_id` (`project_id`,`semantic_search_id`),
  KEY `semantic_search_id` (`semantic_search_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='join table';

-- --------------------------------------------------------

--
-- Struttura della tabella `semantic_search`
--

CREATE TABLE `semantic_search` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `title` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `object` longtext COLLATE utf8_unicode_ci COMMENT 'contains the json serialized search',
  `last_profilation_date` bigint(20) DEFAULT NULL COMMENT 'last profilation date',
  KEY `id` (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=12 ;

-- --------------------------------------------------------

--
-- Struttura della tabella `user_semantic_search`
--

CREATE TABLE `user_semantic_search` (
  `user_id` bigint(20) NOT NULL,
  `semantic_search_id` bigint(20) NOT NULL,
  KEY `user_id` (`user_id`,`semantic_search_id`),
  KEY `semantic_search_id` (`semantic_search_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='join table';

--
-- Limiti per le tabelle scaricate
--

--
-- Limiti per la tabella `project_semantic_search`
--
ALTER TABLE `project_semantic_search`
  ADD CONSTRAINT `project_semantic_search_ibfk_1` FOREIGN KEY (`semantic_search_id`) REFERENCES `semantic_search` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Limiti per la tabella `user_semantic_search`
--
ALTER TABLE `user_semantic_search`
  ADD CONSTRAINT `user_semantic_search_ibfk_1` FOREIGN KEY (`semantic_search_id`) REFERENCES `semantic_search` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;
