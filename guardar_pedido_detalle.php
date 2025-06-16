<?php
include("conexion.php");

if (isset($_POST["id_pedido"]) && isset($_POST["detalle"])) {
    $id_pedido = $_POST["id_pedido"];
    $detalle = json_decode($_POST["detalle"], true);

    $stmt = $conexion->prepare("INSERT INTO detalle_pedido (id_pedido, id_plato, cantidad, subtotal) VALUES (?, ?, ?, ?)");

    foreach ($detalle as $item) {
        $id_plato = $item["id_plato"];
        $cantidad = $item["cantidad"];
        $subtotal = $item["subtotal"];
        $stmt->bind_param("isid", $id_pedido, $id_plato, $cantidad, $subtotal);
        $stmt->execute();
    }

    echo json_encode(["success" => true]);
    $stmt->close();
} else {
    echo json_encode(["success" => false, "message" => "Datos incompletos"]);
}

$conexion->close();
?>
