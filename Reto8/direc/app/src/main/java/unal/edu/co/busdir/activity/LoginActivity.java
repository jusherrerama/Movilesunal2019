package unal.edu.co.busdir.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import unal.edu.co.busdir.R;
import unal.edu.co.busdir.controller.LoginController;

public class LoginActivity extends AppCompatActivity
{

    private String username;
    private String password;

    private LoginController loginController;

    @Override
    protected void onCreate( Bundle savedInstanceState ){
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_login );

        loginController = new LoginController( this );
    }

    public void login( View view ){
        username = ((EditText) findViewById( R.id.editUsername )).getText( ).toString( );
        password = ((EditText) findViewById( R.id.editPassword )).getText( ).toString( );

        if( !fieldEmpty( ) ){
            if( loginController.validateLogin( username, password ) ){
                startActivity( new Intent( this, EnterpriseActivity.class ).putExtra( "<username>", username ) );
                finish( );
            }else{
                ((EditText) findViewById( R.id.editUsername )).setError( "Error with username and password" );
                ((EditText) findViewById( R.id.editPassword )).setError( "Error with username and password" );
            }
        }else{
            ((EditText) findViewById( R.id.editUsername )).setError( "This field can not be Empty" );
        }
    }

    public void signUpActivity( View view ){
        startActivity( new Intent( this, SignUpActivity.class ) );
    }

    private boolean fieldEmpty( ){
        boolean empty = ( username.trim( ).equals( "" ) ) ? true : false;
        return empty;
    }

}