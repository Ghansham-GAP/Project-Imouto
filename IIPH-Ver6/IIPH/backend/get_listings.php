<?php
header('Content-Type: application/json');

include 'db_connect.php';

if (isset($connection_error) && $connection_error) {
    echo json_encode([]); // Return empty array on DB error
    exit;
}

$sql = "SELECT id, company_name, role, type, description, apply_link FROM listings";
$result = $conn->query($sql);

$listings = [];

if ($result && $result->num_rows > 0) {
  while($row = $result->fetch_assoc()) {
    $listings[] = $row;
  }
}

echo json_encode($listings);

$conn->close();
?>