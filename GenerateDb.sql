-- MySQL Script generated by MySQL Workbench
-- Sun Mar 24 10:40:39 2024
-- Model: New Model    Version: 1.0
-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema onlinevintageshop
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema onlinevintageshop
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `onlinevintageshop` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci ;
USE `onlinevintageshop` ;

-- -----------------------------------------------------
-- Table `onlinevintageshop`.`categories`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `onlinevintageshop`.`categories` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(50) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `onlinevintageshop`.`products`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `onlinevintageshop`.`products` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(100) NOT NULL,
  `price` DECIMAL(10,2) NOT NULL,
  `manufacturing_year` YEAR NOT NULL,
  `quality` VARCHAR(50) NOT NULL,
  `quantity` INT NOT NULL,
  `description` VARCHAR(500) NOT NULL,
  `category_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `category_id` (`category_id` ASC) VISIBLE,
  CONSTRAINT `products_ibfk_1`
    FOREIGN KEY (`category_id`)
    REFERENCES `onlinevintageshop`.`categories` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `onlinevintageshop`.`users`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `onlinevintageshop`.`users` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(100) NOT NULL,
  `role` VARCHAR(50) NOT NULL,
  `email` VARCHAR(100) NOT NULL,
  `address` VARCHAR(200) NOT NULL,
  `password` CHAR(10) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `onlinevintageshop`.`shopping_carts`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `onlinevintageshop`.`shopping_carts` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `total_sum` DECIMAL(10,0) NOT NULL,
  `user_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `user_id` (`user_id` ASC) VISIBLE,
  CONSTRAINT `shopping_carts_ibfk_1`
    FOREIGN KEY (`user_id`)
    REFERENCES `onlinevintageshop`.`users` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `onlinevintageshop`.`shopping_cart_items`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `onlinevintageshop`.`shopping_cart_items` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `product_id` INT NOT NULL,
  `shopping_cart_id` INT NOT NULL,
  `price` DECIMAL(10,2) NOT NULL,
  `quantity` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `product_id` (`product_id` ASC) VISIBLE,
  INDEX `shopping_cart_id` (`shopping_cart_id` ASC) VISIBLE,
  CONSTRAINT `shopping_cart_items_ibfk_1`
    FOREIGN KEY (`product_id`)
    REFERENCES `onlinevintageshop`.`products` (`id`),
  CONSTRAINT `shopping_cart_items_ibfk_2`
    FOREIGN KEY (`shopping_cart_id`)
    REFERENCES `onlinevintageshop`.`shopping_carts` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
