<?php
ob_start(); // Start output buffering

include 'db_connect.php';

header('Content-Type: application/json');
$response = [];

if (isset($connection_error) && $connection_error) {
    $response = ['status' => 'error', 'message' => $connection_error];
} else {
    $email = $_POST['email'] ?? null;
    $password = $_POST['pass'] ?? null;

    if (empty($email) || empty($password)) {
        $response = ['status' => 'error', 'message' => 'Email and password are required.'];
    } else {
        $sql = "SELECT id, name, email, course, current_year, password FROM users WHERE email = ?";
        $stmt = $conn->prepare($sql);

        if ($stmt === false) {
            $response = ['status' => 'error', 'message' => 'Prepare failed: ' . $conn->error];
        } else {
            $stmt->bind_param("s", $email);
            $stmt->execute();
            $result = $stmt->get_result();

            if ($result->num_rows > 0) {
                $user = $result->fetch_assoc();
                if (password_verify($password, $user['password'])) {
                    unset($user['password']);
                    $response = ['status' => 'success', 'user' => $user];
                } else {
                    $response = ['status' => 'error', 'message' => 'Invalid credentials.'];
                }
            } else {
                $response = ['status' => 'error', 'message' => 'User not found.'];
            }
            $stmt->close();
        }
    }
    $conn->close();
}

ob_end_clean(); // Discard any previous output (warnings, etc.)
echo json_encode($response); // Echo ONLY the final JSON response
?>