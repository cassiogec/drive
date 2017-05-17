/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientevideo;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Collections;

/**
 *
 * @author Usuario
 */
public class ClienteVideo
{
    private static JacksonFactory       JSON_FACTORY;
    private static NetHttpTransport     HTTP_TRANSPORT;
    private static java.io.File         DATA_STORE_DIR;
    private static FileDataStoreFactory DATA_STORE_FACTORY; 
    private static final String         APPLICATION_NAME = "Trabalho SD 2017-01";
    
    private static Credential authorize() throws Exception
    {        
        // load client secrets
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(
            JSON_FACTORY,
            new InputStreamReader(ClienteVideo.class.getResourceAsStream("client_secret.json"))
        );
        
        // set up authorization code flow
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY, clientSecrets,
          Collections.singleton(DriveScopes.DRIVE_FILE)).setDataStoreFactory(DATA_STORE_FACTORY).build();
        
        // authorize
        return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("domain");
    }
    
    public static Drive getDriveService() throws IOException, Exception
    {
        return new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, authorize()).setApplicationName(APPLICATION_NAME).build();
    }
    
    public static void main(String[] args) throws GeneralSecurityException, IOException, Exception
    {
        JSON_FACTORY       = JacksonFactory.getDefaultInstance();
        HTTP_TRANSPORT     = GoogleNetHttpTransport.newTrustedTransport();
        DATA_STORE_DIR     = new java.io.File(System.getProperty("user.home"), ".dados");
        DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
        
        // Build a new authorized API client service.
        Drive driveService = getDriveService();
        
        FileList result = driveService.files().list().execute();
        
        System.out.println(result);
        
//        File arqTeste = new File();
//        arqTeste.setName("TESTE");
//        arqTeste.setMimeType("application/vnd.google-apps.drive-sdk");
//
//        // Print the names and IDs for up to 10 files.
//        File arq = driveService.files().create(arqTeste).execute();
//        
//        System.out.println(arq);
    }
}
