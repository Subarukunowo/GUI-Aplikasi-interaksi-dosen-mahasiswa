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
     <h1 class="text-center">Data DOSEN</h1>
     <br><br>
     <a href="pembuatan_forum.php"class="btn btn-success">forum_diskusi</a>
   
     <table class="table">
  <thead>
    <tr>
      <th scope="col">NIDN</th>
      <th scope="col">NAMA</th>
      <th scope="col">PRODI</th>
      <th scope="col">JURUSAN</th>
      <th scope="col">NO_KONTAK</th>
    </tr>
  </thead>
  <tbody>
    <?PHP
    $no=1;
    include 'koneksi.php';
    $data=mysqli_query($konek,"SELECT * FROM dosen");
    while ($d=mysqli_fetch_array($data)) {
    ?>
<tr>
    <th> <?php echo $d["NIDN"]; ?> </th>
    <th> <?php echo $d["NAMA"]; ?> </th>
    <th> <?php echo $d["PRODI"]; ?></th>
    <th> <?php echo $d["JURUSAN"]; ?></th>
    <th> <?php echo $d["NO_KONTAK"]; ?></th>
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