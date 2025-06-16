<?php
// Habilitar CORS si pruebas desde un emulador
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json");

// Verificar que se recibió una petición POST con los datos necesarios
if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    if (isset($_POST['latitud']) && isset($_POST['longitud'])) {
        $latitud = $_POST['latitud'];
        $longitud = $_POST['longitud'];

        // Conexión a la base de datos
        $conexion = new mysqli("localhost", "root", "", "giraldos");

        if ($conexion->connect_error) {
            echo json_encode(["estado" => "error", "mensaje" => "Error de conexión"]);
            exit();
        }

        // Insertar datos en la tabla ubicaciones
        $stmt = $conexion->prepare("INSERT INTO ubicaciones (latitud, longitud) VALUES (?, ?)");
        $stmt->bind_param("dd", $latitud, $longitud);

        if ($stmt->execute()) {
            echo json_encode(["estado" => "ok", "mensaje" => "Ubicación guardada"]);
        } else {
            echo json_encode(["estado" => "error", "mensaje" => "No se pudo guardar"]);
        }

        $stmt->close();
        $conexion->close();
    } else {
        echo json_encode(["estado" => "error", "mensaje" => "Faltan datos"]);
    }
} else {
    echo json_encode(["estado" => "error", "mensaje" => "Método no permitido"]);
}
?>
