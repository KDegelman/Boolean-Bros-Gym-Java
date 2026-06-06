# Boolean-Bros
Program to add, remove, read, and update the System database for Boolean Bros cliental list.
## Setup Instructions

### Requirements
- Java JDK
- IntelliJ IDEA
- MySQL Server
- MySQL Workbench
- Android Studio

### Database Setup
1. Open MySQL Workbench.
2. Connect to the local MySQL server.
3. Open `database_setup.sql`.
4. Confirm the `sys.members` table exists.

### Server Setup
1. Copy `.env.example` and rename it to `.env`.
2. Edit `.env` and enter your MySQL username/password.
3. Open the Java server project in IntelliJ.
4. Make sure `mysql-connector-j-9.7.0.jar` is added as a library.
5. Run `Main`.
6. Type `S` to start the server.
7. Confirm the console says:
   `Database has connected. Database name: sys`
   `Server is listening on port 1234`

### Notes
- `.env` is not committed because it contains the local database password.
- `database_setup.sql` recreates the needed database/table structure.
- The server must be running before the Android app can use database features.