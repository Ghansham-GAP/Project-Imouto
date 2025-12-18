<?php
// This file is for establishing a connection ONLY.
// It should have no whitespace or characters before the <?php tag.

$servername = "localhost";
$username = "root"; // Use your actual username
$password = "";       // Use your actual password
$dbname = "iiph_db";   // Use your actual database name

$conn = new mysqli($servername, $username, $password, $dbname);

$connection_error = null;
if ($conn->connect_error) {
    $connection_error = "Connection failed: " . $conn->connect_error;
}
?>