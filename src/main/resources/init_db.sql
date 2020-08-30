CREATE SCHEMA `internet_shop` DEFAULT CHARACTER SET utf8;

CREATE TABLE `internet_shop`.`users`
(
    `user_id`   bigint         NOT NULL AUTO_INCREMENT,
    `user_name` varchar(256)   NOT NULL,
    `pass`      varchar(256)   NOT NULL,
    `login`     varchar(256)   NOT NULL,
    `salt`      varbinary(500) NOT NULL,
    PRIMARY KEY (`user_id`),
    UNIQUE KEY `user_id_UNIQUE` (`user_id`),
    UNIQUE KEY `user_login_UNIQUE` (`login`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE `internet_shop`.`orders`
(
    `order_id` bigint NOT NULL AUTO_INCREMENT,
    `user_id`  bigint NOT NULL,
    PRIMARY KEY (`order_id`),
    UNIQUE KEY `order_id_UNIQUE` (`order_id`),
    KEY `orders_users_fk_idx` (`user_id`),
    CONSTRAINT `orders_users_fk` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE `internet_shop`.`products`
(
    `product_id`   bigint         NOT NULL AUTO_INCREMENT,
    `product_name` varchar(256)   NOT NULL,
    `price`        decimal(25, 2) NOT NULL,
    PRIMARY KEY (`product_id`),
    UNIQUE KEY `product_id_UNIQUE` (`product_id`),
    UNIQUE KEY `product_name_UNIQUE` (`product_name`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE `internet_shop`.`roles`
(
    `role_id`   bigint       NOT NULL AUTO_INCREMENT,
    `role_name` varchar(256) NOT NULL,
    PRIMARY KEY (`role_id`),
    UNIQUE KEY `role_id_UNIQUE` (`role_id`),
    UNIQUE KEY `role_name_UNIQUE` (`role_name`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE `internet_shop`.`shopping_carts`
(
    `cart_id` bigint NOT NULL AUTO_INCREMENT,
    `user_id` bigint NOT NULL,
    PRIMARY KEY (`cart_id`),
    UNIQUE KEY `cart_id_UNIQUE` (`cart_id`),
    KEY `carts_users_fk_idx` (`user_id`),
    CONSTRAINT `carts_users_fk` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE `internet_shop`.`orders_products`
(
    `order_id`   bigint NOT NULL,
    `product_id` bigint NOT NULL,
    KEY `orders_orders_fk_idx` (`order_id`),
    KEY `orders_products_fk_idx` (`product_id`),
    CONSTRAINT `orders_orders_fk` FOREIGN KEY (`order_id`) REFERENCES `orders` (`order_id`),
    CONSTRAINT `orders_products_fk` FOREIGN KEY (`product_id`) REFERENCES `products` (`product_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE `internet_shop`.`shopping_carts_products`
(
    `cart_id`    bigint NOT NULL,
    `product_id` bigint NOT NULL,
    KEY `cart_cart_fk_idx` (`cart_id`),
    KEY `cart_product_fk_idx` (`product_id`),
    CONSTRAINT `cart_cart_fk` FOREIGN KEY (`cart_id`) REFERENCES `shopping_carts` (`cart_id`),
    CONSTRAINT `cart_product_fk` FOREIGN KEY (`product_id`) REFERENCES `products` (`product_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE `internet_shop`.`users_roles`
(
    `user_id` bigint NOT NULL,
    `role_id` bigint NOT NULL,
    KEY `users_roles_fk_idx` (`role_id`),
    KEY `users_users_fk_idx` (`user_id`),
    CONSTRAINT `users_roles_fk` FOREIGN KEY (`role_id`) REFERENCES `roles` (`role_id`),
    CONSTRAINT `users_users_fk` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

INSERT INTO internet_shop.roles (role_id, role_name) VALUES (1, "USER");
INSERT INTO internet_shop.roles (role_id, role_name) VALUES (2, "ADMIN");
