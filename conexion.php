<?php
$host = "localhost";
$usuario = "root";
$contrasena = "";
$base_datos = "giraldos";
$puerto = 3307; // <-- PUERTO CORRECTO DE MariaDB

$conexion = new mysqli($host, $usuario, $contrasena, $base_datos, $puerto);

if ($conexion->connect_error) {
    die("Conexión fallida: " . $conexion->connect_error);
}
?>
