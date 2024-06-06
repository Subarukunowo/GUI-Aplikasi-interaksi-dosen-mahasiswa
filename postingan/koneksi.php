<?php

$host = "localhost";
$username = "root";
$password = "";
$dbname = "postingan";


$konek=mysqli_connect($host,$username,$password,$dbname);

if ($konek){

} else {
    echo "maaf tidak konek";
}
?>