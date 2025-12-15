<?php
header('Content-Type: application/json');

include 'db_connect.php';

if (isset($connection_error) && $connection_error) {
    echo json_encode(['status' => 'error', 'message' => $connection_error]);
    exit;
}

$sender_id = $_POST['sender_id'] ?? null;
$receiver_id = $_POST['receiver_id'] ?? 1; // Default to 1 if not provided
$message_text = $_POST['message_text'] ?? null;

if (empty($sender_id) || empty($receiver_id) || empty($message_text)) {
    echo json_encode(['status' => 'error', 'message' => 'Missing required fields.']);
    exit;
}

$sql = "INSERT INTO messages (sender_id, receiver_id, message_text) VALUES (?, ?, ?)";
$stmt = $conn->prepare($sql);

if ($stmt === false) {
    echo json_encode(['status' => 'error', 'message' => 'Prepare failed: ' . $conn->error]);
    exit;
}

$stmt->bind_param("iis", $sender_id, $receiver_id, $message_text);

if ($stmt->execute()) {
  echo json_encode(['status' => 'success', 'message' => 'Message sent successfully.']);
} else {
  echo json_encode(['status' => 'error', 'message' => 'Execute failed: ' . $stmt->error]);
}

$stmt->close();
$conn->close();
?>