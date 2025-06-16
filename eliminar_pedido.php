<?php
include("conexion.php");

if (isset($_POST["nombre"])) {
    $nombre = $_POST["nombre"];

    $sql = "DELETE FROM pedidos WHERE nombre = ?";
    $stmt = $conexion->prepare($sql);
    $stmt->bind_param("s", $nombre);

    if ($stmt->execute()) {
        echo json_encode(["success" => true]);
    } else {
        echo json_encode(["success" => false, "error" => $stmt->error]);
    }

    $stmt->close();
} else {
    echo json_encode(["success" => false, "message" => "Nombre no recibido"]);
}

$conexion->close();
?>
