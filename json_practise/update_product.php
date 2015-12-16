<?php

/*
 * Following code will update a product information
 * A product is identified by product id (pid)
 */

// array for JSON response
$response = array();
if(!empty($_POST)){
    $pid = $_POST['pid'];
    $name = $_POST['name'];
    $price = $_POST['price'];
    $description = $_POST['description'];

    // include db connect class
    require_once __DIR__ . '/db_connect.php';

    // connecting to db
    $db = new DB_CONNECT();

    // mysql update row with matched pid
    $result = mysql_query("UPDATE products SET name = '$name', price = '$price', description = '$description' WHERE pid = $pid");

    // check if row inserted or not
    if ($result) {
        // successfully updated
        $response["success"] = 1;
        $response["message"] = "Product successfully updated.";
        
        // echoing JSON response
        echo json_encode($response);
    }
    else{
        $response["success"] = 0;
    } 
        
    // required field is missing
    if ($response["success"] == 0){
        $response["message"] = "Required field(s) is missing";
    }

    // echoing JSON response
    echo json_encode($response);
}
else{
    $response["success"] = 0;
    $response["message"] = "No product found";

        // echo no users JSON
        echo json_encode($response);
}
?>
<form action="update_product.php" method="post">

Enter the pid:<br />
            <input type="text" name="pid" placeholder="pid" />
            <br /><br />
            <input type="text" name="name" placeholder="name" />
            <br /><br />
            <input type="text" name="price" placeholder="price" />
            <br /><br />
            <input type="text" name="description" placeholder="description" />
            <br /><br />
            <input type="submit" value="update" />
            </form>
