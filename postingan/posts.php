<!doctype html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Bootstrap demo</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN" crossorigin="anonymous">
  </head>
  <body>
    <div class="container">

    <h1 class = "text-center">postingan</h1>
    <br><br>
    <table class="table">
  <thead>
    <tr>
      <th scope="col">id</th>
      <th scope="col">gambar</th>
      <th scope="col">konten</th>


    </tr>
  </thead>
  <tbody>
    
  <?php
  
  include 'koneksi.php';

  $data = mysqli_query($konek, "SELECT * FROM posts");
  while ($d = mysqli_fetch_array($data)){
    ?>

    <tr>
        <th> <?php echo $d["id"]; ?> </th>
        <th><?php echo $d["gambar"]; ?></th>
        <th><?php echo $d["konten"]; ?></th>
       
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