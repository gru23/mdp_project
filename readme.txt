Redoslijed pokretanja:
1. ServiceServer (Tomcat v9.0)
2. ChatServer (/ServiceServer/src/main/java/org/unibl/etf/chats/ChatServer.java)
3a. BookkeepingServer (/SupplierApp/src/org/unibl/etf/rmi/server/BookkeepingServer.java)
3b. SupplierAppMain (/SupplierApp/src/org/unibl/etf/SupplierAppMain.java)
4.Main (/ServiceApp/src/Main.java)
5. Launcher (/ClientApp/src/gui/Launcher.java)

3a i 3b su za pokretanje dobavljaca, neophodno je startovati BookkeepingServer i odma za njim SupplierAppMain!
Launcher-om se porkecu klijenti i moze ih se pokrenuti vise ali najbolje pokrenuti servisersku aplikaciju prije njih, takodje za oboje je neophodan ChatServer.