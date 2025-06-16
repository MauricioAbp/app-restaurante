<?php
header("Content-Type: application/json");
include("conexion.php");

if (isset($_POST["correo"]) && isset($_POST["contrasena"])) {
    $correo = $_POST["correo"];
    $contrasena = $_POST["contrasena"];

    $sql = "SELECT * FROM usuarios WHERE correo = ? AND contrasena = ?";
    $stmt = $conexion->prepare($sql);
    $stmt->bind_param("ss", $correo, $contrasena);
    $stmt->execute();
    $resultado = $stmt->get_result();

    if ($resultado->num_rows > 0) {
        echo json_encode(["success" => true, "message" => "Inicio de sesión exitoso"]);
    } else {
        echo json_encode(["success" => false, "message" => "Correo o contraseña incorrectos"]);
    }

    $stmt->close();
} else {
    echo json_encode(["success" => false, "message" => "Faltan datos"]);
}

$conexion->close();
?>
