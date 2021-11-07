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

CREATE TABLE "address" (
                             "id" SERIAL PRIMARY KEY,
                             "zipcode" VARCHAR(255),
                             "cityId" int,
                             "address1" VARCHAR(255),
                             "address2" VARCHAR(255)
);

CREATE TABLE "city" (
                          "id" int PRIMARY KEY,
                          "name" VARCHAR(30),
                          "state" VARCHAR(30),
                          "country" VARCHAR(2)
);

CREATE TABLE "serviceCategory" (
                                   "id" SERIAL PRIMARY KEY,
                                   "name" VARCHAR(30),
                                   "description" VARCHAR(255)
);

CREATE TABLE "office" (
                          "id" SERIAL PRIMARY KEY,
                          "addressId" int
);

CREATE TABLE "category" (
                            "id" SERIAL PRIMARY KEY,
                            "name" VARCHAR(255),
                            "description" VARCHAR(255)
);

CREATE TABLE "service" (
                           "id" SERIAL PRIMARY KEY,
                           "serviceCategoryId" int,
                           "name" VARCHAR(255),
                           "duration" interval
);

CREATE TABLE "serviceProperties" (
    "bookingId" int,
    "serviceId" int
);

CREATE TABLE "categoryDetails" (
    "officeId" int,
    "categoryId" int
);

CREATE TABLE "pointOfServices" (
                                   "id" SERIAL PRIMARY KEY,
                                   "name" VARCHAR(255),
                                   "bossUserId" int,
                                   "addressId" int
);

CREATE TABLE "master_pos" (
                              "masterUserId" int,
                              "pointOfServicesId" int
);

CREATE TABLE "master_serviceCategory" (
                                          "masterUserId" int,
                                          "serviceCategoryId" int
);

CREATE TABLE "booking" (
                           "id" SERIAL PRIMARY KEY,
                           "userId" int,
                           "masterId" int,
                           "pointOfServicesId" int,
                           "dt" timestamp,
                           "serviceId" int
);

ALTER TABLE "users_roles" ADD FOREIGN KEY ("user_id") REFERENCES "users" ("id");

ALTER TABLE "users_roles" ADD FOREIGN KEY ("role_id") REFERENCES "roles" ("id");

ALTER TABLE "address" ADD FOREIGN KEY ("cityId") REFERENCES "city" ("id");

ALTER TABLE "office" ADD FOREIGN KEY ("addressId") REFERENCES "address" ("id");

ALTER TABLE "service" ADD FOREIGN KEY ("serviceCategoryId") REFERENCES "serviceCategory" ("id");

ALTER TABLE "pointOfServices" ADD FOREIGN KEY ("bossUserId") REFERENCES "users" ("id");

ALTER TABLE "pointOfServices" ADD FOREIGN KEY ("addressId") REFERENCES "address" ("id");

ALTER TABLE "master_pos" ADD FOREIGN KEY ("masterUserId") REFERENCES "users" ("id");

ALTER TABLE "master_pos" ADD FOREIGN KEY ("pointOfServicesId") REFERENCES "pointOfServices" ("id");

ALTER TABLE "master_serviceCategory" ADD FOREIGN KEY ("masterUserId") REFERENCES "users" ("id");

ALTER TABLE "master_serviceCategory" ADD FOREIGN KEY ("serviceCategoryId") REFERENCES "serviceCategory" ("id");

ALTER TABLE "booking" ADD FOREIGN KEY ("userId") REFERENCES "users" ("id");

ALTER TABLE "booking" ADD FOREIGN KEY ("masterId") REFERENCES "users" ("id");

ALTER TABLE "booking" ADD FOREIGN KEY ("pointOfServicesId") REFERENCES "pointOfServices" ("id");

ALTER TABLE "booking" ADD FOREIGN KEY ("serviceId") REFERENCES "service" ("id");

ALTER TABLE "serviceProperties" ADD FOREIGN KEY ("serviceId") REFERENCES "service" ("id");

ALTER TABLE "serviceProperties" ADD FOREIGN KEY ("bookingId") REFERENCES "booking" ("id");

ALTER TABLE "categoryDetails" ADD FOREIGN KEY ("officeId") REFERENCES "office" ("id");

ALTER TABLE "categoryDetails" ADD FOREIGN KEY ("categoryId") REFERENCES "category" ("id");



