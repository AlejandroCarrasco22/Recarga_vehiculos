
Para que "docker compose up" funcione, se precisa tener creado un usuario MySQL con user "echo", contraseña "echo" y host "%" 
(esto ultimo para que los contenedores se puedan conectar, pues sus IPs estan en el rango de 127.0.0.1).
Para ello, hemos decidido dejar a continuación un par de comandos que permitan llevar a cabo estar tarea (deben ser
ejecutados habiendo iniciado sesion en mysql con un usuario administrativo o con permisos):

CREATE USER 'echo'@'%' IDENTIFIED BY 'echo';

GRANT ALL PRIVILEGES ON \*.\* TO 'echo'@'%' WITH GRANT OPTION;

FLUSH PRIVILEGES;

exit;

Para que las modificaciones se guarden correctamente, reiniciar el sistema mediante
uno de los siguientes comandos dependiendo del SO:

Linux (Ubuntu): sudo systemctl restart mysql<br />
Linux (Debian): sudo service mysql restart<br />
macOS: sudo brew services restart mysql<br />
Windows: net stop mysql<br />
<t><t> net start mysql
