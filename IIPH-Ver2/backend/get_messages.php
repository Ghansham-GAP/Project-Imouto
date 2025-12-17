<?php
ob_start(); // Start output buffering

include 'db_connect.php';

header('Content-Type: application/json');
$response = [];

if (isset($connection_error) && $connection_error) {
    $response = []; // Send empty array on DB error
} else {
    $sql = "SELECT m.id, m.sender_id, m.receiver_id, m.message_text, m.timestamp, u.name as sender_name FROM messages m JOIN users u ON m.sender_id = u.id ORDER BY m.timestamp ASC";
    $result = $conn->query($sql);
    $messages = [];
    if ($result && $result->num_rows > 0) {
        while($row = $result->fetch_assoc()) {
            $messages[] = $row;
        }
    }
    $response = $messages;
    $conn->close();
}

ob_end_clean(); // Discard any previous output
echo json_encode($response); // Echo ONLY the final JSON response
?>