<?php
include 'db_connection.php';
parse_str($_SERVER['QUERY_STRING'], $queries);

$conn = OpenCon();



if (!empty($queries)) {
    if($queries['light']=="true") {
       if ($result = $conn->query("SELECT timestamp, light, heating, cooling FROM excitation WHERE timestamp = (SELECT MAX(timestamp) FROM excitation)")) {
           if ($result->num_rows > 0) {
		        while($row = $result->fetch_assoc()) {
		            echo "timestamp: " . $row["timestamp"]. " | Light: " . $row["light"]. " | heating: " . $row["heating"]. " | cooling: " . $row["cooling"]. "<br>";
		            $output=$row;
		            $conn->query("INSERT INTO excitation (timestamp, light, heating, cooling) VALUES (SYSDATE(), 1, ".$row['heating'].", ".$row['cooling'].")");
		         }
	        } else {
                
                $conn->query("INSERT INTO excitation (timestamp, light, heating, cooling) VALUES (SYSDATE(), 1, 0, 0)");
              
            }
           
        } 
    } elseif($queries['light']=="false"){
        if ($result = $conn->query("SELECT timestamp, light, heating, cooling FROM excitation WHERE timestamp = (SELECT MAX(timestamp) FROM excitation)")) {
           if ($result->num_rows > 0) {
		        while($row = $result->fetch_assoc()) {
		            echo "timestamp: " . $row["timestamp"]. " | Light: " . $row["light"]. " | heating: " . $row["heating"]. " | cooling: " . $row["cooling"]. "<br>";
		            $output=$row;
		            $conn->query("INSERT INTO excitation (timestamp, light, heating, cooling) VALUES (SYSDATE(), 0, ".$row['heating'].", ".$row['cooling'].")");
		         }
	        } else {
                
                $conn->query("INSERT INTO excitation (timestamp, light, heating, cooling) VALUES (SYSDATE(), 0, 0, 0)");
                
            }
           
        } 
        
    }
     if($queries['cooling']=="true") {
       if ($result = $conn->query("SELECT timestamp, light, heating, cooling FROM excitation WHERE timestamp = (SELECT MAX(timestamp) FROM excitation)")) {
           if ($result->num_rows > 0) {
		        while($row = $result->fetch_assoc()) {
		            echo "timestamp: " . $row["timestamp"]. " | Light: " . $row["light"]. " | heating: " . $row["heating"]. " | cooling: " . $row["cooling"]. "<br>";
		            $output=$row;
		            $conn->query("INSERT INTO excitation (timestamp, light, heating, cooling) VALUES (SYSDATE(), ".$row['light'].", ".$row['heating'].", 1)");
		         }
	        } else {
                
                $conn->query("INSERT INTO excitation (timestamp, light, heating, cooling) VALUES (SYSDATE(), 0, 0, 1)");
               
            }
           
        } 
    } elseif($queries['cooling']=="false"){
        if ($result = $conn->query("SELECT timestamp, light, heating, cooling FROM excitation WHERE timestamp = (SELECT MAX(timestamp) FROM excitation)")) {
           if ($result->num_rows > 0) {
		        while($row = $result->fetch_assoc()) {
		            echo "timestamp: " . $row["timestamp"]. " | Light: " . $row["light"]. " | heating: " . $row["heating"]. " | cooling: " . $row["cooling"]. "<br>";
		            $output=$row;
		            $conn->query("INSERT INTO excitation (timestamp, light, heating, cooling) VALUES (SYSDATE(), ".$row['light'].", ".$row['heating'].", 0)");
		         }
	        } else {
                
                $conn->query("INSERT INTO excitation (timestamp, light, heating, cooling) VALUES (SYSDATE(), 0, 0, 0)");
                
            }
           
        } 
        
    }
     if($queries['heating']=="true") {
       if ($result = $conn->query("SELECT timestamp, light, heating, cooling FROM excitation WHERE timestamp = (SELECT MAX(timestamp) FROM excitation)")) {
           if ($result->num_rows > 0) {
		        while($row = $result->fetch_assoc()) {
		            echo "timestamp: " . $row["timestamp"]. " | Light: " . $row["light"]. " | heating: " . $row["heating"]. " | cooling: " . $row["cooling"]. "<br>";
		            $output=$row;
		            $conn->query("INSERT INTO excitation (timestamp, light, heating, cooling) VALUES (SYSDATE(), ".$row['light'].", 1, ".$row['cooling'].")");
		         }
	        } else {
                
                $conn->query("INSERT INTO excitation (timestamp, light, heating, cooling) VALUES (SYSDATE(), 0, 1, 0)");
                
            }
           
        } 
    } elseif($queries['heating']=="false"){
        if ($result = $conn->query("SELECT timestamp, light, heating, cooling FROM excitation WHERE timestamp = (SELECT MAX(timestamp) FROM excitation)")) {
           if ($result->num_rows > 0) {
		        while($row = $result->fetch_assoc()) {
		            echo "timestamp: " . $row["timestamp"]. " | Light: " . $row["light"]. " | heating: " . $row["heating"]. " | cooling: " . $row["cooling"]. "<br>";
		            $output=$row;
		            $conn->query("INSERT INTO excitation (timestamp, light, heating, cooling) VALUES (SYSDATE(), ".$row['light'].", 0, ".$row['cooling'].")");
		         }
	        } else {
            
                $conn->query("INSERT INTO excitation (timestamp, light, heating, cooling) VALUES (SYSDATE(), 0, 0, 0)");
              
            }
           
        } 
        
    } elseif($queries['auto']=="true"){
                
                $conn->query("INSERT INTO excitation (timestamp, light, heating, cooling) VALUES (SYSDATE(), -1, -1, -1)");
                
    }  elseif($queries['auto']=="false"){
                
                $conn->query("INSERT INTO excitation (timestamp, light, heating, cooling) VALUES (SYSDATE(), 0, 0, 0)");
                
    }
    
}
CloseCon($conn);
?>