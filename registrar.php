<?php
header("Content-Type: application/json");
include("conexion.php");

// Validar que se hayan enviado todos los datos requeridos
if (
    isset($_POST["nombre"]) &&
    isset($_POST["apellido"]) &&
    isset($_POST["correo"]) &&
    isset($_POST["fecha_nacimiento"]) &&
    isset($_POST["contrasena"])
) {
    $nombre = $_POST["nombre"];
    $apellido = $_POST["apellido"];
    $correo = $_POST["correo"];
    $fecha_nacimiento = $_POST["fecha_nacimiento"];
    $contrasena = $_POST["contrasena"];

    $sql = "INSERT INTO usuarios (nombre, apellido, correo, fecha_nacimiento, contrasena) 
            VALUES (?, ?, ?, ?, ?)";

    $stmt = $conexion->prepare($sql);
    $stmt->bind_param("sssss", $nombre, $apellido, $correo, $fecha_nacimiento, $contrasena);

    if ($stmt->execute()) {
        echo json_encode(["success" => true, "message" => "Registro exitoso."]);
    } else {
        echo json_encode(["success" => false, "message" => "Error al registrar: " . $stmt->error]);
    }

    $stmt->close();
} else {
    echo json_encode(["success" => false, "message" => "Faltan datos."]);
}

$conexion->close();
?>
