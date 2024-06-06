<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login - Nama Website Anda</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 0;
            display: flex;
            align-items: center;
            justify-content: center;
            height: 100vh;
            background-color: #f4f4f4;
        }

        .login-container {
            background-color: #fff;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
            text-align: center;
        }

        form {
            display: inline-block;
            text-align: left;
        }

        label {
            display: block;
            margin-bottom: 8px;
        }

        input {
            padding: 8px;
            margin-bottom: 16px;
            width: 100%;
        }

        button {
            padding: 10px;
            background-color: #333;
            color: #fff;
            border: none;
            border-radius: 4px;
            cursor: pointer;
        }

        button:hover {
            background-color: #555;
        }

        .error-message {
            color: red;
            margin-top: 10px;
        }
    </style>
</head>
<body>
    <div class="login-container">
        <h2>Login</h2>
        <form action="login.php" method="post">
            <label for="username">Username:</label>
            <input type="text" id="username" name="username" required>

            <label for="password">Password:</label>
            <input type="password" id="password" name="password" required>

            <button type="submit">Login</button>
        </form>

        <?php
        // Simulasi autentikasi sederhana (gantilah dengan logika sesuai kebutuhan)
        $validUsername = 'user';
        $validPassword = 'password';

        if ($_SERVER['REQUEST_METHOD'] === 'POST') {
            $submittedUsername = $_POST['username'];
            $submittedPassword = $_POST['password'];

            if ($submittedUsername === $validUsername && $submittedPassword === $validPassword) {
                // Berhasil login, arahkan ke halaman lain atau lakukan tindakan lain
                header('Location: dashboard.php');
                exit;
            } else {
                // Gagal login, tampilkan pesan kesalahan
                echo '<p class="error-message">Login Gagal. Coba lagi.</p>';
            }
        }
        ?>
    </div>
</body>
</html>
