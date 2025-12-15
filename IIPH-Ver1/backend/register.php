<?php
ob_start(); // Start output buffering

include 'db_connect.php';

header('Content-Type: application/json');
$response = [];

if (isset($connection_error) && $connection_error) {
    $response = ['status' => 'error', 'message' => $connection_error];
} else {
    $name = $_POST['name'] ?? null;
    $email = $_POST['email'] ?? null;
    $password = $_POST['pass'] ?? null;
    $course = $_POST['course'] ?? null;
    $year = $_POST['year'] ?? null;

    if (empty($name) || empty($email) || empty($password) || empty($course) || empty($year)) {
        $response = ['status' => 'error', 'message' => 'All fields are required.'];
    } else {
        $hashed_password = password_hash($password, PASSWORD_DEFAULT);
        $sql = "INSERT INTO users (name, email, password, course, current_year) VALUES (?, ?, ?, ?, ?)";
        $stmt = $conn->prepare($sql);

        if ($stmt === false) {
            $response = ['status' => 'error', 'message' => 'Prepare failed: ' . $conn->error];
        } else {
            $stmt->bind_param("ssssi", $name, $email, $hashed_password, $course, $year);
            if ($stmt->execute()) {
                $response = ['status' => 'success', 'message' => 'User registered successfully.'];
            } else {
                $response = ['status' => 'error', 'message' => 'Execute failed: ' . $stmt->error];
            }
            $stmt->close();
        }
    }
    $conn->close();
}

ob_end_clean(); // Discard any previous output (warnings, etc.)
echo json_encode($response); // Echo ONLY the final JSON response
?>