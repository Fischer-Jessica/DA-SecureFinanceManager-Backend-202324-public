# Installation Guide

## Table of contents

- [Preprinting a laptop to use as a server](#preprinting-a-laptop-to-use-as-a-server)
- [Adjusting the configuration files](#adjusting-the-configuration-files)
  - [Backend-repository](#backend-repository)
    - [Branch `database`](#branch-database)
    - [Branch `secure_finance_manager`](#branch-secure_finance_manager)
  - [Frontend-repository](#frontend-repository)
    - [Branch `secure_finance_manager`](#branch-secure_finance_manager-1)
- [Setting up the applications on the server](#setting-up-the-applications-on-the-server)
  - [Database](#database)
  - [Backend](#backend)
  - [Frontend](#frontend)
  - [Webserver for the Frontend](#webserver-for-the-frontend)
- [Router Configuration](#router-configuration)
- [Routine Tasks](#routine-tasks)
  - [Create backups of the database](#create-backups-of-the-database)
  - [Respond to a new backend version](#respond-to-a-new-backend-version)
  - [Respond to a new frontend version](#respond-to-a-new-frontend-version)

## Preprinting a laptop to use as a server

I recommend using Linux, but it is possible to use another operating system to set up the application.

1. **Download a Linux ISO:**
  - If there is not a server available yet, download a Linux ISO file.
  - **Recommended Distribution:** [Download Ubuntu](https://ubuntu.com/download/desktop) (beginner-friendly).
2. **Create a Bootable USB Stick:**
  - Prepare a USB stick with the downloaded ISO file.
  - Follow these instructions to make it bootable: [Create Bootable USB Stick](https://rufus.ie/de/).
3. **Boot from USB:**
  - Access the BIOS of your laptop or computer.
  - Set the boot priority to the USB stick to install Linux on your device.

## Adjusting the configuration files

### [Backend-repository](https://github.com/Fischer-Jessica/DA-SecureFinanceManager-Backend-202324-public)

#### Branch `database`

1. **Open** the file `database_user_permissions.sql`.
2. **Modify:**
  - `line 2`: Set a secure password for the backend-user.
  - optional `line 2`: Change the username of the backend-user.
    - If the username is updated the following lines of this file need to be changed accordingly.

#### Branch `secure_finance_manager`

1. **Open** the file `/src/main/resources/application.properties`
2. **Modify:**
  - `line 1`: Change the placeholder to the IP-address of your database.
  - `line 2`: Change the placeholder to the username of your database-backend-user.
  - `line 3`: Change the placeholder to the password of your database-backend-user.
3. **Open** the file `/src/main/java/at/htlhl/securefinancemanager/config/SpringJdbcConfig.java`
4. **Modify:**
  - `line 45`: Change the placeholder to the IP-address of your database.
  - `line 46`: Change the placeholder to the username of your database-backend-user.
  - `line 47`: Change the placeholder to the password of your database-backend-user.
5. **Open** the file `/src/main/java/at/htlhl/securefinancemanager/SecureFinanceManagerApplication.java`
6. **Modify:**
  - `line 31`: Set the encryption/decryption key as the value of: `DATABASE_ENCRYPTION_DECRYPTION_KEY`.
  - **Key Generation:** To generate this key you need to execute the following command in the bash of the `secure_finance_manager`-database:
    ```Bash
    SELECT encode(gen_random_bytes(32), 'hex');
    ```

### [Frontend-repository](https://github.com/Fischer-Jessica/DA-SecureFinanceManager-Web-202324-public)

#### Branch `secure_finance_manager`

1. **Open** the file `src/app/app.config.ts`
2. **Modify:**
  - `line 16`: Change the placeholder to the IP-address and port of your backend.
  - *Information*: The IP-address and the port of your backend should be the public address and port of the backend.

## Setting up the applications on the server

Firstly, you will need to execute these commands in the terminal:
```Bash
sudo apt update
```

### Database

1. Install PostgreSQL:
    ```Bash
    sudo apt install postgresql
    ```
2. Start the PostgreSQL service:
    ```Bash
    sudo systemctl start postgresql.service
    ``` 
3. Access the PostgreSQL command line:
    ```Bash
    sudo -u postgres psql
    ```
4. You are advised to change the password of the `postgres` user:
    ```Bash
    ALTER USER postgres with password 'YourPassword';
    ```
5. Then you will need to use the files that can be found in the `database`-branch of the backend-repository.
6. Start with the file `database_structure.sql` and execute the commands in the postgresql bash.
7. Insert the `lines 1 to 14` of the `database_test_data.sql` into the database.
8. Insert the modified code of the `database_user_permissions.sql` into the database.

### Backend

1. Ensure a PostgreSQL database with the correct configuration is running at the IP-address specified in both `application.properties` and `SpringJdbcConfig.java`.
2. Create the .jar-file through executing following command:
    ```Bash
    mvn clean install
    ```
3. Install the Java Runtime Environment:
    ```Bash
    sudo apt install openjdk-17-jre
    ```
4. Run the Secure Finance Manager application:
    ```Bash
    java -jar SecureFinanceManager-version_10.jar
    ```

### Frontend

1. Install Node.js:
    ```Bash
    sudo apt install nodejs
    ```
2. Install curl:
    ```Bash
    sudo apt install curl
    ```
3. Install NYM (Node Version Manager):
    ```Bash
    nvm
    ```
4. Install CLI globally:
    ```Bash
    sudo npm install -g @angular/cli
    ```
5. Install the npm packages:
    ```Bash
    npm install
    ```
6. Build the Angular project:
    ```Bash
    ng build
    ```
7. To be able to use the website inside your home-network you need to:
8. Open the file `src/app/app.config.ts`
9. **Modify:**
  - `line 16`: Change the placeholder to the private IP-address and port of the backend.
10. Start the angular-project:
    ```Bash
    ng serve --host privateIpAddress --port 4200
    ```

### Webserver for the Frontend

1. Install Apache:
    ```Bash
    apt install apache
    ```
2. As soon as `ng build` is completed, copy the contents of the `dist` folder:
    ```Bash
    mv -r xxx/SA-SecureFinanceManager-Web-202324-secure_finance_manager/dist/secure_finance_manager/* /var/www/html
    ```

## Router Configuration

1. Configure Port Forwarding:
  1. Access your router's settings and locate the port forwarding section.
  2. Forward the appropriate port (usually port 80 for HTTP) to the internal IP-address of your server running the Angular app.
2. Set Up a Free Domain (Optional):
  1. If you want a custom domain, consider registering a free domain with [No-Ip](https://www.noip.com/de-DE).
  2. Follow the instructions on their website to set up your domain.
3. Configure DDNS on Your Router:
  1. Add your No-Ip domain to the Dynamic DNS (DDNS) settings on your router.
  2. This configuration ensures that if your router's IP-address changes, the domain will still point to your server's current IP-address.

## Routine Tasks

### Create backups of the database

1. Run this command:
    ```Bash
   pg_dump -U postgres -d secure_finance_manager -F c -f /xxx/tt-mm-jjjj___hh-mm.dump
    ```

### Respond to a new backend version

1. Fetch the newest version from the repository.
2. Ensure a PostgreSQL database with the correct configuration is running at the IP-address specified in both `application.properties` and `SpringJdbcConfig.java`.
3. Create the .jar-file through executing following command:
    ```Bash
    mvn clean install
    ```
4. Run the Secure Finance Manager application:
    ```Bash
    java -jar SecureFinanceManager-version_10.jar
    ```

### Respond to a new frontend version

1. Fetch the newest version from the repository and execute the following command:
    ```Bash
    npm install
    ```
2. Open the file `src/app/app.config.ts`
3. **Modify:**
  - `line 16`: Change the placeholder to the public IP-address and port of the backend.
4. Build the Angular project:
    ```Bash
    ng build
    ```
5. Remove the old version of the website:
    ```Bash
   rm -r /var/www/html/*
    ```
6. As soon as `ng build` is completed, copy the contents of the `dist` folder:
    ```Bash
    mv -r xxx/SA-SecureFinanceManager-Web-202324-secure_finance_manager/dist/secure_finance_manager/* /var/www/html
    ```
7. Open the file `src/app/app.config.ts`
8. **Modify:**
  - `line 16`: Change the placeholder to the private IP-address and port of the backend.
9. Start the angular-project:
    ```Bash
    ng serve --host privateIpAddress --port 4200
    ```
