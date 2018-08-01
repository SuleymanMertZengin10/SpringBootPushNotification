package com.notification.controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.notification.model.Grup;
import com.notification.service.GrupService;

@RestController
public class GrupController {
    @Autowired
    GrupService grupService;
	
    
   @PostMapping("/addGrup")
   public ResponseEntity<Grup> addGrup(@RequestBody Map<String,String>addGrup){
	   String grupName=addGrup.get("grupName");
	   Grup grup=new Grup();
	   grup.setGrupName(grupName);
	   grupService.addGrup(grup);
	   return new  ResponseEntity<Grup>(grup, HttpStatus.OK);
	   
   }

   @GetMapping("/getAllGrup")
   public ResponseEntity<?> getAllGrup(){
	  
	   return new ResponseEntity<>(grupService.getAllGrup(),HttpStatus.OK);
	   
   }

   
   @PostMapping("/updateGroupDevices/{grupName}")
   public void updateGrupDevices(@RequestBody Map<String,List<String>> updateGrupDevices,@PathVariable("grupName")String grupName) throws FirebaseMessagingException {
    
	   List<String> devicesTokenList =updateGrupDevices.get("devices");
	   grupService.updateGrupDevices(devicesTokenList, grupName);
	  
   }
    
 
    @GetMapping("/getDevicesByGrupName/{grupName}")
    public ResponseEntity<?> getDevicesByGrupName(@PathVariable("grupName") String grupName){
        Grup grup=grupService.getGrupByGrupName(grupName);
 
    	return new ResponseEntity<>(grup.getDevices(),HttpStatus.OK);
    }

	 @GetMapping("/sign-in")
	 public void  fcmSignin() throws IOException {
		 FileInputStream serviceAccount =
				  new FileInputStream("a.txt");
		 
				FirebaseOptions options = new FirebaseOptions.Builder()
				  .setCredentials(GoogleCredentials.fromStream(serviceAccount))
				  .setDatabaseUrl("https://productapp-22960.firebaseio.com")
				  .build();
				FirebaseApp.initializeApp(options);
	 }
	 
	
	 @PostMapping("/sendMessageToGrup")
	 public void sendMessageToGrup(@RequestBody Map<String,String>message) {
		  String grupName=message.get("grupName");
		  String title=message.get("title");
		  String body=message.get("body");
		  grupService.sendMessageToGroup(grupName,title,body);
	 }
	 
	 

}
