<?php
/**
    Class to retrieve the radiostations from database
*/
class InternetradioPDO extends PDO
{
    /**
      * Read the ini file to load the config parameters.
      * @param $file The ini file
      */
    public function __construct($file = 'db.ini')
    {
        if (!$settings = parse_ini_file($file, TRUE)) throw new exception('Unable to open ' . $file . '.');
       
        $dns = $settings['database']['driver'] .
        ':host=' . $settings['database']['host'] .
        ((!empty($settings['database']['port'])) ? (';port=' . $settings['database']['port']) : '') .
        ';dbname=' . $settings['database']['schema'];

        $options = array(
    		PDO::MYSQL_ATTR_INIT_COMMAND => 'SET NAMES utf8',
		); 	
       
        parent::__construct($dns, $settings['database']['username'], $settings['database']['password'], $options);
    }

	/**
	 * Executes an SQL query to return the stations matching the given filters.
	 * 
	 * "Inclusive" parameter is used to set whether all a station has to conform to both filters or only one
	 * 
	 * @param map
	 * @param inclusive
	 * @return ArrayList<RadioPlayer.RadioStation>
	 */
    public function getRadiostations() {
    	$res = false;

    	$query = $this->prepare("SELECT * FROM stations");
    	$query->execute();

    	if ($query->rowCount()) {
		    $res = $query->fetchAll(PDO::FETCH_ASSOC);
		}

    	return $res;
    }

	/**
	 * (NOT USED) Executes a SELECT query on the COUNT of a given filter
	 * and returns the count
	 * 
	 * @param key
	 * @return int -1 on error or no result
	 */
    public function getFilteredCount($key) {
    	$res = -1;

        /* Execute a prepared statement by passing an array of values */
        $sql = "SELECT COUNT(DISTINCT $key) AS count FROM stations";
        $query = $this->prepare($sql, array(PDO::ATTR_CURSOR => PDO::CURSOR_FWDONLY));
    	$query->execute();

    	if ($query->rowCount() >= 0) {
		    $res = $query->fetch(PDO::FETCH_ASSOC);
		    $res = (int) $res['count'];
		}

    	return $res;

    }

}

?>