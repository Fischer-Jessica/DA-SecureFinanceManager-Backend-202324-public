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

### Running the Application
Furthermore, when running the application, it is advisable to make regular backups of the database.

[More Information on how to use the application.](https://github.com/Fischer-Jessica/DA-SecureFinanceManager-Backend-202324-public/blob/secure_finance_manager/Installation_Guide.md)