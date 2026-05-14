package corpsync.controlador;
import java.sql.DriverManager; 
import java.sql.Connection;
import java.sql.SQLException;


public class ConexionBBDD {

	public static String url = "jdbc:postgresql://aws-1-eu-central-1.pooler.supabase.com:5432/postgres" ;
	public static String usuario = "postgres.mofuozleqotjlsavxfkv";
	public static String pwd = "b&#i39VTAjeDV3&ity";
	//public static Connection conector = conexion(url,usuario , pwd);

	public static Connection conexion(){
		
		Connection conector = null;
		try{
		conector = DriverManager.getConnection(url,usuario,pwd);
		if (conector == null) { 		System.out.println("　　　No se pudo establecer conexion"); }
		}catch (SQLException e) {
			if (conector == null) { 		System.out.println("　　　No se pudo establecer conexion"); }
			muestraErrorSQL( e );}

		return conector;
		//return c;
	}

	public static void muestraErrorSQL(SQLException e ) {
		// TODO Auto-generated method stub
		System.out.println("SQL ERROR mensaje: " +e.getMessage());
		System.out.println("SQL Estado: " + e.getSQLState());
		System.out.println("SQL codigo especifico: " + e.getErrorCode());
		
	}
	public static void muestraConexionOK() {
		System.out.println("Conexion " + " realizada OK" );
	}
	

	
	
}
