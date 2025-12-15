<?php
ob_start(); // Start output buffering

include 'db_connect.php';

header('Content-Type: application/json');
$response = [];

if (isset($connection_error) && $connection_error) {
    $response = ['status' => 'error', 'message' => $connection_error];
} else {
    $sender_id = $_POST['sender_id'] ?? null;
    $receiver_id = $_POST['receiver_id'] ?? 1;
    $message_text = $_POST['message_text'] ?? null;

    if (empty($sender_id) || empty($receiver_id) || empty($message_text)) {
        $response = ['status' => 'error', 'message' => 'Missing required fields.'];
    } else {
        $sql = "INSERT INTO messages (sender_id, receiver_id, message_text) VALUES (?, ?, ?)";
        $stmt = $conn->prepare($sql);
        if ($stmt === false) {
            $response = ['status' => 'error', 'message' => 'Prepare failed: ' . $conn->error];
        } else {
            $stmt->bind_param("iis", $sender_id, $receiver_id, $message_text);
            if ($stmt->execute()) {
                $response = ['status' => 'success', 'message' => 'Message sent successfully.'];
            } else {
                $response = ['status' => 'error', 'message' => 'Execute failed: ' . $stmt->error];
            }
            $stmt->close();
        }
    }
    $conn->close();
}

ob_end_clean(); // Discard any previous output
echo json_encode($response); // Echo ONLY the final JSON response
?>