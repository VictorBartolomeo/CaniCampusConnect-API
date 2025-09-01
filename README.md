# CaniCampusConnect-API

API back-end de Cani Campus Connect, une plateforme destinée aux clubs canins, coachs et propriétaires pour gérer les chiens, les cours, les inscriptions et le suivi sanitaire (vaccinations, traitements), le tout de manière sécurisée et performante.

## Objectif
Digitaliser la gestion d’un club canin et simplifier les échanges entre les acteurs:
- Faciliter l’inscription des propriétaires et de leurs chiens aux cours.
- Centraliser la gestion des clubs, coachs, types de cours et plannings.
- Assurer le suivi sanitaire (vaccins, traitements) des chiens.
- Gérer les fichiers (avatars, logos, documents) et exposer des médias publics.
- Sécuriser l’accès via JWT et gérer les rôles.

## Fonctionnalités clés
- Authentification et autorisation basées sur JWT (Spring Security, JJWT).
- Gestion des entités métier principales: utilisateurs, propriétaires, chiens, races, âges, poids, clubs, coachs, types de cours, cours, inscriptions.
- Modules santé: vaccins, vaccinations, traitements médicamenteux.
- Téléversement de fichiers avec contrôle des types autorisés (images, PDF) et exposition d’assets publics.
- Emails sortants via SMTP (notifications, communication).
- Endpoints publics pour certaines ressources (ex: races) et privés pour l’administration.

## Architecture et conception
- Application Spring Boot monolithique exposant des endpoints REST.
- Architecture en couches: contrôleurs, services, repositories (Spring Data JPA).
- Base de données MySQL 8, ORM Hibernate; initialisation SQL optionnelle.
- Sécurité: Spring Security + JWT.
- CI/CD: GitHub Actions (build Maven, image Docker, push Docker Hub).
- Conteneurisation: Docker (API), Nginx en reverse proxy, service statique pour les médias, génération automatisée de certificats.

## Stack technique
- Java 17
- Spring Boot 3.4.3
  - spring-boot-starter-web, data-jpa, security, validation, mail, test
- Base de données: MySQL 8 (mysql-connector-j)
- Sécurité: JJWT 0.9.1 + Spring Security
- Outils: Lombok, Commons IO
- Build: Maven (spring-boot-maven-plugin, surefire)
- Conteneurs: Docker, docker-compose
- Reverse proxy: Nginx

## Prérequis
- JDK 17
- Maven 3.9+
- MySQL 8 (si exécution locale sans Docker)
- Docker Desktop (si exécution via Docker/docker-compose)

## Configuration
L’application charge les variables d’environnement depuis des fichiers .env:
- application.properties: `spring.config.import=file:./.env[.properties],optional:file:./.local.env[.properties]`
- Exemple de variables (voir `.env.production`):
  - JWT_SECRET: secret de signature JWT
  - DB_HOST, DB_PORT, DB_USER, DB_PASSWORD, DB_NAME: configuration MySQL
  - DDL_AUTO: mode Hibernate (ex: update, validate, none)
  - SQL_FILE_NAME, EXECUTE_SQL: initialisation SQL
  - EMAIL_HOST, EMAIL_PORT, EMAIL_USER, EMAIL_PASSWORD: SMTP
  - PRIVATE_UPLOAD_FOLDER: dossier des uploads monté dans les conteneurs
  - APP_NAME: nom de l’image Docker à publier en CI

Pour le développement local, créez un fichier `.local.env` à la racine et renseignez ces variables selon votre environnement.

## Lancer en local (sans Docker)
1. Créez une base de données MySQL (ex: `CaniCampusConnect`).
2. Créez `.local.env` avec vos paramètres (ex: DB_HOST=localhost, DB_USER=root, DB_PASSWORD=...).
3. Démarrez l’API:
   - Windows PowerShell: `mvn spring-boot:run`
4. L’API est accessible par défaut sur `http://localhost:8080`.

## Lancer avec Docker (API seule)
1. Construire l’image: `docker build -t canicampusconnect-api:local .`
2. Démarrer le conteneur API connecté à une base MySQL accessible et monter le dossier des uploads si nécessaire. Exemple minimal:
   - `docker run -p 8080:8080 --env-file .env.production -v "${PWD}\uploads":/uploads canicampusconnect-api:local`

## Orchestration complète avec docker-compose
Le fichier `docker-compose.yml` orchestre:
- cert-generator: génère les certificats (volumes `certs`).
- proxy: Nginx (ports 80/443) avec reverse proxy vers front, back et service statique.
- front: image `victorbartolomeo/canicampusconnect-view:0.0.9`.
- back: image `victorbartolomeo/canicampusconnect-api:0.0.9`, lit `.env.production`, monte `${PRIVATE_UPLOAD_FOLDER}` sur `/uploads`.
- static: Nginx servant les médias depuis `${PRIVATE_UPLOAD_FOLDER}`.
- db: MySQL 8 persistant (volume `data`).

Démarrage:
- Windows PowerShell: `docker compose up -d`
Assurez-vous d’adapter `.env.production` (mots de passe, domaines, chemin d’uploads) et de libérer les ports 80/443.

## Variables d’environnement principales
- JWT_SECRET: secret de signature JWT
- DB_HOST, DB_PORT, DB_USER, DB_PASSWORD, DB_NAME: configuration MySQL
- DDL_AUTO: `update`, `validate`, etc.
- EXECUTE_SQL: `always`/`never` pour l’initialisation
- EMAIL_HOST/PORT/USER/PASSWORD: SMTP sortant
- PRIVATE_UPLOAD_FOLDER: chemin local des fichiers uploadés
- app.base-url, app.frontend-url: URLs utilisées pour les liens

Types de fichiers upload autorisés: `image/png, image/jpeg, image/jpg, application/pdf`.

## Tests
- Lancer la suite de tests: `mvn test`
- En profil `prod`, les tests peuvent être désactivés via la propriété `woutUnitTests` (voir `pom.xml`).

## CI/CD
- Workflow GitHub Actions: `.github/workflows/main.yml`
  - Build Maven (profil `prod`), injection des secrets, build et push de l’image Docker `${APP_NAME}:${VERSION}` sur Docker Hub.

## Structure du projet (extrait)
```
CaniCampusConnect-API
├─ src
│  ├─ main
│  │  ├─ java/org/example/canicampusconnectapi
│  │  │  └─ controller, service, repository, model
│  │  └─ resources
│  │     ├─ application.properties
│  │     └─ data-cani-campus-connect.sql (optionnel)
├─ Dockerfile
├─ docker-compose.yml
├─ .env.production
└─ nginx-proxy.conf / nginx-static.conf
```

## Sécurité
- Authentification JWT, expiration configurable (`app.security.token.expiration-hours`).
- Limitation envoi d’emails (`app.security.token.max-emails-per-hour`).
- Exposition contrôlée des fichiers via Nginx statique et le répertoire d’uploads.

## Licence
Aucune licence explicite n’est fournie dans ce dépôt. Merci d’ajouter un fichier LICENSE si nécessaire.

## Aide et support
Pour toute question, ouvrez une issue sur le dépôt ou contactez le mainteneur du projet.
