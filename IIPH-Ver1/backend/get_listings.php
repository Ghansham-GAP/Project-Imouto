<?php
ob_start(); // Start output buffering

include 'db_connect.php';

header('Content-Type: application/json');
$response = [];

if (isset($connection_error) && $connection_error) {
    $response = []; // Send empty array on DB error
} else {
    $sql = "SELECT id, company_name, role, type, description, apply_link FROM listings";
    $result = $conn->query($sql);
    $listings = [];
    if ($result && $result->num_rows > 0) {
        while($row = $result->fetch_assoc()) {
            $listings[] = $row;
        }
    }
    $response = $listings;
    $conn->close();
}

ob_end_clean(); // Discard any previous output
echo json_encode($response); // Echo ONLY the final JSON response
?>