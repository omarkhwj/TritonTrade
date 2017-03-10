<?php
require 'PHPMailerAutoload.php';

$validation = filter_input(INPUT_GET,"validation",FILTER_SANITIZE_STRING);
$email = filter_input(INPUT_GET,"email",FILTER_SANITIZE_STRING);

$mail = new PHPMailer;

$mail->isSMTP();

//Enable SMTP debugging
// 0 = off (for production use)
// 1 = client messages
// 2 = client and server messages
$mail->SMTPDebug = 0;
//Ask for HTML-friendly debug output
$mail->Debugoutput = 'html';

//Set the hostname of the mail server
$mail->Host = 'smtp.gmail.com';
//Set the SMTP port number - 587 for authenticated TLS, a.k.a. RFC4409 SMTP submission
$mail->Port = 587;
//Set the encryption system to use
$mail->SMTPSecure = 'tls';
//Whether to use SMTP authentication
$mail->SMTPAuth = true;
//Username to use for SMTP authentication - use full email address for gmail
$mail->Username = "fspituls@eng.ucsd.edu";
//Password to use for SMTP authentication
$mail->Password = "Fjamess1";
//Set who the message is to be sent from
$mail->setFrom('fspituls@eng.ucsd.edu', 'Triton Trade Email Validation');
//Set who the message is to be sent to
$mail->addAddress($email);

//Set the email contents line
$mail->Subject = 'Triton Trade Email Validation';
$mail->msgHTML("<html><a href=https://spitulski.no-ip.biz/db/emailValidation.php?validation=" . $validation . ">Click to validate your email.</a></html>");
$mail->AltBody = "Go to https://spitulski.no-ip.biz/db/emailValidation.php?validation=" . $validation . " to validate your email.";

//send the message, check for errors
if (!$mail->send()) {
    echo "Mailer Error: " . $mail->ErrorInfo;
} else {
    echo "Message sent!";
}
?>