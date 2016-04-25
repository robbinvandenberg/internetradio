<?php

// Process API GET CALLS
if ($_SERVER["REQUEST_METHOD"] == "GET") {
	if(isset($_GET['query'])) {
		switch($_GET['query']) {
			case 'radiostations':
				echo getRadiostations();
			break;
			case 'radiostationscount':
				$key = 'id';
				if(isset($_GET['param'])) {
					$key = $_GET['param'];
				}
				echo getFilteredCount($key);
			break;
			default:
				echo "Foute API call";
		}	
	}
	else {
		echo "Foute API call";
	}
}

/**
 * Connect to the database with configuration from the db.ini file.
 * 
 */
function connectDatabase() {
	require_once('db.class.php');
	return new InternetradioPDO('db.ini');
}


/**
 * Executes an SQL query to return the stations matching the given filters.
 * 
 * "Inclusive" parameter is used to set whether all a station has to conform to both filters or only one
 * 
 * @param map
 * @param inclusive
 * @return ArrayList<Radiostation>
 */
function getRadioStations() {
	$ret = -1;

	try {

		$db = connectDatabase();
		$result = $db->getRadiostations();

		if(count($result) > 0) {
			$ret = json_encode($result);	
		}

	} catch(Exception $e) {
		//error
	}
	
	return $ret;
}

/**
 * Executes a SELECT query on the COUNT of a given filter
 * and returns the count
 * 
 * @param key
 * @return int
 */
function getFilteredCount($key = 'id') {
	$ret = -1;

	try {

		$db = connectDatabase();
		$result = $db->getFilteredCount($key);

		$ret = json_encode($result);

	} catch(Exception $e) {
		//error
		
	}
	
	return $ret;
}

?>