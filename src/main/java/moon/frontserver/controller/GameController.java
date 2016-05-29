package moon.frontserver.controller;

import com.google.inject.Inject;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import moon.frontserver.connection.ConnectionAdapter;
import moon.frontserver.database.dao.PlayerDao;
import moon.frontserver.database.domain.Player;
import moon.frontserver.security.AuthenticationManager;

import java.io.IOException;

public class GameController {
    @Inject
    PlayerDao playerDao;

    @Inject
    AuthenticationManager auth;

    public void onCommand(ConnectionAdapter source, String command) {
        String[] arguments = command.split(" ");
        if (arguments != null && arguments.length >= 2 && "load".equalsIgnoreCase(arguments[0])) {
            try {
                Player p = playerDao.loadPlayer(Long.parseLong(arguments[1]));
                source.send("Player found: " + p.getName());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if(arguments != null && arguments.length >= 2 && "login".equalsIgnoreCase(arguments[0])){
            String email = "test@test.com";
            String pwd = "123";
            if (email == null || pwd == null) {
                try {
                    source.send("wrong parameters");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                Player player = playerDao.loadPlayerByEmail(email);
                String token = auth.tryLogin(player, pwd);
                try {
                    if (token != null) {
                        source.send("token=" + token);
                        source.send("Login successful. Welcome.");
                    } else {
                        source.send("wrong email/pwd or an error...");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else if(arguments != null && arguments.length >= 2){
            //expects token as first param
            String token = arguments[0];
            try {
                try{
                    String userName = auth.tokenAuth(token);
                    source.send(String.format("Authorization status - ok. Welcome, %s", userName));

                    //process actions
                    source.send("Received data: " + arguments[1]);
                }catch(ExpiredJwtException e){
                    source.send("Token is expired");
                }catch(Exception e){
                    source.send("Wrong token or some other error...");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                source.send("Error");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
