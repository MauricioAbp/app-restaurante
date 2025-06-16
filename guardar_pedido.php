<?php
include("conexion.php");

if (isset($_POST["nombre"]) && isset($_POST["precio"]) && isset($_POST["descripcion"])) {
    $nombre = $_POST["nombre"];
    $precio = $_POST["precio"];
    $descripcion = $_POST["descripcion"];

    $sql = "INSERT INTO pedidos (nombre, precio, descripcion) VALUES (?, ?, ?)";
    $stmt = $conexion->prepare($sql);
    $stmt->bind_param("sds", $nombre, $precio, $descripcion);

    if ($stmt->execute()) {
        $id_pedido = $conexion->insert_id; // ✅ este es el ID generado automáticamente
        echo json_encode([
            "success" => true,
            "id_pedido" => $id_pedido
        ]);
    } else {
        echo json_encode(["success" => false, "error" => $stmt->error]);
    }

    $stmt->close();
} else {
    echo json_encode(["success" => false, "message" => "Datos incompletos"]);
}

$conexion->close();
?>
