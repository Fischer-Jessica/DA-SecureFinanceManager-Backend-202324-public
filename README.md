# Secure Finance Manager

## Description

The Secure Finance Manager is a Java-based backend application designed for managing user finances. It serves as the foundation for the Secure Finance Manager website, iOS app, and Android app. Leveraging the Spring Boot framework, it offers robust functionality. Data storage is handled by a PostgreSQL database, with connectivity facilitated through the Spring JDBC framework.

## Guide

### Branches

The repository consists of four branches:

- `test_program_to_check_the_connection_between_the_nodes`: This branch was created for testing the connection between nodes and is not essential for regular use.
- `database`: Here, you'll find the SQL files necessary for database setup. Execute `database_structure.sql` followed by `database_user_permissions.sql`. Additionally, `database_test_data.sql` can be used to populate the `colours` table. Note that certain values are tied to unique keys, necessitating individual database configurations.
- `secure_finance_manager`: This is the primary branch housing the application's codebase.
- `test_program_for_basic_auth_from_WIH`: This branch contains a test program for basic authentication, which is not integral to the main application.

### Changing Files

Before running the application, several files in the `secure_finance_manager` branch need modification:

1. **`database_user_permissions.sql`**: Update the user's password and username, ensuring consistency across all rows within the file.

2. **`application.properties`**: In the `src/main/resources` directory, modify the first three lines to reflect the database's IP address, username, and password. Ensure consistency with the details in `database_user_permissions.sql`.

3. **`SpringJdbcConfig.java`**: Within `src/main/java/at.htlhl.securefinancemanager.config`, update lines 45 to 47 to match the database's IP address, username, and password. Ensure consistency with the details in `database_user_permissions.sql`.

4. **`SecureFinanceManagerApplication.java`**: Update line 10 with the database's IP address. Additionally, update line 31 to generate a new key using the command `SELECT encode(gen_random_bytes(32), 'hex');` preferably in the `secure_finance_manager` database.

### Running the Application

It is advisable to create a `.jar` file. To do so, use the command `mvn clean install`. When executing this command the database needs to be running. To run this file, use the command `java -jar SecureFinanceManager-version_10.jar`, where the version number can be found in the `pom.xml` file.

Furthermore, when running the application, it is advisable to make regular backups of the database.