<!doctype html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Bootstrap demo</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN" crossorigin="anonymous">
  </head>
  <body>
    <div class="Container">
     <h1 class="text-center">Data admin</h1>
     <br><br>
     <a href="pembuatan_forum.php"class="btn btn-success">forum_diskusi</a>
     <a href="laporanidoma.php"class="btn btn-success">laporanidoma</a>
     <a href="mahasiswa.php"class="btn btn-success">mahasiswa</a>
     <a href="dosen.php"class="btn btn-success">dosen</a>
     
  
     <table class="table">
  <thead>
    <tr>
      <th scope="col">ID_ADMIN</th>
      <th scope="col">Nama</th>
      <th scope="col">alamat</th>
      <th scope="col">No_telepon</th>
     
    </tr>
  </thead>
  <tbody>
    <?PHP
    
    include 'koneksi.php';
    $data=mysqli_query($konek,"SELECT * FROM admin");
    while ($d=mysqli_fetch_array($data)) {
    ?>
<tr>
    
    <th> <?php echo $d["ID_ADMIN"]; ?> </th>
    <th> <?php echo $d["Nama"]; ?> </th>
    <th> <?php echo $d["alamat"]; ?></th>
    <th> <?php echo $d["No_telepon"]; ?></th>

</tr>
  </tbody>
  <?php
    }
  ?>
    
</table>
    </div>
 
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-C6RzsynM9kWDrMNeT87bh95OGNyZPhcTNXj1NW7RuBCsyN/o0jlpcV8Qyq46cDfL" crossorigin="anonymous"></script>
  </body>
</html>