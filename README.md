
# Chorely

### Run instructions

The repository contains two separate projects: the client and the server. To run, first open the /server project in IntelliJ and launch the server by running the main method found in StartProgram.java. You may have to change the project's JDK version to one that exists on your computer. Dependencies should automatically follow, if not you can import the following through maven:

microsoft.sqlserver.mssql.jdbc
org.junit.jupiter:junit-jupiter:5.5.0
sazabi.bcrypt4z.scalaz_2.10

Chorely.bacpac can be used to recreate the MSSQL database locally, or you can request access to the developer-server running on Azure (The dev team will need to provide you with keys and whitelist your IP)

Finally, open the Android Studio project found in the /client directory, then launch the app on your chosen emulation device (min Android 5.0). Note, you may need to change the server's IP address com.mau.chorely.model.ClientNetworkManager if not using the in-built emulator.
