<?php
header('Content-Type: application/json');

include 'db_connect.php';

if (isset($connection_error) && $connection_error) {
    echo json_encode([]); // Return empty array on DB error
    exit;
}

// Joining with the users table to get the sender's name
$sql = "SELECT m.id, m.sender_id, m.receiver_id, m.message_text, m.timestamp, u.name as sender_name FROM messages m JOIN users u ON m.sender_id = u.id ORDER BY m.timestamp ASC";

$result = $conn->query($sql);

$messages = [];

if ($result && $result->num_rows > 0) {
  while($row = $result->fetch_assoc()) {
    $messages[] = $row;
  }
}

echo json_encode($messages);

$conn->close();
?>