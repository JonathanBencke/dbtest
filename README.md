# JAR para teste de conexão com bancos de dados

# Sintaxe:
Bancos de dados suportados dbtypes: postgresql, oracle or sqlserver

`java -jar dbtest.jar <dbtype> <host> <database> <user> <password> <table> <column> <limit>`

#### Examplos de uso:

Oracle: 
`java -jar dbtest.jar oracle 127.0.0.1:1521 test.database.local dev 123456 users name 10`

SQL Server: 
`java -jar dbtest.jar sqlserver 127.0.0.1:1433 dev user 123456 users name 10`

Postgres: 
`java -jar dbtest.jar postgresql 127.0.0.1:5432 dev user 123456 users name 10`
