CREATE TABLE "users" (
                         "id" SERIAL PRIMARY KEY,
                         "username" VARCHAR(255) UNIQUE,
                         "first_name" VARCHAR(255),
                         "last_name" VARCHAR(255),
                         "password" VARCHAR(255),
                         "is_archived" BOOLEAN DEFAULT FALSE,
                         "is_activated" BOOLEAN DEFAULT FALSE,
                         "activation_code" VARCHAR(255),
                         "email" VARCHAR(255) UNIQUE,
                         "phone" VARCHAR(255) UNIQUE
);

CREATE TABLE "roles" (
                         "id" SERIAL PRIMARY KEY,
                         "name" VARCHAR(50) NOT NULL
);

CREATE TABLE "users_roles" (
                               "user_id" BIGINT NOT NULL,
                               "role_id" BIGINT NOT NULL,
                               PRIMARY KEY ("user_id", "role_id")
);

CREATE TABLE "users_points" (
                               "user_id" BIGINT NOT NULL,
                               "point_id" BIGINT NOT NULL,
                               PRIMARY KEY ("user_id", "point_id")
);

CREATE TABLE "masters_services" (
                                "user_id" BIGINT NOT NULL,
                                "service_id" BIGINT NOT NULL,
                                PRIMARY KEY ("user_id", "service_id")
);

CREATE TABLE "address" (
                             "id" SERIAL PRIMARY KEY,
                             "zipcode" VARCHAR(255),
                             "city_id" int,
                             "address1" VARCHAR(255),
                             "address2" VARCHAR(255)
);

CREATE TABLE "city" (
                          "id" int PRIMARY KEY,
                          "name" VARCHAR(30),
                          "state" VARCHAR(30),
                          "country" VARCHAR(2)
);

CREATE TABLE "service_category" (
                                   "id" SERIAL PRIMARY KEY,
                                   "name" VARCHAR(30),
                                   "description" VARCHAR(255)
);

CREATE TABLE "category" (
                            "id" SERIAL PRIMARY KEY,
                            "name" VARCHAR(255),
                            "description" VARCHAR(255)
);

CREATE TABLE "service" (
                           "id" SERIAL PRIMARY KEY,
                           "service_category_id" int,
                           "name" VARCHAR(255),
                           "duration" interval
);

CREATE TABLE "service_properties" (
    "booking_id" int,
    "service_id" int
);

CREATE TABLE "point_of_services" (
                                   "id" SERIAL PRIMARY KEY,
                                   "name" VARCHAR(255),
                                   "boss_user_id" int,
                                   "address_id" int,
                                   "category_id" int
);

CREATE TABLE "master_pos" (
                              "master_user_id" int,
                              "point_of_services_id" int
);

CREATE TABLE "master_service_category" (
                                          "master_user_id" int,
                                          "service_category_id" int
);

CREATE TABLE "booking" (
                           "id" SERIAL PRIMARY KEY,
                           "user_id" int,
                           "master_id" int,
                           "point_of_services_id" int,
                           "dt" timestamp,
                           "service_id" int
);

ALTER TABLE "users_roles" ADD FOREIGN KEY ("user_id") REFERENCES "users" ("id");

ALTER TABLE "users_roles" ADD FOREIGN KEY ("role_id") REFERENCES "roles" ("id");

ALTER TABLE "address" ADD FOREIGN KEY ("city_id") REFERENCES "city" ("id");

ALTER TABLE "service" ADD FOREIGN KEY ("service_category_id") REFERENCES "service_category" ("id");

ALTER TABLE "point_of_services" ADD FOREIGN KEY ("boss_user_id") REFERENCES "users" ("id");

ALTER TABLE "point_of_services" ADD FOREIGN KEY ("address_id") REFERENCES "address" ("id");

ALTER TABLE "point_of_services" ADD FOREIGN KEY ("category_id") REFERENCES "category" ("id");

ALTER TABLE "master_pos" ADD FOREIGN KEY ("master_user_id") REFERENCES "users" ("id");

ALTER TABLE "master_pos" ADD FOREIGN KEY ("point_of_services_id") REFERENCES "point_of_services" ("id");

ALTER TABLE "master_service_category" ADD FOREIGN KEY ("master_user_id") REFERENCES "users" ("id");

ALTER TABLE "master_service_category" ADD FOREIGN KEY ("service_category_id") REFERENCES "service_category" ("id");

ALTER TABLE "booking" ADD FOREIGN KEY ("user_id") REFERENCES "users" ("id");

ALTER TABLE "booking" ADD FOREIGN KEY ("master_id") REFERENCES "users" ("id");

ALTER TABLE "booking" ADD FOREIGN KEY ("point_of_services_id") REFERENCES "point_of_services" ("id");

ALTER TABLE "booking" ADD FOREIGN KEY ("service_id") REFERENCES "service" ("id");

ALTER TABLE "service_properties" ADD FOREIGN KEY ("service_id") REFERENCES "service" ("id");

ALTER TABLE "service_properties" ADD FOREIGN KEY ("booking_id") REFERENCES "booking" ("id");

-- ALTER TABLE "category_details" ADD FOREIGN KEY ("pos_id") REFERENCES "point_of_services" ("id");
--
-- ALTER TABLE "category_details" ADD FOREIGN KEY ("category_id") REFERENCES "category" ("id");



