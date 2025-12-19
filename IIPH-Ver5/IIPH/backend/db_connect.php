<?php
// This file should NOT set the header. It is only for establishing a connection.

$servername = "127.0.0.1";
$username = "root";
$password = "";
$dbname = "iiph_db";

$conn = new mysqli($servername, $username, $password, $dbname);

$connection_error = null;
if ($conn->connect_error) {
    // If connection fails, set the error message to be checked by other scripts.
    $connection_error = "Connection failed: " . $conn->connect_error;
}
?>