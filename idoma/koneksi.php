<?php

$host = "localhost";
$username = "root";
$password = "";
$dbname = "idoma";


$konek=mysqli_connect($host,$username,$password,$dbname);

if ($konek){

} else {
    echo "maaf tidak konek";
}
?>