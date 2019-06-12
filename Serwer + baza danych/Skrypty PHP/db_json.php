<?php
include 'db_connection.php';

$conn = OpenCon();


if ($result = $conn->query("SELECT * FROM sensors_data")) {
    if ($result->num_rows > 0) {
		while($row = $result->fetch_assoc()) {
		    $output[]=$row;
		}
	}
    $result->close();
	print(json_encode($output));
}

CloseCon($conn);
?>