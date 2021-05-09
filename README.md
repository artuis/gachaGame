# Gacha Game

## A game to roll for collectibles

REST API for rolling collectibles

## Technologies Used

* Java - SE1.8
* Spring Boot Maven Plugin
* Spring Boot Starter - 2.4.5
  - Log4J2
  - Webflux
  - AOP
  - Actuator
  - Test
  - Security
* Spring Security Test
* Slf4J - 1.7.30
* Spring Boot Devtools
* Spring Data Cassandra
* Awaitility
* JJWT - 0.11.1
  - API
  - Impl
  - Jackson
* SonarCloud
* 
* Tech # - version #.0

## Features

List of features ready and TODOs for future development
* Awesome feature 1
* Awesome feature 2
* Awesome feature 3

To-do list:
* Monetization :slightly_smiling_face:

## Getting Started
   
git clone https://github.com/artuis/gachaGame  
(include all environment setup steps)

> Be sure to include BOTH Windows and Unix command  
> Be sure to mention if the commands only work on a specific platform (eg. AWS, GCP)

- All the `code` required to get started
- Images of what it should look like

## Usage

Once the API is running, the following routes will be available to use

## ROUTES

### ROUTES AVAILABLE TO ANY USER

```/register```

PUT request that takes in a username and generates a [Gamer](src/main/java/com/group3/beans/Gamer.java), defaulting with a random ID, 10 Stardust, and 1000 Strings.

Returns 409 error if username is in use

```/login```

POST request which logs user in, and is now authenticated to access other routes

Returns 404 if username is not found

```/logout```

DELETE request that signs out user

```/gamer/{username}```

GET request to get information about a specific gamer

### ROUTES AVAILABE TO VERIFIED ACTUAL GAMERS

```/gamers/collectibles/roll```

POST request that rolls for a new collectible using available resources.
Cost: (daily roll -> 1000 strings -> 10 stardust)
Will take in descending order depending on what resources are available.

### ROUTES AVAILABLE TO MODERATORS ONLY

```/gamers```

GET request will retrieve data on all available gamers

PUT request will update any attribute of a gamer including roles. Please include all fields show in [Gamer](src/main/java/com/group3/beans/Gamer.java)

```/gamers/{gamerId}```

POST request will ban a gamer for a specified amount of days depending on form input for ```daysBanned```

```/collectibletypes```

POST request will create a new [CollectibleType](src/main/java/com/group3/beans/CollectibleType.java).

PUT request will update an existing CollectibleType. (As with PUT route for ```/gamer``` above, not including all fields will overwrite them with default values)

## Contributors

* [Thomas An](https://github.com/artuis)
* [Stephanie Tallman](https://github.com/sctallman)
* [Antoine Touma](https://github.com/chielo9513)
* [Austin Withers](https://github.com/AustinWithers)
* [Benjamin Wood](https://github.com/lwood-benjamin)

## License

This project uses the following license: [<license_name>](<link>).
