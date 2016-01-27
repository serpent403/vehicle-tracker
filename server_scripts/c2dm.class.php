<?

class c2dm
{
	//dont change!
    private $google_auth_url = 'https://www.google.com/accounts/ClientLogin';

    private $auth = '';

    public function __construct($google_id, $google_pwd)
    {
        $this->authorize($google_id, $google_pwd);
    }

    public function authorize($google_id, $google_pwd)
    {
        $header = array(
          'Content-type: application/x-www-form-urlencoded',
        );
        $post_list = array(
          'accountType' => 'GOOGLE',
          'Email' => $google_id,
          'Passwd' => $google_pwd,
          'source' => 'sample-sample',
          'service' => 'ac2dm',
        );
        $post = http_build_query($post_list, '&');
         
        $ch = curl_init($this->google_auth_url);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
        curl_setopt($ch, CURLOPT_FAILONERROR, 1);
        curl_setopt($ch, CURLOPT_FOLLOWLOCATION, 1);
        curl_setopt($ch, CURLOPT_POST, TRUE);
        curl_setopt($ch, CURLOPT_HTTPHEADER, $header);
        curl_setopt($ch, CURLOPT_POSTFIELDS, $post);
        //curl_setopt($ch, CURLOPT_TIMEOUT, 5);
        $ret = curl_exec($ch);
         
        preg_match('/Auth=(.*)/', $ret, $matches);
        $this->auth = $matches[1];
        echo $this->auth . "<br/>";
        echo "authenticated!<br/>";
    }

    public function send($registration_id, $data)
    {
		//dont change this!
        $url = 'https://android.apis.google.com/c2dm/send';
         
        $header = array(
          'Content-type: application/x-www-form-urlencoded',
          'Authorization: GoogleLogin auth='.$this->auth,
        );

        $post_list = array(
          'registration_id' => $registration_id,
          'collapse_key' => 1,
        );

        foreach ($data as $key => $value){
            $post_list["data.".$key] = $value;
        }

        $post = http_build_query($post_list, '&');
         
        $ch = curl_init($url);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
        curl_setopt($ch, CURLOPT_FAILONERROR, 1);
        curl_setopt($ch, CURLOPT_FOLLOWLOCATION, 1);
        curl_setopt($ch, CURLOPT_POST, TRUE);
        curl_setopt($ch, CURLOPT_HTTPHEADER, $header);
        curl_setopt($ch, CURLOPT_POSTFIELDS, $post);
        //curl_setopt($ch, CURLOPT_TIMEOUT, 5);
        $ret = curl_exec($ch);
        return $ret;
        
        
    }

    
    
}



?>

