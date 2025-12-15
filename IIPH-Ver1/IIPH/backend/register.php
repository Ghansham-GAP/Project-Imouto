<?php
// A log file to help us debug.
$log_file = __DIR__ . '/debug_log.txt';
file_put_contents($log_file, "--- New Registration Attempt at " . date('Y-m-d H:i:s') . " ---\n");

ob_start();

file_put_contents($log_file, "1. Including db_connect.php...\n", FILE_APPEND);
include 'db_connect.php';

header('Content-Type: application/json');
$response = [];

file_put_contents($log_file, "2. DB Connection Error Var: " . ($connection_error ?? 'Not Set') . "\n", FILE_APPEND);

if (isset($connection_error) && $connection_error) {
    $response = ['status' => 'error', 'message' => $connection_error];
    file_put_contents($log_file, "Error: DB connection failed. Message: $connection_error\n", FILE_APPEND);
} else {
    file_put_contents($log_file, "3. Received POST data: " . json_encode($_POST) . "\n", FILE_APPEND);

    $name = $_POST['name'] ?? null;
    $email = $_POST['email'] ?? null;
    $password = $_POST['pass'] ?? null;
    $course = $_POST['course'] ?? null;
    $year = $_POST['year'] ?? null;
    file_put_contents($log_file, "4. Assigned POST data to variables.\n", FILE_APPEND);

    if (empty($name) || empty($email) || empty($password) || empty($course) || empty($year)) {
        $response = ['status' => 'error', 'message' => 'All fields are required.'];
        file_put_contents($log_file, "Error: Fields were empty.\n", FILE_APPEND);
    } else {
        file_put_contents($log_file, "5. Fields not empty. Hashing password...\n", FILE_APPEND);
        $hashed_password = password_hash($password, PASSWORD_DEFAULT);
        file_put_contents($log_file, "6. Password hashed. Checking connection object...\n", FILE_APPEND);

        // --- EXTREME DEBUGGING CHECK ---
        if (!is_object($conn)) {
            $response = ['status' => 'error', 'message' => 'FATAL: $conn is not an object.'];
            file_put_contents($log_file, "FATAL ERROR: \$conn is not an object. Type is: " . gettype($conn) . "\n", FILE_APPEND);
        } elseif (!method_exists($conn, 'prepare')) {
            $response = ['status' => 'error', 'message' => 'FATAL: method prepare() does not exist on $conn.'];
            file_put_contents($log_file, "FATAL ERROR: method prepare() does not exist on \$conn object.\n", FILE_APPEND);
        } else {
            file_put_contents($log_file, "6a. Connection object and prepare() method are valid. Calling prepare()...\n", FILE_APPEND);
            $sql = "INSERT INTO users (name, email, password, course, current_year) VALUES (?, ?, ?, ?, ?)";
            $stmt = $conn->prepare($sql);

            if ($stmt === false) {
                $response = ['status' => 'error', 'message' => 'Prepare failed: ' . $conn->error];
            } else {
                file_put_contents($log_file, "7. SQL prepared. Binding params...\n", FILE_APPEND);
                $stmt->bind_param("ssssi", $name, $email, $hashed_password, $course, $year);
                file_put_contents($log_file, "8. Params bound. Executing...\n", FILE_APPEND);

                if ($stmt->execute()) {
                    $response = ['status' => 'success', 'message' => 'User registered successfully.'];
                } else {
                    $response = ['status' => 'error', 'message' => 'Execute failed: ' . $stmt->error];
                }
                $stmt->close();
            }
        }
    }
    $conn->close();
}

file_put_contents($log_file, "10. Final response object: " . json_encode($response) . "\n", FILE_APPEND);

ob_end_clean();
echo json_encode($response);
?>