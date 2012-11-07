DROP DATABASE lottgy;
REVOKE ALL PRIVILEGES, GRANT OPTION FROM 'callup'@'localhost';
REVOKE ALL PRIVILEGES, GRANT OPTION FROM 'callup'@'localhost.localdomain';
DROP USER 'callup'@'localhost';
DROP USER 'callup'@'localhost.localdomain';
