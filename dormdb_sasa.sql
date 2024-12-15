-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Dec 15, 2024 at 05:02 AM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `dormdb_sasa`
--

-- --------------------------------------------------------

--
-- Table structure for table `facilities`
--

CREATE TABLE `facilities` (
  `id` int(11) NOT NULL,
  `equipment_type` varchar(100) NOT NULL,
  `description` varchar(255) NOT NULL,
  `status` enum('Available','Faulty','Being Used') NOT NULL DEFAULT 'Available'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `facilities`
--

INSERT INTO `facilities` (`id`, `equipment_type`, `description`, `status`) VALUES
(1, 'Vacuum Cleaner', 'Tineco A11 Hero', 'Faulty'),
(5, 'Broom Stick', 'Broom', 'Faulty');

-- --------------------------------------------------------

--
-- Table structure for table `rooms`
--

CREATE TABLE `rooms` (
  `id` int(11) NOT NULL,
  `room_number` varchar(10) NOT NULL,
  `room_type` enum('Male Double','Female Double','Female Single','Male Single') NOT NULL,
  `total_slots` int(11) NOT NULL,
  `remaining_slots` int(11) NOT NULL CHECK (`remaining_slots` <= `total_slots`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `rooms`
--

INSERT INTO `rooms` (`id`, `room_number`, `room_type`, `total_slots`, `remaining_slots`) VALUES
(3, '308', 'Male Single', 1, 1),
(4, '303', 'Male Double', 2, 1),
(6, '307', 'Female Double', 2, 0),
(7, '310', 'Male Single', 1, 0),
(13, '506', 'Male Double', 2, 2),
(16, '123', 'Female Double', 1, 1);

-- --------------------------------------------------------

--
-- Table structure for table `room_assignments`
--

CREATE TABLE `room_assignments` (
  `id` int(11) NOT NULL,
  `tenant_id` int(11) NOT NULL,
  `room_id` int(11) NOT NULL,
  `stay_from` date NOT NULL,
  `stay_to` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `tenants`
--

CREATE TABLE `tenants` (
  `id` int(11) NOT NULL,
  `full_name` varchar(100) NOT NULL,
  `gender` enum('Male','Female') NOT NULL,
  `room_id` int(11) DEFAULT NULL,
  `stay_from` date DEFAULT NULL,
  `stay_to` date DEFAULT NULL,
  `mobile_number` varchar(15) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `tenants`
--

INSERT INTO `tenants` (`id`, `full_name`, `gender`, `room_id`, `stay_from`, `stay_to`, `mobile_number`) VALUES
(4, 'James Jerico Aquino', 'Male', 16, '2024-12-06', '2027-12-18', '0961952350'),
(5, 'Mary Anne Louise Mabutas', 'Female', 4, '2024-11-23', '2027-12-31', '09619523509'),
(6, 'Sean Zebedee Clavio', 'Male', NULL, '2024-11-30', '2027-06-08', '1231312313'),
(7, 'Mikko Dumaguin', 'Male', NULL, '2024-11-23', '2027-06-23', '123123123123131'),
(8, 'Ivan Andre Santos', 'Male', NULL, '2024-11-23', '2027-06-23', '131313123123131'),
(9, 'Carizza Jhayne Bustamante', 'Female', 6, '2024-11-27', '2032-12-17', '123123123123131'),
(10, 'Karla IDK Last name', 'Female', 6, '2024-11-30', '2025-01-31', '09619523507'),
(11, 'Denmark Benedict Elpa', 'Male', 7, '2024-11-27', '2030-08-27', '812375182367198'),
(15, 'Jims Aquino Gwapo', 'Male', NULL, NULL, NULL, '12345678901');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `username`, `password`, `created_at`) VALUES
(1, 'justin', '$2y$10$cgooA16ili5Hw4jCYa3UkOB81IT/9iB7ipFhAD9a2eDexoreBrlq2', '2024-11-23 07:58:04'),
(2, 'mary', '$2y$10$xmygFtR4sc8foqHR4ZRj0.GKz8Lc90rj6ciqo.erjNBNLgiCSKc8i', '2024-11-27 09:20:07'),
(5, 'arun', '$2a$10$7iS/k64cAjAWu8M4a8f1FuUA8l168UwWbRgGAtOETewoCq9gvcJxq', '2024-12-03 01:55:34'),
(6, 'james', '$2a$10$XymUbA6bYN05PchuptTyxeap6xzeJtFDOD70jGXQewD4AitQOIUPO', '2024-12-03 02:07:20'),
(9, 'asdfasdf', '$2a$10$I/XCrVuBPmUzisqz9.0yUe53gvsqm611VCMPh0Ftm.mIMVkd4DfdG', '2024-12-03 02:08:18'),
(11, 'sean', '$2a$10$Wn0DTixLGMDLLL1tPWvqDuZK.N84C5lyMx6z4e1ErHDRLVFOllJ5.', '2024-12-03 02:08:50'),
(13, 'zebedee', '$2a$10$LiJFyTMKoh8QXFQTb0TEY.12ERmLKeT5YQXYTn2LkBIfPH7h7wjIG', '2024-12-03 02:09:03'),
(14, 'dm', '$2a$10$/acBNliqNkocU6H22aTJse3Oo.OqYtNJ98yXUMzznRo3i9WOG4jRu', '2024-12-03 02:46:29'),
(15, 'mary1', '$2y$10$XFNxuLWSU0he8BspAC/rkeYzUR1uKb2A0ILThIU1B0/zhnYDZHcOq', '2024-12-12 06:05:29');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `facilities`
--
ALTER TABLE `facilities`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `rooms`
--
ALTER TABLE `rooms`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `room_number` (`room_number`);

--
-- Indexes for table `room_assignments`
--
ALTER TABLE `room_assignments`
  ADD PRIMARY KEY (`id`),
  ADD KEY `tenant_id` (`tenant_id`),
  ADD KEY `room_id` (`room_id`);

--
-- Indexes for table `tenants`
--
ALTER TABLE `tenants`
  ADD PRIMARY KEY (`id`),
  ADD KEY `room_id` (`room_id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `username` (`username`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `facilities`
--
ALTER TABLE `facilities`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `rooms`
--
ALTER TABLE `rooms`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=17;

--
-- AUTO_INCREMENT for table `room_assignments`
--
ALTER TABLE `room_assignments`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `tenants`
--
ALTER TABLE `tenants`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=16;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=16;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `room_assignments`
--
ALTER TABLE `room_assignments`
  ADD CONSTRAINT `room_assignments_ibfk_1` FOREIGN KEY (`tenant_id`) REFERENCES `tenants` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `room_assignments_ibfk_2` FOREIGN KEY (`room_id`) REFERENCES `rooms` (`id`) ON DELETE CASCADE;

--
-- Constraints for table `tenants`
--
ALTER TABLE `tenants`
  ADD CONSTRAINT `tenants_ibfk_1` FOREIGN KEY (`room_id`) REFERENCES `rooms` (`id`) ON DELETE SET NULL;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
