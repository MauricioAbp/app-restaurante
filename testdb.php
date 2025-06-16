<?php
$c = new mysqli("localhost", "root", "", "", 3307);
$r = $c->query("SHOW DATABASES");
while ($d = $r->fetch_assoc()) echo $d['Database']."<br>";
?>
