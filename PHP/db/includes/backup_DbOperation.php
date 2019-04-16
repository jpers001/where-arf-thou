<?php
 
class DbOperation
{
    //Database connection link
    private $con;
 
    //Class constructor
    function __construct()
    {
        //Getting the DbConnect.php file
        require_once dirname(__FILE__) . '/DbConnect.php';
 
        //Creating a DbConnect object to connect to the database
        $db = new DbConnect();
 
        //Initializing our connection link of this class
        //by calling the method connect of DbConnect class
        $this->con = $db->connect();
    }
	
	/*
	* The create operation
	* When this method is called a new record is created in the database
	*/
	function createReport($owner, $pet_name, $last_seen, $contact, $description, $photo, $location){
		$stmt = $this->con->prepare("INSERT INTO Pets (owner, pet_name, last_seen, contact, description) VALUES (?, ?, ?, ?, ?)");

		$stmt->bind_param("sssss", $owner, $pet_name, $last_seen, $contact, $description);

		if($stmt->execute())

		return true; 

		return false; 


	}

	/*
	* The read operation
	* When this method is called it is returning all the existing record of the database
	*/
	function getReports(){
		$stmt = $this->con->prepare("SELECT owner, pet_name, last_seen, contact, description, photo, location FROM Pets");
		$stmt->execute();
		$stmt->bind_result($owner, $pet_name, $last_seen, $contact, $description, $photo, $location);
		
		$reports = array(); 
		
		while($stmt->fetch()){
			$report  = array();
			$report['owner'] = $owner; 
			$report['pet_name'] = $pet_name; 
			$report['last_seen'] = $last_seen; 
			$report['contact'] = $contact; 
			$report['description'] = $description; 
			$report['photo'] = $photo;
			$report['location'] = $location; 
			
			array_push($reports, $report); 
		}
		
		return $reports; 
	}
	
	/*
	* The update operation
	* When this method is called the record with the given id is updated with the new given values
	*/
	function updateHero($id, $name, $realname, $rating, $teamaffiliation){
		$stmt = $this->con->prepare("UPDATE heroes SET name = ?, realname = ?, rating = ?, teamaffiliation = ? WHERE id = ?");
		$stmt->bind_param("ssisi", $name, $realname, $rating, $teamaffiliation, $id);
		if($stmt->execute())
			return true; 
		return false; 
	}
	
	
	/*
	* The delete operation
	* When this method is called record is deleted for the given id 
	*/
	function deleteHero($id){
		$stmt = $this->con->prepare("DELETE FROM heroes WHERE id = ? ");
		$stmt->bind_param("i", $id);
		if($stmt->execute())
			return true; 
		
		return false; 
	}
}
